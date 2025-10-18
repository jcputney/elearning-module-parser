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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a date element in LOM metadata, including the date value and an optional description.
 * <pre>{@code
 * <xs:complexType name="date">
 *   <xs:complexContent>
 *     <xs:extension base="DateTime">
 *       <xs:attributeGroup ref="ag:date"/>
 *     </xs:extension>
 *   </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Date implements Serializable {

  /**
   * The actual date-time value.
   */
  @JacksonXmlProperty(localName = "dateTime")
  private String dateTime;

  /**
   * A description of the date, typically a LangString.
   */
  @JacksonXmlProperty(localName = "description")
  private UnboundLangString description;

  /**
   * Default constructor for the {@code Date} class.
   * <p>
   * Initializes a new instance of the {@code Date} class. This constructor performs no operations
   * and primarily exists to allow object creation without any initial state or parameters.
   */
  public Date() {
    // no-op
  }

  /**
   * Retrieves the date-time value associated with this instance.
   *
   * @return the date-time value as a String
   */
  public String getDateTime() {
    return this.dateTime;
  }

  /**
   * Sets the date-time value for this instance.
   *
   * @param dateTime the date-time value to be set as a String, representing a specific date and
   * time
   */
  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  /**
   * Retrieves the description of the date. The description typically contains language-specific
   * string values and is represented as an UnboundLangString.
   *
   * @return the description of the date as an UnboundLangString
   */
  public UnboundLangString getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the date element. The description is typically a language-aware string
   * represented as an {@code UnboundLangString}.
   *
   * @param description the description to set, which contains one or more language-specific string
   * values
   */
  public void setDescription(UnboundLangString description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Date date)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getDateTime(), date.getDateTime())
        .append(getDescription(), date.getDescription())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getDateTime())
        .append(getDescription())
        .toHashCode();
  }
}