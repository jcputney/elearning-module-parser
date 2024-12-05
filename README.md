# eLearning Module Parser

[![Build Status](https://img.shields.io/github/actions/workflow/status/jcputney/elearning-module-parser/snapshot.yml)](https://github.com/jcputney/elearning-module-parser/actions)
[![License: LGPL-3.0](https://img.shields.io/badge/license-LGPL%203.0-blue.svg)](https://opensource.org/license/lgpl-3-0)
[![Version](https://img.shields.io/maven-central/v/dev.jcputney/elearning-module-parser)](https://search.maven.org/search?q=g:dev.jcputney%20a:elearning-module-parser)

A robust, modular library for parsing and validating eLearning module manifests in industry-standard
formats such as SCORM 1.2, SCORM 2004, AICC, and cmi5. This library is designed to help developers
integrate eLearning content into Learning Management Systems (LMS) and other platforms.

---

## Features

- **Manifest Parsing**: Supports SCORM 1.2, SCORM 2004, AICC, and cmi5 manifest file parsing.
- **Validation**: Detects missing or invalid files and ensures compliance with the specifications.
- **Lightweight**: Minimal dependencies with optional support for AWS S3 (SDK v1 or v2) module
  handling.

---

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Gradle 7.0+** (or Maven equivalent)

---

### Installation

#### Gradle

```kotlin
implementation("dev.jcputney:elearning-module-parser:0.0.1")
```

#### Maven

```xml

<dependency>
  <groupId>dev.jcputney</groupId>
  <artifactId>elearning-module-parser</artifactId>
  <version>0.0.1</version>
</dependency>
```

---

### Usage

#### Parsing SCORM 2004 Module

```java
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import dev.jcputney.elearning.parser.api.LocalFileAccess;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

public class Main {
  public static void main(String[] args) {
    String modulePath = "path/to/scorm2004/module";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess());
    ModuleMetadata metadata = parser.parse(modulePath);
    System.out.println("Title: " + metadata.getTitle());
    System.out.println("Version: " + metadata.getVersion());
  }
}
```

#### Parsing a cmi5 Module

```java
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.api.ZipFileAccess;

public class Main {
  public static void main(String[] args) {
    String modulePath = "path/to/cmi5/";
    Cmi5Parser parser = new Cmi5Parser(new ZipFileAccess(modulePath + "module.zip"));
    ModuleMetadata metadata = parser.parse(modulePath);

    System.out.println("Course ID: " + metadata.getIdentifier());
  }
}
```

---

## Supported Formats

| Format     | Features                                                                     | Status         |
|------------|------------------------------------------------------------------------------|----------------|
| SCORM 1.2  | Manifest parsing, resource validation                                        | 🚧 In Progress |
| SCORM 2004 | Manifest parsing (all editions), sequencing, objectives                      | 🚧 In Progress |
| AICC       | INI-based parsing for .crs, .des, .au, .cst, prerequisites, assignable units | 🚧 In Progress |
| cmi5       | XML-based manifest parsing, objectives, blocks, and assignable units         | 🚧 In Progress |

---

## Documentation

- [Contributing Guidelines](CONTRIBUTING.md)

---

## Development

### Build & Test

Clone the repository:

```bash
git clone https://github.dev/jcputney/elearning-module-parser.git
cd elearning-module-parser
```

Build the project:

```bash
mvn clean compile
```

Run tests:

```bash
mvn test
```

### Running Example Modules

You can find sample eLearning modules in the `resources/modules` directory for testing.

---

## Contributing

We welcome contributions! Please read our [Contributing Guidelines](CONTRIBUTING.md)
and [Code of Conduct](CODE_OF_CONDUCT.md) for details on how to participate in this project.

---

## License

This project is licensed under the LGPL v3.0 License. See the [LICENSE](LICENSE) file for details.