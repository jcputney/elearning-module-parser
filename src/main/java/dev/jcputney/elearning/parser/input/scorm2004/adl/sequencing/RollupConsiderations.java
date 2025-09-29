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

package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.RollupConsiderationType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the rollupConsiderationsType complex type, defining attributes for rollup conditions.
 * The following schema snippet shows the structure of the rollupConsiderationsType element:
 * <pre>{@code
 *   <xs:complexType name = "rollupConsiderationsType">
 *      <xs:attribute name = "requiredForSatisfied" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "requiredForNotSatisfied" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "requiredForCompleted" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "requiredForIncomplete" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "measureSatisfactionIfActive" default = "true" type = "xs:boolean"/>
 *   </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RollupConsiderations implements Serializable {

  /**
   * Specifies when satisfaction is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForSatisfied", isAttribute = true)
  @JsonProperty("requiredForSatisfied")
  private RollupConsiderationType requiredForSatisfied = RollupConsiderationType.ALWAYS;

  /**
   * Specifies when not satisfaction is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForNotSatisfied", isAttribute = true)
  @JsonProperty("requiredForNotSatisfied")
  private RollupConsiderationType requiredForNotSatisfied = RollupConsiderationType.ALWAYS;

  /**
   * Specifies when completion is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForCompleted", isAttribute = true)
  @JsonProperty("requiredForCompleted")
  private RollupConsiderationType requiredForCompleted = RollupConsiderationType.ALWAYS;

  /**
   * Specifies when incompletion is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForIncomplete", isAttribute = true)
  @JsonProperty("requiredForIncomplete")
  private RollupConsiderationType requiredForIncomplete = RollupConsiderationType.ALWAYS;

  /**
   * Indicates if satisfaction is measured only when active. Default is true.
   */
  @JacksonXmlProperty(localName = "measureSatisfactionIfActive", isAttribute = true)
  @JsonProperty("measureSatisfactionIfActive")
  private boolean measureSatisfactionIfActive = true;

  public RollupConsiderations() {
    // no-op
  }

  /**
   * Retrieves the rollup consideration type required for satisfaction.
   *
   * @return the RollupConsiderationType that specifies the condition required for satisfaction.
   */
  public RollupConsiderationType getRequiredForSatisfied() {
    return this.requiredForSatisfied;
  }

  /**
   * Sets the rollup consideration type required for marking satisfaction.
   *
   * @param requiredForSatisfied the RollupConsiderationType that specifies the condition required
   * for satisfaction. It determines the criteria under which a rollup satisfaction is considered.
   */
  public void setRequiredForSatisfied(RollupConsiderationType requiredForSatisfied) {
    this.requiredForSatisfied = requiredForSatisfied;
  }

  /**
   * Retrieves the rollup consideration type required for non-satisfaction.
   *
   * @return the RollupConsiderationType that specifies the condition required for determining
   * non-satisfaction.
   */
  public RollupConsiderationType getRequiredForNotSatisfied() {
    return this.requiredForNotSatisfied;
  }

  /**
   * Sets the rollup consideration type required for marking non-satisfaction.
   *
   * @param requiredForNotSatisfied the RollupConsiderationType that specifies the condition
   * required for determining non-satisfaction. It defines the criteria under which a rollup
   * non-satisfaction is considered.
   */
  public void setRequiredForNotSatisfied(RollupConsiderationType requiredForNotSatisfied) {
    this.requiredForNotSatisfied = requiredForNotSatisfied;
  }

  /**
   * Retrieves the rollup consideration type required for marking completion.
   *
   * @return the RollupConsiderationType that specifies the condition required for determining
   * completion.
   */
  public RollupConsiderationType getRequiredForCompleted() {
    return this.requiredForCompleted;
  }

  /**
   * Sets the rollup consideration type required for marking completion.
   *
   * @param requiredForCompleted the RollupConsiderationType that specifies the condition required
   * for determining completion. It defines the criteria under which a rollup completion is
   * considered.
   */
  public void setRequiredForCompleted(RollupConsiderationType requiredForCompleted) {
    this.requiredForCompleted = requiredForCompleted;
  }

  /**
   * Retrieves the rollup consideration type required for marking incompletion.
   *
   * @return the RollupConsiderationType that specifies the condition required for determining
   * incompletion.
   */
  public RollupConsiderationType getRequiredForIncomplete() {
    return this.requiredForIncomplete;
  }

  /**
   * Sets the rollup consideration type required for marking incompletion.
   *
   * @param requiredForIncomplete the RollupConsiderationType that specifies the condition required
   * for determining incompletion. It defines the criteria under which a rollup incompletion is
   * considered.
   */
  public void setRequiredForIncomplete(RollupConsiderationType requiredForIncomplete) {
    this.requiredForIncomplete = requiredForIncomplete;
  }

  /**
   * Determines whether satisfaction measurement is enabled when active.
   *
   * @return true if satisfaction measurement is enabled when active, false otherwise.
   */
  public boolean isMeasureSatisfactionIfActive() {
    return this.measureSatisfactionIfActive;
  }

  /**
   * Sets whether satisfaction measurement is enabled when active.
   *
   * @param measureSatisfactionIfActive a boolean value indicating whether satisfaction measurement
   * should be enabled when active. If true, satisfaction measurement will be active; otherwise, it
   * will not be measured.
   */
  public void setMeasureSatisfactionIfActive(boolean measureSatisfactionIfActive) {
    this.measureSatisfactionIfActive = measureSatisfactionIfActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RollupConsiderations that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isMeasureSatisfactionIfActive(), that.isMeasureSatisfactionIfActive())
        .append(getRequiredForSatisfied(), that.getRequiredForSatisfied())
        .append(getRequiredForNotSatisfied(), that.getRequiredForNotSatisfied())
        .append(getRequiredForCompleted(), that.getRequiredForCompleted())
        .append(getRequiredForIncomplete(), that.getRequiredForIncomplete())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRequiredForSatisfied())
        .append(getRequiredForNotSatisfied())
        .append(getRequiredForCompleted())
        .append(getRequiredForIncomplete())
        .append(isMeasureSatisfactionIfActive())
        .toHashCode();
  }
}
