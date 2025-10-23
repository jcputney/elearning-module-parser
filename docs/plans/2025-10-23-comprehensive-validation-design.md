# Comprehensive eLearning Module Validation System

**Date:** 2025-10-23
**Status:** Design Approved
**Implementation:** Pending

## Overview

Enhance the existing validation system to provide comprehensive, spec-compliant validation for all 5 eLearning standards (SCORM 1.2, SCORM 2004, AICC, cmi5, xAPI) using a rule-based architecture.

## Current State

**Existing Validators (Basic):**
- Scorm12ResourceValidator - validates organizations, resource references, launch URLs
- Scorm2004ResourceValidator - similar to SCORM 1.2
- AiccValidator - basic course/title/launch validation
- Cmi5Validator - basic course/AU validation
- XapiValidator - basic activities/launch validation

**Test Coverage:** 30 validator tests

**Known Gaps:**
- Missing duplicate identifier detection
- No orphaned resource validation
- No path security validation
- No cardinality validation (min/max elements)
- No data format validation
- No circular reference detection
- Limited edge case testing
- No performance/stress testing

## Requirements

### Scope
- **All 5 standards** equally prioritized
- **MUST + SHOULD** requirements (required + recommended practices)
- **Breaking changes acceptable** - strict spec compliance
- **File validation** - use existing toggle, add path security always

### Quality Targets
- **470-630 comprehensive tests** covering:
  - Every MUST requirement
  - Edge cases & combinations
  - Malformed input handling
  - Performance/stress tests

### Approach
- **Parallel TDD per standard** (spec → rules → implement → test → verify)
- **Fetch official specs** for accuracy
- **Rule-based architecture** for testability and maintainability

## Architecture

### Rule-Based Validator Pattern

**Core Interface:**
```java
package dev.jcputney.elearning.parser.validators.rules;

/**
 * Interface for individual validation rules that can be composed together.
 * Each rule encapsulates a single validation concern with spec traceability.
 *
 * @param <T> The manifest type this rule validates
 */
public interface ValidationRule<T> {
  /**
   * Validates the manifest according to this rule.
   *
   * @param manifest The manifest to validate
   * @return ValidationResult with any issues found
   */
  ValidationResult validate(T manifest);

  /**
   * Human-readable name of this rule.
   *
   * @return Rule name (e.g., "Resource Reference Validation")
   */
  String getRuleName();

  /**
   * Specification reference for traceability.
   *
   * @return Spec reference (e.g., "SCORM 1.2 CAM Section 2.3.4")
   */
  String getSpecReference();
}
```

**Enhanced Validators:**
```java
public class Scorm12ResourceValidator {
  private final List<ValidationRule<Scorm12Manifest>> rules;

  public Scorm12ResourceValidator() {
    this.rules = Arrays.asList(
      // Common rules
      new DuplicateIdentifierRule<>(),
      new OrphanedResourcesRule(),
      new PathSecurityRule<>(),

      // SCORM 1.2 specific
      new OrganizationsRequiredRule(),
      new DefaultOrganizationValidRule(),
      new ResourceReferenceValidRule(),
      new LaunchUrlRequiredRule(),
      new ManifestIdentifierRule(),
      new ResourceTypeValidRule(),
      // ... more rules from spec review
    );
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
```

### Package Structure

```
src/main/java/dev/jcputney/elearning/parser/validators/
  rules/
    ValidationRule.java                    # Core interface

    common/                                # Reusable across standards
      DuplicateIdentifierRule.java         # Checks for duplicate IDs
      OrphanedResourcesRule.java           # Finds unused resources
      PathSecurityRule.java                # Validates paths are safe

    scorm12/                               # SCORM 1.2 specific
      OrganizationsRequiredRule.java
      DefaultOrganizationValidRule.java
      ResourceReferenceValidRule.java
      LaunchUrlRequiredRule.java
      ManifestIdentifierRule.java
      ResourceTypeValidRule.java
      # ... more from spec review

    scorm2004/                             # SCORM 2004 specific
      OrganizationsRequiredRule.java       # May share with SCORM 1.2
      SequencingValidRule.java             # SCORM 2004 only
      NavigationValidRule.java             # SCORM 2004 only
      # ... more from spec review

    aicc/                                  # AICC specific
      CourseStructureValidRule.java
      DescriptorFilesRule.java
      # ... more from spec review

    cmi5/                                  # cmi5 specific
      AuStructureValidRule.java
      BlockStructureValidRule.java
      # ... more from spec review

    xapi/                                  # xAPI/TinCan specific
      ActivityStructureValidRule.java
      # ... more from spec review

  Scorm12ResourceValidator.java           # Enhanced orchestrator
  Scorm2004ResourceValidator.java         # Enhanced orchestrator
  AiccValidator.java                       # Enhanced orchestrator
  Cmi5Validator.java                       # Enhanced orchestrator
  XapiValidator.java                       # Enhanced orchestrator
```

### Test Structure

```
src/test/java/dev/jcputney/elearning/parser/
  validators/
    rules/                                 # Rule unit tests (10-20 per rule)
      common/
        DuplicateIdentifierRuleTest.java
        OrphanedResourcesRuleTest.java
        PathSecurityRuleTest.java

      scorm12/
        OrganizationsRequiredRuleTest.java
        ResourceReferenceValidRuleTest.java
        # ... one test per rule

      scorm2004/
        SequencingValidRuleTest.java
        # ... one test per rule

      # ... aicc, cmi5, xapi rule tests

    Scorm12ResourceValidatorTest.java     # Integration tests (5-10)
    Scorm2004ResourceValidatorTest.java
    AiccValidatorTest.java
    Cmi5ValidatorTest.java
    XapiValidatorTest.java

  compliance/                              # Real-world spec compliance (20-30 per standard)
    Scorm12ComplianceTest.java
    Scorm2004ComplianceTest.java
    AiccComplianceTest.java
    Cmi5ComplianceTest.java
    XapiComplianceTest.java

  stress/                                  # Performance tests (5-10 total)
    ValidatorPerformanceTest.java
    DeepNestingTest.java
    LargeManifestTest.java
    CircularReferenceTest.java

src/test/resources/validators/            # Test fixtures
  scorm12/
    duplicate-ids/
      invalid/manifest.xml
    resource-reference/
      valid/manifest.xml
      invalid/manifest.xml
    # ... one directory per rule
  scorm2004/
    # ... similar structure
  # ... aicc, cmi5, xapi fixtures
```

## Rule Categories

### 1. Structural Rules
Validate required elements exist with correct structure.

**Examples:**
- `OrganizationsRequiredRule` - manifest must have `<organizations>` element
- `ResourcesRequiredRule` - manifest must have `<resources>` element
- `CourseRequiredRule` (AICC) - must have course file
- `AuRequiredRule` (cmi5) - must have at least one AU

### 2. Referential Integrity Rules
Validate references point to valid targets.

**Examples:**
- `ResourceReferenceValidRule` - item identifierref points to existing resource
- `DefaultOrganizationValidRule` - default attribute references existing organization
- `PrerequisiteValidRule` (SCORM 2004) - prerequisites reference valid items

### 3. Uniqueness Rules
Validate no duplicate identifiers.

**Examples:**
- `DuplicateIdentifierRule` - all identifiers are unique within scope
- `DuplicateFileNameRule` (AICC) - no duplicate file names

### 4. Data Validation Rules
Validate data formats and values.

**Examples:**
- `PathSecurityRule` - paths don't contain `../`, absolute paths, external URLs
- `ResourceTypeValidRule` - resource type from valid vocabulary
- `VersionFormatRule` - version string follows format
- `UrlFormatRule` - URLs are well-formed
- `DurationFormatRule` - duration follows ISO 8601

### 5. Cardinality Rules
Validate element occurrence constraints.

**Examples:**
- `MinimumOrganizationsRule` - at least one organization
- `MinimumResourcesRule` - at least one resource
- `LaunchUrlRequiredRule` - at least one launchable resource
- `MaximumTitleRule` - no more than one title element

### 6. Security Rules
Validate content is safe to process.

**Examples:**
- `PathTraversalRule` - no path traversal attempts
- `ExternalReferenceRule` - no external URLs in critical paths
- `XmlInjectionRule` - no XML/XPath injection patterns
- `FileExtensionRule` - only allowed file extensions

### 7. SCORM 2004 Sequencing Rules
Validate complex sequencing/navigation.

**Examples:**
- `SequencingRulesValidRule` - sequencing rules follow schema
- `NavigationInterfaceValidRule` - navigation interface valid
- `RollupRulesValidRule` - rollup rules are consistent

## Specification Review Process

### Phase 1: Spec Acquisition
Fetch official specifications for each standard:

1. **SCORM 1.2**
   - Content Aggregation Model (CAM)
   - Run-Time Environment (RTE)
   - Source: ADL SCORM 1.2 documentation

2. **SCORM 2004**
   - CAM 4th Edition
   - SN (Sequencing and Navigation) 4th Edition
   - RTE 4th Edition
   - Source: ADL SCORM 2004 4th Edition documentation

3. **AICC**
   - AGR-006 (Course Structure Format)
   - AGR-010 (Web-Based Computer Managed Instruction)
   - Source: AICC specifications

4. **cmi5**
   - cmi5 Specification
   - Source: xAPI.com cmi5 spec

5. **xAPI/TinCan**
   - TinCan Package Specification
   - Source: GitHub projecttincan/tincan-package-spec

### Phase 2: Rule Extraction Template

For each requirement found in specs:

```markdown
## Rule: [Rule Name]
**Spec Reference:** [Section/Page]
**Requirement Level:** MUST | SHOULD | MAY
**Category:** Structural | Referential | Uniqueness | Data | Cardinality | Security
**Applies To:** [Standard(s)]

**Description:**
[Clear description of what must be validated]

**Validation Logic:**
[Pseudocode or description of validation algorithm]

**Error Code:** `[ERROR_CODE]`
**Error Message:** `"[Template with {placeholders}]"`
**Suggested Fix:** `"[Template with {placeholders}]"`

**Test Cases:**
- [ ] Valid case: [description]
- [ ] Invalid case: [description]
- [ ] Edge case 1: [description]
- [ ] Edge case 2: [description]
- [ ] Multiple violations: [description]
```

### Phase 3: Rule Matrix

Create comprehensive matrix showing which rules apply to which standards:

| Rule Name | SCORM 1.2 | SCORM 2004 | AICC | cmi5 | xAPI | Priority |
|-----------|-----------|------------|------|------|------|----------|
| Duplicate Identifiers | ✓ | ✓ | ✓ | ✓ | ✓ | HIGH |
| Resource References | ✓ | ✓ | - | - | - | HIGH |
| Path Security | ✓ | ✓ | ✓ | ✓ | ✓ | HIGH |
| Orphaned Resources | ✓ | ✓ | ✓ | ✓ | - | MEDIUM |
| ... | ... | ... | ... | ... | ... | ... |

## Implementation Workflow

### Per-Standard Workflow (Parallel for all 5)

For each standard (SCORM 1.2, SCORM 2004, AICC, cmi5, xAPI):

1. **Spec Review** (2-4 hours per standard)
   - Fetch official spec documents
   - Extract MUST/SHOULD requirements
   - Document using rule extraction template
   - Create validation rule list

2. **Rule Implementation** (TDD, 30-60 min per rule)
   - Write failing test for rule
   - Implement rule class
   - Make test pass
   - Refactor for clarity
   - Add edge case tests

3. **Validator Integration** (30 min per standard)
   - Add rule to validator's rule list
   - Write integration test
   - Verify rule execution order doesn't matter

4. **Compliance Testing** (1-2 hours per standard)
   - Create/gather real-world test manifests
   - Test against spec examples
   - Test against known-bad manifests
   - Document results

5. **Performance Testing** (1 hour total, after all validators done)
   - Test deep nesting
   - Test large manifests
   - Test circular references
   - Verify memory usage

### Estimated Effort

| Standard | Rules | Rule Tests | Compliance Tests | Total Tests | Est. Hours |
|----------|-------|------------|------------------|-------------|------------|
| SCORM 1.2 | 15-20 | 150-200 | 20-30 | 170-230 | 20-30 |
| SCORM 2004 | 25-35 | 250-350 | 30-40 | 280-390 | 35-50 |
| AICC | 10-15 | 100-150 | 15-20 | 115-170 | 15-25 |
| cmi5 | 12-18 | 120-180 | 15-20 | 135-200 | 18-28 |
| xAPI | 8-12 | 80-120 | 10-15 | 90-135 | 12-20 |
| **Common** | 5-8 | 50-80 | - | 50-80 | 8-12 |
| **Stress** | - | - | 5-10 | 5-10 | 3-5 |
| **TOTAL** | 75-108 | 750-1080 | 95-135 | 845-1215 | 111-170 |

**Note:** These are comprehensive estimates. Actual implementation may be streamlined by reusing test patterns and rule implementations.

## Success Criteria

### Functional
- [ ] All MUST requirements from specs validated
- [ ] All SHOULD requirements from specs validated
- [ ] Common rules (duplicate IDs, path security, orphaned resources) implemented
- [ ] All 5 validators enhanced with rule-based architecture
- [ ] File validation integrated with existing toggle

### Testing
- [ ] Every rule has 5+ unit tests (valid, invalid, edge cases)
- [ ] Every validator has integration tests
- [ ] Compliance tests with real-world manifests
- [ ] Performance tests with stress scenarios
- [ ] 800+ total tests passing

### Quality
- [ ] Each rule has spec reference in code
- [ ] Error messages are actionable with suggested fixes
- [ ] Test coverage > 90% for validator code
- [ ] Documentation explains each validation rule
- [ ] Performance acceptable (< 100ms per manifest on average)

### Non-Breaking
- [ ] All existing 841 tests still pass
- [ ] Backward compatible API (breaking validation is OK, API breaking is not)
- [ ] ParserOptions integration clean

## Migration Path

### Phase 1: Foundation (Current Session)
- Create ValidationRule interface
- Implement common rules (duplicate IDs, path security, orphaned resources)
- Enhance one validator (SCORM 1.2) as proof of concept
- Comprehensive tests for POC

### Phase 2: Expand (Next 2-3 Sessions)
- Complete SCORM 2004 (most complex)
- Complete AICC, cmi5, xAPI
- Compliance test suites
- Performance testing

### Phase 3: Polish (Final Session)
- Documentation
- Performance optimization if needed
- Final integration testing
- Code review with superpowers:code-reviewer

## Risk Mitigation

### Risk: Specs are ambiguous or contradictory
**Mitigation:** Document interpretation decisions, cite spec sections, make conservative choices

### Risk: Implementation takes longer than estimated
**Mitigation:** Prioritize MUST requirements first, SHOULD requirements second. Can ship incrementally.

### Risk: Performance degrades with many rules
**Mitigation:** Performance tests early, optimize hot paths, consider rule caching

### Risk: Breaking existing valid content
**Mitigation:** Extensive compliance testing, document all breaking changes, provide migration guide

### Risk: Test maintenance burden
**Mitigation:** Reusable test fixtures, clear test organization, good test names

## Appendix: Example Rule Implementation

```java
package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.*;

/**
 * Validates that all identifiers within the manifest are unique.
 * Applies to all standards - duplicate IDs cause unpredictable behavior.
 *
 * Spec References:
 * - SCORM 1.2 CAM Section 2.3.1: "identifier must be unique"
 * - SCORM 2004 CAM Section 2.3.1: "identifier must be unique within scope"
 * - AICC: Implicit requirement for system_id uniqueness
 * - cmi5: AU ids must be unique URIs
 * - xAPI: Activity IDs must be unique URIs
 */
public class DuplicateIdentifierRule<T extends PackageManifest>
    implements ValidationRule<T> {

  @Override
  public String getRuleName() {
    return "Duplicate Identifier Validation";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.1, SCORM 2004 CAM 2.3.1, AICC AGR-006";
  }

  @Override
  public ValidationResult validate(T manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Collect all identifiers with their locations
    Map<String, List<String>> identifierLocations = collectIdentifiers(manifest);

    // Find duplicates
    for (Map.Entry<String, List<String>> entry : identifierLocations.entrySet()) {
      if (entry.getValue().size() > 1) {
        issues.add(ValidationIssue.error(
          "DUPLICATE_IDENTIFIER",
          String.format("Identifier '%s' is used %d times but must be unique",
              entry.getKey(), entry.getValue().size()),
          String.join(", ", entry.getValue()),
          String.format("Change identifier '%s' to be unique in locations: %s",
              entry.getKey(), String.join(", ", entry.getValue()))
        ));
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  /**
   * Collects all identifiers from the manifest with their XPath-like locations.
   * Subclasses can override for standard-specific identifier collection.
   */
  protected Map<String, List<String>> collectIdentifiers(T manifest) {
    Map<String, List<String>> locations = new HashMap<>();
    // Default implementation - can be overridden per standard
    // ...
    return locations;
  }
}
```

## Next Steps

1. Review and approve this design
2. Create detailed implementation plan with task breakdown
3. Set up git worktree for isolated development
4. Begin spec review and rule extraction (SCORM 1.2 first)
5. Implement rules with TDD approach
6. Iterate through all 5 standards
