package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * Implementation of FileAccess using AWS S3 SDK v2.
 */
public class S3FileAccessV2 implements FileAccess {
  private final S3Client s3Client;
  private final String bucketName;

  /**
   * Constructs an S3FileAccessV2 instance with the default S3 client and the specified bucket name.
   *
   * @param bucketName The name of the S3 bucket to access.
   */
  public S3FileAccessV2(String bucketName) {
    this.s3Client = S3Client.builder()
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
    this.bucketName = bucketName;
  }

  /**
   * Constructs an S3FileAccessV2 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   */
  public S3FileAccessV2(S3Client s3Client, String bucketName) {
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
    try {
      s3Client.headObject(HeadObjectRequest.builder()
          .bucket(bucketName)
          .key(path)
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
              .prefix(directoryPath)
              .build())
          .contents()
          .stream()
          .map(S3Object::key)
          .collect(Collectors.toList());
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
        builder.key(path);
      });
      return new ByteArrayInputStream(response.asByteArray());
    } catch (S3Exception e) {
      throw new IOException("Failed to get file contents for path: " + path, e);
    }
  }
}