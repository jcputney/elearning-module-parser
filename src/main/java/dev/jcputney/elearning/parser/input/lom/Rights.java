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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.CopyrightAndOtherRestrictions;
import dev.jcputney.elearning.parser.input.lom.types.Cost;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the rights information of a learning object, including cost, copyright, and
 * descriptions.
 *
 * <p>This class is part of the Learning Object Metadata (LOM) model and is used to describe the
 * rights associated with a learning object.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="rights">
 *   <complexContent>
 *     <extension base="rightsVocab">
 *       <attributeGroup ref="ag:rights"/>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Rights implements Serializable {

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
   * Descriptions of the "rights" information, represented as a list of language-specific strings.
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

  public Rights() {
    // no-op
  }

  public SourceValuePair<Cost> getCost() {
    return this.cost;
  }

  public void setCost(
      SourceValuePair<Cost> cost) {
    this.cost = cost;
  }

  public SourceValuePair<CopyrightAndOtherRestrictions> getCopyrightAndOtherRestrictions() {
    return this.copyrightAndOtherRestrictions;
  }

  public void setCopyrightAndOtherRestrictions(
      SourceValuePair<CopyrightAndOtherRestrictions> copyrightAndOtherRestrictions) {
    this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
  }

  public UnboundLangString getDescriptions() {
    return this.descriptions;
  }

  public void setDescriptions(UnboundLangString descriptions) {
    this.descriptions = descriptions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Rights rights)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCost(), rights.getCost())
        .append(getCopyrightAndOtherRestrictions(), rights.getCopyrightAndOtherRestrictions())
        .append(getDescriptions(), rights.getDescriptions())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCost())
        .append(getCopyrightAndOtherRestrictions())
        .append(getDescriptions())
        .toHashCode();
  }
}