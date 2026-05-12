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
package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible launch type for the PackageProperties.
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 *  <xs:simpleType name="launchType">
 * 		<xs:restriction base="xs:string">
 * 			<xs:enumeration value="frameset" />
 * 			<xs:enumeration value="new window" />
 * 			<xs:enumeration value="new window,after click" />
 * 			<xs:enumeration value="new window without browser toolbar" />
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum LaunchType {
  /**
   * The learning object is launched in a frameset.
   */
  @JsonProperty("frameset")
  FRAMESET,

  /**
   * The learning object is launched in a new window.
   */
  @JsonProperty("new window")
  NEW_WINDOW,

  /**
   * The learning object is launched in a new window after a click.
   */
  @JsonProperty("new window,after click")
  NEW_WINDOW_AFTER_CLICK,

  /**
   * The learning object is launched in a new window without the browser toolbar.
   */
  @JsonProperty("new window without browser toolbar")
  NEW_WINDOW_WITHOUT_BROWSER_TOOLBAR
}
