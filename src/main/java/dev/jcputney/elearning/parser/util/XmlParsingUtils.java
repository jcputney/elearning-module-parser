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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.input.lom.LOM;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Utility class for XML parsing operations used throughout the app.
 * <p>
 * This class provides methods for parsing XML files into Java objects using Jackson's XmlMapper,
 * with appropriate security settings and error handling.
 * </p>
 */
public final class XmlParsingUtils {

  // Private constructor to prevent instantiation
  private XmlParsingUtils() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Creates and configures an XmlMapper with custom deserializers.
   *
   * @return A configured XmlMapper instance
   */
  private static XmlMapper createConfiguredXmlMapper() {
    XmlMapper xmlMapper = new XmlMapper();

    // Configure deserialization features
    xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Register custom deserializers
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Duration.class, new DurationIso8601Deserializer());
    xmlMapper.registerModule(module);

    return xmlMapper;
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
    return parseXmlToObject(stream, clazz, "<unknown>");
  }

  /**
   * Parses an XML file into an object of the specified class using Jackson's XmlMapper.
   *
   * @param <C> The type of the class to parse the XML into.
   * @param stream The InputStream for the XML file.
   * @param clazz The class to parse the XML into.
   * @param filePath The path of the file being parsed for error context.
   * @return A new instance of the specified class with the parsed XML data.
   * @throws IOException If an error occurs while reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IllegalArgumentException if stream or clazz is null
   */
  public static <C> C parseXmlToObject(InputStream stream, Class<C> clazz, String filePath)
      throws IOException, XMLStreamException {
    if (stream == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    if (clazz == null) {
      throw new IllegalArgumentException("Class cannot be null");
    }
    // Debug logging removed - parsing is a normal operation

    // Detect encoding
    EncodingDetector.EncodingAwareInputStream encodingAwareStream =
        EncodingDetector.detectEncoding(stream);

    try {
      XMLInputFactory factory = XMLInputFactory.newFactory();
      factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
      factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
      factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

      // Create a reader with detected encoding
      XMLStreamReader reader = factory.createXMLStreamReader(
          encodingAwareStream.inputStream(),
          encodingAwareStream
              .charset()
              .name()
      );

      XmlMapper xmlMapper = createConfiguredXmlMapper();
      return xmlMapper.readValue(reader, clazz);
    } catch (IOException | XMLStreamException e) {
      String errorMsg = String.format(
          "Failed to parse XML file '%s' to %s (encoding: %s): %s",
          filePath, clazz.getSimpleName(),
          encodingAwareStream
              .charset()
              .name(),
          e.getMessage()
      );

      // Wrap with more context
      if (e instanceof IOException) {
        throw new IOException(errorMsg, e);
      } else {
        throw new XMLStreamException(errorMsg, e);
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
      if (fileAccess.fileExists(metadataPath)) {
        try (InputStream fileContents = fileAccess.getFileContents(metadataPath)) {
          LOM lom = parseXmlToObject(fileContents, LOM.class, metadataPath);
          subMetadata.setLom(lom);
        } catch (IOException | XMLStreamException e) {
          String errorMsg = String.format(
              "Failed to load external metadata from '%s' in root '%s': %s",
              metadataPath, fileAccess.getRootPath(), e.getMessage()
          );

          // Re-throw with enhanced context
          if (e instanceof IOException) {
            throw new IOException(errorMsg, e);
          } else {
            throw new XMLStreamException(errorMsg, e);
          }
        }
      }
      // File not found is not an error - metadata is optional
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
      if (moduleFileProvider.fileExists(metadataPath)) {
        try (InputStream fileContents = moduleFileProvider.getFileContents(metadataPath)) {
          LOM lom = parseXmlToObject(fileContents, LOM.class, metadataPath);
          subMetadata.setLom(lom);
        } catch (IOException | XMLStreamException e) {
          String errorMsg = String.format(
              "Failed to load external metadata from '%s' in module at '%s': %s",
              metadataPath, moduleFileProvider.getRootPath(), e.getMessage()
          );

          // Re-throw with enhanced context
          if (e instanceof IOException) {
            throw new IOException(errorMsg, e);
          } else {
            throw new XMLStreamException(errorMsg, e);
          }
        }
      }
      // File not found is not an error - metadata is optional
    }
  }
}
