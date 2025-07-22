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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Optimized implementation of FileAccess using AWS S3 SDK v1 with batch operations, streaming
 * support, and intelligent caching.
 */
@SuppressWarnings("unused")
public class S3FileAccessV1 extends AbstractS3FileAccess {

  private final AmazonS3 s3Client;

  /**
   * Constructs an optimized S3FileAccessV1 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   */
  public S3FileAccessV1(AmazonS3 s3Client, String bucketName, String rootPath) {
    this(s3Client, bucketName, rootPath, true);
  }

  /**
   * Constructs an optimized S3FileAccessV1 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   * @param eagerCache Whether to eagerly cache all files on initialization.
   */
  public S3FileAccessV1(AmazonS3 s3Client, String bucketName, String rootPath, boolean eagerCache) {
    super(bucketName, rootPath);  // Don't eager-cache in parent constructor
    this.s3Client = s3Client;

    if (eagerCache) {
      // Eagerly cache all files to avoid individual S3 API calls later
      try {
        // Eagerly caching all files for S3 bucket with a prefix
        getAllFiles();
      } catch (IOException e) {
        // Failed to eagerly cache files from S3, will fall back to lazy loading
      }
    }
  }

  // SDK-specific implementations

  @Override
  protected boolean checkFileExistsOnS3(String path) {
    try {
      return s3Client.doesObjectExist(bucketName, fullPath(path));
    } catch (AmazonServiceException e) {
      // Failed to check file existence
      return false;
    }
  }

  @Override
  protected long getFileSizeOnS3(String path) {
    try {
      return s3Client
          .getObjectMetadata(bucketName, fullPath(path))
          .getContentLength();
    } catch (AmazonServiceException e) {
      // Failed to get file size
      return 0;
    }
  }

  @Override
  protected List<String> listFilesOnS3(String directoryPath) {
    try {
      List<String> allKeys = new ArrayList<>();
      String prefix = fullPath(directoryPath);

      ListObjectsRequest request = new ListObjectsRequest()
          .withBucketName(bucketName)
          .withPrefix(prefix)
          .withMaxKeys(1000);

      ObjectListing listing;
      do {
        listing = s3Client.listObjects(request);
        allKeys.addAll(listing
            .getObjectSummaries()
            .stream()
            .map(S3ObjectSummary::getKey)
            .filter(key -> !key.endsWith("/")) // Filter out directory markers
            .toList());
        request.setMarker(listing.getNextMarker());
      } while (listing.isTruncated());

      // Listed files in a directory
      return allKeys;
    } catch (AmazonServiceException e) {
      // Failed to list files in a directory
      return List.of();
    }
  }

  @Override
  protected byte[] getS3ObjectAsBytes(String fullPath) throws IOException {
    return s3Client
        .getObjectAsString(bucketName, fullPath)
        .getBytes();
  }

  @Override
  protected InputStream getS3ObjectStream(String fullPath) throws IOException {
    S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fullPath));
    return s3Object.getObjectContent();
  }

  @Override
  protected String detectInternalRootDirectory(String rootPath) {
    try {
      List<String> commonPrefixes = s3Client
          .listObjects(bucketName, fullPath(rootPath))
          .getCommonPrefixes();
      if (commonPrefixes.size() != 1) {
        return rootPath;
      }
      return commonPrefixes.get(0);
    } catch (AmazonServiceException e) {
      // Failed to detect internal root directory
      return rootPath;
    }
  }

  @Override
  protected InputStream getInputStreamWrapper(InputStream stream, long fileSize) {
    // S3FileAccessV1 doesn't support progress listeners, so just return the stream as-is
    return stream;
  }
}
