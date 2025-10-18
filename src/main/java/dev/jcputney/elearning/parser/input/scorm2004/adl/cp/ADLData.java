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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the data element, which contains multiple map elements.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ADLData implements Serializable {

  /**
   * List of map elements, where each map specifies a target and shared data access.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "map", namespace = ADLCP.NAMESPACE_URI)
  private List<DataMap> mapList;

  /**
   * Default constructor for the ADLData class.
   * <p>
   * This constructor initializes an instance of the ADLData class without setting any properties or
   * performing any actions. It serves as a no-operation (no-op) placeholder for object
   * instantiation.
   */
  public ADLData() {
    // no-op
  }

  /**
   * Retrieves the list of map elements, where each map specifies a target and shared data access.
   *
   * @return a list of DataMap objects representing the target and shared data configuration
   */
  public List<DataMap> getMapList() {
    return this.mapList;
  }

  /**
   * Sets the list of map elements, where each map specifies a target ID and shared data access
   * configuration.
   *
   * @param mapList the list of {@code DataMap} objects to be assigned, representing the target and
   * shared data configuration
   */
  public void setMapList(List<DataMap> mapList) {
    this.mapList = mapList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ADLData adlData)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getMapList(), adlData.getMapList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getMapList())
        .toHashCode();
  }
}
