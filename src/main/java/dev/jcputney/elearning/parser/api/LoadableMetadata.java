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

import dev.jcputney.elearning.parser.input.lom.LOM;

/**
 * Interface representing metadata that can be loaded.
 * <p>
 * This interface provides methods to get the location of the metadata and to get and set the LOM
 * (Learning Object Metadata) object associated with it.
 * </p>
 */
public interface LoadableMetadata {

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
