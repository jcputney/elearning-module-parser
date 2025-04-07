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

import dev.jcputney.elearning.parser.ModuleParser;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.DefaultModuleFileProvider;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.util.LoggingUtils;
import dev.jcputney.elearning.parser.util.XmlParsingUtils;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;

/**
 * Abstract base class for all module parsers, providing shared capability for common operations,
 * like detecting xAPI-related files and utility methods for file parsing.
 * <p>
 * This class shouldn't parse any module types directly, but should provide utility methods and
 * abstract methods to be implemented by the specific module parsers (SCORM, cmi5, LTI, etc.).
 * </p>
 *
 * @param <T> The type of ModuleMetadata that this parser will return.
 * @param <M> The type of PackageManifest that this parser will work with.
 */
public abstract class BaseParser<T extends ModuleMetadata<M>, M extends PackageManifest> implements
    ModuleParser<M> {

  /**
   * The name of the xAPI JavaScript file.
   */
  public static final String XAPI_JS_FILE = "xAPI.js";
  /**
   * The name of the sendStatement JavaScript file.
   */
  public static final String XAPI_SEND_STATEMENT_FILE = "sendStatement.js";
  /**
   * Logger for logging messages related to module parsing.
   */
  private static final Logger log = LoggingUtils.getLogger(BaseParser.class);
  /**
   * The ModuleFileProvider instance used for reading files in the module package.
   */
  protected final ModuleFileProvider moduleFileProvider;

  /**
   * Constructs a BaseParser with the specified ModuleFileProvider instance.
   *
   * @param moduleFileProvider An instance of ModuleFileProvider for reading files in the module
   * package.
   * @throws IllegalArgumentException if moduleFileProvider is null
   */
  protected BaseParser(ModuleFileProvider moduleFileProvider) {
    if (moduleFileProvider == null) {
      throw new IllegalArgumentException("ModuleFileProvider cannot be null");
    }
    this.moduleFileProvider = moduleFileProvider;
  }

  /**
   * Constructs a BaseParser with the specified FileAccess instance. This constructor creates a
   * DefaultModuleFileProvider that wraps the FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   * @throws IllegalArgumentException if fileAccess is null
   */
  protected BaseParser(FileAccess fileAccess) {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }
    this.moduleFileProvider = new DefaultModuleFileProvider(fileAccess);
  }

  /**
   * Abstract method that parses the module and returns the corresponding metadata object. This must
   * be implemented by the child parsers (for example, SCORM, cmi5, LTI).
   *
   * @return A ModuleMetadata object containing the parsed module metadata.
   * @throws ModuleParsingException If the module type can't be determined or there's an error
   * parsing.
   */
  public abstract T parse() throws ModuleParsingException;

  /**
   * Parses the manifest file at the specified path and returns the corresponding manifest object.
   *
   * @param manifestPath The path to the manifest file.
   * @return The parsed manifest object.
   * @throws IOException If an error occurs while reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws ModuleParsingException If there's an error parsing the manifest.
   * @throws IllegalArgumentException if manifestPath is null
   */
  public M parseManifest(String manifestPath)
      throws IOException, XMLStreamException, ModuleParsingException {
    if (manifestPath == null) {
      throw new IllegalArgumentException("Manifest path cannot be null");
    }
    log.debug("Parsing manifest file: {}", manifestPath);
    try (InputStream manifestStream = moduleFileProvider.getFileContents(manifestPath)) {
      M manifest = parseXmlToObject(manifestStream, getManifestClass());
      log.debug("Successfully parsed manifest file: {}", manifestPath);
      return manifest;
    } catch (IOException | XMLStreamException e) {
      log.error("Error parsing manifest file {}: {}", manifestPath, e.getMessage());
      throw e;
    }
  }

  /**
   * Abstract method to return the class of the manifest object for the specific parser.
   *
   * @return The class of the manifest object.
   */
  protected abstract Class<M> getManifestClass();

  /**
   * Checks if the module contains xAPI-related files (for example, xAPI.js, sendStatement.js).
   * These files indicate whether xAPI tracking is enabled for the module.
   *
   * @return true if xAPI is enabled, false otherwise.
   */
  protected boolean checkForXapi() {
    log.debug("Checking for xAPI-related files");
    return moduleFileProvider.hasXapiSupport();
  }

  /**
   * Parses an XML file into an object of the specified class using Jackson's XmlMapper.
   *
   * @param <C> The type of the class to parse the XML into.
   * @param stream The InputStream for the XML file.
   * @param clazz The class to parse the XML into.
   * @return A new instance of the specified class with the parsed XML data.
   * @throws IOException If an error occurs while reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IllegalArgumentException if stream or clazz is null
   */
  protected <C> C parseXmlToObject(InputStream stream, Class<C> clazz)
      throws IOException, XMLStreamException {
    return XmlParsingUtils.parseXmlToObject(stream, clazz);
  }

  /**
   * Loads an external LOM metadata file into the specified LoadableMetadata object.
   * <p>
   * This method reads the external metadata file and sets the LOM object in the LoadableMetadata
   * instance if the file exists and can be parsed.
   * </p>
   * <p>
   * If the external metadata file doesn't exist or can't be parsed, the LoadableMetadata object
   * won't be modified.
   * </p>
   * <p>
   * This method is intended to be used by parsers that support external metadata files, such as
   * SCORM 1.2 and SCORM 2004.
   * </p>
   *
   * @param subMetadata The LoadableMetadata object to load the external metadata into.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   * @throws IllegalArgumentException if subMetadata is null
   */
  protected void loadExternalMetadataIntoMetadata(LoadableMetadata subMetadata)
      throws XMLStreamException, IOException {
    if (subMetadata == null) {
      log.debug("Not loading external metadata: subMetadata is null");
      return;
    }

    XmlParsingUtils.loadExternalMetadataIntoMetadata(subMetadata, moduleFileProvider);
  }
}
