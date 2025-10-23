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
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.output.metadata.xapi.XapiMetadata;
import dev.jcputney.elearning.parser.util.FileUtils;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.io.IOException;

/**
 * Parser for xAPI/TinCan packages.
 *
 * <p>Parses the {@code tincan.xml} manifest file and creates {@link XapiMetadata}
 * containing the module information.</p>
 *
 * <p>The TinCan manifest format is a simple XML structure containing one or more
 * activities with their launch URLs and metadata.</p>
 */
public final class XapiParser extends BaseParser<XapiMetadata, TincanManifest> {

  /**
   * The name of the TinCan XML file that contains the module metadata.
   */
  public static final String TINCAN_XML = "tincan.xml";

  /**
   * Constructs an XapiParser with the specified FileAccess instance.
   *
   * @param fileAccess an instance of FileAccess for reading files in the module package
   */
  public XapiParser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Constructs an XapiParser with the specified FileAccess instance and parser options.
   *
   * @param fileAccess an instance of FileAccess for reading files in the module package
   * @param options the parser options to control validation and calculation behavior
   */
  public XapiParser(FileAccess fileAccess, dev.jcputney.elearning.parser.api.ParserOptions options) {
    super(fileAccess, options);
  }

  /**
   * Parses the xAPI/TinCan manifest.
   *
   * @return the parsed xAPI metadata
   * @throws ModuleParsingException if parsing fails
   */
  @Override
  public XapiMetadata parse() throws ModuleException {
    try {
      // Find the tincan manifest file (case-insensitive)
      var files = moduleFileProvider.listFiles("");
      String tincanFile = FileUtils.findFileIgnoreCase(files, TINCAN_XML);

      if (tincanFile == null) {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("XAPI_MISSING_MANIFEST", "tincan.xml not found in the package at '" + moduleFileProvider.getRootPath() + "'", "package root")
        );
        throw result.toException("Failed to parse xAPI/TinCan module");
      }

      // Parse TinCan manifest from tincan.xml
      var manifest = parseManifest(tincanFile);

      // Validate required fields
      String title = manifest.getTitle();
      if (title == null || title.isEmpty()) {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("XAPI_MISSING_TITLE", "TinCan module missing required title field", "tincan.xml")
        );
        throw result.toException("Failed to parse xAPI/TinCan module");
      }
      String launchUrl = manifest.getLaunchUrl();
      if (launchUrl == null || launchUrl.isEmpty()) {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("XAPI_MISSING_LAUNCH_URL", "TinCan module missing required launch URL field", "tincan.xml")
        );
        throw result.toException("Failed to parse xAPI/TinCan module");
      }

      return new XapiMetadata(manifest);
    } catch (IOException e) {
      throw new ManifestParseException(
          "Error listing files in TinCan module at path: " + this.moduleFileProvider.getRootPath(),
          e);
    } catch (ModuleParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ManifestParseException(
          "Error parsing TinCan module at path: " + this.moduleFileProvider.getRootPath(), e);
    }
  }

  /**
   * Loads external metadata for the given TincanManifest. This method is overridden in the
   * context of TinCan but does not perform any operations because TinCan does not require
   * external metadata loading.
   *
   * @param manifest The TincanManifest object containing data for the TinCan module.
   */
  @Override
  void loadExternalMetadata(TincanManifest manifest) {
    // No external metadata loading is required for TinCan
  }

  /**
   * Returns the class type of the TinCan manifest used by this parser.
   *
   * @return The {@code Class} object representing the {@code TincanManifest} type.
   */
  @Override
  protected Class<TincanManifest> getManifestClass() {
    return TincanManifest.class;
  }
}
