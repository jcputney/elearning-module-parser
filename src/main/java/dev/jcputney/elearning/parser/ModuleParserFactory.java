package dev.jcputney.elearning.parser;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import dev.jcputney.elearning.parser.util.ModuleTypeDetector;

public class ModuleParserFactory {

  private final ModuleTypeDetector moduleTypeDetector;
  private final FileAccess fileAccess;

  public ModuleParserFactory(FileAccess fileAccess) {
    this.fileAccess = fileAccess;
    this.moduleTypeDetector = new ModuleTypeDetector(fileAccess);
  }

  /**
   * Returns an appropriate parser for the module type detected at the given path.
   *
   * @param modulePath The path to the module files.
   * @return A ModuleParser instance for the detected module type.
   * @throws ModuleDetectionException if the module type cannot be determined.
   */
  public ModuleParser getParser(String modulePath) throws ModuleDetectionException {
    ModuleType moduleType = moduleTypeDetector.detectModuleType(modulePath);

    return switch (moduleType) {
      case SCORM_12 -> new Scorm12Parser(fileAccess);
      case SCORM_2004 -> new Scorm2004Parser(fileAccess);
      case AICC -> new AiccParser(fileAccess);
      case CMI5 -> new Cmi5Parser(fileAccess);
    };
  }

  /**
   * Parses the module at the specified path and returns a ModuleMetadata object containing the
   * extracted metadata.
   *
   * @param modulePath The path to the module's root directory.
   * @return A ModuleMetadata object containing the extracted metadata.
   * @throws ModuleDetectionException if the module type cannot be determined.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  public ModuleMetadata parseModule(String modulePath)
      throws ModuleDetectionException, ModuleParsingException {
    return getParser(modulePath).parse(modulePath);
  }
}
