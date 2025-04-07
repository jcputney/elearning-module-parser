/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.lom.properties;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>Represents the <strong>heuristicSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="heuristicSpec">
 *   <xs:all>
 *     <xs:element name="isCompletionTracked" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="isSatisfactionTracked" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="isScoreTracked" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="isIncompleteScoreMeaningful" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="isIncompleteSatisfactionMeaningful" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class HeuristicSpec {

  /**
   * Indicates whether completion is tracked.
   */
  @JacksonXmlProperty(localName = "isCompletionTracked")
  private YesNoType isCompletionTracked;
  /**
   * Indicates whether satisfaction is tracked.
   */
  @JacksonXmlProperty(localName = "isSatisfactionTracked")
  private YesNoType isSatisfactionTracked;
  /**
   * Indicates whether score is tracked.
   */
  @JacksonXmlProperty(localName = "isScoreTracked")
  private YesNoType isScoreTracked;
  /**
   * Indicates whether incomplete score is meaningful.
   */
  @JacksonXmlProperty(localName = "isIncompleteScoreMeaningful")
  private YesNoType isIncompleteScoreMeaningful;
  /**
   * Indicates whether incomplete satisfaction is meaningful.
   */
  @JacksonXmlProperty(localName = "isIncompleteSatisfactionMeaningful")
  private YesNoType isIncompleteSatisfactionMeaningful;

  /**
   * Default constructor for the HeuristicSpec class.
   */
  public HeuristicSpec() {
    // Default constructor
  }
}