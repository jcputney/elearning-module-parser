/* Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.benchmark;

import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.util.LoggingUtils;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.slf4j.Logger;

/**
 * Benchmark for cmi5 module parsing operations.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class Cmi5Benchmark extends BaseBenchmark {

  private static final Logger log = LoggingUtils.getLogger(Cmi5Benchmark.class);

  private static final String MODULE_TYPE = "cmi5";
  private static final String MODULE_RESOURCE_PATH = "modules/cmi5/cmi5.zip";

  /**
   * Benchmarks the parsing of a cmi5 module.
   *
   * @param blackhole a sink for benchmark results to prevent dead code elimination
   * @throws ModuleDetectionException if the module type cannot be determined
   * @throws ModuleParsingException if an error occurs during parsing
   */
  @Benchmark
  public void parseModule(Blackhole blackhole)
      throws ModuleDetectionException, ModuleParsingException {
    log.info("Benchmarking cmi5 module parsing");
    ModuleMetadata<?> metadata = parserFactory.parseModule();
    blackhole.consume(metadata);
  }

  /**
   * Benchmarks the detection of a cmi5 module type.
   *
   * @param blackhole a sink for benchmark results to prevent dead code elimination
   * @throws ModuleDetectionException if the module type cannot be determined
   */
  @Benchmark
  public void detectModuleType(Blackhole blackhole) throws ModuleDetectionException {
    log.info("Benchmarking cmi5 module type detection");
    blackhole.consume(parserFactory.getParser());
  }

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
}