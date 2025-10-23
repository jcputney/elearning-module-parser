# Comprehensive Validation System - Phase 1 Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement rule-based validation architecture with common rules and comprehensive SCORM 1.2 validation as proof-of-concept

**Architecture:** Extract validation logic into discrete ValidationRule classes that validators compose. Each rule encapsulates one validation concern with spec traceability. Validators execute all rules and merge results.

**Tech Stack:** Java 17, JUnit 5, AssertJ, Jackson XML

---

## Phase 1 Overview

This plan implements the foundation:
1. ValidationRule interface
2. Common rules (duplicate IDs, path security, orphaned resources)
3. Enhanced Scorm12ResourceValidator with comprehensive rules
4. Extensive test coverage (~200+ tests)

Later phases will expand to SCORM 2004, AICC, cmi5, and xAPI.

---

## Task 1: Create ValidationRule Interface

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/ValidationRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/ValidationRuleTest.java`

**Step 1: Write the interface**

Create the core ValidationRule interface:

```java
/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.validators.rules;

import dev.jcputney.elearning.parser.validation.ValidationResult;

/**
 * Interface for individual validation rules that can be composed together.
 * Each rule encapsulates a single validation concern with spec traceability.
 *
 * <p>Rules are designed to be:</p>
 * <ul>
 *   <li>Testable - each rule can be tested independently</li>
 *   <li>Reusable - common rules can be shared across validators</li>
 *   <li>Traceable - each rule references the specification it enforces</li>
 *   <li>Composable - rules are combined in validators via merge()</li>
 * </ul>
 *
 * @param <T> The manifest type this rule validates
 */
public interface ValidationRule<T> {
  /**
   * Validates the manifest according to this rule.
   *
   * @param manifest The manifest to validate (must not be null)
   * @return ValidationResult with any issues found (never null)
   * @throws IllegalArgumentException if manifest is null
   */
  ValidationResult validate(T manifest);

  /**
   * Human-readable name of this rule for logging and debugging.
   *
   * @return Rule name (e.g., "Resource Reference Validation")
   */
  String getRuleName();

  /**
   * Specification reference for traceability and documentation.
   * Used to trace validation back to spec requirements.
   *
   * @return Spec reference (e.g., "SCORM 1.2 CAM Section 2.3.4")
   */
  String getSpecReference();
}
```

**Step 2: Write test for interface contract**

Create basic test to document expected behavior:

```java
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
```

**Step 3: Run tests to verify they pass**

Run: `./mvnw test -Dtest=ValidationRuleTest`
Expected: All 4 tests PASS

**Step 4: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/ValidationRule.java
git add src/test/java/dev/jcputney/elearning/parser/validators/rules/ValidationRuleTest.java
git commit -m "feat: add ValidationRule interface for composable validation

Add core interface for rule-based validation:
- Generic interface for type-safe rules
- Spec traceability with getSpecReference()
- Contract tests for null handling
- Foundation for rule-based validator architecture

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 2: Fetch SCORM 1.2 Specification

**Files:**
- Create: `docs/specs/scorm-12-validation-rules.md`

**Step 1: Fetch SCORM 1.2 CAM specification**

Use WebSearch to find and WebFetch to retrieve the SCORM 1.2 Content Aggregation Model specification.

Search query: "SCORM 1.2 Content Aggregation Model specification CAM PDF"

**Step 2: Extract MUST requirements**

Read through the CAM specification and extract all MUST requirements related to:
- Manifest structure (`<manifest>`, `<organizations>`, `<resources>`)
- Identifiers (uniqueness, format)
- References (identifierref, default organization)
- Resource elements (href, type, identifier)
- Organization structure (items, hierarchies)

**Step 3: Document validation rules**

For each MUST requirement, document using this template:

```markdown
## Rule: [Rule Name]
**Spec Reference:** SCORM 1.2 CAM [Section]
**Requirement Level:** MUST
**Category:** [Structural | Referential | Uniqueness | Data | Cardinality]

**Description:**
[What must be validated]

**Error Code:** `SCORM12_[CODE]`
**Error Message:** `"[Template]"`
**Suggested Fix:** `"[Template]"`

**Test Cases:**
- Valid case: [description]
- Invalid case: [description]
- Edge cases: [list]
```

**Step 4: Create comprehensive rule list**

Expected rules to document:
1. Manifest identifier required and unique
2. Organizations element required
3. At least one organization required
4. Default organization must reference existing organization
5. Organization identifier required and unique
6. Item identifiers unique within organization
7. Item identifierref must reference existing resource
8. Resources element required
9. Resource identifier required and unique
10. Resource with type="webcontent" must have href
11. No duplicate identifiers across manifest
12. Resource href paths must be safe (no ../, no absolute paths)
13. At least one launchable resource required
14. Orphaned resources should be flagged (SHOULD requirement)

**Step 5: Commit documentation**

```bash
git add docs/specs/scorm-12-validation-rules.md
git commit -m "docs: extract SCORM 1.2 validation rules from spec

Document 14 validation rules extracted from SCORM 1.2 CAM:
- 12 MUST requirements (errors)
- 2 SHOULD requirements (warnings)
- Includes spec references, error codes, test cases

Foundation for implementing comprehensive SCORM 1.2 validation.

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 3: Implement DuplicateIdentifierRule (Common Rule)

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/common/DuplicateIdentifierRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/common/DuplicateIdentifierRuleTest.java`

**Step 1: Write failing test for valid manifest (no duplicates)**

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DuplicateIdentifierRuleTest {

  private DuplicateIdentifierRule rule;

  @BeforeEach
  void setUp() {
    rule = new DuplicateIdentifierRule();
  }

  @Test
  void validate_withUniqueIdentifiers_returnsValid() {
    // Create manifest with unique identifiers
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("manifest1");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=DuplicateIdentifierRuleTest`
Expected: FAIL - DuplicateIdentifierRule class not found

**Step 3: Write minimal implementation**

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates that all identifiers within the manifest are unique.
 * Duplicate identifiers cause unpredictable behavior in SCORM players.
 *
 * <p>Spec References:</p>
 * <ul>
 *   <li>SCORM 1.2 CAM Section 2.3.1: "identifier must be unique"</li>
 *   <li>SCORM 2004 CAM Section 2.3.1: "identifier must be unique within scope"</li>
 * </ul>
 */
public class DuplicateIdentifierRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Duplicate Identifier Validation";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.1";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();
    Map<String, List<String>> identifierLocations = new HashMap<>();

    // Collect manifest identifier
    if (manifest.getIdentifier() != null) {
      identifierLocations.computeIfAbsent(manifest.getIdentifier(), k -> new ArrayList<>())
          .add("manifest/@identifier");
    }

    // Collect organization identifiers
    if (manifest.getOrganizations() != null && manifest.getOrganizations().getOrganizationList() != null) {
      for (Scorm12Organization org : manifest.getOrganizations().getOrganizationList()) {
        if (org.getIdentifier() != null) {
          identifierLocations.computeIfAbsent(org.getIdentifier(), k -> new ArrayList<>())
              .add("organizations/organization[@identifier='" + org.getIdentifier() + "']");
        }
      }
    }

    // Collect resource identifiers
    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm12Resource resource : manifest.getResources().getResourceList()) {
        if (resource.getIdentifier() != null) {
          identifierLocations.computeIfAbsent(resource.getIdentifier(), k -> new ArrayList<>())
              .add("resources/resource[@identifier='" + resource.getIdentifier() + "']");
        }
      }
    }

    // Find duplicates
    for (Map.Entry<String, List<String>> entry : identifierLocations.entrySet()) {
      if (entry.getValue().size() > 1) {
        issues.add(ValidationIssue.error(
            "DUPLICATE_IDENTIFIER",
            String.format("Identifier '%s' is used %d times but must be unique",
                entry.getKey(), entry.getValue().size()),
            String.join(", ", entry.getValue()),
            String.format("Rename duplicate identifiers to be unique. Locations: %s",
                String.join(", ", entry.getValue()))
        ));
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=DuplicateIdentifierRuleTest::validate_withUniqueIdentifiers_returnsValid`
Expected: PASS

**Step 5: Write test for duplicate identifiers**

Add to DuplicateIdentifierRuleTest.java:

```java
@Test
void validate_withDuplicateOrgIdentifiers_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  manifest.setIdentifier("manifest1");

  Scorm12Organizations organizations = new Scorm12Organizations();
  Scorm12Organization org1 = new Scorm12Organization();
  org1.setIdentifier("duplicate_id");
  Scorm12Organization org2 = new Scorm12Organization();
  org2.setIdentifier("duplicate_id"); // Same ID!
  organizations.setOrganizationList(List.of(org1, org2));
  manifest.setOrganizations(organizations);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.hasErrors()).isTrue();
  assertThat(result.getErrors()).hasSize(1);
  assertThat(result.getErrors().get(0).code()).isEqualTo("DUPLICATE_IDENTIFIER");
  assertThat(result.getErrors().get(0).message()).contains("duplicate_id");
  assertThat(result.getErrors().get(0).message()).contains("2 times");
}

@Test
void validate_withDuplicateResourceIdentifiers_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();

  Scorm12Resources resources = new Scorm12Resources();
  Scorm12Resource res1 = new Scorm12Resource();
  res1.setIdentifier("res_duplicate");
  Scorm12Resource res2 = new Scorm12Resource();
  res2.setIdentifier("res_duplicate"); // Same ID!
  resources.setResourceList(List.of(res1, res2));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors()).hasSize(1);
  assertThat(result.getErrors().get(0).code()).isEqualTo("DUPLICATE_IDENTIFIER");
}

@Test
void validate_withManifestIdMatchingOrgId_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  manifest.setIdentifier("shared_id");

  Scorm12Organizations organizations = new Scorm12Organizations();
  Scorm12Organization org = new Scorm12Organization();
  org.setIdentifier("shared_id"); // Same as manifest!
  organizations.setOrganizationList(Collections.singletonList(org));
  manifest.setOrganizations(organizations);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors()).hasSize(1);
  assertThat(result.getErrors().get(0).message()).contains("shared_id");
}
```

**Step 6: Run all tests to verify they pass**

Run: `./mvnw test -Dtest=DuplicateIdentifierRuleTest`
Expected: All 4 tests PASS

**Step 7: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/common/DuplicateIdentifierRule.java
git add src/test/java/dev/jcputney/elearning/parser/validators/rules/common/DuplicateIdentifierRuleTest.java
git commit -m "feat: add DuplicateIdentifierRule for SCORM validation

Implement common validation rule to detect duplicate identifiers:
- Checks manifest, organization, and resource IDs
- Returns detailed error with all duplicate locations
- 4 comprehensive tests covering all scenarios
- First rule in rule-based validation architecture

Closes gap in original validator that missed duplicate IDs.

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 4: Implement PathSecurityRule (Common Rule)

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/common/PathSecurityRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/common/PathSecurityRuleTest.java`

**Step 1: Write failing test for valid paths**

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathSecurityRuleTest {

  private PathSecurityRule rule;

  @BeforeEach
  void setUp() {
    rule = new PathSecurityRule();
  }

  @Test
  void validate_withSafePaths_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("content/page.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=PathSecurityRuleTest`
Expected: FAIL - PathSecurityRule not found

**Step 3: Write minimal implementation**

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12File;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validates that all file paths in the manifest are safe and don't contain
 * directory traversal patterns, absolute paths, or external references.
 *
 * <p>Security Requirements:</p>
 * <ul>
 *   <li>No path traversal (../ or ..\)</li>
 *   <li>No absolute paths (/path or C:\path)</li>
 *   <li>No external URLs (http://, https://, //)</li>
 *   <li>No null bytes or control characters</li>
 * </ul>
 */
public class PathSecurityRule implements ValidationRule<Scorm12Manifest> {

  private static final Pattern PATH_TRAVERSAL = Pattern.compile("\\.\\./|\\.\\\\");
  private static final Pattern ABSOLUTE_PATH = Pattern.compile("^[/\\\\]|^[a-zA-Z]:");
  private static final Pattern EXTERNAL_URL = Pattern.compile("^(https?:)?//");
  private static final Pattern NULL_BYTE = Pattern.compile("\\x00");

  @Override
  public String getRuleName() {
    return "Path Security Validation";
  }

  @Override
  public String getSpecReference() {
    return "Security Best Practice";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm12Resource resource : manifest.getResources().getResourceList()) {
        // Check resource href
        if (resource.getHref() != null) {
          validatePath(resource.getHref(),
              "resources/resource[@identifier='" + resource.getIdentifier() + "']/@href",
              issues);
        }

        // Check file hrefs
        if (resource.getFiles() != null) {
          for (Scorm12File file : resource.getFiles()) {
            if (file.getHref() != null) {
              validatePath(file.getHref(),
                  "resources/resource[@identifier='" + resource.getIdentifier() + "']/file/@href",
                  issues);
            }
          }
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  private void validatePath(String path, String location, List<ValidationIssue> issues) {
    if (PATH_TRAVERSAL.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_PATH_TRAVERSAL",
          String.format("Path contains directory traversal pattern: '%s'", path),
          location,
          "Remove '../' or '..\' from the path. All content should be within the package."
      ));
    } else if (ABSOLUTE_PATH.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_ABSOLUTE_PATH",
          String.format("Path is absolute but should be relative: '%s'", path),
          location,
          "Use relative paths only. Remove leading '/' or drive letter."
      ));
    } else if (EXTERNAL_URL.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_EXTERNAL_URL",
          String.format("Path references external URL: '%s'", path),
          location,
          "All resources must be packaged within the content. Remove external URL."
      ));
    } else if (NULL_BYTE.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_NULL_BYTE",
          String.format("Path contains null byte: '%s'", path),
          location,
          "Remove null bytes from path."
      ));
    }
  }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=PathSecurityRuleTest::validate_withSafePaths_returnsValid`
Expected: PASS

**Step 5: Write tests for unsafe paths**

Add to PathSecurityRuleTest.java:

```java
@Test
void validate_withPathTraversal_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  Scorm12Resources resources = new Scorm12Resources();

  Scorm12Resource resource = new Scorm12Resource();
  resource.setIdentifier("res1");
  resource.setHref("../../../etc/passwd");
  resources.setResourceList(Collections.singletonList(resource));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors()).hasSize(1);
  assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_PATH_TRAVERSAL");
  assertThat(result.getErrors().get(0).message()).contains("../../../etc/passwd");
}

@Test
void validate_withAbsolutePath_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  Scorm12Resources resources = new Scorm12Resources();

  Scorm12Resource resource = new Scorm12Resource();
  resource.setIdentifier("res1");
  resource.setHref("/usr/local/content.html");
  resources.setResourceList(Collections.singletonList(resource));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors()).hasSize(1);
  assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_ABSOLUTE_PATH");
}

@Test
void validate_withExternalUrl_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  Scorm12Resources resources = new Scorm12Resources();

  Scorm12Resource resource = new Scorm12Resource();
  resource.setIdentifier("res1");
  resource.setHref("http://evil.com/malware.js");
  resources.setResourceList(Collections.singletonList(resource));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors()).hasSize(1);
  assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_EXTERNAL_URL");
}

@Test
void validate_withWindowsAbsolutePath_returnsError() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  Scorm12Resources resources = new Scorm12Resources();

  Scorm12Resource resource = new Scorm12Resource();
  resource.setIdentifier("res1");
  resource.setHref("C:\\Windows\\system32\\evil.exe");
  resources.setResourceList(Collections.singletonList(resource));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_ABSOLUTE_PATH");
}

@Test
void validate_withMultipleUnsafePaths_returnsMultipleErrors() {
  Scorm12Manifest manifest = new Scorm12Manifest();
  Scorm12Resources resources = new Scorm12Resources();

  Scorm12Resource res1 = new Scorm12Resource();
  res1.setIdentifier("res1");
  res1.setHref("../traversal.html");

  Scorm12Resource res2 = new Scorm12Resource();
  res2.setIdentifier("res2");
  res2.setHref("http://external.com/bad.js");

  resources.setResourceList(List.of(res1, res2));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.getErrors()).hasSize(2);
}
```

**Step 6: Run all tests**

Run: `./mvnw test -Dtest=PathSecurityRuleTest`
Expected: All 7 tests PASS

**Step 7: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/common/PathSecurityRule.java
git add src/test/java/dev/jcputney/elearning/parser/validators/rules/common/PathSecurityRuleTest.java
git commit -m "feat: add PathSecurityRule to prevent path traversal attacks

Implement security validation for file paths in manifests:
- Detects path traversal (../ and ..\)
- Blocks absolute paths (/ and C:\)
- Prevents external URLs (http://, https://)
- Validates both resource href and file elements
- 7 comprehensive tests including edge cases

Critical security rule to prevent malicious packages.

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 5: Implement OrphanedResourcesRule (Common Rule)

**Files:**
- Create: `src/main/java/dev/jcputney/elearning/parser/validators/rules/common/OrphanedResourcesRule.java`
- Create: `src/test/java/dev/jcputney/elearning/parser/validators/rules/common/OrphanedResourcesRuleTest.java`

**Step 1: Write failing test**

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrphanedResourcesRuleTest {

  private OrphanedResourcesRule rule;

  @BeforeEach
  void setUp() {
    rule = new OrphanedResourcesRule();
  }

  @Test
  void validate_withAllResourcesReferenced_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item referencing resource
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create resource
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("content.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=OrphanedResourcesRuleTest`
Expected: FAIL - OrphanedResourcesRule not found

**Step 3: Write implementation**

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates that all resources are referenced by at least one item.
 * Orphaned resources waste space and may indicate errors in the manifest.
 *
 * <p>This is a SHOULD requirement (warning, not error) but helps identify
 * potential issues in content packages.</p>
 */
public class OrphanedResourcesRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Orphaned Resources Detection";
  }

  @Override
  public String getSpecReference() {
    return "Best Practice";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    // Collect all referenced resource IDs
    Set<String> referencedResourceIds = new HashSet<>();
    if (manifest.getOrganizations() != null && manifest.getOrganizations().getOrganizationList() != null) {
      for (Scorm12Organization org : manifest.getOrganizations().getOrganizationList()) {
        if (org.getItems() != null) {
          collectReferencedResources(org.getItems(), referencedResourceIds);
        }
      }
    }

    // Check for orphaned resources
    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm12Resource resource : manifest.getResources().getResourceList()) {
        if (resource.getIdentifier() != null && !referencedResourceIds.contains(resource.getIdentifier())) {
          issues.add(ValidationIssue.warning(
              "ORPHANED_RESOURCE",
              String.format("Resource '%s' is not referenced by any item", resource.getIdentifier()),
              "resources/resource[@identifier='" + resource.getIdentifier() + "']",
              "Either reference this resource from an item or remove it to reduce package size"
          ));
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  private void collectReferencedResources(List<Scorm12Item> items, Set<String> referencedResourceIds) {
    for (Scorm12Item item : items) {
      if (item.getIdentifierRef() != null) {
        referencedResourceIds.add(item.getIdentifierRef());
      }
      if (item.getItems() != null) {
        collectReferencedResources(item.getItems(), referencedResourceIds);
      }
    }
  }
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=OrphanedResourcesRuleTest::validate_withAllResourcesReferenced_returnsValid`
Expected: PASS

**Step 5: Add test for orphaned resource**

Add to OrphanedResourcesRuleTest.java:

```java
@Test
void validate_withOrphanedResource_returnsWarning() {
  Scorm12Manifest manifest = new Scorm12Manifest();

  // Create organization with item referencing one resource
  Scorm12Organizations organizations = new Scorm12Organizations();
  Scorm12Organization org = new Scorm12Organization();
  org.setIdentifier("org1");
  Scorm12Item item = new Scorm12Item();
  item.setIdentifier("item1");
  item.setIdentifierRef("res1");
  org.setItems(Collections.singletonList(item));
  organizations.setOrganizationList(Collections.singletonList(org));
  manifest.setOrganizations(organizations);

  // Create two resources, only one referenced
  Scorm12Resources resources = new Scorm12Resources();
  Scorm12Resource res1 = new Scorm12Resource();
  res1.setIdentifier("res1");
  res1.setHref("content.html");

  Scorm12Resource res2 = new Scorm12Resource();
  res2.setIdentifier("orphaned_resource");
  res2.setHref("unused.html");

  resources.setResourceList(List.of(res1, res2));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.isValid()).isTrue(); // Warnings don't invalidate
  assertThat(result.hasWarnings()).isTrue();
  assertThat(result.getWarnings()).hasSize(1);
  assertThat(result.getWarnings().get(0).code()).isEqualTo("ORPHANED_RESOURCE");
  assertThat(result.getWarnings().get(0).message()).contains("orphaned_resource");
}

@Test
void validate_withNestedItemReferences_detectsOrphans() {
  Scorm12Manifest manifest = new Scorm12Manifest();

  // Create nested items
  Scorm12Organizations organizations = new Scorm12Organizations();
  Scorm12Organization org = new Scorm12Organization();
  org.setIdentifier("org1");

  Scorm12Item parentItem = new Scorm12Item();
  parentItem.setIdentifier("parent");
  parentItem.setIdentifierRef("res1");

  Scorm12Item childItem = new Scorm12Item();
  childItem.setIdentifier("child");
  childItem.setIdentifierRef("res2");

  parentItem.setItems(Collections.singletonList(childItem));
  org.setItems(Collections.singletonList(parentItem));
  organizations.setOrganizationList(Collections.singletonList(org));
  manifest.setOrganizations(organizations);

  // Create resources: 2 referenced, 1 orphaned
  Scorm12Resources resources = new Scorm12Resources();
  Scorm12Resource res1 = new Scorm12Resource();
  res1.setIdentifier("res1");
  Scorm12Resource res2 = new Scorm12Resource();
  res2.setIdentifier("res2");
  Scorm12Resource res3 = new Scorm12Resource();
  res3.setIdentifier("orphan");

  resources.setResourceList(List.of(res1, res2, res3));
  manifest.setResources(resources);

  ValidationResult result = rule.validate(manifest);

  assertThat(result.hasWarnings()).isTrue();
  assertThat(result.getWarnings()).hasSize(1);
  assertThat(result.getWarnings().get(0).message()).contains("orphan");
}
```

**Step 6: Run all tests**

Run: `./mvnw test -Dtest=OrphanedResourcesRuleTest`
Expected: All 3 tests PASS

**Step 7: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/rules/common/OrphanedResourcesRule.java
git add src/test/java/dev/jcputney/elearning/parser/validators/rules/common/OrphanedResourcesRuleTest.java
git commit -m "feat: add OrphanedResourcesRule to detect unused resources

Implement best-practice rule to find unreferenced resources:
- Returns warnings (not errors) for SHOULD requirement
- Recursively checks nested item hierarchies
- 3 tests covering simple and complex scenarios
- Helps identify incomplete manifests and wasted space

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 6: Enhance Scorm12ResourceValidator with Rule Composition

**Files:**
- Modify: `src/main/java/dev/jcputney/elearning/parser/validators/Scorm12ResourceValidator.java`
- Modify: `src/test/java/dev/jcputney/elearning/parser/validators/Scorm12ResourceValidatorTest.java`

**Step 1: Write integration test for rule composition**

Add to Scorm12ResourceValidatorTest.java:

```java
@Test
void validate_withMultipleRuleViolations_returnsAllIssues() {
  // Create manifest with multiple violations:
  // 1. Duplicate identifiers
  // 2. Orphaned resource
  // 3. Missing resource reference
  Scorm12Manifest manifest = new Scorm12Manifest();
  manifest.setIdentifier("dup_id");

  Scorm12Organizations organizations = new Scorm12Organizations();
  Scorm12Organization org = new Scorm12Organization();
  org.setIdentifier("dup_id"); // Duplicate!

  Scorm12Item item = new Scorm12Item();
  item.setIdentifier("item1");
  item.setIdentifierRef("missing_resource"); // Missing ref!
  org.setItems(Collections.singletonList(item));
  organizations.setOrganizationList(Collections.singletonList(org));
  manifest.setOrganizations(organizations);

  Scorm12Resources resources = new Scorm12Resources();
  Scorm12Resource res1 = new Scorm12Resource();
  res1.setIdentifier("orphaned"); // Orphaned!
  res1.setHref("unused.html");
  resources.setResourceList(Collections.singletonList(res1));
  manifest.setResources(resources);

  Scorm12ResourceValidator validator = new Scorm12ResourceValidator();
  ValidationResult result = validator.validate(manifest);

  assertThat(result.isValid()).isFalse();
  assertThat(result.hasErrors()).isTrue();
  assertThat(result.hasWarnings()).isTrue();

  // Should have errors from duplicate ID and missing ref
  assertThat(result.getErrors().size()).isGreaterThanOrEqualTo(2);
  // Should have warning from orphaned resource
  assertThat(result.getWarnings().size()).isGreaterThanOrEqualTo(1);
}
```

**Step 2: Run test to verify it fails**

Run: `./mvnw test -Dtest=Scorm12ResourceValidatorTest::validate_withMultipleRuleViolations_returnsAllIssues`
Expected: FAIL - new rules not integrated yet

**Step 3: Enhance Scorm12ResourceValidator to use rules**

Modify Scorm12ResourceValidator.java:

```java
// Add imports at top
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.common.DuplicateIdentifierRule;
import dev.jcputney.elearning.parser.validators.rules.common.OrphanedResourcesRule;
import dev.jcputney.elearning.parser.validators.rules.common.PathSecurityRule;
import java.util.Arrays;
import java.util.List;

// Update class to use rules
public class Scorm12ResourceValidator {

  private final List<ValidationRule<Scorm12Manifest>> commonRules;

  /**
   * Constructs a new Scorm12ResourceValidator with default rules.
   */
  public Scorm12ResourceValidator() {
    this.commonRules = Arrays.asList(
        new DuplicateIdentifierRule(),
        new PathSecurityRule(),
        new OrphanedResourcesRule()
    );
  }

  /**
   * Validates a SCORM 1.2 manifest for structural and reference integrity.
   * Now uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The SCORM 1.2 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Scorm12Manifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Run common rules first
    ValidationResult commonRulesResult = commonRules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);

    // Keep existing validation logic (will be extracted to rules later)
    ValidationResult existingValidation = validateExisting(manifest);

    // Merge all results
    return commonRulesResult.merge(existingValidation);
  }

  /**
   * Existing validation logic - will be extracted to individual rules in future tasks.
   */
  private ValidationResult validateExisting(Scorm12Manifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // [Keep all existing validation code here - unchanged for now]
    // Build resource index for fast lookup
    Map<String, Scorm12Resource> resourceIndex = buildResourceIndex(manifest);

    // Validate organizations
    validateOrganizations(manifest, resourceIndex, issues);

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  // [Keep all existing private methods - unchanged]
}
```

**Step 4: Run test to verify it passes**

Run: `./mvnw test -Dtest=Scorm12ResourceValidatorTest::validate_withMultipleRuleViolations_returnsAllIssues`
Expected: PASS

**Step 5: Run all Scorm12ResourceValidator tests**

Run: `./mvnw test -Dtest=Scorm12ResourceValidatorTest`
Expected: All tests PASS (existing + new integration test)

**Step 6: Commit**

```bash
git add src/main/java/dev/jcputney/elearning/parser/validators/Scorm12ResourceValidator.java
git add src/test/java/dev/jcputney/elearning/parser/validators/Scorm12ResourceValidatorTest.java
git commit -m "refactor: enhance Scorm12ResourceValidator with rule composition

Integrate common validation rules into SCORM 1.2 validator:
- DuplicateIdentifierRule for ID uniqueness
- PathSecurityRule for security validation
- OrphanedResourcesRule for best practices
- Rules execute via composition pattern (merge results)
- Existing validation logic preserved (will extract later)
- Integration test verifies multiple rules work together

Next: Extract remaining validation logic to individual rules.

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>"
```

---

## Task 7: Run Full Test Suite and Verify

**Step 1: Run all tests**

Run: `./mvnw test`
Expected: All 844+ tests PASS (841 existing + 3 new rule tests)

**Step 2: Verify test coverage**

Run: `./mvnw test jacoco:report`
Check: `target/site/jacoco/index.html`
Expected: Validators package coverage increased

**Step 3: Commit if needed**

If any test fixes were needed, commit them.

---

## Phase 1 Complete - Next Steps

At this point, you have:

✅ ValidationRule interface
✅ 3 common rules (DuplicateIdentifier, PathSecurity, OrphanedResources)
✅ Rule composition in Scorm12ResourceValidator
✅ ~18 new tests passing
✅ All existing tests still passing

**Remaining work for comprehensive validation:**

1. **Extract existing SCORM 1.2 validation to rules** (5-8 more rules)
2. **Fetch and implement from spec** (review SCORM 1.2 CAM, add missing rules)
3. **Comprehensive SCORM 1.2 testing** (100+ tests covering all edge cases)
4. **Apply same pattern to other standards** (SCORM 2004, AICC, cmi5, xAPI)
5. **Compliance & stress testing** (real-world manifests, performance tests)

**Estimated remaining effort:** 100-150 hours for all 5 standards with comprehensive testing

**To continue:** Use superpowers:executing-plans skill to execute the remaining tasks, or create follow-on plans for each standard.
