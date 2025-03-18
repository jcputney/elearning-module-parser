/*
 * Copyright (c) 2024. Jonathan Putney
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
import java.util.List;
import lombok.Getter;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Implementation of FileAccess using AWS S3 SDK v2.
 */
@SuppressWarnings("unused")
public class S3FileAccessV2 implements FileAccess {

  private final S3Client s3Client;
  private final String bucketName;

  @Getter
  private final String rootPath;

  /**
   * Constructs an S3FileAccessV2 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   */
  public S3FileAccessV2(S3Client s3Client, String bucketName, String rootPath) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
    if (rootPath == null) {
      rootPath = "";
    }

    rootPath = getInternalRootDirectory(rootPath);

    if (rootPath.endsWith("/")) {
      rootPath = rootPath.substring(0, rootPath.length() - 1);
    }
    this.rootPath = rootPath;
  }

  /**
   * Checks if a file exists at the specified path.
   *
   * @param path The path of the file to check.
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExists(String path) {
    try {
      s3Client.headObject(HeadObjectRequest.builder()
          .bucket(bucketName)
          .key(fullPath(path))
          .build());
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    }
  }

  /**
   * Lists the files in the specified directory path.
   *
   * @param directoryPath The path of the directory to list files from.
   * @return A list of file paths in the specified directory.
   * @throws IOException If an error occurs while listing files.
   */
  @Override
  public List<String> listFiles(String directoryPath) throws IOException {
    try {
      return s3Client.listObjectsV2(ListObjectsV2Request.builder()
              .bucket(bucketName)
              .prefix(fullPath(directoryPath))
              .build())
          .contents()
          .stream()
          .map(S3Object::key)
          .toList();
    } catch (SdkException e) {
      throw new IOException("Failed to list files in directory: " + directoryPath, e);
    }
  }

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path of the file to get contents from.
   * @return An InputStream containing the file contents.
   * @throws IOException If an error occurs while getting file contents.
   */
  @Override
  public InputStream getFileContents(String path) throws IOException {
    try {
      var response = s3Client.getObjectAsBytes(builder -> {
        builder.bucket(bucketName);
        builder.key(fullPath(path));
      });
      return new ByteArrayInputStream(response.asByteArray());
    } catch (S3Exception e) {
      throw new IOException("Failed to get file contents for path: " + path, e);
    }
  }

  public String getInternalRootDirectory(String rootPath) {
    List<CommonPrefix> commonPrefixes = s3Client.listObjectsV2(ListObjectsV2Request.builder()
            .bucket(bucketName)
            .prefix(rootPath)
            .delimiter("/")
            .build())
        .commonPrefixes();
    if (commonPrefixes.size() != 1) {
      return rootPath;
    }
    return commonPrefixes.get(0).prefix();
  }
}