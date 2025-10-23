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

package dev.jcputney.elearning.parser.impl.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.util.FileUtils;
import java.io.IOException;

/**
 * Detector plugin for xAPI/TinCan packages.
 *
 * <p>Detects xAPI modules by looking for the presence of a {@code tincan.xml}
 * manifest file in the root directory.</p>
 *
 * <p>Priority: 40 (after cmi5 which is 50, before SCORM which is 30)</p>
 */
public class XapiDetectorPlugin implements ModuleTypeDetectorPlugin {

  private static final String TINCAN_MANIFEST = "tincan.xml";

  /**
   * Constructs a new XapiDetectorPlugin.
   */
  public XapiDetectorPlugin() {
    // Default constructor
  }

  /**
   * Detects if the module is an xAPI/TinCan package.
   *
   * @param fileAccess the file access interface
   * @return {@link ModuleType#XAPI} if tincan.xml exists, null otherwise
   * @throws ModuleDetectionException if an error occurs during detection
   */
  @Override
  public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }

    try {
      var files = fileAccess.listFiles("");
      String tincanFile = FileUtils.findFileIgnoreCase(files, TINCAN_MANIFEST);

      if (tincanFile != null) {
        return ModuleType.XAPI;
      }

      return null;
    } catch (IOException e) {
      throw new ModuleDetectionException("Error detecting xAPI module", e);
    }
  }

  /**
   * Returns the priority for this detector.
   * Higher priority detectors run first.
   *
   * @return 40
   */
  @Override
  public int getPriority() {
    return 40;
  }

  /**
   * Returns the name of this detector plugin.
   *
   * @return "xAPI/TinCan"
   */
  @Override
  public String getName() {
    return "xAPI/TinCan";
  }
}
