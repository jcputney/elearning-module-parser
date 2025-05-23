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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Purpose;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.TaxonPath;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the classification information about a learning object in a Learning Object Metadata
 * (LOM) document. The classification provides information about the purpose and taxonomy paths of
 * the learning object, aiding in its categorization and retrieval.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="classification">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="purpose"/>
 *     <group ref="taxonPath"/>
 *     <group ref="description"/>
 *     <group ref="keyword"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:classification"/>
 * </complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Classification implements Serializable {

  /**
   * The purpose of this classification, typically represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="purpose">
   *   <complexContent>
   *     <extension base="purposeVocab">
   *       <attributeGroup ref="ag:purpose"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "purpose", useWrapping = false)
  @JacksonXmlProperty(localName = "purpose")
  private SourceValuePair<Purpose> purpose;
  /**
   * The taxonomy paths associated with this classification, allowing for hierarchical
   * categorization.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="taxonPath">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="source"/>
   *     <group ref="taxon"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:taxonPath"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "taxonPath", useWrapping = false)
  @JacksonXmlProperty(localName = "taxonPath")
  private List<TaxonPath> taxonPaths;
  /**
   * A description of this classification, represented as a language-specific string.
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
   * Keywords associated with this classification, represented as a list of language-specific
   * strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="keyword">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:keyword"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "keyword")
  private UnboundLangString keywords;

  /**
   * Default constructor for the Classification class.
   */
  public Classification() {
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

    Classification that = (Classification) o;

    return new EqualsBuilder()
        .append(purpose, that.purpose)
        .append(taxonPaths, that.taxonPaths)
        .append(description, that.description)
        .append(keywords, that.keywords)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(purpose)
        .append(taxonPaths)
        .append(description)
        .append(keywords)
        .toHashCode();
  }
}