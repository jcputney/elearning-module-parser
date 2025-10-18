/*
 * Copyright (c) 2025. Jonathan Putney
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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a catalog entry in LOM metadata. This type is used for fields that require a catalog
 * entry.
 *
 * <pre>{@code
 * <xsd:complexType name="catalogentryType" mixed="true">
 * 		<xsd:sequence>
 * 			<xsd:element ref="catalog"/>
 * 			<xsd:element ref="entry"/>
 * 			<xsd:group ref="grp.any"/>
 * 		</xsd:sequence>
 * 	</xsd:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class CatalogEntry implements Serializable {

  /**
   * The catalog of the entry.
   *
   * <p>Example: "IEEE LOM" or "Dublin Core".
   */
  @JacksonXmlProperty(localName = "catalog")
  private String catalog;

  /**
   * The entry in the catalog.
   *
   * <p>Example: "LOMv1.0" or "Dublin Core v1.0".
   */
  @JacksonXmlProperty(localName = "entry")
  private UnboundLangString entry;

  /**
   * Default constructor for the CatalogEntry class.
   * <p>
   * Initializes an empty instance of the CatalogEntry object with no assigned catalog or entry
   * values. Primarily used for deserialization or creating placeholders before explicit values are
   * set.
   */
  public CatalogEntry() {
    // no-op
  }

  /**
   * Retrieves the catalog associated with this catalog entry.
   *
   * @return the catalog as a string
   */
  public String getCatalog() {
    return this.catalog;
  }

  /**
   * Sets the catalog for this catalog entry.
   *
   * @param catalog the name of the catalog to set
   */
  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  /**
   * Retrieves the entry associated with this catalog entry.
   *
   * @return the entry as an UnboundLangString, representing multiple language-specific string
   * values
   */
  public UnboundLangString getEntry() {
    return this.entry;
  }

  /**
   * Sets the entry for this catalog entry.
   *
   * @param entry the entry to set, represented as an UnboundLangString containing multiple
   * language-specific string values
   */
  public void setEntry(UnboundLangString entry) {
    this.entry = entry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CatalogEntry that)) {
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
