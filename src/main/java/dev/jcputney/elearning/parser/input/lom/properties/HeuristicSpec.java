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
public final class HeuristicSpec implements Serializable {

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

  /**
   * Constructs an instance of {@code HeuristicSpec} with specified tracking and meaningfulness
   * settings.
   *
   * @param isCompletionTracked an instance of {@code YesNoType} indicating whether completion
   * tracking is enabled.
   * @param isSatisfactionTracked an instance of {@code YesNoType} indicating whether satisfaction
   * tracking is enabled.
   * @param isScoreTracked an instance of {@code YesNoType} indicating whether score tracking is
   * enabled.
   * @param isIncompleteScoreMeaningful an instance of {@code YesNoType} indicating whether an
   * incomplete score is considered meaningful.
   * @param isIncompleteSatisfactionMeaningful an instance of {@code YesNoType} indicating whether
   * incomplete satisfaction is considered meaningful.
   */
  public HeuristicSpec(YesNoType isCompletionTracked, YesNoType isSatisfactionTracked,
      YesNoType isScoreTracked, YesNoType isIncompleteScoreMeaningful,
      YesNoType isIncompleteSatisfactionMeaningful) {
    this.isCompletionTracked = isCompletionTracked;
    this.isSatisfactionTracked = isSatisfactionTracked;
    this.isScoreTracked = isScoreTracked;
    this.isIncompleteScoreMeaningful = isIncompleteScoreMeaningful;
    this.isIncompleteSatisfactionMeaningful = isIncompleteSatisfactionMeaningful;
  }

  /**
   * Default constructor for the {@code HeuristicSpec} class.
   * <p>
   * Initializes an instance of {@code HeuristicSpec} with default values for tracking and
   * meaningfulness settings. This constructor performs no additional operations.
   */
  public HeuristicSpec() {
    // no-op
  }

  /**
   * Retrieves the value indicating whether completion tracking is enabled.
   *
   * @return an instance of {@link YesNoType} representing if completion is tracked.
   */
  public YesNoType getIsCompletionTracked() {
    return this.isCompletionTracked;
  }

  /**
   * Sets whether completion tracking is enabled or disabled.
   *
   * @param isCompletionTracked an instance of {@link YesNoType} representing whether completion
   * tracking is enabled
   */
  public void setIsCompletionTracked(YesNoType isCompletionTracked) {
    this.isCompletionTracked = isCompletionTracked;
  }

  /**
   * Retrieves the value indicating whether satisfaction tracking is enabled.
   *
   * @return an instance of {@link YesNoType} representing if satisfaction is tracked.
   */
  public YesNoType getIsSatisfactionTracked() {
    return this.isSatisfactionTracked;
  }

  /**
   * Sets whether satisfaction tracking is enabled or disabled.
   *
   * @param isSatisfactionTracked an instance of {@link YesNoType} representing whether satisfaction
   * tracking is enabled
   */
  public void setIsSatisfactionTracked(YesNoType isSatisfactionTracked) {
    this.isSatisfactionTracked = isSatisfactionTracked;
  }

  /**
   * Retrieves the value indicating whether score tracking is enabled.
   *
   * @return an instance of {@link YesNoType} representing whether score tracking is enabled.
   */
  public YesNoType getIsScoreTracked() {
    return this.isScoreTracked;
  }

  /**
   * Sets whether score tracking is enabled or disabled.
   *
   * @param isScoreTracked an instance of {@link YesNoType} representing whether score tracking is
   * enabled
   */
  public void setIsScoreTracked(YesNoType isScoreTracked) {
    this.isScoreTracked = isScoreTracked;
  }

  /**
   * Retrieves the value indicating whether an incomplete score is meaningful.
   *
   * @return an instance of {@link YesNoType} representing whether an incomplete score is considered
   * meaningful.
   */
  public YesNoType getIsIncompleteScoreMeaningful() {
    return this.isIncompleteScoreMeaningful;
  }

  /**
   * Sets whether an incomplete score is considered meaningful.
   *
   * @param isIncompleteScoreMeaningful an instance of {@link YesNoType} representing if an
   * incomplete score is meaningful
   */
  public void setIsIncompleteScoreMeaningful(
      YesNoType isIncompleteScoreMeaningful) {
    this.isIncompleteScoreMeaningful = isIncompleteScoreMeaningful;
  }

  /**
   * Retrieves the value indicating whether an incomplete satisfaction is considered meaningful.
   *
   * @return an instance of {@link YesNoType} representing whether incomplete satisfaction is
   * meaningful.
   */
  public YesNoType getIsIncompleteSatisfactionMeaningful() {
    return this.isIncompleteSatisfactionMeaningful;
  }

  /**
   * Sets whether incomplete satisfaction is considered meaningful.
   *
   * @param isIncompleteSatisfactionMeaningful an instance of {@link YesNoType} representing whether
   * incomplete satisfaction is meaningful
   */
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