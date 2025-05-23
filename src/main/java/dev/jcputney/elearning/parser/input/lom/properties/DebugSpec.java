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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * <p>Represents the <strong>debugSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="debugSpec">
 *   <xs:all>
 *     <xs:element name="controlAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="controlDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="runtimeAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="runtimeDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="sequencingAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="sequencingDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="sequencingSimple" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="lookaheadAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="lookaheadDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="includeTimestamps" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DebugSpec implements Serializable {

  /**
   * The audit level for control.
   */
  @JacksonXmlProperty(localName = "controlAudit")
  private YesNoType controlAudit;
  /**
   * The detailed level for control.
   */
  @JacksonXmlProperty(localName = "controlDetailed")
  private YesNoType controlDetailed;
  /**
   * The audit level for runtime.
   */
  @JacksonXmlProperty(localName = "runtimeAudit")
  private YesNoType runtimeAudit;
  /**
   * The detailed level for runtime.
   */
  @JacksonXmlProperty(localName = "runtimeDetailed")
  private YesNoType runtimeDetailed;
  /**
   * The audit level for sequencing.
   */
  @JacksonXmlProperty(localName = "sequencingAudit")
  private YesNoType sequencingAudit;
  /**
   * The detailed level for sequencing.
   */
  @JacksonXmlProperty(localName = "sequencingDetailed")
  private YesNoType sequencingDetailed;
  /**
   * The simple level for sequencing.
   */
  @JacksonXmlProperty(localName = "sequencingSimple")
  private YesNoType sequencingSimple;
  /**
   * The audit level for lookahead.
   */
  @JacksonXmlProperty(localName = "lookaheadAudit")
  private YesNoType lookaheadAudit;
  /**
   * The detailed level for lookahead.
   */
  @JacksonXmlProperty(localName = "lookaheadDetailed")
  private YesNoType lookaheadDetailed;
  /**
   * Indicates whether to include timestamps.
   */
  @JacksonXmlProperty(localName = "includeTimestamps")
  private YesNoType includeTimestamps;
}
