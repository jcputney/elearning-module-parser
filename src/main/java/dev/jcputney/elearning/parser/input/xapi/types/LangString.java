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

package dev.jcputney.elearning.parser.input.xapi.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import dev.jcputney.elearning.parser.input.common.TrimAndPreserveIndentationDeserializer;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single localized string with an optional language attribute.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="langstring" maxOccurs="unbounded" minOccurs="1">
 *   <xs:complexType>
 *     <xs:simpleContent>
 *       <xs:extension base="xs:string">
 *         <xs:attribute name="lang" type="xs:language"/>
 *         <xs:attributeGroup ref="anyAttribute"/>
 *       </xs:extension>
 *     </xs:simpleContent>
 *   </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class LangString implements Serializable {

  /**
   * The actual text content of the localized string.
   *
   * <pre>{@code
   * <xs:extension base="xs:string">
   *   <xs:attribute name="lang" type="xs:language"/>
   *   <xs:attributeGroup ref="anyAttribute"/>
   * </xs:extension>
   * }</pre>
   */
  @JacksonXmlText
  @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
  private String value;

  /**
   * The language of the string, represented as an optional attribute.
   *
   * <pre>{@code
   * <xs:attribute name="lang" type="xs:language"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("lang")
  private String lang;

  /**
   * Constructs a new {@code LangString} with the specified value and language.
   *
   * @param value the actual text content of the localized string
   * @param lang the language of the string, represented as a {@code String}; this value is
   * optional
   */
  public LangString(String value, String lang) {
    this.value = value;
    this.lang = lang;
  }

  /**
   * Default no-argument constructor for the LangString class. Initializes a new instance of
   * LangString with no initial value or language set.
   */
  public LangString() {
    // no-op
  }

  /**
   * Retrieves the value of the localized string.
   *
   * @return the actual text content of the localized string
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Sets the value of the localized string.
   *
   * @param value the text content to set for the localized string
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Retrieves the language associated with the localized string.
   *
   * @return the language of the string as a {@code String}, or {@code null} if not set
   */
  public String getLang() {
    return this.lang;
  }

  /**
   * Sets the language attribute for the localized string.
   *
   * @param lang the language to associate with the string, represented as a {@code String}
   */
  public void setLang(String lang) {
    this.lang = lang;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LangString that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getValue(), that.getValue())
        .append(getLang(), that.getLang())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getValue())
        .append(getLang())
        .toHashCode();
  }
}
