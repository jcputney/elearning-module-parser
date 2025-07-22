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
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Optimized implementation of FileAccess using AWS S3 SDK v2 with batch operations, streaming
 * support, and intelligent caching.
 */
@SuppressWarnings("unused")
public class S3FileAccessV2 extends AbstractS3FileAccess {

  private final S3Client s3Client;
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

  @Override
  protected InputStream getInputStreamWrapper(InputStream stream, long fileSize) {
    // Get the progress listener from thread-local storage
    StreamingProgressListener progressListener = currentProgressListener.get();
    if (progressListener != null) {
      return StreamingUtils.createEnhancedStream(stream, fileSize, progressListener);
    }
    return stream;
  }

  // SDK-specific implementations

  @Override
  protected boolean checkFileExistsOnS3(String path) {
    try {
      s3Client.headObject(HeadObjectRequest
          .builder()
          .bucket(bucketName)
          .key(fullPath(path))
          .build());
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    } catch (SdkException e) {
      // Failed to check file existence
      return false;
    }
  }

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

  @Override
  protected List<String> listFilesOnS3(String directoryPath) {
    try {
      List<String> allKeys = new ArrayList<>();
      String prefix = fullPath(directoryPath);

      ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request
          .builder()
          .bucket(bucketName)
          .prefix(prefix)
          .maxKeys(1000);

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
            .toList());

        continuationToken = response.nextContinuationToken();
      } while (continuationToken != null);

      // Listed files in a directory
      return allKeys;
    } catch (SdkException e) {
      // Failed to list files in the S3 directory
      return List.of();
    }
  }

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
}
