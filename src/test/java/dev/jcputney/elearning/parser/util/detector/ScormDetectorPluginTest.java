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

package dev.jcputney.elearning.parser.util.detector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.util.ScormVersionDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

/**
 * Test class for ScormDetectorPlugin.
 */
class ScormDetectorPluginTest {

  @Mock
  private FileAccess mockFileAccess;

  private ScormDetectorPlugin plugin;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    plugin = new ScormDetectorPlugin();
  }

  @Test
  void testConstructor_CreatesPluginSuccessfully() {
    assertNotNull(plugin);
  }

  @Test
  void testGetPriority_ReturnsCorrectValue() {
    assertEquals(100, plugin.getPriority());
  }

  @Test
  void testGetName_ReturnsCorrectValue() {
    assertEquals("SCORM Detector", plugin.getName());
  }

  @Test
  void testDetect_NullFileAccess_ThrowsException() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      plugin.detect(null);
    });
    assertEquals("FileAccess cannot be null", exception.getMessage());
  }

  @Test
  void testDetect_ManifestDoesNotExist_ReturnsNull() throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(false);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
  }

  @Test
  void testDetect_ManifestExistsScorm12_ReturnsScorm12() throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenReturn(ModuleType.SCORM_12);

      // Act
      ModuleType result = plugin.detect(mockFileAccess);

      // Assert
      assertEquals(ModuleType.SCORM_12, result);
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
    }
  }

  @Test
  void testDetect_ManifestExistsScorm2004_ReturnsScorm2004() throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenReturn(ModuleType.SCORM_2004);

      // Act
      ModuleType result = plugin.detect(mockFileAccess);

      // Assert
      assertEquals(ModuleType.SCORM_2004, result);
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
    }
  }

  @Test
  void testDetect_VersionDetectorThrowsException_ThrowsModuleDetectionException() throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      Exception originalException = new RuntimeException("Version detection failed");
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenThrow(originalException);

      // Act & Assert
      ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () -> {
        plugin.detect(mockFileAccess);
      });
      assertEquals("Error detecting SCORM version", exception.getMessage());
      assertSame(originalException, exception.getCause());
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
    }
  }

  @Test
  void testDetect_FileExistsCheckThrowsException_PropagatesException() {
    // Arrange
    RuntimeException runtimeException = new RuntimeException("File access error");
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenThrow(runtimeException);

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      plugin.detect(mockFileAccess);
    });
    assertSame(runtimeException, exception);
    verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
  }

  @Test
  void testDetect_ConstantValueVerification_UsesScorm12ParserConstant() throws Exception {
    // This test verifies that the plugin uses the constant from Scorm12Parser
    // Arrange
    when(mockFileAccess.fileExists("imsmanifest.xml")).thenReturn(false);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).fileExists("imsmanifest.xml");
  }

  @Test
  void testDetect_VersionDetectorReturnsNull_ReturnsNull() throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenReturn(null);

      // Act
      ModuleType result = plugin.detect(mockFileAccess);

      // Assert
      assertNull(result);
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
    }
  }

  @Test
  void testDetect_MultipleCallsSameFileAccess_ConsistentResults() throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenReturn(ModuleType.SCORM_12);

      // Act
      ModuleType result1 = plugin.detect(mockFileAccess);
      ModuleType result2 = plugin.detect(mockFileAccess);

      // Assert
      assertEquals(ModuleType.SCORM_12, result1);
      assertEquals(ModuleType.SCORM_12, result2);
      assertEquals(result1, result2);
      verify(mockFileAccess, times(2)).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess),
          times(2));
    }
  }

  @Test
  void testDetect_DifferentFileAccessInstances_IndependentResults() throws Exception {
    // Arrange
    FileAccess mockFileAccess2 = mock(FileAccess.class);
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);
    when(mockFileAccess2.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(false);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenReturn(ModuleType.SCORM_2004);

      // Act
      ModuleType result1 = plugin.detect(mockFileAccess);
      ModuleType result2 = plugin.detect(mockFileAccess2);

      // Assert
      assertEquals(ModuleType.SCORM_2004, result1);
      assertNull(result2);
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      verify(mockFileAccess2).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess2),
          never());
    }
  }

  @Test
  void testDetect_VersionDetectorThrowsIOException_WrapsInModuleDetectionException()
      throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      java.io.IOException ioException = new java.io.IOException(
          "IO error during version detection");
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenThrow(ioException);

      // Act & Assert
      ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () -> {
        plugin.detect(mockFileAccess);
      });
      assertEquals("Error detecting SCORM version", exception.getMessage());
      assertSame(ioException, exception.getCause());
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
    }
  }

  @Test
  void testPluginProperties_ConsistentWithInterface() {
    // Assert plugin implements the interface correctly
    assertInstanceOf(ModuleTypeDetectorPlugin.class, plugin);

    // Verify plugin properties are reasonable
    assertTrue(plugin.getPriority() > 0);
    assertNotNull(plugin.getName());
    assertFalse(plugin
        .getName()
        .trim()
        .isEmpty());
  }

  @Test
  void testDetect_EdgeCase_ManifestExistsButVersionDetectionReturnsUnexpectedType()
      throws Exception {
    // Arrange
    when(mockFileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)).thenReturn(true);

    try (MockedStatic<ScormVersionDetector> mockedDetector = mockStatic(
        ScormVersionDetector.class)) {
      // Return a non-SCORM type (edge case)
      mockedDetector
          .when(() -> ScormVersionDetector.detectScormVersion(mockFileAccess))
          .thenReturn(ModuleType.AICC);

      // Act
      ModuleType result = plugin.detect(mockFileAccess);

      // Assert
      assertEquals(ModuleType.AICC, result); // Plugin should return whatever the detector returns
      verify(mockFileAccess).fileExists(Scorm12Parser.MANIFEST_FILE);
      mockedDetector.verify(() -> ScormVersionDetector.detectScormVersion(mockFileAccess));
    }
  }
}