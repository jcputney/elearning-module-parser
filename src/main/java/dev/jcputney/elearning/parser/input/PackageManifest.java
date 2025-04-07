/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input;

import java.time.Duration;

/**
 * Represents the manifest of a package, containing metadata about the package. This interface
 * defines methods to retrieve various attributes of the package manifest.
 */
public interface PackageManifest {

  /**
   * The title of the package, which is a human-readable name.
   *
   * @return the title of the package
   */
  String getTitle();

  /**
   * The description of the package, which provides additional information about it.
   *
   * @return the description of the package
   */
  String getDescription();

  /**
   * The launch URL of the package, which is the URL to start the package.
   *
   * @return the launch URL of the package
   */
  String getLaunchUrl();

  /**
   * The identifier of the package, which is a unique string that identifies the package.
   *
   * @return the identifier of the package
   */
  String getIdentifier();

  /**
   * The version of the package.
   *
   * @return the version of the package
   */
  String getVersion();

  /**
   * The duration of the package, which indicates how long the package is expected to take.
   *
   * @return the duration of the package
   */
  Duration getDuration();
}
