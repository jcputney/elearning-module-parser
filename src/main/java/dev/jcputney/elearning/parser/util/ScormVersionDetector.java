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

import static dev.jcputney.elearning.parser.enums.ModuleType.SCORM_12;
import static dev.jcputney.elearning.parser.enums.ModuleType.SCORM_2004;
import static dev.jcputney.elearning.parser.parsers.Scorm12Parser.MANIFEST_FILE;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility class for detecting the version of SCORM modules based on their manifest files.
 *
 * <p>This class examines the imsmanifest.xml file of a SCORM module to determine whether
 * it's a SCORM 1.2 or SCORM 2004 module. The detection is based on:
 * <ul>
 *   <li>The schema and schemaversion elements in the manifest</li>
 *   <li>The presence of SCORM 2004-specific namespaces</li>
 * </ul>
 *
 * <p>If the version can't be determined with certainty, the class defaults to SCORM 1.2.
 */
public final class ScormVersionDetector {

  /**
   * Private constructor to prevent instantiation.
   */
  private ScormVersionDetector() {
    // Prevent instantiation
  }

  /**
   * Detects the version of a SCORM module based on its manifest file.
   *
   * <p>This method examines the imsmanifest.xml file to determine whether the module
   * is SCORM 1.2 or SCORM 2004. It first checks the schema and schemaversion elements, and if those
   * are inconclusive, it looks for SCORM 2004-specific namespaces.
   *
   * @param fileAccess the FileAccess implementation to use for accessing the manifest file
   * @return the detected SCORM version as a {@link ModuleType} (SCORM_12 or SCORM_2004)
   * @throws ParserConfigurationException if a DocumentBuilder can't be created
   * @throws IOException if an I/O error occurs while reading the manifest file
   * @throws SAXException if a parsing error occurs while reading the manifest file
   */
  public static ModuleType detectScormVersion(FileAccess fileAccess)
      throws ParserConfigurationException, IOException, SAXException {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setExpandEntityReferences(false);
    byte[] manifestBytes = readManifestBytes(fileAccess);
    Document document = parseManifest(factory, manifestBytes);

    // Check schema version
    Element schemaElement = (Element) document
        .getElementsByTagName("schema")
        .item(0);
    Element versionElement = (Element) document
        .getElementsByTagName("schemaversion")
        .item(0);
    if (schemaElement != null && versionElement != null) {
      String schema = schemaElement.getTextContent();
      String version = versionElement.getTextContent();

      if ("ADL SCORM".equalsIgnoreCase(schema)) {
        if ("1.2".equalsIgnoreCase(version)) {
          return SCORM_12;
        } else if (version.startsWith("2004")) {
          return SCORM_2004;
        }
      }
    }

    // Check for SCORM 2004-specific namespaces (adlcp v1p3 for 3rd/4th, v1p2 for 2nd)
    String adlcpNs = document
        .getDocumentElement()
        .getAttribute("xmlns:adlcp");
    if (adlcpNs.contains("adlcp_v1p3") || adlcpNs.contains("adlcp_v1p2")) {
      return SCORM_2004;
    }

    // Default to SCORM 1.2 if uncertain
    return SCORM_12;
  }

  /**
   * Reads the bytes of the SCORM manifest file (imsmanifest.xml) using the provided
   * {@link FileAccess} implementation.
   *
   * @param fileAccess the FileAccess implementation used to retrieve the manifest file contents
   * @return a byte array containing the contents of the manifest file
   * @throws IOException if an I/O error occurs while reading the manifest file
   */
  private static byte[] readManifestBytes(FileAccess fileAccess) throws IOException {
    var files = fileAccess.listFiles("");
    String manifestFile = FileUtils.findFileIgnoreCase(files, MANIFEST_FILE);

    if (manifestFile == null) {
      throw new IOException("SCORM manifest file not found: " + MANIFEST_FILE);
    }

    try (InputStream inputStream = fileAccess.getFileContents(manifestFile)) {
      return inputStream.readAllBytes();
    }
  }

  /**
   * Parses the SCORM manifest file from the provided byte array using the given
   * DocumentBuilderFactory. If the default parsing fails and the error cause is related to
   * character encoding, this method attempts to parse using predefined fallback character sets.
   *
   * @param factory the DocumentBuilderFactory instance used for creating DocumentBuilder objects
   * @param manifestBytes the byte array containing the manifest file contents
   * @return a Document object representing the parsed SCORM manifest
   * @throws ParserConfigurationException if an error occurs while configuring the DocumentBuilder
   * @throws IOException if an I/O error occurs during the parsing process
   * @throws SAXException if a parsing error occurs or if all parsing attempts fail
   */
  private static Document parseManifest(DocumentBuilderFactory factory, byte[] manifestBytes)
      throws ParserConfigurationException, IOException, SAXException {
    try {
      return parseWithDefault(factory, manifestBytes);
    } catch (SAXException ex) {
      if (!hasCharConversionCause(ex)) {
        throw ex;
      }
    }

    SAXException lastFailure = null;
    for (Charset charset : fallbackCharsets()) {
      try {
        return parseWithCharset(factory, manifestBytes, charset);
      } catch (SAXException ex) {
        lastFailure = ex;
      }
    }

    if (lastFailure != null) {
      throw lastFailure;
    }
    throw new SAXException("Unable to parse manifest contents with available encodings");
  }

  /**
   * Parses an XML document from the provided byte array using the specified DocumentBuilderFactory.
   * This method defaults to using the standard parse mechanism for creating a Document object.
   *
   * @param factory the DocumentBuilderFactory instance used to create DocumentBuilder objects
   * @param manifestBytes the byte array containing the contents of the XML document to be parsed
   * @return a Document object representing the parsed XML document
   * @throws ParserConfigurationException if an error occurs while configuring the DocumentBuilder
   * @throws IOException if an I/O error occurs during the parsing process
   * @throws SAXException if a parsing error occurs while processing the XML document
   */
  private static Document parseWithDefault(DocumentBuilderFactory factory, byte[] manifestBytes)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder builder = createDocumentBuilder(factory);
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(manifestBytes)) {
      return builder.parse(inputStream);
    }
  }

  /**
   * Parses an XML document from the provided byte array using the specified DocumentBuilderFactory
   * and a given character set. This method allows parsing to account for the specified encoding.
   *
   * @param factory the DocumentBuilderFactory instance used to create DocumentBuilder objects
   * @param manifestBytes the byte array containing the contents of the XML document to be parsed
   * @param charset the character set to be used for decoding the byte array into a string
   * @return a Document object representing the parsed XML document
   * @throws ParserConfigurationException if an error occurs while configuring the DocumentBuilder
   * @throws IOException if an I/O error occurs during the parsing process
   * @throws SAXException if a parsing error occurs while processing the XML document
   */
  private static Document parseWithCharset(DocumentBuilderFactory factory, byte[] manifestBytes,
      Charset charset)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder builder = createDocumentBuilder(factory);
    InputSource source = new InputSource();
    source.setEncoding(charset.name());
    source.setCharacterStream(new java.io.StringReader(new String(manifestBytes, charset)));
    return builder.parse(source);
  }

  /**
   * Creates a new instance of a {@link DocumentBuilder} using the specified
   * {@link DocumentBuilderFactory}. The created builder utilizes a silent error handler to suppress
   * warnings during XML parsing.
   *
   * @param factory the {@link DocumentBuilderFactory} used to create the {@link DocumentBuilder}
   * @return a configured {@link DocumentBuilder} instance
   * @throws ParserConfigurationException if a {@link DocumentBuilder} cannot be created
   */
  private static DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory)
      throws ParserConfigurationException {
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setErrorHandler(SilentErrorHandler.INSTANCE);
    return builder;
  }

  /**
   * Checks if the given {@link Throwable} or any of its causes is an instance of
   * {@link CharConversionException}.
   *
   * @param throwable the throwable to check for the presence of a {@link CharConversionException}
   * as the cause or within its causal chain
   * @return {@code true} if the throwable or any cause in its chain is an instance of
   * {@link CharConversionException}, {@code false} otherwise
   */
  private static boolean hasCharConversionCause(Throwable throwable) {
    Throwable current = throwable;
    while (current != null) {
      if (current instanceof CharConversionException) {
        return true;
      }
      current = current.getCause();
    }
    return false;
  }

  /**
   * Provides a list of fallback character sets to be used for decoding when the default character
   * set is unsupported or results in errors.
   *
   * @return a list of fallback {@link Charset} objects, including ISO-8859-1 and Windows-1252, in
   * the order of preference.
   */
  private static List<Charset> fallbackCharsets() {
    return List.of(StandardCharsets.ISO_8859_1, Charset.forName("windows-1252"));
  }

  /**
   * SilentErrorHandler is a private singleton implementation of the {@link ErrorHandler} interface.
   * It is designed to handle XML parsing errors in a silent or controlled manner.
   * <p>
   * This error handler ignores warnings, but propagates errors and fatal errors by throwing the
   * corresponding {@link SAXParseException}. It is used in scenarios where suppressing warnings and
   * handling critical errors explicitly is desired, such as XML parsing during SCORM version
   * detection.
   * <p>
   * This class enforces the singleton pattern to ensure a single shared instance is used.
   */
  private static class SilentErrorHandler implements ErrorHandler {

    private static final SilentErrorHandler INSTANCE = new SilentErrorHandler();

    /**
     * Private constructor for the SilentErrorHandler singleton class. This constructor ensures that
     * the class cannot be instantiated externally. SilentErrorHandler is a singleton implementation
     * of the {@link ErrorHandler} interface, used for handling XML parsing errors in a controlled
     * and silent manner.
     */
    private SilentErrorHandler() {
      // singleton
    }

    /**
     * Handles XML parsing warnings during detection attempts. This method ignores all warnings,
     * allowing detection processes to proceed without interruption.
     *
     * @param exception the {@link SAXParseException} instance representing the XML parse warning.
     */
    @Override
    public void warning(SAXParseException exception) {
      // ignore warnings during detection attempts
    }

    /**
     * Handles XML parsing errors that occur during detection attempts. This method propagates the
     * error by throwing the provided {@link SAXParseException}.
     *
     * @param exception the {@link SAXParseException} instance representing the XML parse error.
     * @throws SAXException if an XML parse error occurs.
     */
    @Override
    public void error(SAXParseException exception) throws SAXException {
      throw exception;
    }

    /**
     * Handles XML parsing fatal errors that occur during detection attempts. This method propagates
     * the fatal error by throwing the provided {@link SAXParseException}.
     *
     * @param exception the {@link SAXParseException} instance representing the XML parse fatal
     * error.
     * @throws SAXException if a fatal XML parsing error occurs.
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
      throw exception;
    }
  }
}
