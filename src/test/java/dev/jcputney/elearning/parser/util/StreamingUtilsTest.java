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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for StreamingUtils functionality.
 */
class StreamingUtilsTest {

  @Test
  void testCreateBufferedStream() {
    byte[] data = "Test data for buffering".getBytes();
    InputStream originalStream = new ByteArrayInputStream(data);
    
    InputStream bufferedStream = StreamingUtils.createBufferedStream(originalStream);
    
    assertNotNull(bufferedStream);
    assertNotSame(originalStream, bufferedStream);
  }

  @Test
  void testCreateProgressTrackingStream() throws IOException {
    byte[] data = "Test data for progress tracking".getBytes();
    InputStream originalStream = new ByteArrayInputStream(data);
    
    AtomicLong totalBytesRead = new AtomicLong(0);
    AtomicLong completedBytes = new AtomicLong(0);
    
    StreamingProgressListener listener = new StreamingProgressListener() {
      @Override
      public void onProgress(long bytesRead, long totalSize) {
        totalBytesRead.set(bytesRead);
      }
      
      @Override
      public void onComplete(long totalBytesRead) {
        completedBytes.set(totalBytesRead);
      }
    };
    
    InputStream progressStream = StreamingUtils.createProgressTrackingStream(
        originalStream, data.length, listener);
    
    // Read all data
    byte[] buffer = new byte[1024];
    int totalRead = 0;
    int bytesRead;
    while ((bytesRead = progressStream.read(buffer)) != -1) {
      totalRead += bytesRead;
    }
    progressStream.close();
    
    assertEquals(data.length, totalRead);
    assertTrue(completedBytes.get() > 0);
  }

  @Test
  void testCreateEnhancedStream() {
    byte[] data = "Test data for enhanced streaming".getBytes();
    InputStream originalStream = new ByteArrayInputStream(data);
    
    InputStream enhancedStream = StreamingUtils.createEnhancedStream(
        originalStream, data.length, null);
    
    assertNotNull(enhancedStream);
  }

  @Test
  void testCreateEnhancedStreamWithProgressListener() throws IOException {
    byte[] data = "Test data for enhanced streaming with progress".getBytes();
    InputStream originalStream = new ByteArrayInputStream(data);
    
    AtomicLong progressCallCount = new AtomicLong(0);
    
    StreamingProgressListener listener = new StreamingProgressListener() {
      @Override
      public void onProgress(long bytesRead, long totalSize) {
        progressCallCount.incrementAndGet();
      }
      
      @Override
      public void onComplete(long totalBytesRead) {
        // Completion handler
      }
    };
    
    InputStream enhancedStream = StreamingUtils.createEnhancedStream(
        originalStream, data.length, listener);
    
    // Read all data
    byte[] buffer = new byte[1024];
    while (enhancedStream.read(buffer) != -1) {
      // Continue reading
    }
    enhancedStream.close();
    
    assertNotNull(enhancedStream);
  }

  @Test
  void testProgressTrackingWithNoListener() {
    byte[] data = "Test data".getBytes();
    InputStream originalStream = new ByteArrayInputStream(data);
    
    InputStream progressStream = StreamingUtils.createProgressTrackingStream(
        originalStream, data.length, null);
    
    // Should return the original stream when no listener is provided
    assertSame(originalStream, progressStream);
  }
}