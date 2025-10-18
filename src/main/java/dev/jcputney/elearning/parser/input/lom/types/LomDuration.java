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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.common.serialization.DurationIso8601Deserializer;
import java.io.Serializable;
import java.time.Duration;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class LomDuration implements Serializable {

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

  /**
   * Default constructor for the {@code LomDuration} class.
   * <p>
   * Initializes a new instance of the {@code LomDuration} class with no parameters. This
   * constructor performs no operations and primarily exists to allow the creation of an empty
   * {@code LomDuration} instance.
   */
  public LomDuration() {
    // no-op
  }

  /**
   * Constructs a new instance of the {@code LomDuration} class with the specified duration.
   *
   * @param duration the duration of the resource, represented as a {@link Duration} object
   */
  public LomDuration(Duration duration) {
    this.duration = duration;
  }

  /**
   * Retrieves the duration of the resource. The duration is typically specified in ISO 8601 format
   * (e.g., "PT10M" for 10 minutes).
   *
   * @return the duration of the resource as a {@link Duration} object
   */
  public Duration getDuration() {
    return this.duration;
  }

  /**
   * Sets the duration of the resource. The duration is typically specified in ISO 8601 format
   * (e.g., "PT10M" for 10 minutes).
   *
   * @param duration the duration of the resource as a {@link Duration} object
   */
  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  /**
   * Retrieves the description associated with this instance.
   *
   * @return the description as a SingleLangString
   */
  public SingleLangString getDescription() {
    return this.description;
  }

  /**
   * Sets the description associated with this instance. The description is typically a
   * language-specific string represented as a {@link SingleLangString}.
   *
   * @param description the description to set, represented as a {@link SingleLangString} object
   */
  public void setDescription(SingleLangString description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LomDuration that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getDuration(), that.getDuration())
        .append(getDescription(), that.getDescription())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getDuration())
        .append(getDescription())
        .toHashCode();
  }
}
