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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an <code>orComposite</code> element in the LOM schema, defining a set of conditions
 * related to platform or software requirements. Each <code>orComposite</code> provides specific
 * information about the type, name, and version constraints for the required environment.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="orComposite">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="type"/>
 *     <xs:group ref="name"/>
 *     <xs:group ref="minimumVersion"/>
 *     <xs:group ref="maximumVersion"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:orComposite"/>
 * </xs:complexType>
 * }</pre>
 */
@SuperBuilder
@Getter
@Jacksonized
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class OrComposite implements Serializable {

  /**
   * The type of platform or software requirement, represented as controlled 'vocabulary'.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="type">
   *   <xs:complexContent>
   *     <xs:extension base="typeVocab">
   *       <xs:attributeGroup ref="ag:type"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "type", useWrapping = false)
  @JacksonXmlProperty(localName = "type")
  private SourceValuePair<Type> type;
  /**
   * The name of the platform or software, represented as controlled vocabulary.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="name">
   *   <xs:complexContent>
   *     <xs:extension base="nameVocab">
   *       <xs:attributeGroup ref="ag:name"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "name", useWrapping = false)
  @JacksonXmlProperty(localName = "name")
  private SourceValuePair<Name> name;
  /**
   * The minimum version of the platform or software required.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="minimumVersion">
   *   <xs:simpleContent>
   *     <xs:extension base="CharacterString">
   *       <xs:attributeGroup ref="ag:minimumVersion"/>
   *     </xs:extension>
   *   </xs:simpleContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "minimumVersion")
  private String minimumVersion;
  /**
   * The maximum version of the platform or software required.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="maximumVersion">
   *   <xs:simpleContent>
   *     <xs:extension base="CharacterString">
   *       <xs:attributeGroup ref="ag:maximumVersion"/>
   *     </xs:extension>
   *   </xs:simpleContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "maximumVersion")
  private String maximumVersion;

  /**
   * Default constructor for the OrComposite class.
   */
  public OrComposite() {
    // Default constructor
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OrComposite that = (OrComposite) o;

    return new EqualsBuilder()
        .append(type, that.type)
        .append(name, that.name)
        .append(minimumVersion, that.minimumVersion)
        .append(maximumVersion, that.maximumVersion)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(type)
        .append(name)
        .append(minimumVersion)
        .append(maximumVersion)
        .toHashCode();
  }
}