/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
 * Plugins are registered with the {@link ModuleTypeDetector} and are called in order of priority
 * until one of them successfully detects a module type.
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