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

import dev.jcputney.elearning.parser.input.lom.LOM;
import java.io.Serializable;

/**
 * Interface representing metadata that can be loaded.
 * <p>
 * This interface provides methods to get the location of the metadata and to get and set the LOM
 * (Learning Object Metadata) object associated with it.
 * </p>
 */
public interface LoadableMetadata extends Serializable {

  /**
   * Gets the location of the metadata.
   *
   * @return the location of the metadata as a String.
   */
  String getLocation();

  /**
   * Gets the LOM (Learning Object Metadata) object associated with this metadata.
   *
   * @return the LOM object.
   */
  LOM getLom();

  /**
   * Sets the LOM (Learning Object Metadata) object associated with this metadata.
   *
   * @param lom the LOM object to set.
   */
  void setLom(LOM lom);
}
