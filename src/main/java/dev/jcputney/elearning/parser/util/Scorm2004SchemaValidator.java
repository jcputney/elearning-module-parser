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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility for validating SCORM 2004 imsmanifest.xml against local XSDs.
 * <p>
 * Validation is optional and disabled by default. Enable it by setting the system property
 * {@code elearning.parser.scorm2004.validateXsd=true} or the environment variable
 * {@code ELEARNING_SCORM2004_VALIDATE_XSD=true}.
 * </p>
 */
public final class Scorm2004SchemaValidator {

  /**
   * System property key used to determine whether SCORM 2004 XSD validation should be enabled.
   * <p>
   * This property is checked by the Scorm2004SchemaValidator class to decide if validation against
   * SCORM 2004 XML schemas is required. The value of this property, if set, typically needs to be
   * "true" (case-insensitive) to enable validation. If not set or set to any other value,
   * validation may be disabled by default.
   */
  private static final String VALIDATE_SYSPROP = "elearning.parser.scorm2004.validateXsd";

  /**
   * Environment variable key used to enable SCORM 2004 XSD validation.
   * <p>
   * This variable specifies whether the SCORM 2004 manifest validation against its corresponding
   * XSDs is enabled. When set, it indicates that validation should be performed during SCORM
   * manifest processing.
   * <p>
   * Typically, this environment variable is used in combination with related system properties to
   * configure validation behavior dynamically.
   */
  private static final String VALIDATE_ENV = "ELEARNING_SCORM2004_VALIDATE_XSD";

  /**
   * Array of resource paths to SCORM 2004 schema definition (XSD) files.
   * <p>
   * This variable holds the paths to the XSD schema resources required for validating SCORM 2004
   * manifests. The main schema file included is the IMS Content Packaging 1.1 schema
   * (imscp_v1p1.xsd), which serves as the root schema.
   * <p>
   * The referenced schema files facilitate the XML validation process, ensuring conformance to
   * SCORM 2004 standards. The `imscp_v1p1.xsd` schema further includes dependencies such as
   * `xml.xsd`, which may reference additional resources like `XMLSchema.dtd`.
   * <p>
   * This constant is intended for internal use within validation logic and should not be modified
   * to preserve the integrity of the validation process.
   */
  private static final String[] SCHEMA_RESOURCE_PATHS = new String[]{
      // IMS Content Packaging 1.1 (root CP schema)
      "schemas/scorm2004/imscp_v1p1.xsd"
      // Note: imscp_v1p1 includes xml.xsd which in turn references XMLSchema.dtd.
  };

  /**
   * Private constructor to prevent instantiation of the utility class.
   * <p>
   * The Scorm2004SchemaValidator class is designed to provide static methods for validating SCORM
   * 2004 manifests and checking if validation is enabled. It should not be instantiated, as its
   * functionality is only accessible through the static methods provided.
   * <p>
   * Attempting to instantiate this class will result in an {@link AssertionError}.
   */
  private Scorm2004SchemaValidator() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Returns true if SCORM 2004 XSD validation is enabled via system property or environment.
   */
  public static boolean isEnabled() {
    String fromSysProp = System.getProperty(VALIDATE_SYSPROP);
    if (fromSysProp != null) {
      return Boolean.parseBoolean(fromSysProp);
    }
    String fromEnv = System.getenv(VALIDATE_ENV);
    return Boolean.parseBoolean(fromEnv);
  }

  /**
   * Validates the provided manifest XML bytes against local SCORM 2004 XSDs.
   *
   * @param manifestXml The XML document bytes
   * @throws SAXException when validation fails with details
   * @throws IOException when reading schema resources fails
   */
  public static void validate(byte[] manifestXml) throws SAXException, IOException {
    SchemaFactory factory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    // Harden resolver settings while allowing local classpath URLs (file/jar)
    try {
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    } catch (Exception ignored) {
    }
    try {
      factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file,jar");
      factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "file,jar");
    } catch (Exception ignored) {
    }

    // Load schemas from classpath with proper systemId so relative includes resolve
    List<Source> sources = new ArrayList<>();
    ClassLoader cl = Thread
        .currentThread()
        .getContextClassLoader();
    for (String path : SCHEMA_RESOURCE_PATHS) {
      URL url = cl.getResource(path);
      if (url == null) {
        throw new IOException("Missing schema resource: " + path);
      }
      sources.add(new StreamSource(url.openStream(), url.toExternalForm()));
    }

    Schema schema = factory.newSchema(sources.toArray(Source[]::new));
    Validator validator = schema.newValidator();

    List<String> errors = new ArrayList<>();
    validator.setErrorHandler(new CollectingErrorHandler(errors));
    try {
      validator.validate(new StreamSource(new ByteArrayInputStream(manifestXml)));
    } catch (SAXException e) {
      // Fall through to aggregate message below
    }

    if (!errors.isEmpty()) {
      StringBuilder sb = new StringBuilder("XSD validation failed (SCORM 2004):\n");
      for (String err : errors) {
        sb
            .append(" - ")
            .append(err)
            .append('\n');
      }
      throw new SAXException(sb.toString());
    }
  }

  /**
   * An implementation of the {@link ErrorHandler} interface that collects error and fatal error
   * messages encountered during XML processing.
   * <p>
   * This handler ignores warnings and formats error messages into a standardized string
   * representation, including system ID, line number, column number, and error message. All
   * collected error messages are stored in the provided list.
   */
  private record CollectingErrorHandler(List<String> errors) implements ErrorHandler {

    @Override
    public void warning(SAXParseException exception) {
      // treat warnings as info; do not collect
    }

    @Override
    public void error(SAXParseException exception) {
      errors.add(format(exception));
    }

    @Override
    public void fatalError(SAXParseException exception) {
      errors.add(format(exception));
    }

    /**
     * Formats the details of a {@link SAXParseException} into a standardized string representation.
     * The formatted string includes the system ID, line number, column number, and error message.
     *
     * @param e the {@link SAXParseException} containing details of the XML processing error
     * @return a formatted string representing the details of the given exception
     */
    private String format(SAXParseException e) {
      String sysId = e.getSystemId() != null ? e.getSystemId() : "<unknown>";
      return String.format("%s:%d:%d: %s", sysId, e.getLineNumber(), e.getColumnNumber(),
          e.getMessage());
    }
  }
}

