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
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the constrainedChoiceConsiderationsType complex type, defining choice and activation
 * restrictions. The following schema snippet shows the structure of the
 * constrainedChoiceConsiderationsType element:
 * <pre>{@code
 *   <xs:complexType name = "constrainChoiceConsiderationsType">
 *      <xs:attribute name = "preventActivation" default = "false" type = "xs:boolean"/>
 *      <xs:attribute name = "constrainChoice" default = "false" type = "xs:boolean"/>
 *   </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ConstrainChoiceConsiderations implements Serializable {

  /**
   * Prevents activation when true. Default is false.
   */
  @JacksonXmlProperty(localName = "preventActivation", isAttribute = true)
  @JsonProperty("preventActivation")
  private boolean preventActivation = false;

  /**
   * Constrains choice when true. Default is false.
   */
  @JacksonXmlProperty(localName = "constrainChoice", isAttribute = true)
  @JsonProperty("constrainChoice")
  private boolean constrainChoice = false;

  public ConstrainChoiceConsiderations() {
    // no-op
  }

  /**
   * Indicates whether activation is prevented.
   *
   * @return true if activation is prevented, false otherwise.
   */
  public boolean isPreventActivation() {
    return this.preventActivation;
  }

  /**
   * Sets whether to prevent activation.
   *
   * @param preventActivation true to prevent activation, false otherwise.
   */
  public void setPreventActivation(boolean preventActivation) {
    this.preventActivation = preventActivation;
  }

  /**
   * Checks whether choice is constrained based on the current state of the object.
   *
   * @return true if choice is constrained, false otherwise.
   */
  public boolean isConstrainChoice() {
    return this.constrainChoice;
  }

  /**
   * Sets whether the choice should be constrained.
   *
   * @param constrainChoice true if the choice is to be constrained, false otherwise.
   */
  public void setConstrainChoice(boolean constrainChoice) {
    this.constrainChoice = constrainChoice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ConstrainChoiceConsiderations that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isPreventActivation(), that.isPreventActivation())
        .append(isConstrainChoice(), that.isConstrainChoice())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(isPreventActivation())
        .append(isConstrainChoice())
        .toHashCode();
  }
}
