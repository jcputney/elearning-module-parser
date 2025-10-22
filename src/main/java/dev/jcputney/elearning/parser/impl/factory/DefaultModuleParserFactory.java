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

package dev.jcputney.elearning.parser.impl.factory;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleParser;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.api.ModuleTypeDetector;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.detector.DefaultModuleTypeDetector;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import dev.jcputney.elearning.parser.parsers.XapiParser;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Default implementation of the ModuleParserFactory interface.
 *
 * <p>This class creates appropriate parsers for different types of eLearning modules
 * based on the detected module type. It uses a ModuleTypeDetector to determine the module type and
 * then creates the corresponding parser.
 */
public final class DefaultModuleParserFactory implements ModuleParserFactory {

  private final ModuleTypeDetector moduleTypeDetector;
  private final FileAccess fileAccess;
  private final Map<ModuleType, Function<FileAccess, ModuleParser<?>>> parserRegistry;

  /**
   * Constructs a new DefaultModuleParserFactory with the specified FileAccess and
   * ModuleTypeDetector implementations.
   *
   * @param fileAccess the FileAccess implementation to use for accessing module files
   * @param moduleTypeDetector the ModuleTypeDetector implementation to use for detecting module
   * types
   * @throws IllegalArgumentException if fileAccess or moduleTypeDetector is null
   */
  public DefaultModuleParserFactory(FileAccess fileAccess, ModuleTypeDetector moduleTypeDetector) {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }
    if (moduleTypeDetector == null) {
      throw new IllegalArgumentException("ModuleTypeDetector cannot be null");
    }
    this.fileAccess = fileAccess;
    this.moduleTypeDetector = moduleTypeDetector;
    this.parserRegistry = new EnumMap<>(ModuleType.class);
    registerDefaultParsers();
  }

  /**
   * Constructs a new DefaultModuleParserFactory with the specified FileAccess implementation and a
   * default ModuleTypeDetector.
   *
   * @param fileAccess the FileAccess implementation to use for accessing module files
   * @throws IllegalArgumentException if fileAccess is null
   */
  public DefaultModuleParserFactory(FileAccess fileAccess) {
    this(fileAccess, new DefaultModuleTypeDetector(fileAccess));
  }

  /**
   * Registers a parser factory function for the specified module type.
   *
   * @param moduleType the module type to register the parser for
   * @param parserFactory the factory function that creates a parser for the specified module type
   * @throws IllegalArgumentException if moduleType or parserFactory is null
   */
  public void registerParser(ModuleType moduleType,
      Function<FileAccess, ModuleParser<?>> parserFactory) {
    if (moduleType == null) {
      throw new IllegalArgumentException("Module type cannot be null");
    }
    if (parserFactory == null) {
      throw new IllegalArgumentException("Parser factory cannot be null");
    }
    parserRegistry.put(moduleType, parserFactory);
  }

  /**
   * Unregisters the parser factory function for the specified module type.
   *
   * @param moduleType the module type to unregister the parser for
   * @return true if a parser was unregistered, false if no parser was registered for the specified
   * module type
   * @throws IllegalArgumentException if moduleType is null
   */
  public boolean unregisterParser(ModuleType moduleType) {
    if (moduleType == null) {
      throw new IllegalArgumentException("Module type cannot be null");
    }
    return parserRegistry.remove(moduleType) != null;
  }

  /**
   * Returns an appropriate parser for the module type detected at the given path.
   *
   * @return A ModuleParser instance for the detected module type.
   * @throws ModuleDetectionException if the module type cannot be determined.
   */
  @Override
  public ModuleParser<?> getParser() throws ModuleDetectionException {
    ModuleType moduleType = moduleTypeDetector.detectModuleType();

    Function<FileAccess, ModuleParser<?>> parserFactory = parserRegistry.get(moduleType);
    if (parserFactory == null) {
      throw new ModuleDetectionException("No parser registered for module type: " + moduleType);
    }
    return parserFactory.apply(fileAccess);
  }

  /**
   * Parses the module at the specified path and returns a ModuleMetadata object containing the
   * extracted metadata.
   *
   * @return A ModuleMetadata object containing the extracted metadata.
   * @throws ModuleDetectionException if the module type cannot be determined.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  @Override
  public ModuleMetadata<?> parseModule() throws ModuleDetectionException, ModuleParsingException {
    return getParser().parse();
  }

  /**
   * Registers the default set of parsers.
   */
  private void registerDefaultParsers() {
    registerParser(ModuleType.SCORM_12, Scorm12Parser::new);
    registerParser(ModuleType.SCORM_2004, Scorm2004Parser::new);
    registerParser(ModuleType.AICC, AiccParser::new);
    registerParser(ModuleType.CMI5, Cmi5Parser::new);
    registerParser(ModuleType.XAPI, XapiParser::new);
  }
}
