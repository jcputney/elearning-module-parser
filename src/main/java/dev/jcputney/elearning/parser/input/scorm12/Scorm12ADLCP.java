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
