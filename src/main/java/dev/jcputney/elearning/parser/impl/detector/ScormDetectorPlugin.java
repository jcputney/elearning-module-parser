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
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.util.FileUtils;
import dev.jcputney.elearning.parser.util.ScormVersionDetector;
import java.io.IOException;

/**
 * Plugin for detecting SCORM modules (both 1.2 and 2004 versions).
 * <p>
 * This plugin checks for the presence of an imsmanifest.xml file, which is the standard manifest
 * file for SCORM modules. If found, it uses the {@link ScormVersionDetector} to determine the
 * specific SCORM version.
 * </p>
 */
public class ScormDetectorPlugin implements ModuleTypeDetectorPlugin {

  /**
   * The priority of this detector plugin.
   */
  private static final int PRIORITY = 100; // High priority since SCORM is common

  /**
   * The name of this detector plugin.
   */
  private static final String NAME = "SCORM Detector";

  /**
   * Default constructor for the SCORM detector plugin.
   */
  public ScormDetectorPlugin() {
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
   * Detects the SCORM module type by checking for the presence of the SCORM manifest file.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   * @return The detected SCORM module type, or null if not a SCORM module.
   * @throws ModuleDetectionException If an error occurs during detection.
   */
  @Override
  public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }

    try {
      var files = fileAccess.listFiles("");
      String manifestFile = FileUtils.findFileIgnoreCase(files, Scorm12Parser.MANIFEST_FILE);

      if (manifestFile != null) {
        // Use the ScormVersionDetector to determine the specific SCORM version
        return ScormVersionDetector.detectScormVersion(fileAccess);
      }

      return null; // Not SCORM module
    } catch (IOException e) {
      throw new ModuleDetectionException("Error detecting SCORM module", e);
    } catch (Exception e) {
      throw new ModuleDetectionException("Error detecting SCORM version", e);
    }
  }
}