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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>captureHistorySpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="captureHistorySpec">
 *   <xs:all>
 *     <xs:element name="captureHistory" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="captureHistoryDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class CaptureHistorySpec implements Serializable {

  /**
   * The capture history flag.
   */
  @JacksonXmlProperty(localName = "captureHistory")
  private YesNoType captureHistory;

  /**
   * The detailed capture history flag.
   */
  @JacksonXmlProperty(localName = "captureHistoryDetailed")
  private YesNoType captureHistoryDetailed;

  /**
   * Constructs a new instance of the {@code CaptureHistorySpec} class with the specified values.
   *
   * @param captureHistory the capture history flag, which indicates whether capture history is
   * enabled or not. The value should be of type {@code YesNoType}.
   * @param captureHistoryDetailed the detailed capture history flag, which specifies whether
   * detailed capture history is enabled or not. The value should be of type {@code YesNoType}.
   */
  public CaptureHistorySpec(YesNoType captureHistory, YesNoType captureHistoryDetailed) {
    this.captureHistory = captureHistory;
    this.captureHistoryDetailed = captureHistoryDetailed;
  }

  /**
   * Default constructor for the {@code CaptureHistorySpec} class.
   * <p>
   * Creates an instance of {@code CaptureHistorySpec} with no initial values set. This constructor
   * is primarily used when no specific initialization is required or when values will be set
   * manually after object creation.
   */
  public CaptureHistorySpec() {
    // no-op
  }

  /**
   * Retrieves the captureHistory value.
   *
   * @return the capture history flag, which indicates whether capture history is enabled or not.
   */
  public YesNoType getCaptureHistory() {
    return this.captureHistory;
  }

  /**
   * Sets the capture history flag, which indicates whether capture history should be enabled or
   * not.
   *
   * @param captureHistory the capture history flag, represented as a {@code YesNoType}. Acceptable
   * values include "yes", "no", "true", or "false".
   */
  public void setCaptureHistory(YesNoType captureHistory) {
    this.captureHistory = captureHistory;
  }

  /**
   * Retrieves the detailed capture history flag.
   *
   * @return the detailed capture history flag, indicating whether detailed capture history is
   * enabled or not.
   */
  public YesNoType getCaptureHistoryDetailed() {
    return this.captureHistoryDetailed;
  }

  /**
   * Sets the detailed capture history flag, specifying whether detailed capture history is enabled
   * or not.
   *
   * @param captureHistoryDetailed the detailed capture history flag, represented as a
   * {@code YesNoType}. Acceptable values are "yes", "no", "true", or "false".
   */
  public void setCaptureHistoryDetailed(YesNoType captureHistoryDetailed) {
    this.captureHistoryDetailed = captureHistoryDetailed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CaptureHistorySpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCaptureHistory(), that.getCaptureHistory())
        .append(getCaptureHistoryDetailed(), that.getCaptureHistoryDetailed())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCaptureHistory())
        .append(getCaptureHistoryDetailed())
        .toHashCode();
  }
}
