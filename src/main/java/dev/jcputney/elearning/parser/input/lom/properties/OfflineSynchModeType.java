/*
 * Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Enum representing the offlineSynchModeType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="offlineSynchModeType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="most recent" />
 *     <xs:enumeration value="simple" />
 *     <xs:enumeration value="most complete" />
 *     <xs:enumeration value="most satisfied" />
 *     <xs:enumeration value="most done" />
 *     <xs:enumeration value="best done " />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum OfflineSynchModeType {

  /**
   * The offline synchronization mode is the most recent.
   */
  @JsonProperty("most recent")
  MOST_RECENT,

  /**
   * The offline synchronization mode is straightforward.
   */
  @JsonProperty("simple")
  SIMPLE,

  /**
   * The offline synchronization mode is the most complete.
   */
  @JsonProperty("most complete")
  MOST_COMPLETE,

  /**
   * The offline synchronization mode is most satisfied.
   */
  @JsonProperty("most satisfied")
  MOST_SATISFIED,

  /**
   * The offline synchronization mode is most done.
   */
  @JsonProperty("most done")
  MOST_DONE,

  /**
   * The offline synchronization mode is best done.
   */
  @JsonProperty("best done")
  BEST_DONE
}