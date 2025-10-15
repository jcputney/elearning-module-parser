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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Tests for the {@link S3FileAccessV1} class using Testcontainers with LocalStack.
 */
@Testcontainers
class S3FileAccessV1Test {

  private static final String TEST_BUCKET_NAME = "test-bucket";

  //language=xml
  private static final String TEST_MANIFEST_CONTENT = """
      <?xml version="1.0" encoding="UTF-8"?>
      <manifest identifier="com.example.test">
        <metadata>
          <schema>ADL SCORM</schema>
          <schemaversion>1.2</schemaversion>
        </metadata>
        <organizations default="default_org">
          <organization identifier="default_org">
            <title>Test Course</title>
            <item identifier="item_1" identifierref="resource_1">
              <title>Test Item</title>
            </item>
          </organization>
        </organizations>
        <resources>
          <resource identifier="resource_1" type="webcontent" adlcp:scormtype="sco" href="content/index.html">
            <file href="content/index.html"/>
          </resource>
        </resources>
      </manifest>
      """;

  @Container
  private static final LocalStackContainer localstack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:2.3.2"))
      .withServices(LocalStackContainer.Service.S3);

  private AmazonS3 s3Client;
  private S3FileAccessV1 s3FileAccess;

  @BeforeEach
  void setUp() {
    // Create S3 client connected to localstack
    s3Client = AmazonS3ClientBuilder
        .standard()
        .withEndpointConfiguration(
            new EndpointConfiguration(
                localstack
                    .getEndpoint()
                    .toString(),
                localstack.getRegion()))
        .withCredentials(
            new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(localstack.getAccessKey(), localstack.getSecretKey())))
        .withPathStyleAccessEnabled(true)
        .build();

    // Create test bucket
    s3Client.createBucket(TEST_BUCKET_NAME);

    // Upload test files
    uploadTestFile("imsmanifest.xml", TEST_MANIFEST_CONTENT);
    uploadTestFile("content/index.html", "<html><body>Test Content</body></html>");
    uploadTestFile("nested/folder/file.txt", "Test file in nested folder");

    // Create S3FileAccessV1 instance
    s3FileAccess = new S3FileAccessV1(s3Client, TEST_BUCKET_NAME, "");
  }

  @AfterEach
  void tearDown() {
    if (s3Client != null) {
      s3Client.shutdown();
    }
  }

  @Test
  void constructor_withValidParameters_createsInstance() {
    assertNotNull(s3FileAccess);
    assertEquals("", s3FileAccess.getRootPath());
  }

  @Test
  void constructor_withRootPath_setsRootPath() {
    S3FileAccessV1 accessWithRoot = new S3FileAccessV1(s3Client, TEST_BUCKET_NAME, "root");
    assertEquals("root", accessWithRoot.getRootPath());
  }

  @Test
  void fileExists_withExistingFile_returnsTrue() {
    assertTrue(s3FileAccess.fileExists("imsmanifest.xml"));
  }

  @Test
  void fileExists_withNonExistingFile_returnsFalse() {
    assertFalse(s3FileAccess.fileExists("nonexistent.txt"));
  }

  @Test
  void fileExists_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> s3FileAccess.fileExists(null));
  }

  @Test
  void listFiles_returnsCorrectFiles() throws IOException {
    List<String> files = s3FileAccess.listFiles("");
    assertTrue(files.contains("imsmanifest.xml"));
    assertTrue(files.contains("content/index.html"));
    assertTrue(files.contains("nested/folder/file.txt"));
  }

  @Test
  void listFiles_withPrefix_returnsFilteredFiles() throws IOException {
    List<String> files = s3FileAccess.listFiles("content/");
    assertTrue(files.contains("content/index.html"));
    assertFalse(files.contains("imsmanifest.xml"));
  }

  @Test
  void listFiles_withNonExistingDirectory_returnsEmptyList() throws IOException {
    List<String> files = s3FileAccess.listFiles("nonexistent/");
    assertTrue(files.isEmpty());
  }

  @Test
  void listFiles_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> s3FileAccess.listFiles(null));
  }

  @Test
  void getFileContents_withExistingFile_returnsContent() throws IOException {
    try (InputStream is = s3FileAccess.getFileContents("imsmanifest.xml")) {
      String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      assertEquals(TEST_MANIFEST_CONTENT, content);
    }
  }

  @Test
  void getFileContents_withNonExistingFile_throwsIOException() {
    assertThrows(AmazonS3Exception.class, () -> s3FileAccess.getFileContents("nonexistent.txt"));
  }

  @Test
  void getFileContents_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> s3FileAccess.getFileContents(null));
  }

  @Test
  void fullPath_withRelativePath_returnsPathWithRootPrefix() {
    S3FileAccessV1 accessWithRoot = new S3FileAccessV1(s3Client, TEST_BUCKET_NAME, "root");
    assertEquals("root/file.txt", accessWithRoot.fullPath("file.txt"));
  }

  @Test
  void fullPath_withAbsolutePath_returnsPathWithoutLeadingSlash() {
    assertEquals("file.txt", s3FileAccess.fullPath("/file.txt"));
  }

  @Test
  void fullPath_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> s3FileAccess.fullPath(null));
  }

  @Test
  void getInternalRootDirectory_withValidPath_returnsPath() {
    String rootPath = s3FileAccess.getInternalRootDirectory();
    assertEquals("", rootPath);
  }

  private void uploadTestFile(String key, String content) {
    s3Client.putObject(TEST_BUCKET_NAME, key, content);
  }
}