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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Date;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Annotation {

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

  /**
   * Default constructor for the Annotation class.
   */
  public Annotation() {
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

    Annotation that = (Annotation) o;

    return new EqualsBuilder()
        .append(entity, that.entity)
        .append(date, that.date)
        .append(description, that.description)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(entity)
        .append(date)
        .append(description)
        .toHashCode();
  }
}