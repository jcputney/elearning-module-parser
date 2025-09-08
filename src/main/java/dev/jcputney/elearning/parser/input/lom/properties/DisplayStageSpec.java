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
 * <p>Represents the <strong>displayStageSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="displayStageSpec">
 *   <xs:all>
 *     <xs:element name="required" type="stageSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="desired" type="stageSpec" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DisplayStageSpec implements Serializable {

  /**
   * The {@code <required>} element, which specifies the required display stage.
   */
  @JacksonXmlProperty(localName = "required")
  private StageSpec required;
  /**
   * The {@code <desired>} element, which specifies the desired display stage.
   */
  @JacksonXmlProperty(localName = "desired")
  private StageSpec desired;

  public DisplayStageSpec() {
  }

  public StageSpec getRequired() {
    return this.required;
  }

  public void setRequired(StageSpec required) {
    this.required = required;
  }

  public StageSpec getDesired() {
    return this.desired;
  }

  public void setDesired(StageSpec desired) {
    this.desired = desired;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DisplayStageSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRequired(), that.getRequired())
        .append(getDesired(), that.getDesired())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRequired())
        .append(getDesired())
        .toHashCode();
  }
}
