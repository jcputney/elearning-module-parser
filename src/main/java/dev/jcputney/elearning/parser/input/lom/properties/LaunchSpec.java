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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>Represents the <strong>launchSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="launchSpec">
 *   <xs:all>
 *     <xs:element name="sco" type="launchType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="player" type="launchType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="wrapScoWindowWithApi" type="yesNoType" minOccurs="0" maxOccurs="1" />
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
public class LaunchSpec {

  /**
   * The SCO (Sharable Content Object) launch type.
   */
  @JacksonXmlProperty(localName = "sco")
  private LaunchType sco;
  /**
   * The player launch type.
   */
  @JacksonXmlProperty(localName = "player")
  private LaunchType player;
  /**
   * Indicates whether to wrap the SCO window with API.
   */
  @JacksonXmlProperty(localName = "wrapScoWindowWithApi")
  private YesNoType wrapScoWindowWithApi;

  /**
   * Default constructor for the LaunchSpec class.
   */
  public LaunchSpec() {
    // Default constructor
  }
}