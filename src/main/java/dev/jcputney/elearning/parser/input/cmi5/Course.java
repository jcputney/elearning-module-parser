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

package dev.jcputney.elearning.parser.input.cmi5;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the root course element in a CMI5 course structure, including metadata such as title,
 * description, and optional custom extensions.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="course">
 *   <xs:complexType>
 *     <xs:sequence>
 *       <xs:element name="title" type="textType"/>
 *       <xs:element name="description" type="textType"/>
 *       <xs:group ref="anyElement"/>
 *     </xs:sequence>
 *     <xs:attributeGroup ref="anyAttribute"/>
 *     <xs:attribute name="id" type="xs:anyURI" use="required"/>
 *   </xs:complexType>
 * </xs:element>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Course {

  /**
   * The title of the course, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;
  /**
   * The description of the course, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;
  /**
   * A list of additional custom elements (extensions) included in the course definition.
   *
   * <pre>{@code
   * <xs:group ref="anyElement"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "anyElement")
  private List<Object> customExtensions;
  /**
   * The unique identifier for the course, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("id")
  private String id;

  /**
   * Default constructor for the Course class.
   */
  public Course() {
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

    Course course = (Course) o;

    return new EqualsBuilder()
        .append(title, course.title)
        .append(description, course.description)
        .append(customExtensions, course.customExtensions)
        .append(id, course.id)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(title)
        .append(description)
        .append(customExtensions)
        .append(id)
        .toHashCode();
  }
}
