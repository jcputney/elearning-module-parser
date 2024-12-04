/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.util;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import java.io.IOException;

/**
 * Determines the type of eLearning module based on the files present in the file system.
 */
public class ModuleTypeDetector {

  private final FileAccess fileAccess;

  public ModuleTypeDetector(FileAccess fileAccess) {
    this.fileAccess = fileAccess;
  }

  public ModuleType detectModuleType() throws ModuleDetectionException {
    try {
      // Check for SCORM 2004
      if (isScorm()) {
        return ScormVersionDetector.detectScormVersion(this.fileAccess);
      }

      // Check for AICC
      if (isAicc()) {
        return ModuleType.AICC;
      }

      // Check for cmi5
      if (fileAccess.fileExists(Cmi5Parser.CMI5_XML)) {
        return ModuleType.CMI5;
      }

      // Unknown module type
      throw new ModuleDetectionException("Unknown module type");
    } catch (Exception e) {
      if (e instanceof ModuleDetectionException) {
        throw (ModuleDetectionException) e;
      }
      throw new ModuleDetectionException("Error detecting module type", e);
    }
  }

  private boolean isScorm() {
    return fileAccess.fileExists(Scorm12Parser.MANIFEST_FILE);
  }

  private boolean isAicc() throws IOException {
    var files = fileAccess.listFiles("");
    return files
        .stream()
        .anyMatch(file -> file.endsWith(AiccParser.AU_EXTENSION) &&
            files.stream().anyMatch(f -> f.endsWith(AiccParser.CRS_EXTENSION)));
  }
}

