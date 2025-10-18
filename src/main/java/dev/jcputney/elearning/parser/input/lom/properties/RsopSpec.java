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
 * <p>Represents the <strong>rsopSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="rsopSpec">
 *   <xs:all>
 *     <xs:element name="offlineSynchMode" type="offlineSynchModeType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RsopSpec implements Serializable {

  /**
   * The offline synchronization mode for the resource.
   */
  @JacksonXmlProperty(localName = "offlineSynchMode")
  private OfflineSynchModeType offlineSynchMode;

  /**
   * Default constructor for the RsopSpec class. Initializes a new instance of the RsopSpec class
   * with no specific configuration.
   */
  public RsopSpec() {
    // no-op
  }

  /**
   * Retrieves the offline synchronization mode associated with the current resource.
   *
   * @return the offline synchronization mode as an {@code OfflineSynchModeType}.
   */
  public OfflineSynchModeType getOfflineSynchMode() {
    return this.offlineSynchMode;
  }

  /**
   * Sets the offline synchronization mode for the resource.
   *
   * @param offlineSynchMode the offline synchronization mode to be applied, represented as an
   * {@code OfflineSynchModeType}.
   */
  public void setOfflineSynchMode(OfflineSynchModeType offlineSynchMode) {
    this.offlineSynchMode = offlineSynchMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RsopSpec rsopSpec)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getOfflineSynchMode(), rsopSpec.getOfflineSynchMode())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getOfflineSynchMode())
        .toHashCode();
  }
}