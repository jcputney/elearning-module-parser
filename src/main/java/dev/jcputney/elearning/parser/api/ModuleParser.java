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

import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.validation.ValidationResult;

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
   * Validates the module without parsing.
   * Collects all validation issues and returns them without throwing exceptions.
   *
   * @return ValidationResult containing any errors or warnings found
   */
  ValidationResult validate();

  /**
   * Parses the module files and extracts metadata.
   * Runs validation first according to ParserOptions.
   * In strict mode, throws exception if validation errors found.
   * In lenient mode, continues parsing and attaches validation result to metadata.
   *
   * @return A ModuleMetadata object containing standardized metadata.
   * @throws ModuleException if parsing fails or validation fails in strict mode.
   */
  ModuleMetadata<M> parse() throws ModuleException;

  /**
   * Gets the parser options controlling validation behavior.
   *
   * @return ParserOptions for this parser
   */
  ParserOptions getOptions();
}
