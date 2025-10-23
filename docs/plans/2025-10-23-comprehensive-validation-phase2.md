# Comprehensive Validation System - Phase 2 Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Complete SCORM 1.2 validation by extracting remaining validation logic to rules and implementing missing rules from spec

**Status:** Phase 1 complete (ValidationRule interface + 3 common rules integrated)

**Architecture:** Continue rule-based pattern established in Phase 1

---

## Phase 2 Overview

This plan completes SCORM 1.2 validation:
1. Extract existing validation logic from `Scorm12ResourceValidator.validateExisting()` to individual rules
2. Implement missing rules from spec document
3. Add comprehensive edge case tests
4. Remove `validateExisting()` method once all logic is extracted

**Rules to implement: 7 total**
- 4 rules from existing validator logic
- 3 new rules from spec

**Estimated tests: ~50-60 additional tests**

---

## Current State Analysis

### Already Implemented (Phase 1):
✅ Rule 11: Duplicate identifiers (DuplicateIdentifierRule)
✅ Rule 12: Path security (PathSecurityRule)
✅ Rule 14: Orphaned resources (OrphanedResourcesRule)

### Existing in validateExisting():
- Rule 2: Organizations element required
- Rule 4: Default organization validity
- Rule 7: Item identifierref must reference resource
- Rule 10: Resource must have href

### Missing from Spec:
- Rule 1: Manifest identifier required
- Rule 3: At least one organization required
- Rule 8: Resources element required
- Rule 13: At least one launchable resource required

---

## Task 1: Extract OrganizationsRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/OrganizationsRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/OrganizationsRequiredRuleTest.java`

**Extract logic from:** `Scorm12ResourceValidator.validateOrganizations()` lines 99-106

**Step 1: Write failing test**

```java
package dev.jcputney.elearning.parser.validators.rules.scorm12;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationsRequiredRuleTest {

  private OrganizationsRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new OrganizationsRequiredRule();
  }

  @Test
  void validate_withOrganizations_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setOrganizations(new Scorm12Organizations());

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withoutOrganizations_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setOrganizations(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_ORGANIZATIONS");
  }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=OrganizationsRequiredRuleTest`

**Step 3: Implement rule**

```java
package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that the manifest contains an organizations element.
 * Required by SCORM 1.2 CAM specification.
 */
public class OrganizationsRequiredRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Organizations Element Required";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.2";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getOrganizations() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM12_MISSING_ORGANIZATIONS",
              "Manifest must contain an <organizations> element",
              "manifest",
              "Add <organizations> element to manifest"
          )
      );
    }

    return ValidationResult.valid();
  }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=OrganizationsRequiredRuleTest`

**Step 5: Add to Scorm12ResourceValidator**

In `Scorm12ResourceValidator` constructor, add:
```java
import dev.jcputney.elearning.parser.validators.rules.scorm12.OrganizationsRequiredRule;

this.commonRules = Arrays.asList(
    new DuplicateIdentifierRule(),
    new PathSecurityRule(),
    new OrphanedResourcesRule(),
    new OrganizationsRequiredRule()  // Add this
);
```

**Step 6: Remove from validateExisting()**

Remove lines 99-106 from `validateOrganizations()` since rule now handles it.

**Step 7: Run all tests to verify**

Run: `./mvnw test -Dtest=Scorm12ResourceValidatorTest`

**Step 8: Commit**

```bash
git add .
git commit -m "feat: extract OrganizationsRequiredRule from validator

Extract organizations validation to dedicated rule:
- Checks manifest has organizations element
- 2 tests for valid/invalid cases
- Integrated into Scorm12ResourceValidator
- Removed logic from validateExisting()

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 2: Extract DefaultOrganizationValidRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/DefaultOrganizationValidRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/DefaultOrganizationValidRuleTest.java`

**Extract logic from:** `Scorm12ResourceValidator.validateOrganizations()` lines 109-122

**Implementation:** Follow TDD pattern from Task 1

Key test cases:
- Valid: Default references existing organization
- Valid: No default attribute
- Invalid: Default references non-existent organization
- Invalid: Default with empty value

**Commit after completion**

---

## Task 3: Extract ResourceReferenceValidRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ResourceReferenceValidRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ResourceReferenceValidRuleTest.java`

**Extract logic from:** `Scorm12ResourceValidator.validateItem()` lines 164-179

**Implementation:** Follow TDD pattern

Key test cases:
- Valid: Item identifierref matches resource
- Valid: Item without identifierref (container)
- Invalid: Identifierref references non-existent resource
- Invalid: Identifierref with empty value
- Edge: Nested items

**Commit after completion**

---

## Task 4: Extract ResourceHrefRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ResourceHrefRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ResourceHrefRequiredRuleTest.java`

**Extract logic from:** `Scorm12ResourceValidator.validateResourceHref()` lines 197-209

**Implementation:** Follow TDD pattern

Key test cases:
- Valid: Resource with non-empty href
- Invalid: Resource with null href
- Invalid: Resource with empty href
- Invalid: Resource with whitespace-only href

**Commit after completion**

---

## Task 5: Implement ManifestIdentifierRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ManifestIdentifierRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ManifestIdentifierRequiredRuleTest.java`

**Based on:** Rule 1 from spec document

**Implementation:** Follow TDD pattern

Key test cases:
- Valid: Manifest with non-empty identifier
- Invalid: Manifest without identifier
- Invalid: Manifest with empty identifier
- Invalid: Manifest with whitespace-only identifier

**Commit after completion**

---

## Task 6: Implement ResourcesRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ResourcesRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/ResourcesRequiredRuleTest.java`

**Based on:** Rule 8 from spec document

**Implementation:** Follow TDD pattern

Key test cases:
- Valid: Manifest with resources element
- Invalid: Manifest without resources element

**Commit after completion**

---

## Task 7: Implement LaunchableResourceRequiredRule

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/scorm12/LaunchableResourceRequiredRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/scorm12/LaunchableResourceRequiredRuleTest.java`

**Based on:** Rule 13 from spec document

**Implementation:** Follow TDD pattern

Validates that at least one resource has an href (launchable).

Key test cases:
- Valid: At least one resource with href
- Invalid: No resources with href
- Invalid: Empty resources list
- Edge: Multiple resources, only some with href

**Commit after completion**

---

## Task 8: Remove validateExisting() Method

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/validators/Scorm12ResourceValidator.java`

**Step 1: Verify all logic extracted**

Check that `validateExisting()` is now empty or only contains helper methods.

**Step 2: Remove validateExisting() and helper methods**

Remove:
- `validateExisting()` method
- `buildResourceIndex()` method (if not used by rules)
- `validateOrganizations()` method
- `validateOrganization()` method
- `validateItem()` method
- `validateResourceHref()` method

**Step 3: Simplify validate() method**

```java
public ValidationResult validate(Scorm12Manifest manifest) {
  return allRules.stream()
      .map(rule -> rule.validate(manifest))
      .reduce(ValidationResult.valid(), ValidationResult::merge);
}
```

**Step 4: Update constructor with all rules**

```java
public Scorm12ResourceValidator() {
  this.allRules = Arrays.asList(
      // Common rules
      new DuplicateIdentifierRule(),
      new PathSecurityRule(),
      new OrphanedResourcesRule(),
      // SCORM 1.2 specific rules
      new ManifestIdentifierRequiredRule(),
      new OrganizationsRequiredRule(),
      new DefaultOrganizationValidRule(),
      new ResourcesRequiredRule(),
      new ResourceReferenceValidRule(),
      new ResourceHrefRequiredRule(),
      new LaunchableResourceRequiredRule()
  );
}
```

**Step 5: Run all tests**

Run: `./mvnw test`
Expected: All tests pass

**Step 6: Commit**

```bash
git add .
git commit -m "refactor: complete rule-based validation for SCORM 1.2

Remove validateExisting() method - all logic now in rules:
- Simplified validate() to pure rule composition
- 10 total rules (3 common + 7 SCORM 1.2 specific)
- Clean separation of concerns
- All tests passing

Phase 2 complete: SCORM 1.2 validation fully rule-based.

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 9: Run Full Test Suite and Generate Report

**Step 1: Run all tests**

Run: `./mvnw test`
Expected: ~900+ tests passing

**Step 2: Generate coverage report**

Run: `./mvnw test jacoco:report`
Check: `target/site/jacoco/index.html`

**Step 3: Document results**

Create brief summary of:
- Total tests
- New rules added
- Coverage improvements

---

## Phase 2 Complete - Next Steps

At this point, you have:

✅ All SCORM 1.2 validation logic extracted to rules
✅ 10 total rules for SCORM 1.2 (3 common + 7 specific)
✅ ~60 new tests covering all rules
✅ Clean, composable validator architecture
✅ All existing tests still passing

**Remaining work for comprehensive validation:**

1. **Apply pattern to SCORM 2004** (~15-20 rules, more complex)
2. **Apply pattern to AICC** (~10-12 rules)
3. **Apply pattern to cmi5** (~8-10 rules)
4. **Apply pattern to xAPI** (~6-8 rules)
5. **Compliance testing** (real-world manifests)
6. **Performance testing** (stress tests)

**Estimated remaining effort:** 80-120 hours for all standards

**To continue:** Create Phase 3 plan for next standard (recommend SCORM 2004 as most complex)
