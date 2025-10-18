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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.CatalogEntry;
import dev.jcputney.elearning.parser.input.lom.types.ContributeMeta;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the meta-metadata information about a learning object in a Learning Object Metadata
 * (LOM) document. Meta-metadata provides information about the metadata itself, including its
 * origin, purpose, and maintenance.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="metaMetadata">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="identifier"/>
 *     <group ref="contributeMeta"/>
 *     <group ref="metadataSchema"/>
 *     <group ref="language"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:metaMetadata"/>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class MetaMetadata implements Serializable {

  /**
   * The list of unique identifiers for the metadata. Each identifier typically includes a catalog
   * and an entry.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="identifier">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="catalog"/>
   *     <group ref="entry"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:identifier"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "identifier", useWrapping = false)
  @JacksonXmlProperty(localName = "identifier")
  private List<Identifier> identifier;

  /**
   * A list of catalog entries for the metadata.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xsd:element ref="catalogentry" minOccurs="0" maxOccurs="unbounded"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "catalogentry", useWrapping = false)
  @JacksonXmlProperty(localName = "catalogentry")
  private List<CatalogEntry> catalogEntries;

  /**
   * The list of contributors to the metadata, including their roles, entities, and contribution
   * dates.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="contributeMeta">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="roleMeta"/>
   *     <group ref="entityUnbounded"/>
   *     <group ref="date"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:contribute"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "contribute", useWrapping = false)
  @JacksonXmlProperty(localName = "contribute")
  private List<ContributeMeta> contribute;

  /**
   * The metadata schema or standard being used. Typically represented as a list of strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="metadataSchema">
   *   <simpleContent>
   *     <extension base="CharacterString">
   *       <attributeGroup ref="ag:metadataSchema"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "metadataSchema", useWrapping = false)
  @JacksonXmlProperty(localName = "metadataSchema")
  @JsonAlias("metadatascheme")
  private List<String> metadataSchema;

  /**
   * The language used for the metadata content.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="language">
   *   <simpleContent>
   *     <extension base="LanguageId">
   *       <attributeGroup ref="ag:language"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "language")
  private String language;

  /**
   * A placeholder for custom elements that extend the meta-metadata information. This allows for
   * additional metadata to be included that is not part of the standard schema.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="customElements">
   *   <complexContent>
   *     <extension base="xs:anyType"/>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "customElements", useWrapping = false)
  @JacksonXmlProperty(localName = "customElements")
  private List<Object> customElements;

  /**
   * Constructs a new instance of the MetaMetadata class. This is a no-operation constructor,
   * primarily used for initializing an empty instance.
   */
  public MetaMetadata() {
    // no-op
  }

  /**
   * Retrieves the list of identifiers associated with this object.
   *
   * @return a list of {@link Identifier} objects representing the identifiers
   */
  public List<Identifier> getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the list of identifiers associated with this object.
   *
   * @param identifier the list of {@link Identifier} objects to set
   */
  public void setIdentifier(List<Identifier> identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the list of catalog entries associated with this object.
   *
   * @return a list of {@link CatalogEntry} objects representing the catalog entries
   */
  public List<CatalogEntry> getCatalogEntries() {
    return this.catalogEntries;
  }

  /**
   * Sets the list of catalog entries associated with this object.
   *
   * @param catalogEntries the list of {@link CatalogEntry} objects to set
   */
  public void setCatalogEntries(List<CatalogEntry> catalogEntries) {
    this.catalogEntries = catalogEntries;
  }

  /**
   * Retrieves the list of contribution metadata associated with this object.
   *
   * @return a list of {@link ContributeMeta} objects representing the contribution metadata
   */
  public List<ContributeMeta> getContribute() {
    return this.contribute;
  }

  /**
   * Sets the list of contribution metadata associated with this object.
   *
   * @param contribute the list of {@link ContributeMeta} objects to set
   */
  public void setContribute(List<ContributeMeta> contribute) {
    this.contribute = contribute;
  }

  /**
   * Retrieves the metadata schema associated with this object.
   *
   * @return a list of strings representing the metadata schema
   */
  public List<String> getMetadataSchema() {
    return this.metadataSchema;
  }

  /**
   * Sets the metadata schema associated with this object.
   *
   * @param metadataSchema the list of strings representing the metadata schema to set
   */
  public void setMetadataSchema(List<String> metadataSchema) {
    this.metadataSchema = metadataSchema;
  }

  /**
   * Retrieves the language associated with this object.
   *
   * @return a string representing the language.
   */
  public String getLanguage() {
    return this.language;
  }

  /**
   * Sets the language associated with this object.
   *
   * @param language the language to set, represented as a string
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Retrieves the list of custom elements associated with this object.
   *
   * @return a list of {@link Object} representing the custom elements
   */
  public List<Object> getCustomElements() {
    return this.customElements;
  }

  /**
   * Sets the list of custom elements associated with this object.
   *
   * @param customElements the list of {@link Object} instances representing the custom elements to
   * set
   */
  public void setCustomElements(List<Object> customElements) {
    this.customElements = customElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof MetaMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifier(), that.getIdentifier())
        .append(getCatalogEntries(), that.getCatalogEntries())
        .append(getContribute(), that.getContribute())
        .append(getMetadataSchema(), that.getMetadataSchema())
        .append(getLanguage(), that.getLanguage())
        .append(getCustomElements(), that.getCustomElements())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getCatalogEntries())
        .append(getContribute())
        .append(getMetadataSchema())
        .append(getLanguage())
        .append(getCustomElements())
        .toHashCode();
  }
}