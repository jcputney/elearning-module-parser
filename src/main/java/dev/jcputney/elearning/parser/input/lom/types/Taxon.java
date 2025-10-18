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
public final class Taxon implements Serializable {

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

  /**
   * Default constructor for the {@code Taxon} class.
   * <p>
   * Initializes a new instance of the {@code Taxon} class. This constructor performs no operations
   * and is primarily provided to allow object creation without any initial parameters or state.
   */
  public Taxon() {
    // no-op
  }

  /**
   * Retrieves the unique identifier for the taxon.
   *
   * @return the identifier of the taxon as a string, which is typically used to reference the taxon
   * in a classification system.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Sets the unique identifier for the taxon.
   *
   * @param id the identifier to be set for the taxon, typically a string used in a classification
   * or taxonomy system
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Retrieves the entry for the taxon.
   *
   * @return the entry associated with the taxon, represented as a {@code SingleLangString}, which
   * typically contains a string value and its corresponding language attribute
   */
  public SingleLangString getEntry() {
    return this.entry;
  }

  /**
   * Sets the entry associated with the taxon. The entry is represented as an instance of
   * {@code SingleLangString}, which typically includes a string value and a language attribute.
   *
   * @param entry the {@code SingleLangString} object to be set as the entry for this taxon
   */
  public void setEntry(SingleLangString entry) {
    this.entry = entry;
  }

  /**
   * Retrieves the list of custom elements for the taxon. Custom elements are additional elements
   * associated with the taxon that are not part of the core taxonomy structure.
   *
   * @return a list of objects representing the custom elements associated with this taxon
   */
  public List<Object> getCustomElements() {
    return this.customElements;
  }

  /**
   * Sets the list of custom elements associated with this taxon. Custom elements represent
   * additional user-defined data that is not part of the core taxonomy structure.
   *
   * @param customElements the list of custom elements to associate with this taxon, where each
   * element is represented as an Object
   */
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