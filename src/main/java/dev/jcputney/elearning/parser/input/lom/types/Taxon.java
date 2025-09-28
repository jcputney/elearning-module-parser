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
 * Represents a taxon within a taxon path in the Learning Object Metadata (LOM) schema. A taxon is
 * used to identify a specific concept or category in a classification hierarchy.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="taxon">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="id"/>
 *     <xs:group ref="entryTaxon"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:taxon"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Taxon implements Serializable {

  /**
   * The unique identifier for the taxon, typically used to reference the taxon in a classification
   * system.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="id">
   *   <xs:element name="id" type="CharacterString"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "id")
  private String id;

  /**
   * The entry providing a description or label for the taxon, often represented as a multilingual
   * string.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="entryTaxon">
   *   <xs:element name="entry" type="LangString"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "entry")
  private SingleLangString entry;

  /**
   * Any custom elements or extensions defined within the taxon, allowing for schema extensibility.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:group ref="ex:customElements"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "customElement", namespace = LOM.NAMESPACE_URI + "/extend")
  private List<Object> customElements;

  public Taxon() {
    // no-op
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SingleLangString getEntry() {
    return this.entry;
  }

  public void setEntry(SingleLangString entry) {
    this.entry = entry;
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

    if (!(o instanceof Taxon taxon)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getId(), taxon.getId())
        .append(getEntry(), taxon.getEntry())
        .append(getCustomElements(), taxon.getCustomElements())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getId())
        .append(getEntry())
        .append(getCustomElements())
        .toHashCode();
  }
}