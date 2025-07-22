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