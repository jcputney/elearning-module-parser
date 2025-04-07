/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.exception;

/**
 * Exception thrown when there's a runtime error related to file access. This exception is a wrapper
 * for other exceptions that may occur during file access operations.
 */
public class RuntimeFileAccessException extends RuntimeException {

  /**
   * Constructs a new RuntimeFileAccessException with the specified detail message.
   *
   * @param cause the cause of the exception
   */
  public RuntimeFileAccessException(Exception cause) {
    super(cause);
  }
}
