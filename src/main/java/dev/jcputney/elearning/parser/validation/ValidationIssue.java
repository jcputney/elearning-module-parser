package dev.jcputney.elearning.parser.validation;

/**
 * Represents a single validation issue found during module parsing.
 * Immutable record containing severity, error code, message, location, and optional suggestion.
 */
public record ValidationIssue(
    Severity severity,
    String code,
    String message,
    String location,
    String suggestedFix
) {

    /**
     * Severity levels for validation issues.
     */
    public enum Severity {
        /**
         * Spec violation that prevents proper operation.
         * Will fail in compliant LMS systems.
         */
        ERROR,

        /**
         * Bad practice or missing optional files.
         * Might work but risky.
         */
        WARNING
    }

    /**
     * Creates an error-level validation issue without a suggested fix.
     *
     * @param code Error code (e.g., "SCORM12_MISSING_RESOURCE_REF")
     * @param message Human-readable error message
     * @param location Location context (e.g., XPath or file location)
     * @return ValidationIssue with ERROR severity
     */
    public static ValidationIssue error(String code, String message, String location) {
        return new ValidationIssue(Severity.ERROR, code, message, location, null);
    }

    /**
     * Creates an error-level validation issue with a suggested fix.
     *
     * @param code Error code (e.g., "SCORM12_MISSING_RESOURCE_REF")
     * @param message Human-readable error message
     * @param location Location context (e.g., XPath or file location)
     * @param suggestedFix Suggested fix for the issue
     * @return ValidationIssue with ERROR severity
     */
    public static ValidationIssue error(String code, String message, String location, String suggestedFix) {
        return new ValidationIssue(Severity.ERROR, code, message, location, suggestedFix);
    }

    /**
     * Creates a warning-level validation issue without a suggested fix.
     *
     * @param code Warning code
     * @param message Human-readable warning message
     * @param location Location context
     * @return ValidationIssue with WARNING severity
     */
    public static ValidationIssue warning(String code, String message, String location) {
        return new ValidationIssue(Severity.WARNING, code, message, location, null);
    }

    /**
     * Creates a warning-level validation issue with a suggested fix.
     *
     * @param code Warning code
     * @param message Human-readable warning message
     * @param location Location context
     * @param suggestedFix Suggested fix for the issue
     * @return ValidationIssue with WARNING severity
     */
    public static ValidationIssue warning(String code, String message, String location, String suggestedFix) {
        return new ValidationIssue(Severity.WARNING, code, message, location, suggestedFix);
    }
}
