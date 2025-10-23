# Validation System Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Add comprehensive validation feedback system that collects all errors, provides structured error reporting, and supports strict/lenient parsing modes.

**Architecture:** Immutable ValidationResult objects composed from independent validators. ModuleParsingException enhanced with typed ValidationResult instead of generic metadata Map. Two-phase API: validate() returns issues, parse() throws enhanced exceptions in strict mode.

**Tech Stack:** Java 17 records, AssertJ for testing, Maven for builds

---

## Task 1: Create ValidationIssue and Severity

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validation/ValidationIssue.java`
- Test: `src/test/java/dev/jcputney/elearning/parser/validation/ValidationIssueTest.java`

**Step 1: Write the failing test**

Create test file:

```java
package dev.jcputney.elearning.parser.validation;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ValidationIssueTest {

    @Test
    void testCreateErrorIssue() {
        ValidationIssue issue = ValidationIssue.error(
            "TEST_CODE",
            "Test message",
            "test/location"
        );

        assertThat(issue.severity()).isEqualTo(ValidationIssue.Severity.ERROR);
        assertThat(issue.code()).isEqualTo("TEST_CODE");
        assertThat(issue.message()).isEqualTo("Test message");
        assertThat(issue.location()).isEqualTo("test/location");
        assertThat(issue.suggestedFix()).isNull();
    }

    @Test
    void testCreateWarningIssue() {
        ValidationIssue issue = ValidationIssue.warning(
            "WARN_CODE",
            "Warning message",
            "warn/location"
        );

        assertThat(issue.severity()).isEqualTo(ValidationIssue.Severity.WARNING);
    }

    @Test
    void testCreateIssueWithSuggestion() {
        ValidationIssue issue = new ValidationIssue(
            ValidationIssue.Severity.ERROR,
            "CODE",
            "message",
            "location",
            "Try this fix"
        );

        assertThat(issue.suggestedFix()).isEqualTo("Try this fix");
    }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=ValidationIssueTest`

Expected: Compilation failure - class ValidationIssue does not exist

**Step 3: Write minimal implementation**

Create implementation file:

```java
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
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=ValidationIssueTest`

Expected: All 3 tests PASS

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validation/ValidationIssue.java
git add src/test/java/dev/jcputney/elearning/parser/validation/ValidationIssueTest.java
git commit -m "feat: add ValidationIssue record with severity levels"
```

---

## Task 2: Create ValidationResult

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validation/ValidationResult.java`
- Test: `src/test/java/dev/jcputney/elearning/parser/validation/ValidationResultTest.java`

**Step 1: Write the failing test**

Create test file:

```java
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
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=ValidationResultTest`

Expected: Compilation failure - class ValidationResult does not exist

**Step 3: Write minimal implementation**

Create implementation file:

```java
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
    public ModuleParsingException toException(String contextMessage) {
        return new ModuleParsingException(contextMessage, this);
    }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=ValidationResultTest`

Expected: Compilation error - ModuleParsingException constructor doesn't exist yet. Comment out the toException() method for now.

Comment out toException() method and its test temporarily:

```java
// public ModuleParsingException toException(String contextMessage) {
//     return new ModuleParsingException(contextMessage, this);
// }
```

Run: `./mvnw test -Dtest=ValidationResultTest`

Expected: All tests PASS

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validation/ValidationResult.java
git add src/test/java/dev/jcputney/elearning/parser/validation/ValidationResultTest.java
git commit -m "feat: add ValidationResult with merge and formatting"
```

---

## Task 3: Remove Metadata from ModuleException

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/exception/ModuleException.java:36-138`
- Test: Run existing tests to ensure no breakage

**Step 1: Remove metadata fields and methods**

Edit `ModuleException.java`, replace lines 36-138 with:

```java
public sealed class ModuleException extends Exception permits FileAccessException,
    ManifestParseException, ModuleDetectionException, ModuleParsingException {

  /**
   * Constructs a new ModuleException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public ModuleException(String message) {
    super(message);
  }

  /**
   * Constructs a new ModuleException with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public ModuleException(String message, Throwable cause) {
    super(message, cause);
  }
}
```

**Step 2: Run tests to check for compilation errors**

Run: `./mvnw clean compile`

Expected: Compilation succeeds

**Step 3: Run all tests**

Run: `./mvnw test`

Expected: All tests still pass (no code was using metadata Map)

**Step 4: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/exception/ModuleException.java
git commit -m "refactor: remove metadata Map from ModuleException"
```

---

## Task 4: Update ModuleParsingException

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/exception/ModuleParsingException.java:44-84`
- Test: `src/test/java/dev/jcputney/elearning/parser/exception/ModuleParsingExceptionTest.java`

**Step 1: Write the failing test**

Create test file:

```java
package dev.jcputney.elearning.parser.exception;

import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ModuleParsingExceptionTest {

    @Test
    void testExceptionFromValidationResult() {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CODE1", "First error", "location1"),
            ValidationIssue.error("CODE2", "Second error", "location2")
        );

        ModuleParsingException ex = new ModuleParsingException("Parse failed", result);

        assertThat(ex.getMessage()).contains("Parse failed");
        assertThat(ex.getMessage()).contains("2 error(s) found");
        assertThat(ex.getMessage()).contains("CODE1");
        assertThat(ex.getMessage()).contains("CODE2");
        assertThat(ex.getValidationResult()).isEqualTo(result);
    }

    @Test
    void testExceptionViaToException() {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CODE", "Error message", "location")
        );

        ModuleParsingException ex = result.toException("Context message");

        assertThat(ex.getMessage()).contains("Context message");
        assertThat(ex.getValidationResult()).isEqualTo(result);
    }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=ModuleParsingExceptionTest`

Expected: Compilation failure - constructor doesn't match

**Step 3: Update ModuleParsingException implementation**

Replace lines 44-84 in `ModuleParsingException.java`:

```java
public final class ModuleParsingException extends ModuleException {

  private final ValidationResult validationResult;

  /**
   * Constructs a new ModuleParsingException with validation result.
   * Package-private constructor - should only be called from ValidationResult.toException().
   *
   * @param contextMessage Context describing what was being parsed
   * @param result ValidationResult containing all validation issues
   */
  ModuleParsingException(String contextMessage, ValidationResult result) {
    super(contextMessage + ":\n" + result.formatErrors());
    this.validationResult = result;
  }

  /**
   * Gets the validation result containing all issues found during parsing.
   *
   * @return ValidationResult with errors and warnings
   */
  public ValidationResult getValidationResult() {
    return validationResult;
  }
}
```

**Step 4: Uncomment toException() in ValidationResult**

In `ValidationResult.java`, uncomment:

```java
public ModuleParsingException toException(String contextMessage) {
    return new ModuleParsingException(contextMessage, this);
}
```

**Step 5: Run test to verify it passes**

Run: `./mvnw test -Dtest=ModuleParsingExceptionTest`

Expected: All tests PASS

**Step 6: Run all tests**

Run: `./mvnw test`

Expected: All tests PASS (breaking change is acceptable per user)

**Step 7: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/exception/ModuleParsingException.java
git add src/main/java/dev/jcputney/elearning/parser/validation/ValidationResult.java
git add src/test/java/dev/jcputney/elearning/parser/exception/ModuleParsingExceptionTest.java
git commit -m "feat: add ValidationResult to ModuleParsingException"
```

---

## Task 5: Create ParserOptions

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/api/ParserOptions.java`
- Test: `src/test/java/dev/jcputney/elearning/parser/api/ParserOptionsTest.java`

**Step 1: Write the failing test**

Create test file:

```java
package dev.jcputney.elearning.parser.api;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ParserOptionsTest {

    @Test
    void testDefaultIsStrict() {
        ParserOptions options = new ParserOptions();

        assertThat(options.isStrictMode()).isTrue();
    }

    @Test
    void testSetStrictMode() {
        ParserOptions options = new ParserOptions()
            .setStrictMode(false);

        assertThat(options.isStrictMode()).isFalse();
    }

    @Test
    void testStrictFactory() {
        ParserOptions options = ParserOptions.strict();

        assertThat(options.isStrictMode()).isTrue();
    }

    @Test
    void testLenientFactory() {
        ParserOptions options = ParserOptions.lenient();

        assertThat(options.isStrictMode()).isFalse();
    }

    @Test
    void testFluentChaining() {
        ParserOptions options = new ParserOptions()
            .setStrictMode(false)
            .setStrictMode(true);

        assertThat(options.isStrictMode()).isTrue();
    }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=ParserOptionsTest`

Expected: Compilation failure - class ParserOptions does not exist

**Step 3: Write minimal implementation**

Create implementation file (note: already exists as untracked file per git status, but we'll create the proper version):

```java
package dev.jcputney.elearning.parser.api;

/**
 * Configuration options for module parsing behavior.
 * Controls validation strictness and other parsing settings.
 */
public class ParserOptions {
    private boolean strictMode = true;

    /**
     * Creates parser options with default settings (strict mode enabled).
     */
    public ParserOptions() {}

    /**
     * Sets whether to use strict validation mode.
     * In strict mode, parsing fails on any validation errors.
     * In lenient mode, parsing continues despite errors.
     *
     * @param strict true for strict mode, false for lenient
     * @return this ParserOptions instance for method chaining
     */
    public ParserOptions setStrictMode(boolean strict) {
        this.strictMode = strict;
        return this;
    }

    /**
     * Checks if strict validation mode is enabled.
     *
     * @return true if strict mode is enabled
     */
    public boolean isStrictMode() {
        return strictMode;
    }

    /**
     * Creates parser options with strict mode enabled.
     * Parsing will fail on any validation errors.
     *
     * @return ParserOptions configured for strict mode
     */
    public static ParserOptions strict() {
        return new ParserOptions().setStrictMode(true);
    }

    /**
     * Creates parser options with lenient mode enabled.
     * Parsing will continue despite validation errors.
     *
     * @return ParserOptions configured for lenient mode
     */
    public static ParserOptions lenient() {
        return new ParserOptions().setStrictMode(false);
    }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=ParserOptionsTest`

Expected: All tests PASS

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/api/ParserOptions.java
git add src/test/java/dev/jcputney/elearning/parser/api/ParserOptionsTest.java
git commit -m "feat: add ParserOptions for strict/lenient mode"
```

---

## Task 6: Create Validator Interface and ValidationCodes

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validation/Validator.java`
- Create: `src/main/java/dev/jcputney/elearning/parser/validation/ValidationCodes.java`

**Step 1: Create Validator interface**

No test needed for simple interface. Create file directly:

```java
package dev.jcputney.elearning.parser.validation;

/**
 * Functional interface for validators that check module manifests for issues.
 * Validators should be stateless and return immutable ValidationResult objects.
 *
 * @param <T> The type of object being validated (e.g., ScormManifest, AiccCourse)
 */
@FunctionalInterface
public interface Validator<T> {
    /**
     * Validates the target object and returns any issues found.
     *
     * @param target The object to validate
     * @return ValidationResult containing any errors or warnings
     */
    ValidationResult validate(T target);
}
```

**Step 2: Create ValidationCodes constants**

Create file:

```java
package dev.jcputney.elearning.parser.validation;

/**
 * Standard validation error and warning codes.
 * Organized by eLearning standard.
 */
public final class ValidationCodes {

    // ===== SCORM 1.2 =====
    public static final String SCORM12_MISSING_RESOURCE_REF = "SCORM12_MISSING_RESOURCE_REF";
    public static final String SCORM12_MISSING_LAUNCH_URL = "SCORM12_MISSING_LAUNCH_URL";
    public static final String SCORM12_INVALID_SCORMTYPE = "SCORM12_INVALID_SCORMTYPE";
    public static final String SCORM12_INVALID_DEFAULT_ORG = "SCORM12_INVALID_DEFAULT_ORG";
    public static final String SCORM12_FILE_NOT_FOUND = "SCORM12_FILE_NOT_FOUND";
    public static final String SCORM12_MISSING_ORGANIZATIONS = "SCORM12_MISSING_ORGANIZATIONS";
    public static final String SCORM12_MISSING_RESOURCES = "SCORM12_MISSING_RESOURCES";
    public static final String SCORM12_INVALID_IDENTIFIER = "SCORM12_INVALID_IDENTIFIER";
    public static final String SCORM12_DUPLICATE_IDENTIFIER = "SCORM12_DUPLICATE_IDENTIFIER";
    public static final String SCORM12_MISSING_TITLE = "SCORM12_MISSING_TITLE";

    // ===== SCORM 2004 =====
    public static final String SCORM2004_INVALID_SEQUENCING = "SCORM2004_INVALID_SEQUENCING";
    public static final String SCORM2004_MISSING_RESOURCE_REF = "SCORM2004_MISSING_RESOURCE_REF";

    // ===== AICC =====
    public static final String AICC_INVALID_AU_REFERENCE = "AICC_INVALID_AU_REFERENCE";
    public static final String AICC_MISSING_COURSE_ID = "AICC_MISSING_COURSE_ID";

    // ===== cmi5 =====
    public static final String CMI5_MISSING_LAUNCH_METHOD = "CMI5_MISSING_LAUNCH_METHOD";
    public static final String CMI5_INVALID_LAUNCH_URL = "CMI5_INVALID_LAUNCH_URL";

    // ===== xAPI =====
    public static final String XAPI_INVALID_ACTIVITY_ID = "XAPI_INVALID_ACTIVITY_ID";
    public static final String XAPI_MISSING_LAUNCH_URL = "XAPI_MISSING_LAUNCH_URL";

    private ValidationCodes() {
        // Prevent instantiation
    }
}
```

**Step 3: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validation/Validator.java
git add src/main/java/dev/jcputney/elearning/parser/validation/ValidationCodes.java
git commit -m "feat: add Validator interface and ValidationCodes constants"
```

---

## Task 7: Update ModuleParser Interface

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/api/ModuleParser.java:38-47`

**Step 1: Add validate() method to interface**

Edit `ModuleParser.java`, replace lines 38-47:

```java
public interface ModuleParser<M extends PackageManifest> {

  /**
   * Validates the module without parsing.
   * Collects all validation issues and returns them without throwing exceptions.
   *
   * @return ValidationResult containing any errors or warnings found
   */
  ValidationResult validate();

  /**
   * Parses the module files and extracts metadata.
   * Runs validation first according to ParserOptions.
   * In strict mode, throws exception if validation errors found.
   * In lenient mode, continues parsing and attaches validation result to metadata.
   *
   * @return A ModuleMetadata object containing standardized metadata.
   * @throws ModuleParsingException if parsing fails or validation fails in strict mode.
   */
  ModuleMetadata<M> parse() throws ModuleParsingException;

  /**
   * Gets the parser options controlling validation behavior.
   *
   * @return ParserOptions for this parser
   */
  ParserOptions getOptions();
}
```

**Step 2: Add missing import**

Add import at top of file:

```java
import dev.jcputney.elearning.parser.validation.ValidationResult;
```

**Step 3: Run compilation**

Run: `./mvnw clean compile`

Expected: Compilation fails - implementations don't have these methods yet

This is expected. We'll fix implementations in next tasks.

**Step 4: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/api/ModuleParser.java
git commit -m "feat: add validate() and getOptions() to ModuleParser interface"
```

---

## Task 8: Create Scorm12ResourceValidator (Proof of Concept)

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validation/scorm12/Scorm12ResourceValidator.java`
- Test: `src/test/java/dev/jcputney/elearning/parser/validation/scorm12/Scorm12ResourceValidatorTest.java`

**Step 1: Write the failing test**

Create test file:

```java
package dev.jcputney.elearning.parser.validation.scorm12;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Organizations.Organization;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Organizations.Organization.Item;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Resources;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Resources.Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class Scorm12ResourceValidatorTest {

    @Test
    void testValidManifest() {
        Scorm2004Manifest manifest = createManifest();

        // Add matching resource
        Resource resource = new Resource();
        resource.setIdentifier("resource_1");
        resource.setHref("index.html");
        manifest.getResources().getResource().add(resource);

        // Add item referencing it
        Item item = new Item();
        item.setIdentifier("item_1");
        item.setIdentifierref("resource_1");
        manifest.getOrganizations().getOrganization().get(0).getItem().add(item);

        Scorm12ResourceValidator validator = new Scorm12ResourceValidator();
        ValidationResult result = validator.validate(manifest);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getAllIssues()).isEmpty();
    }

    @Test
    void testMissingResourceReference() {
        Scorm2004Manifest manifest = createManifest();

        // Add resource with different ID
        Resource resource = new Resource();
        resource.setIdentifier("SCO_ID1_RES");
        resource.setHref("index.html");
        manifest.getResources().getResource().add(resource);

        // Add item referencing non-existent resource
        Item item = new Item();
        item.setIdentifier("item_1");
        item.setIdentifierref("resource_1");
        manifest.getOrganizations().getOrganization().get(0).getItem().add(item);

        Scorm12ResourceValidator validator = new Scorm12ResourceValidator();
        ValidationResult result = validator.validate(manifest);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).hasSize(1);

        ValidationIssue error = result.getErrors().get(0);
        assertThat(error.code()).isEqualTo("SCORM12_MISSING_RESOURCE_REF");
        assertThat(error.message()).contains("resource_1");
        assertThat(error.message()).contains("no such resource exists");
        assertThat(error.location()).contains("item_1");
    }

    @Test
    void testMissingLaunchUrl() {
        Scorm2004Manifest manifest = createManifest();

        // Add resource without href
        Resource resource = new Resource();
        resource.setIdentifier("resource_1");
        resource.setScormType("sco");
        // Missing: resource.setHref(...)
        manifest.getResources().getResource().add(resource);

        Scorm12ResourceValidator validator = new Scorm12ResourceValidator();
        ValidationResult result = validator.validate(manifest);

        assertThat(result.hasErrors()).isTrue();
        ValidationIssue error = result.getErrors().get(0);
        assertThat(error.code()).isEqualTo("SCORM12_MISSING_LAUNCH_URL");
    }

    private Scorm2004Manifest createManifest() {
        Scorm2004Manifest manifest = new Scorm2004Manifest();

        Organizations orgs = new Organizations();
        Organization org = new Organization();
        org.setIdentifier("org_1");
        orgs.getOrganization().add(org);
        manifest.setOrganizations(orgs);

        Resources resources = new Resources();
        manifest.setResources(resources);

        return manifest;
    }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=Scorm12ResourceValidatorTest`

Expected: Compilation failure - class Scorm12ResourceValidator does not exist

**Step 3: Write minimal implementation**

Create implementation file:

```java
package dev.jcputney.elearning.parser.validation.scorm12;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Organizations.Organization.Item;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest.Resources.Resource;
import dev.jcputney.elearning.parser.validation.ValidationCodes;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates SCORM 1.2 manifest resource references and launch URLs.
 */
public class Scorm12ResourceValidator implements Validator<Scorm2004Manifest> {

    @Override
    public ValidationResult validate(Scorm2004Manifest manifest) {
        ValidationResult result = ValidationResult.valid();

        // Validate resource references
        List<Item> allItems = getAllItems(manifest);
        for (Item item : allItems) {
            if (item.getIdentifierref() != null && !item.getIdentifierref().isEmpty()) {
                result = result.merge(validateResourceReference(item, manifest));
            }
        }

        // Validate SCO resources have launch URLs
        for (Resource resource : manifest.getResources().getResource()) {
            if ("sco".equalsIgnoreCase(resource.getScormType())) {
                result = result.merge(validateLaunchUrl(resource));
            }
        }

        return result;
    }

    private List<Item> getAllItems(Scorm2004Manifest manifest) {
        return manifest.getOrganizations().getOrganization().stream()
            .flatMap(org -> org.getItem().stream())
            .collect(Collectors.toList());
    }

    private ValidationResult validateResourceReference(Item item, Scorm2004Manifest manifest) {
        String ref = item.getIdentifierref();
        boolean found = manifest.getResources().getResource().stream()
            .anyMatch(r -> ref.equals(r.getIdentifier()));

        if (!found) {
            List<String> availableIds = manifest.getResources().getResource().stream()
                .map(Resource::getIdentifier)
                .collect(Collectors.toList());

            return ValidationResult.of(
                new ValidationIssue(
                    ValidationIssue.Severity.ERROR,
                    ValidationCodes.SCORM12_MISSING_RESOURCE_REF,
                    "Item references resource '" + ref + "' but no such resource exists",
                    "item[@identifier='" + item.getIdentifier() + "']",
                    "Available resource IDs: " + String.join(", ", availableIds)
                )
            );
        }

        return ValidationResult.valid();
    }

    private ValidationResult validateLaunchUrl(Resource resource) {
        if (resource.getHref() == null || resource.getHref().isEmpty()) {
            return ValidationResult.of(
                ValidationIssue.error(
                    ValidationCodes.SCORM12_MISSING_LAUNCH_URL,
                    "SCO resource '" + resource.getIdentifier() + "' is missing launch URL (href)",
                    "resource[@identifier='" + resource.getIdentifier() + "']"
                )
            );
        }

        return ValidationResult.valid();
    }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=Scorm12ResourceValidatorTest`

Expected: All tests PASS

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validation/scorm12/Scorm12ResourceValidator.java
git add src/test/java/dev/jcputney/elearning/parser/validation/scorm12/Scorm12ResourceValidatorTest.java
git commit -m "feat: add Scorm12ResourceValidator with resource ref validation"
```

---

## Task 9: Update BaseParser with Options and Stub Methods

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/parsers/BaseParser.java`

**Step 1: Add ParserOptions field and stub methods**

Add to BaseParser class:

```java
protected ParserOptions options;

public BaseParser(FileAccess fileAccess, ParserOptions options) {
    this.fileAccess = fileAccess;
    this.options = options != null ? options : new ParserOptions();
}

@Override
public ParserOptions getOptions() {
    return options;
}

@Override
public ValidationResult validate() {
    // Default implementation - subclasses should override
    return ValidationResult.valid();
}
```

Update existing constructor to chain:

```java
protected BaseParser(FileAccess fileAccess) {
    this(fileAccess, new ParserOptions());
}
```

Add imports:

```java
import dev.jcputney.elearning.parser.api.ParserOptions;
import dev.jcputney.elearning.parser.validation.ValidationResult;
```

**Step 2: Run compilation**

Run: `./mvnw clean compile`

Expected: Compilation succeeds

**Step 3: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/parsers/BaseParser.java
git commit -m "feat: add ParserOptions support to BaseParser"
```

---

## Task 10: Update Scorm12Parser with Validation

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/parsers/Scorm12Parser.java`
- Test: Integration test with real manifest

**Step 1: Add validate() implementation**

Add to Scorm12Parser class:

```java
@Override
public ValidationResult validate() {
    try {
        Scorm2004Manifest manifest = parseManifest();
        return new Scorm12ResourceValidator().validate(manifest);
    } catch (Exception e) {
        // If we can't even parse manifest, return error
        return ValidationResult.of(
            ValidationIssue.error(
                "SCORM12_PARSE_FAILED",
                "Failed to parse manifest: " + e.getMessage(),
                "imsmanifest.xml"
            )
        );
    }
}
```

Add imports:

```java
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.scorm12.Scorm12ResourceValidator;
```

**Step 2: Update parse() to use validation**

Update the parse() method to validate first:

```java
@Override
public ModuleMetadata<Scorm2004Manifest> parse() throws ModuleParsingException {
    // Run validation first
    ValidationResult validationResult = validate();

    // In strict mode, fail on errors
    if (options.isStrictMode() && validationResult.hasErrors()) {
        throw validationResult.toException("Failed to parse SCORM 1.2 manifest");
    }

    // Continue with existing parsing logic
    Scorm2004Manifest manifest = parseManifest();
    // ... rest of existing parse code ...

    // Attach validation result to metadata for inspection
    ModuleMetadata<Scorm2004Manifest> metadata = createMetadata(manifest);
    // Note: We'll add setValidationResult() to ModuleMetadata in next task

    return metadata;
}
```

**Step 3: Run compilation**

Run: `./mvnw clean compile`

Expected: Compilation succeeds

**Step 4: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/parsers/Scorm12Parser.java
git commit -m "feat: add validation to Scorm12Parser"
```

---

## Task 11: Add ValidationResult to ModuleMetadata

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/output/ModuleMetadata.java`

**Step 1: Add field and methods**

Add to ModuleMetadata class:

```java
private ValidationResult validationResult;

/**
 * Gets the validation result containing any warnings or errors found.
 * Primarily used in lenient mode where parsing continues despite issues.
 *
 * @return ValidationResult, or null if validation was not performed
 */
public ValidationResult getValidationResult() {
    return validationResult;
}

/**
 * Sets the validation result for this metadata.
 * Package-private - only parsers should set this.
 *
 * @param validationResult The validation result to attach
 */
void setValidationResult(ValidationResult validationResult) {
    this.validationResult = validationResult;
}
```

Add import:

```java
import dev.jcputney.elearning.parser.validation.ValidationResult;
```

**Step 2: Update Scorm12Parser to set validation result**

In Scorm12Parser.parse(), after creating metadata:

```java
metadata.setValidationResult(validationResult);
```

**Step 3: Run compilation**

Run: `./mvnw clean compile`

Expected: Compilation succeeds

**Step 4: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/output/ModuleMetadata.java
git add src/main/java/dev/jcputney/elearning/parser/parsers/Scorm12Parser.java
git commit -m "feat: add ValidationResult to ModuleMetadata"
```

---

## Task 12: Write Integration Test for Validation System

**Files:**
- Create: `src/test/java/dev/jcputney/elearning/parser/validation/ValidationIntegrationTest.java`
- Create: `src/test/resources/modules/scorm12/invalid-resource-ref/imsmanifest.xml`

**Step 1: Create test manifest with validation error**

Create directory and file `src/test/resources/modules/scorm12/invalid-resource-ref/imsmanifest.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest identifier="test_manifest" version="1.0"
          xmlns="http://www.imsproject.org/xsd/imscp_rootv1p1p2"
          xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_rootv1p2">
    <metadata>
        <schema>ADL SCORM</schema>
        <schemaversion>1.2</schemaversion>
    </metadata>

    <organizations default="org_1">
        <organization identifier="org_1">
            <title>Test Course</title>
            <item identifier="item_1" identifierref="resource_1">
                <title>Test Item</title>
            </item>
        </organization>
    </organizations>

    <resources>
        <resource identifier="SCO_ID1_RES" type="webcontent" href="index.html" adlcp:scormtype="sco">
            <file href="index.html"/>
        </resource>
    </resources>
</manifest>
```

**Step 2: Write integration test**

Create test file:

```java
package dev.jcputney.elearning.parser.validation;

import dev.jcputney.elearning.parser.api.ModuleParser;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.api.ParserOptions;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.assertj.core.api.Assertions.*;

class ValidationIntegrationTest {

    private static final Path TEST_MANIFEST = Paths.get(
        "src/test/resources/modules/scorm12/invalid-resource-ref"
    );

    @Test
    void testValidateReturnsErrors() throws Exception {
        ModuleParserFactory factory = new ModuleParserFactory();
        ModuleParser<?> parser = factory.getParser(TEST_MANIFEST);

        ValidationResult result = parser.validate();

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_RESOURCE_REF");
    }

    @Test
    void testStrictModeThrowsException() throws Exception {
        ModuleParserFactory factory = new ModuleParserFactory();
        ModuleParser<?> parser = factory.getParser(TEST_MANIFEST, ParserOptions.strict());

        ModuleParsingException ex = assertThrows(
            ModuleParsingException.class,
            () -> parser.parse()
        );

        assertThat(ex.getMessage()).contains("SCORM12_MISSING_RESOURCE_REF");
        assertThat(ex.getMessage()).contains("resource_1");
        assertThat(ex.getValidationResult().hasErrors()).isTrue();
    }

    @Test
    void testLenientModeContinues() throws Exception {
        ModuleParserFactory factory = new ModuleParserFactory();
        ModuleParser<?> parser = factory.getParser(TEST_MANIFEST, ParserOptions.lenient());

        ModuleMetadata<?> metadata = parser.parse(); // Should not throw

        assertThat(metadata).isNotNull();
        assertThat(metadata.getValidationResult()).isNotNull();
        assertThat(metadata.getValidationResult().hasErrors()).isTrue();
    }
}
```

**Step 3: Update ModuleParserFactory to accept ParserOptions**

Modify `DefaultModuleParserFactory.java` to add:

```java
public ModuleParser<?> getParser(Path path, ParserOptions options) throws ModuleDetectionException {
    // ... existing detection logic ...
    // Pass options to parser constructor
}

// Keep existing method for backward compatibility
public ModuleParser<?> getParser(Path path) throws ModuleDetectionException {
    return getParser(path, new ParserOptions());
}
```

**Step 4: Run test**

Run: `./mvnw test -Dtest=ValidationIntegrationTest`

Expected: Tests may fail initially - fix parser factory and parsers to accept options

**Step 5: Fix compilation errors and rerun**

Run: `./mvnw test -Dtest=ValidationIntegrationTest`

Expected: All tests PASS

**Step 6: Commit**

```bash
git add src/test/resources/modules/scorm12/invalid-resource-ref/
git add src/test/java/dev/jcputney/elearning/parser/validation/ValidationIntegrationTest.java
git add src/main/java/dev/jcputney/elearning/parser/impl/factory/DefaultModuleParserFactory.java
git commit -m "test: add integration test for validation system"
```

---

## Task 13: Update Remaining Parser Implementations (Stub)

**Files:**
- Modify: All parser classes (Scorm2004Parser, AiccParser, Cmi5Parser, XapiParser)

**Step 1: Update each parser to satisfy interface**

For each parser (Scorm2004Parser, AiccParser, Cmi5Parser, XapiParser):

1. Add constructor accepting ParserOptions
2. Add validate() override that returns ValidationResult.valid() (stub for now)
3. Add validation call to parse() method

Example for Scorm2004Parser:

```java
@Override
public ValidationResult validate() {
    // TODO: Implement SCORM 2004 specific validation
    return ValidationResult.valid();
}

@Override
public ModuleMetadata<Scorm2004Manifest> parse() throws ModuleParsingException {
    ValidationResult validationResult = validate();

    if (options.isStrictMode() && validationResult.hasErrors()) {
        throw validationResult.toException("Failed to parse SCORM 2004 manifest");
    }

    // ... existing parse logic ...
}
```

**Step 2: Run all tests**

Run: `./mvnw test`

Expected: All tests PASS

**Step 3: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/parsers/
git commit -m "feat: add validation stubs to all parser implementations"
```

---

## Task 14: Run Full Test Suite and Fix Issues

**Step 1: Run all tests**

Run: `./mvnw test`

Expected: Identify any failing tests

**Step 2: Fix any failing tests**

Review failures and fix:
- Update tests that directly instantiate parsers to pass ParserOptions
- Fix any null pointer exceptions from missing options
- Update factory tests

**Step 3: Run tests again**

Run: `./mvnw test`

Expected: All tests PASS

**Step 4: Commit**

```bash
git add -A
git commit -m "fix: update tests for validation system changes"
```

---

## Task 15: Final Verification and Documentation

**Step 1: Run full build with coverage**

Run: `./mvnw clean test jacoco:report`

Expected: Build succeeds, coverage report generated

**Step 2: Review coverage report**

Open: `target/site/jacoco/index.html`

Check validation package has reasonable coverage (>80%)

**Step 3: Update CHANGELOG or release notes if needed**

Document breaking changes and new features

**Step 4: Final commit**

```bash
git add -A
git commit -m "docs: update documentation for validation system"
```

---

## Summary

This plan implements a comprehensive validation system with:

1. **Immutable ValidationResult** - Composable validation results
2. **Typed ValidationIssue** - Structured error information with location and suggestions
3. **Clean exception hierarchy** - Removed generic metadata Map
4. **Flexible API** - validate() for pre-flight checks, parse() for execution
5. **Strict/lenient modes** - User control over error handling
6. **Extensible validators** - Easy to add new validation rules per standard

**Next steps after completion:**
- Implement validators for other SCORM 1.2 rules
- Add SCORM 2004 specific validators
- Add AICC, cmi5, xAPI validators
- Consider adding file existence validation
- Add validation for referenced metadata files
