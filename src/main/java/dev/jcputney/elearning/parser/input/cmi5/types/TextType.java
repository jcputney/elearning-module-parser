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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a localized text element, supporting multiple languages via langstring elements.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="textType">
 *   <xs:sequence>
 *     <xs:element name="langstring" maxOccurs="unbounded" minOccurs="1">
 *       <xs:complexType>
 *         <xs:simpleContent>
 *           <xs:extension base="xs:string">
 *             <xs:attribute name="lang" type="xs:language"/>
 *             <xs:attributeGroup ref="anyAttribute"/>
 *           </xs:extension>
 *         </xs:simpleContent>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class TextType implements Serializable {

  /**
   * List of localized strings, each represented as a {@link LangString}.
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
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "langstring")
  private List<LangString> strings;

  public TextType(List<LangString> strings) {
    this.strings = strings;
  }

  public TextType() {
    // no-op
  }

  /**
   * Retrieves the list of localized strings associated with this object.
   *
   * @return a list of {@link LangString} instances, each representing a localized string with an
   * optional language attribute
   */
  public List<LangString> getStrings() {
    return this.strings;
  }

  /**
   * Sets the list of localized strings for this object.
   *
   * @param strings a list of {@link LangString} instances, each representing a localized string
   * with its associated language
   */
  public void setStrings(List<LangString> strings) {
    this.strings = strings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof TextType textType)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getStrings(), textType.getStrings())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getStrings())
        .toHashCode();
  }
}
