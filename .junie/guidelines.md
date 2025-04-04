# Java Style Guide for elearning-module-parser

This document outlines the coding standards and best practices for the elearning-module-parser
project. Following these guidelines ensures consistency, maintainability, and readability across the
codebase.

## Table of Contents

1. [Code Formatting and Style](#code-formatting-and-style)
2. [Naming Conventions](#naming-conventions)
3. [Architectural Patterns](#architectural-patterns)
4. [Error Handling](#error-handling)
5. [Documentation Standards](#documentation-standards)
6. [Testing Practices](#testing-practices)
7. [Common Pitfalls to Avoid](#common-pitfalls-to-avoid)
8. [Preferred Programming Paradigms](#preferred-programming-paradigms)

## Code Formatting and Style

### General Guidelines

- Use Java 17 language features where appropriate
- Indent with 2 spaces, not tabs
- Line length should not exceed 100 characters
- Use UTF-8 file encoding
- Files should end with a newline character
- Remove trailing whitespace from all lines
- Use multiline string literals for long strings, instead of concatenating them

### Braces

- Opening braces should be on the same line as the declaration
- Closing braces should be on their own line unless followed by `else`, `catch`, etc.
- Always use braces for control statements, even for single-line blocks

```java
// Good
if(condition){

doSomething();
}

    // Avoid
    if(condition)

doSomething();
```

### Imports

- Do not use wildcard imports (`import java.util.*`)
- Organize imports in the following order:
  1. Static imports
  2. Java standard library imports
  3. Third-party library imports
  4. Project imports
- Within each group, sort alphabetically

## Naming Conventions

### General Guidelines

- Use meaningful, descriptive names
- Avoid abbreviations unless they are widely understood
- Prefer clarity over brevity
- Local variable and method names should match the regular expression '^[a-z][a-zA-Z0-9]*$'

### Specific Naming Patterns

- **Classes**: `PascalCase` (e.g., `ZipFileAccess`)
- **Interfaces**: `PascalCase` (e.g., `FileAccess`)
- **Methods**: `camelCase` (e.g., `getFileContents`)
- **Variables**: `camelCase` (e.g., `zipFile`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `DEFAULT_TIMEOUT`)
- **Packages**: All lowercase, with meaningful hierarchical names (e.g.,
  `dev.jcputney.elearning.parser.api`)

### Special Cases

- Test classes should be named with the class they test followed by `Test` (e.g.,
  `ModuleParserFactoryTest`)
- Exception classes should be named with a descriptive name followed by `Exception` (e.g.,
  `ModuleDetectionException`)

## Architectural Patterns

### Interface-Based Design

- Define contracts using interfaces
- Implement interfaces in concrete classes
- Use dependency injection to provide implementations

```java
// Interface definition
public interface FileAccess {
  InputStream getFileContents(String path) throws IOException;
}


// Implementation
public class ZipFileAccess implements FileAccess {
  @Override
  public InputStream getFileContents(String path) throws IOException {
    // Implementation
  }
}
```

### Package Structure

- Organize code into logical packages:
  - `api`: Interfaces and API definitions
  - `impl`: Implementations of interfaces
  - `exception`: Custom exception classes
  - `util`: Utility classes
  - `input`: Input processing classes
  - `output`: Output processing classes

### Design Patterns

- Use the Factory pattern for creating objects (e.g., `ModuleParserFactory`)
- Use the Strategy pattern for interchangeable algorithms
- Prefer composition over inheritance
- Follow the Single Responsibility Principle (SRP)

## Error Handling

### Exception Types

- Use checked exceptions for recoverable errors
- Use unchecked exceptions (runtime exceptions) for programming errors
- Create custom exception classes for domain-specific errors

### Exception Handling

- Always include meaningful error messages in exceptions
- Chain exceptions to preserve the original cause
- Close resources properly using try-with-resources
- Don't catch exceptions you can't handle properly

```java
// Good
try{
    // Code that might throw an exception
    }catch(IOException e){
    throw new

ModuleParsingException("Failed to parse module",e);
}

    // Avoid
    try{
    // Code that might throw an exception
    }catch(
Exception e){
    // Generic catch block with no specific handling
    }
```

### Null Handling

- Use `Optional` for values that might be absent
- Validate parameters early in methods
- Use defensive programming to check for null values

## Documentation Standards

### Javadoc

- All public classes, interfaces, and methods should have Javadoc comments
- Include a brief description of the purpose and behavior
- Document parameters, return values, and exceptions
- Use HTML tags for formatting when necessary

```java
/**
 * Retrieves the contents of a file as an InputStream.
 *
 * @param path The path to retrieve contents from.
 * @return An InputStream of the file contents.
 * @throws IOException if the file can't be read.
 */
InputStream getFileContents(String path) throws IOException;
```

### Code Comments

- Use comments to explain complex logic or non-obvious behavior
- Avoid redundant comments that merely restate the code
- Keep comments up-to-date with code changes
- Use TODO comments for temporary code or future improvements

### Copyright Headers

- Include the standard copyright header in all source files
- Ensure the year is current

## Testing Practices

### Test Structure

- Use JUnit 5 for testing
- Name test methods descriptively, following the pattern:
  `[method_under_test]_[condition]_[expected_result]`
- Each test should focus on a single behavior
- Use appropriate assertions to verify expected outcomes

### Test Coverage

- Aim for high test coverage, especially for critical code paths
- Test both normal and edge cases
- Test error conditions and exception handling

### Test Resources

- Place test resources in the `src/test/resources` directory
- Organize test resources to mirror the structure of the code they test

### Mocking

- Use mocking frameworks (e.g., Mockito) when appropriate
- Mock external dependencies to isolate the code under test
- Avoid excessive mocking that makes tests brittle

## Common Pitfalls to Avoid

### Resource Management

- Always close resources (files, streams, etc.) properly
- Use try-with-resources for automatic resource management
- Be aware of memory leaks from unclosed resources

### Concurrency Issues

- Be careful with shared mutable state
- Use thread-safe collections when needed
- Consider using immutable objects for shared data

### Performance Considerations

- Avoid premature optimization
- Profile code before optimizing
- Be mindful of expensive operations in loops

### Security Concerns

- Validate all input data
- Don't expose sensitive information in logs or error messages
- Use secure coding practices for handling user input

## Preferred Programming Paradigms

### Object-Oriented vs. Functional

- Use object-oriented design for domain models and business logic
- Use functional programming for data transformations and operations
- Leverage Java's functional interfaces and streams for cleaner code

### Immutability

- Prefer immutable objects when possible
- Use final fields to enforce immutability
- Consider using Lombok's `@Value` annotation for immutable classes

### Dependency Injection

- Use constructor injection for required dependencies
- Use setter injection for optional dependencies
- Consider using a dependency injection framework for complex applications

### Code Reuse

- Extract common functionality into utility classes
- Use composition to share behavior between classes
- Create reusable components with well-defined interfaces

### Exclusions

- Ignore all files in the `src/test/resources/modules` directory
