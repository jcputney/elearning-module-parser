# Validation System Design

## Overview

Add comprehensive validation feedback to the eLearning module parser library. Current implementation throws exceptions on the first error encountered. New system will collect all validation issues, provide structured error reporting, and support both strict and lenient parsing modes.

## Goals

1. Show users ALL validation issues at once, not just the first error
2. Provide actionable error messages with location context and suggested fixes
3. Support both strict (fail on errors) and lenient (allow errors) modes
4. Maintain immutable, functional design patterns
5. Keep validation logic separate and testable

## Core Design Decisions

### Immutable Builder Pattern
Validators return immutable `ValidationResult` objects that can be merged together. Each validator is independent and composable.

### Separate Validator Classes
Each validation concern gets its own validator class (e.g., `Scorm12ResourceValidator`, `Scorm12OrganizationValidator`) following Single Responsibility Principle.

### Static Factory for Exceptions
`ValidationResult.toException()` creates `ModuleParsingException` instances, making the conversion point explicit and keeping exception creation logic with the result.

### Strict/Lenient Mode
Simple boolean flag in `ParserOptions`. Two severity levels only: ERROR and WARNING.

### Clean Break from Metadata Map
Remove generic `Map<String, Object>` metadata from `ModuleException` base class. Use typed `ValidationResult` in `ModuleParsingException` instead.

## Architecture

### Package Structure

```
dev.jcputney.elearning.parser.validation/
  ├── ValidationIssue.java          (record)
  ├── ValidationResult.java         (immutable)
  ├── Validator.java                (interface)
  ├── ValidationCodes.java          (constants)
  └── scorm12/
      ├── Scorm12ResourceValidator.java
      ├── Scorm12OrganizationValidator.java
      └── Scorm12MetadataValidator.java
```

### Core Classes

**ValidationIssue** (immutable record):
```java
public record ValidationIssue(
    Severity severity,
    String code,
    String message,
    String location,
    String suggestedFix
) {
    public enum Severity { ERROR, WARNING }

    public static ValidationIssue error(String code, String message, String location) {
        return new ValidationIssue(Severity.ERROR, code, message, location, null);
    }

    public static ValidationIssue warning(String code, String message, String location) {
        return new ValidationIssue(Severity.WARNING, code, message, location, null);
    }
}
```

**ValidationResult** (immutable):
```java
public final class ValidationResult {
    private final List<ValidationIssue> issues;

    public static ValidationResult valid() { ... }
    public static ValidationResult of(ValidationIssue... issues) { ... }

    public ValidationResult merge(ValidationResult other) {
        List<ValidationIssue> merged = new ArrayList<>(this.issues);
        merged.addAll(other.issues);
        return new ValidationResult(merged);
    }

    public boolean isValid() { return !hasErrors(); }
    public boolean hasErrors() { ... }
    public List<ValidationIssue> getErrors() { ... }
    public List<ValidationIssue> getWarnings() { ... }

    public String formatErrors() { ... }

    public ModuleParsingException toException(String contextMessage) {
        return new ModuleParsingException(contextMessage, this);
    }
}
```

**Validator Interface**:
```java
@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T target);
}
```

### Exception Changes

**ModuleException** (simplified):
- Remove: `metadata` Map, `addMetadata()`, `getMetadata()`
- Keep: Basic Exception constructors only

**ModuleParsingException** (enhanced):
```java
public final class ModuleParsingException extends ModuleException {
    private final ValidationResult validationResult;

    // Package-private - only called from ValidationResult.toException()
    ModuleParsingException(String contextMessage, ValidationResult result) {
        super(contextMessage + ":\n" + result.formatErrors());
        this.validationResult = result;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
```

### API Changes

**ModuleParser Interface**:
```java
public interface ModuleParser<M extends PackageManifest> {
    ValidationResult validate();
    ModuleMetadata<M> parse() throws ModuleParsingException;
    ParserOptions getOptions();
}
```

**ParserOptions**:
```java
public class ParserOptions {
    private boolean strictMode = true;

    public ParserOptions setStrictMode(boolean strict) { ... }
    public boolean isStrictMode() { ... }

    public static ParserOptions strict() { ... }
    public static ParserOptions lenient() { ... }
}
```

### Parser Implementation Pattern

```java
public class Scorm12Parser extends BaseParser<ScormManifest> {

    @Override
    public ValidationResult validate() {
        ScormManifest manifest = parseManifest();

        return new Scorm12ResourceValidator().validate(manifest)
            .merge(new Scorm12OrganizationValidator().validate(manifest))
            .merge(new Scorm12MetadataValidator().validate(manifest));
    }

    @Override
    public ModuleMetadata<ScormManifest> parse() throws ModuleParsingException {
        ValidationResult result = validate();

        if (getOptions().isStrictMode() && result.hasErrors()) {
            throw result.toException("Failed to parse SCORM 1.2 manifest");
        }

        // In lenient mode: log warnings but continue
        ModuleMetadata<ScormManifest> metadata = buildMetadata();
        metadata.setValidationResult(result); // Attach for user inspection
        return metadata;
    }
}
```

### Example Validator

```java
public class Scorm12ResourceValidator implements Validator<ScormManifest> {
    @Override
    public ValidationResult validate(ScormManifest manifest) {
        ValidationResult result = ValidationResult.valid();

        for (Item item : manifest.getOrganizations().getAllItems()) {
            if (item.getIdentifierref() != null) {
                result = result.merge(validateResourceReference(item, manifest));
            }
        }

        return result;
    }

    private ValidationResult validateResourceReference(Item item, ScormManifest manifest) {
        String ref = item.getIdentifierref();
        if (manifest.getResources().findById(ref) == null) {
            return ValidationResult.of(
                ValidationIssue.error(
                    "SCORM12_MISSING_RESOURCE_REF",
                    "Item references resource '" + ref + "' but no such resource exists",
                    "item[@identifier='" + item.getIdentifier() + "']"
                )
            );
        }
        return ValidationResult.valid();
    }
}
```

## Error Codes

Organized by standard in `ValidationCodes` class:

```java
public final class ValidationCodes {
    // SCORM 1.2
    public static final String SCORM12_MISSING_RESOURCE_REF = "SCORM12_MISSING_RESOURCE_REF";
    public static final String SCORM12_MISSING_LAUNCH_URL = "SCORM12_MISSING_LAUNCH_URL";
    public static final String SCORM12_INVALID_DEFAULT_ORG = "SCORM12_INVALID_DEFAULT_ORG";
    public static final String SCORM12_FILE_NOT_FOUND = "SCORM12_FILE_NOT_FOUND";

    // SCORM 2004
    public static final String SCORM2004_INVALID_SEQUENCING = "SCORM2004_INVALID_SEQUENCING";

    // AICC
    public static final String AICC_INVALID_AU_REFERENCE = "AICC_INVALID_AU_REFERENCE";

    // cmi5
    public static final String CMI5_MISSING_LAUNCH_METHOD = "CMI5_MISSING_LAUNCH_METHOD";

    // xAPI
    public static final String XAPI_INVALID_ACTIVITY_ID = "XAPI_INVALID_ACTIVITY_ID";

    private ValidationCodes() {}
}
```

### Severity Guidelines

- **ERROR**: Violates specification, will fail in compliant LMS systems
- **WARNING**: Bad practice or missing optional files, might work but risky

## Usage Examples

### Validate Before Parsing
```java
ModuleParser parser = factory.getParser(file, ParserOptions.strict());
ValidationResult validation = parser.validate();

if (!validation.isValid()) {
    validation.getErrors().forEach(error ->
        log.error("[{}] {}", error.code(), error.message())
    );
    return;
}

ModuleMetadata metadata = parser.parse();
```

### Let parse() Validate (Traditional)
```java
try {
    ModuleParser parser = factory.getParser(file, ParserOptions.strict());
    ModuleMetadata metadata = parser.parse();
} catch (ModuleParsingException e) {
    // Exception message shows ALL errors formatted
    log.error(e.getMessage());

    // Access structured errors programmatically
    e.getValidationResult().getErrors().forEach(error -> {
        // Process each error
    });
}
```

### Lenient Mode
```java
ModuleParser parser = factory.getParser(file, ParserOptions.lenient());
ModuleMetadata metadata = parser.parse(); // Won't throw on errors

if (metadata.getValidationResult().hasWarnings()) {
    // Inspect and handle warnings
}
```

## Testing Strategy

### Unit Test Validators
```java
@Test
void testMissingResourceReference() {
    ScormManifest manifest = createManifestWithMissingRef();

    ValidationResult result = new Scorm12ResourceValidator().validate(manifest);

    assertThat(result.hasErrors()).isTrue();
    ValidationIssue issue = result.getErrors().get(0);
    assertThat(issue.code()).isEqualTo("SCORM12_MISSING_RESOURCE_REF");
}
```

### Integration Test Parse Behavior
```java
@Test
void testStrictModeFailsOnErrors() {
    Scorm12Parser parser = new Scorm12Parser(invalidPath, ParserOptions.strict());

    ModuleParsingException ex = assertThrows(
        ModuleParsingException.class,
        () -> parser.parse()
    );

    assertThat(ex.getValidationResult().hasErrors()).isTrue();
}

@Test
void testLenientModeContinues() {
    Scorm12Parser parser = new Scorm12Parser(warningPath, ParserOptions.lenient());

    ModuleMetadata metadata = parser.parse(); // Doesn't throw

    assertThat(metadata.getValidationResult().hasWarnings()).isTrue();
}
```

## Implementation Order

1. Create validation package with core classes (ValidationIssue, ValidationResult, Validator)
2. Remove metadata from ModuleException base class
3. Update ModuleParsingException with ValidationResult
4. Add ParserOptions class
5. Update ModuleParser interface with validate() method
6. Implement first validator (Scorm12ResourceValidator) as proof of concept
7. Update Scorm12Parser to use new validation
8. Add tests for validation framework
9. Implement remaining validators for each standard
10. Update ModuleMetadata to store ValidationResult

## Benefits

- **User-friendly**: See all errors at once, not whack-a-mole
- **Actionable**: Error messages include location and suggestions
- **Testable**: Validators are isolated and independently testable
- **Flexible**: Strict or lenient mode depending on use case
- **Type-safe**: Structured ValidationResult vs generic metadata Map
- **Composable**: Immutable validators can be easily combined
