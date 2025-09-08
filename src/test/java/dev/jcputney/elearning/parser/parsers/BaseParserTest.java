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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for BaseParser.
 */
class BaseParserTest {

  @Mock
  private ModuleFileProvider mockModuleFileProvider;

  @Mock
  private FileAccess mockFileAccess;

  private TestableBaseParser parser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    parser = new TestableBaseParser(mockModuleFileProvider);
  }

  @Test
  void testConstructor_WithModuleFileProvider_NullProvider_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new TestableBaseParser((ModuleFileProvider) null));
    assertEquals("ModuleFileProvider cannot be null", exception.getMessage());
  }

  @Test
  void testConstructor_WithFileAccess_NullFileAccess_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new TestableBaseParser((FileAccess) null));
    assertEquals("FileAccess cannot be null", exception.getMessage());
  }

  @Test
  void testConstructor_WithFileAccess_ValidFileAccess_Success() {
    TestableBaseParser parser = new TestableBaseParser(mockFileAccess);
    assertNotNull(parser);
    assertNotNull(parser.moduleFileProvider);
  }

  @Test
  void testParseManifest_NullPath_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> parser.parseManifest(null));
    assertEquals("Manifest path cannot be null", exception.getMessage());
    verifyNoInteractions(mockModuleFileProvider);
  }

  @Test
  void testParseManifest_ValidPath_Success() throws Exception {
    // Arrange
    String manifestPath = "imsmanifest.xml";
    String xmlContent = "<?xml version=\"1.0\"?><root><test>value</test></root>";
    InputStream manifestStream = new ByteArrayInputStream(xmlContent.getBytes());

    when(mockModuleFileProvider.getFileContents(manifestPath)).thenReturn(manifestStream);

    // Act
    TestManifest result = parser.parseManifest(manifestPath);

    // Assert
    assertNotNull(result);
    verify(mockModuleFileProvider).getFileContents(manifestPath);
    assertTrue(parser.loadExternalMetadataCalled);
    assertEquals(result, parser.lastManifestParsed);
  }

  @Test
  void testParseManifest_IOExceptionInFileContents_PropagatesException() throws Exception {
    // Arrange
    String manifestPath = "imsmanifest.xml";
    IOException expectedException = new IOException("File not found");
    when(mockModuleFileProvider.getFileContents(manifestPath)).thenThrow(expectedException);

    // Act & Assert
    ModuleParsingException actualException = assertThrows(ModuleParsingException.class,
        () -> parser.parseManifest(manifestPath));
    assertEquals("Failed to read manifest file 'imsmanifest.xml': File not found",
        actualException.getMessage());
    assertSame(expectedException, actualException.getCause());
    verify(mockModuleFileProvider).getFileContents(manifestPath);
    assertFalse(parser.loadExternalMetadataCalled);
  }

  @Test
  void testParseManifest_XMLStreamExceptionInParsing_PropagatesException() throws Exception {
    // Arrange
    String manifestPath = "imsmanifest.xml";
    String invalidXml = "invalid xml content";
    InputStream manifestStream = new ByteArrayInputStream(invalidXml.getBytes());

    when(mockModuleFileProvider.getFileContents(manifestPath)).thenReturn(manifestStream);

    // Act & Assert
    assertThrows(ModuleParsingException.class, () -> parser.parseManifest(manifestPath));
    verify(mockModuleFileProvider).getFileContents(manifestPath);
    assertFalse(parser.loadExternalMetadataCalled);
  }

  @Test
  void testParseManifest_ExceptionInLoadExternalMetadata_PropagatesException() throws Exception {
    // Arrange
    String manifestPath = "imsmanifest.xml";
    String xmlContent = "<?xml version=\"1.0\"?><root><test>value</test></root>";
    InputStream manifestStream = new ByteArrayInputStream(xmlContent.getBytes());

    when(mockModuleFileProvider.getFileContents(manifestPath)).thenReturn(manifestStream);
    parser.shouldThrowInLoadExternalMetadata = true;

    // Act & Assert
    ModuleParsingException exception = assertThrows(ModuleParsingException.class,
        () -> parser.parseManifest(manifestPath));
    assertEquals("Failed to read manifest file 'imsmanifest.xml': External metadata loading failed",
        exception.getMessage());
    verify(mockModuleFileProvider).getFileContents(manifestPath);
    assertTrue(parser.loadExternalMetadataCalled);
  }

  @Test
  void testCheckForXapi_DelegatesToModuleFileProvider() {
    // Arrange
    when(mockModuleFileProvider.hasXapiSupport()).thenReturn(true);

    // Act
    boolean result = parser.testCheckForXapi();

    // Assert
    assertTrue(result);
    verify(mockModuleFileProvider).hasXapiSupport();
  }

  @Test
  void testCheckForXapi_NoXapiSupport_ReturnsFalse() {
    // Arrange
    when(mockModuleFileProvider.hasXapiSupport()).thenReturn(false);

    // Act
    boolean result = parser.testCheckForXapi();

    // Assert
    assertFalse(result);
    verify(mockModuleFileProvider).hasXapiSupport();
  }

  @Test
  void testParseXmlToObject_ValidXml_Success() throws Exception {
    // Arrange
    String xmlContent = "<?xml version=\"1.0\"?><root><test>value</test></root>";
    InputStream stream = new ByteArrayInputStream(xmlContent.getBytes());

    // Act
    TestManifest result = parser.testParseXmlToObject(stream, TestManifest.class);

    // Assert
    assertNotNull(result);
  }

  @Test
  void testParseXmlToObject_InvalidXml_ThrowsException() {
    // Arrange
    String invalidXml = "invalid xml content";
    InputStream stream = new ByteArrayInputStream(invalidXml.getBytes());

    // Act & Assert
    assertThrows(IOException.class, () -> parser.testParseXmlToObject(stream, TestManifest.class));
  }

  @Test
  void testLoadExternalMetadataIntoMetadata_NullMetadata_DoesNothing() throws Exception {
    // Act - should not throw exception
    parser.testLoadExternalMetadataIntoMetadata(null);

    // Assert - no interactions with moduleFileProvider expected
    verifyNoInteractions(mockModuleFileProvider);
  }

  @Test
  void testLoadExternalMetadataIntoMetadata_ValidMetadata_CallsUtils() throws Exception {
    // Arrange
    LoadableMetadata mockMetadata = mock(LoadableMetadata.class);
    when(mockMetadata.getLocation()).thenReturn("metadata.xml");
    when(mockModuleFileProvider.fileExists("metadata.xml")).thenReturn(false);

    // Act
    parser.testLoadExternalMetadataIntoMetadata(mockMetadata);

    // Assert - XmlParsingUtils should be called internally
    // Since we can't easily mock static methods, we verify the flow doesn't throw
    verify(mockMetadata, atLeastOnce()).getLocation();
  }

  @Test
  void testConstants_AreCorrect() {
    assertEquals("xAPI.js", BaseParser.XAPI_JS_FILE);
    assertEquals("sendStatement.js", BaseParser.XAPI_SEND_STATEMENT_FILE);
  }

  /**
   * Concrete implementation of BaseParser for testing purposes.
   */
  private static class TestableBaseParser extends BaseParser<TestModuleMetadata, TestManifest> {

    boolean loadExternalMetadataCalled = false;
    boolean shouldThrowInLoadExternalMetadata = false;
    TestManifest lastManifestParsed = null;

    public TestableBaseParser(ModuleFileProvider moduleFileProvider) {
      super(moduleFileProvider);
    }

    public TestableBaseParser(FileAccess fileAccess) {
      super(fileAccess);
    }

    @Override
    public TestModuleMetadata parse() {
      return new TestModuleMetadata();
    }

    // Expose protected methods for testing
    public boolean testCheckForXapi() {
      return checkForXapi();
    }

    public <C> C testParseXmlToObject(InputStream stream, Class<C> clazz)
        throws IOException, XMLStreamException {
      return parseXmlToObject(stream, clazz);
    }

    public void testLoadExternalMetadataIntoMetadata(LoadableMetadata subMetadata)
        throws XMLStreamException, IOException {
      loadExternalMetadataIntoMetadata(subMetadata);
    }

    @Override
    void loadExternalMetadata(TestManifest manifest) throws IOException {
      loadExternalMetadataCalled = true;
      lastManifestParsed = manifest;
      if (shouldThrowInLoadExternalMetadata) {
        throw new IOException("External metadata loading failed");
      }
    }

    @Override
    protected Class<TestManifest> getManifestClass() {
      return TestManifest.class;
    }
  }

  /**
   * Test manifest class for testing purposes.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JacksonXmlRootElement(localName = "root")
  public static class TestManifest implements PackageManifest {

    private String test;

    @JacksonXmlProperty(localName = "test")
    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }

    @Override
    public String getTitle() {
      return null;
    }

    @Override
    public String getDescription() {
      return null;
    }

    @Override
    public String getLaunchUrl() {
      return null;
    }

    @Override
    public String getIdentifier() {
      return null;
    }

    @Override
    public String getVersion() {
      return null;
    }

    @Override
    public Duration getDuration() {
      return null;
    }
  }

  /**
   * Test metadata class for testing purposes.
   */
  public static class TestModuleMetadata extends ModuleMetadata<TestManifest> {

    @Override
    public TestManifest getManifest() {
      return new TestManifest();
    }
  }
}