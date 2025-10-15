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

package dev.jcputney.elearning.parser.input.scorm12.adl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the {@code <adlcp:prerequisites>} element in SCORM 1.2. This element includes a string
 * value and an optional type attribute.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm12Prerequisites implements Serializable {

  /**
   * The string content of the "prerequisites" element.
   */
  @JacksonXmlText
  private String value;

  /**
   * The type attribute of the prerequisites element, which is optional. Example value:
   * "aicc_script".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "type")
  @JsonProperty("type")
  private String type;

  public Scorm12Prerequisites() {
    // no-op
  }

  /**
   * Retrieves the string value of the prerequisites element.
   *
   * @return The string content of the "prerequisites" element.
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Sets the string value of the prerequisites element.
   *
   * @param value The string content to set for the "prerequisites" element.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Retrieves the type attribute of the "prerequisites" element.
   *
   * @return The type attribute of the "prerequisites" element, or null if not specified.
   */
  public String getType() {
    return this.type;
  }

  /**
   * Sets the type attribute of the "prerequisites" element.
   *
   * @param type The type attribute to set for the "prerequisites" element. This value may represent
   * a specification-dependent string, such as "aicc_script".
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Prerequisites that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getValue(), that.getValue())
        .append(getType(), that.getType())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getValue())
        .append(getType())
        .toHashCode();
  }
}
