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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for Cmi5DetectorPlugin.
 */
class Cmi5DetectorPluginTest {

  @Mock
  private FileAccess mockFileAccess;

  private Cmi5DetectorPlugin plugin;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    plugin = new Cmi5DetectorPlugin();
  }

  @Test
  void testConstructor_CreatesPluginSuccessfully() {
    assertNotNull(plugin);
  }

  @Test
  void testGetPriority_ReturnsCorrectValue() {
    assertEquals(90, plugin.getPriority());
  }

  @Test
  void testGetName_ReturnsCorrectValue() {
    assertEquals("cmi5 Detector", plugin.getName());
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
  void testDetect_Cmi5XmlExists_ReturnsCMI5() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(List.of(Cmi5Parser.CMI5_XML, "index.html"));

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.CMI5, result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_Cmi5XmlDoesNotExist_ReturnsNull() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(List.of("index.html"));

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_ConstantValueVerification_UsesCmi5ParserConstant() throws Exception {
    // This test verifies that the plugin uses the constant from Cmi5Parser
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(List.of("cmi5.xml", "index.html"));

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.CMI5, result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FileAccessDoesNotThrowException_ProcessesNormally() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(List.of("index.html"));

    // Act & Assert - Should not throw any exception
    ModuleType result = plugin.detect(mockFileAccess);
    assertNull(result);
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_FileAccessThrowsRuntimeException_PropagatesException() throws Exception {
    // Arrange
    IOException ioException = new IOException("File access error");
    when(mockFileAccess.listFiles("")).thenThrow(ioException);

    // Act & Assert
    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () -> {
      plugin.detect(mockFileAccess);
    });
    assertEquals("Error detecting cmi5 module", exception.getMessage());
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_MultipleCallsSameFileAccess_ConsistentResults() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(List.of(Cmi5Parser.CMI5_XML, "index.html"));

    // Act
    ModuleType result1 = plugin.detect(mockFileAccess);
    ModuleType result2 = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.CMI5, result1);
    assertEquals(ModuleType.CMI5, result2);
    assertEquals(result1, result2);
    verify(mockFileAccess, times(2)).listFiles("");
  }

  @Test
  void testDetect_DifferentFileAccessInstances_IndependentResults() throws Exception {
    // Arrange
    FileAccess mockFileAccess2 = mock(FileAccess.class);
    when(mockFileAccess.listFiles("")).thenReturn(List.of(Cmi5Parser.CMI5_XML, "index.html"));
    when(mockFileAccess2.listFiles("")).thenReturn(List.of("index.html"));

    // Act
    ModuleType result1 = plugin.detect(mockFileAccess);
    ModuleType result2 = plugin.detect(mockFileAccess2);

    // Assert
    assertEquals(ModuleType.CMI5, result1);
    assertNull(result2);
    verify(mockFileAccess).listFiles("");
    verify(mockFileAccess2).listFiles("");
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
  void testDetect_EdgeCase_ExceptionHandling() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenThrow(
        new IOException("Simulated error"));

    // Act & Assert
    assertThrows(ModuleDetectionException.class, () -> {
      plugin.detect(mockFileAccess);
    });
    verify(mockFileAccess).listFiles("");
  }

  @Test
  void testDetect_CaseInsensitive_DetectsCmi5WithDifferentCasing() throws Exception {
    // Arrange
    when(mockFileAccess.listFiles("")).thenReturn(List.of("CMI5.XML", "index.html"));

    // Act
    ModuleType result = plugin.detect(mockFileAccess);

    // Assert
    assertEquals(ModuleType.CMI5, result);
    verify(mockFileAccess).listFiles("");
  }
}
