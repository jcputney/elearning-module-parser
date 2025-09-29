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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a source-value pair, commonly used in the LOM schema to describe a value and its
 * associated source. This structure is used for elements such as structure, aggregation level, and
 * other vocabulary-based fields.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="sourceValuePair">
 *   <xs:sequence>
 *     <xs:element name="source" type="CharacterString"/>
 *     <xs:element name="value" type="CharacterString"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="ag:sourceValuePair"/>
 * </xs:complexType>
 * }</pre>
 *
 * @param <T> the type of the value, which can be an enumeration or a string
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SourceValuePair<T extends Serializable> implements Serializable {

  /**
   * The source of the value, typically a reference to a controlled vocabulary or schema.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:element name="source" type="CharacterString"/>
   * }</pre>
   */
  @JsonProperty("source")
  private String source;

  /**
   * The value associated with the source, representing the specific term or definition.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:element name="value" type="CharacterString"/>
   * }</pre>
   */
  @JsonProperty("value")
  private T value;

  public SourceValuePair() {
    // no-op
  }

  /**
   * Retrieves the source associated with this instance. The source typically refers to a controlled
   * vocabulary or schema that defines the context or meaning of the value.
   *
   * @return the source as a String
   */
  public String getSource() {
    return this.source;
  }

  /**
   * Sets the source for this instance. The source typically refers to a controlled vocabulary or
   * schema that defines the context or meaning of the associated value.
   *
   * @param source the source to set, represented as a String
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Retrieves the value associated with this instance. The value represents the specific term or
   * definition associated with the source in a source-value pair.
   *
   * @return the value associated with this instance, of type T
   */
  public T getValue() {
    return this.value;
  }

  /**
   * Sets the value associated with this instance. The value represents the specific term or
   * definition associated with the source in a source-value pair.
   *
   * @param value the value to set, of type T
   */
  public void setValue(T value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SourceValuePair<?> that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSource(), that.getSource())
        .append(getValue(), that.getValue())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSource())
        .append(getValue())
        .toHashCode();
  }
}