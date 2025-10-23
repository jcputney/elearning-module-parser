package dev.jcputney.elearning.parser.validation;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Immutable container for validation issues found during module parsing.
 * Supports composition via merge() for combining results from multiple validators.
 */
public final class ValidationResult {
    private final List<ValidationIssue> issues;

    private ValidationResult(List<ValidationIssue> issues) {
        this.issues = List.copyOf(issues);
    }

    /**
     * Creates a valid result with no issues.
     *
     * @return Empty ValidationResult
     */
    public static ValidationResult valid() {
        return new ValidationResult(List.of());
    }

    /**
     * Creates a result with the specified issues.
     *
     * @param issues Validation issues to include
     * @return ValidationResult containing the issues
     */
    public static ValidationResult of(ValidationIssue... issues) {
        return new ValidationResult(List.of(issues));
    }

    /**
     * Merges this result with another, combining their issues.
     *
     * @param other Another ValidationResult to merge
     * @return New ValidationResult containing issues from both
     */
    public ValidationResult merge(ValidationResult other) {
        List<ValidationIssue> merged = new ArrayList<>(this.issues);
        merged.addAll(other.issues);
        return new ValidationResult(merged);
    }

    /**
     * Checks if this result is valid (no errors).
     * Warnings do not affect validity.
     *
     * @return true if no errors present
     */
    public boolean isValid() {
        return !hasErrors();
    }

    /**
     * Checks if any ERROR-level issues are present.
     *
     * @return true if at least one error exists
     */
    public boolean hasErrors() {
        return issues.stream()
            .anyMatch(issue -> issue.severity() == ValidationIssue.Severity.ERROR);
    }

    /**
     * Checks if any WARNING-level issues are present.
     *
     * @return true if at least one warning exists
     */
    public boolean hasWarnings() {
        return issues.stream()
            .anyMatch(issue -> issue.severity() == ValidationIssue.Severity.WARNING);
    }

    /**
     * Gets all ERROR-level issues.
     *
     * @return Immutable list of errors
     */
    public List<ValidationIssue> getErrors() {
        return issues.stream()
            .filter(issue -> issue.severity() == ValidationIssue.Severity.ERROR)
            .toList();
    }

    /**
     * Gets all WARNING-level issues.
     *
     * @return Immutable list of warnings
     */
    public List<ValidationIssue> getWarnings() {
        return issues.stream()
            .filter(issue -> issue.severity() == ValidationIssue.Severity.WARNING)
            .toList();
    }

    /**
     * Gets all issues regardless of severity.
     *
     * @return Immutable list of all issues
     */
    public List<ValidationIssue> getAllIssues() {
        return issues;
    }

    /**
     * Formats all errors as a human-readable string.
     * Used for exception messages.
     *
     * @return Formatted error message
     */
    public String formatErrors() {
        if (!hasErrors()) {
            return "No errors";
        }

        StringBuilder sb = new StringBuilder();
        List<ValidationIssue> errors = getErrors();
        sb.append(errors.size()).append(" error(s) found\n");

        int count = 1;
        for (ValidationIssue issue : errors) {
            sb.append(formatIssue(count++, issue));
        }
        return sb.toString();
    }

    private String formatIssue(int number, ValidationIssue issue) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(number).append(". [")
          .append(issue.code()).append("] ")
          .append(issue.message()).append("\n");

        if (issue.location() != null) {
            sb.append("     Location: ").append(issue.location()).append("\n");
        }
        if (issue.suggestedFix() != null) {
            sb.append("     Suggestion: ").append(issue.suggestedFix()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Converts this ValidationResult to a ModuleParsingException.
     * Factory method for creating exceptions from validation failures.
     *
     * @param contextMessage Context message describing what was being parsed
     * @return ModuleParsingException containing this result
     */
    // NOTE: Commented out temporarily because ModuleParsingException constructor doesn't exist yet
    // Will be uncommented in Task 4
    // public ModuleParsingException toException(String contextMessage) {
    //     return new ModuleParsingException(contextMessage, this);
    // }
}
