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