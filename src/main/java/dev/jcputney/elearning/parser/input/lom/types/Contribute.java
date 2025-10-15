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
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a contribution to the lifecycle of the learning object in the Learning Object Metadata
 * (LOM). This class is part of the lifecycle element and provides detailed information about the
 * contributors, their roles, and the dates associated with their contributions.
 *
 * <p>The following schema snippet defines the structure of this element:
 * <pre>{@code
 * <xs:complexType name="contribute">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="role"/>
 *     <xs:group ref="entityUnbounded"/>
 *     <xs:group ref="date"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:contribute"/>
 * </xs:complexType>
 * }</pre>
 *
 * <p>Example XML representation:
 * <pre>{@code
 * <contribute>
 *   <role>
 *     <source>LOMv1.0</source>
 *     <value>publisher</value>
 *   </role>
 *   <entity><![CDATA[BEGIN:VCARD
 * VERSION:2.1
 * FN:John Doe
 * END:VCARD]]></entity>
 *   <date>
 *     <dateTime>2023-05-01</dateTime>
 *     <description>
 *       <string language="en">The date when the content was published.</string>
 *     </description>
 *   </date>
 * </contribute>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Contribute implements Serializable {

  /**
   * The role of the contributor, specifying their relationship to the learning object. Example
   * roles include "publisher", "creator", or "validator".
   *
   * <p>Schema Definition:
   * <pre>{@code
   * <xs:complexType name="role">
   *   <xs:complexContent>
   *     <xs:extension base="roleVocab">
   *       <xs:attributeGroup ref="ag:role"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "role", useWrapping = false)
  @JacksonXmlProperty(localName = "role")
  private SourceValuePair<Role> role;

  /**
   * A list of entities contributing to the learning object, typically represented as vCard data.
   * Example:
   * <pre>{@code
   * <![CDATA[BEGIN:VCARD
   * VERSION:2.1
   * FN:Jane Doe
   * ORG:Example Organization
   * TEL:+123456789
   * END:VCARD]]>
   * }</pre>
   *
   * <p>Schema Definition:
   * <pre>{@code
   * <xs:complexType name="entity">
   *   <xs:simpleContent>
   *     <xs:extension base="VCard">
   *       <xs:attributeGroup ref="ag:entity"/>
   *     </xs:extension>
   *   </xs:simpleContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "entity", useWrapping = false)
  @JacksonXmlProperty(localName = "entity")
  private List<String> entities;

  /**
   * A list of entities contributing to the learning object, typically represented as vCard data.
   *
   * <p>Schema Definition:
   * <pre>{@code
   * <xsd:complexType name="centityType">
   * 		<xsd:sequence>
   * 			<xsd:element ref="vcard"/>
   * 		</xsd:sequence>
   * 	</xsd:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "centity", useWrapping = false)
  @JacksonXmlProperty(localName = "centity")
  private List<ContributeEntity> cEntities;

  /**
   * The date associated with the contribution, such as when the contribution was made or its
   * significance.
   *
   * <p>This includes an optional description that provides additional details about the date.
   *
   * <p>Schema Definition:
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
  @JacksonXmlProperty(localName = "date")
  private Date date;

  public Contribute() {
    // no-op
  }

  /**
   * Retrieves the role associated with the current
   */
  public SourceValuePair<Role> getRole() {
    return this.role;
  }

  /**
   * Sets the role associated with the current contribution.
   *
   * @param role the source-value pair representing the role of the entity contributing to the
   * resource. The value should be one of the predefined roles in the {@link Role} enumeration, such
   * as "author", "editor", "publisher", etc.
   */
  public void setRole(
      SourceValuePair<Role> role) {
    this.role = role;
  }

  /**
   * Retrieves the list of entities associated with the current contribution.
   *
   * @return a list of strings representing the names or identifiers of the entities.
   */
  public List<String> getEntities() {
    return this.entities;
  }

  /**
   * Sets the list of entities associated with the current contribution.
   *
   * @param entities a list of strings representing the names or identifiers of the entities to be
   * associated with the current contribution
   */
  public void setEntities(List<String> entities) {
    this.entities = entities;
  }

  /**
   * Retrieves the list of ContributeEntity objects associated with the current contribution.
   *
   * @return a list of ContributeEntity objects representing entities such as individuals,
   * organizations, or systems that contribute to the lifecycle of the resource.
   */
  public List<ContributeEntity> getCEntities() {
    return this.cEntities;
  }

  /**
   * Sets the list of ContributeEntity objects associated with the current contribution.
   *
   * @param cEntities a list of ContributeEntity objects representing entities such as individuals,
   * organizations, or systems that contribute to the lifecycle of the resource
   */
  public void setCEntities(List<ContributeEntity> cEntities) {
    this.cEntities = cEntities;
  }

  /**
   * Retrieves the date associated with the current contribution.
   *
   * @return the date object representing the associated date. This may include the actual date-time
   * value and an optional description.
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * Sets the date associated with the current contribution.
   *
   * @param date the {@code Date} object representing the specific date associated with the
   * contribution. It may include a date-time value and an optional description providing additional
   * context about the date.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Contribute that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRole(), that.getRole())
        .append(getEntities(), that.getEntities())
        .append(cEntities, that.cEntities)
        .append(getDate(), that.getDate())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRole())
        .append(getEntities())
        .append(cEntities)
        .append(getDate())
        .toHashCode();
  }
}