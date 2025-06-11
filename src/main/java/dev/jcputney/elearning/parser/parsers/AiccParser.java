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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Parses AICC (Aviation Industry CBT Committee) eLearning modules by handling both INI-style and
 * CSV-style files with specific extensions such as .crs, .des, .au, .cst, .ort, and .pre.
 */
public class AiccParser extends BaseParser<AiccMetadata, AiccManifest> {

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
        throw new ModuleParsingException("AICC module at '" + this.moduleFileProvider.getRootPath() + 
            "' has empty or missing title in course file (expected in [Course_Data] section)");
      }
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException("AICC module at '" + this.moduleFileProvider.getRootPath() + 
            "' has empty or missing launch URL in course file (expected in [Course_Data] section)");
      }

      // Build and return metadata
      return AiccMetadata.create(aiccManifest, checkForXapi());
    } catch (IOException | ManifestParseException e) {
      throw new ModuleParsingException(
          "Error parsing AICC module at '" + this.moduleFileProvider.getRootPath() + 
          "' (requires .crs, .des, .au, and .cst files): " + e.getMessage(), e);
    } catch (ModuleParsingException e) {
      // Re-throw ModuleParsingException directly without wrapping
      throw e;
    } catch (Exception e) {
      // Catch any other unexpected exceptions
      throw new ModuleParsingException(
          "Unexpected error parsing AICC module at '" + this.moduleFileProvider.getRootPath() + 
          "': " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
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
  void loadExternalMetadata(AiccManifest manifest) throws XMLStreamException, IOException {
    // No external metadata to load for AICC
  }

  @Override
  protected Class<AiccManifest> getManifestClass() {
    return AiccManifest.class;
  }

  /**
   * Parses a CSV file and returns a list of objects of the specified class.
   */
  private <T> List<T> parseCsvFile(Class<T> clazz, String extension)
      throws IOException {
    String fileName = findFileByExtension(extension);
    if (fileName == null) {
      List<String> availableFiles = moduleFileProvider.listFiles("");
      String suggestion = availableFiles.stream()
          .filter(f -> f.toLowerCase().contains(extension.substring(1)))
          .findFirst()
          .map(f -> " Did you mean: " + f + "?")
          .orElse(" Available files: " + String.join(", ", availableFiles.stream().limit(5).toList()) + 
                 (availableFiles.size() > 5 ? " (and " + (availableFiles.size() - 5) + " more)" : ""));
      throw new IOException("AICC CSV file with extension '" + extension + "' not found in module at '" + 
          moduleFileProvider.getRootPath() + "'." + suggestion);
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
   * Parses an INI file and returns an object of the specified class.
   */
  private <T> T parseIniFile(Class<T> clazz, String extension)
      throws IOException, ManifestParseException {
    String fileName = findFileByExtension(extension);
    if (fileName == null) {
      List<String> availableFiles = moduleFileProvider.listFiles("");
      String suggestion = availableFiles.stream()
          .filter(f -> f.toLowerCase().contains(extension.substring(1)))
          .findFirst()
          .map(f -> " Did you mean: " + f + "?")
          .orElse(" Available files: " + String.join(", ", availableFiles.stream().limit(5).toList()) + 
                 (availableFiles.size() > 5 ? " (and " + (availableFiles.size() - 5) + " more)" : ""));
      throw new IOException("AICC INI file with extension '" + extension + "' not found in module at '" + 
          moduleFileProvider.getRootPath() + "'." + suggestion);
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
          String value = confSection
              .getProperty(key)
              .toString();
          subSectionMap.put(key, value);
        }
        mapData.put(section, subSectionMap);
      }
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.convertValue(mapData, clazz);
    } catch (ConfigurationException e) {
      throw new ManifestParseException("Error parsing AICC INI file '" + fileName + "' in module at '" + 
          moduleFileProvider.getRootPath() + "' (check file format and encoding): " + e.getMessage(), e);
    }
  }

  private String findFileByExtension(String extension) throws IOException {
    return moduleFileProvider
        .listFiles("")
        .stream()
        .filter(fileName -> fileName.endsWith(extension))
        .findFirst()
        .orElse(null);
  }
}
