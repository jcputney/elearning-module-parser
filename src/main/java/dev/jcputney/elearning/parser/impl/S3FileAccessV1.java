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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import lombok.Getter;

/**
 * Implementation of FileAccess using AWS S3 SDK v1.
 */
@SuppressWarnings("unused")
public class S3FileAccessV1 implements FileAccess {

  private final AmazonS3 s3Client;
  private final String bucketName;

  @Getter
  private final String rootPath;

  /**
   * Constructs an S3FileAccessV1 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   */
  public S3FileAccessV1(AmazonS3 s3Client, String bucketName, String rootPath) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;

    String processedPath = rootPath;
    if (processedPath == null) {
      processedPath = "";
    }

    processedPath = getInternalRootDirectory(processedPath);

    if (processedPath.endsWith("/")) {
      processedPath = processedPath.substring(0, processedPath.length() - 1);
    }
    this.rootPath = processedPath;
  }

  /**
   * Checks if a file exists at the specified path.
   *
   * @param path The path of the file to check (guaranteed to be non-null).
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    return s3Client.doesObjectExist(bucketName, fullPath(path));
  }

  /**
   * Lists the files in the specified directory path.
   *
   * @param directoryPath The path of the directory to list files from (guaranteed to be non-null).
   * @return A list of file paths in the specified directory.
   */
  @Override
  public List<String> listFilesInternal(String directoryPath) {
    return s3Client.listObjects(bucketName, fullPath(directoryPath)).getObjectSummaries().stream()
        .map(S3ObjectSummary::getKey)
        .toList();
  }

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path of the file to get contents from (guaranteed to be non-null).
   * @return An InputStream containing the file contents.
   */
  @Override
  public InputStream getFileContentsInternal(String path) {
    String content = s3Client.getObjectAsString(bucketName, fullPath(path));
    return new ByteArrayInputStream(content.getBytes());
  }

  /**
   * Determines the internal root directory within the S3 bucket.
   *
   * <p>This method checks if there is a single common prefix at the specified path,
   * which indicates a directory structure. If found, it returns that prefix as the root. Otherwise,
   * it returns the original path.</p>
   *
   * @param rootPath The initial root path to check.
   * @return The detected internal root directory or the original path if none is detected.
   */
  public String getInternalRootDirectory(String rootPath) {
    List<String> commonPrefixes = s3Client.listObjects(bucketName, fullPath(rootPath))
        .getCommonPrefixes();
    if (commonPrefixes.size() != 1) {
      return rootPath;
    }
    return commonPrefixes.get(0);
  }
}
