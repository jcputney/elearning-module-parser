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
public final class DataMap implements Serializable {

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

  /**
   * Default constructor for the DataMap class.
   * <p>
   * This constructor initializes an instance of the DataMap class without setting any properties or
   * performing any actions. It serves as a no-operation (no-op) placeholder for object
   * instantiation.
   */
  public DataMap() {
    // no-op
  }

  /**
   * Retrieves the target ID URI for shared data mapping.
   *
   * @return the target ID as a String
   */
  public String getTargetID() {
    return this.targetID;
  }

  /**
   * Sets the target ID URI for shared data mapping.
   *
   * @param targetID the target ID as a String to be assigned
   */
  public void setTargetID(String targetID) {
    this.targetID = targetID;
  }

  /**
   * Determines whether shared data is readable.
   *
   * @return true if shared data is readable, otherwise false
   */
  public boolean isReadSharedData() {
    return this.readSharedData;
  }

  /**
   * Sets the read access configuration for shared data.
   *
   * @param readSharedData a boolean value where {@code true} enables read access to shared data,
   * and {@code false} disables it
   */
  public void setReadSharedData(boolean readSharedData) {
    this.readSharedData = readSharedData;
  }

  /**
   * Determines whether shared data is writable.
   *
   * @return true if shared data is writable, otherwise false
   */
  public boolean isWriteSharedData() {
    return this.writeSharedData;
  }

  /**
   * Configures the write access for shared data.
   *
   * @param writeSharedData a boolean value where {@code true} enables write access to shared data,
   * and {@code false} disables it
   */
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
