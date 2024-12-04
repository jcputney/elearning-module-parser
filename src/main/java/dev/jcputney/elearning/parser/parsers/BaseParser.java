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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.ModuleParser;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Abstract base class for all module parsers, providing shared functionality for common operations,
 * like detecting xAPI-related files and utility methods for file parsing.
 * <p>
 * This class should not parse any module types directly, but should provide utility methods and
 * abstract methods to be implemented by the specific module parsers (SCORM, cmi5, LTI, etc.).
 * </p>
 */
public abstract class BaseParser<T extends ModuleMetadata<M>, M extends PackageManifest> implements
    ModuleParser<M> {

  public static final String XAPI_JS_FILE = "xAPI.js";
  public static final String XAPI_SEND_STATEMENT_FILE = "sendStatement.js";

  protected final FileAccess fileAccess;

  /**
   * Constructs a BaseParser with the specified FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   */
  protected BaseParser(FileAccess fileAccess) {
    this.fileAccess = fileAccess;
  }

  /**
   * Abstract method that parses the module and returns the corresponding metadata object. This must
   * be implemented by the child parsers (e.g., SCORM, cmi5, LTI).
   *
   * @return A ModuleMetadata object containing the parsed module metadata.
   * @throws ModuleParsingException If the module type cannot be determined or there is an error
   * parsing.
   */
  public abstract T parse() throws ModuleParsingException;

  public M parseManifest(String manifestPath)
      throws IOException, XMLStreamException, ModuleParsingException {
    try (InputStream manifestStream = fileAccess.getFileContents(manifestPath)) {
      return parseXmlToObject(manifestStream, getManifestClass());
    }
  }

  /**
   * Abstract method to return the class of the manifest object for the specific parser.
   *
   * @return The class of the manifest object.
   */
  protected abstract Class<M> getManifestClass();

  /**
   * Checks if the module contains xAPI-related files (e.g., xAPI.js, sendStatement.js). These files
   * indicate whether xAPI tracking is enabled for the module.
   *
   * @return true if xAPI is enabled, false otherwise.
   */
  protected boolean checkForXapi() {
    // Check for common xAPI-related files in the module
    return fileAccess.fileExists(XAPI_JS_FILE) || fileAccess.fileExists(XAPI_SEND_STATEMENT_FILE);
  }

  /**
   * Parses an XML file into an object of the specified class using Jackson's XmlMapper.
   *
   * @param stream The InputStream for the XML file.
   * @return A new instance of the specified class with the parsed XML data.
   * @throws IOException If an error occurs while reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   */
  protected <C> C parseXmlToObject(InputStream stream, Class<C> clazz)
      throws IOException, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newFactory();
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    XMLStreamReader reader = factory.createXMLStreamReader(stream);
    XmlMapper xmlMapper = new XmlMapper();
    return xmlMapper.readValue(reader, clazz);
  }

  /**
   * Loads an external LOM metadata file into the specified LoadableMetadata object.
   * <p>
   * This method reads the external metadata file and sets the LOM object in the LoadableMetadata
   * instance if the file exists and can be parsed.
   * </p>
   * <p>
   * If the external metadata file does not exist or cannot be parsed, the LoadableMetadata object
   * will not be modified.
   * </p>
   * <p>
   * This method is intended to be used by parsers that support external metadata files, such as
   * SCORM 1.2 and SCORM 2004.
   * </p>
   *
   * @param subMetadata The LoadableMetadata object to load the external metadata into.
   */
  protected void loadExternalMetadataIntoMetadata(LoadableMetadata subMetadata)
      throws XMLStreamException, IOException {
    if (subMetadata != null && subMetadata.getLocation() != null && !subMetadata.getLocation()
        .isEmpty()) {
      String metadataPath = subMetadata.getLocation();
      if (fileAccess.fileExists(metadataPath)) {
        try (InputStream fileContents = fileAccess.getFileContents(metadataPath)) {
          LOM lom = parseXmlToObject(fileContents, LOM.class);
          subMetadata.setLom(lom);
        }
      }
    }
  }
}