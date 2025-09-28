# eLearning Module Parser

[![Build][build-badge]][build-link]
[![License][license-badge]][license-link]
[![Maven Central][maven-badge]][maven-link]
[![Java 17+][java-badge]][java-link]

A production-ready Java 17 library for parsing and validating SCORM 1.2, SCORM 2004, AICC, and cmi5
modules. The parser normalizes metadata across formats, making it simple to integrate new eLearning
packages into LMS platforms, content pipelines, and QA tooling.

## Highlights

- Auto-detects SCORM 1.2, SCORM 2004 (editions 2-4), AICC, and cmi5 manifests via pluggable
  detectors
- Produces typed metadata (`Scorm2004Metadata`, `Scorm12Metadata`, `AiccMetadata`, `Cmi5Metadata`)
  with consistent accessors for titles, launch URLs, structure, and mastery data
- Uses storage-agnostic `FileAccess` strategies (local directories, ZIP files, AWS S3 SDK v1/v2,
  in-memory payloads, and cached wrappers)
- Emits rich domain exceptions (`ModuleDetectionException`, `ManifestParseException`,
  `ModuleParsingException`) for graceful error handling
- Supports progress and event listeners for observability, plus optional SCORM 2004 schema
  validation
- Backed by extensive unit, integration, and property-based tests with real-world fixtures

## Maven Coordinates

The library is published to Maven Central.

### Maven

```xml

<dependency>
   <groupId>dev.jcputney</groupId>
   <artifactId>elearning-module-parser</artifactId>
   <version>0.0.19</version>
</dependency>
```

### Gradle (Kotlin DSL)

```kotlin
implementation("dev.jcputney:elearning-module-parser:0.0.19")
```

### Optional dependencies

Add one of the AWS SDKs to enable S3-backed storage:

```xml
<!-- Recommended: AWS SDK v2 -->
<dependency>
   <groupId>software.amazon.awssdk</groupId>
   <artifactId>s3</artifactId>
   <version>2.32.5</version>
</dependency>

    <!-- Alternatively: AWS SDK v1 -->
<dependency>
<groupId>com.amazonaws</groupId>
<artifactId>aws-java-sdk-s3</artifactId>
<version>1.12.788</version>
</dependency>
```

## Quick Start

```java
import dev.jcputney.elearning.parser.ModuleParser;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

try(ZipFileAccess access = new ZipFileAccess("path/to/module.zip")){
ModuleParserFactory factory = new DefaultModuleParserFactory(access);
ModuleParser<?> parser = factory.getParser();
ModuleMetadata<?> metadata = parser.parse();

  System.out.

println("Module Type: "+metadata.getModuleType());
    System.out.

println("Title: "+metadata.getTitle());
    System.out.

println("Launch URL: "+metadata.getLaunchUrl());
    }
```

## Supported Formats

- **SCORM 1.2**: detected via `imsmanifest.xml` using the SCORM 1.2 namespace; exposes the
  organization tree, prerequisites, and mastery data.
- **SCORM 2004**: detected via `imsmanifest.xml` with SCORM 2004 namespaces; surfaces edition
  details, sequencing, rollup rules, and delivery controls.
- **AICC**: detected by a `.crs` descriptor containing a `[Course]` section; parses assignable units
  and prerequisite graphs.
- **cmi5**: detected by the presence of `cmi5.xml`; provides AU metadata, move-on criteria, and
  launch parameters.

## Usage

### Parse modules from different storage options

```java
// ZIP archives
try(ZipFileAccess zip = new ZipFileAccess("module.zip")){
ModuleMetadata<?> metadata = new DefaultModuleParserFactory(zip).parseModule();
}

    // Extracted directories
    try(
LocalFileAccess local = new LocalFileAccess("/path/to/folder")){
ModuleMetadata<?> metadata = new DefaultModuleParserFactory(local).parseModule();
}

// AWS S3 (SDK v2)
S3FileAccessV2 s3 = new S3FileAccessV2(s3Client, "bucket", "prefix/");
ModuleMetadata<?> metadata = new DefaultModuleParserFactory(s3).parseModule();
```

Available `FileAccess` implementations include `LocalFileAccess`, `ZipFileAccess`, `S3FileAccessV1`,
`S3FileAccessV2`, `ClasspathFileAccess`, `InMemoryFileAccess`, and `CachedFileAccess`. You can also
implement `FileAccess` for custom backends.

### Work with metadata

```java
ModuleMetadata<?> metadata = factory.parseModule();
System.out.

println(metadata.getTitle());
    System.out.

println(metadata.getDuration());
    System.out.

println(metadata.getSizeOnDisk());

    switch(metadata.

getModuleType()){
    case SCORM_2004 ->{
Scorm2004Metadata scorm = (Scorm2004Metadata) metadata;
    scorm.

getActivityDeliveryControls().

forEach((id, controls) ->{
    // Inspect sequencing and navigation controls
    });
    }
    case AICC ->{
AiccMetadata aicc = (AiccMetadata) metadata;
    System.out.

println(aicc.getPrerequisitesGraph());
    }
    case CMI5 ->{
Cmi5Metadata cmi5 = (Cmi5Metadata) metadata;
    cmi5.

getAuDetails().

forEach(au ->System.out.

println(au.getLaunchMethod()));
    }
    case SCORM_12 ->{
Scorm12Metadata scorm12 = (Scorm12Metadata) metadata;
    System.out.

println(scorm12.getMasteryScores());
    }
    }
```

### Listen for parsing progress

```java
ParsingEventListener listener = new ParsingEventListener() {
   @Override
   public void onDetectionStarted() {
      log.info("Detecting module type");
   }

   @Override
   public void onModuleTypeDetected(ModuleType type) {
      log.info("Detected {}", type);
   }
};

ModuleParserFactory factory =
    new DefaultModuleParserFactory(new CachedFileAccess(new ZipFileAccess("module.zip"), listener));
ModuleParser<?> parser = factory.getParser();
parser.

parse();
```

For long-running transfers, `StreamingProgressListener` surfaces download progress in file-access
implementations that stream remote content (for example `S3FileAccessV2`).

### Extend detection and parsing

Register additional detection plugins or parsers without touching the core pipeline:

```java
ModuleTypeDetector detector = new DefaultModuleTypeDetector(fileAccess);
detector.

registerPlugin(new ModuleTypeDetectorPlugin() {
   @Override public int getPriority () {
      return 100;
   }

   @Override public ModuleType detect (FileAccess access){
      return access.fileExists("custom-manifest.xml") ? ModuleType.CUSTOM : null;
   }
});

DefaultModuleParserFactory factory = new DefaultModuleParserFactory(fileAccess, detector);
factory.

registerParser(ModuleType.CUSTOM, CustomModuleParser::new);
```

## Validation and error handling

- Exceptions are typed to help you distinguish detection, manifest, and parsing failures.
- SCORM 2004 XSD validation is disabled by default. Enable it via the system property
  `elearning.parser.scorm2004.validateXsd=true` or the environment variable
  `ELEARNING_SCORM2004_VALIDATE_XSD=true`.
- Use `CachedFileAccess` to de-duplicate I/O when the same module is parsed repeatedly.

## Development

Use the Maven wrapper to build and test:

```bash
./mvnw clean compile
./mvnw test
./mvnw verify
./mvnw test jacoco:report
```

Guidelines:

- Requires Java 17+
- Follows the repository `.editorconfig` (2-space indentation, 100-character lines)
- Tests use JUnit 5 and jqwik; integration fixtures live under `src/test/resources/modules`
- Pull requests should include updated tests and pass CI

## Acknowledgments

- Advanced Distributed Learning (ADL) for SCORM and cmi5 specifications
- Aviation Industry CBT Committee (AICC) for the AICC standard
- IMS Global Learning Consortium for content packaging guidance

## License

This project is licensed under the GNU Lesser General Public License v3.0. See [LICENSE](LICENSE)
for full details.

[build-badge]: https://img.shields.io/badge/build-GitHub%20Actions-blue

[build-link]: https://github.com/jcputney/elearning-module-parser/actions

[license-badge]: https://img.shields.io/badge/license-LGPL%203.0-blue.svg

[license-link]: https://opensource.org/license/lgpl-3-0

[maven-badge]: https://img.shields.io/maven-central/v/dev.jcputney/elearning-module-parser

[maven-link]: https://search.maven.org/search?q=g:dev.jcputney%20a:elearning-module-parser

[java-badge]: https://img.shields.io/badge/java-17%2B-blue.svg

[java-link]: https://adoptium.net/
