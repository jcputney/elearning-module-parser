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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single language string in LOM metadata. This type is used for fields that only
 * require a single string value with a language attribute.
 *
 * <pre>{@code
 * <xs:complexType name="singleLangString">
 *   <xs:sequence>
 *     <xs:element name="string" type="langString"/>
 *   </xs:sequence>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SingleLangString implements Serializable {

  /**
   * The string value for a given language.
   */
  @JacksonXmlProperty(localName = "string")
  @JsonAlias("langstring")
  private LangString langString;

  public SingleLangString() {
  }

  public LangString getLangString() {
    return this.langString;
  }

  @JsonAlias("langstring")
  @JacksonXmlProperty(localName = "string")
  public void setLangString(LangString langString) {
    this.langString = langString;
  }

  public String toString() {
    return "SingleLangString(langString=" + this.getLangString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SingleLangString that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getLangString(), that.getLangString())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLangString())
        .toHashCode();
  }
}
