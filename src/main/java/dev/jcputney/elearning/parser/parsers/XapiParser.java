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
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.output.metadata.xapi.XapiMetadata;

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
   * Parses the xAPI/TinCan manifest.
   *
   * @return the parsed xAPI metadata
   * @throws ModuleParsingException if parsing fails
   */
  @Override
  public XapiMetadata parse() throws ModuleParsingException {
    try {
      // Check if tincan.xml exists before attempting to parse
      if (moduleFileProvider.getFileContents(TINCAN_XML) == null) {
        throw new ModuleParsingException("tincan.xml not found in the package");
      }

      // Parse TinCan manifest from tincan.xml
      var manifest = parseManifest(TINCAN_XML);

      // Validate required fields
      String title = manifest.getTitle();
      if (title == null || title.isEmpty()) {
        throw new ModuleParsingException("TinCan module missing required title field");
      }
      String launchUrl = manifest.getLaunchUrl();
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException("TinCan module missing required launch URL field");
      }

      return new XapiMetadata(manifest);
    } catch (ModuleParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ModuleParsingException(
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
