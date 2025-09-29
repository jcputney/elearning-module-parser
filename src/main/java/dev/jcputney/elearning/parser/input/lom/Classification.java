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
import dev.jcputney.elearning.parser.input.lom.types.Purpose;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.TaxonPath;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import java.util.List;
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
  private UnboundLangString description;

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

  public Classification() {
    // no-op
  }

  /**
   * Retrieves the purpose of the classification. The purpose is encapsulated as a source-value
   * pair, where the source indicates the context or origin of the value, and the value specifies
   * the actual purpose.
   *
   * @return a SourceValuePair containing the source and value representing the purpose
   */
  public SourceValuePair<Purpose> getPurpose() {
    return this.purpose;
  }

  /**
   * Sets the purpose of the classification. The purpose is encapsulated as a source-value pair,
   * where the source indicates the context or origin of the value, and the value specifies the
   * actual purpose.
   *
   * @param purpose a SourceValuePair containing the source and value representing the purpose
   */
  public void setPurpose(
      SourceValuePair<Purpose> purpose) {
    this.purpose = purpose;
  }

  /**
   * Retrieves the list of taxon paths associated with this classification. A taxon path represents
   * a hierarchical classification or categorization.
   *
   * @return a list of {@code TaxonPath} objects associated with the classification.
   */
  public List<TaxonPath> getTaxonPaths() {
    return this.taxonPaths;
  }

  /**
   * Sets the list of taxon paths associated with this classification. A taxon path represents a
   * hierarchical classification or categorization.
   *
   * @param taxonPaths a list of {@code TaxonPath} objects to associate with the classification
   */
  public void setTaxonPaths(List<TaxonPath> taxonPaths) {
    this.taxonPaths = taxonPaths;
  }

  /**
   * Retrieves the description of the classification. The description is represented as a list of
   * language strings, allowing for multilingual support.
   *
   * @return an UnboundLangString object containing the multilingual description of the
   * classification.
   */
  public UnboundLangString getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the classification. The description is represented as an
   * UnboundLangString, allowing for multilingual support.
   *
   * @param description an UnboundLangString object containing the multilingual description of the
   * classification
   */
  public void setDescription(UnboundLangString description) {
    this.description = description;
  }

  /**
   * Retrieves the keywords associated with the classification. The keywords are represented as an
   * UnboundLangString object, which supports multilingual strings.
   *
   * @return an UnboundLangString object containing the keywords of the classification.
   */
  public UnboundLangString getKeywords() {
    return this.keywords;
  }

  /**
   * Sets the keywords associated with the classification. The keywords are represented as an
   * UnboundLangString, allowing for multilingual support.
   *
   * @param keywords an UnboundLangString object containing the keywords to associate with the
   * classification
   */
  public void setKeywords(UnboundLangString keywords) {
    this.keywords = keywords;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Classification that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getPurpose(), that.getPurpose())
        .append(getTaxonPaths(), that.getTaxonPaths())
        .append(getDescription(), that.getDescription())
        .append(getKeywords(), that.getKeywords())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getPurpose())
        .append(getTaxonPaths())
        .append(getDescription())
        .append(getKeywords())
        .toHashCode();
  }
}