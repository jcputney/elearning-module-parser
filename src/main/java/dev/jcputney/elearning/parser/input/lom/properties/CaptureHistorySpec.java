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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class CaptureHistorySpec implements Serializable {

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
   * Default constructor for the CaptureHistorySpec class.
   */
  @SuppressWarnings("unused")
  public CaptureHistorySpec() {
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

    CaptureHistorySpec that = (CaptureHistorySpec) o;

    return new EqualsBuilder()
        .append(captureHistory, that.captureHistory)
        .append(captureHistoryDetailed, that.captureHistoryDetailed)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(captureHistory)
        .append(captureHistoryDetailed)
        .toHashCode();
  }
}
