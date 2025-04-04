/*
 * Copyright (c) 2024-2025. Jonathan Putney
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

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ClasspathFileAccess;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.util.LoggingUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.slf4j.Logger;

/**
 * Base class for all benchmarks, providing common setup and teardown operations.
 */
@State(Scope.Thread)
public abstract class BaseBenchmark {

  private static final Logger log = LoggingUtils.getLogger(BaseBenchmark.class);

  protected FileAccess fileAccess;
  protected ModuleParserFactory parserFactory;
  protected Path tempDir;

  /**
   * Sets up the benchmark environment. This method is called before each benchmark iteration.
   *
   * @throws IOException if an I/O error occurs
   */
  @Setup(Level.Trial)
  public void setup() throws IOException {
    log.info("Setting up benchmark environment");

    // Create a temporary directory for the benchmark
    tempDir = Files.createTempDirectory("benchmark");
    log.debug("Created temporary directory: {}", tempDir);

    // Copy the test module to the temporary directory
    Path moduleFile = copyModuleToTempDir();

    // Create a FileAccess instance for the module
    fileAccess = createFileAccess(moduleFile);

    // Create a ModuleParserFactory instance
    parserFactory = new DefaultModuleParserFactory(fileAccess);

    log.info("Benchmark environment setup complete");
  }

  /**
   * Tears down the benchmark environment. This method is called after each benchmark iteration.
   *
   * @throws IOException if an I/O error occurs
   */
  @TearDown(Level.Trial)
  public void tearDown() throws IOException {
    log.info("Tearing down benchmark environment");

    // Delete the temporary directory and its contents
    if (tempDir != null) {
      deleteDirectory(tempDir.toFile());
      log.debug("Deleted temporary directory: {}", tempDir);
    }

    log.info("Benchmark environment teardown complete");
  }

  /**
   * Copies the test module to the temporary directory.
   *
   * @return the path to the copied module file
   * @throws IOException if an I/O error occurs
   */
  protected Path copyModuleToTempDir() throws IOException {
    String moduleResourcePath = getModuleResourcePath();
    Path moduleFile = tempDir.resolve(new File(moduleResourcePath).getName());

    try (var inputStream = getClass().getClassLoader().getResourceAsStream(moduleResourcePath)) {
      if (inputStream == null) {
        throw new IOException("Module resource not found: " + moduleResourcePath);
      }
      Files.copy(inputStream, moduleFile, StandardCopyOption.REPLACE_EXISTING);
      log.debug("Copied module to: {}", moduleFile);
    }

    return moduleFile;
  }

  /**
   * Creates a FileAccess instance for the module.
   *
   * @param moduleFile the path to the module file
   * @return a FileAccess instance
   * @throws IOException if an I/O error occurs
   */
  protected FileAccess createFileAccess(Path moduleFile) throws IOException {
    if (isZipModule()) {
      log.debug("Creating ZipFileAccess for: {}", moduleFile);
      return new ZipFileAccess(moduleFile.toString());
    } else {
      log.debug("Creating ClasspathFileAccess for module type: {}", getModuleType());
      return new ClasspathFileAccess("modules/" + getModuleType());
    }
  }

  /**
   * Returns the resource path to the module file.
   *
   * @return the resource path to the module file
   */
  protected abstract String getModuleResourcePath();

  /**
   * Returns the module type (e.g., "scorm12", "scorm2004", "aicc", "cmi5").
   *
   * @return the module type
   */
  protected abstract String getModuleType();

  /**
   * Returns whether the module is a ZIP file.
   *
   * @return true if the module is a ZIP file, false otherwise
   */
  protected abstract boolean isZipModule();

  /**
   * Recursively deletes a directory and its contents.
   *
   * @param directory the directory to delete
   */
  private void deleteDirectory(File directory) {
    if (directory.exists()) {
      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            deleteDirectory(file);
          } else {
            file.delete();
          }
        }
      }
      directory.delete();
    }
  }
}