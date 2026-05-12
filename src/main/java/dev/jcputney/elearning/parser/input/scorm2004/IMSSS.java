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
package dev.jcputney.elearning.parser.input.scorm2004;

import java.io.Serializable;

/**
 * Represents the SCORM IMS Simple Sequencing (IMSSS) schema, which defines the sequencing and
 * navigation rules for SCORM-compliant content. This schema is designed to control the progression
 * of learners through SCORM packages based on defined conditions and constraints.
 *
 * <p>The IMSSS schema includes elements for setting control modes, defining rollup and sequencing
 * rules, establishing limit conditions, and configuring objectives, delivery, and randomization
 * controls.</p>
 *
 * <p>The IMSSS namespace is specified by {@link #NAMESPACE_URI}, following the SCORM 2004
 * standards.</p>
 *
 * @see <a href="https://www.imsglobal.org">IMS Global Learning Consortium</a>
 */
public final class IMSSS implements Serializable {

  /**
   * The XML namespace URI for SCORM IMS Simple Sequencing (imsss).
   */
  public static final String NAMESPACE_URI = "http://www.imsglobal.org/xsd/imsss";

  /**
   * Default constructor for IMSSS. This constructor is private to prevent instantiation.
   */
  private IMSSS() {
    // Prevent instantiation
  }

}
