package dev.jcputney.elearning.parser.exception;

public class ModuleParsingException extends Exception {

  public ModuleParsingException(String message) {
    super(message);
  }

  public ModuleParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
