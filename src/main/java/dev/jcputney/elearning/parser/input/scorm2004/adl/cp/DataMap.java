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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the map element, specifying shared data configuration.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DataMap implements Serializable {

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
  private boolean readSharedData = true;
  /**
   * Indicates if shared data is writable.
   */
  @JacksonXmlProperty(localName = "writeSharedData", isAttribute = true)
  @JsonProperty("writeSharedData")
  private boolean writeSharedData = false;

  public DataMap() {
  }

  public String getTargetID() {
    return this.targetID;
  }

  public void setTargetID(String targetID) {
    this.targetID = targetID;
  }

  public boolean isReadSharedData() {
    return this.readSharedData;
  }

  public void setReadSharedData(boolean readSharedData) {
    this.readSharedData = readSharedData;
  }

  public boolean isWriteSharedData() {
    return this.writeSharedData;
  }

  public void setWriteSharedData(boolean writeSharedData) {
    this.writeSharedData = writeSharedData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DataMap dataMap)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isReadSharedData(), dataMap.isReadSharedData())
        .append(isWriteSharedData(), dataMap.isWriteSharedData())
        .append(getTargetID(), dataMap.getTargetID())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTargetID())
        .append(isReadSharedData())
        .append(isWriteSharedData())
        .toHashCode();
  }
}
