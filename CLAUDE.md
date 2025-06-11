# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java library for parsing and validating eLearning module manifests in industry-standard formats:
- SCORM 1.2 and SCORM 2004 (all editions)
- AICC (INI-based format)
- cmi5 (XML-based)

## Build Commands

```bash
# Build the project
./mvnw clean compile

# Run all tests (unit tests)
./mvnw test

# Run integration tests
./mvnw verify

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName

# Package the library (creates JAR)
./mvnw package

# Install to local Maven repository
./mvnw install

# Generate Javadoc
./mvnw javadoc:javadoc

# Run with code coverage
./mvnw test jacoco:report
# Coverage report: target/site/jacoco/index.html
```

## Architecture

### Core Design Patterns

1. **Factory Pattern**: `ModuleParserFactory` creates appropriate parsers based on module type detection
2. **Strategy Pattern**: Different `FileAccess` implementations for various storage backends (local, ZIP, S3, cached)
3. **Plugin Architecture**: Extensible module type detection via `ModuleTypeDetectorPlugin`

### Package Structure

- `api/` - Core interfaces and contracts
- `parsers/` - Parser implementations for each eLearning standard
- `input/` - Input models organized by standard (scorm12, scorm2004, aicc, cmi5)
- `output/` - Unified output metadata models
- `util/` - Utility classes for XML parsing, file operations, etc.
- `exception/` - Custom exceptions for parser errors

### Key Classes

- `ModuleParser` - Main entry point for parsing modules
- `ModuleParserFactory` - Creates appropriate parser instances
- `FileAccess` - Interface for abstracting file system access
- `ModuleMetadata` - Unified output format for all parser types

### Module Detection

The library automatically detects module types by looking for:
- SCORM: `imsmanifest.xml`
- AICC: `.crs`, `.des`, `.au`, `.cst` files
- cmi5: `cmi5.xml`

### Testing Approach

- Unit tests use JUnit 5 with AssertJ assertions
- Integration tests (`*IntegrationTest.java`) test end-to-end parsing
- Test modules are located in `src/test/resources/modules/`
- Property-based testing with jqwik for edge cases
- Benchmarks available via JMH (run with `-P benchmark` profile)