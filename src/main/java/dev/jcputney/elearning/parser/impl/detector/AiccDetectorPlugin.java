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