/* Copyright (c) 2024-2025. Jonathan Putney
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.input.lom.LOM;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link XmlParsingUtils} class.
 */
class XmlParsingUtilsTest {

  public static final String ROOT_PATH = "root/path";
  public static final String METADATA_XML = "metadata.xml";

  @Test
  void parseXmlToObjectWithValidXmlReturnsObject() throws Exception {
    String xml = "<TestXmlClass><name>Test</name><value>42</value></TestXmlClass>";
    InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

    TestXmlClass result = XmlParsingUtils.parseXmlToObject(stream, TestXmlClass.class);

    assertEquals("Test", result.getName());
    assertEquals(42, result.getValue());
  }

  @Test
  void parseXmlToObjectWithBareAmpersandSanitizesAndParses() throws Exception {
    String xml = "<TextXmlClass><value>Health & Safety</value></TextXmlClass>";
    InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

    TextXmlClass result = XmlParsingUtils.parseXmlToObject(stream, TextXmlClass.class);

    assertEquals("Health & Safety", result.getValue());
  }

  @Test
  void parseXmlToObjectWithNullInputStreamThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> XmlParsingUtils.parseXmlToObject(null, TestXmlClass.class));
  }

  @Test
  void parseXmlToObjectWithNullClassThrowsIllegalArgumentException() {
    String xml = "<TestXmlClass><name>Test</name><value>42</value></TestXmlClass>";
    InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

    assertThrows(IllegalArgumentException.class,
        () -> XmlParsingUtils.parseXmlToObject(stream, null));
  }

  @Test
  void loadExternalMetadataIntoMetadataWithFileAccessNullLoadableMetadataThrowsIllegalArgumentException() {
    MockFileAccess fileAccess = new MockFileAccess(ROOT_PATH);

    assertThrows(IllegalArgumentException.class,
        () -> XmlParsingUtils.loadExternalMetadataIntoMetadata(null, fileAccess));
  }

  @Test
  void loadExternalMetadataIntoMetadataWithFileAccessNullFileAccessThrowsIllegalArgumentException() {
    MockLoadableMetadata metadata = new MockLoadableMetadata(METADATA_XML);

    assertThrows(IllegalArgumentException.class,
        () -> XmlParsingUtils.loadExternalMetadataIntoMetadata(metadata, (FileAccess) null));
  }

  @Test
  void loadExternalMetadataIntoMetadataWithFileAccessValidFileSetsLom() throws Exception {
    MockLoadableMetadata metadata = new MockLoadableMetadata(METADATA_XML);
    MockFileAccess fileAccess = new MockFileAccess(ROOT_PATH);
    fileAccess.setFileExists(METADATA_XML, true);
    fileAccess.setFileContents(METADATA_XML, "<lom></lom>");

    XmlParsingUtils.loadExternalMetadataIntoMetadata(metadata, fileAccess);

    assertNotNull(metadata.getLom());
  }

  @Test
  void loadExternalMetadataIntoMetadataWithModuleFileProviderNullLoadableMetadataThrowsIllegalArgumentException() {
    MockModuleFileProvider moduleFileProvider = new MockModuleFileProvider(ROOT_PATH);

    assertThrows(IllegalArgumentException.class,
        () -> XmlParsingUtils.loadExternalMetadataIntoMetadata(null, moduleFileProvider));
  }

  @Test
  void loadExternalMetadataIntoMetadataWithModuleFileProviderNullModuleFileProviderThrowsIllegalArgumentException() {
    MockLoadableMetadata metadata = new MockLoadableMetadata(METADATA_XML);

    assertThrows(IllegalArgumentException.class,
        () -> XmlParsingUtils.loadExternalMetadataIntoMetadata(metadata,
            (ModuleFileProvider) null));
  }

  @Test
  void loadExternalMetadataIntoMetadataWithModuleFileProviderValidFileSetsLom()
      throws Exception {
    MockLoadableMetadata metadata = new MockLoadableMetadata(METADATA_XML);
    MockModuleFileProvider moduleFileProvider = new MockModuleFileProvider(ROOT_PATH);
    moduleFileProvider.setFileExists(METADATA_XML, true);
    moduleFileProvider.setFileContents(METADATA_XML, "<lom></lom>");

    XmlParsingUtils.loadExternalMetadataIntoMetadata(metadata, moduleFileProvider);

    assertNotNull(metadata.getLom());
  }

  /**
   * A simple test class for XML parsing.
   */
  public static class TestXmlClass {

    private String name;
    private int value;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }
  }

  /**
   * A simple test class for XML parsing where text should be preserved.
   */
  public static class TextXmlClass {

    private String value;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  /**
   * A mock implementation of {@link LoadableMetadata} for testing.
   */
  private static class MockLoadableMetadata implements LoadableMetadata {

    private final String location;
    private LOM lom;

    public MockLoadableMetadata(String location) {
      this.location = location;
    }

    @Override
    public String getLocation() {
      return location;
    }

    @Override
    public LOM getLom() {
      return lom;
    }

    @Override
    public void setLom(LOM lom) {
      this.lom = lom;
    }
  }

  /**
   * A mock implementation of {@link FileAccess} for testing.
   */
  private static class MockFileAccess implements FileAccess {

    private final String rootPath;
    private final Map<String, Boolean> fileExistsResponses = new HashMap<>();
    private final Map<String, String> fileContents = new HashMap<>();

    public MockFileAccess(String rootPath) {
      this.rootPath = rootPath;
    }

    public void setFileExists(String path, boolean exists) {
      fileExistsResponses.put(path, exists);
    }

    public void setFileContents(String path, String contents) {
      fileContents.put(path, contents);
    }

    @Override
    public String getRootPath() {
      return rootPath;
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return fileExistsResponses.getOrDefault(path, false);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      return Collections.emptyList();
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      String contents = fileContents.getOrDefault(path, "");
      return new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
    }
  }

  /**
   * A mock implementation of {@link ModuleFileProvider} for testing.
   */
  private static class MockModuleFileProvider implements ModuleFileProvider {

    private final String rootPath;
    private final Map<String, Boolean> fileExistsResponses = new HashMap<>();
    private final Map<String, String> fileContents = new HashMap<>();
    private boolean hasXapiSupport;

    public MockModuleFileProvider(String rootPath) {
      this.rootPath = rootPath;
    }

    public void setFileExists(String path, boolean exists) {
      fileExistsResponses.put(path, exists);
    }

    public void setFileContents(String path, String contents) {
      fileContents.put(path, contents);
    }

    public void setHasXapiSupport(boolean hasXapiSupport) {
      this.hasXapiSupport = hasXapiSupport;
    }

    @Override
    public InputStream getFileContents(String path) throws IOException {
      String contents = fileContents.getOrDefault(path, "");
      return new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean fileExists(String path) {
      return fileExistsResponses.getOrDefault(path, false);
    }

    @Override
    public String getRootPath() {
      return rootPath;
    }

    @Override
    public boolean hasXapiSupport() {
      return hasXapiSupport;
    }

    @Override
    public List<String> listFiles(String directory) throws IOException {
      return Collections.emptyList();
    }
  }
}
