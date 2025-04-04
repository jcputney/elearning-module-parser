/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>copyrightAndOtherRestrictions</code> in a LOM
 * element, specifying the copyright and other restrictions associated with the learning object. The
 * following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name="copyrightAndOtherRestrictions">
 *    <xs:restriction base="xs:token">
 *      <xs:enumeration value="yes"/>
 *      <xs:enumeration value="no"/>
 *    </xs:restriction>
 *  </xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum CopyrightAndOtherRestrictions {
  /**
   * The learning object has copyright and other restrictions.
   */
  @JsonProperty("yes")
  YES,

  /**
   * The learning object does not have copyright and other restrictions.
   */
  @JsonProperty("no")
  NO,

  /**
   * The copyright and other restrictions of the learning object are unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
