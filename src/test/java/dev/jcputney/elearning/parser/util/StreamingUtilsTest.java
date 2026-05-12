/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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