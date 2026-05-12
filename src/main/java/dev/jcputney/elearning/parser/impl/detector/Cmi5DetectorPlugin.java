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
package dev.jcputney.elearning.parser.impl.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.util.FileUtils;
import java.io.IOException;

/**
 * Plugin for detecting cmi5 modules.
 * <p>
 * This plugin checks for the presence of a cmi5.xml file, which is the standard manifest file for
 * cmi5 modules.
 * </p>
 */
public class Cmi5DetectorPlugin implements ModuleTypeDetectorPlugin {

  /**
   * The priority of this detector plugin.
   */
  private static final int PRIORITY = 90; // Medium-high priority

  /**
   * The name of this detector plugin.
   */
  private static final String NAME = "cmi5 Detector";

  /**
   * Default constructor for the cmi5 detector plugin.
   */
  public Cmi5DetectorPlugin() {
    // No initialization required
  }

  /**
   * Retrieves the priority of this detector plugin.
   *
   * @return the priority value as an integer, where a higher value indicates a higher priority.
   */
  @Override
  public int getPriority() {
    return PRIORITY;
  }

  /**
   * Retrieves the name of this detector plugin.
   *
   * @return the name of the detector plugin as a String.
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * Detects if the provided FileAccess instance contains a cmi5 module.
   *
   * @param fileAccess The FileAccess instance to check for cmi5 module.
   * @return ModuleType.CMI5 if a cmi5 module is detected, null otherwise.
   * @throws ModuleDetectionException if an error occurs during detection.
   */
  @Override
  public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }

    try {
      var files = fileAccess.listFiles("");
      String cmi5File = FileUtils.findFileIgnoreCase(files, Cmi5Parser.CMI5_XML);

      if (cmi5File != null) {
        return ModuleType.CMI5;
      }

      return null; // Not a cmi5 module
    } catch (IOException e) {
      throw new ModuleDetectionException("Error detecting cmi5 module", e);
    }
  }
}