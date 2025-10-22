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

package dev.jcputney.elearning.parser.output.metadata.xapi;

import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;

/**
 * Metadata for xAPI/TinCan packaged modules.
 *
 * <p>TinCan packages contain a single activity and always have xAPI enabled.
 * They do not support multiple launchable units.</p>
 */
public class XapiMetadata extends ModuleMetadata<TincanManifest> {

  /**
   * Constructor for XapiMetadata.
   *
   * @param manifest the TinCan manifest
   */
  public XapiMetadata(TincanManifest manifest) {
    super(manifest, ModuleType.XAPI, ModuleEditionType.XAPI, true);
  }

  /**
   * TinCan packages always have a single activity, so this always returns false.
   *
   * @return false
   */
  @Override
  public boolean hasMultipleLaunchableUnits() {
    return false;
  }

  @Override
  public String getManifestFile() {
    return "tincan.xml";
  }
}
