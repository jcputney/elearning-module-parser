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

package dev.jcputney.elearning.parser.impl.detector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for AiccDetectorPlugin.
 */
class AiccDetectorPluginTest {

  @Mock
  private FileAccess mockFileAccess;

  private AiccDetectorPlugin plugin;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    plugin = new AiccDetectorPlugin();
  }

  @Test
  void testConstructor_CreatesPluginSuccessfully() {
    assertNotNull(plugin);
  }

  @Test
  void testGetPriority_ReturnsCorrectValue() {
    assertEquals(80, plugin.getPriority());
  }

  @Test
  void testGetName_ReturnsCorrectValue() {
    assertEquals("AICC Detector", plugin.getName());
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
  void testDetect_BothAuAndCrsFilesPresent_ReturnsAICC() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "course.au",
        "course.crs",
        "index.html",
        "content.js"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.AICC, result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_MultipleAuAndCrsFiles_ReturnsAICC() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "lesson1.au",
        "lesson2.au",
        "course1.crs",
        "course2.crs",
        "index.html"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.AICC, result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_OnlyAuFilePresent_ReturnsNull() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "course.au",
        "index.html",
        "content.js"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_OnlyCrsFilePresent_ReturnsNull() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "course.crs",
        "index.html",
        "content.js"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_NoAiccFiles_ReturnsNull() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "index.html",
        "style.css",
        "script.js",
        "manifest.xml"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_EmptyFileList_ReturnsNull() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(Collections.emptyList());

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FilesWithDifferentExtensions_CaseInsensitive_ReturnsNull() throws Exception {
    // Arrange - Extensions don't match exactly (.AU vs .au)
    List<String> files = Arrays.asList(
        "course.AU",  // Different case
        "course.CRS", // Different case
        "index.html"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result); // Current implementation is case-sensitive
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FilesInSubdirectories_ReturnsAICC() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "modules/lesson1.au",
        "config/course.crs",
        "index.html"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.AICC, result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FilesWithSimilarExtensions_ReturnsNull() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "course.aud",  // Similar but not .au
        "course.crst", // Similar but not .crs
        "index.html"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FileAccessThrowsIOException_ThrowsModuleDetectionException() throws Exception {
    // Arrange
    IOException ioException = new IOException("Unable to access files");
    when(mockFileAccess.listFiles("")).thenThrow(ioException);

    // Act & Assert
    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () -> {
      plugin.detect(mockFileAccess);
    });
    assertEquals("Error detecting AICC module", exception.getMessage());
    assertSame(ioException, exception.getCause());
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FileAccessThrowsRuntimeException_PropagatesException() throws Exception {
    // Arrange
    RuntimeException runtimeException = new RuntimeException("Unexpected error");
    when(mockFileAccess.listFiles("")).thenThrow(runtimeException);

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      plugin.detect(mockFileAccess);
    });
    assertSame(runtimeException, exception);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_ComplexFileStructure_ReturnsAICC() throws Exception {
    // Arrange - More realistic file structure
    List<String> files = Arrays.asList(
        "aicc/assessment.au",
        "aicc/assessment.crs",
        "aicc/assessment.cst",
        "aicc/assessment.des",
        "package/Api.js",
        "package/default.htm",
        "shared/style.css",
        "shared/common.js"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.AICC, result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_AuFileWithoutCrs_MultipleChecks_ReturnsNull() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "course1.au",
        "course2.au",
        "course3.au",
        "index.html",
        "content.xml"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_CrsFileWithoutAu_MultipleChecks_ReturnsNull() throws Exception {
    // Arrange
    List<String> files = Arrays.asList(
        "course1.crs",
        "course2.crs",
        "course3.crs",
        "index.html",
        "content.xml"
    );
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_EdgeCase_SingleAuAndCrsFile_ReturnsAICC() throws Exception {
    // Arrange
    List<String> files = Arrays.asList("single.au", "single.crs");
    when(mockFileAccess.listFiles("")).thenReturn(files);

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.AICC, result);
    verify(mockFileAccess).listFiles("");
  }
}