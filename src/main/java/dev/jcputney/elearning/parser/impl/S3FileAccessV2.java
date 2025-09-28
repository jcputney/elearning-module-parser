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

import dev.jcputney.elearning.parser.api.StreamingProgressListener;
import dev.jcputney.elearning.parser.util.StreamingUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Optimized implementation of FileAccess using AWS S3 SDK v2 with batch operations, streaming
 * support, and intelligent caching.
 */
public class S3FileAccessV2 extends AbstractS3FileAccess {

  private final S3Client s3Client;
  private final boolean eagerCache;
  private final ThreadLocal<StreamingProgressListener> currentProgressListener = new ThreadLocal<>();

  /**
   * Constructs an optimized S3FileAccessV2 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   */
  public S3FileAccessV2(S3Client s3Client, String bucketName, String rootPath) {
    this(s3Client, bucketName, rootPath, true);
  }

  /**
   * Constructs an optimized S3FileAccessV2 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   * @param eagerCache Whether to eagerly cache all files on initialization.
   */
  public S3FileAccessV2(S3Client s3Client, String bucketName, String rootPath, boolean eagerCache) {
    super(bucketName, rootPath);  // Don't eager-cache in parent constructor
    this.s3Client = s3Client;
    this.eagerCache = eagerCache;

    if (eagerCache) {
      // Eagerly cache all files to avoid individual S3 API calls later
      try {
        // Eagerly caching all files for S3 bucket
        getAllFiles();
      } catch (IOException e) {
        // Failed to eagerly cache files from S3, will fall back to lazy loading
      }
    }
  }

  /**
   * Prepare this file access instance for interacting with a specific module root. Clears any
   * cached state and optionally re-populates caches when eager caching is enabled.
   *
   * @param moduleRoot The module prefix/key to scope subsequent operations to.
   */
  public void prepareForModule(String moduleRoot) {
    reconfigureRootPath(moduleRoot);
    if (eagerCache) {
      try {
        getAllFiles();
      } catch (IOException e) {
        // Failed to eagerly cache files from S3, will fall back to lazy loading
      }
    }
  }


  // Override to add progress listener support
  @Override
  public InputStream getFileContentsInternal(String path) throws IOException {
    return getFileContentsInternal(path, null);
  }

  /**
   * Gets the contents of a file as an InputStream with optional progress tracking.
   *
   * @param path The path of the file to get contents from (guaranteed to be non-null).
   * @param progressListener Optional progress listener for tracking large file operations.
   * @return An InputStream containing the file contents.
   * @throws IOException If an error occurs while getting file contents.
   */
  public InputStream getFileContentsInternal(String path,
      StreamingProgressListener progressListener) throws IOException {
    // Store the progress listener in a thread-local for the wrapper to use
    this.currentProgressListener.set(progressListener);
    try {
      // Use the base implementation which will call our getInputStreamWrapper
      return super.getFileContentsBase(path);
    } finally {
      // Clear the thread-local after use
      this.currentProgressListener.remove();
    }
  }

  /**
   * Wraps the provided InputStream to add optional progress tracking functionality if a
   * StreamingProgressListener is available in the thread-local context. If no listener is set, the
   * original InputStream is returned unmodified.
   *
   * @param stream The InputStream to be wrapped or returned as-is if no progress listener is
   * found.
   * @param fileSize The size of the file being read, in bytes, or -1 if unknown.
   * @return An InputStream that may provide progress tracking via a wrapped implementation, or the
   * original InputStream if no listener is available.
   */
  @Override
  protected InputStream getInputStreamWrapper(InputStream stream, long fileSize) {
    // Get the progress listener from thread-local storage
    StreamingProgressListener progressListener = currentProgressListener.get();
    if (progressListener != null) {
      return StreamingUtils.createEnhancedStream(stream, fileSize, progressListener);
    }
    return stream;
  }

  /**
   * Checks if a file exists in the S3 bucket at the specified path.
   *
   * @param path The relative path of the file to check within the S3 bucket.
   * @return true if the file exists on S3, otherwise false.
   */
  @Override
  protected boolean checkFileExistsOnS3(String path) {
    try {
      s3Client.headObject(HeadObjectRequest
          .builder()
          .bucket(bucketName)
          .key(fullPath(path))
          .build());
      return true;
    } catch (SdkException e) {
      // Failed to check file existence
      return false;
    }
  }

  /**
   * Retrieves the file size of a specified file stored on S3.
   *
   * @param path The relative path of the file within the S3 bucket.
   * @return The size of the file in bytes if found, or 0 if the file does not exist or an error
   * occurs.
   */
  @Override
  protected long getFileSizeOnS3(String path) {
    try {
      return s3Client
          .headObject(HeadObjectRequest
              .builder()
              .bucket(bucketName)
              .key(fullPath(path))
              .build())
          .contentLength();
    } catch (SdkException e) {
      // Failed to get file size
      return 0;
    }
  }

  /**
   * Lists all files in a specified directory within an S3 bucket. The method recursively retrieves
   * all file keys under the specified directory path, filtering out directory markers or irrelevant
   * paths.
   *
   * @param directoryPath The relative path of the directory within the S3 bucket to list files
   * from.
   * @return A list of file keys representing the files in the specified directory. If an error
   * occurs during the operation, an empty list is returned.
   */
  @Override
  protected List<String> listFilesOnS3(String directoryPath) {
    try {
      List<String> allKeys = new ArrayList<>();
      String prefix = buildDirectoryPrefix(directoryPath);

      final int DEFAULT_MAX_KEYS = 1000;
      ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request
          .builder()
          .bucket(bucketName)
          .prefix(prefix)
          .maxKeys(DEFAULT_MAX_KEYS);

      String continuationToken = null;
      do {
        if (continuationToken != null) {
          requestBuilder.continuationToken(continuationToken);
        }

        ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
        allKeys.addAll(response
            .contents()
            .stream()
            .map(S3Object::key)
            .filter(key -> !key.endsWith("/")) // Filter out directory markers
            .filter(this::shouldIncludeKey)
            .map(this::toRelativeKey)
            .toList());

        continuationToken = response.nextContinuationToken();
      } while (continuationToken != null);

      return allKeys;
    } catch (SdkException e) {
      return List.of();
    }
  }

  /**
   * Retrieves an object stored in S3 as a byte array.
   *
   * @param fullPath The full path of the object in the S3 bucket.
   * @return A byte array containing the data of the specified object from S3.
   * @throws IOException If an error occurs while fetching the object from S3.
   */
  @Override
  protected byte[] getS3ObjectAsBytes(String fullPath) throws IOException {
    try {
      return s3Client
          .getObjectAsBytes(builder -> {
            builder.bucket(bucketName);
            builder.key(fullPath);
          })
          .asByteArray();
    } catch (S3Exception e) {
      throw new IOException("Failed to get object as bytes from S3: " + fullPath, e);
    }
  }

  /**
   * Retrieves the InputStream of an object stored in S3 at the specified full path.
   *
   * @param fullPath The full path of the object in the S3 bucket, including directories and file
   * name.
   * @return An InputStream allowing access to the content of the object stored in S3.
   * @throws IOException If an error occurs while accessing the object in S3.
   */
  @Override
  protected InputStream getS3ObjectStream(String fullPath) throws IOException {
    try {
      return s3Client.getObject(GetObjectRequest
          .builder()
          .bucket(bucketName)
          .key(fullPath)
          .build());
    } catch (S3Exception e) {
      throw new IOException("Failed to get object stream from S3: " + fullPath, e);
    }
  }

  /**
   * Detects the internal root directory from a given root path by querying the S3 bucket and
   * inspecting the common prefixes. If exactly one common prefix is found, it is returned as the
   * internal root directory; otherwise, the provided rootPath is returned. This method is used to
   * resolve a potential internal hierarchy structure within the provided root path.
   *
   * @param rootPath The initial root path within the S3 bucket to detect the internal root
   * directory from.
   * @return The detected internal root directory based on the common prefixes in the S3 bucket. If
   * detection fails or no single common prefix is found, the original rootPath is returned.
   */
  @Override
  protected String detectInternalRootDirectory(String rootPath) {
    try {
      List<CommonPrefix> commonPrefixes = s3Client
          .listObjectsV2(ListObjectsV2Request
              .builder()
              .bucket(bucketName)
              .prefix(rootPath)
              .delimiter("/")
              .build())
          .commonPrefixes();
      if (commonPrefixes.size() != 1) {
        return rootPath;
      }
      return commonPrefixes
          .get(0)
          .prefix();
    } catch (SdkException e) {
      // Failed to detect internal root directory
      return rootPath;
    }
  }

  /**
   * Builds a directory prefix for the given directory path, ensuring that it ends with a slash if
   * it is not empty and does not already end with one.
   *
   * @param directoryPath The directory path for which to build the prefix.
   * @return The directory prefix derived from the input path, guaranteed to end with a slash unless
   * the input is empty.
   */
  private String buildDirectoryPrefix(String directoryPath) {
    String prefix = fullPath(directoryPath);
    if (!prefix.isEmpty() && !prefix.endsWith("/")) {
      prefix = prefix + "/";
    }
    return prefix;
  }

  /**
   * Determines if the specified key should be included based on the configured root path. If no
   * root path is set or the key matches the root path or starts with the root path followed by a
   * slash, the key is included.
   *
   * @param key The key to evaluate for inclusion.
   * @return true if the key should be included, otherwise false.
   */
  private boolean shouldIncludeKey(String key) {
    if (rootPath == null || rootPath.isEmpty()) {
      return true;
    }
    return key.equals(rootPath) || key.startsWith(rootPath + "/");
  }

  /**
   * Converts an absolute S3 key to a relative key by removing the configured root path, if
   * applicable. If the root path is not set or does not match the start of the key, the original
   * key is returned unchanged.
   *
   * @param key The absolute S3 key to convert to a relative key. Must not be null.
   * @return The relative key obtained by removing the root path from the absolute key, or the
   * original key if no root path is set or it does not match the beginning of the key.
   */
  private String toRelativeKey(String key) {
    if (rootPath == null || rootPath.isEmpty()) {
      return key;
    }
    return key.startsWith(rootPath + "/") ? key.substring(rootPath.length() + 1) : key;
  }
}
