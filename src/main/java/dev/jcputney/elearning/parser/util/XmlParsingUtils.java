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

package dev.jcputney.elearning.parser.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.input.lom.LOM;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.slf4j.Logger;

/**
 * Utility class for XML parsing operations used throughout the app.
 * <p>
 * This class provides methods for parsing XML files into Java objects using Jackson's XmlMapper,
 * with appropriate security settings and error handling.
 * </p>
 */
public final class XmlParsingUtils {

  /**
   * Logger for logging messages related to XML parsing.
   */
  private static final Logger log = LoggingUtils.getLogger(XmlParsingUtils.class);

  // Private constructor to prevent instantiation
  private XmlParsingUtils() {
    throw new AssertionError("Utility class should not be instantiated");
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
  public static <C> C parseXmlToObject(InputStream stream, Class<C> clazz)
      throws IOException, XMLStreamException {
    if (stream == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    if (clazz == null) {
      throw new IllegalArgumentException("Class cannot be null");
    }
    log.debug("Parsing XML to object of type: {}", clazz.getSimpleName());
    try {
      XMLInputFactory factory = XMLInputFactory.newFactory();
      factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
      factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
      factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
      XMLStreamReader reader = factory.createXMLStreamReader(stream);
      XmlMapper xmlMapper = new XmlMapper();
      C result = xmlMapper.readValue(reader, clazz);
      log.debug("Successfully parsed XML to object of type: {}", clazz.getSimpleName());
      return result;
    } catch (IOException | XMLStreamException e) {
      log.error("Error parsing XML to object of type {}: {}", clazz.getSimpleName(),
          e.getMessage());
      throw e;
    }
  }

  /**
   * Loads an external LOM metadata file into the specified LoadableMetadata object.
   * <p>
   * This method reads the external metadata file and sets the LOM object in the LoadableMetadata
   * instance if the file exists and can be parsed.
   * </p>
   * <p>
   * If the external metadata file doesn't exist or can't be parsed, the LoadableMetadata object
   * will not be modified.
   * </p>
   *
   * @param subMetadata The LoadableMetadata object to load the external metadata into.
   * @param fileAccess The FileAccess instance to use for reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   * @throws IllegalArgumentException if subMetadata or fileAccess is null
   */
  public static void loadExternalMetadataIntoMetadata(LoadableMetadata subMetadata,
      FileAccess fileAccess)
      throws XMLStreamException, IOException {
    if (subMetadata == null) {
      throw new IllegalArgumentException("LoadableMetadata cannot be null");
    }
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }

    if (subMetadata.getLocation() != null && !subMetadata
        .getLocation()
        .isEmpty()) {
      String metadataPath = subMetadata.getLocation();
      log.debug("Checking for external metadata file: {}", metadataPath);
      if (fileAccess.fileExists(metadataPath)) {
        log.debug("Loading external metadata from: {}", metadataPath);
        try (InputStream fileContents = fileAccess.getFileContents(metadataPath)) {
          LOM lom = parseXmlToObject(fileContents, LOM.class);
          subMetadata.setLom(lom);
          log.debug("Successfully loaded external metadata from: {}", metadataPath);
        } catch (IOException | XMLStreamException e) {
          log.warn("Error loading external metadata from {}: {}", metadataPath, e.getMessage());
          throw e;
        }
      } else {
        log.debug("External metadata file not found: {}", metadataPath);
      }
    }
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
   *
   * @param subMetadata The LoadableMetadata object to load the external metadata into.
   * @param moduleFileProvider The ModuleFileProvider instance to use for reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   * @throws IllegalArgumentException if subMetadata or moduleFileProvider is null
   */
  public static void loadExternalMetadataIntoMetadata(LoadableMetadata subMetadata,
      ModuleFileProvider moduleFileProvider)
      throws XMLStreamException, IOException {
    if (subMetadata == null) {
      throw new IllegalArgumentException("LoadableMetadata cannot be null");
    }
    if (moduleFileProvider == null) {
      throw new IllegalArgumentException("ModuleFileProvider cannot be null");
    }

    if (subMetadata.getLocation() != null && !subMetadata
        .getLocation()
        .isEmpty()) {
      String metadataPath = subMetadata.getLocation();
      log.debug("Checking for external metadata file: {}", metadataPath);
      if (moduleFileProvider.fileExists(metadataPath)) {
        log.debug("Loading external metadata from: {}", metadataPath);
        try (InputStream fileContents = moduleFileProvider.getFileContents(metadataPath)) {
          LOM lom = parseXmlToObject(fileContents, LOM.class);
          subMetadata.setLom(lom);
          log.debug("Successfully loaded external metadata from: {}", metadataPath);
        } catch (IOException | XMLStreamException e) {
          log.warn("Error loading external metadata from {}: {}", metadataPath, e.getMessage());
          throw e;
        }
      } else {
        log.debug("External metadata file not found: {}", metadataPath);
      }
    }
  }
}
