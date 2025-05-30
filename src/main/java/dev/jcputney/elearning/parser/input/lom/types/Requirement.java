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
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a requirement for a learning object in a Learning Object Metadata (LOM) document. A
 * requirement is a list of conditions that must be met to use the learning object.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="requirement">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="orComposite"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:requirement"/>
 * </complexType>
 * }</pre>
 */
@SuperBuilder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Requirement extends OrComposite {

  /**
   * The list of OR-composite elements representing a set of alternative conditions that must be met
   * to use the learning object. This is for SCORM 2004 compatibility.
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="orComposite">
   *   <xs:element name="orComposite" type="OrComposite" maxOccurs="unbounded"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "orComposite", useWrapping = false)
  @JacksonXmlProperty(localName = "orComposite")
  private List<OrComposite> orCompositeList;
}
