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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ConstrainChoiceConsiderations implements Serializable {

  /**
   * Prevents activation when true. Default is false.
   */
  @JacksonXmlProperty(localName = "preventActivation", isAttribute = true)
  @JsonProperty("preventActivation")
  @Default
  private boolean preventActivation = false;
  /**
   * Constrains choice when true. Default is false.
   */
  @JacksonXmlProperty(localName = "constrainChoice", isAttribute = true)
  @JsonProperty("constrainChoice")
  @Default
  private boolean constrainChoice = false;
}
