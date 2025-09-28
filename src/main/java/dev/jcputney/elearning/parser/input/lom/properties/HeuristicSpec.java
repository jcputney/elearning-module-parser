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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class HeuristicSpec implements Serializable {

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
   * Indicates whether the score is tracked.
   */
  @JacksonXmlProperty(localName = "isScoreTracked")
  private YesNoType isScoreTracked;

  /**
   * Indicates whether an incomplete score is meaningful.
   */
  @JacksonXmlProperty(localName = "isIncompleteScoreMeaningful")
  private YesNoType isIncompleteScoreMeaningful;

  /**
   * Indicates whether incomplete satisfaction is meaningful.
   */
  @JacksonXmlProperty(localName = "isIncompleteSatisfactionMeaningful")
  private YesNoType isIncompleteSatisfactionMeaningful;

  public HeuristicSpec(YesNoType isCompletionTracked, YesNoType isSatisfactionTracked,
      YesNoType isScoreTracked, YesNoType isIncompleteScoreMeaningful,
      YesNoType isIncompleteSatisfactionMeaningful) {
    this.isCompletionTracked = isCompletionTracked;
    this.isSatisfactionTracked = isSatisfactionTracked;
    this.isScoreTracked = isScoreTracked;
    this.isIncompleteScoreMeaningful = isIncompleteScoreMeaningful;
    this.isIncompleteSatisfactionMeaningful = isIncompleteSatisfactionMeaningful;
  }

  public HeuristicSpec() {
    // no-op
  }

  public YesNoType getIsCompletionTracked() {
    return this.isCompletionTracked;
  }

  public void setIsCompletionTracked(YesNoType isCompletionTracked) {
    this.isCompletionTracked = isCompletionTracked;
  }

  public YesNoType getIsSatisfactionTracked() {
    return this.isSatisfactionTracked;
  }

  public void setIsSatisfactionTracked(YesNoType isSatisfactionTracked) {
    this.isSatisfactionTracked = isSatisfactionTracked;
  }

  public YesNoType getIsScoreTracked() {
    return this.isScoreTracked;
  }

  public void setIsScoreTracked(YesNoType isScoreTracked) {
    this.isScoreTracked = isScoreTracked;
  }

  public YesNoType getIsIncompleteScoreMeaningful() {
    return this.isIncompleteScoreMeaningful;
  }

  public void setIsIncompleteScoreMeaningful(
      YesNoType isIncompleteScoreMeaningful) {
    this.isIncompleteScoreMeaningful = isIncompleteScoreMeaningful;
  }

  public YesNoType getIsIncompleteSatisfactionMeaningful() {
    return this.isIncompleteSatisfactionMeaningful;
  }

  public void setIsIncompleteSatisfactionMeaningful(
      YesNoType isIncompleteSatisfactionMeaningful) {
    this.isIncompleteSatisfactionMeaningful = isIncompleteSatisfactionMeaningful;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof HeuristicSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIsCompletionTracked(), that.getIsCompletionTracked())
        .append(getIsSatisfactionTracked(), that.getIsSatisfactionTracked())
        .append(getIsScoreTracked(), that.getIsScoreTracked())
        .append(getIsIncompleteScoreMeaningful(), that.getIsIncompleteScoreMeaningful())
        .append(getIsIncompleteSatisfactionMeaningful(),
            that.getIsIncompleteSatisfactionMeaningful())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIsCompletionTracked())
        .append(getIsSatisfactionTracked())
        .append(getIsScoreTracked())
        .append(getIsIncompleteScoreMeaningful())
        .append(getIsIncompleteSatisfactionMeaningful())
        .toHashCode();
  }
}