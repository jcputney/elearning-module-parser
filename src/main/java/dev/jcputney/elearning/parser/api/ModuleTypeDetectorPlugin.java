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

/**
 * Interface for plugins that detect specific types of eLearning modules.
 * <p>
 * Implementations of this interface are responsible for detecting a specific type of eLearning
 * module (e.g., SCORM 1.2, SCORM 2004, AICC, cmi5) by examining the structure and manifest files of
 * the module.
 * </p>
 * <p>
 * Plugins are registered with the {@link dev.jcputney.elearning.parser.util.ModuleTypeDetector} and
 * are called in order of priority until one of them successfully detects a module type.
 * </p>
 */
public interface ModuleTypeDetectorPlugin {

  /**
   * Returns the priority of this plugin.
   * <p>
   * Plugins with higher priority values are called before plugins with lower priority values.
   * </p>
   *
   * @return the priority of this plugin
   */
  int getPriority();

  /**
   * Returns the name of this plugin.
   * <p>
   * The name should be a descriptive identifier for the plugin, typically indicating the module
   * type it detects (e.g., "SCORM 1.2 Detector", "AICC Detector").
   * </p>
   *
   * @return the name of this plugin
   */
  String getName();

  /**
   * Attempts to detect if the module is of the type handled by this plugin.
   * <p>
   * This method examines the structure and manifest files of the module to determine if it matches
   * the module type handled by this plugin.
   * </p>
   *
   * @param fileAccess the FileAccess implementation to use for accessing module files
   * @return the detected {@link ModuleType} if this plugin can detect the module type, or null if
   * this plugin cannot detect the module type
   * @throws ModuleDetectionException if there's an error during the detection process
   */
  ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException;
}