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
public class ScormVersionDetector {

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
    String adlcpNs = document.getDocumentElement().getAttribute("xmlns:adlcp");
    if (adlcpNs != null && (adlcpNs.contains("adlcp_v1p3") || adlcpNs.contains("adlcp_v1p2"))) {
      return SCORM_2004;
    }

    // Default to SCORM 1.2 if uncertain
    return SCORM_12;
  }

  private static byte[] readManifestBytes(FileAccess fileAccess) throws IOException {
    try (InputStream inputStream = fileAccess.getFileContents(MANIFEST_FILE)) {
      return inputStream.readAllBytes();
    }
  }

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

  private static Document parseWithDefault(DocumentBuilderFactory factory, byte[] manifestBytes)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder builder = createDocumentBuilder(factory);
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(manifestBytes)) {
      return builder.parse(inputStream);
    }
  }

  private static Document parseWithCharset(DocumentBuilderFactory factory, byte[] manifestBytes,
      Charset charset)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder builder = createDocumentBuilder(factory);
    InputSource source = new InputSource();
    source.setEncoding(charset.name());
    source.setCharacterStream(new java.io.StringReader(new String(manifestBytes, charset)));
    return builder.parse(source);
  }

  private static DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory)
      throws ParserConfigurationException {
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setErrorHandler(SilentErrorHandler.INSTANCE);
    return builder;
  }

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

  private static List<Charset> fallbackCharsets() {
    return List.of(StandardCharsets.ISO_8859_1, Charset.forName("windows-1252"));
  }

  private enum SilentErrorHandler implements ErrorHandler {
    INSTANCE;

    @Override
    public void warning(SAXParseException exception) {
      // ignore warnings during detection attempts
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
      throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
      throw exception;
    }
  }
}
