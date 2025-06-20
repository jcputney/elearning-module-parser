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

package dev.jcputney.elearning.parser.exception;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ModuleDetectionException.
 */
class ModuleDetectionExceptionTest {

  @Test
  void testConstructor_WithMessage_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed");

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  @Test
  void testConstructor_WithMessageAndCause_Success() {
    // Arrange
    Throwable cause = new java.io.IOException("File access error");

    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", cause);

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  @Test
  void testConstructor_WithMessageCauseAndMetadata_Success() {
    // Arrange
    Throwable cause = new java.io.IOException("File access error");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("detectorPlugin", "ScormDetectorPlugin");
    metadata.put("checkedFiles", java.util.Arrays.asList("imsmanifest.xml", "cmi5.xml"));

    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", cause, metadata);

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertEquals(2, exception.getMetadata().size());
    assertEquals("ScormDetectorPlugin", exception.getMetadata("detectorPlugin"));
    assertNotNull(exception.getMetadata("checkedFiles"));
  }

  @Test
  void testConstructor_WithNullMessage_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException(null);

    // Assert
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  @Test
  void testConstructor_WithNullCause_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", null);

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  @Test
  void testConstructor_WithNullMetadata_Success() {
    // Arrange
    Throwable cause = new java.io.IOException("File access error");

    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", cause, null);

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  @Test
  void testInheritance_ExtendsModuleException() {
    // Arrange
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed");

    // Assert
    assertTrue(exception instanceof ModuleException);
    assertTrue(exception instanceof Exception);
    assertTrue(exception instanceof Throwable);
  }

  @Test
  void testInheritedMetadataFunctionality_AddMetadata_Success() {
    // Arrange
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed");

    // Act
    exception.addMetadata("modulePath", "/modules/unknown-module.zip")
            .addMetadata("detectionAttempts", 3);

    // Assert
    assertEquals("/modules/unknown-module.zip", exception.getMetadata("modulePath"));
    assertEquals(3, exception.getMetadata("detectionAttempts"));
    assertEquals(2, exception.getMetadata().size());
  }

  @Test
  void testInheritedMetadataFunctionality_GetMetadata_Success() {
    // Arrange
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("supportedTypes", java.util.Arrays.asList("SCORM_1_2", "SCORM_2004", "AICC", "CMI5"));
    metadata.put("detectionTime", 1500L);
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", null, metadata);

    // Act & Assert
    assertNotNull(exception.getMetadata("supportedTypes"));
    assertEquals(1500L, exception.getMetadata("detectionTime"));
    assertNull(exception.getMetadata("nonexistent"));
  }

  @Test
  void testInheritedToString_WithMetadata_Success() {
    // Arrange
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed");
    exception.addMetadata("zipFile", "unknown-module.zip")
            .addMetadata("fileCount", 15);

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleDetectionException"));
    assertTrue(result.contains("Detection failed"));
    assertTrue(result.contains("Metadata"));
    assertTrue(result.contains("zipFile"));
    assertTrue(result.contains("unknown-module.zip"));
    assertTrue(result.contains("fileCount"));
    assertTrue(result.contains("15"));
  }

  @Test
  void testRealisticScenario_UnsupportedModuleType_Success() {
    // Arrange
    ModuleDetectionException exception = new ModuleDetectionException("Unsupported module type");
    exception.addMetadata("foundFiles", java.util.Arrays.asList("course.xml", "lesson.html"))
            .addMetadata("missingFiles", java.util.Arrays.asList("imsmanifest.xml", "cmi5.xml"))
            .addMetadata("moduleSize", "2.5MB")
            .addMetadata("detectorPlugins", java.util.Arrays.asList("ScormDetectorPlugin", "Cmi5DetectorPlugin", "AiccDetectorPlugin"));

    // Act & Assert
    assertEquals("Unsupported module type", exception.getMessage());
    assertNotNull(exception.getMetadata("foundFiles"));
    assertNotNull(exception.getMetadata("missingFiles"));
    assertEquals("2.5MB", exception.getMetadata("moduleSize"));
    assertNotNull(exception.getMetadata("detectorPlugins"));
  }

  @Test
  void testRealisticScenario_FileAccessError_Success() {
    // Arrange
    java.io.IOException ioException = new java.io.IOException("Unable to read ZIP file");
    Map<String, Object> context = new HashMap<>();
    context.put("zipPath", "/uploads/module.zip");
    context.put("operation", "file-listing");
    context.put("attemptNumber", 2);

    // Act
    ModuleDetectionException exception = new ModuleDetectionException(
        "Error accessing module files during detection", ioException, context);

    // Assert
    assertEquals("Error accessing module files during detection", exception.getMessage());
    assertTrue(exception.getCause() instanceof java.io.IOException);
    assertEquals("Unable to read ZIP file", exception.getCause().getMessage());
    assertEquals("/uploads/module.zip", exception.getMetadata("zipPath"));
    assertEquals("file-listing", exception.getMetadata("operation"));
    assertEquals(2, exception.getMetadata("attemptNumber"));
  }

  @Test
  void testRealisticScenario_CorruptModule_Success() {
    // Arrange
    ModuleDetectionException exception = new ModuleDetectionException("Corrupt module package detected");
    exception.addMetadata("corruptionType", "incomplete-manifest")
            .addMetadata("expectedElements", java.util.Arrays.asList("organizations", "resources"))
            .addMetadata("foundElements", java.util.Arrays.asList("organizations"))
            .addMetadata("manifestSize", 512L)
            .addMetadata("validationErrors", 5);

    // Act & Assert
    assertEquals("Corrupt module package detected", exception.getMessage());
    assertEquals("incomplete-manifest", exception.getMetadata("corruptionType"));
    assertNotNull(exception.getMetadata("expectedElements"));
    assertNotNull(exception.getMetadata("foundElements"));
    assertEquals(512L, exception.getMetadata("manifestSize"));
    assertEquals(5, exception.getMetadata("validationErrors"));
  }

  @Test
  void testRealisticScenario_MultipleDetectorFailures_Success() {
    // Arrange
    RuntimeException detectorException = new RuntimeException("Detector plugin crashed");
    ModuleDetectionException exception = new ModuleDetectionException(
        "All detection plugins failed", detectorException);
    
    Map<String, String> pluginResults = new HashMap<>();
    pluginResults.put("ScormDetectorPlugin", "IOException: manifest not found");
    pluginResults.put("Cmi5DetectorPlugin", "NullPointerException: invalid XML");
    pluginResults.put("AiccDetectorPlugin", "FileNotFoundException: missing .au file");
    
    exception.addMetadata("pluginResults", pluginResults)
            .addMetadata("totalPlugins", 3)
            .addMetadata("successfulPlugins", 0)
            .addMetadata("detectionStrategy", "fail-fast");

    // Act & Assert
    assertEquals("All detection plugins failed", exception.getMessage());
    assertTrue(exception.getCause() instanceof RuntimeException);
    assertEquals(pluginResults, exception.getMetadata("pluginResults"));
    assertEquals(3, exception.getMetadata("totalPlugins"));
    assertEquals(0, exception.getMetadata("successfulPlugins"));
    assertEquals("fail-fast", exception.getMetadata("detectionStrategy"));
  }

  @Test
  void testChainedExceptions_ComplexErrorChain_Success() {
    // Arrange
    RuntimeException rootCause = new RuntimeException("XML parser initialization failed");
    javax.xml.stream.XMLStreamException xmlException = new javax.xml.stream.XMLStreamException("Invalid XML structure", rootCause);
    ModuleDetectionException exception = new ModuleDetectionException("Module detection failed", xmlException);

    // Act & Assert
    assertEquals("Module detection failed", exception.getMessage());
    assertSame(xmlException, exception.getCause());
    assertSame(rootCause, exception.getCause().getCause());
  }

  @Test
  void testEmptyMessage_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException("");

    // Assert
    assertEquals("", exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  @Test
  void testComplexMetadata_DetailedDetectionContext_Success() {
    // Arrange
    Map<String, Object> fileStructure = new HashMap<>();
    fileStructure.put("manifestFiles", java.util.Arrays.asList("manifest.xml", "course.xml"));
    fileStructure.put("contentFiles", java.util.Arrays.asList("index.html", "style.css"));
    fileStructure.put("directories", java.util.Arrays.asList("assets/", "content/"));
    
    ModuleDetectionException exception = new ModuleDetectionException("Complex detection failure");
    exception.addMetadata("fileStructure", fileStructure)
            .addMetadata("detectionStartTime", java.time.Instant.now())
            .addMetadata("heuristics", java.util.Map.of(
                "hasManifest", false,
                "hasLaunchFile", true,
                "hasMetadata", false
            ));

    // Act & Assert
    assertEquals(fileStructure, exception.getMetadata("fileStructure"));
    assertNotNull(exception.getMetadata("detectionStartTime"));
    assertNotNull(exception.getMetadata("heuristics"));
    assertEquals(3, exception.getMetadata().size());
  }
}