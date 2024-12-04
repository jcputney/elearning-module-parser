package dev.jcputney.elearning.parser.parsers;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.aicc.AiccCourse;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.input.aicc.CourseStructure;
import dev.jcputney.elearning.parser.input.aicc.Descriptor;
import dev.jcputney.elearning.parser.output.aicc.AiccMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Parses AICC (Aviation Industry CBT Committee) eLearning modules by handling both INI-style and
 * CSV-style files with specific extensions such as .crs, .des, .au, .cst, .ort, and .pre.
 */
public class AiccParser extends BaseParser<AiccMetadata, AiccManifest> {

  public static final String CRS_EXTENSION = ".crs";
  public static final String DES_EXTENSION = ".des";
  public static final String AU_EXTENSION = ".au";
  public static final String CST_EXTENSION = ".cst";

  public AiccParser(FileAccess fileAccess) {
    super(fileAccess);
  }

  @Override
  public AiccMetadata parse() throws ModuleParsingException {
    try {
      var aiccManifest = parseManifest();

      String title = aiccManifest.getTitle();
      String launchUrl = aiccManifest.getLaunchUrl();
      if (title == null || title.isEmpty()) {
        throw new ModuleParsingException("AICC module title is empty.");
      }
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException("AICC module launch URL is empty.");
      }

      // Build and return metadata
      return new AiccMetadata(
          aiccManifest,
          ModuleType.AICC,
          checkForXapi()
      );
    } catch (Exception e) {
      throw new ModuleParsingException(
          "Error parsing AICC module at path: " + this.fileAccess.getRootPath(), e);
    }
  }

  public AiccManifest parseManifest() throws IOException, ModuleParsingException {
    AiccCourse aiccCourse = parseIniFile(AiccCourse.class, CRS_EXTENSION);

    // Parse CSV-style course data
    var descriptors = parseCsvFile(Descriptor.class, DES_EXTENSION);
    var assignableUnits = parseCsvFile(AssignableUnit.class, AU_EXTENSION);
    var courseStructure = parseCsvFile(CourseStructure.class, CST_EXTENSION);

    return new AiccManifest(
        aiccCourse,
        assignableUnits,
        descriptors,
        courseStructure
    );
  }

  @Override
  protected Class<AiccManifest> getManifestClass() {
    return AiccManifest.class;
  }

  private <T> List<T> parseCsvFile(Class<T> clazz, String extension)
      throws IOException {
    String fileName = findFileByExtension(extension);
    if (fileName == null) {
      throw new IOException("CSV file with extension " + extension + " not found.");
    }

    try (InputStream inputStream = fileAccess.getFileContents(fileName)) {
      MappingIterator<T> objectMappingIterator = new CsvMapper()
          .readerWithTypedSchemaFor(clazz)
          .with(CsvSchema.emptySchema().withHeader().withColumnSeparator(',').withQuoteChar('"'))
          .readValues(inputStream);
      return new ArrayList<>(objectMappingIterator.readAll());
    }
  }

  private <T> T parseIniFile(Class<T> clazz, String extension)
      throws IOException {
    String fileName = findFileByExtension(extension);
    if (fileName == null) {
      throw new IOException("INI file with extension " + extension + " not found.");
    }

    try (InputStream inputStream = fileAccess.getFileContents(
        fileName); InputStreamReader reader = new InputStreamReader(inputStream)) {
      INIConfiguration iniData = new INIConfiguration();
      iniData.read(reader);
      Map<String, Map<String, String>> mapData = new HashMap<>();
      for (String section : iniData.getSections()) {
        Map<String, String> subSectionMap = new HashMap<>();
        SubnodeConfiguration confSection = iniData.getSection(section);
        Iterator<String> keyIterator = confSection.getKeys();
        while (keyIterator.hasNext()) {
          String key = keyIterator.next();
          String value = confSection.getProperty(key).toString();
          subSectionMap.put(key, value);
        }
        mapData.put(section, subSectionMap);
      }
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.convertValue(mapData, clazz);
    } catch (ConfigurationException e) {
      throw new ManifestParseException("Error parsing INI file: " + fileName);
    }
  }

  private String findFileByExtension(String extension) throws IOException {
    return fileAccess.listFiles("").stream()
        .filter(fileName -> fileName.endsWith(extension))
        .findFirst()
        .orElse(null);
  }
}