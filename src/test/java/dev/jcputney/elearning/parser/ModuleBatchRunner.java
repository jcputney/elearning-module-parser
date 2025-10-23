package dev.jcputney.elearning.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.Styler;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.access.S3FileAccessV2;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.api.ParserOptions;
import dev.jcputney.elearning.parser.input.scorm2004.SequencingUsageDetector.SequencingLevel;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import dev.jcputney.elearning.parser.output.metadata.cmi5.Cmi5Metadata;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.output.metadata.xapi.XapiMetadata;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

/**
 * Standalone runner for parsing one or many eLearning modules from S3 or local storage.
 *
 * <p>Lives in {@code src/test/java} so it is not packaged with the main artifact, but it is not a
 * JUnit test. Invoke with:
 * {@code mvn -q -Dexec.mainClass=dev.jcputney.elearning.parser.tools.ModuleBatchRunner exec:java
 * --
 * <args>}.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class ModuleBatchRunner {

  public static final String LOG_ELAPSED = "Checked {} - Elapsed: {}s {}ms";
  public static final String LOG_ELAPSED_FAILED = "Checked {} - Elapsed: {}s {}ms (failed)";
  private static final int DEFAULT_S3_CONCURRENCY = 4;
  private static final int MAX_S3_CONCURRENCY = 16;
  private static final Pattern UUID_PATTERN = Pattern.compile(
      "(?i)[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
  private static final Pattern VERSION_PATTERN = Pattern.compile("\\d+");
  private static final ObjectMapper OBJECT_MAPPER = createConfiguredObjectMapper();
  private static final String RESET = "\u001B[0m";
  private static final String GREEN = "\u001B[32m";
  private static final String RED = "\u001B[31m";
  private static final String[] HEADERS = {
      "STATUS",
      "SOURCE",
      "LOCATION",
      "UUID",
      "VERSION",
      "TYPE / ERROR",
      "TITLE",
      "DESCRIPTION",
      "DURATION",
      "JSON PARSED",
      "SEQUENCING"
  };
  Logger logger = LoggerFactory.getLogger(ModuleBatchRunner.class);

  private ModuleBatchRunner() {
  }

  private static String ensureTrailingSlash(String path) {
    if (path.endsWith("/")) {
      return path;
    }
    return path + "/";
  }

  private static String extractSegment(String parentPrefixWithSlash,
      String childPrefixWithSlash) {
    String parent = ensureTrailingSlash(parentPrefixWithSlash);
    String child = ensureTrailingSlash(childPrefixWithSlash);
    if (!child.startsWith(parent) || child.length() <= parent.length()) {
      return "";
    }
    return child.substring(parent.length(), child.length() - 1);
  }

  public static void main(String[] args) throws Exception {
    Options options = Options.parse(args);
    ModuleBatchRunner runner = new ModuleBatchRunner();
    runner.execute(options);
  }

  private static ObjectMapper createConfiguredObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  private static String formatSequencingLevel(SequencingLevel level) {
    if (level == null) {
      return "N/A";
    }
    String lower = level
        .name()
        .toLowerCase(Locale.ROOT);
    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
  }

  static String resolveModuleRoot(S3Config config) {
    String basePrefix = ensureTrailingSlash(config.prefix());
    String modulePrefix = config
        .modulePrefix()
        .orElseThrow(() -> new IllegalStateException("Module prefix must be provided"));
    String normalised = stripLeadingSlashes(modulePrefix
        .trim()
        .replace('\\', '/'));
    if (normalised.isEmpty()) {
      throw new IllegalStateException("Module prefix must not be blank");
    }
    normalised = ensureTrailingSlash(normalised);
    if (normalised.startsWith(basePrefix)) {
      return normalised;
    }
    return ensureTrailingSlash(basePrefix + normalised);
  }

  private static String stripLeadingSlashes(String value) {
    int index = 0;
    while (index < value.length() && value.charAt(index) == '/') {
      index++;
    }
    return value.substring(index);
  }

  private S3Client createS3Client(S3Config config) {
    S3ClientBuilder builder = S3Client.builder();

    config
        .region()
        .ifPresent(builder::region);
    config
        .endpointOverride()
        .ifPresent(builder::endpointOverride);
    builder.credentialsProvider(resolveCredentialsProvider());

    if (config
        .endpointOverride()
        .isPresent()) {
      builder.serviceConfiguration(S3Configuration
          .builder()
          .pathStyleAccessEnabled(true)
          .build());
    }

    return builder.build();
  }

  private AwsCredentialsProvider resolveCredentialsProvider() {
    String profile = System.getenv("AWS_PROFILE");
    if (profile != null && !profile
        .trim()
        .isEmpty()) {
      return ProfileCredentialsProvider
          .builder()
          .profileName(profile.trim())
          .build();
    }
    return DefaultCredentialsProvider
        .builder()
        .build();
  }

  private void execute(Options options) throws Exception {
    ResultSink sink = ResultSink.of(options.outputFormat, options.outputPath);
    sink.start();
    Counters counters = new Counters();

    try {
      if (options.mode == Mode.S3) {
        processFromS3(options, sink, counters);
      } else {
        processFromLocal(options, sink, counters);
      }
    } finally {
      sink.finish();
    }

    printSummary(counters);
  }

  private void processFromS3(Options options, ResultSink sink, Counters counters)
      throws IOException, InterruptedException {
    OptionalInt configLimit = options.limit.isPresent()
        ? OptionalInt.of((int) Math.min(options.limit.getAsLong(), Integer.MAX_VALUE))
        : OptionalInt.empty();
    S3Config config = new S3Config(
        options.s3Bucket,
        ensureTrailingSlash(options.s3Prefix),
        options.region,
        options.endpointOverride,
        options.modulePrefix,
        options.concurrency,
        configLimit
    );

    try (S3Client client = createS3Client(config)) {
      if (options.modulePrefix.isPresent()) {
        S3ModuleJob job = buildSingleS3Job(config);
        S3ModuleResources resources = new S3ModuleResources(client, job.bucket);
        try {
          ModuleProcessingResult result = processS3Module(resources, job);
          recordResult(sink, counters, result);
        } finally {
          resources.shutdown();
        }
        return;
      }

      int concurrency = determineS3Concurrency(options);
      ConcurrentLinkedQueue<S3ModuleResources> resourcePool = new ConcurrentLinkedQueue<>();
      ThreadLocal<S3ModuleResources> threadLocalResources = ThreadLocal.withInitial(() -> {
        S3ModuleResources resources = new S3ModuleResources(client, config.bucket());
        resourcePool.add(resources);
        return resources;
      });

      try {
        runParallelProcessing(
            options,
            sink,
            counters,
            concurrency,
            submitter -> enumerateS3Modules(client, config, options.limit, submitter),
            job -> processS3Module(threadLocalResources.get(), (S3ModuleJob) job)
        );
      } finally {
        resourcePool.forEach(S3ModuleResources::shutdown);
      }
    }
  }

  private void processFromLocal(Options options, ResultSink sink, Counters counters)
      throws IOException, InterruptedException {
    if (options.localFile.isPresent()) {
      Path modulePath = options.localFile.get();
      Path baseDir = modulePath.getParent();
      if (baseDir == null) {
        baseDir = modulePath
            .getFileSystem()
            .getPath(".");
      }
      ModuleProcessingResult result = processLocalModule(new LocalModuleJob(baseDir, modulePath));
      recordResult(sink, counters, result);
      return;
    }

    Path baseDirectory = options.localDirectory
        .orElseThrow(() -> new IllegalStateException("--local-dir is required for local mode"));

    int concurrency = options.localConcurrency();
    runParallelProcessing(
        options,
        sink,
        counters,
        concurrency,
        submitter -> enumerateLocalModules(baseDirectory, options.limit, submitter),
        job -> processLocalModule((LocalModuleJob) job)
    );
  }

  private void runParallelProcessing(
      Options options,
      ResultSink sink,
      Counters counters,
      int concurrency,
      JobEnumerator enumerator,
      ModuleProcessor processor
  ) throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(concurrency, new WorkerFactory());
    try {
      CompletionService<ModuleProcessingResult> completionService =
          new ExecutorCompletionService<>(executor);
      Semaphore inFlight = new Semaphore(concurrency);
      AtomicInteger submitted = new AtomicInteger();
      AtomicInteger processed = new AtomicInteger();

      try {
        enumerator.enumerate(job -> {
          acquirePermit(inFlight);
          completionService.submit(() -> {
            try {
              return processor.process(job);
            } finally {
              inFlight.release();
            }
          });
          submitted.incrementAndGet();
          processed.addAndGet(drainCompletedResults(completionService, options, sink, counters));
        });
      } catch (IOException enumerationException) {
        throw new RuntimeException("Failed to enumerate modules", enumerationException);
      }

      int remaining = submitted.get() - processed.get();
      for (int i = 0; i < remaining; i++) {
        Future<ModuleProcessingResult> future = completionService.take();
        handleCompletedFuture(future, options, sink, counters);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      executor.shutdown();
      if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    }
  }

  private void acquirePermit(Semaphore inFlight) {
    try {
      inFlight.acquire();
    } catch (InterruptedException interruptedException) {
      Thread
          .currentThread()
          .interrupt();
      throw new RuntimeException("Interrupted while submitting module job", interruptedException);
    }
  }

  private int drainCompletedResults(CompletionService<ModuleProcessingResult> completionService,
      Options options, ResultSink sink, Counters counters) {
    int processed = 0;
    while (true) {
      Future<ModuleProcessingResult> future = completionService.poll();
      if (future == null) {
        break;
      }
      try {
        handleCompletedFuture(future, options, sink, counters);
        processed++;
      } catch (IOException ioException) {
        throw new RuntimeException("Failed to record module processing result", ioException);
      }
    }
    return processed;
  }

  private void handleCompletedFuture(Future<ModuleProcessingResult> future, Options options,
      ResultSink sink, Counters counters) throws IOException {
    try {
      ModuleProcessingResult result = future.get();
      recordResult(sink, counters, result);
    } catch (InterruptedException interruptedException) {
      Thread
          .currentThread()
          .interrupt();
      throw new RuntimeException("Interrupted while waiting for module processing",
          interruptedException);
    } catch (ExecutionException executionException) {
      Throwable cause = executionException.getCause();
      SourceType sourceType = options.mode == Mode.S3 ? SourceType.S3 : SourceType.LOCAL;
      String errorMessage = cause == null ? executionException.getMessage() : cause.getMessage();
      if (cause instanceof Exception exception) {
        logModuleFailure(sourceType, "-", "-", "-", exception);
      } else {
        logger.error("Module processing task failed (source={})", sourceType,
            executionException);
      }
      ModuleProcessingResult failure = ModuleProcessingResult.failure(
          sourceType,
          "-",
          "-",
          "-",
          errorMessage);
      recordResult(sink, counters, failure);
    }
  }

  private void enumerateS3Modules(S3Client client, S3Config config, OptionalLong limit,
      JobSubmitter submitter) {
    AtomicLong remaining = new AtomicLong(limit.orElse(Long.MAX_VALUE));
    String basePrefix = ensureTrailingSlash(config.prefix());

    streamCommonPrefixes(client, config.bucket(), basePrefix, remaining, learnerPrefix -> {
      if (limit.isPresent() && remaining.get() <= 0) {
        return;
      }

      String uuidSegment = extractSegment(basePrefix, learnerPrefix);
      if (!UUID_PATTERN
          .matcher(uuidSegment)
          .matches()) {
        return;
      }

      streamVersionPrefixes(client, config.bucket(), learnerPrefix, remaining, versionPrefix -> {
        if (limit.isPresent() && remaining.get() <= 0) {
          return;
        }

        String versionSegment = extractSegment(learnerPrefix, versionPrefix);
        if (!VERSION_PATTERN
            .matcher(versionSegment)
            .matches()) {
          return;
        }

        String moduleRoot = ensureTrailingSlash(versionPrefix);
        submitter.submit(new S3ModuleJob(config.bucket(), moduleRoot, uuidSegment, versionSegment));
        if (limit.isPresent()) {
          remaining.decrementAndGet();
        }
      });
    });
  }

  private void streamCommonPrefixes(S3Client client, String bucket, String prefix,
      AtomicLong remaining, PrefixConsumer consumer) {
    if (remaining.get() <= 0) {
      return;
    }

    String continuationToken = null;
    do {
      int maxKeys = computeMaxKeys(remaining);
      ListObjectsV2Request.Builder builder = ListObjectsV2Request
          .builder()
          .bucket(bucket)
          .delimiter("/")
          .prefix(prefix)
          .maxKeys(maxKeys);
      if (continuationToken != null) {
        builder.continuationToken(continuationToken);
      }

      ListObjectsV2Response response = client.listObjectsV2(builder.build());
      for (CommonPrefix commonPrefix : response.commonPrefixes()) {
        consumer.accept(commonPrefix.prefix());
      }

      continuationToken = response.nextContinuationToken();
    } while (continuationToken != null && remaining.get() > 0);
  }

  private void streamVersionPrefixes(S3Client client, String bucket, String learnerPrefix,
      AtomicLong remaining, PrefixConsumer consumer) {
    if (remaining.get() <= 0) {
      return;
    }

    String continuationToken = null;
    do {
      int maxKeys = computeMaxKeys(remaining);
      ListObjectsV2Request.Builder builder = ListObjectsV2Request
          .builder()
          .bucket(bucket)
          .delimiter("/")
          .prefix(ensureTrailingSlash(learnerPrefix))
          .maxKeys(maxKeys);
      if (continuationToken != null) {
        builder.continuationToken(continuationToken);
      }

      ListObjectsV2Response response = client.listObjectsV2(builder.build());
      for (CommonPrefix commonPrefix : response.commonPrefixes()) {
        consumer.accept(commonPrefix.prefix());
      }

      continuationToken = response.nextContinuationToken();
    } while (continuationToken != null && remaining.get() > 0);
  }

  private int computeMaxKeys(AtomicLong remaining) {
    long value = remaining.get();
    if (value <= 0) {
      return 1;
    }
    if (value == Long.MAX_VALUE) {
      return 1000;
    }
    return (int) Math.min(value, 1000);
  }

  private void enumerateLocalModules(Path baseDir, OptionalLong limit, JobSubmitter submitter)
      throws IOException {
    AtomicLong remaining = new AtomicLong(limit.orElse(Long.MAX_VALUE));

    try (Stream<Path> children = Files.list(baseDir)) {
      children
          .sorted()
          .forEach(path -> {
            if (remaining.get() <= 0) {
              return;
            }
            if (Files.isDirectory(path) || path
                .getFileName()
                .toString()
                .toLowerCase(Locale.ROOT)
                .endsWith(".zip")) {
              submitter.submit(new LocalModuleJob(baseDir, path));
              if (limit.isPresent()) {
                remaining.decrementAndGet();
              }
            }
          });
    }
  }

  private S3ModuleJob buildSingleS3Job(S3Config config) {
    String moduleRoot = resolveModuleRoot(config);
    String basePrefix = ensureTrailingSlash(config.prefix());
    if (!moduleRoot.startsWith(basePrefix)) {
      throw new IllegalStateException("Module prefix must start with the configured base prefix");
    }
    String relative = moduleRoot.substring(basePrefix.length());
    if (relative.startsWith("/")) {
      relative = relative.substring(1);
    }
    String[] segments = relative.split("/");
    if (segments.length < 2) {
      throw new IllegalStateException(
          "Module prefix must include learner UUID and version: " + moduleRoot);
    }
    return new S3ModuleJob(config.bucket(), moduleRoot, segments[0], segments[1]);
  }

  private ModuleProcessingResult processS3Module(S3ModuleResources resources, S3ModuleJob job) {
    long startTime = System.currentTimeMillis();
    try {
      ModuleMetadata<?> metadata = resources.parse(job);
      long elapsed = System.currentTimeMillis() - startTime;
      logger.info("S3: " + LOG_ELAPSED, job.modulePrefix, elapsed / 1000, elapsed % 1000);
      return successResult(SourceType.S3, job.modulePrefix, job.uuid, job.version, metadata);
    } catch (ModuleException | IOException exception) {
      long elapsed = System.currentTimeMillis() - startTime;
      logger.error("S3: " + LOG_ELAPSED_FAILED, job.modulePrefix, elapsed / 1000, elapsed % 1000);
      logModuleFailure(SourceType.S3, job.modulePrefix, job.uuid, job.version, exception);
      return ModuleProcessingResult.failure(SourceType.S3, job.modulePrefix, job.uuid, job.version,
          exception.getMessage());
    }
  }

  private ModuleProcessingResult processLocalModule(LocalModuleJob job) {
    long startTime = System.currentTimeMillis();
    try {
      ModuleParserFactory parserFactory = buildLocalFactory(job.modulePath);
      ModuleMetadata<?> metadata = parserFactory.parseModule();
      long elapsed = System.currentTimeMillis() - startTime;
      logger.info("Local: " + LOG_ELAPSED, job.displayPath(), elapsed / 1000, elapsed % 1000);
      return successResult(SourceType.LOCAL, job.displayPath(), "-", "-", metadata);
    } catch (ModuleException | IOException exception) {
      String location = job.displayPath();
      long elapsed = System.currentTimeMillis() - startTime;
      logger.error("Local: " + LOG_ELAPSED_FAILED, location, elapsed / 1000, elapsed % 1000);
      logModuleFailure(SourceType.LOCAL, location, "-", "-", exception);
      return ModuleProcessingResult.failure(SourceType.LOCAL, location, "-", "-",
          exception.getMessage());
    }
  }

  private ModuleParserFactory buildLocalFactory(Path modulePath) throws IOException {
    // Use batch processing options (no file existence validation, no size calculation)
    ParserOptions options = new ParserOptions();

    if (Files.isDirectory(modulePath)) {
      return new DefaultModuleParserFactory(new LocalFileAccess(modulePath.toString()), options);
    }
    return new DefaultModuleParserFactory(new ZipFileAccess(modulePath.toString()), options);
  }

  private ModuleProcessingResult successResult(SourceType source, String location, String uuid,
      String version, ModuleMetadata<?> metadata) throws IOException {
    String json = OBJECT_MAPPER.writeValueAsString(metadata);
    JsonNode node = OBJECT_MAPPER.readTree(json);
    ModuleType moduleType = ModuleType.valueOf(node
        .get("moduleType")
        .asText());

    ModuleMetadata<?> typedMetadata = switch (moduleType) {
      case SCORM_12 -> OBJECT_MAPPER.readValue(json, Scorm12Metadata.class);
      case SCORM_2004 -> OBJECT_MAPPER.readValue(json, Scorm2004Metadata.class);
      case AICC -> OBJECT_MAPPER.readValue(json, AiccMetadata.class);
      case CMI5 -> OBJECT_MAPPER.readValue(json, Cmi5Metadata.class);
      case XAPI -> OBJECT_MAPPER.readValue(json, XapiMetadata.class);
    };

    boolean jsonMatches = metadata.equals(typedMetadata);
    String sequencingInfo = "N/A";
    if (moduleType == ModuleType.SCORM_2004 && metadata instanceof Scorm2004Metadata scorm2004) {
      sequencingInfo = formatSequencingLevel(scorm2004.getSequencingLevel());
    }

    return ModuleProcessingResult.success(
        source,
        location,
        uuid,
        version,
        moduleType.name(),
        metadata.getTitle(),
        metadata.getDescription(),
        metadata.getDuration(),
        jsonMatches,
        sequencingInfo
    );
  }

  private void recordResult(ResultSink sink, Counters counters, ModuleProcessingResult result)
      throws IOException {
    sink.accept(result);
    if (result.success) {
      counters.success.incrementAndGet();
    } else {
      counters.failure.incrementAndGet();
    }
  }

  private void printSummary(Counters counters) {
    System.out.printf("%n%nProcessed %d modules (%d succeeded, %d failed)%n",
        counters.total(),
        counters.success.get(),
        counters.failure.get());
    if (counters.failure.get() > 0) {
      System.exit(1);
    }
  }

  private int determineS3Concurrency(Options options) {
    int requested = options.concurrency.orElse(DEFAULT_S3_CONCURRENCY);
    int capped = Math.min(Math.max(1, requested), MAX_S3_CONCURRENCY);
    if (options.limit.isPresent()) {
      capped = (int) Math.min(capped, options.limit.getAsLong());
    }
    return capped;
  }

  private void logModuleFailure(SourceType sourceType, String location, String uuid, String version,
      Exception exception) {
    String errorMessage = Optional
        .ofNullable(exception.getMessage())
        .orElse(exception
            .getClass()
            .getName());
    System.err.printf(
        "%nFailed to parse module:%n  source: %s%n  location: %s%n  uuid: %s%n  version: %s%n  error: %s%n%n",
        sourceType,
        location,
        uuid,
        version,
        errorMessage);
    logger.error("Failed to parse module (source={}, location={}, uuid={}, version={}): {}",
        sourceType,
        location,
        uuid,
        version,
        errorMessage,
        exception);
  }

  private enum Mode {
    S3,
    LOCAL
  }

  private enum OutputFormat {
    ASCII,
    CSV,
    EXCEL
  }

  private enum SourceType {
    S3,
    LOCAL
  }

  private interface PrefixConsumer {

    void accept(String prefix);
  }

  private interface JobSubmitter {

    void submit(ModuleJob job);
  }

  private interface JobEnumerator {

    void enumerate(JobSubmitter submitter) throws IOException;
  }

  @FunctionalInterface
  private interface ModuleProcessor {

    ModuleProcessingResult process(ModuleJob job) throws Exception;
  }

  private interface ResultSink {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static ResultSink of(OutputFormat format, Optional<Path> outputPath) {
      return switch (format) {
        case ASCII -> new AsciiTableSink();
        case CSV -> new CsvSink(outputPath.orElseThrow(() ->
            new IllegalArgumentException("CSV output requires --output-file")));
        case EXCEL -> new ExcelSink(outputPath.orElseThrow(() ->
            new IllegalArgumentException("Excel output requires --output-file")));
      };
    }

    void start() throws IOException;

    void accept(ModuleProcessingResult result) throws IOException;

    void finish() throws IOException;
  }

  protected record S3Config(String bucket, String prefix,
                            Optional<Region> region, Optional<URI> endpointOverride,
                            Optional<String> modulePrefix, OptionalInt concurrency,
                            OptionalInt limit) {

    public S3Config {
      Objects.requireNonNull(bucket, "bucket");
      Objects.requireNonNull(prefix, "prefix");
      Objects.requireNonNull(region, "region");
      Objects.requireNonNull(endpointOverride, "endpointOverride");
      Objects.requireNonNull(modulePrefix, "modulePrefix");
      Objects.requireNonNull(concurrency, "concurrency");
      Objects.requireNonNull(limit, "limit");
    }
  }

  private abstract static class ModuleJob {

    final SourceType sourceType;

    ModuleJob(SourceType sourceType) {
      this.sourceType = sourceType;
    }
  }

  private static final class S3ModuleJob extends ModuleJob {

    final String bucket;
    final String modulePrefix;
    final String uuid;
    final String version;

    S3ModuleJob(String bucket, String modulePrefix, String uuid, String version) {
      super(SourceType.S3);
      this.bucket = bucket;
      this.modulePrefix = modulePrefix;
      this.uuid = uuid;
      this.version = version;
    }
  }

  private static final class S3ModuleResources {

    private final S3FileAccessV2 fileAccess;

    S3ModuleResources(S3Client client, String bucket) {
      this.fileAccess = new S3FileAccessV2(client, bucket, "", false);
    }

    ModuleMetadata<?> parse(S3ModuleJob job)
        throws ModuleException {
      fileAccess.prepareForModule(job.modulePrefix);
      // Use batch processing options (no file existence validation, no size calculation)
      ParserOptions options = new ParserOptions();
      ModuleParserFactory parserFactory = new DefaultModuleParserFactory(fileAccess, options);
      return parserFactory.parseModule();
    }

    void shutdown() {
      fileAccess.shutdown();
    }
  }

  private static final class LocalModuleJob extends ModuleJob {

    final Path baseDir;
    final Path modulePath;

    LocalModuleJob(Path baseDir, Path modulePath) {
      super(SourceType.LOCAL);
      this.baseDir = baseDir;
      this.modulePath = modulePath;
    }

    String displayPath() {
      try {
        return baseDir
            .relativize(modulePath)
            .toString();
      } catch (IllegalArgumentException ex) {
        return modulePath.toString();
      }
    }
  }

  private static final class WorkerFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setName("module-runner-" + counter.incrementAndGet());
      thread.setDaemon(true);
      return thread;
    }
  }

  private static final class Counters {

    final AtomicInteger success = new AtomicInteger();
    final AtomicInteger failure = new AtomicInteger();

    int total() {
      return success.get() + failure.get();
    }
  }

  private record Options(Mode mode, String s3Bucket, String s3Prefix, Optional<String> modulePrefix,
                         OptionalInt concurrency, OptionalLong limit, Optional<Region> region,
                         Optional<URI> endpointOverride, Optional<Path> outputPath,
                         OutputFormat outputFormat, Optional<Path> localFile,
                         Optional<Path> localDirectory) {

    static Options parse(String[] args) {
      if (args.length == 0) {
        throw new IllegalArgumentException("No arguments provided. Use --mode local|s3 ...");
      }

      Mode mode = null;
      String bucket = null;
      String prefix = null;
      String module = null;
      String region = null;
      String endpoint = null;
      String concurrency = null;
      String limit = null;
      String outputFormat = null;
      String outputFile = null;
      String localFile = null;
      String localDir = null;

      for (int i = 0; i < args.length; i++) {
        String arg = args[i];
        switch (arg) {
          case "--mode" -> mode = Mode.valueOf(readValue(arg, args, ++i).toUpperCase(Locale.ROOT));
          case "--bucket" -> bucket = readValue(arg, args, ++i);
          case "--prefix" -> prefix = readValue(arg, args, ++i);
          case "--module" -> module = readValue(arg, args, ++i);
          case "--region" -> region = readValue(arg, args, ++i);
          case "--endpoint" -> endpoint = readValue(arg, args, ++i);
          case "--concurrency" -> concurrency = readValue(arg, args, ++i);
          case "--limit" -> limit = readValue(arg, args, ++i);
          case "--output" -> outputFormat = readValue(arg, args, ++i);
          case "--output-file" -> outputFile = readValue(arg, args, ++i);
          case "--local-file" -> localFile = readValue(arg, args, ++i);
          case "--local-dir" -> localDir = readValue(arg, args, ++i);
          default -> throw new IllegalArgumentException("Unknown argument: " + arg);
        }
      }

      if (mode == null) {
        throw new IllegalArgumentException("--mode must be provided");
      }

      OutputFormat format = outputFormat == null ? OutputFormat.ASCII
          : OutputFormat.valueOf(outputFormat.toUpperCase(Locale.ROOT));

      Optional<Path> outputPath = Optional
          .ofNullable(outputFile)
          .map(Paths::get);
      if (format != OutputFormat.ASCII && outputPath.isEmpty()) {
        throw new IllegalArgumentException("--output-file is required for CSV and Excel outputs");
      }

      OptionalInt parsedConcurrency = concurrency == null ? OptionalInt.empty()
          : OptionalInt.of(Integer.parseInt(concurrency));
      OptionalLong parsedLimit = limit == null ? OptionalLong.empty()
          : OptionalLong.of(Long.parseLong(limit));
      Optional<String> modulePrefix = Optional
          .ofNullable(module)
          .map(String::trim)
          .filter(StringUtils::isNotBlank);
      Optional<Region> regionOpt = Optional
          .ofNullable(region)
          .filter(StringUtils::isNotBlank)
          .map(value -> {
            try {
              return Region.of(value.toLowerCase(Locale.ROOT));
            } catch (Exception ex) {
              throw new IllegalArgumentException("Invalid AWS region: " + value, ex);
            }
          });
      Optional<URI> endpointOpt = Optional
          .ofNullable(endpoint)
          .filter(StringUtils::isNotBlank)
          .map(URI::create);

      switch (mode) {
        case S3 -> {
          if (bucket == null || prefix == null) {
            throw new IllegalArgumentException("--bucket and --prefix are required for S3 mode");
          }
          return new Options(
              mode,
              bucket,
              prefix,
              modulePrefix,
              parsedConcurrency,
              parsedLimit,
              regionOpt,
              endpointOpt,
              outputPath,
              format,
              Optional.empty(),
              Optional.empty()
          );
        }
        case LOCAL -> {
          Optional<Path> file = Optional
              .ofNullable(localFile)
              .map(Paths::get);
          Optional<Path> dir = Optional
              .ofNullable(localDir)
              .map(Paths::get);
          if (file.isEmpty() && dir.isEmpty()) {
            throw new IllegalArgumentException(
                "Provide either --local-file for a single module or --local-dir for batch processing");
          }
          if (file.isPresent() && dir.isPresent()) {
            throw new IllegalArgumentException("Specify only one of --local-file or --local-dir");
          }
          return new Options(
              mode,
              "",
              "",
              Optional.empty(),
              parsedConcurrency,
              parsedLimit,
              Optional.empty(),
              Optional.empty(),
              outputPath,
              format,
              file,
              dir
          );
        }
        default -> throw new IllegalStateException("Unexpected mode " + mode);
      }
    }

    private static String readValue(String flag, String[] args, int index) {
      if (index >= args.length) {
        return resolveMissingValue(flag);
      }
      String value = args[index];
      if ("--module".equals(flag) && (value.startsWith("--") || StringUtils.isBlank(value))) {
        return promptForModuleValue();
      }
      return value;
    }

    private static String resolveMissingValue(String flag) {
      if ("--module".equals(flag)) {
        return promptForModuleValue();
      }
      throw new IllegalArgumentException("Missing value for " + flag);
    }

    private static String promptForModuleValue() {
      String prompt = "Enter module prefix: ";
      Console console = System.console();
      if (console != null) {
        return validateModuleInput(console.readLine(prompt));
      }
      System.out.print(prompt);
      try {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        return validateModuleInput(reader.readLine());
      } catch (IOException ex) {
        throw new IllegalStateException("Unable to read module prefix from standard input", ex);
      }
    }

    private static String validateModuleInput(String input) {
      if (StringUtils.isBlank(input)) {
        throw new IllegalArgumentException("Module prefix cannot be blank");
      }
      return input.trim();
    }

    int localConcurrency() {
      return concurrency.orElse(Math.max(2, Runtime
          .getRuntime()
          .availableProcessors()));
    }
  }

  private record ModuleProcessingResult(SourceType sourceType, boolean success, String status,
                                        String location, String uuid, String version,
                                        String moduleTypeOrError, String title, String description,
                                        Duration duration, boolean jsonMatches,
                                        String sequencingInfo) {

    static ModuleProcessingResult success(SourceType source, String location, String uuid,
        String version, String moduleType, String title, String description, Duration duration,
        boolean jsonMatches, String sequencingInfo) {
      return new ModuleProcessingResult(source, true, "PASS", location, uuid, version,
          moduleType, title, description, duration, jsonMatches, sequencingInfo);
    }

    static ModuleProcessingResult failure(SourceType source, String location, String uuid,
        String version, String errorMessage) {
      return new ModuleProcessingResult(source, false, "FAIL", location, uuid, version,
          errorMessage, "", "", Duration.ZERO, false, "N/A");
    }
  }

  private static final class AsciiTableSink implements ResultSink {

    private final List<ModuleProcessingResult> rows = new ArrayList<>();

    @Override
    public void start() {
      // no-op
    }

    @Override
    public void accept(ModuleProcessingResult result) {
      rows.add(result);
    }

    @Override
    public void finish() {
      Styler styler = new Styler() {
        @Override
        public List<String> styleCell(Column column, int row, int col, List<String> data) {
          return data
              .stream()
              .map(value -> switch (value.trim()) {
                case "PASS" -> GREEN + value + RESET;
                case "FAIL" -> RED + value + RESET;
                default -> value;
              })
              .toList();
        }
      };

      AsciiTable
          .builder()
          .border(AsciiTable.BASIC_ASCII)
          .styler(styler)
          .data(rows, List.of(
              new Column()
                  .header("STATUS")
                  .with(r -> r.status),
              new Column()
                  .header("SOURCE")
                  .with(r -> r.sourceType.name()),
              new Column()
                  .header("LOCATION")
                  .with(r -> truncate(r.location)),
              new Column()
                  .header("UUID")
                  .with(r -> truncate(r.uuid)),
              new Column()
                  .header("VERSION")
                  .with(r -> r.version),
              new Column()
                  .header("TYPE / ERROR")
                  .with(r -> truncate(r.moduleTypeOrError)),
              new Column()
                  .header("TITLE")
                  .with(r -> truncate(r.title)),
              new Column()
                  .header("DESCRIPTION")
                  .with(r -> truncate(r.description)),
              new Column()
                  .header("DURATION")
                  .with(r -> r.duration.toString()),
              new Column()
                  .header("JSON PARSED")
                  .with(r -> r.jsonMatches ? "YES" : "NO"),
              new Column()
                  .header("SEQUENCING")
                  .with(r -> r.sequencingInfo)
          ))
          .writeTo(System.out);
    }

    private String truncate(String value) {
      if (value == null) {
        return "";
      }
      if (value.length() <= 60) {
        return value;
      }
      return value.substring(0, 57) + "...";
    }
  }

  private static final class CsvSink implements ResultSink {

    private final Path outputPath;
    private BufferedWriter writer;

    CsvSink(Path outputPath) {
      this.outputPath = Objects.requireNonNull(outputPath, "outputPath");
    }

    @Override
    public void start() throws IOException {
      Path parent = outputPath.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8);
      writer.write(String.join(",", HEADERS));
      writer.newLine();
    }

    @Override
    public void accept(ModuleProcessingResult result) throws IOException {
      writer.write(String.join(",",
          escape(result.status),
          escape(result.sourceType.name()),
          escape(result.location),
          escape(result.uuid),
          escape(result.version),
          escape(result.moduleTypeOrError),
          escape(result.title),
          escape(result.description),
          escape(result.duration.toString()),
          escape(result.jsonMatches ? "YES" : "NO"),
          escape(result.sequencingInfo)));
      writer.newLine();
    }

    @Override
    public void finish() throws IOException {
      if (writer != null) {
        writer.flush();
        writer.close();
      }
    }

    private String escape(String value) {
      if (value == null) {
        return "";
      }
      String escaped = value.replace("\"", "\"\"");
      if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\r")) {
        return '"' + escaped + '"';
      }
      return escaped;
    }
  }

  private static final class ExcelSink implements ResultSink {

    private static final int VERSION_COLUMN_INDEX = 4;
    private static final int DESCRIPTION_COLUMN_INDEX = 7;
    private static final int DESCRIPTION_MAX_COLUMN_WIDTH = 12000;

    private final Path outputPath;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int rowIndex;
    private CellStyle defaultCellStyle;
    private CellStyle textColumnStyle;
    private CellStyle wrappingDescriptionStyle;

    ExcelSink(Path outputPath) {
      this.outputPath = Objects.requireNonNull(outputPath, "outputPath");
    }

    @Override
    public void start() throws IOException {
      Path parent = outputPath.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      workbook = new XSSFWorkbook();
      sheet = workbook.createSheet("Results");
      Font baseFont = workbook.createFont();
      baseFont.setFontHeightInPoints((short) 12);
      defaultCellStyle = workbook.createCellStyle();
      defaultCellStyle.setFont(baseFont);

      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.cloneStyleFrom(defaultCellStyle);
      Font headerFont = workbook.createFont();
      headerFont.setFontHeightInPoints((short) 12);
      headerFont.setBold(true);
      headerCellStyle.setFont(headerFont);

      DataFormat dataFormat = workbook.createDataFormat();
      textColumnStyle = workbook.createCellStyle();
      textColumnStyle.cloneStyleFrom(defaultCellStyle);
      textColumnStyle.setDataFormat(dataFormat.getFormat("@"));
      sheet.setDefaultColumnStyle(VERSION_COLUMN_INDEX, textColumnStyle);

      wrappingDescriptionStyle = workbook.createCellStyle();
      wrappingDescriptionStyle.cloneStyleFrom(defaultCellStyle);
      wrappingDescriptionStyle.setWrapText(true);
      wrappingDescriptionStyle.setVerticalAlignment(VerticalAlignment.TOP);
      sheet.setDefaultColumnStyle(DESCRIPTION_COLUMN_INDEX, wrappingDescriptionStyle);
      Row header = sheet.createRow(rowIndex++);
      for (int i = 0; i < HEADERS.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(HEADERS[i]);
        cell.setCellStyle(headerCellStyle);
      }
    }

    @Override
    public void accept(ModuleProcessingResult result) {
      Row row = sheet.createRow(rowIndex++);
      row.setHeight((short) -1);
      Cell statusCell = row.createCell(0);
      statusCell.setCellStyle(defaultCellStyle);
      statusCell.setCellValue(result.status);

      Cell sourceCell = row.createCell(1);
      sourceCell.setCellStyle(defaultCellStyle);
      sourceCell.setCellValue(result.sourceType.name());

      Cell locationCell = row.createCell(2);
      locationCell.setCellStyle(defaultCellStyle);
      locationCell.setCellValue(result.location);

      Cell uuidCell = row.createCell(3);
      uuidCell.setCellStyle(defaultCellStyle);
      uuidCell.setCellValue(result.uuid);

      Cell versionCell = row.createCell(4);
      versionCell.setCellStyle(textColumnStyle);
      versionCell.setCellValue(result.version);

      Cell typeCell = row.createCell(5);
      typeCell.setCellStyle(defaultCellStyle);
      typeCell.setCellValue(result.moduleTypeOrError);

      Cell titleCell = row.createCell(6);
      titleCell.setCellStyle(defaultCellStyle);
      titleCell.setCellValue(result.title);

      Cell descriptionCell = row.createCell(7);
      descriptionCell.setCellStyle(wrappingDescriptionStyle);
      descriptionCell.setCellValue(result.description);

      Cell durationCell = row.createCell(8);
      durationCell.setCellStyle(defaultCellStyle);
      durationCell.setCellValue(result.duration.toString());

      Cell jsonCell = row.createCell(9);
      jsonCell.setCellStyle(defaultCellStyle);
      jsonCell.setCellValue(result.jsonMatches ? "YES" : "NO");

      Cell sequencingCell = row.createCell(10);
      sequencingCell.setCellStyle(defaultCellStyle);
      sequencingCell.setCellValue(result.sequencingInfo);
    }

    @Override
    public void finish() throws IOException {
      try {
        if (sheet != null) {
          createTable();
          autoSizeColumns();
          autoSizeRows();
          applyConditionalFormatting();
          suppressVersionWarnings();
        }
        try (OutputStream outputStream = Files.newOutputStream(outputPath)) {
          workbook.write(outputStream);
        }
      } finally {
        if (workbook != null) {
          workbook.close();
        }
      }
    }

    private void createTable() {
      int lastRowIndex = Math.max(rowIndex - 1, 0);
      AreaReference area = new AreaReference(
          new CellReference(0, 0),
          new CellReference(lastRowIndex, HEADERS.length - 1),
          SpreadsheetVersion.EXCEL2007);
      XSSFTable table = sheet.createTable(area);
      table.setName("ModuleResults");
      table.setDisplayName("ModuleResults");

      CTTableStyleInfo styleInfo = getTableStyleInfo(table);
      styleInfo.setName("TableStyleMedium2");
      styleInfo.setShowColumnStripes(false);
      styleInfo.setShowRowStripes(true);
    }

    private CTTableStyleInfo getTableStyleInfo(XSSFTable table) {
      CTTable ctTable = table.getCTTable();
      CTTableColumns columns = ctTable.getTableColumns();
      if (columns == null) {
        columns = ctTable.addNewTableColumns();
      }
      while (columns.sizeOfTableColumnArray() > 0) {
        columns.removeTableColumn(0);
      }
      columns.setCount(HEADERS.length);
      for (int i = 0; i < HEADERS.length; i++) {
        CTTableColumn column = columns.addNewTableColumn();
        column.setId(i + 1);
        column.setName(HEADERS[i]);
      }

      // Enable auto-filter
      if (!ctTable.isSetAutoFilter()) {
        ctTable.addNewAutoFilter();
      }
      ctTable.getAutoFilter().setRef(table.getArea().formatAsString());

      return ctTable.isSetTableStyleInfo()
          ? ctTable.getTableStyleInfo()
          : ctTable.addNewTableStyleInfo();
    }

    private void autoSizeColumns() {
      for (int i = 0; i < HEADERS.length; i++) {
        sheet.autoSizeColumn(i);
      }
      if (sheet.getColumnWidth(DESCRIPTION_COLUMN_INDEX) > DESCRIPTION_MAX_COLUMN_WIDTH) {
        sheet.setColumnWidth(DESCRIPTION_COLUMN_INDEX, DESCRIPTION_MAX_COLUMN_WIDTH);
      }
    }

    private void autoSizeRows() {
      for (int i = 1; i < rowIndex; i++) {
        Row row = sheet.getRow(i);
        if (row != null) {
          row.setHeight((short) -1);
        }
      }
    }

    private void applyConditionalFormatting() {
      int lastDataRowIndex = rowIndex - 1;
      int firstDataRowIndex = 1; // first row after headers
      if (lastDataRowIndex < firstDataRowIndex) {
        return;
      }
      SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();

      CellRangeAddress[] statusRange = {
          new CellRangeAddress(firstDataRowIndex, lastDataRowIndex, 0, 0)
      };
      int firstExcelRow = firstDataRowIndex + 1;
      String statusColumnLetter = CellReference.convertNumToColString(0);
      String passFormula = "UPPER($" + statusColumnLetter + firstExcelRow + ")=\"PASS\"";
      ConditionalFormattingRule passRule = scf.createConditionalFormattingRule(passFormula);
      PatternFormatting passPattern = passRule.createPatternFormatting();
      passPattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
      passPattern.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
      passPattern.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

      String failFormula = "UPPER($" + statusColumnLetter + firstExcelRow + ")=\"FAIL\"";
      ConditionalFormattingRule failRule = scf.createConditionalFormattingRule(failFormula);
      PatternFormatting failPattern = failRule.createPatternFormatting();
      failPattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
      failPattern.setFillForegroundColor(IndexedColors.ROSE.getIndex());
      failPattern.setFillBackgroundColor(IndexedColors.ROSE.getIndex());

      scf.addConditionalFormatting(statusRange,
          new ConditionalFormattingRule[]{passRule, failRule});

      CellRangeAddress[] sequencingRange = {
          new CellRangeAddress(firstDataRowIndex, lastDataRowIndex, 10, 10)
      };
      String sequencingColumnLetter = CellReference.convertNumToColString(10);
      String sequencingBase = "$" + sequencingColumnLetter + firstExcelRow;
      ConditionalFormattingRule minimalRule = scf.createConditionalFormattingRule(
          sequencingBase + "=\"Minimal\"");
      PatternFormatting minimalPattern = minimalRule.createPatternFormatting();
      minimalPattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
      minimalPattern.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
      minimalPattern.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

      ConditionalFormattingRule multiRule = scf.createConditionalFormattingRule(
          sequencingBase + "=\"Multi\"");
      PatternFormatting multiPattern = multiRule.createPatternFormatting();
      multiPattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
      multiPattern.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
      multiPattern.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.getIndex());

      ConditionalFormattingRule fullRule = scf.createConditionalFormattingRule(
          sequencingBase + "=\"Full\"");
      PatternFormatting fullPattern = fullRule.createPatternFormatting();
      fullPattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
      fullPattern.setFillForegroundColor(IndexedColors.CORAL.getIndex());
      fullPattern.setFillBackgroundColor(IndexedColors.CORAL.getIndex());

      scf.addConditionalFormatting(sequencingRange,
          new ConditionalFormattingRule[]{minimalRule, multiRule, fullRule});
    }

    private void suppressVersionWarnings() {
      if (rowIndex <= 1) {
        return;
      }
      sheet.addIgnoredErrors(new CellRangeAddress(1, rowIndex - 1,
              VERSION_COLUMN_INDEX, VERSION_COLUMN_INDEX),
          IgnoredErrorType.NUMBER_STORED_AS_TEXT);
    }
  }
}
