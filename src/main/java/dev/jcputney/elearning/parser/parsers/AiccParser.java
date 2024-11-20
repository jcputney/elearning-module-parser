/*
 * Copyright (c) 2024. Jonathan Putney
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

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.output.aicc.AiccMetadata;
import dev.jcputney.elearning.parser.output.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.output.aicc.AssignableUnitData;
import dev.jcputney.elearning.parser.output.aicc.CourseData;
import dev.jcputney.elearning.parser.output.aicc.CourseStructureData;
import dev.jcputney.elearning.parser.output.aicc.CourseStructureElement;
import dev.jcputney.elearning.parser.output.aicc.DescriptionData;
import dev.jcputney.elearning.parser.output.aicc.ObjectiveRelationship;
import dev.jcputney.elearning.parser.output.aicc.PrerequisiteData;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

/**
 * Parses AICC (Aviation Industry CBT Committee) eLearning modules by reading INI-style files with
 * specific extensions such as .crs, .des, .au, .cst, .ort, and .pre to extract core metadata and
 * organize it in structured format.
 */
public class AiccParser extends BaseParser<AiccMetadata, AiccManifest> {

  private static final String CRS_EXTENSION = ".crs";
  private static final String DES_EXTENSION = ".des";
  private static final String AU_EXTENSION = ".au";
  private static final String CST_EXTENSION = ".cst";
  private static final String ORT_EXTENSION = ".ort";
  private static final String PRE_EXTENSION = ".pre";

  public AiccParser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Parses the AICC module at the specified path, extracting metadata from the required AICC
   * files.
   *
   * @param modulePath The path to the module's root directory.
   * @return An AiccMetadata object containing the extracted metadata.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  @Override
  public AiccMetadata parse(String modulePath) throws ModuleParsingException {
    try {
      // Parse each required AICC file into structured data classes
      CourseData courseData = parseCourseData(modulePath);
      DescriptionData descriptionData = parseDescriptionData(modulePath);
      AssignableUnitData assignableUnitData = parseAssignableUnits(modulePath);
      PrerequisiteData prerequisiteData = parsePrerequisites(modulePath);
      CourseStructureData courseStructureData = parseCourseStructure(modulePath);

      // Use helper methods for complex structures
      List<ObjectiveRelationship> objectiveRelationships = parseObjectiveRelationships(modulePath);

      return new AiccMetadata.Builder()
          .xapiEnabled(false)
          .title(descriptionData.getCourseName().orElse(null))
          .description(descriptionData.getCourseDescription().orElse(null))
          .identifier(courseData.getId())
          .version(courseData.getVersion().orElse("AICC"))
          .moduleType(ModuleType.AICC)
          .courseStructure(courseStructureData)
          .assignableUnits(assignableUnitData.getAssignableUnits())
          .prerequisites(prerequisiteData.getPrerequisites().orElse(null))
          .credit(courseData.getCredit().orElse(null))
          .timeLimitAction(courseData.getTimeLimitAction().orElse(null))
          .objectiveRelationships(objectiveRelationships)
          .build();

    } catch (Exception e) {
      throw new ModuleParsingException("Error parsing AICC module at path: " + modulePath, e);
    }
  }

  /**
   * Detects if the specified module directory contains the required AICC files.
   *
   * @param modulePath The path to the module's root directory.
   * @return True if the module contains the required AICC files, false otherwise.
   */
  @Override
  public boolean isSupported(String modulePath) {
    return findFileByExtension(modulePath, CRS_EXTENSION) != null
        && findFileByExtension(modulePath, AU_EXTENSION) != null;
  }

  @Override
  protected Class<AiccManifest> getManifestClass() {
    return AiccManifest.class;
  }

  /**
   * Parses the course data from the .crs file in the specified module directory.
   *
   * @param modulePath The path to the module's root directory.
   * @return A CourseData object representing the parsed course data.
   * @throws IOException if an error occurs while reading the file.
   * @throws ConfigurationException if the file cannot be parsed as an INI file.
   */
  private CourseData parseCourseData(String modulePath) throws IOException, ConfigurationException {
    INIConfiguration config = loadIniFile(modulePath, CRS_EXTENSION);
    return new CourseData(
        config.getString("Course.Course_ID"),
        config.getString("Course.Version"),
        config.getString("Course.Credit"),
        config.getString("Course.Time_Limit_Action")
    );
  }

  /**
   * Parses the course description from the .des file in the specified module directory.
   *
   * @param modulePath The path to the module's root directory.
   * @return A DescriptionData object representing the parsed description.
   * @throws IOException if an error occurs while reading the file.
   * @throws ConfigurationException if the file cannot be parsed as an INI file.
   */
  private DescriptionData parseDescriptionData(String modulePath)
      throws IOException, ConfigurationException {
    INIConfiguration config = loadIniFile(modulePath, DES_EXTENSION);
    return new DescriptionData(
        config.getString("Course.Course_Name"),
        config.getString("Course.Course_Description")
    );
  }

  /**
   * Parses the assignable units from the .au file in the specified module directory.
   *
   * @param modulePath The path to the module's root directory.
   * @return A list of AssignableUnit objects representing the parsed units.
   * @throws IOException if an error occurs while reading the file.
   * @throws ConfigurationException if the file cannot be parsed as an INI file.
   */
  private AssignableUnitData parseAssignableUnits(String modulePath)
      throws IOException, ConfigurationException {
    INIConfiguration config = loadIniFile(modulePath, AU_EXTENSION);
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    config.getKeys("AU").forEachRemaining(key -> {
      String id = key.replace("AU.", "");
      String fileName = config.getString("AU.File_Name");
      String maxScore = config.getString("AU.Max_Score");
      String minScore = config.getString("AU.Min_Score");
      assignableUnits.add(new AssignableUnit(id, fileName, maxScore, minScore));
    });
    return new AssignableUnitData(assignableUnits);
  }

  /**
   * Parses the course structure from the .cst file in the specified module directory.
   *
   * @param modulePath The path to the module's root directory.
   * @return A list of CourseStructureElement objects representing the parsed structure.
   * @throws IOException if an error occurs while reading the file.
   * @throws ConfigurationException if the file cannot be parsed as an INI file.
   */
  private PrerequisiteData parsePrerequisites(String modulePath)
      throws IOException, ConfigurationException {
    INIConfiguration config = loadIniFile(modulePath, PRE_EXTENSION);
    return new PrerequisiteData(config.getString("Prerequisites.Required"));
  }

  /**
   * Parses the .cst file to extract the hierarchical course structure.
   *
   * @param modulePath The base path to the module's root directory.
   * @return A {@link CourseStructureData} object containing a list of course structure elements.
   * @throws IOException If there is an error reading the file.
   * @throws ConfigurationException If there is an error parsing the INI file.
   */
  private CourseStructureData parseCourseStructure(String modulePath)
      throws IOException, ConfigurationException {
    INIConfiguration config = loadIniFile(modulePath, CST_EXTENSION);
    List<CourseStructureElement> courseStructure = new ArrayList<>();
    config.getKeys("Structure").forEachRemaining(key -> {
      String id = key.replace("Structure.", "");
      String title = config.getString(key);
      courseStructure.add(new CourseStructureElement(id, title, List.of()));
    });
    return new CourseStructureData(courseStructure);
  }

  /**
   * Parses objective relationships from the .ort file in the specified module directory.
   *
   * @param modulePath The path to the module's root directory.
   * @return A list of ObjectiveRelationship objects representing the parsed relationships.
   * @throws IOException if an error occurs while reading the file.
   * @throws ConfigurationException if the file cannot be parsed as an INI file.
   */
  private List<ObjectiveRelationship> parseObjectiveRelationships(String modulePath)
      throws IOException, ConfigurationException {
    INIConfiguration config = loadIniFile(modulePath, ORT_EXTENSION);
    List<ObjectiveRelationship> relationships = new ArrayList<>();
    config.getKeys("Objective_Relationships").forEachRemaining(key -> {
      String source = key.replace("Objective_Relationships.", "");
      String target = config.getString(key);
      relationships.add(new ObjectiveRelationship(source, target, "prerequisite"));
    });
    return relationships;
  }

  /**
   * Loads an INI-style configuration file from the specified module directory.
   *
   * @param modulePath The path to the module's root directory.
   * @param extension The file extension to look for (e.g., .crs, .des).
   * @return An INIConfiguration object representing the parsed file contents.
   * @throws IOException if an error occurs while reading the file.
   * @throws ConfigurationException if the file cannot be parsed as an INI file.
   */
  private INIConfiguration loadIniFile(String modulePath, String extension)
      throws IOException, ConfigurationException {
    String fileName = findFileByExtension(modulePath, extension);
    if (fileName == null) {
      throw new ConfigurationException(
          "Required AICC file with extension " + extension + " not found.");
    }

    try (InputStream inputStream = fileAccess.getFileContents(modulePath + "/" + fileName)) {
      INIConfiguration config = new INIConfiguration();
      FileHandler handler = new FileHandler(config);
      handler.load(inputStream);
      return config;
    }
  }

  /**
   * Finds the first file in the specified directory with the given extension.
   *
   * @param modulePath The path to the module's root directory.
   * @param extension The file extension to look for (e.g., .crs, .des).
   * @return The name of the file with the specified extension, or null if none is found.
   */
  private String findFileByExtension(String modulePath, String extension) {
    try {
      List<String> files = fileAccess.listFiles(modulePath);
      for (String file : files) {
        if (file.toLowerCase().endsWith(extension.toLowerCase())) {
          return file;
        }
      }
    } catch (IOException e) {
      System.err.println("Error listing files in directory " + modulePath + ": " + e.getMessage());
    }
    return null;
  }
}