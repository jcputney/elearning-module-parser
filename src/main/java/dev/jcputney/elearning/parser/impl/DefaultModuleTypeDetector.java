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

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetector;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.util.detector.AiccDetectorPlugin;
import dev.jcputney.elearning.parser.util.detector.Cmi5DetectorPlugin;
import dev.jcputney.elearning.parser.util.detector.ScormDetectorPlugin;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Default implementation of the ModuleTypeDetector interface.
 *
 * <p>This class determines the type of eLearning module based on the files present in the file
 * system. It uses a plugin system where each plugin is responsible for detecting a specific module
 * type. Plugins are called in order of priority until one of them successfully detects a module
 * type.
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
 */
public class DefaultModuleTypeDetector implements ModuleTypeDetector {

  private final FileAccess fileAccess;
  private final List<ModuleTypeDetectorPlugin> plugins;

  /**
   * Constructs a new DefaultModuleTypeDetector with the specified FileAccess implementation.
   *
   * @param fileAccess the FileAccess implementation to use for accessing module files
   * @throws IllegalArgumentException if fileAccess is null
   */
  public DefaultModuleTypeDetector(FileAccess fileAccess) {
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
   * @throws IllegalArgumentException if the plugin is null
   */
  @Override
  public void registerPlugin(ModuleTypeDetectorPlugin plugin) {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }
    plugins.add(plugin);
    // Sort plugins by priority (highest first)
    plugins.sort(Comparator
        .comparingInt(ModuleTypeDetectorPlugin::getPriority)
        .reversed());
  }

  /**
   * Unregisters a module type detector plugin.
   *
   * @param plugin the plugin to unregister
   * @return true if the plugin was unregistered, false if it wasn't registered
   * @throws IllegalArgumentException if the plugin is null
   */
  @Override
  public boolean unregisterPlugin(ModuleTypeDetectorPlugin plugin) {
    if (plugin == null) {
      throw new IllegalArgumentException("Plugin cannot be null");
    }
    return plugins.remove(plugin);
  }

  /**
   * Returns an unmodifiable list of the registered plugins.
   *
   * @return an unmodifiable list of the registered plugins
   */
  @Override
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
   * @throws ModuleDetectionException if the module type cannot be detected, or if there's an error
   * during the detection process
   */
  @Override
  public ModuleType detectModuleType() throws ModuleDetectionException {
    if (plugins.isEmpty()) {
      throw new ModuleDetectionException(
          "No module type detector plugins are registered. Cannot detect module type."
      );
    }

    try {
      for (ModuleTypeDetectorPlugin plugin : plugins) {
        ModuleType moduleType = plugin.detect(fileAccess);
        if (moduleType != null) {
          return moduleType;
        }
      }

      // If we get here, none of the plugins could detect the module type
      // Build a helpful error message listing what was tried
      StringBuilder triedPlugins = new StringBuilder();
      for (int i = 0; i < plugins.size(); i++) {
        if (i > 0) {
          triedPlugins.append(", ");
        }
        triedPlugins.append(plugins
            .get(i)
            .getName());
      }

      throw new ModuleDetectionException(String.format(
          "Unable to detect module type at '%s'. Tried plugins: [%s]. " +
              "Module might be corrupted or of an unsupported type.",
          fileAccess.getRootPath(), triedPlugins
      ));
    } catch (Exception e) {
      if (e instanceof ModuleDetectionException) {
        throw e;
      }
      throw new ModuleDetectionException(
          String.format("Error detecting module type at '%s': %s",
              fileAccess.getRootPath(), e.getMessage()),
          e
      );
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