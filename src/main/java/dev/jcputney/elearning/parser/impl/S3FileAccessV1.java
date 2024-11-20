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
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of FileAccess using AWS S3 SDK v1.
 */
public class S3FileAccessV1 implements FileAccess {
  private final AmazonS3 s3Client;
  private final String bucketName;

  /**
   * Constructs an S3FileAccessV1 instance with the default S3 client and the specified bucket name.
   *
   * @param bucketName The name of the S3 bucket to access.
   */
  public S3FileAccessV1(String bucketName) {
    this.s3Client = AmazonS3ClientBuilder.defaultClient();
    this.bucketName = bucketName;
  }

  /**
   * Constructs an S3FileAccessV1 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   */
  public S3FileAccessV1(AmazonS3 s3Client, String bucketName) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  /**
   * Checks if a file exists at the specified path.
   *
   * @param path The path of the file to check.
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExists(String path) {
    return s3Client.doesObjectExist(bucketName, path);
  }

  /**
   * Lists the files in the specified directory path.
   *
   * @param directoryPath The path of the directory to list files from.
   * @return A list of file paths in the specified directory.
   */
  @Override
  public List<String> listFiles(String directoryPath) {
    return s3Client.listObjects(bucketName, directoryPath).getObjectSummaries().stream()
        .map(S3ObjectSummary::getKey)
        .collect(Collectors.toList());
  }

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path of the file to get contents from.
   * @return An InputStream containing the file contents.
   */
  @Override
  public InputStream getFileContents(String path) {
    String content = s3Client.getObjectAsString(bucketName, path);
    return new ByteArrayInputStream(content.getBytes());
  }
}