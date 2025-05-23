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

package dev.jcputney.elearning.parser.input.aicc;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the descriptor element in an AICC module.
 *
 * <p>This class is used to parse the descriptor element of an AICC module file.
 * It contains information about the system ID, developer ID, title, and description of the module.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonNaming(SnakeCaseStrategy.class)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Descriptor implements Serializable {

  /**
   * The system ID of the descriptor element.
   */
  @JsonProperty(value = "System_ID", required = true)
  private String systemId;
  /**
   * The developer ID of the descriptor element.
   */
  @JsonProperty(value = "Developer_ID", required = true)
  private String developerId;
  /**
   * The title of the descriptor element.
   */
  @JsonProperty(value = "Title", required = true)
  private String title;
  /**
   * The version of the descriptor element.
   */
  @JsonProperty(value = "Description")
  private String description;

  /**
   * Default constructor for the Descriptor class.
   */
  @SuppressWarnings("unused")
  public Descriptor() {
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

    Descriptor that = (Descriptor) o;

    return new EqualsBuilder()
        .append(systemId, that.systemId)
        .append(developerId, that.developerId)
        .append(title, that.title)
        .append(description, that.description)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(systemId)
        .append(developerId)
        .append(title)
        .append(description)
        .toHashCode();
  }

}
