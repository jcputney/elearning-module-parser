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

package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DefaultModuleFileProvider.
 */
class DefaultModuleFileProviderTest {

  @Mock
  private FileAccess mockFileAccess;

  private DefaultModuleFileProvider fileProvider;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    fileProvider = new DefaultModuleFileProvider(mockFileAccess);
  }

  @Test
  void testConstructor_NullFileAccess_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      new DefaultModuleFileProvider(null);
    });
    assertEquals("FileAccess cannot be null", exception.getMessage());
  }

  @Test
  void testConstructor_ValidFileAccess_Success() {
    assertNotNull(fileProvider);
  }

  @Test
  void testGetFileContents_ValidPath_DelegatesToFileAccess() throws IOException {
    // Arrange
    String testPath = "test/path.txt";
    InputStream expectedStream = new ByteArrayInputStream("test content".getBytes());
    when(mockFileAccess.getFileContents(testPath)).thenReturn(expectedStream);

    // Act
    InputStream result = fileProvider.getFileContents(testPath);

    // Assert
    assertSame(expectedStream, result);
    verify(mockFileAccess).getFileContents(testPath);
  }

  @Test
  void testGetFileContents_NullPath_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      fileProvider.getFileContents(null);
    });
    assertEquals("Path cannot be null", exception.getMessage());
    verifyNoInteractions(mockFileAccess);
  }

  @Test
  void testGetFileContents_FileAccessThrowsException_PropagatesException() throws IOException {
    // Arrange
    String testPath = "nonexistent.txt";
    IOException expectedException = new IOException("File not found");
    when(mockFileAccess.getFileContents(testPath)).thenThrow(expectedException);

    // Act & Assert
    IOException actualException = assertThrows(IOException.class, () -> {
      fileProvider.getFileContents(testPath);
    });
    assertSame(expectedException, actualException);
    verify(mockFileAccess).getFileContents(testPath);
  }

  @Test
  void testFileExists_ValidPath_DelegatesToFileAccess() {
    // Arrange
    String testPath = "existing-file.txt";
    when(mockFileAccess.fileExists(testPath)).thenReturn(true);

    // Act
    boolean result = fileProvider.fileExists(testPath);

    // Assert
    assertTrue(result);
    verify(mockFileAccess).fileExists(testPath);
  }

  @Test
  void testFileExists_NullPath_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      fileProvider.fileExists(null);
    });
    assertEquals("Path cannot be null", exception.getMessage());
    verifyNoInteractions(mockFileAccess);
  }

  @Test
  void testFileExists_FileDoesNotExist_ReturnsFalse() {
    // Arrange
    String testPath = "nonexistent.txt";
    when(mockFileAccess.fileExists(testPath)).thenReturn(false);

    // Act
    boolean result = fileProvider.fileExists(testPath);

    // Assert
    assertFalse(result);
    verify(mockFileAccess).fileExists(testPath);
  }

  @Test
  void testGetRootPath_DelegatesToFileAccess() {
    // Arrange
    String expectedRootPath = "/path/to/module";
    when(mockFileAccess.getRootPath()).thenReturn(expectedRootPath);

    // Act
    String result = fileProvider.getRootPath();

    // Assert
    assertEquals(expectedRootPath, result);
    verify(mockFileAccess).getRootPath();
  }

  @Test
  void testListFiles_ValidDirectory_DelegatesToFileAccess() throws IOException {
    // Arrange
    String testDirectory = "test-dir";
    List<String> expectedFiles = Arrays.asList("file1.txt", "file2.txt", "file3.txt");
    when(mockFileAccess.listFiles(testDirectory)).thenReturn(expectedFiles);

    // Act
    List<String> result = fileProvider.listFiles(testDirectory);

    // Assert
    assertEquals(expectedFiles, result);
    verify(mockFileAccess).listFiles(testDirectory);
  }

  @Test
  void testListFiles_NullDirectory_ThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      fileProvider.listFiles(null);
    });
    assertEquals("Directory cannot be null", exception.getMessage());
    verifyNoInteractions(mockFileAccess);
  }

  @Test
  void testListFiles_FileAccessThrowsException_PropagatesException() throws IOException {
    // Arrange
    String testDirectory = "error-dir";
    IOException expectedException = new IOException("Directory not found");
    when(mockFileAccess.listFiles(testDirectory)).thenThrow(expectedException);

    // Act & Assert
    IOException actualException = assertThrows(IOException.class, () -> {
      fileProvider.listFiles(testDirectory);
    });
    assertSame(expectedException, actualException);
    verify(mockFileAccess).listFiles(testDirectory);
  }

  @Test
  void testHasXapiSupport_BothFilesExist_ReturnsTrue() {
    // Arrange
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_JS_FILE)).thenReturn(true);
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE)).thenReturn(true);

    // Act
    boolean result = fileProvider.hasXapiSupport();

    // Assert
    assertTrue(result);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_JS_FILE);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }

  @Test
  void testHasXapiSupport_OnlyXapiJsExists_ReturnsTrue() {
    // Arrange
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_JS_FILE)).thenReturn(true);
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE)).thenReturn(false);

    // Act
    boolean result = fileProvider.hasXapiSupport();

    // Assert
    assertTrue(result);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_JS_FILE);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }

  @Test
  void testHasXapiSupport_OnlySendStatementExists_ReturnsTrue() {
    // Arrange
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_JS_FILE)).thenReturn(false);
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE)).thenReturn(true);

    // Act
    boolean result = fileProvider.hasXapiSupport();

    // Assert
    assertTrue(result);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_JS_FILE);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }

  @Test
  void testHasXapiSupport_NoXapiFiles_ReturnsFalse() {
    // Arrange
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_JS_FILE)).thenReturn(false);
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE)).thenReturn(false);

    // Act
    boolean result = fileProvider.hasXapiSupport();

    // Assert
    assertFalse(result);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_JS_FILE);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }

  @Test
  void testConstants_AreCorrect() {
    assertEquals("xAPI.js", DefaultModuleFileProvider.XAPI_JS_FILE);
    assertEquals("sendStatement.js", DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }

  @Test
  void testHasXapiSupport_FileAccessThrowsException_DoesNotPropagateException() {
    // Arrange - one call succeeds, one throws exception
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_JS_FILE)).thenReturn(true);
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE))
        .thenThrow(new RuntimeException("File system error"));

    // Act
    boolean result = fileProvider.hasXapiSupport();

    // Assert - Should still return true because first file exists
    assertTrue(result);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_JS_FILE);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }

  @Test
  void testHasXapiSupport_FirstFileCheckFails_StillChecksSecondFile() {
    // Arrange
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_JS_FILE))
        .thenThrow(new RuntimeException("File system error"));
    when(mockFileAccess.fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE)).thenReturn(true);

    // Act
    boolean result = fileProvider.hasXapiSupport();

    // Assert - Should return true because second file exists
    assertTrue(result);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_JS_FILE);
    verify(mockFileAccess).fileExists(DefaultModuleFileProvider.XAPI_SEND_STATEMENT_FILE);
  }
}