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
import dev.jcputney.elearning.parser.input.lom.LOM;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a taxon path in the Learning Object Metadata (LOM) schema. A taxon path defines a
 * hierarchical classification or categorization of a learning object, consisting of a source and a
 * list of taxa.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="taxonPath">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="source"/>
 *     <xs:group ref="taxon"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:taxonPath"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class TaxonPath implements Serializable {

  /**
   * The source describing the classification system or taxonomy from which the taxa are derived.
   * Often represented as a multilingual string.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="source">
   *   <xs:element name="source" type="LangString"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "source")
  private SingleLangString source;

  /**
   * A list of taxon elements representing nodes or concepts in the hierarchical classification.
   * Each taxon specifies a unique concept within the taxonomy.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="taxon">
   *   <xs:element name="taxon" type="Taxon" maxOccurs="unbounded"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "taxon")
  private List<Taxon> taxons;

  /**
   * Any custom elements or extensions defined within the taxon path, allowing for schema
   * extensibility.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="ex:customElements"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "customElement", namespace = LOM.NAMESPACE_URI + "/extend")
  private List<Object> customElements;

  public TaxonPath(SingleLangString source, List<Taxon> taxons, List<Object> customElements) {
    this.source = source;
    this.taxons = taxons;
    this.customElements = customElements;
  }

  public TaxonPath() {
    // no-op
  }

  public SingleLangString getSource() {
    return this.source;
  }

  public void setSource(SingleLangString source) {
    this.source = source;
  }

  public List<Taxon> getTaxons() {
    return this.taxons;
  }

  public void setTaxons(List<Taxon> taxons) {
    this.taxons = taxons;
  }

  public List<Object> getCustomElements() {
    return this.customElements;
  }

  public void setCustomElements(List<Object> customElements) {
    this.customElements = customElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof TaxonPath taxonPath)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSource(), taxonPath.getSource())
        .append(getTaxons(), taxonPath.getTaxons())
        .append(getCustomElements(), taxonPath.getCustomElements())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSource())
        .append(getTaxons())
        .append(getCustomElements())
        .toHashCode();
  }
}