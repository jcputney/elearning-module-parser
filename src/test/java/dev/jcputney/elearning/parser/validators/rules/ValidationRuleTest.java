package dev.jcputney.elearning.parser.validators.rules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.Test;

/**
 * Tests for ValidationRule interface contract.
 * These tests use a simple mock implementation to verify expected behavior.
 */
class ValidationRuleTest {

  @Test
  void validate_withNullManifest_throwsIllegalArgumentException() {
    ValidationRule<String> rule = new MockValidationRule();

    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest");
  }

  @Test
  void validate_withValidManifest_returnsNonNull() {
    ValidationRule<String> rule = new MockValidationRule();

    ValidationResult result = rule.validate("test manifest");

    assertThat(result).isNotNull();
  }

  @Test
  void getRuleName_returnsNonEmpty() {
    ValidationRule<String> rule = new MockValidationRule();

    assertThat(rule.getRuleName()).isNotEmpty();
  }

  @Test
  void getSpecReference_returnsNonEmpty() {
    ValidationRule<String> rule = new MockValidationRule();

    assertThat(rule.getSpecReference()).isNotEmpty();
  }

  /**
   * Simple mock implementation for testing interface contract.
   */
  private static class MockValidationRule implements ValidationRule<String> {
    @Override
    public ValidationResult validate(String manifest) {
      if (manifest == null) {
        throw new IllegalArgumentException("manifest must not be null");
      }
      return ValidationResult.valid();
    }

    @Override
    public String getRuleName() {
      return "Mock Validation Rule";
    }

    @Override
    public String getSpecReference() {
      return "Test Spec";
    }
  }
}
