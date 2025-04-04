/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.util;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.util.detector.AiccDetectorPlugin;
import dev.jcputney.elearning.parser.util.detector.Cmi5DetectorPlugin;
import dev.jcputney.elearning.parser.util.detector.ScormDetectorPlugin;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;

/**
 * Determines the type of eLearning module based on the files present in the file system.
 *
 * <p>This class is responsible for detecting the type of eLearning module (SCORM 1.2, SCORM 2004,
 * AICC, cmi5) by examining the structure and manifest files of the module. It uses a
 * {@link FileAccess} implementation to access the module files.
 *
 * <p>The detection process uses a plugin system where each plugin is responsible for detecting
 * a specific module type. Plugins are called in order of priority until one of them successfully
 * detects a module type.
 *
 * <p>By default, the following plugins are registered:
 * <ol>
 *   <li>{@link ScormDetectorPlugin} - Detects SCORM 1.2 and SCORM 2004 modules</li>
 *   <li>{@link Cmi5DetectorPlugin} - Detects cmi5 modules</li>
 *   <li>{@link AiccDetectorPlugin} - Detects AICC modules</li>
 * </ol>
 *
 * <p>Additional plugins can be registered using the {@link #registerPlugin(ModuleTypeDetectorPlugin)}
 * method.
 *
 * <p>If none of the registered plugins can detect a module type, a {@link ModuleDetectionException}
 * is thrown.
 */
public class ModuleTypeDetector {

  private static final Logger log = LoggingUtils.getLogger(ModuleTypeDetector.class);

  /**
   * The FileAccess implementation used to access module files.
   */
  private final FileAccess fileAccess;

  /**
   * The list of registered module type detector plugins.
   */
  private final List<ModuleTypeDetectorPlugin> plugins;

  /**
   * Constructs a new ModuleTypeDetector with the specified FileAccess implementation.
   *
   * @param fileAccess the FileAccess implementation to use for accessing module files
   * @throws IllegalArgumentException if fileAccess is null
   */
  public ModuleTypeDetector(FileAccess fileAccess) {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }
    this.fileAccess = fileAccess;
    this.plugins = new ArrayList<>();

    // Register default plugins
    registerDefaultPlugins();
  }

  /**
   * Registers a module type detector plugin.
   *
   * @param plugin the plugin to register
   * @throws IllegalArgumentException if plugin is null
   */
  public void registerPlugin(ModuleTypeDetectorPlugin plugin) {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }
    log.debug("Registering module type detector plugin: {}", plugin.getName());
    plugins.add(plugin);
    // Sort plugins by priority (highest first)
    plugins.sort(Comparator.comparingInt(ModuleTypeDetectorPlugin::getPriority).reversed());
  }

  /**
   * Unregisters a module type detector plugin.
   *
   * @param plugin the plugin to unregister
   * @return true if the plugin was unregistered, false if it wasn't registered
   * @throws IllegalArgumentException if plugin is null
   */
  public boolean unregisterPlugin(ModuleTypeDetectorPlugin plugin) {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }
    log.debug("Unregistering module type detector plugin: {}", plugin.getName());
    return plugins.remove(plugin);
  }

  /**
   * Returns an unmodifiable list of the registered plugins.
   *
   * @return an unmodifiable list of the registered plugins
   */
  public List<ModuleTypeDetectorPlugin> getPlugins() {
    return List.copyOf(plugins);
  }

  /**
   * Detects the type of eLearning module based on the files present in the file system.
   *
   * <p>This method calls each registered plugin in order of priority until one of them
   * successfully detects a module type.
   *
   * @return the detected {@link ModuleType}
   * @throws ModuleDetectionException if the module type cannot be detected or if there's an error
   * during the detection process
   */
  public ModuleType detectModuleType() throws ModuleDetectionException {
    log.debug("Detecting module type using {} plugins", plugins.size());

    try {
      for (ModuleTypeDetectorPlugin plugin : plugins) {
        log.debug("Trying plugin: {}", plugin.getName());
        ModuleType moduleType = plugin.detect(fileAccess);
        if (moduleType != null) {
          log.debug("Plugin {} detected module type: {}", plugin.getName(), moduleType);
          return moduleType;
        }
      }

      // If we get here, none of the plugins could detect the module type
      log.error("No plugin could detect the module type");
      throw new ModuleDetectionException("Unknown module type");
    } catch (Exception e) {
      if (e instanceof ModuleDetectionException) {
        throw e;
      }
      log.error("Error detecting module type: {}", e.getMessage());
      throw new ModuleDetectionException("Error detecting module type", e);
    }
  }

  /**
   * Registers the default set of module type detector plugins.
   */
  private void registerDefaultPlugins() {
    registerPlugin(new ScormDetectorPlugin());
    registerPlugin(new Cmi5DetectorPlugin());
    registerPlugin(new AiccDetectorPlugin());
  }
}
