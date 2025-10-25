# Phase 5 Implementation Plan - cmi5 Rule-Based Validation

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans or superpowers:subagent-driven-development to implement this plan task-by-task.

**Goal:** Migrate Cmi5Validator from inline validation logic to rule-based architecture following the pattern established in Phases 2, 3, and 4.

**Architecture:** Extract 3 cmi5-specific validation rules and refactor Cmi5Validator to compose these rules using the stream/merge pattern. Like AICC, cmi5 has no common rules (no duplicate IDs, path security, or orphaned resources).

**Tech Stack:** Java 17, JUnit 5, AssertJ, ValidationRule<Cmi5Manifest> interface

---

## Background

### Current State

The Cmi5Validator has 3 inline validations (77 lines total):
1. Course required validation (lines 50-56)
2. Title required validation (lines 58-66)
3. Launch URL required validation (lines 68-77)

### Target State

Following SCORM 1.2, SCORM 2004, and AICC pattern:
- Each validation becomes a separate ValidationRule class
- Validator composes rules via stream/merge
- Each rule has 2-5 focused unit tests
- Integration test validates all rules work together

### Differences from Other Validators

- **Simpler:** Only 3 rules (same as AICC)
- **No common rules:** cmi5 doesn't have identifiers, resources, or file paths to validate
- **Similar to AICC:** ~13 total tests (vs 29 for SCORM 1.2, 14 for SCORM 2004, 13 for AICC)

---

## Tasks

### Task 1: Extract CourseRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/cmi5/CourseRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/cmi5/CourseRequiredRuleTest.java`

**Step 1: Write the failing test**

Create test file with 3 tests:

```java
package dev.jcputney.elearning.parser.validators.rules.cmi5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseRequiredRuleTest {

  private CourseRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new CourseRequiredRule();
  }

  @Test
  void validate_withValidCourse_returnsValid() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");
    manifest.setCourse(course);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullCourse_returnsError() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_COURSE");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("cmi5 manifest must contain course element");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
```

**Step 2: Run test to verify it fails**

```bash
./mvnw test -Dtest=CourseRequiredRuleTest
```

Expected output: `COMPILATION FAILURE` (CourseRequiredRule class doesn't exist)

**Step 3: Write minimal implementation**

Create rule class:

```java
package dev.jcputney.elearning.parser.validators.rules.cmi5;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a cmi5 manifest contains course element.
 *
 * <p>According to cmi5 specification, every cmi5 package must have a course element
 * that defines the basic course structure.</p>
 *
 * @see <a href="https://github.com/AICC/CMI-5_Spec_Current/blob/quartz/cmi5_spec.md">cmi5 Specification</a>
 */
public class CourseRequiredRule implements ValidationRule<Cmi5Manifest> {

  /**
   * Validates that the manifest contains course element.
   *
   * @param manifest The cmi5 manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(Cmi5Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getCourse() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "CMI5_MISSING_COURSE",
              "cmi5 manifest must contain course element",
              "cmi5.xml/course"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "CourseRequired";
  }

  @Override
  public String getSpecReference() {
    return "cmi5 Specification - Course Structure (cmi5.xml)";
  }
}
```

**Step 4: Run test to verify it passes**

```bash
./mvnw test -Dtest=CourseRequiredRuleTest
```

Expected output: `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0`

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/cmi5/CourseRequiredRule.java \
        src/test/java/dev/jcputney/elearning/parser/validators/rules/cmi5/CourseRequiredRuleTest.java
git commit -m "feat: extract CourseRequiredRule for cmi5

- Validates cmi5 manifest contains course element
- 3 tests: valid course, null course, null manifest
- Follows ValidationRule<Cmi5Manifest> pattern
- Consistent with SCORM and AICC rule extraction pattern

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

### Task 2: Extract TitleRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/cmi5/TitleRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/cmi5/TitleRequiredRuleTest.java`

**Step 1: Write the failing test**

Create test file with 5 tests:

```java
package dev.jcputney.elearning.parser.validators.rules.cmi5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.input.xapi.types.LangString;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleRequiredRuleTest {

  private TitleRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new TitleRequiredRule();
  }

  @Test
  void validate_withValidTitle_returnsValid() {
    Cmi5Manifest manifest = createManifestWithTitle("Test Course");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullTitle_returnsError() {
    Cmi5Manifest manifest = createManifestWithTitle(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_TITLE");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("cmi5 course must have a title");
  }

  @Test
  void validate_withEmptyTitle_returnsError() {
    Cmi5Manifest manifest = createManifestWithTitle("");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_TITLE");
  }

  @Test
  void validate_withNullCourse_returnsValid() {
    // Defer to CourseRequiredRule for null course validation
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }

  private Cmi5Manifest createManifestWithTitle(String titleValue) {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");

    if (titleValue != null) {
      LangString title = new LangString();
      title.setValue(titleValue);
      TextType titleType = new TextType();
      titleType.setStrings(Collections.singletonList(title));
      course.setTitle(titleType);
    }

    manifest.setCourse(course);
    return manifest;
  }
}
```

**Step 2: Run test to verify it fails**

```bash
./mvnw test -Dtest=TitleRequiredRuleTest
```

Expected output: `COMPILATION FAILURE`

**Step 3: Write minimal implementation**

```java
package dev.jcputney.elearning.parser.validators.rules.cmi5;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a cmi5 course has a title.
 *
 * <p>According to cmi5 specification, the title element in the course is required
 * and must not be empty.</p>
 *
 * <p>This rule defers validation when the course is null, as that is handled by
 * {@link CourseRequiredRule}.</p>
 *
 * @see <a href="https://github.com/AICC/CMI-5_Spec_Current/blob/quartz/cmi5_spec.md">cmi5 Specification</a>
 */
public class TitleRequiredRule implements ValidationRule<Cmi5Manifest> {

  /**
   * Validates that the course has a non-empty title.
   *
   * @param manifest The cmi5 manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(Cmi5Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    // Defer to CourseRequiredRule for null course
    if (manifest.getCourse() == null) {
      return ValidationResult.valid();
    }

    String title = manifest.getTitle();
    if (title == null || title.isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "CMI5_MISSING_TITLE",
              "cmi5 course must have a title",
              "cmi5.xml/course/title",
              "Add a <title> element to the course"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "TitleRequired";
  }

  @Override
  public String getSpecReference() {
    return "cmi5 Specification - Course Structure - title element";
  }
}
```

**Step 4: Run test to verify it passes**

```bash
./mvnw test -Dtest=TitleRequiredRuleTest
```

Expected output: `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/cmi5/TitleRequiredRule.java \
        src/test/java/dev/jcputney/elearning/parser/validators/rules/cmi5/TitleRequiredRuleTest.java
git commit -m "feat: extract TitleRequiredRule for cmi5

- Validates cmi5 course has a non-empty title
- 5 tests: valid, null, empty, null course (deferred), null manifest
- Defers to CourseRequiredRule for null course validation
- Follows ValidationRule<Cmi5Manifest> pattern

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

### Task 3: Extract LaunchUrlRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/cmi5/LaunchUrlRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/cmi5/LaunchUrlRequiredRuleTest.java`

**Step 1: Write the failing test**

Create test file with 4 tests:

```java
package dev.jcputney.elearning.parser.validators.rules.cmi5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LaunchUrlRequiredRuleTest {

  private LaunchUrlRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new LaunchUrlRequiredRule();
  }

  @Test
  void validate_withValidLaunchUrl_returnsValid() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    manifest.setCourse(course);

    AU au = new AU();
    au.setUrl("course.html");
    manifest.setAssignableUnits(Collections.singletonList(au));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullLaunchUrl_returnsError() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    manifest.setCourse(course);
    // No AUs means getLaunchUrl() returns null

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("cmi5 course must have at least one AU with a launch URL");
  }

  @Test
  void validate_withEmptyLaunchUrl_returnsError() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    manifest.setCourse(course);

    AU au = new AU();
    au.setUrl("   "); // Empty/whitespace
    manifest.setAssignableUnits(Collections.singletonList(au));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_LAUNCH_URL");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
```

**Step 2: Run test to verify it fails**

```bash
./mvnw test -Dtest=LaunchUrlRequiredRuleTest
```

Expected output: `COMPILATION FAILURE`

**Step 3: Write minimal implementation**

```java
package dev.jcputney.elearning.parser.validators.rules.cmi5;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a cmi5 course has at least one AU with a launch URL.
 *
 * <p>According to cmi5 specification, at least one assignable unit (AU) must have a url
 * attribute that serves as the entry point for the course.</p>
 *
 * @see <a href="https://github.com/AICC/CMI-5_Spec_Current/blob/quartz/cmi5_spec.md">cmi5 Specification</a>
 */
public class LaunchUrlRequiredRule implements ValidationRule<Cmi5Manifest> {

  /**
   * Validates that the course has at least one AU with a non-empty launch URL.
   *
   * @param manifest The cmi5 manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(Cmi5Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    String launchUrl = manifest.getLaunchUrl();
    if (launchUrl == null || launchUrl.trim().isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "CMI5_MISSING_LAUNCH_URL",
              "cmi5 course must have at least one AU with a launch URL",
              "cmi5.xml/course/au",
              "Ensure at least one AU has a url attribute"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "LaunchUrlRequired";
  }

  @Override
  public String getSpecReference() {
    return "cmi5 Specification - Assignable Unit (AU) - url attribute";
  }
}
```

**Step 4: Run test to verify it passes**

```bash
./mvnw test -Dtest=LaunchUrlRequiredRuleTest
```

Expected output: `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0`

**Step 5: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/cmi5/LaunchUrlRequiredRule.java \
        src/test/java/dev/jcputney/elearning/parser/validators/rules/cmi5/LaunchUrlRequiredRuleTest.java
git commit -m "feat: extract LaunchUrlRequiredRule for cmi5

- Validates cmi5 course has at least one AU with launch URL
- 4 tests: valid, null, empty, null manifest
- Follows ValidationRule<Cmi5Manifest> pattern
- Completes cmi5 rule extraction

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

### Task 4: Refactor Cmi5Validator to Use Rule Composition

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/validators/Cmi5Validator.java:38-77`
- Modify: `src/test/java/dev/jcputney/elearning/parser/validators/Cmi5ValidatorTest.java:35-187`

**Step 1: Add integration test for rule-based validation**

Add new test to Cmi5ValidatorTest:

```java
@Test
void validate_usesRuleBasedArchitecture() {
  // This test verifies the validator uses the rule-based architecture
  // by checking that multiple rules are applied
  Cmi5Manifest manifest = new Cmi5Manifest();
  Course course = new Course();
  course.setId("course1");
  // Course exists but has null title and no AUs (null launch URL)
  manifest.setCourse(course);

  ValidationResult result = validator.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.hasErrors()).isTrue();
  // Should have 2 errors (title, launch URL) - course exists but invalid fields
  assertThat(result.getErrors()).hasSize(2);
  assertThat(result.getErrors().stream()
      .map(issue -> issue.code())
      .toList())
      .containsExactlyInAnyOrder(
          "CMI5_MISSING_TITLE",
          "CMI5_MISSING_LAUNCH_URL"
      );
}
```

**Step 2: Run tests to see integration test fail**

```bash
./mvnw test -Dtest=Cmi5ValidatorTest
```

Expected output: Test fails because current validator doesn't report all 2 errors

**Step 3: Refactor Cmi5Validator to use rule composition**

Replace the validator implementation:

```java
package dev.jcputney.elearning.parser.validators;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.cmi5.CourseRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.cmi5.LaunchUrlRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.cmi5.TitleRequiredRule;
import java.util.Arrays;
import java.util.List;

/**
 * Validator for cmi5 manifests.
 * <p>
 * This validator checks that:
 * </p>
 * <ul>
 *   <li>Required course structure exists</li>
 *   <li>AUs have valid launch URLs</li>
 *   <li>Metadata is properly defined</li>
 * </ul>
 */
public class Cmi5Validator {

  private final List<ValidationRule<Cmi5Manifest>> rules;

  /**
   * Constructs a new Cmi5Validator with default validation rules.
   */
  public Cmi5Validator() {
    this.rules = Arrays.asList(
        new CourseRequiredRule(),
        new TitleRequiredRule(),
        new LaunchUrlRequiredRule()
    );
  }

  /**
   * Validates a cmi5 manifest for structural integrity.
   * Uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The cmi5 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Cmi5Manifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
```

**Step 4: Run all tests to verify they pass**

```bash
./mvnw test -Dtest=Cmi5ValidatorTest
```

Expected output: `Tests run: 7, Failures: 0, Errors: 0, Skipped: 0`

**Step 5: Run all validator and rule tests**

```bash
./mvnw test -Dtest="*cmi5*"
```

Expected output: All cmi5 tests passing (3 rule tests + 1 validator test = ~19 total tests)

**Step 6: Run full test suite**

```bash
./mvnw test
```

Expected output: All 937+ tests passing

**Step 7: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/Cmi5Validator.java \
        src/test/java/dev/jcputney/elearning/parser/validators/Cmi5ValidatorTest.java
git commit -m "refactor: migrate Cmi5Validator to rule-based architecture

- Replace inline validation with rule composition pattern
- Compose 3 cmi5-specific rules via stream/merge
- Add integration test for rule-based validation
- Reduce validator from 77 to ~68 lines (12% reduction)
- Consistent with SCORM 1.2, SCORM 2004, and AICC validators
- All 937+ tests passing

Benefits:
- Each rule independently testable
- Easy to add/remove rules
- Clear separation of concerns
- Improved maintainability

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

### Task 5: Create Phase 5 Completion Report

**Files:**
- Create: `docs/plans/2025-10-24-validation-phase5-COMPLETE.md`

**Step 1: Write completion report**

```markdown
# Phase 5 Completion Report - cmi5 Rule-Based Validation

**Date:** October 24, 2025
**Status:** ✅ COMPLETE
**All Tests:** PASSING (949+ tests)

## Executive Summary

Phase 5 successfully migrated Cmi5Validator from inline validation logic to rule-based architecture, following the pattern established in Phases 2, 3, and 4. The validator now composes 3 discrete cmi5-specific rules via the stream/merge pattern, improving testability, maintainability, and extensibility.

## Tasks Completed

### Rule Extraction Tasks (3 cmi5-specific rules)
1. ✅ **CourseRequiredRule** - Validates course element exists (3 tests)
2. ✅ **TitleRequiredRule** - Validates course title exists and not empty (5 tests)
3. ✅ **LaunchUrlRequiredRule** - Validates AU launch URL exists and not empty (4 tests)

### Refactoring Task (1)
4. ✅ **Cmi5Validator** - Now uses rule composition pattern (1 integration test)

## Metrics

### Code Changes
- **New Rule Classes:** 3
- **New Test Files:** 3 rule test classes
- **New Tests Added:** 12 rule tests + 1 integration test = 13 total
- **Lines Added:** ~360 (rules + tests)
- **Lines Removed:** ~35 (eliminated inline validation)
- **Net Change:** +325 lines
- **Commits:** 4 (3 rule extractions + 1 refactoring)

### Commit Details
1. feat: extract CourseRequiredRule for cmi5
2. feat: extract TitleRequiredRule for cmi5
3. feat: extract LaunchUrlRequiredRule for cmi5
4. refactor: migrate Cmi5Validator to rule-based architecture

### Test Results
- **Total Tests:** 949+ tests passing
- **Rule Tests:** 12 tests for cmi5-specific rules
- **Integration Test:** 1 new integration test
- **Validator Tests:** 7 tests for Cmi5Validator
- **No Regressions:** All existing tests maintained
- **Build Status:** ✅ BUILD SUCCESS

## Architecture Improvements

### Before Phase 5

```java
public class Cmi5Validator {
  public ValidationResult validate(Cmi5Manifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Inline validation 1: Course required
    if (manifest.getCourse() == null) {
      issues.add(...);
    }

    // Inline validation 2: Title required
    if (manifest.getTitle() == null || manifest.getTitle().isEmpty()) {
      issues.add(...);
    }

    // Inline validation 3: Launch URL required
    if (manifest.getLaunchUrl() == null || manifest.getLaunchUrl().trim().isEmpty()) {
      issues.add(...);
    }

    return ValidationResult.of(issues.toArray(...));
  }
}
```

**Issues:**
- All validation logic in one method
- Difficult to test individual rules in isolation
- Hard to extend with new validation rules
- Coupling between different validation concerns

### After Phase 5

```java
public class Cmi5Validator {
  private final List<ValidationRule<Cmi5Manifest>> rules;

  public Cmi5Validator() {
    this.rules = Arrays.asList(
        new CourseRequiredRule(),
        new TitleRequiredRule(),
        new LaunchUrlRequiredRule()
    );
  }

  public ValidationResult validate(Cmi5Manifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
```

**Improvements:**
- Clean composition of discrete rules
- Each rule independently testable
- Easy to add/remove rules
- Clear separation of concerns
- Consistent with SCORM and AICC validators

## Rules Implemented

### cmi5 Specific Rules

| Rule | Purpose | Error Code | Tests |
|------|---------|------------|-------|
| CourseRequiredRule | Validates manifest contains course element | CMI5_MISSING_COURSE | 3 |
| TitleRequiredRule | Validates course has non-empty title | CMI5_MISSING_TITLE | 5 |
| LaunchUrlRequiredRule | Validates course has AU with launch URL | CMI5_MISSING_LAUNCH_URL | 4 |

## Benefits Achieved

✅ **Consistency** - cmi5 validator now follows same pattern as SCORM and AICC validators
✅ **Testability** - Each rule independently testable with focused unit tests
✅ **Composability** - Rules can be composed differently for different use cases
✅ **Extensibility** - Easy to add new cmi5-specific rules
✅ **Maintainability** - Clear separation of concerns, easier to understand
✅ **Documentation** - Each rule documents its spec reference and purpose

## Validation Coverage

The cmi5 validator now validates:

1. ✅ Manifest contains course element (cmi5 Specification - Course Structure)
2. ✅ Course has a non-empty title (cmi5 Specification - title element)
3. ✅ Course has at least one AU with launch URL (cmi5 Specification - AU url attribute)

## Next Steps

### Phase 6: xAPI Validator (Final Phase)
- Extract xAPI-specific validation rules
- Apply same rule-based pattern
- Estimated: 3-5 rules, 10-15 tests
- Target: 1-2 days

### Future Enhancements
- Consider more cmi5-specific rules (AU metadata, moveOn criteria, etc.)
- Performance optimization for large manifests
- Parallel rule execution for independent rules

## Validation System Progress

| Standard | Status | Rules | Tests | Phase | Completion Date |
|----------|--------|-------|-------|-------|----------------|
| SCORM 1.2 | ✅ Complete | 10 | 29 | Phase 2 | Oct 23, 2025 |
| SCORM 2004 | ✅ Complete | 7 | 14 | Phase 3 | Oct 24, 2025 |
| AICC | ✅ Complete | 3 | 12 | Phase 4 | Oct 24, 2025 |
| cmi5 | ✅ Complete | 3 | 12 | Phase 5 | Oct 24, 2025 |
| xAPI | ⏳ Pending | - | - | Phase 6 | TBD |

**Overall Progress:** 4/5 validators complete (80%)

## Technical Learnings

### Pattern Refinements
- cmi5 follows same pattern as AICC (3 rules, simple structure)
- No common rules needed for cmi5 (no identifiers, resources, paths)
- Stream/merge pattern works consistently across all validators
- Each rule should have 3-5 focused tests

### Best Practices Maintained
- Rule classes: 50-80 lines (focused, single responsibility)
- Test classes: 3-5 tests per rule (comprehensive coverage)
- Error codes: Standard CMI5_ prefix with clear messages
- Spec references: Always include in rule documentation

## Conclusion

Phase 5 successfully achieved all objectives:

1. ✅ Extracted 3 cmi5-specific validation rules
2. ✅ Refactored Cmi5Validator to use rule composition
3. ✅ Maintained 100% test coverage (949+ tests passing)
4. ✅ Achieved consistency with SCORM and AICC validators
5. ✅ Improved code maintainability and extensibility

The validation system now has 4/5 validators using the rule-based pattern. cmi5 was straightforward due to its similarity to AICC.

**Ready for Phase 6: xAPI Validator (Final Phase)**

---

**Generated:** October 24, 2025
**By:** Claude Code with Jonathan Putney
**Branch:** main
**Build Status:** ✅ PASSING (949+ tests)
```

**Step 2: Save the completion report**

```bash
git add docs/plans/2025-10-24-validation-phase5-COMPLETE.md
git commit -m "docs: add Phase 5 completion report for cmi5 validation

- Documents all 4 tasks completed
- Metrics: 3 rules, 13 tests, 4 commits
- Architecture before/after comparison
- 80% overall progress (4/5 validators complete)
- Ready for Phase 6 (xAPI validator - final phase)

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Success Criteria

- ✅ All 3 cmi5 rules extracted with tests
- ✅ Cmi5Validator refactored to use rule composition
- ✅ All existing tests passing (no regressions)
- ✅ New tests added for each rule (12 total)
- ✅ Integration test validates rule composition
- ✅ 5 commits created (3 rules + 1 refactor + 1 doc)
- ✅ Completion report documents Phase 5 results
- ✅ Consistent with SCORM 1.2, 2004, and AICC pattern

## Testing Strategy

1. **Unit Tests:** Each rule has 3-5 focused tests
2. **Integration Test:** Validator test validates all rules work together
3. **Regression Tests:** All existing 937+ tests must pass
4. **Build Verification:** `./mvnw test` succeeds

## Notes

- cmi5 is similar to AICC (3 rules)
- No common rules apply to cmi5 (no identifiers, resources, or file paths)
- Validator reduction: 77 → 68 lines (~12% reduction)
- Follows TDD: red-green-refactor for each rule
- Frequent commits: one per task completed
