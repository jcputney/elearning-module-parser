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

package dev.jcputney.elearning.parser.input.cmi5.types;

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
public class LangString implements Serializable {

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

  public LangString(String value, String lang) {
    this.value = value;
    this.lang = lang;
  }

  public LangString() {
    // no-op
  }

  public static LangStringBuilder builder() {
    return new LangStringBuilder();
  }

  public String getValue() {
    return this.value;
  }

  public String getLang() {
    return this.lang;
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

  @com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder(withPrefix = "")
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class LangStringBuilder {

    private String value;
    private String lang;

    LangStringBuilder() {
    }

    @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
    @JacksonXmlText
    public LangStringBuilder value(String value) {
      this.value = value;
      return this;
    }

    @JacksonXmlProperty(isAttribute = true)
    @JsonProperty("lang")
    public LangStringBuilder lang(String lang) {
      this.lang = lang;
      return this;
    }

    public LangString build() {
      return new LangString(this.value, this.lang);
    }

    public String toString() {
      return "LangString.LangStringBuilder(value=" + this.value + ", lang=" + this.lang + ")";
    }
  }
}
