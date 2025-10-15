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

package dev.jcputney.elearning.parser.input.scorm2004.adl.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.common.PercentType;
import dev.jcputney.elearning.parser.input.common.PercentTypeDeserializer;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureTypeDeserializer;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the completion threshold element, which specifies the minimum progress required to
 * mark the content as complete. Includes attributes for progress weight and a boolean indicating if
 * completion is by measure.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class CompletionThreshold implements Serializable {

  /**
   * Defines the minimum progress measure for the content to be marked complete. The value should be
   * between -1.0 and 1.0, representing the fraction of content that must be completed.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "minProgressMeasure")
  @JsonDeserialize(using = MeasureTypeDeserializer.class)
  @JsonProperty("minProgressMeasure")
  private MeasureType minProgressMeasure = new MeasureType(BigDecimal.ONE);

  /**
   * Specifies the relative weight of this content item towards overall progress. The value is
   * between 0.0 and 1.0, where 1.0 indicates the full weight.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "progressWeight")
  @JsonDeserialize(using = PercentTypeDeserializer.class)
  @JsonProperty("progressWeight")
  private PercentType progressWeight = new PercentType(BigDecimal.ONE);

  /**
   * Indicates whether a measure determines completion. If true, completion is based on achieving
   * the minimum progress measure defined.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "completedByMeasure")
  @JsonProperty("completedByMeasure")
  private Boolean completedByMeasure = false;

  public CompletionThreshold(MeasureType minProgressMeasure, PercentType progressWeight,
      Boolean completedByMeasure) {
    this.minProgressMeasure = minProgressMeasure;
    this.progressWeight = progressWeight;
    this.completedByMeasure = completedByMeasure;
  }

  public CompletionThreshold() {
    // no-op
  }

  /**
   * Retrieves the minimum progress measure for the completion threshold.
   *
   * @return the minimum progress measure as a {@code MeasureType} instance.
   */
  public MeasureType getMinProgressMeasure() {
    return this.minProgressMeasure;
  }

  /**
   * Sets the minimum progress measure for the completion threshold. If the provided measure is
   * null, the method does nothing.
   *
   * @param minProgressMeasure the minimum progress measure to be set, represented as a
   * {@code MeasureType}
   */
  public void setMinProgressMeasure(MeasureType minProgressMeasure) {
    if (minProgressMeasure == null) {
      return;
    }
    this.minProgressMeasure = minProgressMeasure;
  }

  /**
   * Retrieves the progress weight associated with the completion threshold.
   *
   * @return the progress weight as a {@code PercentType} instance, representing a percentage value
   * between 0 and 1 inclusive.
   */
  public PercentType getProgressWeight() {
    return this.progressWeight;
  }

  /**
   * Sets the progress weight for the completion threshold. The progress weight is represented as a
   * percentage value between 0 and 1, inclusive.
   *
   * @param progressWeight the progress weight to be assigned, represented as a {@code PercentType}.
   * Must be a value between 0 and 1; otherwise, an exception will be thrown.
   */
  public void setProgressWeight(PercentType progressWeight) {
    this.progressWeight = progressWeight;
  }

  /**
   * Retrieves the value indicating whether completion is determined by the measure.
   *
   * @return a {@code Boolean} value representing whether completion is based on the measure;
   * returns {@code true} if completion is determined by the measure, otherwise {@code false}.
   */
  public Boolean getCompletedByMeasure() {
    return this.completedByMeasure;
  }

  /**
   * Sets the value indicating whether completion is determined by the measure.
   *
   * @param completedByMeasure a {@code Boolean} value that specifies whether completion is based on
   * the measure. Pass {@code true} if completion should depend on the measure, otherwise
   * {@code false}.
   */
  public void setCompletedByMeasure(Boolean completedByMeasure) {
    this.completedByMeasure = completedByMeasure;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CompletionThreshold that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getMinProgressMeasure(),
            that.getMinProgressMeasure())
        .append(getProgressWeight(), that.getProgressWeight())
        .append(getCompletedByMeasure(), that.getCompletedByMeasure())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getMinProgressMeasure())
        .append(getProgressWeight())
        .append(getCompletedByMeasure())
        .toHashCode();
  }
}
