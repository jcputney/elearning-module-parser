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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the {@link LocalFileAccess} class.
 */
class LocalFileAccessTest {

  @TempDir
  Path tempDir;

  private LocalFileAccess localFileAccess;
  private Path testFile;
  private Path testSubDir;
  private Path testSubDirFile;

  @BeforeEach
  void setUp() throws IOException {
    // Create a test file in the temp directory
    testFile = tempDir.resolve("test.txt");
    Files.write(testFile, "Test content".getBytes(StandardCharsets.UTF_8));

    // Create a subdirectory with a file
    testSubDir = tempDir.resolve("subdir");
    Files.createDirectory(testSubDir);
    testSubDirFile = testSubDir.resolve("subfile.txt");
    Files.write(testSubDirFile, "Subdir test content".getBytes(StandardCharsets.UTF_8));

    // Initialize LocalFileAccess with the temp directory
    localFileAccess = new LocalFileAccess(tempDir.toString());
  }

  @Test
  void constructor_withValidPath_createsInstance() {
    assertNotNull(localFileAccess);
    assertEquals(tempDir.toString(), localFileAccess.getRootPath());
  }

  @Test
  void constructor_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new LocalFileAccess(null));
  }

  @Test
  void constructor_withNonExistentDirectory_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> new LocalFileAccess(tempDir.resolve("nonexistent").toString()));
  }

  @Test
  void constructor_withTrailingSlash_removesSlash() {
    LocalFileAccess access = new LocalFileAccess(tempDir.toString() + "/");
    assertEquals(tempDir.toString(), access.getRootPath());
  }

  @Test
  void fileExists_withExistingFile_returnsTrue() {
    assertTrue(localFileAccess.fileExists("test.txt"));
  }

  @Test
  void fileExists_withNonExistingFile_returnsFalse() {
    assertFalse(localFileAccess.fileExists("nonexistent.txt"));
  }

  @Test
  void fileExists_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> localFileAccess.fileExists(null));
  }

  @Test
  void fileExists_withSubdirectoryFile_returnsTrue() {
    assertTrue(localFileAccess.fileExists("subdir/subfile.txt"));
  }

  @Test
  void listFiles_withValidDirectory_returnsCorrectFiles() throws IOException {
    List<String> files = localFileAccess.listFiles("");

    // Should contain test.txt but not directories
    assertTrue(files.contains("test.txt"));
    assertFalse(files.contains("subdir"));
  }

  @Test
  void listFiles_withSubdirectory_returnsCorrectFiles() throws IOException {
    List<String> files = localFileAccess.listFiles("subdir");

    // Should contain subfile.txt
    assertTrue(files.contains("subdir/subfile.txt"));
  }

  @Test
  void listFiles_withNonExistentDirectory_throwsIOException() {
    assertThrows(IOException.class, () -> localFileAccess.listFiles("nonexistent"));
  }

  @Test
  void listFiles_withFileAsDirectory_throwsIOException() {
    assertThrows(IOException.class, () -> localFileAccess.listFiles("test.txt"));
  }

  @Test
  void listFiles_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> localFileAccess.listFiles(null));
  }

  @Test
  void getFileContents_withExistingFile_returnsCorrectContent() throws IOException {
    try (InputStream is = localFileAccess.getFileContents("test.txt")) {
      byte[] content = is.readAllBytes();
      assertEquals("Test content", new String(content, StandardCharsets.UTF_8));
    }
  }

  @Test
  void getFileContents_withSubdirectoryFile_returnsCorrectContent() throws IOException {
    try (InputStream is = localFileAccess.getFileContents("subdir/subfile.txt")) {
      byte[] content = is.readAllBytes();
      assertEquals("Subdir test content", new String(content, StandardCharsets.UTF_8));
    }
  }

  @Test
  void getFileContents_withNonExistentFile_throwsNoSuchFileException() {
    assertThrows(NoSuchFileException.class,
        () -> localFileAccess.getFileContents("nonexistent.txt"));
  }

  @Test
  void getFileContents_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> localFileAccess.getFileContents(null));
  }

  @Test
  void getFileContents_withDirectory_throwsIOException() throws IOException {
    // In LocalFileAccess, the fileExistsInternal method returns true for directories,
    // but Files.newInputStream will throw an IOException when trying to open a directory
    assertThrows(IOException.class, () -> {
      try (InputStream is = localFileAccess.getFileContents("subdir")) {
        // Reading from the stream will cause the exception
        is.read();
      }
    });
  }

  @Test
  void fullPath_withRelativePath_returnsPathWithRootPrefix() {
    assertEquals(Paths.get(tempDir.toString(), "file.txt").toString(),
        localFileAccess.fullPath("file.txt"));
  }

  @Test
  void fullPath_withAbsolutePath_returnsPathWithoutLeadingSlash() {
    assertEquals("file.txt", localFileAccess.fullPath("/file.txt"));
  }

  @Test
  void fullPath_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> localFileAccess.fullPath(null));
  }

  @Test
  void fullPath_withEmptyPath_returnsRootPathWithSlash() {
    // The FileAccess.fullPath() method adds a trailing slash when the path is empty
    assertEquals(tempDir.toString() + "/", localFileAccess.fullPath(""));
  }

  @Test
  void fileExists_withSpecialCharactersInPath_handlesCorrectly() throws IOException {
    // Create files with special characters in their names
    Path specialFile1 = tempDir.resolve("file with spaces.txt");
    Files.write(specialFile1, "Content with spaces".getBytes(StandardCharsets.UTF_8));

    Path specialFile2 = tempDir.resolve("file-with-dashes.txt");
    Files.write(specialFile2, "Content with dashes".getBytes(StandardCharsets.UTF_8));

    Path specialFile3 = tempDir.resolve("file_with_underscores.txt");
    Files.write(specialFile3, "Content with underscores".getBytes(StandardCharsets.UTF_8));

    // Test that fileExists works correctly with these files
    assertTrue(localFileAccess.fileExists("file with spaces.txt"));
    assertTrue(localFileAccess.fileExists("file-with-dashes.txt"));
    assertTrue(localFileAccess.fileExists("file_with_underscores.txt"));
  }

  @Test
  void getFileContents_withSpecialCharactersInPath_returnsCorrectContent() throws IOException {
    // Create a file with special characters in its name
    String fileName = "special!@#$%^&*().txt";
    Path specialFile = tempDir.resolve(fileName);
    String content = "Special content";
    Files.write(specialFile, content.getBytes(StandardCharsets.UTF_8));

    // Test that getFileContents works correctly with this file
    try (InputStream is = localFileAccess.getFileContents(fileName)) {
      byte[] readContent = is.readAllBytes();
      assertEquals(content, new String(readContent, StandardCharsets.UTF_8));
    }
  }

  @Test
  void getFileContents_withLargeFile_handlesCorrectly() throws IOException {
    // Create a large file (1MB)
    Path largeFile = tempDir.resolve("large_file.bin");
    byte[] largeContent = new byte[1024 * 1024]; // 1MB
    for (int i = 0; i < largeContent.length; i++) {
      largeContent[i] = (byte) (i % 256);
    }
    Files.write(largeFile, largeContent);

    // Test that getFileContents works correctly with this large file
    try (InputStream is = localFileAccess.getFileContents("large_file.bin")) {
      byte[] readContent = is.readAllBytes();
      assertArrayEquals(largeContent, readContent);
    }
  }

  @Test
  void getFileContents_withDifferentEncoding_handlesCorrectly() throws IOException {
    // Create files with different encodings
    Path utf8File = tempDir.resolve("utf8.txt");
    String utf8Content = "UTF-8 content with special characters: äöüß";
    Files.write(utf8File, utf8Content.getBytes(StandardCharsets.UTF_8));

    Path iso8859File = tempDir.resolve("iso8859.txt");
    String iso8859Content = "ISO-8859-1 content";
    Files.write(iso8859File, iso8859Content.getBytes(StandardCharsets.ISO_8859_1));

    // Test that getFileContents works correctly with UTF-8 encoding
    try (InputStream is = localFileAccess.getFileContents("utf8.txt")) {
      byte[] readContent = is.readAllBytes();
      assertEquals(utf8Content, new String(readContent, StandardCharsets.UTF_8));
    }

    // Test that getFileContents works correctly with ISO-8859-1 encoding
    try (InputStream is = localFileAccess.getFileContents("iso8859.txt")) {
      byte[] readContent = is.readAllBytes();
      assertEquals(iso8859Content, new String(readContent, StandardCharsets.ISO_8859_1));
    }
  }

  @Test
  void fileExists_withParentDirectoryReference_handlesCorrectly() throws IOException {
    // Create a file in a subdirectory
    Path nestedDir = tempDir.resolve("nested");
    Files.createDirectory(nestedDir);
    Path nestedFile = nestedDir.resolve("nested_file.txt");
    Files.write(nestedFile, "Nested content".getBytes(StandardCharsets.UTF_8));

    // Test with parent directory reference
    assertTrue(localFileAccess.fileExists("nested/../test.txt")); // Should resolve to test.txt
    assertFalse(localFileAccess.fileExists(
        "nested/../nonexistent.txt")); // Should resolve to nonexistent.txt
  }

  @Test
  void fileExists_withCurrentDirectoryReference_handlesCorrectly() throws IOException {
    // Test with current directory reference
    assertTrue(localFileAccess.fileExists("./test.txt")); // Should resolve to test.txt
    assertFalse(
        localFileAccess.fileExists("./nonexistent.txt")); // Should resolve to nonexistent.txt
  }

  @Test
  void fileExists_withMultipleSlashes_handlesCorrectly() throws IOException {
    // Test with multiple slashes
    assertTrue(
        localFileAccess.fileExists("subdir//subfile.txt")); // Should resolve to subdir/subfile.txt
  }

  @Test
  void fileExists_withFileWithNoExtension_handlesCorrectly() throws IOException {
    // Create a file with no extension
    Path noExtensionFile = tempDir.resolve("noextension");
    Files.write(noExtensionFile, "No extension content".getBytes(StandardCharsets.UTF_8));

    // Test that fileExists works correctly with this file
    assertTrue(localFileAccess.fileExists("noextension"));
  }

  @Test
  void listFiles_withHiddenFiles_listsCorrectly() throws IOException {
    // Create a hidden file (starting with .)
    Path hiddenFile = tempDir.resolve(".hidden.txt");
    Files.write(hiddenFile, "Hidden content".getBytes(StandardCharsets.UTF_8));

    // Test that listFiles includes the hidden file
    List<String> files = localFileAccess.listFiles("");
    assertTrue(files.contains(".hidden.txt"));
  }

  @Test
  void fullPath_withParentDirectoryReference_returnsCorrectPath() {
    // Test with parent directory reference
    String path = "subdir/../file.txt";
    assertEquals(Paths.get(tempDir.toString(), path).toString(), localFileAccess.fullPath(path));
  }

  @Test
  void fullPath_withCurrentDirectoryReference_returnsCorrectPath() {
    // Test with current directory reference
    String path = "./file.txt";
    assertEquals(Paths.get(tempDir.toString(), path).toString(), localFileAccess.fullPath(path));
  }

  @Test
  void fullPath_withMultipleSlashes_returnsCorrectPath() {
    // Test with multiple slashes
    // Note: fullPath() doesn't normalize multiple slashes in the middle of the path
    String path = "subdir//file.txt";
    assertEquals(tempDir.toString() + "/" + path, localFileAccess.fullPath(path));
  }
}
