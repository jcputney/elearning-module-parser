# eLearning Module Parser

[![Build Status](https://img.shields.io/github/actions/workflow/status/jcputney/elearning-module-parser/snapshot.yml)](https://github.com/jcputney/elearning-module-parser/actions)
[![License: LGPL-3.0](https://img.shields.io/badge/license-LGPL%203.0-blue.svg)](https://opensource.org/license/lgpl-3-0)
[![Version](https://img.shields.io/maven-central/v/dev.jcputney/elearning-module-parser)](https://search.maven.org/search?q=g:dev.jcputney%20a:elearning-module-parser)
[![Java 17+](https://img.shields.io/badge/java-17%2B-blue.svg)](https://adoptium.net/)

A comprehensive Java library for parsing and validating eLearning module manifests in industry-standard formats including SCORM 1.2, SCORM 2004 (all editions), AICC, and cmi5. This library provides a unified API for integrating eLearning content into Learning Management Systems (LMS), content repositories, and educational platforms.

## Table of Contents

- [Features](#features)
- [Quick Start](#quick-start)
- [Installation](#installation)
- [Usage Examples](#usage-examples)
- [Supported Formats](#supported-formats)
- [Architecture](#architecture)
- [API Documentation](#api-documentation)
- [Advanced Usage](#advanced-usage)
- [Performance](#performance)
- [Contributing](#contributing)
- [License](#license)

## Features

### Core Capabilities
- **Automatic Format Detection**: Automatically detects module type (SCORM, AICC, cmi5) without manual specification
- **Unified API**: Consistent interface across all eLearning standards with normalized metadata output
- **Multiple Storage Backends**: Support for local files, ZIP archives, AWS S3 (v1 & v2), and custom storage implementations
- **Comprehensive Parsing**: Full support for manifests, metadata, sequencing rules, and course structure
- **Performance Optimized**: Built-in caching, batch operations, and streaming support for large modules
- **Extensible Architecture**: Plugin system for custom module types and storage backends
- **Production Ready**: Robust error handling, comprehensive logging, and extensive test coverage

### Storage Options
- **Local File System**: Parse extracted modules directly from disk
- **ZIP Archives**: Parse modules directly from ZIP files without extraction
- **AWS S3**: Native support for modules stored in S3 buckets (both SDK v1 and v2)
- **Cached Access**: Wrapper for any storage backend to improve performance
- **Custom Backends**: Implement `FileAccess` interface for custom storage solutions

### Standards Compliance
- Full adherence to official specifications for each supported format
- Validation of required elements and structure
- Support for all standard metadata fields
- Handling of vendor-specific extensions

## Quick Start

```java
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

// Parse a module from a ZIP file
try (ZipFileAccess zipAccess = new ZipFileAccess("path/to/module.zip")) {
    ModuleParserFactory factory = new DefaultModuleParserFactory(zipAccess);
    ModuleMetadata<?> metadata = factory.parseModule();
    
    System.out.println("Module Type: " + metadata.getModuleType());
    System.out.println("Title: " + metadata.getTitle());
    System.out.println("Launch URL: " + metadata.getLaunchUrl());
}
```

## Installation

### Requirements
- Java 17 or higher
- Maven 3.6+ or Gradle 7.0+ (optional - Maven Wrapper included)

### Maven
```xml
<dependency>
    <groupId>dev.jcputney</groupId>
    <artifactId>elearning-module-parser</artifactId>
    <version>0.0.15</version>
</dependency>
```

### Gradle
```kotlin
implementation("dev.jcputney:elearning-module-parser:0.0.15")
```

### Optional Dependencies

For AWS S3 support, add one of the following:

#### AWS SDK v2 (Recommended)
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.32.5</version>
</dependency>
```

#### AWS SDK v1
```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-s3</artifactId>
    <version>1.12.788</version>
</dependency>
```

## Usage Examples

### Basic Module Parsing

```java
// Auto-detect and parse any supported module type
ModuleParserFactory factory = new DefaultModuleParserFactory(fileAccess);
ModuleParser<?> parser = factory.getParser(); // Auto-detects type
ModuleMetadata<?> metadata = parser.parse();

// Access common metadata
System.out.println("Title: " + metadata.getTitle());
System.out.println("Description: " + metadata.getDescription());
System.out.println("Version: " + metadata.getVersion());
System.out.println("Launch URL: " + metadata.getLaunchUrl());
System.out.println("Duration: " + metadata.getDuration());
System.out.println("Total Size: " + metadata.getSizeOnDisk() + " bytes");
```

### Parsing from Different Sources

#### ZIP Files
```java
try (ZipFileAccess zipAccess = new ZipFileAccess("module.zip")) {
    ModuleParserFactory factory = new DefaultModuleParserFactory(zipAccess);
    ModuleMetadata<?> metadata = factory.parseModule();
}
```

#### Local Directory
```java
LocalFileAccess localAccess = new LocalFileAccess("/path/to/extracted/module");
ModuleParserFactory factory = new DefaultModuleParserFactory(localAccess);
ModuleMetadata<?> metadata = factory.parseModule();
```

#### AWS S3
```java
// Using AWS SDK v2
S3Client s3Client = S3Client.builder()
    .region(Region.US_EAST_1)
    .credentialsProvider(DefaultCredentialsProvider.create())
    .build();

S3FileAccessV2 s3Access = new S3FileAccessV2(s3Client, "my-bucket", "modules/scorm/");
ModuleParserFactory factory = new DefaultModuleParserFactory(s3Access);
ModuleMetadata<?> metadata = factory.parseModule();
```

### Handling Module-Specific Metadata

```java
ModuleMetadata<?> metadata = parser.parse();

// Check module type and cast to specific metadata class
switch (metadata.getModuleType()) {
    case SCORM_12:
        Scorm12Metadata scorm12 = (Scorm12Metadata) metadata;
        // Access SCORM 1.2 specific features
        List<Scorm12Resource> resources = scorm12.getManifest()
            .getResources().getResourceList();
        break;
        
    case SCORM_2004:
        Scorm2004Metadata scorm2004 = (Scorm2004Metadata) metadata;
        // Access SCORM 2004 specific features like sequencing
        System.out.println("Edition: " + metadata.getModuleEditionType());
        break;
        
    case AICC:
        AiccMetadata aicc = (AiccMetadata) metadata;
        // Access AICC specific features
        break;
        
    case CMI5:
        Cmi5Metadata cmi5 = (Cmi5Metadata) metadata;
        // Access cmi5 specific features
        break;
}
```

SCORM 2004 modules expose ADL/IMSSS details through typed accessors on
`Scorm2004Metadata` (for example `getControlModes()`, `getCompletionThresholds()` and
`getHideLmsUi()`). Delivery controls are available directly on the metadata instance:

```java
Scorm2004Metadata scorm2004 = (Scorm2004Metadata) metadata;
Map<String, DeliveryControls> controlsByActivity = scorm2004.getActivityDeliveryControls();
boolean overridesDefaults = scorm2004.overridesDeliveryControlDefaults("item_1");
```

Other module types expose their rich metadata in a similar fashion:

- `Cmi5Metadata` → assignable unit details via `getAuDetails()`, mastery scores via
  `getMasteryScores()`, etc.
- `AiccMetadata` → prerequisites summaries via `getPrerequisitesGraph()` and
  `getPrerequisitesEdgeCount()`.
- `Scorm12Metadata` → item-level data through `getPrerequisites()`, `getMasteryScores()` and
  `getCustomData()`.

Every activity appears in the map with resolved `DeliveryControls`, honoring SCORM defaults when no
modifiers are defined in the manifest (`tracked` defaults to `true`, completion/objective flags
default to `false`). Use `getActivitiesOverridingDeliveryControlDefaults()` or
`overridesDeliveryControlDefaults(String)` to discover which activities explicitly set delivery
controls in the manifest.

### Error Handling

```java
try {
    ModuleMetadata<?> metadata = factory.parseModule();
} catch (ModuleDetectionException e) {
    // Module type could not be detected
    logger.error("Unable to detect module type: {}", e.getMessage());
} catch (ManifestParseException e) {
    // Manifest XML/structure is invalid
    logger.error("Invalid manifest: {}", e.getMessage());
} catch (ModuleParsingException e) {
    // General parsing error
    logger.error("Parsing failed: {}", e.getMessage());
} catch (FileAccessException e) {
    // File system or network error
    logger.error("File access error: {}", e.getMessage());
}
```

### Using Event Listeners

```java
// Implement parsing event listener for diagnostics
ParsingEventListener listener = new ParsingEventListener() {
    @Override
    public void onDetectionStarted() {
        System.out.println("Starting module detection...");
    }
    
    @Override
    public void onModuleTypeDetected(ModuleType type) {
        System.out.println("Detected module type: " + type);
    }
    
    @Override
    public void onParsingProgress(String stage, int percentage) {
        System.out.println("Parsing " + stage + ": " + percentage + "%");
    }
    
    @Override
    public void onParsingCompleted() {
        System.out.println("Parsing completed successfully");
    }
};

// Use with parser
Scorm2004Parser parser = new Scorm2004Parser(fileAccess, listener);
```

## Supported Formats

### SCORM 1.2
- **Features**: Basic content packaging, prerequisites, mastery scores, hierarchical organization
- **Metadata**: Title, description, launch URL, custom LMS data, duration
- **Files**: `imsmanifest.xml` with IMS CP 1.1.2 namespace
- **Detection**: Presence of `imsmanifest.xml` with SCORM 1.2 namespace

### SCORM 2004 (All Editions)
- **Editions**: 
  - 2nd Edition (CAM 1.3)
  - 3rd Edition
  - 4th Edition
  - Generic (unspecified edition)
- **Features**: 
  - Advanced sequencing and navigation (IMS SS)
  - Completion thresholds and progress tracking
  - Global objectives for cross-SCO communication
  - Activity trees and rollup rules
  - Control modes and navigation restrictions
  - Randomization and selection controls
- **Files**: `imsmanifest.xml` with SCORM 2004 namespaces
- **Detection**: Namespace and schema version analysis

### AICC
- **Features**: Legacy INI-based format, CSV course structure, assignable units
- **Files**: 
  - `.crs` - Course descriptor (INI format)
  - `.des` - Descriptor file (CSV)
  - `.au` - Assignable Unit file (CSV)
  - `.cst` - Course Structure file (CSV)
- **Detection**: Presence of `.crs` file with `[Course]` section

### cmi5
- **Features**: 
  - Modern xAPI-based standard
  - Flexible course structure with blocks and AUs
  - Move-on criteria (Passed, Completed, etc.)
  - Multi-language support
  - Launch methods and parameters
- **Files**: `cmi5.xml` course structure
- **Detection**: Presence of `cmi5.xml` file

## Architecture

### Design Patterns

1. **Factory Pattern**: `ModuleParserFactory` creates appropriate parsers based on detected module type
2. **Strategy Pattern**: `FileAccess` implementations for different storage backends
3. **Plugin Architecture**: Extensible module type detection via `ModuleTypeDetectorPlugin`
4. **Builder Pattern**: Immutable metadata objects with fluent builders
5. **Observer Pattern**: Event listeners for parsing diagnostics

### Key Components

```
┌─────────────────────┐
│ ModuleParserFactory │
└──────────┬──────────┘
           │ creates
           ▼
┌─────────────────────┐     ┌──────────────────┐
│    ModuleParser     │────▶│  ModuleMetadata  │
└──────────┬──────────┘     └──────────────────┘
           │ uses
           ▼
┌─────────────────────┐     ┌──────────────────┐
│     FileAccess      │     │ModuleTypeDetector│
└─────────────────────┘     └──────────────────┘
```

### Package Structure

```
dev.jcputney.elearning.parser
├── api/                    # Core interfaces
├── parsers/                # Parser implementations
├── input/                  # Input models by format
│   ├── scorm12/
│   ├── scorm2004/
│   ├── aicc/
│   └── cmi5/
├── output/                 # Unified output models
├── impl/                   # Interface implementations
├── util/                   # Utilities
└── exception/              # Custom exceptions
```

## API Documentation

### Core Interfaces

#### ModuleParser
```java
public interface ModuleParser<M extends PackageManifest> {
    ModuleMetadata<M> parse() throws ModuleParsingException;
}
```

#### FileAccess
```java
public interface FileAccess extends AutoCloseable {
    boolean fileExists(String path);
    InputStream getFileContents(String path) throws IOException;
    List<String> listFiles(String directory);
    long getTotalSize();
}
```

#### ModuleParserFactory
```java
public interface ModuleParserFactory {
    ModuleParser<?> getParser() throws ModuleDetectionException;
    ModuleMetadata<?> parseModule() throws ModuleException;
}
```

### Metadata Classes

All module types produce metadata extending `ModuleMetadata`:
- `Scorm12Metadata` - SCORM 1.2 specific metadata
- `Scorm2004Metadata` - SCORM 2004 specific metadata
- `AiccMetadata` - AICC specific metadata
- `Cmi5Metadata` - cmi5 specific metadata

## Advanced Usage

### Performance Optimization

```java
// Use caching for better performance
FileAccess baseAccess = new ZipFileAccess("module.zip");
CachedFileAccess cachedAccess = new CachedFileAccess(baseAccess);

// Pre-fetch common files
ModuleFileProvider fileProvider = new DefaultModuleFileProvider(cachedAccess);
fileProvider.prefetchCommonFiles();
```

### Custom Module Type Detection

```java
// Create custom detector plugin
ModuleTypeDetectorPlugin customPlugin = new ModuleTypeDetectorPlugin() {
    @Override
    public int getPriority() {
        return 100; // Higher = checked first
    }
    
    @Override
    public ModuleType detect(FileAccess fileAccess) {
        if (fileAccess.fileExists("custom-manifest.xml")) {
            return ModuleType.CUSTOM;
        }
        return null;
    }
};

// Register plugin
DefaultModuleTypeDetector detector = new DefaultModuleTypeDetector(fileAccess);
detector.registerPlugin(customPlugin);
```

### Streaming Large Modules

```java
// Use progress listener for large modules
StreamingProgressListener progressListener = new StreamingProgressListener() {
    @Override
    public void onProgress(long bytesRead, long totalSize) {
        int percentage = (int) ((bytesRead * 100) / totalSize);
        System.out.println("Progress: " + percentage + "%");
    }
};

// Apply to S3 downloads
S3FileAccessV2 s3Access = new S3FileAccessV2(s3Client, bucket, prefix);
s3Access.setProgressListener(progressListener);
```

## Performance

### Benchmarks

The library includes JMH benchmarks for performance testing:

```bash
# Run all benchmarks
./mvnw clean test-compile exec:java -P benchmark

# Run specific benchmark
./mvnw clean test-compile exec:java -P benchmark -Djmh.options="Scorm2004Benchmark"

# Run with custom iterations
./mvnw clean test-compile exec:java -P benchmark -Djmh.options="-wi 5 -i 10 -f 2"
```

### Performance Tips

1. **Use CachedFileAccess** for modules accessed multiple times
2. **Pre-fetch common files** when processing many modules
3. **Use batch operations** when checking multiple files
4. **Enable parallel processing** for independent operations
5. **Configure appropriate timeouts** for network storage

## Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Development Setup

```bash
# Clone repository
git clone https://github.com/jcputney/elearning-module-parser.git
cd elearning-module-parser

# Build project
./mvnw clean compile

# Run tests
./mvnw test

# Run integration tests
./mvnw verify

# Generate code coverage report
./mvnw test jacoco:report
# Report available at: target/site/jacoco/index.html
```

### Code Style

The project uses:
- Google Java Style Guide
- Lombok for boilerplate reduction
- AssertJ for test assertions
- SLF4J for logging

## License

This project is licensed under the GNU Lesser General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- SCORM specifications by ADL (Advanced Distributed Learning)
- AICC specifications by Aviation Industry CBT Committee
- cmi5 specification by ADL
- IMS Global Learning Consortium for content packaging standards
