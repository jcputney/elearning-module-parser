/*
 * Copyright (c) 2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.parsers;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.aicc.AiccCourse;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.input.aicc.CourseStructure;
import dev.jcputney.elearning.parser.input.aicc.Descriptor;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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
public final class AiccParser extends BaseParser<AiccMetadata, AiccManifest> {

  /**
   * The file extension for the AICC course file.
   */
  public static final String CRS_EXTENSION = ".crs";

  /**
   * The file extension for the AICC descriptor file.
   */
  public static final String DES_EXTENSION = ".des";

  /**
   * The file extension for the AICC assignable unit file.
   */
  public static final String AU_EXTENSION = ".au";

  /**
   * The file extension for the AICC course structure file.
   */
  public static final String CST_EXTENSION = ".cst";

  /**
   * The file extension for the AICC objectives relation table file.
   */
  public static final String ORT_EXTENSION = ".ort";

  /**
   * The file extension for the AICC prerequisites file.
   */
  public static final String PRE_EXTENSION = ".pre";

  /**
   * Default constructor for the AiccParser class.
   *
   * @param fileAccess An instance of FileAccess for reading files.
   */
  public AiccParser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Constructs an AiccParser with the specified ModuleFileProvider instance.
   *
   * @param moduleFileProvider An instance of ModuleFileProvider for reading files in the module
   * package.
   */
  public AiccParser(ModuleFileProvider moduleFileProvider) {
    super(moduleFileProvider);
  }

  /**
   * Parses the AICC module and returns its metadata.
   */
  @Override
  public AiccMetadata parse() throws ModuleParsingException {
    try {
      var aiccManifest = parseManifest();

      String title = aiccManifest.getTitle();
      String launchUrl = aiccManifest.getLaunchUrl();
      if (title == null || title.isEmpty()) {
        throw new ModuleParsingException("AICC module at '" + this.moduleFileProvider.getRootPath()
            + "' has empty or missing title in course file (expected in [Course_Data] section)");
      }
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException("AICC module at '" + this.moduleFileProvider.getRootPath()
            + "' has empty or missing launch URL in course file (expected in [Course_Data] section)");
      }

      // Build and return metadata
      return AiccMetadata.create(aiccManifest, checkForXapi());
    } catch (IOException | ManifestParseException e) {
      throw new ModuleParsingException(
          "Error parsing AICC module at '" + this.moduleFileProvider.getRootPath()
              + "' (requires .crs, .des, .au, and .cst files): " + e.getMessage(), e);
    } catch (ModuleParsingException e) {
      // Re-throw ModuleParsingException directly without wrapping
      throw e;
    } catch (Exception e) {
      // Catch any other unexpected exceptions
      throw new ModuleParsingException(
          "Unexpected error parsing AICC module at '" + this.moduleFileProvider.getRootPath()
              + "': " + e
              .getClass()
              .getSimpleName() + " - " + e.getMessage(), e);
    }
  }

  /**
   * Parses the AICC manifest and returns an instance of AiccManifest.
   *
   * @return An instance of AiccManifest containing parsed data.
   * @throws IOException If an error occurs while reading files.
   * @throws ModuleParsingException If an error occurs during parsing.
   * @throws ManifestParseException If an error occurs while parsing the manifest.
   */
  public AiccManifest parseManifest()
      throws IOException, ModuleParsingException, ManifestParseException {
    AiccCourse aiccCourse = parseIniFile();

    // Parse CSV-style course data
    var descriptors = parseCsvFile(Descriptor.class, DES_EXTENSION);
    var assignableUnits = parseCsvFile(AssignableUnit.class, AU_EXTENSION);
    var courseStructure = parseCsvFile(CourseStructure.class, CST_EXTENSION);

    AiccManifest manifest = new AiccManifest(aiccCourse, assignableUnits, descriptors,
        courseStructure);

    // Optional AICC files: .pre (prerequisites) and .ort (objective relations)
    // Parse if present; ignore if missing.
    List<Map<String, String>> prerequisites = parseOptionalCsvAsMap(PRE_EXTENSION);
    manifest.setPrerequisitesTable(prerequisites);
    List<Map<String, String>> objectives = parseOptionalCsvAsMap(ORT_EXTENSION);
    manifest.setObjectivesRelationTable(objectives);

    return manifest;
  }

  /**
   * Loads external metadata from the provided AICC manifest. This method is a no-op as there is no
   * external metadata to load for AICC.
   *
   * @param manifest The {@code AiccManifest} instance containing metadata to be loaded.
   */
  @Override
  void loadExternalMetadata(AiccManifest manifest) {
    // No external metadata to load for AICC
  }

  @Override
  protected Class<AiccManifest> getManifestClass() {
    return AiccManifest.class;
  }

  /**
   * Parses a CSV file with the specified extension into a list of objects of the given class type.
   * This method reads the contents of the CSV file, maps it to the schema of the provided class,
   * and returns a list of mapped objects. If the file is not found, an {@code IOException} is
   * thrown.
   *
   * @param <T> The type of the objects into which the CSV file will be parsed.
   * @param clazz The {@code Class} object representing the type of objects in the resulting list.
   * @param extension The file extension used to locate the target CSV file.
   * @return A {@code List} containing objects of the specified type parsed from the CSV file.
   * @throws IOException If the file cannot be located or an error occurs while reading its
   * contents.
   */
  private <T> List<T> parseCsvFile(Class<T> clazz, String extension) throws IOException {
    String fileName = findFileByExtension(extension);
    if (fileName == null) {
      checkAvailableFiles(extension, "AICC CSV file with extension '");
    }

    try (InputStream inputStream = moduleFileProvider.getFileContents(fileName)) {
      MappingIterator<T> objectMappingIterator = new CsvMapper()
          .readerWithTypedSchemaFor(clazz)
          .with(CsvSchema
              .emptySchema()
              .withHeader()
              .withColumnSeparator(',')
              .withQuoteChar('"'))
          .readValues(inputStream);
      return new ArrayList<>(objectMappingIterator.readAll());
    }
  }

  /**
   * Parses a CSV file with an unknown schema into a list of case-insensitive maps. Returns an empty
   * list if the file is not found.
   */
  private List<Map<String, String>> parseOptionalCsvAsMap(String extension) throws IOException {
    String fileName = findFileByExtension(extension);
    if (fileName == null) {
      return Collections.emptyList();
    }

    try (InputStream inputStream = moduleFileProvider.getFileContents(fileName)) {
      MappingIterator<Map<String, String>> it = new CsvMapper()
          .readerFor(Map.class)
          .with(CsvSchema
              .emptySchema()
              .withHeader()
              .withColumnSeparator(',')
              .withQuoteChar('"'))
          .readValues(inputStream);
      List<Map<String, String>> rows = new ArrayList<>(it.readAll());
      // filter out completely empty rows that some authoring tools include
      rows.removeIf(row -> row == null || row
          .values()
          .stream()
          .allMatch(v -> v == null || v
              .trim()
              .isEmpty()));
      return rows;
    }
  }

  /**
   * Checks for available files in the module package with the specified extension. If no matching
   * file is found, an {@code IOException} is thrown containing suggestions for available files.
   *
   * @param extension The file extension to look for.
   * @param x The prefix message describing the error or context.
   * @throws IOException If no file with the specified extension is found or an error occurs while
   * fetching the list of files.
   */
  private void checkAvailableFiles(String extension, String x) throws IOException {
    List<String> availableFiles = moduleFileProvider.listFiles("");
    String suggestion = availableFiles
        .stream()
        .filter(f -> f
            .toLowerCase()
            .contains(extension.substring(1)))
        .findFirst()
        .map(f -> " Did you mean: " + f + "?")
        .orElse(" Available files: " + String.join(", ", availableFiles
            .stream()
            .limit(5)
            .toList()) + (availableFiles.size() > 5 ? " (and " + (availableFiles.size() - 5)
            + " more)" : ""));
    throw new IOException(
        x + extension + "' not found in module at '" + moduleFileProvider.getRootPath() + "'."
            + suggestion);
  }

  /**
   * Parses an AICC INI file with the specified extension, converting its contents into an object of
   * the target type. This method reads the INI configuration, processes its sections and keys into
   * a structured map, and maps the resulting data to the target class using a JSON object mapper.
   * If the file is not found or is improperly formatted, appropriate exceptions are thrown.
   *
   * @param <T> The target type to which the parsed INI data will be mapped.
   * @return An object of the specified type containing the parsed INI data.
   * @throws IOException If the INI file cannot be located or an error occurs while reading its
   * contents.
   * @throws ManifestParseException If an error occurs during INI file parsing or data conversion.
   */
  private <T> T parseIniFile() throws IOException, ManifestParseException {
    String fileName = findFileByExtension(AiccParser.CRS_EXTENSION);
    if (fileName == null) {
      checkAvailableFiles(AiccParser.CRS_EXTENSION, "AICC INI file with extension '");
    }

    try (InputStream inputStream = moduleFileProvider.getFileContents(
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
          Object rawValue = confSection.getProperty(key);
          // Authoring tools sometimes emit keys without values; treat those as null instead of
          // failing with a NullPointerException when calling toString().
          String value = rawValue != null ? rawValue.toString() : null;
          subSectionMap.put(key, value);
        }
        mapData.put(section, subSectionMap);
      }
      ObjectMapper objectMapper = new ObjectMapper();
      //noinspection unchecked
      return objectMapper.convertValue(mapData, (Class<T>) AiccCourse.class);
    } catch (ConfigurationException e) {
      throw new ManifestParseException(
          "Error parsing AICC INI file '" + fileName + "' in module at '"
              + moduleFileProvider.getRootPath() + "' (check file format and encoding): "
              + e.getMessage(), e);
    }
  }

  /**
   * Finds a file with the specified extension in the module package. This method searches through
   * the list of available files and returns the first file that matches the given extension, or
   * null if no such file is found.
   *
   * @param extension The file extension to search for. Must not be null or empty.
   * @return The name of the first file matching the specified extension, or null if no file is
   * found.
   * @throws IOException If an error occurs while accessing the file system.
   */
  private String findFileByExtension(String extension) throws IOException {
    return moduleFileProvider
        .listFiles("")
        .stream()
        .filter(fileName -> fileName.endsWith(extension))
        .findFirst()
        .orElse(null);
  }
}
