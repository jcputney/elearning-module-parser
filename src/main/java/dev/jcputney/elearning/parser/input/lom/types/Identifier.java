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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an identifier for a related resource.
 * <pre>{@code
 * <xs:complexType name="identifier">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:element name="catalog" type="catalog" minOccurs="0"/>
 *     <xs:element name="entry" type="entry" minOccurs="0"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:identifier"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Identifier implements Serializable {

  /**
   * The catalog for the identifier.
   */
  @JacksonXmlProperty(localName = "catalog")
  private String catalog;

  /**
   * The entry for the identifier.
   */
  @JacksonXmlProperty(localName = "entry")
  private String entry;

  public Identifier() {
    // no-op
  }

  /**
   * Creates an identifier from a plain text value where only an entry is provided.
   *
   * @param value the string value of the identifier entry
   */
  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public Identifier(String value) {
    this.entry = value;
  }

  /**
   * Retrieves the catalog associated with this identifier.
   *
   * @return the catalog as a String
   */
  public String getCatalog() {
    return this.catalog;
  }

  /**
   * Sets the catalog for the identifier.
   *
   * @param catalog the catalog value to be set, represented as a String
   */
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  /**
   * Retrieves the entry for this identifier.
   *
   * @return the entry as a String
   */
  public String getEntry() {
    return this.entry;
  }

  /**
   * Sets the entry value for the identifier.
   *
   * @param entry the entry value to be set, represented as a String
   */
  public void setEntry(String entry) {
    this.entry = entry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Identifier that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCatalog(), that.getCatalog())
        .append(getEntry(), that.getEntry())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCatalog())
        .append(getEntry())
        .toHashCode();
  }
}
