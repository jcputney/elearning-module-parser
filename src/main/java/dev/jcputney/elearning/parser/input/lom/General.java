/*
 * Copyright (c) 2024. Jonathan Putney
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.AggregationLevel;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.Structure;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import lombok.Data;

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
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
}
