package dev.jcputney.elearning.parser.exception;

/**
 * Exception thrown when a module cannot be detected.
 *
 * @author jcputney
 * @since 1.0.0
 */
public class ModuleDetectionException extends Exception {

  public ModuleDetectionException(String message) {
    super(message);
  }

  public ModuleDetectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
