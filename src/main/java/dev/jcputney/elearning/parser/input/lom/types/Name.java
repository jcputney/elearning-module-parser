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
