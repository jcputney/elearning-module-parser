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

package dev.jcputney.elearning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
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

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.SCORM_12, metadata.getModuleType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
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

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
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

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
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

    // Act
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    ModuleMetadata<?> metadata = parser.parse();

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());
    assertNotNull(metadata.getTitle());
    assertNotNull(metadata.getLaunchUrl());
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
      ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
          new ZipFileAccess(modulePath));
      parserFactory.getParser();
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
    ModuleParserFactory scorm12Factory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/scorm12.zip"));
    ModuleParser<?> scorm12Parser = scorm12Factory.getParser();
    ModuleMetadata<?> scorm12Metadata = scorm12Parser.parse();
    assertNotNull(scorm12Metadata);
    assertEquals(ModuleType.SCORM_12, scorm12Metadata.getModuleType());

    // Test SCORM 2004
    ModuleParserFactory scorm2004Factory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/scorm2004.zip"));
    ModuleParser<?> scorm2004Parser = scorm2004Factory.getParser();
    ModuleMetadata<?> scorm2004Metadata = scorm2004Parser.parse();
    assertNotNull(scorm2004Metadata);
    assertEquals(ModuleType.SCORM_2004, scorm2004Metadata.getModuleType());

    // Test AICC
    ModuleParserFactory aiccFactory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/aicc.zip"));
    ModuleParser<?> aiccParser = aiccFactory.getParser();
    ModuleMetadata<?> aiccMetadata = aiccParser.parse();
    assertNotNull(aiccMetadata);
    assertEquals(ModuleType.AICC, aiccMetadata.getModuleType());

    // Test CMI5
    ModuleParserFactory cmi5Factory = new DefaultModuleParserFactory(
        new ZipFileAccess("src/test/resources/modules/zips/cmi5.zip"));
    ModuleParser<?> cmi5Parser = cmi5Factory.getParser();
    ModuleMetadata<?> cmi5Metadata = cmi5Parser.parse();
    assertNotNull(cmi5Metadata);
    assertEquals(ModuleType.CMI5, cmi5Metadata.getModuleType());
  }
}