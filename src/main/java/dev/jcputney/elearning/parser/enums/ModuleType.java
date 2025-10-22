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

package dev.jcputney.elearning.parser.enums;

/**
 * Enum representing the different types of modules.
 * <p>
 * This enum defines the possible module types that can be used in the system.
 * </p>
 */
public enum ModuleType {
  /**
   * SCORM 1.2 module type.
   */
  SCORM_12,

  /**
   * SCORM 2004 module type.
   */
  SCORM_2004,

  /**
   * AICC (Aviation Industry Computer-Based Training Committee) module type.
   */
  AICC,

  /**
   * cmi5 (xAPI for cmi5) module type.
   */
  CMI5,

  /**
   * xAPI/TinCan module type.
   */
  XAPI,
}
