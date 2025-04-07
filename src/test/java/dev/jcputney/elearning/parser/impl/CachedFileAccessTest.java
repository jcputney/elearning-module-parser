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

package dev.jcputney.elearning.parser.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.FileAccessException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CachedFileAccess} class.
 */
class CachedFileAccessTest {

  private TrackingMockFileAccess mockFileAccess;
  private CachedFileAccess cachedFileAccess;

  @BeforeEach
  void setUp() {
    mockFileAccess = new TrackingMockFileAccess("root/path");
    cachedFileAccess = new CachedFileAccess(mockFileAccess);
  }

  @Test
  void constructorWithNullDelegateThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new CachedFileAccess(null));
  }

  @Test
  void getRootPathDelegatesToUnderlyingImplementation() {
    String rootPath = cachedFileAccess.getRootPath();

    assertEquals("root/path", rootPath);
    assertEquals(1, mockFileAccess.getRootPathCallCount());
  }

  @Test
  void fileExistsCachesMisses() {
    mockFileAccess.setFileExistsResponse("file1.txt", true);

    // First call should miss the cache
    boolean exists1 = cachedFileAccess.fileExists("file1.txt");
    assertTrue(exists1);
    assertEquals(1, mockFileAccess.getFileExistsCallCount("file1.txt"));

    // Second call should hit the cache
    boolean exists2 = cachedFileAccess.fileExists("file1.txt");
    assertTrue(exists2);
    assertEquals(1, mockFileAccess.getFileExistsCallCount("file1.txt"));
  }

  @Test
  void listFilesCachesMisses() throws IOException {
    List<String> files = Arrays.asList("file1.txt", "file2.txt");
    mockFileAccess.setListFilesResponse("dir", files);

    // First call should miss the cache
    List<String> result1 = cachedFileAccess.listFiles("dir");
    assertEquals(files, result1);
    assertEquals(1, mockFileAccess.getListFilesCallCount("dir"));

    // Second call should hit the cache
    List<String> result2 = cachedFileAccess.listFiles("dir");
    assertEquals(files, result2);
    assertEquals(1, mockFileAccess.getListFilesCallCount("dir"));
  }

  @Test
  void getFileContentsCachesMisses() throws IOException {
    byte[] contents = "file contents".getBytes();
    mockFileAccess.setFileContentsResponse("file.txt", contents);

    // First call should miss the cache
    InputStream stream1 = cachedFileAccess.getFileContents("file.txt");
    byte[] result1 = stream1.readAllBytes();
    assertArrayEquals(contents, result1);
    assertEquals(1, mockFileAccess.getFileContentsCallCount("file.txt"));

    // Second call should hit the cache
    InputStream stream2 = cachedFileAccess.getFileContents("file.txt");
    byte[] result2 = stream2.readAllBytes();
    assertArrayEquals(contents, result2);
    assertEquals(1, mockFileAccess.getFileContentsCallCount("file.txt"));
  }

  @Test
  void clearCacheClearsAllCaches() throws IOException {
    // Set up responses
    mockFileAccess.setFileExistsResponse("file.txt", true);
    List<String> files = Arrays.asList("file1.txt", "file2.txt");
    mockFileAccess.setListFilesResponse("dir", files);
    byte[] contents = "file contents".getBytes();
    mockFileAccess.setFileContentsResponse("file.txt", contents);

    // First calls should miss the cache
    cachedFileAccess.fileExists("file.txt");
    cachedFileAccess.listFiles("dir");
    cachedFileAccess.getFileContents("file.txt");

    // Clear the cache
    cachedFileAccess.clearCache();

    // Second calls should miss the cache again
    cachedFileAccess.fileExists("file.txt");
    cachedFileAccess.listFiles("dir");
    cachedFileAccess.getFileContents("file.txt");

    // Verify that the delegate was called twice for each method
    assertEquals(2, mockFileAccess.getFileExistsCallCount("file.txt"));
    assertEquals(2, mockFileAccess.getListFilesCallCount("dir"));
    assertEquals(2, mockFileAccess.getFileContentsCallCount("file.txt"));
  }

  @Test
  void clearCacheWithPathClearsCacheForSpecificPath() {
    // Set up responses
    mockFileAccess.setFileExistsResponse("file1.txt", true);
    mockFileAccess.setFileExistsResponse("file2.txt", true);

    // First calls should miss the cache
    cachedFileAccess.fileExists("file1.txt");
    cachedFileAccess.fileExists("file2.txt");

    // Clear the cache for file1.txt only
    cachedFileAccess.clearCache("file1.txt");

    // Second calls
    cachedFileAccess.fileExists("file1.txt"); // Should miss the cache
    cachedFileAccess.fileExists("file2.txt"); // Should hit the cache

    // Verify that the delegate was called twice for file1.txt but only once for file2.txt
    assertEquals(2, mockFileAccess.getFileExistsCallCount("file1.txt"));
    assertEquals(1, mockFileAccess.getFileExistsCallCount("file2.txt"));
  }

  @Test
  void clearCacheWithNullPathThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cachedFileAccess.clearCache(null));
  }

  @Test
  void listFilesHandlesIOException() {
    // Set up the mock to throw an IOException
    mockFileAccess = new TrackingMockFileAccess("root/path") {
      @Override
      public List<String> listFilesInternal(String directoryPath) throws IOException {
        super.listFilesInternal(directoryPath);
        throw new IOException("Test exception");
      }
    };
    cachedFileAccess = new CachedFileAccess(mockFileAccess);

    // Verify that the exception is properly wrapped and thrown
    IOException exception = assertThrows(IOException.class,
        () -> cachedFileAccess.listFiles("dir"));

    // Check that the exception message contains the expected text
    assertTrue(exception.getMessage().contains("Error listing files in directory"));
    // The cause is a FileAccessException which wraps the original IOException
    assertInstanceOf(FileAccessException.class, exception.getCause());
    // The original exception is wrapped inside the FileAccessException
    assertInstanceOf(IOException.class, exception.getCause().getCause());
    assertEquals("Test exception", exception.getCause().getCause().getMessage());
  }

  @Test
  void getFileContentsHandlesIOException() {
    // Set up the mock to throw an IOException
    mockFileAccess = new TrackingMockFileAccess("root/path") {
      @Override
      public InputStream getFileContentsInternal(String path) throws IOException {
        super.getFileContentsInternal(path);
        throw new IOException("Test exception");
      }
    };
    cachedFileAccess = new CachedFileAccess(mockFileAccess);

    // Verify that the exception is properly wrapped and thrown
    IOException exception = assertThrows(IOException.class,
        () -> cachedFileAccess.getFileContents("file.txt"));

    // Check that the exception message contains the expected text
    assertTrue(exception.getMessage().contains("Error reading file contents for path"));
    // The cause is a FileAccessException which wraps the original IOException
    assertInstanceOf(FileAccessException.class, exception.getCause());
    // The original exception is wrapped inside the FileAccessException
    assertInstanceOf(IOException.class, exception.getCause().getCause());
    assertEquals("Test exception", exception.getCause().getCause().getMessage());
  }

  @Test
  void fileExistsWithNullPathThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cachedFileAccess.fileExists(null));
  }

  @Test
  void listFilesWithNullPathThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cachedFileAccess.listFiles(null));
  }

  @Test
  void getFileContentsWithNullPathThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cachedFileAccess.getFileContents(null));
  }

  @Test
  void fullPathWithRelativePathReturnsPathWithRootPrefix() {
    assertEquals("root/path/file.txt", cachedFileAccess.fullPath("file.txt"));
  }

  @Test
  void fullPathWithAbsolutePathReturnsPathWithoutLeadingSlash() {
    assertEquals("file.txt", cachedFileAccess.fullPath("/file.txt"));
  }

  @Test
  void fullPathWithNullPathThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cachedFileAccess.fullPath(null));
  }

  @Test
  void fullPathWithEmptyRootPathReturnsPathAsIs() {
    // Set up a mock with empty root path
    mockFileAccess = new TrackingMockFileAccess("");
    cachedFileAccess = new CachedFileAccess(mockFileAccess);

    assertEquals("file.txt", cachedFileAccess.fullPath("file.txt"));
  }

  @Test
  void fullPathWithMultipleSlashesHandlesPathCorrectly() {
    // Test with path containing multiple slashes
    // Note: fullPath() doesn't normalize multiple slashes in the middle of the path
    assertEquals("root/path/dir//file.txt", cachedFileAccess.fullPath("dir//file.txt"));
    // Note: fullPath() only removes the first slash when a path starts with a slash
    assertEquals("/dir/file.txt", cachedFileAccess.fullPath("//dir/file.txt"));
  }

  @Test
  void fullPathWithParentDirectoryReferencesReturnsPathAsIs() {
    // Note: fullPath() doesn't resolve parent directory references
    assertEquals("root/path/../file.txt", cachedFileAccess.fullPath("../file.txt"));
    assertEquals("root/path/dir/../file.txt", cachedFileAccess.fullPath("dir/../file.txt"));
  }

  @Test
  void fullPathWithCurrentDirectoryReferencesReturnsPathAsIs() {
    // Note: fullPath() doesn't resolve current directory references
    assertEquals("root/path/./file.txt", cachedFileAccess.fullPath("./file.txt"));
    assertEquals("root/path/dir/./file.txt", cachedFileAccess.fullPath("dir/./file.txt"));
  }

  @Test
  void fullPathWithMixedPathElementsHandlesPathCorrectly() {
    // Test with a path containing both relative and absolute elements
    assertEquals("dir/subdir/file.txt", cachedFileAccess.fullPath("/dir/subdir/file.txt"));
    assertEquals("root/path/dir/../subdir/./file.txt",
        cachedFileAccess.fullPath("dir/../subdir/./file.txt"));
  }

  @Test
  void fullPathWithSpecialCharactersHandlesPathCorrectly() {
    // Test with a path containing spaces and special characters
    assertEquals("root/path/my file.txt", cachedFileAccess.fullPath("my file.txt"));
    assertEquals("root/path/file-with-dashes.txt",
        cachedFileAccess.fullPath("file-with-dashes.txt"));
    assertEquals("root/path/file_with_underscores.txt",
        cachedFileAccess.fullPath("file_with_underscores.txt"));
    assertEquals("root/path/file with spaces and symbols !@#$%.txt",
        cachedFileAccess.fullPath("file with spaces and symbols !@#$%.txt"));
  }

  @Test
  void fullPathWithVeryLongPathHandlesPathCorrectly() {
    // Create a very long path (over 255 characters)
    StringBuilder longPathBuilder = new StringBuilder();
    for (int i = 0; i < 20; i++) {
      longPathBuilder.append("very_long_directory_name_");
    }
    longPathBuilder.append("file.txt");
    String longPath = longPathBuilder.toString();

    assertEquals("root/path/" + longPath, cachedFileAccess.fullPath(longPath));
  }

  @Test
  void fullPathWithPathEndingWithSlashHandlesPathCorrectly() {
    // Test with a path ending with a slash
    assertEquals("root/path/directory/", cachedFileAccess.fullPath("directory/"));
    assertEquals("directory/", cachedFileAccess.fullPath("/directory/"));
  }

  @Test
  void fullPathWithEmptyPathReturnsRootPath() {
    // Test with an empty path
    assertEquals("root/path/", cachedFileAccess.fullPath(""));
  }

  @Test
  void concurrentAccessEnsuresThreadSafety() throws InterruptedException {
    // Set up responses
    mockFileAccess.setFileExistsResponse("file.txt", true);
    byte[] contents = "file contents".getBytes();
    mockFileAccess.setFileContentsResponse("file.txt", contents);

    final int threadCount = 10;
    final int operationsPerThread = 100;
    final CountDownLatch startLatch = new CountDownLatch(1);
    final CountDownLatch finishLatch = new CountDownLatch(threadCount);
    final AtomicInteger exceptionCount = new AtomicInteger(0);

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    // Create and submit tasks
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          startLatch.await(); // Wait for all threads to be ready

          for (int j = 0; j < operationsPerThread; j++) {
            // Perform a mix of operations
            if (j % 3 == 0) {
              cachedFileAccess.fileExists("file.txt");
            } else if (j % 3 == 1) {
              try {
                cachedFileAccess.getFileContents("file.txt").close();
              } catch (IOException e) {
                exceptionCount.incrementAndGet();
              }
            } else {
              cachedFileAccess.clearCache("file.txt");
            }
          }
        } catch (Exception e) {
          exceptionCount.incrementAndGet();
        } finally {
          finishLatch.countDown();
        }
      });
    }

    // Start all threads simultaneously
    startLatch.countDown();

    // Wait for all threads to complete
    boolean completed = finishLatch.await(30, TimeUnit.SECONDS);
    executor.shutdown();

    // Verify no exceptions occurred and all threads completed
    assertTrue(completed, "Not all threads completed in time");
    assertEquals(0, exceptionCount.get(), "Exceptions occurred during concurrent access");

    // Verify the delegate was called the expected number of times
    // The exact number depends on the interleaving of threads and cache clearing operations
    assertTrue(mockFileAccess.getFileExistsCallCount("file.txt") > 0,
        "Delegate should have been called at least once for fileExists");
    assertTrue(mockFileAccess.getFileContentsCallCount("file.txt") > 0,
        "Delegate should have been called at least once for getFileContents");
  }

  @Test
  void largeFileHandlingCachesContentCorrectly() throws IOException {
    // Create a large file content (1MB)
    byte[] largeContent = new byte[1024 * 1024];
    for (int i = 0; i < largeContent.length; i++) {
      largeContent[i] = (byte) (i % 256);
    }

    mockFileAccess.setFileContentsResponse("large-file.txt", largeContent);

    // First access should cache the content
    try (InputStream stream1 = cachedFileAccess.getFileContents("large-file.txt")) {
      byte[] result1 = stream1.readAllBytes();
      assertArrayEquals(largeContent, result1);
    }

    assertEquals(1, mockFileAccess.getFileContentsCallCount("large-file.txt"));

    // Second access should use the cached content
    try (InputStream stream2 = cachedFileAccess.getFileContents("large-file.txt")) {
      byte[] result2 = stream2.readAllBytes();
      assertArrayEquals(largeContent, result2);
    }

    assertEquals(1, mockFileAccess.getFileContentsCallCount("large-file.txt"));
  }

  /**
   * A mock implementation of {@link FileAccess} that tracks method calls and allows customizing
   * responses for testing purposes.
   */
  private static class TrackingMockFileAccess implements FileAccess {

    private final String rootPath;
    private final Map<String, Boolean> fileExistsResponses = new HashMap<>();
    private final Map<String, List<String>> listFilesResponses = new HashMap<>();
    private final Map<String, byte[]> fileContentsResponses = new HashMap<>();
    private final Map<String, Integer> fileExistsCallCounts = new HashMap<>();
    private final Map<String, Integer> listFilesCallCounts = new HashMap<>();
    private final Map<String, Integer> fileContentsCallCounts = new HashMap<>();
    // Counters to track method calls
    private int getRootPathCallCount = 0;

    public TrackingMockFileAccess(String rootPath) {
      this.rootPath = rootPath;
    }

    public void setFileExistsResponse(String path, boolean exists) {
      fileExistsResponses.put(path, exists);
    }

    public void setListFilesResponse(String path, List<String> files) {
      listFilesResponses.put(path, files);
    }

    public void setFileContentsResponse(String path, byte[] contents) {
      fileContentsResponses.put(path, contents);
    }

    public int getRootPathCallCount() {
      return getRootPathCallCount;
    }

    public int getFileExistsCallCount(String path) {
      return fileExistsCallCounts.getOrDefault(path, 0);
    }

    public int getListFilesCallCount(String path) {
      return listFilesCallCounts.getOrDefault(path, 0);
    }

    public int getFileContentsCallCount(String path) {
      return fileContentsCallCounts.getOrDefault(path, 0);
    }

    @Override
    public String getRootPath() {
      getRootPathCallCount++;
      return rootPath;
    }

    @Override
    public boolean fileExistsInternal(String path) {
      fileExistsCallCounts.put(path, fileExistsCallCounts.getOrDefault(path, 0) + 1);
      return fileExistsResponses.getOrDefault(path, false);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      listFilesCallCounts.put(directoryPath,
          listFilesCallCounts.getOrDefault(directoryPath, 0) + 1);
      return listFilesResponses.getOrDefault(directoryPath, Collections.emptyList());
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      fileContentsCallCounts.put(path, fileContentsCallCounts.getOrDefault(path, 0) + 1);
      byte[] contents = fileContentsResponses.getOrDefault(path, new byte[0]);
      return new ByteArrayInputStream(contents);
    }
  }
}
