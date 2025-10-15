/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.impl.access;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link InMemoryFileAccess}.
 */
class InMemoryFileAccessTest {

  @Test
  void testConstructorWithByteArray() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess).isNotNull();
      assertThat(fileAccess.getFileCount()).isEqualTo(5);
      assertThat(fileAccess.getDirectoryCount()).isGreaterThan(0);
    }
  }

  @Test
  void testConstructorWithInputStream() throws IOException {
    byte[] zipData = createTestZipData();
    ByteArrayInputStream bais = new ByteArrayInputStream(zipData);

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(bais)) {
      assertThat(fileAccess).isNotNull();
      assertThat(fileAccess.getFileCount()).isEqualTo(5);
    }
  }

  @Test
  void testConstructorWithNullByteArray() {
    assertThatThrownBy(() -> new InMemoryFileAccess((byte[]) null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ZIP data cannot be null");
  }

  @Test
  void testConstructorWithNullInputStream() {
    assertThatThrownBy(() -> new InMemoryFileAccess((InputStream) null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ZIP input stream cannot be null");
  }

  @Test
  void testConstructorWithInvalidZipData() throws IOException {
    byte[] invalidData = "This is not a ZIP file".getBytes(StandardCharsets.UTF_8);

    // Invalid ZIP data should create an empty file access (no files)
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(invalidData)) {
      assertThat(fileAccess.getFileCount()).isEqualTo(0);
      assertThat(fileAccess.getAllFiles()).isEmpty();
    }
  }

  @Test
  void testFileExists() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.fileExists("manifest.xml")).isTrue();
      assertThat(fileAccess.fileExists("content/page1.html")).isTrue();
      assertThat(fileAccess.fileExists("resources/images/logo.png")).isTrue();
      assertThat(fileAccess.fileExists("nonexistent.txt")).isFalse();
      assertThat(fileAccess.fileExists("content/nonexistent.html")).isFalse();
    }
  }

  @Test
  void testFileExistsWithNullPath() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThatThrownBy(() -> fileAccess.fileExists(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Path cannot be null");
    }
  }

  @Test
  void testListFiles() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // List files in content directory
      List<String> contentFiles = fileAccess.listFiles("content");
      assertThat(contentFiles)
          .containsExactlyInAnyOrder("content/page1.html", "content/page2.html");

      // List files in resources directory
      List<String> resourceFiles = fileAccess.listFiles("resources");
      assertThat(resourceFiles)
          .containsExactlyInAnyOrder("resources/style.css", "resources/images/logo.png");

      // List all files (from root)
      List<String> allFiles = fileAccess.listFiles("");
      assertThat(allFiles).containsExactlyInAnyOrder(
          "manifest.xml",
          "content/page1.html",
          "content/page2.html",
          "resources/style.css",
          "resources/images/logo.png"
      );
    }
  }

  @Test
  void testListFilesWithNullPath() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThatThrownBy(() -> fileAccess.listFiles(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Directory path cannot be null");
    }
  }

  @Test
  void testGetFileContents() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Read manifest.xml
      try (InputStream is = fileAccess.getFileContents("manifest.xml")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("<manifest>test</manifest>");
      }

      // Read nested file
      try (InputStream is = fileAccess.getFileContents("content/page1.html")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("<html>Page 1</html>");
      }

      // Read CSS file
      try (InputStream is = fileAccess.getFileContents("resources/style.css")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("body { margin: 0; }");
      }
    }
  }

  @Test
  void testGetFileContentsWithNonexistentFile() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThatThrownBy(() -> fileAccess.getFileContents("nonexistent.txt"))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("File not found in in-memory ZIP")
          .hasMessageContaining("nonexistent.txt");
    }
  }

  @Test
  void testGetFileContentsWithNullPath() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThatThrownBy(() -> fileAccess.getFileContents(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Path cannot be null");
    }
  }

  @Test
  void testGetAllFiles() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      List<String> allFiles = fileAccess.getAllFiles();
      assertThat(allFiles).hasSize(5);
      assertThat(allFiles).containsExactlyInAnyOrder(
          "manifest.xml",
          "content/page1.html",
          "content/page2.html",
          "resources/style.css",
          "resources/images/logo.png"
      );
    }
  }

  @Test
  void testGetTotalSize() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      long totalSize = fileAccess.getTotalSize();
      assertThat(totalSize).isGreaterThan(0);

      // Verify it's the sum of all file contents
      String expectedContent = "<manifest>test</manifest>" +
          "<html>Page 1</html>" +
          "<html>Page 2</html>" +
          "body { margin: 0; }" +
          "PNG_DATA";
      assertThat(totalSize).isEqualTo(expectedContent.getBytes(StandardCharsets.UTF_8).length);
    }
  }

  @Test
  void testRootPathDetection() throws IOException {
    // Test ZIP with files at root
    byte[] zipDataNoRoot = createTestZipData();
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipDataNoRoot)) {
      assertThat(fileAccess.getRootPath()).isEmpty();
    }

    // Test ZIP with single root directory
    byte[] zipDataWithRoot = createZipWithRootDirectory();
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipDataWithRoot)) {
      assertThat(fileAccess.getRootPath()).isEqualTo("module-root");
    }
  }

  @Test
  void testFullPathHandling() throws IOException {
    byte[] zipData = createZipWithRootDirectory();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Root path should be "module-root"
      assertThat(fileAccess.getRootPath()).isEqualTo("module-root");

      // Files should be accessible with relative paths
      assertThat(fileAccess.fileExists("manifest.xml")).isTrue();
      assertThat(fileAccess.fileExists("content/page1.html")).isTrue();

      // Full path should be constructed correctly
      assertThat(fileAccess.fullPath("manifest.xml")).isEqualTo("module-root/manifest.xml");
      assertThat(fileAccess.fullPath("/manifest.xml")).isEqualTo("manifest.xml");
    }
  }

  @Test
  void testEmptyZip() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Create empty ZIP
    }
    byte[] emptyZipData = baos.toByteArray();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(emptyZipData)) {
      assertThat(fileAccess.getFileCount()).isEqualTo(0);
      assertThat(fileAccess.getAllFiles()).isEmpty();
      assertThat(fileAccess.getTotalSize()).isEqualTo(0);
      assertThat(fileAccess.fileExists("anything.txt")).isFalse();
    }
  }

  @Test
  void testLargeFile() throws IOException {
    // Create ZIP with a large file
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Create 1MB of data
      StringBuilder largeContent = new StringBuilder();
      for (int i = 0; i < 1024 * 1024; i++) {
        largeContent.append('X');
      }
      addZipEntry(zos, "large-file.txt", largeContent.toString());
    }
    byte[] zipData = baos.toByteArray();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.fileExists("large-file.txt")).isTrue();
      assertThat(fileAccess.getTotalSize()).isEqualTo(1024 * 1024);

      try (InputStream is = fileAccess.getFileContents("large-file.txt")) {
        byte[] content = is.readAllBytes();
        assertThat(content.length).isEqualTo(1024 * 1024);
      }
    }
  }

  @Test
  void testClose() throws IOException {
    byte[] zipData = createTestZipData();
    InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData);

    // Close should not throw exception
    fileAccess.close();

    // Should still be able to access files after close (no external resources)
    assertThat(fileAccess.fileExists("manifest.xml")).isTrue();
  }

  @Test
  void testSimilarFileSuggestions() throws IOException {
    byte[] zipData = createTestZipData();

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Try to access a file with similar name
      assertThatThrownBy(() -> fileAccess.getFileContents("page1.html"))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("Similar files")
          .hasMessageContaining("content/page1.html");

      // Try to access completely nonexistent file
      assertThatThrownBy(() -> fileAccess.getFileContents("xyz123.abc"))
          .isInstanceOf(IOException.class)
          .hasMessageContaining("File not found in in-memory ZIP");
    }
  }

  /**
   * Creates a simple ZIP file in memory with test content.
   *
   * @return byte array containing ZIP data
   * @throws IOException if ZIP creation fails
   */
  private byte[] createTestZipData() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Add root file
      addZipEntry(zos, "manifest.xml", "<manifest>test</manifest>");

      // Add files in directories
      addZipEntry(zos, "content/page1.html", "<html>Page 1</html>");
      addZipEntry(zos, "content/page2.html", "<html>Page 2</html>");
      addZipEntry(zos, "resources/style.css", "body { margin: 0; }");
      addZipEntry(zos, "resources/images/logo.png", "PNG_DATA");

      // Add directory entry
      ZipEntry dirEntry = new ZipEntry("empty-dir/");
      zos.putNextEntry(dirEntry);
      zos.closeEntry();
    }
    return baos.toByteArray();
  }

  /**
   * Creates a ZIP file with all content in a single root directory.
   *
   * @return byte array containing ZIP data
   * @throws IOException if ZIP creation fails
   */
  private byte[] createZipWithRootDirectory() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // All files under "module-root" directory
      addZipEntry(zos, "module-root/manifest.xml", "<manifest>test</manifest>");
      addZipEntry(zos, "module-root/content/page1.html", "<html>Page 1</html>");
      addZipEntry(zos, "module-root/resources/style.css", "body { margin: 0; }");
    }
    return baos.toByteArray();
  }

  /**
   * Helper method to add an entry to a ZIP output stream.
   *
   * @param zos the ZIP output stream
   * @param path the path of the entry
   * @param content the content of the entry
   * @throws IOException if writing fails
   */
  private void addZipEntry(ZipOutputStream zos, String path, String content) throws IOException {
    ZipEntry entry = new ZipEntry(path);
    zos.putNextEntry(entry);
    zos.write(content.getBytes(StandardCharsets.UTF_8));
    zos.closeEntry();
  }
}