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
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public sealed class OrComposite implements Serializable permits Requirement {

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

  public OrComposite() {
    // no-op
  }

  /**
   * Retrieves the type associated with the current object.
   *
   * @return a {@link SourceValuePair} containing the source and value of the type, where the value
   * is an enumeration of {@link Type}.
   */
  public SourceValuePair<Type> getType() {
    return this.type;
  }

  /**
   * Sets the type associated with the current object.
   *
   * @param type a {@link SourceValuePair} containing the source and value of the type, where the
   * value is an enumeration of {@link Type}.
   */
  public void setType(SourceValuePair<Type> type) {
    this.type = type;
  }

  /**
   * Retrieves the name associated with the current object.
   *
   * @return a {@link SourceValuePair} containing the source and value of the name, where the value
   * is an enumeration of {@link Name}.
   */
  public SourceValuePair<Name> getName() {
    return this.name;
  }

  /**
   * Sets the name associated with the current object.
   *
   * @param name a {@link SourceValuePair} containing the source and value of the name, where the
   * value is an enumeration of {@link Name}.
   */
  public void setName(SourceValuePair<Name> name) {
    this.name = name;
  }

  /**
   * Retrieves the minimum version associated with the current object.
   *
   * @return the minimum version as a String
   */
  public String getMinimumVersion() {
    return this.minimumVersion;
  }

  /**
   * Sets the minimum version associated with the current object.
   *
   * @param minimumVersion the minimum version to set, represented as a String
   */
  public void setMinimumVersion(String minimumVersion) {
    this.minimumVersion = minimumVersion;
  }

  /**
   * Retrieves the maximum version associated with the current object.
   *
   * @return the maximum version as a String
   */
  public String getMaximumVersion() {
    return this.maximumVersion;
  }

  /**
   * Sets the maximum version associated with the current object.
   *
   * @param maximumVersion the maximum version to set, represented as a String
   */
  public void setMaximumVersion(String maximumVersion) {
    this.maximumVersion = maximumVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof OrComposite that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getType(), that.getType())
        .append(getName(), that.getName())
        .append(getMinimumVersion(), that.getMinimumVersion())
        .append(getMaximumVersion(), that.getMaximumVersion())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getType())
        .append(getName())
        .append(getMinimumVersion())
        .append(getMaximumVersion())
        .toHashCode();
  }
}