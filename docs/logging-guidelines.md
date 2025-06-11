# Logging Guidelines for E-Learning Module Parser Library

## Overview

This library uses SLF4J for logging, allowing consumers to use their preferred logging implementation (Logback, Log4j2, etc.). The library follows best practices for minimal and appropriate logging in a library context.

## Logging Principles

1. **No Default Configuration**: The library does not include any logging configuration files in the production JAR to avoid conflicts with consumer applications.

2. **Minimal Logging**: By default, only ERROR and WARN level messages are logged for critical issues that users need to know about.

3. **Optional Verbose Logging**: Detailed logging is available using SLF4J markers for users who need to debug specific operations.

## Log Levels

- **ERROR**: Used only for unrecoverable errors that prevent operations from completing
- **WARN**: Used sparingly for issues that might affect functionality but don't prevent operation
- **INFO**: Reserved for significant lifecycle events (avoided in normal operations)
- **DEBUG**: Detailed operational information (disabled by default, available via markers)

## SLF4J Markers for Verbose Logging

The library provides several markers for enabling verbose logging of specific operations:

- `PARSER_VERBOSE`: Detailed parsing operations
- `FILE_ACCESS_VERBOSE`: File access and caching details
- `XML_VERBOSE`: XML parsing and metadata loading
- `S3_VERBOSE`: S3-specific operations (prefetching, streaming, etc.)

### Example: Enabling Verbose Logging with Logback

To enable verbose logging for specific operations in your application:

```xml
<configuration>
  <!-- Enable verbose parser logging -->
  <turboFilter class="ch.qos.logback.classic.turbo.MarkerFilter">
    <Marker>PARSER_VERBOSE</Marker>
    <OnMatch>ACCEPT</OnMatch>
  </turboFilter>
  
  <!-- Enable S3 operation logging -->
  <turboFilter class="ch.qos.logback.classic.turbo.MarkerFilter">
    <Marker>S3_VERBOSE</Marker>
    <OnMatch>ACCEPT</OnMatch>
  </turboFilter>
  
  <!-- Your appenders here -->
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>
  
  <!-- Set library log level -->
  <logger name="dev.jcputney.elearning.parser" level="DEBUG"/>
  
  <root level="INFO">
    <appender-ref ref="STDOUT"/>
  </root>
</configuration>
```

### Example: Enabling Verbose Logging with Log4j2

```xml
<Configuration>
  <Appenders>
    <Console name="Console" target="SYSTEM_OUT">
      <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
    </Console>
  </Appenders>
  
  <Loggers>
    <!-- Enable verbose logging for specific markers -->
    <Logger name="dev.jcputney.elearning.parser" level="DEBUG">
      <MarkerFilter marker="PARSER_VERBOSE" onMatch="ACCEPT" onMismatch="DENY"/>
      <MarkerFilter marker="S3_VERBOSE" onMatch="ACCEPT" onMismatch="DENY"/>
    </Logger>
    
    <Root level="INFO">
      <AppenderRef ref="Console"/>
    </Root>
  </Loggers>
</Configuration>
```

## Recommended Configuration for Production

For production use, we recommend:

```xml
<!-- Logback example -->
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>
  
  <!-- Only show warnings and errors from the library -->
  <logger name="dev.jcputney.elearning.parser" level="WARN"/>
  
  <root level="INFO">
    <appender-ref ref="STDOUT"/>
  </root>
</configuration>
```

## Troubleshooting

If you need to debug parsing issues:

1. Enable DEBUG level for the library logger
2. Enable specific markers for the operations you're investigating
3. Review the detailed logs to identify the issue
4. Remember to disable verbose logging in production

## Development and Testing

For development and testing, a `logback-test.xml` file is included in the test resources but is not packaged in the production JAR.