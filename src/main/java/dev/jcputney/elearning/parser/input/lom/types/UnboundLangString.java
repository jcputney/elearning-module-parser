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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.common.LangStringListDeserializer;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a list of language strings in LOM metadata. This type is used for fields that require
 * multiple string values with language attributes.
 *
 * <pre>{@code
 * <xs:complexType name="unboundLangString">
 *   <xs:sequence>
 *     <xs:element name="string" type="langString" minOccurs="0" maxOccurs="unbounded"/>
 *   </xs:sequence>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class UnboundLangString implements Serializable {

  /**
   * The string values for multiple languages.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "string")
  @JsonAlias("langstring")
  @JsonDeserialize(using = LangStringListDeserializer.class)
  private List<LangString> langStrings;

  /**
   * Default constructor for the {@code UnboundLangString} class.
   * <p>
   * Initializes a new instance of the {@code UnboundLangString} class. This constructor performs no
   * operations and is primarily used to create an object without any initial state or parameters.
   */
  public UnboundLangString() {
    // no-op
  }

  /**
   * Constructs an instance of the {@code UnboundLangString} class with a specified list of
   * {@code LangString} objects.
   *
   * @param langStrings the list of {@code LangString} objects to initialize the instance with; each
   * {@code LangString} represents a string value and its associated language attributes
   */
  public UnboundLangString(List<LangString> langStrings) {
    this.langStrings = langStrings;
  }

  /**
   * Retrieves the list of language strings.
   *
   * @return a list of LangString objects representing strings with their associated language
   * attributes
   */
  public List<LangString> getLangStrings() {
    return this.langStrings;
  }

  /**
   * Sets the list of language strings associated with this instance.
   *
   * @param langStrings the list of {@code LangString} objects to set, with each object representing
   * a string value and its associated language attributes
   */
  public void setLangStrings(List<LangString> langStrings) {
    this.langStrings = langStrings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UnboundLangString that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getLangStrings(), that.getLangStrings())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLangStrings())
        .toHashCode();
  }
}
