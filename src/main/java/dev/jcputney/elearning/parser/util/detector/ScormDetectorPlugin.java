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

package dev.jcputney.elearning.parser.util.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.util.ScormVersionDetector;

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

  @Override
  public int getPriority() {
    return PRIORITY;
  }

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

    if (fileAccess.fileExists(Scorm12Parser.MANIFEST_FILE)) {
      try {
        // Use the ScormVersionDetector to determine the specific SCORM version
        return ScormVersionDetector.detectScormVersion(fileAccess);
      } catch (Exception e) {
        throw new ModuleDetectionException("Error detecting SCORM version", e);
      }
    }

    return null; // Not SCORM module
  }
}