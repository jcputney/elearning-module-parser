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

package dev.jcputney.elearning.parser.input.lom.types;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.util.DurationIso8601Deserializer;
import java.io.Serializable;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the duration metadata of a resource, including the duration value in ISO 8601 format
 * and an optional description.
 * <p>
 * The following schema snippet illustrates the expected XML structure for duration:
 * <pre>{@code
 * <xs:complexType name="duration">
 *   <xs:complexContent>
 *     <xs:extension base="Duration">
 *       <xs:attributeGroup ref="ag:duration"/>
 *     </xs:extension>
 *   </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 * <p>
 * A duration value typically conforms to the ISO 8601 format (e.g., "PT10M" for 10 minutes). The
 * description element, if present, allows for a natural language explanation of the duration.
 * <p>
 * Example XML:
 * <pre>{@code
 * <duration>
 *   <duration>PT10M</duration>
 *   <description>
 *     <string language="en-us">This video is 10 minutes long.</string>
 *   </description>
 * </duration>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LomDuration implements Serializable {

  /**
   * The duration of the resource in ISO 8601 format (e.g., "PT10M").
   */
  @JacksonXmlProperty(localName = "duration")
  @JsonAlias("datetime")
  @JsonDeserialize(using = DurationIso8601Deserializer.class)
  private Duration duration;
  /**
   * A natural language description of the duration, represented as a {@link LangString}.
   * <p>
   * Example:
   * <pre>{@code
   * <description>
   *   <string language="en-us">This video is 10 minutes long.</string>
   * </description>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private SingleLangString description;
}
