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

package dev.jcputney.elearning.parser.util.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.util.LoggingUtils;
import org.slf4j.Logger;

/**
 * Plugin for detecting cmi5 modules.
 * <p>
 * This plugin checks for the presence of a cmi5.xml file, which is the standard manifest file for
 * cmi5 modules.
 * </p>
 */
public class Cmi5DetectorPlugin implements ModuleTypeDetectorPlugin {

  /**
   * Logger for logging messages related to cmi5 module detection.
   */
  private static final Logger log = LoggingUtils.getLogger(Cmi5DetectorPlugin.class);

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

  @Override
  public int getPriority() {
    return PRIORITY;
  }

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

    log.debug("Checking for cmi5 module");
    if (fileAccess.fileExists(Cmi5Parser.CMI5_XML)) {
      log.debug("Found cmi5 manifest file: {}", Cmi5Parser.CMI5_XML);
      return ModuleType.CMI5;
    }

    log.debug("No cmi5 manifest file found");
    return null; // Not a cmi5 module
  }
}