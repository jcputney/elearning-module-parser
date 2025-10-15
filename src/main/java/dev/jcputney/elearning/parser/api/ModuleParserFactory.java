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

package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

/**
 * Interface for creating and managing module parsers.
 *
 * <p>This interface defines the contract for factory classes that create appropriate parsers
 * for different types of eLearning modules. Implementations should detect the module type and
 * return the corresponding parser.
 *
 * <p>This interface follows the Factory pattern, allowing different strategies for creating
 * parsers to be implemented and used interchangeably.
 */
public interface ModuleParserFactory {

  /**
   * Returns an appropriate parser for the module type detected.
   *
   * @return A ModuleParser instance for the detected module type.
   * @throws ModuleDetectionException if the module type cannot be determined.
   */
  ModuleParser<?> getParser() throws ModuleDetectionException;

  /**
   * Parses the module and returns a ModuleMetadata object containing the extracted metadata.
   *
   * @return A ModuleMetadata object containing the extracted metadata.
   * @throws ModuleDetectionException if the module type cannot be determined.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  ModuleMetadata<?> parseModule() throws ModuleDetectionException, ModuleParsingException;
}