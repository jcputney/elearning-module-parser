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

package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a dependency element within a resource, specifying a relationship to another resource
 * that this resource relies upon.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Dependency implements Serializable {

  /**
   * The identifier reference for the dependency. This points to another resource in the content
   * package that this resource depends on.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  @JsonProperty("identifierref")
  private String identifierRef;

  public Scorm2004Dependency(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  public Scorm2004Dependency() {
    // no-op
  }

  /**
   * Retrieves the identifier reference for the dependency. This refers to another resource in the
   * content package that this resource relies upon.
   *
   * @return the identifier reference of the dependency.
   */
  public String getIdentifierRef() {
    return this.identifierRef;
  }

  /**
   * Sets the identifier reference for the dependency. This reference points to another resource in
   * the content package that this resource depends on.
   *
   * @param identifierRef the identifier reference for the dependency
   */
  public void setIdentifierRef(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Dependency that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifierRef(), that.getIdentifierRef())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifierRef())
        .toHashCode();
  }
}
