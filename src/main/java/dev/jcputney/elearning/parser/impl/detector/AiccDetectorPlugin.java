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
import dev.jcputney.elearning.parser.parsers.AiccParser;
import java.io.IOException;
import org.apache.commons.lang3.Strings;

/**
 * Plugin for detecting AICC modules.
 * <p>
 * This plugin checks for the presence of .au and .crs files, which are standard files for AICC
 * modules.
 * </p>
 */
public class AiccDetectorPlugin implements ModuleTypeDetectorPlugin {

  /**
   * The priority of this detector plugin.
   */
  private static final int PRIORITY = 80; // Medium priority

  /**
   * The name of this detector plugin.
   */
  private static final String NAME = "AICC Detector";

  /**
   * Default constructor for the AICC detector plugin.
   */
  public AiccDetectorPlugin() {
    // No initialization required
  }

  /**
   * Retrieves the priority of this detector plugin.
   *
   * @return the priority value as an integer, where a higher value denotes a higher priority.
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
   * Detects if the provided FileAccess instance contains AICC module files.
   *
   * @param fileAccess The FileAccess instance to check for AICC module files.
   * @return ModuleType.AICC if AICC files are found, null otherwise.
   * @throws ModuleDetectionException if an error occurs during detection.
   */
  @Override
  public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }

    try {
      var files = fileAccess.listFiles("");
      boolean isAicc = files
          .stream()
          .anyMatch(file -> Strings.CI.endsWith(file, AiccParser.AU_EXTENSION)
              && files
              .stream()
              .anyMatch(f -> Strings.CI.endsWith(f, AiccParser.CRS_EXTENSION)));

      if (isAicc) {
        return ModuleType.AICC;
      }

      return null; // Not an AICC module
    } catch (IOException e) {
      throw new ModuleDetectionException("Error detecting AICC module", e);
    }
  }
}