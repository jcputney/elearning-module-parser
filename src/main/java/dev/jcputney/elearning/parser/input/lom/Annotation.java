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

package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Date;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the annotation information about a learning object in a Learning Object Metadata (LOM)
 * document. Annotations provide additional comments, instructions, or explanations related to the
 * learning object.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="annotation">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="entity"/>
 *     <group ref="date"/>
 *     <group ref="description"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:annotation"/>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Annotation implements Serializable {

  /**
   * The entity that provided the annotation, typically represented as a vCard.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="entity">
   *   <simpleContent>
   *     <extension base="VCard">
   *       <attributeGroup ref="ag:entity"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "entity")
  private String entity;

  /**
   * The date when the annotation was created or last modified.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="date">
   *   <complexContent>
   *     <extension base="DateTime">
   *       <attributeGroup ref="ag:date"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "date")
  private Date date;

  /**
   * A description of the annotation, represented as a language-specific string.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="description">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:description"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private SingleLangString description;

  public Annotation() {
    // no-op
  }

  public String getEntity() {
    return this.entity;
  }

  public void setEntity(String entity) {
    this.entity = entity;
  }

  public Date getDate() {
    return this.date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public SingleLangString getDescription() {
    return this.description;
  }

  public void setDescription(SingleLangString description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Annotation that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getEntity(), that.getEntity())
        .append(getDate(), that.getDate())
        .append(getDescription(), that.getDescription())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getEntity())
        .append(getDate())
        .append(getDescription())
        .toHashCode();
  }
}