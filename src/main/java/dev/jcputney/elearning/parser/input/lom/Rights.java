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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.CopyrightAndOtherRestrictions;
import dev.jcputney.elearning.parser.input.lom.types.Cost;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import lombok.Data;

@Data
public class Rights {

  /**
   * The cost information associated with the learning object, represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="cost">
   *   <complexContent>
   *     <extension base="costVocab">
   *       <attributeGroup ref="ag:cost"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "cost", useWrapping = false)
  @JacksonXmlProperty(localName = "cost")
  private SourceValuePair<Cost> cost;

  /**
   * Copyright and other restrictions associated with the learning object, represented as a
   * source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="copyrightAndOtherRestrictions">
   *   <complexContent>
   *     <extension base="copyrightAndOtherRestrictionsVocab">
   *       <attributeGroup ref="ag:copyrightAndOtherRestrictions"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "copyrightAndOtherRestrictions", useWrapping = false)
  @JacksonXmlProperty(localName = "copyrightAndOtherRestrictions")
  private SourceValuePair<CopyrightAndOtherRestrictions> copyrightAndOtherRestrictions;

  /**
   * Descriptions of the rights information, represented as a list of language-specific strings.
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
  private UnboundLangString descriptions;
}