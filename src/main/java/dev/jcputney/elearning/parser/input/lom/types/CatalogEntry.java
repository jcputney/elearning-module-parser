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
public class CatalogEntry implements Serializable {

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

  public CatalogEntry() {
  }

  public String getCatalog() {
    return this.catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public UnboundLangString getEntry() {
    return this.entry;
  }

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
