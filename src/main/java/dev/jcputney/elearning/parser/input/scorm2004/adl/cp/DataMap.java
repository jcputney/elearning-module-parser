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

package dev.jcputney.elearning.parser.input.scorm2004.adl.cp;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the map element, specifying shared data configuration.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DataMap {

  /**
   * Target ID URI for shared data mapping.
   */
  @JacksonXmlProperty(localName = "targetID", isAttribute = true)
  @JsonProperty("targetID")
  private String targetID;
  /**
   * Indicates if shared data is readable.
   */
  @JacksonXmlProperty(localName = "readSharedData", isAttribute = true)
  @JsonProperty("readSharedData")
  @Default
  private boolean readSharedData = true;
  /**
   * Indicates if shared data is writable.
   */
  @JacksonXmlProperty(localName = "writeSharedData", isAttribute = true)
  @JsonProperty("writeSharedData")
  @Default
  private boolean writeSharedData = false;

  /**
   * Default constructor for the DataMap class.
   */
  @SuppressWarnings("unused")
  public DataMap() {
    // Default constructor
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DataMap dataMap = (DataMap) o;

    return new EqualsBuilder()
        .append(readSharedData, dataMap.readSharedData)
        .append(writeSharedData, dataMap.writeSharedData)
        .append(targetID, dataMap.targetID)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(targetID)
        .append(readSharedData)
        .append(writeSharedData)
        .toHashCode();
  }
}
