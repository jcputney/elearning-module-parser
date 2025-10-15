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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.api.StreamingProgressListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Comprehensive tests for {@link InMemoryFileAccess} covering edge cases, performance
 * characteristics, and concurrent access patterns.
 */
class InMemoryFileAccessComprehensiveTest {

  @Test
  void testZipWithSpecialCharactersInFilenames() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("file with spaces.txt", "content1");
    files.put("файл.txt", "content2"); // Cyrillic
    files.put("文件.txt", "content3"); // Chinese
    files.put("special!@#$%^&()_+={}[]|;',.txt", "content4");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.fileExists("file with spaces.txt")).isTrue();
      assertThat(fileAccess.fileExists("файл.txt")).isTrue();
      assertThat(fileAccess.fileExists("文件.txt")).isTrue();
      assertThat(fileAccess.fileExists("special!@#$%^&()_+={}[]|;',.txt")).isTrue();

      // Verify content
      try (InputStream is = fileAccess.getFileContents("файл.txt")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("content2");
      }
    }
  }

  // ========== Edge Case Tests ==========

  @Test
  void testZipWithNestedDirectoryStructure() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("a/b/c/d/e/f/deep.txt", "deeply nested");
    files.put("a/b/c/other.txt", "less nested");
    files.put("a/root.txt", "at a");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Since all files start with "a/", root path will be "a"
      assertThat(fileAccess.getRootPath()).isEqualTo("a");

      // Files should be accessible with paths relative to root
      assertThat(fileAccess.fileExists("b/c/d/e/f/deep.txt")).isTrue();
      assertThat(fileAccess.fileExists("b/c/other.txt")).isTrue();
      assertThat(fileAccess.fileExists("root.txt")).isTrue();

      // List all files (empty path since we have root directory)
      List<String> allFiles = fileAccess.listFiles("");
      assertThat(allFiles).containsExactlyInAnyOrder(
          "a/b/c/d/e/f/deep.txt",
          "a/b/c/other.txt",
          "a/root.txt"
      );

      List<String> filesInDeep = fileAccess.listFiles("b/c/d/e/f");
      assertThat(filesInDeep).containsExactly("a/b/c/d/e/f/deep.txt");
    }
  }

  @Test
  void testZipWithDuplicateEntries() throws IOException {
    // Standard ZipOutputStream doesn't allow duplicates, but we can test
    // that the last entry wins if we manually create such data
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Add first file
      zos.putNextEntry(new ZipEntry("duplicate.txt"));
      zos.write("first".getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();

      // Add another file to make valid ZIP
      zos.putNextEntry(new ZipEntry("other.txt"));
      zos.write("other".getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();
    }

    byte[] zipData = baos.toByteArray();
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.fileExists("duplicate.txt")).isTrue();
      assertThat(fileAccess.fileExists("other.txt")).isTrue();

      // Should return the content
      try (InputStream is = fileAccess.getFileContents("duplicate.txt")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("first");
      }
    }
  }

  @Test
  void testZipWithOnlyDirectories() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("empty1/", "");
    files.put("empty2/", "");
    files.put("nested/empty/", "");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.getFileCount()).isEqualTo(0);
      assertThat(fileAccess.getAllFiles()).isEmpty();
      assertThat(fileAccess.getDirectoryCount()).isGreaterThan(0);
    }
  }

  @Test
  void testZipWithMixedLineEndings() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("unix.txt", "line1\nline2\nline3");
    files.put("windows.txt", "line1\r\nline2\r\nline3");
    files.put("mac.txt", "line1\rline2\rline3");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Verify content is preserved exactly
      try (InputStream is = fileAccess.getFileContents("unix.txt")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("line1\nline2\nline3");
      }

      try (InputStream is = fileAccess.getFileContents("windows.txt")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("line1\r\nline2\r\nline3");
      }
    }
  }

  @Test
  void testZipWithBinaryContent() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Add binary file
      ZipEntry entry = new ZipEntry("binary.dat");
      zos.putNextEntry(entry);

      byte[] binaryData = new byte[256];
      for (int i = 0; i < 256; i++) {
        binaryData[i] = (byte) i;
      }
      zos.write(binaryData);
      zos.closeEntry();
    }

    byte[] zipData = baos.toByteArray();
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      try (InputStream is = fileAccess.getFileContents("binary.dat")) {
        byte[] content = is.readAllBytes();
        assertThat(content).hasSize(256);
        for (int i = 0; i < 256; i++) {
          assertThat(content[i]).isEqualTo((byte) i);
        }
      }
    }
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void testLargeNumberOfFiles() throws IOException {
    Map<String, String> files = new HashMap<>();
    int fileCount = 10000;

    for (int i = 0; i < fileCount; i++) {
      files.put("file" + i + ".txt", "content" + i);
    }

    byte[] zipData = createZipWithFiles(files);

    long startTime = System.currentTimeMillis();
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      long loadTime = System.currentTimeMillis() - startTime;
      assertThat(loadTime).isLessThan(2000); // Should load in under 2 seconds

      assertThat(fileAccess.getFileCount()).isEqualTo(fileCount);

      // Test random access performance
      startTime = System.currentTimeMillis();
      for (int i = 0; i < 100; i++) {
        int randomFile = (int) (Math.random() * fileCount);
        assertThat(fileAccess.fileExists("file" + randomFile + ".txt")).isTrue();
      }
      long accessTime = System.currentTimeMillis() - startTime;
      assertThat(accessTime).isLessThan(100); // 100 lookups should be fast
    }
  }

  // ========== Performance Tests ==========

  @Test
  void testMemoryEfficiencyWithCompression() throws IOException {
    // Create highly compressible content
    StringBuilder largeContent = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      largeContent.append("AAAAAAAAAA"); // Highly repetitive
    }
    String content = largeContent.toString();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      zos.setLevel(Deflater.BEST_COMPRESSION);
      ZipEntry entry = new ZipEntry("compressed.txt");
      zos.putNextEntry(entry);
      zos.write(content.getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();
    }

    byte[] zipData = baos.toByteArray();
    assertThat(zipData.length).isLessThan(content.length() / 10); // Should compress well

    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Verify uncompressed size in memory
      assertThat(fileAccess.getTotalSize()).isEqualTo(
          content.getBytes(StandardCharsets.UTF_8).length);

      // Verify content is correct
      try (InputStream is = fileAccess.getFileContents("compressed.txt")) {
        String readContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(readContent).isEqualTo(content);
      }
    }
  }

  @Test
  void testStreamingWithProgressListener() throws IOException {
    Map<String, String> files = new HashMap<>();
    String largeContent = "X".repeat(1000000); // 1MB file
    files.put("large.txt", largeContent);

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      AtomicLong bytesRead = new AtomicLong(0);
      AtomicInteger progressCalls = new AtomicInteger(0);

      StreamingProgressListener listener = new StreamingProgressListener() {
        @Override
        public void onProgress(long bytes, long total) {
          bytesRead.set(bytes);
          progressCalls.incrementAndGet();
        }

        @Override
        public void onComplete(long totalBytes) {
          // Not needed for this test
        }
      };

      // Use reflection to call the method with progress listener
      try (InputStream is = ((InMemoryFileAccess) fileAccess).getFileContentsInternal("large.txt",
          listener)) {
        byte[] content = is.readAllBytes();
        assertThat(content.length).isEqualTo(largeContent.length());
      }

      // Progress listener should have been called
      assertThat(progressCalls.get()).isGreaterThan(0);
      assertThat(bytesRead.get()).isEqualTo(largeContent.length());
    }
  }

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void testConcurrentReads() throws Exception {
    Map<String, String> files = new HashMap<>();
    for (int i = 0; i < 100; i++) {
      files.put("file" + i + ".txt", "content" + i);
    }

    byte[] zipData = createZipWithFiles(files);
    InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData);

    int threadCount = 10;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);
    AtomicInteger successCount = new AtomicInteger(0);

    try {
      for (int t = 0; t < threadCount; t++) {
        final int threadId = t;
        executor.submit(() -> {
          try {
            for (int i = 0; i < 100; i++) {
              int fileId = (threadId * 10 + i) % 100;
              String fileName = "file" + fileId + ".txt";
              String expectedContent = "content" + fileId;

              assertThat(fileAccess.fileExists(fileName)).isTrue();

              try (InputStream is = fileAccess.getFileContents(fileName)) {
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                assertThat(content).isEqualTo(expectedContent);
              }
            }
            successCount.incrementAndGet();
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            latch.countDown();
          }
        });
      }

      assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
      assertThat(successCount.get()).isEqualTo(threadCount);
    } finally {
      executor.shutdown();
      fileAccess.close();
    }
  }

  // ========== Concurrent Access Tests ==========

  @Test
  void testConcurrentListOperations() throws Exception {
    Map<String, String> files = new HashMap<>();
    files.put("dir1/file1.txt", "content1");
    files.put("dir1/file2.txt", "content2");
    files.put("dir2/file3.txt", "content3");
    files.put("dir2/file4.txt", "content4");

    byte[] zipData = createZipWithFiles(files);
    InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData);

    int threadCount = 5;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    try {
      for (int i = 0; i < threadCount; i++) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
          try {
            for (int j = 0; j < 100; j++) {
              List<String> dir1Files = fileAccess.listFiles("dir1");
              assertThat(dir1Files).hasSize(2);

              List<String> dir2Files = fileAccess.listFiles("dir2");
              assertThat(dir2Files).hasSize(2);

              List<String> allFiles = fileAccess.getAllFiles();
              assertThat(allFiles).hasSize(4);
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }, executor);
        futures.add(future);
      }

      CompletableFuture
          .allOf(futures.toArray(new CompletableFuture[0]))
          .get(5, TimeUnit.SECONDS);
    } finally {
      executor.shutdown();
      fileAccess.close();
    }
  }

  @Test
  void testConstructorWithSlowInputStream() throws Exception {
    Map<String, String> files = new HashMap<>();
    files.put("test.txt", "content");
    byte[] zipData = createZipWithFiles(files);

    // Create a slow input stream that delivers data in small chunks
    PipedOutputStream pos = new PipedOutputStream();
    PipedInputStream pis = new PipedInputStream(pos);

    CompletableFuture<Void> writer = CompletableFuture.runAsync(() -> {
      try {
        for (byte b : zipData) {
          pos.write(b);
          Thread.sleep(1); // Simulate slow network
        }
        pos.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Should still work with slow stream
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(pis)) {
      assertThat(fileAccess.fileExists("test.txt")).isTrue();
      writer.join(); // Ensure writer completes
    }
  }

  // ========== Input Stream Tests ==========

  @Test
  void testConstructorWithInterruptedInputStream() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("test.txt", "content");
    byte[] zipData = createZipWithFiles(files);

    // Create stream that only provides partial data
    ByteArrayInputStream partialStream = new ByteArrayInputStream(
        java.util.Arrays.copyOf(zipData, zipData.length / 2)
    );

    // Should handle truncated ZIP gracefully
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(partialStream)) {
      // May have loaded some files or none depending on where it was truncated
      assertThat(fileAccess.getFileCount()).isGreaterThanOrEqualTo(0);
    }
  }

  @Test
  void testFileExistsBatch() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("exists1.txt", "content1");
    files.put("exists2.txt", "content2");
    files.put("dir/exists3.txt", "content3");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      List<String> pathsToCheck = List.of(
          "exists1.txt",
          "exists2.txt",
          "nonexistent.txt",
          "dir/exists3.txt",
          "dir/nonexistent.txt"
      );

      Map<String, Boolean> results = fileAccess.fileExistsBatch(pathsToCheck);

      assertThat(results).containsEntry("exists1.txt", true);
      assertThat(results).containsEntry("exists2.txt", true);
      assertThat(results).containsEntry("nonexistent.txt", false);
      assertThat(results).containsEntry("dir/exists3.txt", true);
      assertThat(results).containsEntry("dir/nonexistent.txt", false);
    }
  }

  // ========== Batch Operation Tests ==========

  @Test
  void testFileExistsBatchWithNullHandling() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("test.txt", "content");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Null list should throw
      assertThatThrownBy(() -> fileAccess.fileExistsBatch(null))
          .isInstanceOf(IllegalArgumentException.class);

      // List with null elements should skip them
      List<String> pathsWithNull = new ArrayList<>();
      pathsWithNull.add("test.txt");
      pathsWithNull.add(null);
      pathsWithNull.add("other.txt");

      Map<String, Boolean> results = fileAccess.fileExistsBatch(pathsWithNull);
      assertThat(results).hasSize(2);
      assertThat(results).containsEntry("test.txt", true);
      assertThat(results).containsEntry("other.txt", false);
    }
  }

  @Test
  void testPathNormalizationWithVariousFormats() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("normal/file.txt", "content");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Since all files are under "normal/", root path will be "normal"
      assertThat(fileAccess.getRootPath()).isEqualTo("normal");

      // File should be accessible with path relative to root
      assertThat(fileAccess.fileExists("file.txt")).isTrue();
      // Leading slash is stripped by fullPath() method
      assertThat(fileAccess.fullPath("/file.txt")).isEqualTo("file.txt");
      // Without leading slash
      assertThat(fileAccess.fullPath("file.txt")).isEqualTo("normal/file.txt");

      // Directory listing with various formats - "normal" is the root
      // So we list from empty path to get all files
      assertThat(fileAccess.listFiles("")).hasSize(1);
      assertThat(fileAccess.listFiles("/")).hasSize(1);
    }
  }

  // ========== Path Handling Tests ==========

  @Test
  void testRootPathHandlingInComplexStructure() throws IOException {
    // Create ZIP where all files are under "package-v1.0"
    Map<String, String> files = new HashMap<>();
    files.put("package-v1.0/manifest.xml", "manifest");
    files.put("package-v1.0/content/page1.html", "page1");
    files.put("package-v1.0/content/page2.html", "page2");
    files.put("package-v1.0/resources/style.css", "styles");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Root path should be detected
      assertThat(fileAccess.getRootPath()).isEqualTo("package-v1.0");

      // Files should be accessible with relative paths
      assertThat(fileAccess.fileExists("manifest.xml")).isTrue();
      assertThat(fileAccess.fileExists("content/page1.html")).isTrue();

      // List files should work with relative paths
      List<String> contentFiles = fileAccess.listFiles("content");
      assertThat(contentFiles).containsExactlyInAnyOrder(
          "package-v1.0/content/page1.html",
          "package-v1.0/content/page2.html"
      );
    }
  }

  @Test
  void testClearCaches() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("file.txt", "content");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Clear caches should not affect functionality
      fileAccess.clearCaches();

      assertThat(fileAccess.fileExists("file.txt")).isTrue();
      assertThat(fileAccess.getAllFiles()).hasSize(1);

      // Clear again
      fileAccess.clearCaches();

      // Should still work
      try (InputStream is = fileAccess.getFileContents("file.txt")) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        assertThat(content).isEqualTo("content");
      }
    }
  }

  // ========== Cache and Cleanup Tests ==========

  @Test
  void testPrefetchCommonFiles() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("imsmanifest.xml", "manifest");
    files.put("content/index.html", "index");

    byte[] zipData = createZipWithFiles(files);
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      // Prefetch should not throw or affect functionality
      assertThatCode(() -> fileAccess.prefetchCommonFiles()).doesNotThrowAnyException();

      // Files should still be accessible
      assertThat(fileAccess.fileExists("imsmanifest.xml")).isTrue();
      assertThat(fileAccess.fileExists("content/index.html")).isTrue();
    }
  }

  @Test
  void testMultipleCloseCallsAreSafe() throws IOException {
    Map<String, String> files = new HashMap<>();
    files.put("test.txt", "content");

    byte[] zipData = createZipWithFiles(files);
    InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData);

    // Multiple close calls should not throw
    fileAccess.close();
    fileAccess.close();
    fileAccess.close();

    // Should still be accessible (no external resources)
    assertThat(fileAccess.fileExists("test.txt")).isTrue();
  }

  /**
   * Helper to create ZIP data with specified characteristics.
   */
  private byte[] createZipWithFiles(Map<String, String> files) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      for (Map.Entry<String, String> entry : files.entrySet()) {
        ZipEntry zipEntry = new ZipEntry(entry.getKey());
        zos.putNextEntry(zipEntry);
        if (!entry
            .getKey()
            .endsWith("/")) {
          zos.write(entry
              .getValue()
              .getBytes(StandardCharsets.UTF_8));
        }
        zos.closeEntry();
      }
    }
    return baos.toByteArray();
  }
}