/*
 * Copyright (c) 2024. Jonathan Putney
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
import lombok.Data;

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
@Data
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
}