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

import dev.jcputney.elearning.parser.ModuleParser;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ResourceUtils.
 */
class ResourceUtilsTest {

  @Mock
  private FileAccess mockFileAccess;

  @Mock
  private ModuleParser<TestManifest> mockParser;

  @Mock
  private ModuleMetadata<TestManifest> mockMetadata;

  @Test
  void testUtilityClass_CannotBeInstantiated() throws Exception {
    var constructor = ResourceUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
  }

  @Test
  void testParseZipModule_ValidZip_Success(@TempDir File tempDir) throws Exception {
    MockitoAnnotations.openMocks(this);
    
    // Create a test ZIP file
    File zipFile = new File(tempDir, "test.zip");
    createTestZipFile(zipFile);
    
    // Mock parser behavior
    when(mockParser.parse()).thenReturn(mockMetadata);
    
    // Act
    ModuleMetadata<TestManifest> result = ResourceUtils.parseZipModule(
        zipFile.getAbsolutePath(),
        fileAccess -> mockParser
    );
    
    // Assert
    assertSame(mockMetadata, result);
    verify(mockParser).parse();
  }

  @Test
  void testParseZipModule_InvalidZipPath_ThrowsIOException() {
    // Act & Assert
    IOException exception = assertThrows(IOException.class, () -> {
      ResourceUtils.parseZipModule(
          "/nonexistent/path.zip",
          fileAccess -> mockParser
      );
    });
    
    assertTrue(exception.getMessage().contains("Failed to open ZIP file"));
  }

  @Test
  void testParseZipModule_ParserThrowsException_PropagatesException(@TempDir File tempDir) throws Exception {
    MockitoAnnotations.openMocks(this);
    
    // Create a test ZIP file
    File zipFile = new File(tempDir, "test.zip");
    createTestZipFile(zipFile);
    
    // Mock parser to throw exception
    ModuleParsingException expectedException = new ModuleParsingException("Parse failed");
    when(mockParser.parse()).thenThrow(expectedException);
    
    // Act & Assert
    ModuleParsingException actualException = assertThrows(ModuleParsingException.class, () -> {
      ResourceUtils.parseZipModule(
          zipFile.getAbsolutePath(),
          fileAccess -> mockParser
      );
    });
    
    assertSame(expectedException, actualException);
    verify(mockParser).parse();
  }

  @Test
  void testSafeRead_ValidStream_Success() throws IOException {
    // Arrange
    byte[] data = "test data".getBytes();
    InputStream stream = new ByteArrayInputStream(data);
    
    // Act
    String result = ResourceUtils.safeRead(stream, inputStream -> {
      try {
        return new String(inputStream.readAllBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    
    // Assert
    assertEquals("test data", result);
  }

  @Test
  void testSafeRead_NullStream_ThrowsException() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      ResourceUtils.safeRead(null, stream -> "test");
    });
    assertEquals("Stream cannot be null", exception.getMessage());
  }

  @Test
  void testSafeRead_ReaderThrowsException_PropagatesException() {
    // Arrange
    InputStream stream = new ByteArrayInputStream("test".getBytes());
    IOException expectedException = new IOException("Read failed");
    
    // Act & Assert
    IOException actualException = assertThrows(IOException.class, () -> {
      ResourceUtils.safeRead(stream, inputStream -> {
        throw expectedException;
      });
    });
    
    assertSame(expectedException, actualException);
  }

  @Test
  void testCloseIfCloseable_AutoCloseableFileAccess_CallsClose() throws Exception {
    // Arrange
    MockitoAnnotations.openMocks(this);
    AutoCloseableFileAccess closeableFileAccess = mock(AutoCloseableFileAccess.class);
    
    // Act
    ResourceUtils.closeIfCloseable(closeableFileAccess);
    
    // Assert
    verify(closeableFileAccess).close();
  }

  @Test
  void testCloseIfCloseable_NonAutoCloseableFileAccess_DoesNotCallClose() {
    // Arrange
    MockitoAnnotations.openMocks(this);
    
    // Act - should not throw exception
    ResourceUtils.closeIfCloseable(mockFileAccess);
    
    // Assert - no verification needed as mockFileAccess is not AutoCloseable
  }

  @Test
  void testCloseIfCloseable_CloseThrowsException_LogsWarning() throws Exception {
    // Arrange
    MockitoAnnotations.openMocks(this);
    AutoCloseableFileAccess closeableFileAccess = mock(AutoCloseableFileAccess.class);
    doThrow(new RuntimeException("Close failed")).when(closeableFileAccess).close();
    
    // Act - should not throw exception
    ResourceUtils.closeIfCloseable(closeableFileAccess);
    
    // Assert
    verify(closeableFileAccess).close();
  }

  @Test
  void testSafeFileAccess_GetRootPath_DelegatesToOriginal() {
    // Arrange
    MockitoAnnotations.openMocks(this);
    when(mockFileAccess.getRootPath()).thenReturn("/test/path");
    ResourceUtils.SafeFileAccess safeFileAccess = new ResourceUtils.SafeFileAccess(mockFileAccess);
    
    // Act
    String result = safeFileAccess.getRootPath();
    
    // Assert
    assertEquals("/test/path", result);
    verify(mockFileAccess).getRootPath();
  }

  @Test
  void testSafeFileAccess_FileExists_DelegatesToOriginal() {
    // Arrange
    MockitoAnnotations.openMocks(this);
    when(mockFileAccess.fileExists("test.txt")).thenReturn(true);
    ResourceUtils.SafeFileAccess safeFileAccess = new ResourceUtils.SafeFileAccess(mockFileAccess);
    
    // Act
    boolean result = safeFileAccess.fileExists("test.txt");
    
    // Assert
    assertTrue(result);
    verify(mockFileAccess).fileExists("test.txt");
  }

  @Test
  void testSafeFileAccess_ListFiles_DelegatesToOriginal() throws IOException {
    // Arrange
    MockitoAnnotations.openMocks(this);
    List<String> expectedFiles = Arrays.asList("file1.txt", "file2.txt");
    when(mockFileAccess.listFiles("testdir")).thenReturn(expectedFiles);
    ResourceUtils.SafeFileAccess safeFileAccess = new ResourceUtils.SafeFileAccess(mockFileAccess);
    
    // Act
    List<String> result = safeFileAccess.listFiles("testdir");
    
    // Assert
    assertEquals(expectedFiles, result);
    verify(mockFileAccess).listFiles("testdir");
  }

  @Test
  void testSafeFileAccess_GetFileContents_WrapsStream() throws IOException {
    // Arrange
    MockitoAnnotations.openMocks(this);
    InputStream originalStream = new ByteArrayInputStream("test content".getBytes());
    when(mockFileAccess.getFileContents("test.txt")).thenReturn(originalStream);
    ResourceUtils.SafeFileAccess safeFileAccess = new ResourceUtils.SafeFileAccess(mockFileAccess);
    
    // Act
    InputStream result = safeFileAccess.getFileContents("test.txt");
    
    // Assert
    assertNotNull(result);
    assertNotSame(originalStream, result); // Should be wrapped
    verify(mockFileAccess).getFileContents("test.txt");
    
    // Verify we can read from the wrapped stream
    String content = new String(result.readAllBytes());
    assertEquals("test content", content);
  }

  @Test
  void testSafeFileAccess_GetFileContents_StreamCanBeClosed() throws IOException {
    // Arrange
    MockitoAnnotations.openMocks(this);
    InputStream originalStream = new ByteArrayInputStream("test content".getBytes());
    when(mockFileAccess.getFileContents("test.txt")).thenReturn(originalStream);
    ResourceUtils.SafeFileAccess safeFileAccess = new ResourceUtils.SafeFileAccess(mockFileAccess);
    
    // Act
    InputStream result = safeFileAccess.getFileContents("test.txt");
    
    // Assert - should not throw exception
    result.close();
  }

  private void createTestZipFile(File zipFile) throws IOException {
    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
      ZipEntry entry = new ZipEntry("test.txt");
      zos.putNextEntry(entry);
      zos.write("test content".getBytes());
      zos.closeEntry();
    }
  }

  /**
   * Test interface that extends both FileAccess and AutoCloseable for testing.
   */
  interface AutoCloseableFileAccess extends FileAccess, AutoCloseable {
  }

  /**
   * Test manifest class for testing purposes.
   */
  private static class TestManifest implements PackageManifest {
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
    public java.time.Duration getDuration() {
      return null;
    }
  }
}