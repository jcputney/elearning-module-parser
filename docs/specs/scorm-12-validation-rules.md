# SCORM 1.2 Validation Rules

This document extracts validation rules from the SCORM 1.2 Content Aggregation Model (CAM) specification.
These rules form the basis for comprehensive validation of SCORM 1.2 packages.

## Rule 1: Manifest Identifier Required

**Spec Reference:** SCORM 1.2 CAM Section 2.3.1
**Requirement Level:** MUST
**Category:** Structural

**Description:**
The manifest element must have an identifier attribute that uniquely identifies the content package.

**Error Code:** `SCORM12_MISSING_MANIFEST_ID`
**Error Message:** `"Manifest element missing required identifier attribute"`
**Suggested Fix:** `"Add identifier attribute to manifest element (e.g., <manifest identifier='course_001'>)"`

**Test Cases:**
- Valid case: Manifest with non-empty identifier attribute
- Invalid case: Manifest without identifier attribute
- Invalid case: Manifest with empty identifier attribute
- Edge cases: Identifier with special characters, very long identifier

---

## Rule 2: Organizations Element Required

**Spec Reference:** SCORM 1.2 CAM Section 2.3.2
**Requirement Level:** MUST
**Category:** Structural

**Description:**
The manifest must contain exactly one organizations element.

**Error Code:** `SCORM12_MISSING_ORGANIZATIONS`
**Error Message:** `"Manifest must contain an <organizations> element"`
**Suggested Fix:** `"Add <organizations> element to manifest"`

**Test Cases:**
- Valid case: Manifest with organizations element
- Invalid case: Manifest without organizations element

---

## Rule 3: At Least One Organization Required

**Spec Reference:** SCORM 1.2 CAM Section 2.3.2
**Requirement Level:** MUST
**Category:** Cardinality

**Description:**
The organizations element must contain at least one organization element.

**Error Code:** `SCORM12_NO_ORGANIZATIONS`
**Error Message:** `"<organizations> element must contain at least one <organization>"`
**Suggested Fix:** `"Add at least one <organization> element within <organizations>"`

**Test Cases:**
- Valid case: Organizations with one or more organization elements
- Invalid case: Empty organizations element
- Edge cases: Multiple organizations

---

## Rule 4: Default Organization Reference Validity

**Spec Reference:** SCORM 1.2 CAM Section 2.3.2
**Requirement Level:** MUST
**Category:** Referential

**Description:**
If the organizations element has a default attribute, it must reference an existing organization identifier.

**Error Code:** `SCORM12_INVALID_DEFAULT_ORG`
**Error Message:** `"Default organization '{default_id}' not found"`
**Suggested Fix:** `"Ensure the default attribute references a valid organization identifier"`

**Test Cases:**
- Valid case: Default attribute referencing existing organization
- Valid case: No default attribute (optional)
- Invalid case: Default attribute referencing non-existent organization
- Invalid case: Default attribute with empty value
- Edge cases: Case sensitivity, whitespace handling

---

## Rule 5: Organization Identifier Required and Unique

**Spec Reference:** SCORM 1.2 CAM Section 2.3.2
**Requirement Level:** MUST
**Category:** Uniqueness

**Description:**
Each organization element must have a unique identifier attribute.

**Error Code:** `SCORM12_DUPLICATE_ORG_ID`
**Error Message:** `"Organization identifier '{id}' is used {count} times but must be unique"`
**Suggested Fix:** `"Rename duplicate organization identifiers to be unique"`

**Test Cases:**
- Valid case: All organizations have unique identifiers
- Invalid case: Two organizations with same identifier
- Invalid case: Organization without identifier attribute
- Edge cases: Identifier matching manifest or resource ID

---

## Rule 6: Item Identifiers Unique Within Scope

**Spec Reference:** SCORM 1.2 CAM Section 2.3.3
**Requirement Level:** MUST
**Category:** Uniqueness

**Description:**
All item identifiers must be unique within the manifest. This includes nested items.

**Error Code:** `SCORM12_DUPLICATE_ITEM_ID`
**Error Message:** `"Item identifier '{id}' is used {count} times but must be unique"`
**Suggested Fix:** `"Rename duplicate item identifiers to be unique"`

**Test Cases:**
- Valid case: All items have unique identifiers
- Invalid case: Two items with same identifier at same level
- Invalid case: Parent and child item with same identifier
- Edge cases: Deep nesting, items across different organizations

---

## Rule 7: Item Identifierref Must Reference Existing Resource

**Spec Reference:** SCORM 1.2 CAM Section 2.3.3
**Requirement Level:** MUST
**Category:** Referential

**Description:**
If an item has an identifierref attribute, it must reference an existing resource identifier.

**Error Code:** `SCORM12_MISSING_RESOURCE_REF`
**Error Message:** `"Item references non-existent resource '{resource_id}'"`
**Suggested Fix:** `"Ensure the identifierref attribute references a valid resource identifier"`

**Test Cases:**
- Valid case: Item identifierref matches resource identifier
- Valid case: Item without identifierref (container item)
- Invalid case: Identifierref references non-existent resource
- Invalid case: Identifierref with empty value
- Edge cases: Circular references, case sensitivity

---

## Rule 8: Resources Element Required

**Spec Reference:** SCORM 1.2 CAM Section 2.3.4
**Requirement Level:** MUST
**Category:** Structural

**Description:**
The manifest must contain exactly one resources element.

**Error Code:** `SCORM12_MISSING_RESOURCES`
**Error Message:** `"Manifest must contain a <resources> element"`
**Suggested Fix:** `"Add <resources> element to manifest"`

**Test Cases:**
- Valid case: Manifest with resources element
- Invalid case: Manifest without resources element

---

## Rule 9: Resource Identifier Required and Unique

**Spec Reference:** SCORM 1.2 CAM Section 2.3.4
**Requirement Level:** MUST
**Category:** Uniqueness

**Description:**
Each resource element must have a unique identifier attribute.

**Error Code:** `SCORM12_DUPLICATE_RESOURCE_ID`
**Error Message:** `"Resource identifier '{id}' is used {count} times but must be unique"`
**Suggested Fix:** `"Rename duplicate resource identifiers to be unique"`

**Test Cases:**
- Valid case: All resources have unique identifiers
- Invalid case: Two resources with same identifier
- Invalid case: Resource without identifier attribute
- Edge cases: Identifier matching manifest or organization ID

---

## Rule 10: Resource with Type Webcontent Must Have Href

**Spec Reference:** SCORM 1.2 CAM Section 2.3.4
**Requirement Level:** MUST
**Category:** Data

**Description:**
Resources with type="webcontent" (the default type) must have an href attribute pointing to the launch file.

**Error Code:** `SCORM12_MISSING_LAUNCH_URL`
**Error Message:** `"Resource '{resource_id}' is missing href attribute (launch URL)"`
**Suggested Fix:** `"Add an href attribute pointing to the SCO's launch file"`

**Test Cases:**
- Valid case: Resource with href attribute
- Invalid case: Resource without href attribute
- Invalid case: Resource with empty href attribute
- Edge cases: Relative vs absolute paths, special characters in href

---

## Rule 11: No Duplicate Identifiers Across Manifest

**Spec Reference:** SCORM 1.2 CAM Section 2.3.1
**Requirement Level:** MUST
**Category:** Uniqueness

**Description:**
All identifiers (manifest, organization, item, resource) must be globally unique within the package.
Duplicate identifiers cause unpredictable behavior in SCORM players.

**Error Code:** `DUPLICATE_IDENTIFIER`
**Error Message:** `"Identifier '{id}' is used {count} times but must be unique"`
**Suggested Fix:** `"Rename duplicate identifiers to be unique. Locations: {locations}"`

**Test Cases:**
- Valid case: All identifiers unique across all elements
- Invalid case: Manifest ID matches organization ID
- Invalid case: Resource ID matches item ID
- Invalid case: Same ID used in multiple resource elements
- Edge cases: Case sensitivity, whitespace differences

---

## Rule 12: Resource Href Paths Must Be Safe

**Spec Reference:** Security Best Practice
**Requirement Level:** MUST
**Category:** Security

**Description:**
Resource href and file paths must be safe and not contain directory traversal patterns,
absolute paths, or external references. This prevents malicious packages from accessing
files outside the package or making external requests.

**Error Code:** `UNSAFE_PATH_TRAVERSAL` | `UNSAFE_ABSOLUTE_PATH` | `UNSAFE_EXTERNAL_URL` | `UNSAFE_NULL_BYTE`
**Error Message:** `"Path contains directory traversal pattern: '{path}'"` (varies by violation)
**Suggested Fix:** `"Remove '../' or '..\' from the path. All content should be within the package."`

**Test Cases:**
- Valid case: Relative paths like "content/page.html"
- Invalid case: Path traversal "../../../etc/passwd"
- Invalid case: Absolute paths "/usr/local/file.html" or "C:\path\file.html"
- Invalid case: External URLs "http://evil.com/malware.js"
- Invalid case: Protocol-relative URLs "//evil.com/script.js"
- Invalid case: Null bytes in paths
- Edge cases: Mixed separators, Unicode characters, URL encoding

---

## Rule 13: At Least One Launchable Resource Required

**Spec Reference:** SCORM 1.2 CAM Section 2.1
**Requirement Level:** MUST
**Category:** Cardinality

**Description:**
A SCORM package must contain at least one resource that can be launched (has an href attribute).
Empty packages with no launchable content are invalid.

**Error Code:** `SCORM12_NO_LAUNCHABLE_RESOURCES`
**Error Message:** `"Package must contain at least one launchable resource with an href attribute"`
**Suggested Fix:** `"Add at least one resource element with a valid href attribute"`

**Test Cases:**
- Valid case: Package with one or more resources with href
- Invalid case: Package with resources but none have href
- Invalid case: Package with empty resources element
- Edge cases: Resources referenced but not launchable

---

## Rule 14: Orphaned Resources Should Be Flagged

**Spec Reference:** Best Practice
**Requirement Level:** SHOULD
**Category:** Data

**Description:**
Resources that are not referenced by any item should be flagged as potential issues.
While not strictly invalid, orphaned resources waste space and may indicate incomplete manifests.
This is a warning, not an error.

**Error Code:** `ORPHANED_RESOURCE`
**Error Message:** `"Resource '{resource_id}' is not referenced by any item"`
**Suggested Fix:** `"Either reference this resource from an item or remove it to reduce package size"`

**Test Cases:**
- Valid case: All resources referenced by items
- Warning case: Resource not referenced by any item
- Warning case: Multiple orphaned resources
- Edge cases: Resources referenced by nested items, resources in non-default organizations

---

## Summary

- **12 MUST requirements** (errors that invalidate the package)
- **2 SHOULD requirements** (warnings for best practices)
- **Total: 14 validation rules**

These rules cover:
- **Structural validation** (4 rules): Required elements and structure
- **Referential integrity** (2 rules): References point to valid targets
- **Uniqueness** (4 rules): No duplicate identifiers
- **Data validation** (2 rules): Required attributes and valid values
- **Cardinality** (1 rule): Minimum number of elements
- **Security** (1 rule): Safe file paths

## Implementation Priority

**Phase 1 (Highest Priority):**
- Rule 11: Duplicate identifiers (common bug, affects player behavior)
- Rule 12: Path security (critical security issue)
- Rule 7: Missing resource references (causes runtime failures)

**Phase 2 (Core Validation):**
- Rules 1-6: Structural and referential integrity
- Rules 8-10: Resource validation

**Phase 3 (Best Practices):**
- Rule 13: Launchable resource check
- Rule 14: Orphaned resources (warning only)
