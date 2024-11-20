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
