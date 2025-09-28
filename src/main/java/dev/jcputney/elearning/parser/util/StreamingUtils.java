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
  public static final long DEFAULT_PROGRESS_INTERVAL = 1024 * 1024L;

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

    /**
     * The total size of the data to be read from the input stream. Used for tracking progress and
     * notifying the listener of completion and updates.
     */
    private final long totalSize;

    /**
     * Listener to track the progress of data being read from the input stream. The listener is
     * periodically notified of the progress through the
     * {@link StreamingProgressListener#onProgress(long, long)} method. It is also notified upon
     * completion of the streaming operation through the
     * {@link StreamingProgressListener#onComplete(long)} method. Uses the
     * {@link StreamingProgressListener} interface to handle progress updates and completion
     * events.
     */
    private final StreamingProgressListener listener;

    /**
     * Specifies the interval, in bytes, at which progress updates are triggered. This value
     * determines the frequency with which the associated {@link StreamingProgressListener} will be
     * notified of the progress while reading data from the input stream. A higher value reduces the
     * frequency of updates, while a lower value increases it. Used to control the performance
     * impact of frequent notifications during streaming.
     */
    private final long progressInterval;

    /**
     * Tracks the total number of bytes read from the input stream. This variable is incremented
     * every time bytes are successfully read or skipped, and is used to provide progress updates to
     * the associated listener. It is initialized to 0 and updated atomically to ensure thread
     * safety in concurrent access scenarios.
     */
    private final AtomicLong bytesRead = new AtomicLong(0);

    /**
     * Tracks the timestamp of the most recent progress update. This value is measured in
     * milliseconds since the epoch. It is used to determine when to trigger the next progress
     * update based on the configured progress interval.
     */
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

    /**
     * Reads the next byte of data from the input stream. Updates the progress based on the number
     * of bytes read.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
      int b = super.read();
      if (b != -1) {
        updateProgress(1);
      }
      return b;
    }

    /**
     * Reads up to a specified number of bytes from the input stream into an array of bytes,
     * starting at the specified offset. Updates the progress based on the number of bytes read.
     *
     * @param b the byte array into which data is read
     * @param off the start offset in the array at which to write data
     * @param len the maximum number of bytes to read
     * @return the total number of bytes read into the buffer, or -1 if there is no more data
     * because the end of the stream has been reached
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      int readBytes = super.read(b, off, len);
      if (readBytes > 0) {
        updateProgress(readBytes);
      }
      return readBytes;
    }

    /**
     * Skips over and discards a specified number of bytes from this input stream while updating the
     * progress based on the number of bytes skipped.
     *
     * @param n the number of bytes to skip
     * @return the actual number of bytes skipped
     * @throws IOException if an I/O error occurs
     */
    @Override
    public long skip(long n) throws IOException {
      long skipped = super.skip(n);
      if (skipped > 0) {
        updateProgress(skipped);
      }
      return skipped;
    }

    /**
     * Closes the input stream, ensuring any necessary cleanup is performed and notifying the
     * progress listener of the completion of the stream processing.
     * <p>
     * If the total number of bytes read is greater than 0, the progress listener will be updated
     * with the current progress and notified of the completion event.
     * <p>
     * Any exceptions thrown while notifying the progress listener are caught and ignored to ensure
     * the stream is closed properly. The parent class's close method is invoked in a `finally`
     * block to ensure resource cleanup even in the case of errors.
     *
     * @throws IOException if an I/O error occurs during closing
     */
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

    /**
     * Updates the progress of the input stream based on the number of bytes processed. If the
     * accumulated bytes since the last progress update exceeds the specified interval, this method
     * notifies the progress listener with the current progress.
     *
     * @param bytes the number of bytes processed since the last method invocation
     */
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