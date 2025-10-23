package dev.jcputney.elearning.parser.validation;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ValidationResultTest {

    @Test
    void testValidResult() {
        ValidationResult result = ValidationResult.valid();

        assertThat(result.isValid()).isTrue();
        assertThat(result.hasErrors()).isFalse();
        assertThat(result.hasWarnings()).isFalse();
        assertThat(result.getAllIssues()).isEmpty();
    }

    @Test
    void testResultWithErrors() {
        ValidationIssue error = ValidationIssue.error("CODE", "message", "location");
        ValidationResult result = ValidationResult.of(error);

        assertThat(result.isValid()).isFalse();
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().get(0)).isEqualTo(error);
    }

    @Test
    void testResultWithWarnings() {
        ValidationIssue warning = ValidationIssue.warning("WARN", "message", "location");
        ValidationResult result = ValidationResult.of(warning);

        assertThat(result.isValid()).isTrue();
        assertThat(result.hasErrors()).isFalse();
        assertThat(result.hasWarnings()).isTrue();
        assertThat(result.getWarnings()).hasSize(1);
    }

    @Test
    void testMergeResults() {
        ValidationResult result1 = ValidationResult.of(
            ValidationIssue.error("CODE1", "msg1", "loc1")
        );
        ValidationResult result2 = ValidationResult.of(
            ValidationIssue.error("CODE2", "msg2", "loc2")
        );

        ValidationResult merged = result1.merge(result2);

        assertThat(merged.getErrors()).hasSize(2);
        assertThat(merged.getErrors().get(0).code()).isEqualTo("CODE1");
        assertThat(merged.getErrors().get(1).code()).isEqualTo("CODE2");
    }

    @Test
    void testFormatErrors() {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CODE1", "First error", "location1"),
            ValidationIssue.error("CODE2", "Second error", "location2")
        );

        String formatted = result.formatErrors();

        assertThat(formatted).contains("2 error(s) found");
        assertThat(formatted).contains("[CODE1]");
        assertThat(formatted).contains("First error");
        assertThat(formatted).contains("location1");
        assertThat(formatted).contains("[CODE2]");
    }

    @Test
    void testFormatErrorsWithSuggestion() {
        ValidationIssue issue = new ValidationIssue(
            ValidationIssue.Severity.ERROR,
            "CODE",
            "Error message",
            "location",
            "Try this fix"
        );
        ValidationResult result = ValidationResult.of(issue);

        String formatted = result.formatErrors();

        assertThat(formatted).contains("Suggestion: Try this fix");
    }
}
