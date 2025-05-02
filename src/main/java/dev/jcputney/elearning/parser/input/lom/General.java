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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.AggregationLevel;
import dev.jcputney.elearning.parser.input.lom.types.CatalogEntry;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.Structure;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the <code>general</code> element in the LOM schema, which provides general information
 * about a learning object. This includes its identifiers, titles, language, descriptions, keywords,
 * and other general metadata.
 *
 * <p>The following schema snippet defines the <code>general</code> element:</p>
 * <pre>{@code
 * <xs:complexType name="general">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="identifier"/>
 *     <xs:group ref="title"/>
 *     <xs:group ref="languageIdOrNone"/>
 *     <xs:group ref="descriptionUnbounded"/>
 *     <xs:group ref="keyword"/>
 *     <xs:group ref="coverage"/>
 *     <xs:group ref="structure"/>
 *     <xs:group ref="aggregationLevel"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:general"/>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class General {

  /**
   * A list of identifiers that uniquely identify the learning object.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="identifier"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "identifier", useWrapping = false)
  @JacksonXmlProperty(localName = "identifier")
  private List<Identifier> identifiers;
  /**
   * A list of titles for the learning object. Titles are represented as language-specific strings.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="title"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private UnboundLangString title;
  /**
   * A list of catalog entries for the learning object. Catalog entries provide information about
   * the learning object in a structured format.
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
   * The primary language of the learning object. This can also indicate the absence of a language.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="languageIdOrNone"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "language")
  private String language;
  /**
   * A wrapper for a list of descriptions for the learning object, represented as language-specific
   * strings.
   */
  @JacksonXmlProperty(localName = "description")
  private UnboundLangString description;
  /**
   * A wrapper for a list of keywords or phrases describing the content of the learning object,
   * represented as language-specific strings.
   */
  @JacksonXmlElementWrapper(localName = "keyword", useWrapping = false)
  @JacksonXmlProperty(localName = "keyword")
  private List<SingleLangString> keywords;
  /**
   * A wrapper for a list of coverage statements for the learning object, represented as
   * language-specific strings. Coverage defines the extent or scope of the content.
   */
  @JacksonXmlProperty(localName = "coverage")
  private UnboundLangString coverage;
  /**
   * The structure of the learning object, such as hierarchical or linear. Represented as a
   * source-value pair.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="structure"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "structure", useWrapping = false)
  @JacksonXmlProperty(localName = "structure")
  private SourceValuePair<Structure> structure;
  /**
   * The aggregation level of the learning object, indicating its granularity or size. Represented
   * as a source-value pair.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="aggregationLevel"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "aggregationLevel", useWrapping = false)
  @JacksonXmlProperty(localName = "aggregationLevel")
  private SourceValuePair<AggregationLevel> aggregationLevel;

  /**
   * Default constructor for the General class.
   */
  public General() {
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

    General general = (General) o;

    return new EqualsBuilder()
        .append(identifiers, general.identifiers)
        .append(title, general.title)
        .append(catalogEntries, general.catalogEntries)
        .append(language, general.language)
        .append(description, general.description)
        .append(keywords, general.keywords)
        .append(coverage, general.coverage)
        .append(structure, general.structure)
        .append(aggregationLevel, general.aggregationLevel)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(identifiers)
        .append(title)
        .append(catalogEntries)
        .append(language)
        .append(description)
        .append(keywords)
        .append(coverage)
        .append(structure)
        .append(aggregationLevel)
        .toHashCode();
  }
}
