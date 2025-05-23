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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
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
  private SingleLangString entry;

  /**
   * Default constructor for the CatalogEntry class.
   */
  @SuppressWarnings("unused")
  public CatalogEntry() {
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

    CatalogEntry that = (CatalogEntry) o;

    return new EqualsBuilder()
        .append(catalog, that.catalog)
        .append(entry, that.entry)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(catalog)
        .append(entry)
        .toHashCode();
  }
}
