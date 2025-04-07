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

package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import java.util.List;

/**
 * Interface for detecting the type of eLearning module.
 *
 * <p>This interface defines the contract for classes that detect the type of eLearning module
 * based on the files present in the file system. Implementations should examine the structure and
 * manifest files of the module to determine its type.
 *
 * <p>The detection process typically uses a plugin system where each plugin is responsible for
 * detecting a specific module type. Plugins are called in order of priority until one of them
 * successfully detects a module type.
 */
public interface ModuleTypeDetector {

  /**
   * Registers a module type detector plugin.
   *
   * @param plugin the plugin to register
   * @throws IllegalArgumentException if plugin is null
   */
  void registerPlugin(ModuleTypeDetectorPlugin plugin);

  /**
   * Unregisters a module type detector plugin.
   *
   * @param plugin the plugin to unregister
   * @return true if the plugin was unregistered, false if it wasn't registered
   * @throws IllegalArgumentException if plugin is null
   */
  boolean unregisterPlugin(ModuleTypeDetectorPlugin plugin);

  /**
   * Returns an unmodifiable list of the registered plugins.
   *
   * @return an unmodifiable list of the registered plugins
   */
  List<ModuleTypeDetectorPlugin> getPlugins();

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
  ModuleType detectModuleType() throws ModuleDetectionException;
}