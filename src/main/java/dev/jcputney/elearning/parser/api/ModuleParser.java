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

import dev.jcputney.elearning.parser.exception.ModuleException;
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
   * Parses and validates the eLearning module in a single operation.
   * <p>
   * This is the recommended method for most use cases. It parses the manifest once and returns both
   * validation results and extracted metadata.
   * </p>
   *
   * @return ParseResult containing validation results and module metadata
   * @throws ModuleException if a fatal error occurs (file not found, XML corruption)
   */
  ParseResult<M> parseAndValidate() throws ModuleException;

  /**
   * Parses the eLearning module without validation.
   * <p>
   * Use this method only in performance-critical scenarios where validation is handled separately.
   * Skips all validation checks.
   * </p>
   *
   * @return ModuleMetadata containing the extracted module information
   * @throws ModuleException if a fatal error occurs (file not found, XML corruption)
   */
  ModuleMetadata<M> parseOnly() throws ModuleException;

  /**
   * Gets the parser options controlling validation behavior.
   *
   * @return ParserOptions for this parser
   */
  ParserOptions getOptions();
}
