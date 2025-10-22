/* Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.api.ModuleParser;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for end-to-end parsing scenarios.
 * <p>
 * These tests verify the entire parsing process from start to finish, ensuring that all components
 * work together correctly. They test the integration between the ModuleParserFactory, the various
 * parsers, and the file access mechanisms.
 * </p>
 */
public class IntegrationTest {

  /**
   * Tests the end-to-end parsing process for a SCORM 1.2 module.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly detects the module type 2. The
   * appropriate parser is created 3. The parser successfully parses the module 4. The parsed
   * metadata contains the expected values
   * </p>
   */
  @Test
  void testEndToEndParsingScorm12()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/zips/scorm12.zip";

    // Act - Use try-with-resources to ensure ZipFileAccess is closed
    try (ZipFileAccess zipAccess = new ZipFileAccess(modulePath)) {
      ModuleParserFactory parserFactory = new DefaultModuleParserFactory(zipAccess);
      ModuleParser<?> parser = parserFactory.getParser();
      ModuleMetadata<?> metadata = parser.parse();

      // Assert
      assertNotNull(metadata);
      assertEquals(ModuleType.SCORM_12, metadata.getModuleType());
      assertNotNull(metadata.getTitle());
      assertNotNull(metadata.getLaunchUrl());
    }
  }

  /**
   * Tests the end-to-end parsing process for a SCORM 2004 module.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly detects the module type 2. The
   * appropriate parser is created 3. The parser successfully parses the module 4. The parsed
   * metadata contains the expected values
   * </p>
   */
  @Test
  void testEndToEndParsingScorm2004()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/zips/scorm2004.zip";

    // Act - Use try-with-resources to ensure ZipFileAccess is closed
    try (ZipFileAccess zipAccess = new ZipFileAccess(modulePath)) {
      ModuleParserFactory parserFactory = new DefaultModuleParserFactory(zipAccess);
      ModuleParser<?> parser = parserFactory.getParser();
      ModuleMetadata<?> metadata = parser.parse();

      // Assert
      assertNotNull(metadata);
      assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
      assertNotNull(metadata.getTitle());
      assertNotNull(metadata.getLaunchUrl());
    }
  }

  /**
   * Tests the end-to-end parsing process for an AICC module.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly detects the module type 2. The
   * appropriate parser is created 3. The parser successfully parses the module 4. The parsed
   * metadata contains the expected values
   * </p>
   */
  @Test
  void testEndToEndParsingAicc()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/zips/aicc.zip";

    // Act - Use try-with-resources to ensure ZipFileAccess is closed
    try (ZipFileAccess zipAccess = new ZipFileAccess(modulePath)) {
      ModuleParserFactory parserFactory = new DefaultModuleParserFactory(zipAccess);
      ModuleParser<?> parser = parserFactory.getParser();
      ModuleMetadata<?> metadata = parser.parse();

      // Assert
      assertNotNull(metadata);
      assertEquals(ModuleType.AICC, metadata.getModuleType());
      assertNotNull(metadata.getTitle());
      assertNotNull(metadata.getLaunchUrl());
    }
  }

  /**
   * Tests the end-to-end parsing process for a CMI5 module.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly detects the module type 2. The
   * appropriate parser is created 3. The parser successfully parses the module 4. The parsed
   * metadata contains the expected values
   * </p>
   */
  @Test
  void testEndToEndParsingCmi5()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/zips/cmi5.zip";

    // Act - Use try-with-resources to ensure ZipFileAccess is closed
    try (ZipFileAccess zipAccess = new ZipFileAccess(modulePath)) {
      ModuleParserFactory parserFactory = new DefaultModuleParserFactory(zipAccess);
      ModuleParser<?> parser = parserFactory.getParser();
      ModuleMetadata<?> metadata = parser.parse();

      // Assert
      assertNotNull(metadata);
      assertEquals(ModuleType.CMI5, metadata.getModuleType());
      assertNotNull(metadata.getTitle());
      assertNotNull(metadata.getLaunchUrl());
    }
  }

  /**
   * Tests the end-to-end parsing process for an xAPI/TinCan module.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly detects the module type 2. The
   * appropriate parser is created 3. The parser successfully parses the module 4. The parsed
   * metadata contains the expected values
   * </p>
   */
  @Test
  void testEndToEndParsingXapi()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/xapi/basic_course";

    // Act
    LocalFileAccess fileAccess = new LocalFileAccess(modulePath);
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(fileAccess);
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.XAPI, metadata.getModuleType());
    assertEquals("Basic xAPI Course", metadata.getTitle());
    assertEquals("index.html", metadata.getLaunchUrl());
  }

  /**
   * Tests the error handling for an invalid module.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly throws an exception for an
   * invalid module
   * </p>
   */
  @Test
  void testEndToEndParsingInvalidModule() {
    // Arrange
    String modulePath = "src/test/resources/modules/zips/invalid.zip";

    // Act & Assert
    assertThrows(IOException.class, () -> {
      try (ZipFileAccess zipAccess = new ZipFileAccess(modulePath)) {
        ModuleParserFactory parserFactory = new DefaultModuleParserFactory(zipAccess);
        parserFactory.getParser();
      }
    });
  }

  /**
   * Tests the end-to-end parsing process for all supported module types.
   * <p>
   * This test verifies that: 1. The ModuleParserFactory correctly detects the module type for all
   * supported types 2. The appropriate parser is created for each type 3. Each parser successfully
   * parses its module 4. The parsed metadata contains the expected values for each module
   * </p>
   */
  @Test
  void testEndToEndParsingAllModuleTypes()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Test SCORM 1.2
    try (ZipFileAccess scorm12Zip = new ZipFileAccess(
        "src/test/resources/modules/zips/scorm12.zip")) {
      ModuleParserFactory scorm12Factory = new DefaultModuleParserFactory(scorm12Zip);
      ModuleParser<?> scorm12Parser = scorm12Factory.getParser();
      ModuleMetadata<?> scorm12Metadata = scorm12Parser.parse();
      assertNotNull(scorm12Metadata);
      assertEquals(ModuleType.SCORM_12, scorm12Metadata.getModuleType());
    }

    // Test SCORM 2004
    try (ZipFileAccess scorm2004Zip = new ZipFileAccess(
        "src/test/resources/modules/zips/scorm2004.zip")) {
      ModuleParserFactory scorm2004Factory = new DefaultModuleParserFactory(scorm2004Zip);
      ModuleParser<?> scorm2004Parser = scorm2004Factory.getParser();
      ModuleMetadata<?> scorm2004Metadata = scorm2004Parser.parse();
      assertNotNull(scorm2004Metadata);
      assertEquals(ModuleType.SCORM_2004, scorm2004Metadata.getModuleType());
    }

    // Test AICC
    try (ZipFileAccess aiccZip = new ZipFileAccess("src/test/resources/modules/zips/aicc.zip")) {
      ModuleParserFactory aiccFactory = new DefaultModuleParserFactory(aiccZip);
      ModuleParser<?> aiccParser = aiccFactory.getParser();
      ModuleMetadata<?> aiccMetadata = aiccParser.parse();
      assertNotNull(aiccMetadata);
      assertEquals(ModuleType.AICC, aiccMetadata.getModuleType());
    }

    // Test CMI5
    try (ZipFileAccess cmi5Zip = new ZipFileAccess("src/test/resources/modules/zips/cmi5.zip")) {
      ModuleParserFactory cmi5Factory = new DefaultModuleParserFactory(cmi5Zip);
      ModuleParser<?> cmi5Parser = cmi5Factory.getParser();
      ModuleMetadata<?> cmi5Metadata = cmi5Parser.parse();
      assertNotNull(cmi5Metadata);
      assertEquals(ModuleType.CMI5, cmi5Metadata.getModuleType());
    }

    // Test xAPI/TinCan
    LocalFileAccess xapiFileAccess = new LocalFileAccess(
        "src/test/resources/modules/xapi/basic_course");
    ModuleParserFactory xapiFactory = new DefaultModuleParserFactory(xapiFileAccess);
    ModuleParser<?> xapiParser = xapiFactory.getParser();
    ModuleMetadata<?> xapiMetadata = xapiParser.parse();
    assertNotNull(xapiMetadata);
    assertEquals(ModuleType.XAPI, xapiMetadata.getModuleType());
  }

  /**
   * Tests the edition detection for SCORM 2004 2nd Edition modules.
   */
  @Test
  void testScorm2004SecondEditionDetection()
      throws ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingSingleSCO_SCORM20042ndEdition";

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new LocalFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    assertEquals(ModuleEditionType.SCORM_2004_2ND_EDITION, metadata.getModuleEditionType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
  }

  /**
   * Tests the edition detection for SCORM 2004 3rd Edition modules.
   */
  @Test
  void testScorm2004ThirdEditionDetection()
      throws ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingSingleSCO_SCORM20043rdEdition";

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new LocalFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    assertEquals(ModuleEditionType.SCORM_2004_3RD_EDITION, metadata.getModuleEditionType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
  }

  /**
   * Tests the edition detection for SCORM 2004 4th Edition modules.
   */
  @Test
  void testScorm2004FourthEditionDetection()
      throws ModuleDetectionException, ModuleParsingException {
    // Arrange
    String modulePath = "src/test/resources/modules/scorm2004/SequencingPostTestRollup4thEd_SCORM20044thEdition";

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new LocalFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    assertEquals(ModuleEditionType.SCORM_2004_4TH_EDITION, metadata.getModuleEditionType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
  }

  /**
   * Tests that other module types have their edition type set correctly.
   */
  @Test
  void testNonScorm2004EditionTypes()
      throws IOException, ModuleDetectionException, ModuleParsingException {
    // Test SCORM 1.2
    ModuleParserFactory scorm12Factory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/scorm12.zip"));
    ModuleParser<?> scorm12Parser = scorm12Factory.getParser();
    ModuleMetadata<?> scorm12Metadata = scorm12Parser.parse();
    assertEquals(ModuleEditionType.SCORM_12, scorm12Metadata.getModuleEditionType());

    // Test AICC
    ModuleParserFactory aiccFactory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/aicc.zip"));
    ModuleParser<?> aiccParser = aiccFactory.getParser();
    ModuleMetadata<?> aiccMetadata = aiccParser.parse();
    assertEquals(ModuleEditionType.AICC, aiccMetadata.getModuleEditionType());

    // Test CMI5
    ModuleParserFactory cmi5Factory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/cmi5.zip"));
    ModuleParser<?> cmi5Parser = cmi5Factory.getParser();
    ModuleMetadata<?> cmi5Metadata = cmi5Parser.parse();
    assertEquals(ModuleEditionType.CMI5, cmi5Metadata.getModuleEditionType());
  }
}