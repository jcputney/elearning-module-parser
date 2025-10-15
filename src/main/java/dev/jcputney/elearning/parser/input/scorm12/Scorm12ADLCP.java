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

package dev.jcputney.elearning.parser.input.scorm12;

import java.io.Serializable;

/**
 * This class contains constants and utility methods for handling SCORM 1.2 ADLCP elements. It isn't
 * intended to be instantiated.
 */
public final class Scorm12ADLCP implements Serializable {

  /**
   * The namespace URI for SCORM 1.2 ADLCP elements.
   */
  public static final String NAMESPACE_URI = "http://www.adlnet.org/xsd/adlcp_rootv1p2";

  /**
   * Default constructor for the Scorm12ADLCP class. This constructor is private to prevent
   * instantiation.
   */
  private Scorm12ADLCP() {
    throw new IllegalStateException("Utility class");
  }

}
