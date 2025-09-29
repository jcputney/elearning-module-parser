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
 * <p>Represents the <strong>exitActionsSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="exitActionsSpec">
 *   <xs:all>
 *     <xs:element name="intermediateSco" type="scoExitActionSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="finalSco" type="scoExitActionSpec" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ExitActionsSpec implements Serializable {

  /**
   * The exit action for the intermediate SCO.
   */
  @JacksonXmlProperty(localName = "intermediateSco")
  private ScoExitActionSpec intermediateSco;

  /**
   * The exit action for the final SCO.
   */
  @JacksonXmlProperty(localName = "finalSco")
  private ScoExitActionSpec finalSco;

  public ExitActionsSpec() {
    // no-op
  }

  /**
   * Retrieves the exit action specification for the intermediate SCO.
   *
   * @return the {@code ScoExitActionSpec} representing the exit action for the intermediate SCO, or
   * {@code null} if no action has been specified.
   */
  public ScoExitActionSpec getIntermediateSco() {
    return this.intermediateSco;
  }

  /**
   * Sets the exit action specification for the intermediate SCO.
   *
   * @param intermediateSco the {@code ScoExitActionSpec} instance defining the exit action for the
   * intermediate SCO
   */
  public void setIntermediateSco(ScoExitActionSpec intermediateSco) {
    this.intermediateSco = intermediateSco;
  }

  /**
   * Retrieves the exit action specification for the final SCO.
   *
   * @return the {@code ScoExitActionSpec} representing the exit action for the final SCO, or
   * {@code null} if no action has been specified.
   */
  public ScoExitActionSpec getFinalSco() {
    return this.finalSco;
  }

  /**
   * Sets the exit action specification for the final SCO.
   *
   * @param finalSco the {@code ScoExitActionSpec} instance defining the exit action for the final
   * SCO
   */
  public void setFinalSco(ScoExitActionSpec finalSco) {
    this.finalSco = finalSco;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ExitActionsSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIntermediateSco(), that.getIntermediateSco())
        .append(getFinalSco(), that.getFinalSco())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIntermediateSco())
        .append(getFinalSco())
        .toHashCode();
  }
}