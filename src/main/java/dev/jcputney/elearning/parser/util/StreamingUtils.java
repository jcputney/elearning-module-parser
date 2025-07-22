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

import dev.jcputney.elearning.parser.api.StreamingProgressListener;
import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for working with streaming file operations. Provides enhanced streaming
 * capabilities including progress tracking and buffering.
 */
public final class StreamingUtils {

  /**
   * Default buffer size for buffered streams (64KB).
   */
  public static final int DEFAULT_BUFFER_SIZE = 64 * 1024;

  /**
   * Default progress update interval in bytes (1MB).
   */
  public static final long DEFAULT_PROGRESS_INTERVAL = 1024 * 1024;

  private StreamingUtils() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Wraps an InputStream with buffering for improved performance.
   *
   * @param inputStream The input stream to wrap
   * @return A buffered input stream
   */
  public static InputStream createBufferedStream(InputStream inputStream) {
    return createBufferedStream(inputStream, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Wraps an InputStream with buffering for improved performance.
   *
   * @param inputStream The input stream to wrap
   * @param bufferSize The buffer size to use
   * @return A buffered input stream
   */
  public static InputStream createBufferedStream(InputStream inputStream, int bufferSize) {
    if (inputStream instanceof BufferedInputStream) {
      return inputStream; // Already buffered
    }
    return new BufferedInputStream(inputStream, bufferSize);
  }

  /**
   * Creates a progress-tracking input stream that reports progress at regular intervals.
   *
   * @param inputStream The input stream to wrap
   * @param totalSize The total size of the stream in bytes (or -1 if unknown)
   * @param listener The progress listener to notify
   * @return A progress-tracking input stream
   */
  public static InputStream createProgressTrackingStream(
      InputStream inputStream,
      long totalSize,
      StreamingProgressListener listener) {
    return createProgressTrackingStream(inputStream, totalSize, listener,
        DEFAULT_PROGRESS_INTERVAL);
  }

  /**
   * Creates a progress-tracking input stream with a custom progress interval.
   *
   * @param inputStream The input stream to wrap
   * @param totalSize The total size of the stream in bytes (or -1 if unknown)
   * @param listener The progress listener to notify
   * @param progressInterval The interval in bytes at which to report progress
   * @return A progress-tracking input stream
   */
  public static InputStream createProgressTrackingStream(
      InputStream inputStream,
      long totalSize,
      StreamingProgressListener listener,
      long progressInterval) {

    if (listener == null) {
      return inputStream; // No listener, no need to track
    }

    return new ProgressTrackingInputStream(inputStream, totalSize, listener, progressInterval);
  }

  /**
   * Creates a combined buffered and progress-tracking stream.
   *
   * @param inputStream The input stream to wrap
   * @param totalSize The total size of the stream in bytes (or -1 if unknown)
   * @param listener The progress listener to notify (can be null)
   * @return A buffered, progress-tracking input stream
   */
  public static InputStream createEnhancedStream(
      InputStream inputStream,
      long totalSize,
      StreamingProgressListener listener) {

    InputStream buffered = createBufferedStream(inputStream);
    if (listener != null) {
      return createProgressTrackingStream(buffered, totalSize, listener);
    }
    return buffered;
  }

  /**
   * Input stream wrapper that tracks reading progress and notifies a listener.
   */
  @SuppressWarnings("NullableProblems")
  private static class ProgressTrackingInputStream extends FilterInputStream {

    private final long totalSize;
    private final StreamingProgressListener listener;
    private final long progressInterval;
    private final AtomicLong bytesRead = new AtomicLong(0);
    private long lastProgressUpdate = 0;

    protected ProgressTrackingInputStream(
        InputStream in,
        long totalSize,
        StreamingProgressListener listener,
        long progressInterval) {
      super(in);
      this.totalSize = totalSize;
      this.listener = listener;
      this.progressInterval = progressInterval;
    }

    @Override
    public int read() throws IOException {
      int b = super.read();
      if (b != -1) {
        updateProgress(1);
      }
      return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      int bytesRead = super.read(b, off, len);
      if (bytesRead > 0) {
        updateProgress(bytesRead);
      }
      return bytesRead;
    }

    @Override
    public long skip(long n) throws IOException {
      long skipped = super.skip(n);
      if (skipped > 0) {
        updateProgress(skipped);
      }
      return skipped;
    }

    @Override
    public void close() throws IOException {
      try {
        // Notify completion
        long total = bytesRead.get();
        if (total > 0) {
          listener.onProgress(total, totalSize);
          listener.onComplete(total);
        }
      } catch (Exception e) {
        // Error notifying progress listener on close
      } finally {
        super.close();
      }
    }

    private void updateProgress(long bytes) {
      long total = bytesRead.addAndGet(bytes);

      // Check if we should report progress
      if (total - lastProgressUpdate >= progressInterval) {
        lastProgressUpdate = total;
        try {
          listener.onProgress(total, totalSize);
        } catch (Exception e) {
          // Error notifying progress listener
        }
      }
    }
  }
}