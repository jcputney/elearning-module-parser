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
    return s3Client.doesObjectExist(bucketName, fullPath(path));
  }

  /**
   * Lists the files in the specified directory path.
   *
   * @param directoryPath The path of the directory to list files from.
   * @return A list of file paths in the specified directory.
   */
  @Override
  public List<String> listFiles(String directoryPath) {
    return s3Client.listObjects(bucketName, fullPath(directoryPath)).getObjectSummaries().stream()
        .map(S3ObjectSummary::getKey)
        .toList();
  }

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path of the file to get contents from.
   * @return An InputStream containing the file contents.
   */
  @Override
  public InputStream getFileContents(String path) {
    String content = s3Client.getObjectAsString(bucketName, fullPath(path));
    return new ByteArrayInputStream(content.getBytes());
  }

  public String getInternalRootDirectory(String rootPath) {
    List<String> commonPrefixes = s3Client.listObjects(bucketName, fullPath(rootPath)).getCommonPrefixes();
    if (commonPrefixes.size() != 1) {
      return rootPath;
    }
    return commonPrefixes.get(0);
  }
}