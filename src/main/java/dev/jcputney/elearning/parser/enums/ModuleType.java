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
