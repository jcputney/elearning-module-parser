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

package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a dependency within SCORM 1.2 resource.
 *
 * <p>A dependency specifies a resource required by the parent resource.
 * This is commonly used to define external files, such as images, scripts, or other assets, that
 * must be loaded for the parent resource to function correctly.</p>
 *
 * <p>Schema Snippet:</p>
 * <pre>{@code
 * <xsd:element name="dependency">
 *   <xsd:complexType>
 *     <xsd:attribute name="identifierref" type="xsd:IDREF" use="required"/>
 *   </xsd:complexType>
 * </xsd:element>
 * }</pre>
 *
 * <p>Example Usage in imsmanifest.xml:</p>
 * <pre>{@code
 * <resource identifier="resource_1" type="webcontent" adlcp:scormType="sco">
 *   <file href="index.html"/>
 *   <dependency identifierref="resource_2"/>
 * </resource>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12Dependency implements Serializable {

  /**
   * A reference to the identifier of the required resource. This is an IDREF pointing to another
   * resource within the same manifest file.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  @JsonProperty(value = "identifierref", required = true)
  private String identifierRef;


  public Scorm12Dependency() {
  }

  public String getIdentifierRef() {
    return this.identifierRef;
  }

  public void setIdentifierRef(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Dependency that)) {
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