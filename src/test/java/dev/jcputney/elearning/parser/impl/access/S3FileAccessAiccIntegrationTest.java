/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Integration test to verify AICC module parsing with S3 file access using AWS SDK v1, specifically
 * testing the caching issue where file sizes might be cached as 0.
 */
@Testcontainers
class S3FileAccessAiccIntegrationTest {

  private static final String TEST_BUCKET_NAME = "test-bucket";
  private static final String MODULE_PATH = "modules/aicc-test";

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
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
            localstack
                .getEndpointOverride(LocalStackContainer.Service.S3)
                .toString(),
            localstack.getRegion()))
        .withCredentials(new AWSStaticCredentialsProvider(
            new BasicAWSCredentials(localstack.getAccessKey(), localstack.getSecretKey())))
        .withPathStyleAccessEnabled(true)
        .build();

    // Create the bucket
    s3Client.createBucket(TEST_BUCKET_NAME);

    // Create S3FileAccess without eager caching to control the test flow
    s3FileAccess = new S3FileAccessV1(s3Client, TEST_BUCKET_NAME, MODULE_PATH, false);
  }

  @AfterEach
  void tearDown() {
    if (s3FileAccess != null) {
      s3FileAccess.shutdown();
    }
  }

  @Test
  void testAiccModuleParsingWithCorrectCaching() {
    // Upload AICC module files to S3
    uploadAiccModuleFiles();

    // Test that AICC parser can successfully parse the module
    AiccParser parser = new AiccParser(s3FileAccess);
    var metadata = assertDoesNotThrow(parser::parseOnly);

    assertNotNull(metadata);
    assertEquals("Test AICC Course", metadata.getTitle());
    assertEquals("AICC_TEST_001", metadata.getIdentifier());

    // Verify that file sizes are cached correctly (non-zero)
    assertThat(s3FileAccess.fileSizeCache).isNotEmpty();
    s3FileAccess.fileSizeCache.forEach((key, size) -> {
      assertThat(size)
          .as("File size for %s should not be 0", key)
          .isGreaterThan(0);
    });
  }

  @Test
  void testCachingDoesNotStoreZeroSizes() throws IOException {
    // Upload a test file
    String testContent = "Test content";
    uploadFile("test.txt", testContent);

    // First, verify the file exists and has correct size
    boolean exists = s3FileAccess.fileExistsInternal("test.txt");
    assertThat(exists).isTrue();

    // Get the file size
    long size = s3FileAccess.getCachedFileSize("test.txt");
    assertThat(size).isEqualTo(testContent.length());

    // Verify that the size is cached and not 0
    assertThat(s3FileAccess.fileSizeCache.get("test.txt")).isEqualTo(testContent.length());

    // Clear caches and verify we can still get correct size
    s3FileAccess.clearCaches();
    long sizeAfterClear = s3FileAccess.getCachedFileSize("test.txt");
    assertThat(sizeAfterClear).isEqualTo(testContent.length());
  }

  @Test
  void testAiccFileDiscoveryAndCaching() throws IOException {
    // Upload AICC module files
    uploadAiccModuleFiles();

    // Test file discovery using listFiles (how AICC finds manifest files)
    var files = s3FileAccess.listFilesInternal("");

    // Verify all AICC files are found (as relative paths)
    assertThat(files).contains(
        "course.crs",
        "course.des",
        "course.au",
        "course.cst"
    );

    // Verify that files are listed
    assertThat(files).isNotEmpty();

    // After listing, we should be able to check file existence
    boolean crsExists = s3FileAccess.fileExistsInternal("course.crs");
    assertThat(crsExists).isTrue();

    // Now file existence should be cached
    assertThat(s3FileAccess.fileExistsCache).isNotEmpty();

    // Verify that we can read each file
    for (String extension : new String[]{".crs", ".des", ".au", ".cst"}) {
      String fileName = "course" + extension;
      var content = s3FileAccess.getFileContentsInternal(fileName);
      assertNotNull(content);
      content.close();

      // Verify size is cached and non-zero
      Long cachedSize = s3FileAccess.fileSizeCache.get(fileName);
      assertThat(cachedSize)
          .isNotNull()
          .isGreaterThan(0);
    }
  }

  @Test
  void testPrefetchDoesNotCacheZeroSizes() {
    // Upload a common module file
    uploadFile("imsmanifest.xml", "<?xml version=\"1.0\"?><manifest/>");

    // Clear any existing caches
    s3FileAccess.clearCaches();

    // Prefetch common files
    s3FileAccess.prefetchCommonFiles();

    // Verify that if any files were cached, they have non-zero sizes
    s3FileAccess.fileSizeCache.forEach((key, size) -> {
      assertThat(size)
          .as("Prefetched file size for %s should not be 0", key)
          .isGreaterThan(0);
    });
  }

  private void uploadAiccModuleFiles() {
    String crsContent = """
        [Course]
        Course_ID=AICC_TEST_001
        Course_Title=Test AICC Course
        Level=1
        Total_AUs=1
        Total_Blocks=1
        Version=1.0
        
        [Course_Description]
        This is a test AICC course for integration testing
        """;

    String desContent = """
        "system_id","title","description"
        "AU_001","Test AU","Test Assignable Unit"
        """;

    String auContent = """
        "system_id","command_line","file_name","max_score","mastery_score","max_time_allowed","time_limit_action","type","weight"
        "AU_001","index.html","index.html","100","80","01:00:00","exit,message","1","1"
        """;

    String cstContent = """
        "block","member"
        "root","AU_001"
        """;

    uploadFile("course.crs", crsContent);
    uploadFile("course.des", desContent);
    uploadFile("course.au", auContent);
    uploadFile("course.cst", cstContent);
  }

  private void uploadFile(String fileName, String content) {
    byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(contentBytes.length);

    PutObjectRequest request = new PutObjectRequest(
        TEST_BUCKET_NAME,
        MODULE_PATH + "/" + fileName,
        new ByteArrayInputStream(contentBytes),
        metadata
    );

    s3Client.putObject(request);
  }
}