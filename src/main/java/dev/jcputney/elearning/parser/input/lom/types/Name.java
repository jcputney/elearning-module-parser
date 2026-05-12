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
package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>name</code> in a technical element, specifying
 * the name of the platform or software requirements.
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="nameVocab">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="pc-dos"/>
 *     <xs:enumeration value="ms-windows"/>
 *     <xs:enumeration value="macos"/>
 *     <xs:enumeration value="unix"/>
 *     <xs:enumeration value="multi-os"/>
 *     <xs:enumeration value="none"/>
 *     <xs:enumeration value="any"/>
 *     <xs:enumeration value="netscape communicator"/>
 *     <xs:enumeration value="ms-internet explorer"/>
 *     <xs:enumeration value="opera"/>
 *     <xs:enumeration value="amaya"/>
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Name {
  /**
   * The "pc-dos" value specifies that the platform or software requirements are for PC-DOS.
   */
  @JsonProperty("pc-dos")
  PC_DOS,

  /**
   * The "ms-windows" value specifies that the platform or software requirements are for MS
   * Windows.
   */
  @JsonProperty("ms-windows")
  MS_WINDOWS,

  /**
   * The "macOS" value specifies that the platform or software requirements are for macOS.
   */
  @JsonProperty("macos")
  MACOS,

  /**
   * The "unix" value specifies that the platform or software requirements are for Unix.
   */
  @JsonProperty("unix")
  UNIX,

  /**
   * The "multi-os" value specifies that the platform or software requirements are for multiple
   * operating systems.
   */
  @JsonProperty("multi-os")
  MULTI_OS,

  /**
   * The "none" value specifies that there are no platform or software requirements.
   */
  @JsonProperty("none")
  NONE,

  /**
   * The "any" value specifies that the platform or software requirements are for any operating
   * system.
   */
  @JsonProperty("any")
  ANY,

  /**
   * The "netscape communicator" value specifies that the platform or software requirements are for
   * Netscape Communicator.
   */
  @JsonProperty("netscape communicator")
  NETSCAPE_COMMUNICATOR,

  /**
   * The "ms-internet explorer" value specifies that the platform or software requirements are for
   * MS Internet Explorer.
   */
  @JsonProperty("ms-internet explorer")
  MS_INTERNET_EXPLORER,

  /**
   * The "opera" value specifies that the platform or software requirements are for Opera.
   */
  @JsonProperty("opera")
  OPERA,

  /**
   * The "amaya" value specifies that the platform or software requirements are for Amaya.
   */
  @JsonProperty("amaya")
  AMAYA,

  /**
   * The "unknown" value specifies that the platform or software requirements are unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
