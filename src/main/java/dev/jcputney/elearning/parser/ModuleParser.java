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

package dev.jcputney.elearning.parser;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

public interface ModuleParser {

  /**
   * Parses the module files and extracts metadata.
   *
   * @param modulePath The path to the module in the file system.
   * @return A ModuleMetadata object containing standardized metadata.
   * @throws ModuleParsingException if parsing fails or required files are missing.
   */
  ModuleMetadata parse(String modulePath) throws ModuleParsingException;

  /**
   * Checks if the module at the provided path is supported by this parser.
   *
   * @param modulePath The path to the module in the file system.
   * @return True if the module is of a type that this parser can handle; false otherwise.
   */
  boolean isSupported(String modulePath);
}
