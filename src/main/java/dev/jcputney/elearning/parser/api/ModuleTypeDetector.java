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
   * @throws IllegalArgumentException if the plugin is null
   */
  void registerPlugin(ModuleTypeDetectorPlugin plugin);

  /**
   * Unregisters a module type detector plugin.
   *
   * @param plugin the plugin to unregister
   * @return true if the plugin was unregistered, false if it wasn't registered
   * @throws IllegalArgumentException if the plugin is null
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
   * @throws ModuleDetectionException if the module type cannot be detected, or if there's an error
   * during the detection process
   */
  ModuleType detectModuleType() throws ModuleDetectionException;
}