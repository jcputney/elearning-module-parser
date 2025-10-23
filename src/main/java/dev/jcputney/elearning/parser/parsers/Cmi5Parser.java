/*
 * Copyright (c) 2024-2025. Jonathan Putney
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
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.output.metadata.cmi5.Cmi5Metadata;
import dev.jcputney.elearning.parser.util.FileUtils;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.Cmi5Validator;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;

/**
 * Cmi5Parser is responsible for parsing cmi5-specific metadata from the cmi5.xml file.
 * <p>
 * The "cmi5" xAPI-based specification includes Assignable Units (AUs) as discrete learning objects
 * with metadata for LMS tracking and reporting. This parser extracts metadata such as the title,
 * launch URL, prerequisites, dependencies, detailed AU information, and custom metadata.
 * </p>
 */
public final class Cmi5Parser extends BaseParser<Cmi5Metadata, Cmi5Manifest> {

  /**
   * The name of the cmi5 XML file that contains the module metadata.
   */
  public static final String CMI5_XML = "cmi5.xml";

  /**
   * Constructs a Cmi5Parser with the specified FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   */
  public Cmi5Parser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Constructs a Cmi5Parser with the specified FileAccess instance and parser options.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   * @param options The parser options to control validation and calculation behavior.
   */
  public Cmi5Parser(FileAccess fileAccess, dev.jcputney.elearning.parser.api.ParserOptions options) {
    super(fileAccess, options);
  }

  /**
   * Validates the cmi5 module without fully parsing it.
   * This method provides efficient validation by only parsing the manifest file
   * and running structural validation checks.
   *
   * @return ValidationResult containing any errors or warnings found
   */
  @Override
  public ValidationResult validate() {
    try {
      // Parse manifest only (lightweight)
      Cmi5Manifest manifest = parseManifest(CMI5_XML);

      // Run validator
      Cmi5Validator validator = new Cmi5Validator();
      return validator.validate(manifest);
    } catch (ManifestParseException | IOException | XMLStreamException e) {
      return ValidationResult.of(
          ValidationIssue.error(
              "MANIFEST_PARSE_ERROR",
              "Failed to parse manifest: " + e.getMessage(),
              CMI5_XML
          )
      );
    }
  }

  /**
   * Parses the cmi5 module located at the specified modulePath.
   * <p>
   * This method reads the cmi5.xml file to extract metadata such as the title, launch URL,
   * prerequisites, dependencies, list of Assignable Units (AUs), and custom data for LMS tracking.
   * Since cmi5 is always xAPI-enabled, the isXapiEnabled flag is set to true for all cmi5 modules.
   * </p>
   *
   * @return A Cmi5Metadata object containing the parsed metadata.
   * @throws ModuleParsingException If the module's manifest cannot be parsed or if required fields
   * are missing.
   */
  @Override
  public Cmi5Metadata parse() throws ModuleException {
    try {
      // Find the cmi5 manifest file (case-insensitive)
      var files = moduleFileProvider.listFiles("");
      String cmi5File = FileUtils.findFileIgnoreCase(files, CMI5_XML);

      if (cmi5File == null) {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CMI5_MISSING_MANIFEST",
                "cmi5 manifest file not found: " + CMI5_XML + " in module at '" + moduleFileProvider.getRootPath() + "'",
                "package root")
        );
        throw result.toException("Failed to parse cmi5 module");
      }

      // Parse cmi5-specific metadata from cmi5.xml
      var manifest = parseManifest(cmi5File);

      String title = manifest.getTitle();
      if (title == null || title.isEmpty()) {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CMI5_MISSING_TITLE", "cmi5 module missing required title field", "cmi5.xml")
        );
        throw result.toException("Failed to parse cmi5 module");
      }
      String launchUrl = manifest.getLaunchUrl();
      if (launchUrl == null || launchUrl.isEmpty()) {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CMI5_MISSING_LAUNCH_URL", "cmi5 module missing required launch URL field", "cmi5.xml")
        );
        throw result.toException("Failed to parse cmi5 module");
      }

      // Build and return the Cmi5Metadata
      return Cmi5Metadata.create(manifest, true); // cmi5 modules are always xAPI-enabled

    } catch (IOException e) {
      throw new ManifestParseException(
          "Error listing files in cmi5 module at path: " + this.moduleFileProvider.getRootPath(), e);
    } catch (ModuleParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ManifestParseException(
          "Error parsing cmi5 module at path: " + this.moduleFileProvider.getRootPath(), e);
    }
  }

  /**
   * Loads external metadata for the given Cmi5Manifest. This method is overridden in the context of
   * cmi5 but does not perform any operations because cmi5 does not require external metadata
   * loading.
   *
   * @param manifest The Cmi5Manifest object containing data for the cmi5 module.
   */
  @Override
  void loadExternalMetadata(Cmi5Manifest manifest) {
    // No external metadata loading is required for cmi5
  }

  /**
   * Returns the class type of the Cmi5 manifest used by this parser.
   *
   * @return The {@code Class} object representing the {@code Cmi5Manifest} type.
   */
  @Override
  protected Class<Cmi5Manifest> getManifestClass() {
    return Cmi5Manifest.class;
  }
}
