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
   * Protected no-argument constructor for XapiMetadata.
   * <p>
   * This constructor is primarily used for internal and subclassing purposes. It initializes an
   * instance of XapiMetadata without setting a manifest or other properties.
   * <p>
   * Instances created using this constructor are expected to be configured manually or used in
   * specific scenarios where the default state is sufficient.
   */
  protected XapiMetadata() {
  }

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
