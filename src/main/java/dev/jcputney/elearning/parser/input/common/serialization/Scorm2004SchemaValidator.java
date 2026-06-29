/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.input.common.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
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
   * Classpath directory holding the bundled SCORM 2004 schema set. Every schema referenced via
   * {@code import}/{@code include} (and the W3C {@code xml.xsd}) lives flat in this directory.
   */
  private static final String SCHEMA_DIR = "schemas/scorm2004/";

  /**
   * Resource paths to the root SCORM 2004 schema definition (XSD) files, composed together into a
   * single {@link Schema}.
   * <p>
   * A conformant SCORM 2004 manifest mixes five namespaces: IMS Content Packaging (the
   * {@code <manifest>} root, {@code imscp_v1p1.xsd}) plus the ADL and IMS Simple Sequencing
   * extensions that supply attributes/elements such as {@code adlcp:scormType},
   * {@code adlseq:objectivesGlobalToSystem}, {@code adlnav:presentation}, and
   * {@code imsss:sequencing}. The IMS CP schema validates these via {@code <xsd:anyAttribute>}/
   * {@code <xsd:any namespace="##other">} wildcards, some with {@code processContents="strict"}, so
   * every extension namespace must be present in the same compiled schema or validation rejects the
   * attribute (e.g. "Attribute 'adlcp:scormType' is not allowed to appear in element 'resource'").
   * All five roots have distinct target namespaces and do not import one another, so they compose
   * cleanly. Their transitive dependencies ({@code xml.xsd}, the {@code imsss_v1p0*} includes) are
   * resolved to bundled resources by {@link ClasspathResourceResolver}.
   * <p>
   * This constant is intended for internal use within validation logic and should not be modified
   * to preserve the integrity of the validation process.
   */
  private static final String[] SCHEMA_RESOURCE_PATHS = new String[]{
      SCHEMA_DIR + "imscp_v1p1.xsd",   // IMS Content Packaging 1.1 (root CP schema, <manifest>)
      SCHEMA_DIR + "adlcp_v1p3.xsd",   // ADL Content Packaging (adlcp:scormType, completionThreshold)
      SCHEMA_DIR + "adlseq_v1p3.xsd",  // ADL Sequencing extensions
      SCHEMA_DIR + "adlnav_v1p3.xsd",  // ADL Navigation (adlnav:presentation)
      SCHEMA_DIR + "imsss_v1p0.xsd"    // IMS Simple Sequencing (imsss:sequencing + includes)
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
   * Determines whether validation is enabled based on system properties or environment variables.
   * <p>
   * The method first checks the system property corresponding to {@code VALIDATE_SYSPROP}. If a
   * value is found, it converts it to a boolean and returns the result. If the system property is
   * not set, it then checks the environment variable corresponding to {@code VALIDATE_ENV} and
   * converts it to a boolean.
   *
   * @return {@code true} if validation is enabled based on the corresponding system property or
   * environment variable; otherwise {@code false}.
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

    // Load schemas from classpath with proper systemId so relative includes resolve.
    // Prefer the thread context class loader so consumers can supply custom classloaders
    // (e.g. OSGi, app servers), but fall back to this class's own loader when the TCCL
    // is null (can happen in ForkJoinPool threads, GraalVM native images, etc.).
    ClassLoader cl = Thread
        .currentThread()
        .getContextClassLoader();
    if (cl == null) {
      cl = Scorm2004SchemaValidator.class.getClassLoader();
    }

    // Resolve every import/include (and the W3C xml.xsd) to a bundled classpath resource so schema
    // assembly never touches the network, regardless of relative or absolute schemaLocation.
    factory.setResourceResolver(new ClasspathResourceResolver(cl));

    List<Source> sources = new ArrayList<>();
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

    /**
     * Handles warning-level SAX parse exceptions encountered during XML processing. Warnings are
     * treated as informational messages and are not collected.
     *
     * @param exception the {@link SAXParseException} containing details of the warning
     */
    @Override
    public void warning(SAXParseException exception) {
      // treat warnings as info; do not collect
    }

    /**
     * Handles error-level SAX parse exceptions encountered during XML processing. Errors are
     * collected and formatted into a standardized string representation before being added to the
     * internal list.
     *
     * @param exception the {@link SAXParseException} containing details of the error
     */
    @Override
    public void error(SAXParseException exception) {
      errors.add(format(exception));
    }

    /**
     * Handles fatal error-level SAX parse exceptions encountered during XML processing. Fatal
     * errors are collected and formatted into a standardized string representation before being
     * added to the internal list.
     *
     * @param exception the {@link SAXParseException} containing details of the fatal error
     */
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

  /**
   * Resolves schema {@code import}/{@code include} references to the bundled classpath resources in
   * {@link #SCHEMA_DIR}. Resolution is by file name (the last path segment of the requested
   * {@code systemId}), which covers both relative references (e.g. {@code imsss_v1p0util.xsd}) and
   * absolute ones (e.g. {@code http://www.w3.org/2001/xml.xsd} -> bundled {@code xml.xsd}). Anything
   * not present in the bundle resolves to {@code null}, letting the default machinery handle it (or
   * fail loudly) rather than reaching out to the network.
   */
  private record ClasspathResourceResolver(ClassLoader classLoader) implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId,
        String systemId, String baseURI) {
      if (systemId == null) {
        return null;
      }
      String fileName = systemId.substring(systemId.lastIndexOf('/') + 1);
      if (fileName.isEmpty()) {
        return null;
      }
      URL url = classLoader.getResource(SCHEMA_DIR + fileName);
      if (url == null) {
        return null;
      }
      try {
        return new ClasspathLsInput(publicId, url.toExternalForm(), baseURI, url.openStream());
      } catch (IOException e) {
        return null;
      }
    }
  }

  /**
   * Minimal {@link LSInput} backed by a byte stream from a bundled schema resource. Only the fields
   * the JAXP schema loader consults (byte stream, system/public id, base URI) are meaningful; the
   * remaining accessors are no-op stubs required by the interface.
   */
  private static final class ClasspathLsInput implements LSInput {

    private String publicId;
    private String systemId;
    private String baseURI;
    private InputStream byteStream;
    private String encoding;
    private boolean certifiedText;

    private ClasspathLsInput(String publicId, String systemId, String baseURI,
        InputStream byteStream) {
      this.publicId = publicId;
      this.systemId = systemId;
      this.baseURI = baseURI;
      this.byteStream = byteStream;
    }

    @Override
    public InputStream getByteStream() {
      return byteStream;
    }

    @Override
    public void setByteStream(InputStream byteStream) {
      this.byteStream = byteStream;
    }

    @Override
    public Reader getCharacterStream() {
      return null;
    }

    @Override
    public void setCharacterStream(Reader characterStream) {
      // Not used; schemas are supplied as byte streams.
    }

    @Override
    public String getStringData() {
      return null;
    }

    @Override
    public void setStringData(String stringData) {
      // Not used; schemas are supplied as byte streams.
    }

    @Override
    public String getSystemId() {
      return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
      this.systemId = systemId;
    }

    @Override
    public String getPublicId() {
      return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
      this.publicId = publicId;
    }

    @Override
    public String getBaseURI() {
      return baseURI;
    }

    @Override
    public void setBaseURI(String baseURI) {
      this.baseURI = baseURI;
    }

    @Override
    public String getEncoding() {
      return encoding;
    }

    @Override
    public void setEncoding(String encoding) {
      this.encoding = encoding;
    }

    @Override
    public boolean getCertifiedText() {
      return certifiedText;
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
      this.certifiedText = certifiedText;
    }
  }
}

