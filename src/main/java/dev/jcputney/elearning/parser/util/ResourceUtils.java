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

package dev.jcputney.elearning.parser.util;

import dev.jcputney.elearning.parser.ModuleParser;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for managing resources safely, ensuring proper cleanup of ZipFileAccess and other
 * AutoCloseable resources.
 */
@Slf4j
public final class ResourceUtils {

  private ResourceUtils() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Parses a module from a ZIP file path, ensuring the ZipFileAccess is properly closed.
   *
   * @param <M> The type of package manifest
   * @param zipPath Path to the ZIP file
   * @param parserFactory Function to create a parser from FileAccess
   * @return The parsed module metadata
   * @throws ModuleParsingException if parsing fails
   * @throws IOException if file access fails
   */
  public static <M extends PackageManifest> ModuleMetadata<M> parseZipModule(
      String zipPath,
      Function<FileAccess, ModuleParser<M>> parserFactory)
      throws ModuleParsingException, IOException {

    try (ZipFileAccess zipAccess = new ZipFileAccess(zipPath)) {
      ModuleParser<M> parser = parserFactory.apply(zipAccess);
      return parser.parse();
    } catch (IOException e) {
      log.error("Failed to access ZIP file: {}", zipPath, e);
      throw e;
    } catch (ModuleParsingException e) {
      log.error("Failed to parse module from ZIP: {}", zipPath, e);
      throw e;
    }
  }

  /**
   * Safely reads content from an InputStream, ensuring it's closed properly.
   *
   * @param stream The input stream to read
   * @param reader Function to process the stream
   * @param <T> The return type
   * @return The result of the reader function
   * @throws IOException if reading fails
   */
  public static <T> T safeRead(InputStream stream, StreamReader<T> reader) throws IOException {
    if (stream == null) {
      throw new IllegalArgumentException("Stream cannot be null");
    }

    try (InputStream safeStream = stream) {
      return reader.read(safeStream);
    }
  }

  /**
   * Ensures a FileAccess instance is properly closed if it's AutoCloseable. This is useful for
   * conditional cleanup when the FileAccess type is unknown.
   *
   * @param fileAccess The FileAccess instance to potentially close
   */
  public static void closeIfCloseable(FileAccess fileAccess) {
    if (fileAccess instanceof AutoCloseable) {
      try {
        ((AutoCloseable) fileAccess).close();
      } catch (Exception e) {
        log.warn("Failed to close FileAccess resource", e);
      }
    }
  }

  /**
   * Functional interface for reading from a stream.
   *
   * @param <T> The return type
   */
  @FunctionalInterface
  public interface StreamReader<T> {

    /**
     * Reads data from the given input stream and converts it into an instance of type {@code T}.
     *
     * @param stream The input stream to read data from
     * @return An instance of type {@code T} created from the data read from the stream
     * @throws IOException If an I/O error occurs while reading the stream
     */
    T read(InputStream stream) throws IOException;
  }

  /**
   * Wraps a FileAccess to ensure streams are properly closed. This prevents resource leaks when
   * parsers don't close streams properly.
   */
  public static class SafeFileAccess implements FileAccess {

    private final FileAccess delegate;

    /**
     * Constructs a SafeFileAccess that wraps the provided FileAccess instance.
     *
     * @param delegate The FileAccess instance to wrap
     * @throws IllegalArgumentException if delegate is null
     */
    public SafeFileAccess(FileAccess delegate) {
      this.delegate = delegate;
    }

    /**
     * Gets the underlying FileAccess instance.
     *
     * @return The wrapped FileAccess instance
     */
    @Override
    public String getRootPath() {
      return delegate.getRootPath();
    }

    /**
     * Checks if a file exists at the specified path using the wrapped FileAccess implementation.
     *
     * @param path The path of the file to check.
     * @return True if the file exists at the specified path, false otherwise.
     */
    @Override
    public boolean fileExistsInternal(String path) {
      return delegate.fileExists(path);
    }

    /**
     * Lists all files in the specified directory path by delegating the operation to the underlying
     * FileAccess implementation.
     *
     * @param directoryPath The path of the directory from which files will be listed.
     * @return A list of file paths within the specified directory.
     * @throws IOException If an error occurs while accessing the directory or retrieving its
     * contents.
     */
    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      return delegate.listFiles(directoryPath);
    }

    /**
     * Retrieves the contents of a file as an InputStream while ensuring proper resource management.
     * This method wraps the returned InputStream in a {@code CloseLoggingInputStream} to log a
     * warning if the caller does not properly close the stream.
     *
     * @param path The path of the file to retrieve contents from. Must not be null.
     * @return An InputStream to read the file's contents.
     * @throws IOException If an error occurs while accessing the file.
     * @throws IllegalArgumentException If the provided path is null.
     */
    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      // Wrap the stream to log when it's not closed properly
      InputStream originalStream = delegate.getFileContents(path);
      return new CloseLoggingInputStream(originalStream, path);
    }
  }

  /**
   * InputStream wrapper that logs if the stream is not properly closed.
   */
  private static class CloseLoggingInputStream extends InputStream {

    private final InputStream delegate;
    private final String path;
    private boolean closed = false;

    CloseLoggingInputStream(InputStream delegate, String path) {
      this.delegate = delegate;
      this.path = path;
    }

    @Override
    public int read() throws IOException {
      return delegate.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
      return delegate.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      return delegate.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
      closed = true;
      delegate.close();
    }

    @Override
    protected void finalize() throws Throwable {
      if (!closed) {
        log.warn("InputStream for path '{}' was not properly closed!", path);
      }
      super.finalize();
    }
  }
}