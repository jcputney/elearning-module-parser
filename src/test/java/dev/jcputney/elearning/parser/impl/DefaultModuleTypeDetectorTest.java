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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetector;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.util.detector.AiccDetectorPlugin;
import dev.jcputney.elearning.parser.util.detector.Cmi5DetectorPlugin;
import dev.jcputney.elearning.parser.util.detector.ScormDetectorPlugin;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DefaultModuleTypeDetector} class.
 */
class DefaultModuleTypeDetectorTest {

  private MockFileAccess mockFileAccess;
  private DefaultModuleTypeDetector detector;

  @BeforeEach
  void setUp() {
    mockFileAccess = new MockFileAccess("root/path");
    detector = new DefaultModuleTypeDetector(mockFileAccess);
  }

  @Test
  void constructor_withNullFileAccess_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultModuleTypeDetector(null));
  }

  @Test
  void constructor_registersDefaultPlugins() {
    List<ModuleTypeDetectorPlugin> plugins = detector.getPlugins();

    // Verify that default plugins are registered
    assertEquals(3, plugins.size());
    assertInstanceOf(ScormDetectorPlugin.class, plugins.get(0));
    assertInstanceOf(Cmi5DetectorPlugin.class, plugins.get(1));
    assertInstanceOf(AiccDetectorPlugin.class, plugins.get(2));
  }

  @Test
  void registerPlugin_withValidPlugin_addsPluginToList() {
    MockDetectorPlugin plugin = new MockDetectorPlugin("TestPlugin", 100, ModuleType.SCORM_12);

    detector.registerPlugin(plugin);

    List<ModuleTypeDetectorPlugin> plugins = detector.getPlugins();
    assertTrue(plugins.contains(plugin));
  }

  @Test
  void registerPlugin_withNullPlugin_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> detector.registerPlugin(null));
  }

  @Test
  void registerPlugin_sortsPluginsByPriorityDescending() {
    // Clear default plugins
    List<ModuleTypeDetectorPlugin> defaultPlugins = detector.getPlugins();
    for (ModuleTypeDetectorPlugin plugin : defaultPlugins) {
      detector.unregisterPlugin(plugin);
    }

    // Register plugins with different priorities
    MockDetectorPlugin lowPriorityPlugin = new MockDetectorPlugin("LowPriority", 1,
        ModuleType.SCORM_12);
    MockDetectorPlugin mediumPriorityPlugin = new MockDetectorPlugin("MediumPriority", 5,
        ModuleType.SCORM_2004);
    MockDetectorPlugin highPriorityPlugin = new MockDetectorPlugin("HighPriority", 10,
        ModuleType.CMI5);

    detector.registerPlugin(lowPriorityPlugin);
    detector.registerPlugin(mediumPriorityPlugin);
    detector.registerPlugin(highPriorityPlugin);

    List<ModuleTypeDetectorPlugin> plugins = detector.getPlugins();
    assertEquals(3, plugins.size());
    assertEquals(highPriorityPlugin, plugins.get(0));
    assertEquals(mediumPriorityPlugin, plugins.get(1));
    assertEquals(lowPriorityPlugin, plugins.get(2));
  }

  @Test
  void unregisterPlugin_withRegisteredPlugin_removesPluginFromList() {
    MockDetectorPlugin plugin = new MockDetectorPlugin("TestPlugin", 100, ModuleType.SCORM_12);
    detector.registerPlugin(plugin);

    boolean result = detector.unregisterPlugin(plugin);

    assertTrue(result);
    List<ModuleTypeDetectorPlugin> plugins = detector.getPlugins();
    assertFalse(plugins.contains(plugin));
  }

  @Test
  void unregisterPlugin_withUnregisteredPlugin_returnsFalse() {
    MockDetectorPlugin plugin = new MockDetectorPlugin("TestPlugin", 100, ModuleType.SCORM_12);

    boolean result = detector.unregisterPlugin(plugin);

    assertFalse(result);
  }

  @Test
  void unregisterPlugin_withNullPlugin_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> detector.unregisterPlugin(null));
  }

  @Test
  void getPlugins_returnsUnmodifiableList() {
    List<ModuleTypeDetectorPlugin> plugins = detector.getPlugins();

    assertThrows(UnsupportedOperationException.class, () -> plugins.add(
        new MockDetectorPlugin("TestPlugin", 100, ModuleType.SCORM_12)));
  }

  @Test
  void detectModuleType_withSuccessfulDetection_returnsModuleType()
      throws ModuleDetectionException {
    // Clear default plugins
    List<ModuleTypeDetectorPlugin> defaultPlugins = detector.getPlugins();
    for (ModuleTypeDetectorPlugin plugin : defaultPlugins) {
      detector.unregisterPlugin(plugin);
    }

    // Register a plugin that will successfully detect a module type
    MockDetectorPlugin plugin = new MockDetectorPlugin("TestPlugin", 100, ModuleType.SCORM_12);
    detector.registerPlugin(plugin);

    ModuleType result = detector.detectModuleType();

    assertEquals(ModuleType.SCORM_12, result);
    assertEquals(1, plugin.getDetectCallCount());
  }

  @Test
  void detectModuleType_withMultiplePlugins_callsPluginsInPriorityOrder()
      throws ModuleDetectionException {
    // Clear default plugins
    List<ModuleTypeDetectorPlugin> defaultPlugins = detector.getPlugins();
    for (ModuleTypeDetectorPlugin plugin : defaultPlugins) {
      detector.unregisterPlugin(plugin);
    }

    // Register plugins with different priorities
    MockDetectorPlugin lowPriorityPlugin = new MockDetectorPlugin("LowPriority", 1,
        ModuleType.SCORM_12);
    MockDetectorPlugin mediumPriorityPlugin = new MockDetectorPlugin("MediumPriority", 5, null);
    MockDetectorPlugin highPriorityPlugin = new MockDetectorPlugin("HighPriority", 10, null);

    detector.registerPlugin(lowPriorityPlugin);
    detector.registerPlugin(mediumPriorityPlugin);
    detector.registerPlugin(highPriorityPlugin);

    ModuleType result = detector.detectModuleType();

    assertEquals(ModuleType.SCORM_12, result);
    assertEquals(1, highPriorityPlugin.getDetectCallCount());
    assertEquals(1, mediumPriorityPlugin.getDetectCallCount());
    assertEquals(1, lowPriorityPlugin.getDetectCallCount());
  }

  @Test
  void detectModuleType_withNoSuccessfulDetection_throwsModuleDetectionException() {
    // Clear default plugins
    List<ModuleTypeDetectorPlugin> defaultPlugins = detector.getPlugins();
    for (ModuleTypeDetectorPlugin plugin : defaultPlugins) {
      detector.unregisterPlugin(plugin);
    }

    // Register a plugin that will not detect any module type
    MockDetectorPlugin plugin = new MockDetectorPlugin("TestPlugin", 100, null);
    detector.registerPlugin(plugin);

    assertThrows(ModuleDetectionException.class, () -> detector.detectModuleType());
    assertEquals(1, plugin.getDetectCallCount());
  }

  @Test
  void detectModuleType_withPluginThatThrowsException_propagatesException() {
    // Clear default plugins
    List<ModuleTypeDetectorPlugin> defaultPlugins = detector.getPlugins();
    for (ModuleTypeDetectorPlugin plugin : defaultPlugins) {
      detector.unregisterPlugin(plugin);
    }

    // Register a plugin that will throw an exception
    MockDetectorPlugin plugin = new MockDetectorPlugin("TestPlugin", 100, null, true);
    detector.registerPlugin(plugin);

    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class,
        () -> detector.detectModuleType());
    assertEquals("Mock detection exception", exception.getMessage());
    assertEquals(1, plugin.getDetectCallCount());
  }

  @Test
  void detectModuleType_withPluginThatThrowsNonModuleDetectionException_wrapsException() {
    // Clear default plugins
    List<ModuleTypeDetectorPlugin> defaultPlugins = detector.getPlugins();
    for (ModuleTypeDetectorPlugin plugin : defaultPlugins) {
      detector.unregisterPlugin(plugin);
    }

    // Register a plugin that will throw a non-ModuleDetectionException
    ModuleTypeDetectorPlugin plugin = new ModuleTypeDetectorPlugin() {
      @Override
      public int getPriority() {
        return 100;
      }

      @Override
      public String getName() {
        return "ExceptionPlugin";
      }

      @Override
      public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
        throw new RuntimeException("Test exception");
      }
    };
    detector.registerPlugin(plugin);

    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class,
        () -> detector.detectModuleType());
    assertEquals("Error detecting module type", exception.getMessage());
    assertInstanceOf(RuntimeException.class, exception.getCause());
    assertEquals("Test exception", exception.getCause().getMessage());
  }

  /**
   * A mock implementation of {@link FileAccess} for testing.
   */
  private static class MockFileAccess implements FileAccess {

    private final String rootPath;
    private final Map<String, Boolean> fileExistsResponses = new HashMap<>();
    private final Map<String, List<String>> listFilesResponses = new HashMap<>();
    private final Map<String, byte[]> fileContentsResponses = new HashMap<>();

    public MockFileAccess(String rootPath) {
      this.rootPath = rootPath;
    }

    public void setFileExistsResponse(String path, boolean exists) {
      fileExistsResponses.put(path, exists);
    }

    public void setListFilesResponse(String path, List<String> files) {
      listFilesResponses.put(path, files);
    }

    public void setFileContentsResponse(String path, byte[] contents) {
      fileContentsResponses.put(path, contents);
    }

    @Override
    public String getRootPath() {
      return rootPath;
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return fileExistsResponses.getOrDefault(path, false);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      return listFilesResponses.getOrDefault(directoryPath, Collections.emptyList());
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      byte[] contents = fileContentsResponses.getOrDefault(path, new byte[0]);
      return new ByteArrayInputStream(contents);
    }
  }

  /**
   * A mock implementation of {@link ModuleTypeDetectorPlugin} for testing.
   */
  private static class MockDetectorPlugin implements ModuleTypeDetectorPlugin {

    private final String name;
    private final int priority;
    private final ModuleType moduleType;
    private final boolean throwException;
    private int detectCallCount = 0;

    public MockDetectorPlugin(String name, int priority, ModuleType moduleType) {
      this(name, priority, moduleType, false);
    }

    public MockDetectorPlugin(String name, int priority, ModuleType moduleType,
        boolean throwException) {
      this.name = name;
      this.priority = priority;
      this.moduleType = moduleType;
      this.throwException = throwException;
    }

    public int getDetectCallCount() {
      return detectCallCount;
    }

    @Override
    public int getPriority() {
      return priority;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
      detectCallCount++;
      if (throwException) {
        throw new ModuleDetectionException("Mock detection exception");
      }
      return moduleType;
    }
  }
}