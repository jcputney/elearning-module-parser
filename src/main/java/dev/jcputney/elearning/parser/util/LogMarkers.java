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

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * SLF4J Markers for optional verbose logging in the e-learning parser library.
 * <p>
 * These markers allow consuming applications to selectively enable verbose logging for specific
 * operations without cluttering logs in normal operation.
 * <p>
 * Example usage in logback.xml to enable verbose logging:
 * <pre>
 * &lt;configuration&gt;
 *   &lt;turboFilter class="ch.qos.logback.classic.turbo.MarkerFilter"&gt;
 *     &lt;Marker&gt;PARSER_VERBOSE&lt;/Marker&gt;
 *     &lt;OnMatch&gt;ACCEPT&lt;/OnMatch&gt;
 *   &lt;/turboFilter&gt;
 * &lt;/configuration&gt;
 * </pre>
 */
public final class LogMarkers {

  /**
   * Marker for verbose parsing operations logging. Use this to log detailed parsing steps that are
   * normally too verbose.
   */
  public static final Marker PARSER_VERBOSE = MarkerFactory.getMarker("PARSER_VERBOSE");

  /**
   * Marker for verbose file access operations logging. Use this to log detailed file access
   * operations like cache hits/misses.
   */
  public static final Marker FILE_ACCESS_VERBOSE = MarkerFactory.getMarker("FILE_ACCESS_VERBOSE");

  /**
   * Marker for verbose XML processing logging. Use this to log detailed XML parsing and metadata
   * loading operations.
   */
  public static final Marker XML_VERBOSE = MarkerFactory.getMarker("XML_VERBOSE");

  /**
   * Marker for verbose S3 operations logging. Use this to log detailed S3 operations like
   * prefetching and streaming.
   */
  public static final Marker S3_VERBOSE = MarkerFactory.getMarker("S3_VERBOSE");

  private LogMarkers() {
    // Private constructor to prevent instantiation
  }
}