/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleException;
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
  ModuleMetadata<?> parseModule() throws ModuleException;
}