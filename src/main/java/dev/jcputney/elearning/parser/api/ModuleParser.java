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

package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

/**
 * Interface for parsing module files and extracting metadata.
 * <p>
 * This interface defines the contract for parsers that handle different types of eLearning modules,
 * such as SCORM, xAPI, and others. Implementations should provide methods to parse the module files
 * and extract relevant metadata.
 * </p>
 *
 * @param <M> The type of package manifest associated with the module.
 */
public interface ModuleParser<M extends PackageManifest> {

  /**
   * Parses the module files and extracts metadata.
   *
   * @return A ModuleMetadata object containing standardized metadata.
   * @throws ModuleParsingException if parsing fails or required files are missing.
   */
  ModuleMetadata<M> parse() throws ModuleParsingException;
}
