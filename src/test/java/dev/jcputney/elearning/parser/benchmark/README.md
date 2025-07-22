# Performance Benchmarks for elearning-module-parser

This package contains JMH (Java Microbenchmark Harness) benchmarks for measuring the performance of
parsing operations in the elearning-module-parser library.

## Available Benchmarks

The following benchmark classes are available:

- `Scorm12Benchmark`: Benchmarks for SCORM 1.2 module parsing operations
- `Scorm2004Benchmark`: Benchmarks for SCORM 2004 module parsing operations
- `AiccBenchmark`: Benchmarks for AICC module parsing operations
- `Cmi5Benchmark`: Benchmarks for cmi5 module parsing operations

Each benchmark class includes the following benchmark methods:

- `parseModule()`: Benchmarks the parsing of a module
- `detectModuleType()`: Benchmarks the detection of a module type

## Running the Benchmarks

### Building the Benchmark JAR

To build the benchmark JAR, run the following command from the project root:

```bash
mvn clean package -P benchmark
```

This will create a JAR file named `benchmarks.jar` in the `target` directory.

### Running All Benchmarks

To run all benchmarks, execute the following command:

```bash
java -jar target/benchmarks.jar
```

### Running Specific Benchmarks

To run a specific benchmark class, use the following command:

```bash
java -jar target/benchmarks.jar <benchmark-class-name>
```

For example, to run the SCORM 1.2 benchmarks:

```bash
java -jar target/benchmarks.jar Scorm12Benchmark
```

To run a specific benchmark method, use the following command:

```bash
java -jar target/benchmarks.jar <benchmark-class-name>.<benchmark-method-name>
```

For example, to run the parseModule benchmark for SCORM 1.2:

```bash
java -jar target/benchmarks.jar Scorm12Benchmark.parseModule
```

### Customizing Benchmark Execution

JMH provides many options for customizing benchmark execution. Here are some common options:

- `-f <number>`: Number of forks (default: 1)
- `-i <number>`: Number of measurement iterations (default: 5)
- `-wi <number>`: Number of warmup iterations (default: 2)
- `-r <time>`: Minimum time per iteration (default: 1s)
- `-w <time>`: Minimum warmup time per iteration (default: 1s)
- `-o <filename>`: Write results to a file
- `-rf <format>`: Results format (default: text)

For example, to run the SCORM 1.2 benchmarks with 3 forks, 10 measurement iterations, and 5 warmup
iterations:

```bash
java -jar target/benchmarks.jar Scorm12Benchmark -f 3 -i 10 -wi 5
```

For more information on JMH options, run:

```bash
java -jar target/benchmarks.jar -h
```

## Interpreting Results

JMH reports benchmark results in the specified time unit (milliseconds by default). The results
include the following metrics:

- **Score**: The average time taken for each operation
- **Error**: The error margin for the score
- **Units**: The time unit for the score (ms = milliseconds)

Lower scores are better, indicating faster execution.

## Adding New Benchmarks

To add a new benchmark:

1. Create a new class that extends `BaseBenchmark`
2. Implement the required abstract methods:

- `getModuleResourcePath()`: Returns the resource path to the module file
- `getModuleType()`: Returns the module type (e.g., "scorm12", "scorm2004", "aicc", "cmi5")
- `isZipModule()`: Returns whether the module is a ZIP file

3. Add benchmark methods annotated with `@Benchmark`

Example:

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class MyBenchmark extends BaseBenchmark {

  private static final Logger log = LoggerFactory.getLogger(MyBenchmark.class);
  
  private static final String MODULE_TYPE = "mytype";
  private static final String MODULE_RESOURCE_PATH = "modules/mytype/mymodule.zip";
  
  @Override
  protected String getModuleResourcePath() {
    return MODULE_RESOURCE_PATH;
  }
  
  @Override
  protected String getModuleType() {
    return MODULE_TYPE;
  }
  
  @Override
  protected boolean isZipModule() {
    return true;
  }
  
  @Benchmark
  public void myBenchmarkMethod(Blackhole blackhole) {
    // Benchmark code here
    blackhole.consume(result);
  }
}
```
