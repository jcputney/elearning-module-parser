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

import static lombok.AccessLevel.PRIVATE;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the completion threshold element, which specifies the minimum progress required to
 * mark the content as complete. Includes attributes for progress weight and a boolean indicating if
 * completion is by measure.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class CompletionThreshold implements Serializable {

  /**
   * Defines the minimum progress measure for the content to be marked complete. The value should be
   * between -1.0 and 1.0, representing the fraction of content that must be completed.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "minProgressMeasure")
  @JsonDeserialize(using = MeasureTypeDeserializer.class)
  @JsonProperty("minProgressMeasure")
  @Default
  private MeasureType minProgressMeasure = new MeasureType(BigDecimal.ONE);
  /**
   * Specifies the relative weight of this content item towards overall progress. The value is
   * between 0.0 and 1.0, where 1.0 indicates the full weight.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "progressWeight")
  @JsonDeserialize(using = PercentTypeDeserializer.class)
  @JsonProperty("progressWeight")
  @Default
  private PercentType progressWeight = new PercentType(BigDecimal.ONE);
  /**
   * Indicates whether measure determines completion. If true, completion is based on achieving the
   * minimum progress measure defined.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "completedByMeasure")
  @JsonProperty("completedByMeasure")
  @Default
  private Boolean completedByMeasure = false;

  /**
   * Default constructor for the CompletionThreshold class.
   */
  @SuppressWarnings("unused")
  public CompletionThreshold() {
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

    CompletionThreshold that = (CompletionThreshold) o;

    return new EqualsBuilder()
        .append(minProgressMeasure, that.minProgressMeasure)
        .append(progressWeight, that.progressWeight)
        .append(completedByMeasure, that.completedByMeasure)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(minProgressMeasure)
        .append(progressWeight)
        .append(completedByMeasure)
        .toHashCode();
  }
}
