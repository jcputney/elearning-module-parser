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
package dev.jcputney.elearning.parser.api;

/**
 * Interface for listening to streaming progress events. Implementations can use this to track the
 * progress of streaming operations such as file downloads, uploads, or processing.
 */
public interface StreamingProgressListener {

  /**
   * Called periodically during a streaming operation to report progress.
   *
   * @param bytesRead The number of bytes read so far
   * @param totalSize The total size of the stream in bytes, or -1 if unknown
   */
  void onProgress(long bytesRead, long totalSize);

  /**
   * Called when the streaming operation is complete.
   *
   * @param totalBytesRead The total number of bytes that were read
   */
  void onComplete(long totalBytesRead);
}