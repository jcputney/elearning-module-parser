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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import dev.jcputney.elearning.parser.input.common.TrimAndPreserveIndentationDeserializer;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a LangString in LOM metadata, which is a collection of strings with language
 * attributes. This type is used for fields such as title, description, and keyword.
 *
 * <pre>{@code
 * <xs:complexType name="LangString">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:element name="string" type="langString"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 * </xs:complexType>
 *
 * <xs:complexType name="langString">
 *   <xs:simpleContent>
 *     <xs:extension base="CharacterString">
 *       <xs:attribute name="language" type="LanguageId"/>
 *     </xs:extension>
 *   </xs:simpleContent>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class LangString implements Serializable {

  /**
   * The language of the string.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("language")
  @JsonAlias("lang")
  private String language;

  /**
   * The actual string value.
   */
  @JacksonXmlText
  @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
  private String value;

  /**
   * Constructs a {@code LangString} object with a specified language and value.
   *
   * @param language the language code for the string, generally using ISO 639-1 or ISO 639-3 codes
   * @param value the value of the string in the specified language
   */
  public LangString(String language, String value) {
    this.language = language;
    this.value = value;
  }

  /**
   * Constructs a {@code LangString} object with a specified value.
   *
   * @param value the value of the string; typically representing text in a specific language
   */
  public LangString(String value) {
    this.value = value;
  }

  /**
   * Default constructor for the {@code LangString} class.
   *
   * Constructs an instance of the {@code LangString} class without initializing the language or
   * value fields. This constructor is primarily provided for use cases requiring an object without
   * any preset state.
   */
  public LangString() {
    // no-op
  }

  /**
   * Retrieves the language associated with this instance.
   *
   * @return the language as a String
   */
  public String getLanguage() {
    return this.language;
  }

  /**
   * Sets the language attribute of this instance.
   *
   * @param language the language code to set, typically represented as an ISO 639-1 or ISO 639-3
   * code
   */
  @JsonAlias("lang")
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("language")
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Retrieves the value of the string associated with this instance.
   *
   * @return the value as a String
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Sets the value of this instance.
   *
   * @param value the string value to set; trimmed of leading whitespaces while preserving
   * indentation
   */
  @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
  @JacksonXmlText
  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    return "LangString(language=" + this.getLanguage() + ", value=" + this.getValue() + ")";
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
        .append(getLanguage(), that.getLanguage())
        .append(getValue(), that.getValue())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLanguage())
        .append(getValue())
        .toHashCode();
  }
}
