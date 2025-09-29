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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.AggregationLevel;
import dev.jcputney.elearning.parser.input.lom.types.CatalogEntry;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.Structure;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import java.util.List;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class General implements Serializable {

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
  private List<UnboundLangString> keywords;

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

  public General(List<Identifier> identifiers, UnboundLangString title,
      List<CatalogEntry> catalogEntries, String language, UnboundLangString description,
      List<UnboundLangString> keywords, UnboundLangString coverage,
      SourceValuePair<Structure> structure, SourceValuePair<AggregationLevel> aggregationLevel) {
    this.identifiers = identifiers;
    this.title = title;
    this.catalogEntries = catalogEntries;
    this.language = language;
    this.description = description;
    this.keywords = keywords;
    this.coverage = coverage;
    this.structure = structure;
    this.aggregationLevel = aggregationLevel;
  }

  public General() {
    // no-op
  }

  /**
   * Retrieves the list of identifiers associated with the instance.
   *
   * @return a list of {@link Identifier} objects representing the identifiers
   */
  public List<Identifier> getIdentifiers() {
    return this.identifiers;
  }

  /**
   * Sets the list of identifiers associated with the instance.
   *
   * @param identifiers a list of {@link Identifier} objects representing the identifiers to be set
   */
  public void setIdentifiers(List<Identifier> identifiers) {
    this.identifiers = identifiers;
  }

  /**
   * Retrieves the title associated with this object.
   *
   * @return an {@code UnboundLangString} representing the title, which may include multiple string
   * values with language attributes
   */
  public UnboundLangString getTitle() {
    return this.title;
  }

  /**
   * Sets the title associated with this object.
   *
   * @param title an {@code UnboundLangString} representing the title, which may include multiple
   * string values with language attributes
   */
  public void setTitle(UnboundLangString title) {
    this.title = title;
  }

  /**
   * Retrieves the list of catalog entries associated with the instance.
   *
   * @return a list of {@code CatalogEntry} objects representing catalog entries
   */
  public List<CatalogEntry> getCatalogEntries() {
    return this.catalogEntries;
  }

  /**
   * Sets the list of catalog entries associated with the instance.
   *
   * @param catalogEntries a list of {@link CatalogEntry} objects representing the catalog entries
   * to be set
   */
  public void setCatalogEntries(List<CatalogEntry> catalogEntries) {
    this.catalogEntries = catalogEntries;
  }

  /**
   * Retrieves the language associated with this object.
   *
   * @return a {@code String} representing the language.
   */
  public String getLanguage() {
    return this.language;
  }

  /**
   * Sets the language associated with this object.
   *
   * @param language a {@code String} representing the language to be set
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Retrieves the description associated with this object.
   *
   * @return an {@code UnboundLangString} representing the description, which may include multiple
   * string values with language attributes
   */
  public UnboundLangString getDescription() {
    return this.description;
  }

  /**
   * Sets the description for this object.
   *
   * @param description an {@code UnboundLangString} representing the description, which may include
   * multiple string values with language attributes
   */
  public void setDescription(UnboundLangString description) {
    this.description = description;
  }

  /**
   * Retrieves the list of keywords associated with this object.
   *
   * @return a list of {@code UnboundLangString} objects representing the keywords, which may
   * include multiple string values with language attributes
   */
  public List<UnboundLangString> getKeywords() {
    return this.keywords;
  }

  /**
   * Sets the list of keywords associated with this object.
   *
   * @param keywords a list of {@code UnboundLangString} objects representing the keywords, which
   * may include multiple string values with language attributes
   */
  public void setKeywords(List<UnboundLangString> keywords) {
    this.keywords = keywords;
  }

  /**
   * Retrieves the coverage information associated with this object.
   *
   * @return an {@code UnboundLangString} representing the coverage, which may include multiple
   * string values with language attributes.
   */
  public UnboundLangString getCoverage() {
    return this.coverage;
  }

  /**
   * Sets the coverage information associated with this object.
   *
   * @param coverage an {@code UnboundLangString} representing the coverage, which may include
   * multiple string values with language attributes.
   */
  public void setCoverage(UnboundLangString coverage) {
    this.coverage = coverage;
  }

  /**
   * Retrieves the structure of the learning object associated with this instance.
   *
   * @return a {@code SourceValuePair<Structure>} representing the structure, which indicates the
   * organizational form of the learning object.
   */
  public SourceValuePair<Structure> getStructure() {
    return this.structure;
  }

  /**
   * Sets the structure of the learning object associated with this instance.
   *
   * @param structure a {@code SourceValuePair<Structure>} representing the structure to be set.
   * This indicates the organizational form of the learning object, such as atomic, collection,
   * networked, hierarchical, or linear.
   */
  public void setStructure(
      SourceValuePair<Structure> structure) {
    this.structure = structure;
  }

  /**
   * Retrieves the aggregation level associated with this object.
   *
   * @return a {@code SourceValuePair<AggregationLevel>} representing the aggregation level, which
   * specifies the level of granularity or complexity of the learning object.
   */
  public SourceValuePair<AggregationLevel> getAggregationLevel() {
    return this.aggregationLevel;
  }

  /**
   * Sets the aggregation level for the learning object. The aggregation level specifies the level
   * of granularity or complexity of the learning object.
   *
   * @param aggregationLevel a {@code SourceValuePair<AggregationLevel>} representing the
   * aggregation level to be set. Possible values include LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, or
   * UNKNOWN.
   */
  public void setAggregationLevel(
      SourceValuePair<AggregationLevel> aggregationLevel) {
    this.aggregationLevel = aggregationLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof General general)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifiers(), general.getIdentifiers())
        .append(getTitle(), general.getTitle())
        .append(getCatalogEntries(), general.getCatalogEntries())
        .append(getLanguage(), general.getLanguage())
        .append(getDescription(), general.getDescription())
        .append(getKeywords(), general.getKeywords())
        .append(getCoverage(), general.getCoverage())
        .append(getStructure(), general.getStructure())
        .append(getAggregationLevel(), general.getAggregationLevel())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifiers())
        .append(getTitle())
        .append(getCatalogEntries())
        .append(getLanguage())
        .append(getDescription())
        .append(getKeywords())
        .append(getCoverage())
        .append(getStructure())
        .append(getAggregationLevel())
        .toHashCode();
  }
}
