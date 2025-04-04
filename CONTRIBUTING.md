# Contributing to eLearning Module Parser

First off, thank you for considering contributing to this project! Contributions of all kinds are welcome: code, documentation, bug reports, feature suggestions, and more. This document outlines the process and guidelines to help you get started.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How to Contribute](#how-to-contribute)
  - [Reporting Issues](#reporting-issues)
  - [Feature Requests](#feature-requests)
  - [Submitting Code Changes](#submitting-code-changes)
- [Setting Up Your Development Environment](#setting-up-your-development-environment)
- [Code Guidelines](#code-guidelines)
  - [Code Style](#code-style)
  - [Testing](#testing)
  - [Documentation](#documentation)
- [Pull Request Process](#pull-request-process)
- [Community Support](#community-support)

---

## Code of Conduct

This project adheres to the [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you agree to uphold this code. Please report unacceptable behavior to the project maintainers.

---

## How to Contribute

### Reporting Issues

If you find a bug or have a problem using the library, please [create an issue](https://github.com/jcputney/elearning-module-parser/issues/new). Include as much detail as possible:

- Steps to reproduce the problem.
- Expected vs. actual behavior.
- Version of the library, Java, and any other relevant environment details.

### Feature Requests

Have an idea for a new feature? [Submit a feature request](https://github.com/jcputney/elearning-module-parser/issues/new?labels=enhancement) and describe:

- The problem the feature solves.
- A high-level description of the proposed solution.
- Examples or use cases, if possible.

### Submitting Code Changes

If you’re ready to contribute code, follow these steps:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/my-feature`).
3. Commit your changes (`git commit -m "Add my feature"`).
4. Push your branch (`git push origin feature/my-feature`).
5. Open a pull request against the `main` branch.

---

## Setting Up Your Development Environment

1. Clone your forked repository:

   ```bash
   git clone https://github.com/your-username/elearning-module-parser.git
   cd elearning-module-parser
   ```

2. Build the project:

   ```bash
   mvn clean compile
   ```

3. Run the tests:

   ```bash
   mvn test
   ```

---

## Code Guidelines

### Code Style

Follow these practices:

- Use Java 17 language features where appropriate.
- Maintain consistency with the existing codebase.
- Ensure your IDE is configured to use the `.editorconfig` file.

### Testing

- Add unit tests for new functionality.
- Ensure test coverage for edge cases.
- Use the JUnit 5 testing framework.
- Mock external dependencies using the appropriate tools (e.g., Mockito).

Run tests locally before submitting:

```bash
mvn test
```

### Documentation

- Add Javadoc for all public classes and methods.
- Update the `README.md` if your changes introduce new features or affect usage.

---

## Pull Request Process

1. Ensure your changes pass all tests and adhere to the code style.
2. Provide a clear description of the changes in your pull request.
3. Reference the issue your pull request addresses (if applicable).
4. Ensure your branch is up to date with `main`.

Pull requests will be reviewed by the maintainers. Feedback may be provided for improvement.

---

## Community Support

If you need help or have questions, feel free to:

- [Open a discussion](https://github.com/jcputney/elearning-module-parser/discussions).
- Join the conversation on relevant issues or pull requests.

---

Thank you for contributing to eLearning Module Parser! Your support makes this project better for everyone.
