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
 * Validation is optional and disabled by default. Enable it by setting the
 * system property {@code elearning.parser.scorm2004.validateXsd=true} or the
 * environment variable {@code ELEARNING_SCORM2004_VALIDATE_XSD=true}.
 * </p>
 */
public final class Scorm2004SchemaValidator {

  private static final String VALIDATE_SYSPROP = "elearning.parser.scorm2004.validateXsd";
  private static final String VALIDATE_ENV = "ELEARNING_SCORM2004_VALIDATE_XSD";

  // Classpath locations for minimal SCORM 2004 XSDs (IMS CP only is sufficient for structure)
  private static final String[] SCHEMA_RESOURCE_PATHS = new String[] {
      // IMS Content Packaging 1.1 (root CP schema)
      "schemas/scorm2004/imscp_v1p1.xsd"
      // Note: imscp_v1p1 includes xml.xsd which in turn references XMLSchema.dtd.
  };

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
    return fromEnv != null && Boolean.parseBoolean(fromEnv);
  }

  /**
   * Validates the provided manifest XML bytes against local SCORM 2004 XSDs.
   *
   * @param manifestXml The XML document bytes
   * @throws SAXException when validation fails with details
   * @throws IOException when reading schema resources fails
   */
  public static void validate(byte[] manifestXml) throws SAXException, IOException {
    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

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
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
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
        sb.append(" - ").append(err).append('\n');
      }
      throw new SAXException(sb.toString());
    }
  }

  private static final class CollectingErrorHandler implements ErrorHandler {
    private final List<String> errors;

    private CollectingErrorHandler(List<String> errors) {
      this.errors = errors;
    }

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

    private String format(SAXParseException e) {
      String sysId = e.getSystemId() != null ? e.getSystemId() : "<unknown>";
      return String.format("%s:%d:%d: %s", sysId, e.getLineNumber(), e.getColumnNumber(), e.getMessage());
    }
  }
}

