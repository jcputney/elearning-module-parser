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

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Integration tests for {@link InMemoryFileAccess} with actual module parsers.
 * Tests loading real SCORM packages from the test resources into memory and parsing them.
 */
class InMemoryFileAccessIntegrationTest {

  private static final String TEST_MODULES_PATH = "src/test/resources/modules";

  /**
   * Loads a ZIP file from the test resources into a byte array.
   */
  private byte[] loadTestModule(String modulePath) throws IOException {
    Path path = Paths.get(TEST_MODULES_PATH, modulePath);
    if (!Files.exists(path)) {
      // If it's not a file, might be a directory - create a ZIP from it
      if (Files.isDirectory(Paths.get(TEST_MODULES_PATH).resolve(modulePath))) {
        return createZipFromDirectory(Paths.get(TEST_MODULES_PATH).resolve(modulePath));
      }
      throw new IOException("Test module not found: " + path);
    }
    return Files.readAllBytes(path);
  }

  /**
   * Creates a ZIP file from a directory structure.
   */
  private byte[] createZipFromDirectory(Path directory) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      Files.walk(directory)
          .filter(path -> !Files.isDirectory(path))
          .forEach(path -> {
            try {
              String relativePath = directory.relativize(path).toString().replace('\\', '/');
              ZipEntry entry = new ZipEntry(relativePath);
              zos.putNextEntry(entry);
              Files.copy(path, zos);
              zos.closeEntry();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
    }
    return baos.toByteArray();
  }

  @Test
  void testParsingScorm12FromMemory() throws Exception {
    // Create a simple SCORM 1.2 manifest in memory
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Add imsmanifest.xml
      ZipEntry manifestEntry = new ZipEntry("imsmanifest.xml");
      zos.putNextEntry(manifestEntry);
      String manifestContent = """
          <?xml version="1.0" encoding="UTF-8"?>
          <manifest identifier="test-scorm12" version="1.0"
                    xmlns="http://www.imsproject.org/xsd/imscp_rootv1p1p2"
                    xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_rootv1p2"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <metadata>
              <schema>ADL SCORM</schema>
              <schemaversion>1.2</schemaversion>
            </metadata>
            <organizations default="ORG-1">
              <organization identifier="ORG-1">
                <title>Test Course</title>
                <item identifier="ITEM-1" identifierref="RES-1">
                  <title>Lesson 1</title>
                </item>
              </organization>
            </organizations>
            <resources>
              <resource identifier="RES-1" type="webcontent" href="content/index.html"
                        adlcp:scormtype="sco">
                <file href="content/index.html"/>
              </resource>
            </resources>
          </manifest>
          """;
      zos.write(manifestContent.getBytes());
      zos.closeEntry();

      // Add content file
      ZipEntry contentEntry = new ZipEntry("content/index.html");
      zos.putNextEntry(contentEntry);
      zos.write("<html><body>Test Content</body></html>".getBytes());
      zos.closeEntry();
    }

    byte[] zipData = baos.toByteArray();

    // Parse using InMemoryFileAccess
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      Scorm12Parser parser = new Scorm12Parser(fileAccess);
      ModuleMetadata metadata = parser.parse();

      assertThat(metadata).isNotNull();
      assertThat(metadata.getTitle()).isEqualTo("Test Course");
      assertThat(metadata.getIdentifier()).isEqualTo("test-scorm12");
      assertThat(metadata.getLaunchUrl()).isNotNull();
      assertThat(metadata.getVersion()).isNotNull();
    }
  }

  @Test
  void testParsingScorm2004FromMemory() throws Exception {
    // Create a SCORM 2004 manifest in memory
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // Add imsmanifest.xml
      ZipEntry manifestEntry = new ZipEntry("imsmanifest.xml");
      zos.putNextEntry(manifestEntry);
      String manifestContent = """
          <?xml version="1.0" encoding="UTF-8"?>
          <manifest identifier="test-scorm2004" version="1.3.0"
                    xmlns="http://www.imsglobal.org/xsd/imscp_v1p1"
                    xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_v1p3"
                    xmlns:adlseq="http://www.adlnet.org/xsd/adlseq_v1p3"
                    xmlns:adlnav="http://www.adlnet.org/xsd/adlnav_v1p3"
                    xmlns:imsss="http://www.imsglobal.org/xsd/imsss"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <metadata>
              <schema>ADL SCORM</schema>
              <schemaversion>2004 4th Edition</schemaversion>
            </metadata>
            <organizations default="ORG-1">
              <organization identifier="ORG-1">
                <title>SCORM 2004 Test Course</title>
                <item identifier="ITEM-1" identifierref="RES-1">
                  <title>Module 1</title>
                  <imsss:sequencing>
                    <imsss:controlMode choice="true" flow="true"/>
                  </imsss:sequencing>
                </item>
              </organization>
            </organizations>
            <resources>
              <resource identifier="RES-1" type="webcontent" href="content/module1.html"
                        adlcp:scormType="sco">
                <file href="content/module1.html"/>
              </resource>
            </resources>
          </manifest>
          """;
      zos.write(manifestContent.getBytes());
      zos.closeEntry();

      // Add content file
      ZipEntry contentEntry = new ZipEntry("content/module1.html");
      zos.putNextEntry(contentEntry);
      zos.write("<html><body>Module 1 Content</body></html>".getBytes());
      zos.closeEntry();
    }

    byte[] zipData = baos.toByteArray();

    // Parse using InMemoryFileAccess
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
      ModuleMetadata metadata = parser.parse();

      assertThat(metadata).isNotNull();
      assertThat(metadata.getTitle()).isEqualTo("SCORM 2004 Test Course");
      assertThat(metadata.getIdentifier()).isEqualTo("test-scorm2004");
      assertThat(metadata.getLaunchUrl()).isNotNull();
      assertThat(metadata.getVersion()).isNotNull();
    }
  }

  @Test
  void testParsingWithNestedRootDirectory() throws Exception {
    // Create ZIP with all content in a root directory
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      // All files under "course-package/"
      ZipEntry manifestEntry = new ZipEntry("course-package/imsmanifest.xml");
      zos.putNextEntry(manifestEntry);
      String manifestContent = """
          <?xml version="1.0" encoding="UTF-8"?>
          <manifest identifier="nested-root" version="1.0"
                    xmlns="http://www.imsproject.org/xsd/imscp_rootv1p1p2"
                    xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_rootv1p2">
            <metadata>
              <schema>ADL SCORM</schema>
              <schemaversion>1.2</schemaversion>
            </metadata>
            <organizations default="ORG-1">
              <organization identifier="ORG-1">
                <title>Nested Root Test</title>
              </organization>
            </organizations>
            <resources>
              <resource identifier="RES-1" type="webcontent" href="index.html" adlcp:scormtype="sco">
                <file href="index.html"/>
              </resource>
            </resources>
          </manifest>
          """;
      zos.write(manifestContent.getBytes());
      zos.closeEntry();

      // Add the referenced index.html
      ZipEntry indexEntry = new ZipEntry("course-package/index.html");
      zos.putNextEntry(indexEntry);
      zos.write("<html><body>Index</body></html>".getBytes());
      zos.closeEntry();
    }

    byte[] zipData = baos.toByteArray();

    // Parse using InMemoryFileAccess - should handle root directory automatically
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.getRootPath()).isEqualTo("course-package");

      // Manifest should be accessible with relative path
      assertThat(fileAccess.fileExists("imsmanifest.xml")).isTrue();
      // Index.html should also be accessible
      assertThat(fileAccess.fileExists("index.html")).isTrue();

      // Verify content can be read
      try (InputStream is = fileAccess.getFileContents("imsmanifest.xml")) {
        String content = new String(is.readAllBytes());
        assertThat(content).contains("Nested Root Test");
      }

      try (InputStream is = fileAccess.getFileContents("index.html")) {
        String content = new String(is.readAllBytes());
        assertThat(content).contains("<html>");
      }
    }
  }

  @Test
  void testComparisonWithZipFileAccess() throws Exception {
    // Create test ZIP in memory
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      zos.putNextEntry(new ZipEntry("test.txt"));
      zos.write("test content".getBytes());
      zos.closeEntry();

      zos.putNextEntry(new ZipEntry("dir/file.txt"));
      zos.write("nested content".getBytes());
      zos.closeEntry();
    }
    byte[] zipData = baos.toByteArray();

    // Write to temp file for ZipFileAccess
    Path tempFile = Files.createTempFile("test", ".zip");
    Files.write(tempFile, zipData);

    try {
      // Test with ZipFileAccess
      String fromZipFile;
      try (ZipFileAccess zipAccess = new ZipFileAccess(tempFile.toString())) {
        assertThat(zipAccess.fileExists("test.txt")).isTrue();
        assertThat(zipAccess.fileExists("dir/file.txt")).isTrue();

        try (InputStream is = zipAccess.getFileContents("test.txt")) {
          fromZipFile = new String(is.readAllBytes());
        }
      }

      // Test with InMemoryFileAccess
      String fromMemory;
      try (InMemoryFileAccess memAccess = new InMemoryFileAccess(zipData)) {
        assertThat(memAccess.fileExists("test.txt")).isTrue();
        assertThat(memAccess.fileExists("dir/file.txt")).isTrue();

        try (InputStream is = memAccess.getFileContents("test.txt")) {
          fromMemory = new String(is.readAllBytes());
        }
      }

      // Both should return the same content
      assertThat(fromMemory).isEqualTo(fromZipFile);
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  void testLoadingFromInputStream() throws Exception {
    // Create test data
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      zos.putNextEntry(new ZipEntry("manifest.xml"));
      zos.write("<manifest/>".getBytes());
      zos.closeEntry();
    }
    byte[] zipData = baos.toByteArray();

    // Load from InputStream
    try (InputStream inputStream = new ByteArrayInputStream(zipData);
         InMemoryFileAccess fileAccess = new InMemoryFileAccess(inputStream)) {

      assertThat(fileAccess.fileExists("manifest.xml")).isTrue();
      assertThat(fileAccess.getFileCount()).isEqualTo(1);

      try (InputStream is = fileAccess.getFileContents("manifest.xml")) {
        String content = new String(is.readAllBytes());
        assertThat(content).isEqualTo("<manifest/>");
      }
    }
  }

  private static Stream<Arguments> provideMemorySizes() {
    return Stream.of(
        Arguments.of(100, 10),      // 100 files, 10KB each
        Arguments.of(1000, 1),       // 1000 files, 1KB each
        Arguments.of(10, 100),       // 10 files, 100KB each
        Arguments.of(1, 1000)        // 1 file, 1MB
    );
  }

  @ParameterizedTest
  @MethodSource("provideMemorySizes")
  void testVariousMemoryConfigurations(int fileCount, int sizeKb) throws Exception {
    // Create ZIP with specified configuration
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      byte[] content = new byte[sizeKb * 1024];
      for (int i = 0; i < sizeKb * 1024; i++) {
        content[i] = (byte) (i % 256);
      }

      for (int i = 0; i < fileCount; i++) {
        ZipEntry entry = new ZipEntry("file" + i + ".dat");
        zos.putNextEntry(entry);
        zos.write(content);
        zos.closeEntry();
      }
    }

    byte[] zipData = baos.toByteArray();

    // Load and verify
    try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
      assertThat(fileAccess.getFileCount()).isEqualTo(fileCount);
      assertThat(fileAccess.getTotalSize()).isEqualTo((long) fileCount * sizeKb * 1024);

      // Verify random file
      int randomFile = (int) (Math.random() * fileCount);
      assertThat(fileAccess.fileExists("file" + randomFile + ".dat")).isTrue();

      // Verify content integrity
      try (InputStream is = fileAccess.getFileContents("file" + randomFile + ".dat")) {
        byte[] readContent = is.readAllBytes();
        assertThat(readContent.length).isEqualTo(sizeKb * 1024);
        for (int i = 0; i < Math.min(100, readContent.length); i++) {
          assertThat(readContent[i]).isEqualTo((byte) (i % 256));
        }
      }
    }
  }
}