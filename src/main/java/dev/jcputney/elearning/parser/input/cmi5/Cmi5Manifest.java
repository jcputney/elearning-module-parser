/*
 * Copyright (c) 2024. Jonathan Putney
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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.cmi5.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.util.List;
import java.util.Optional;
import lombok.Data;

/**
 * Represents the root element of a CMI5 course structure manifest. Implements the
 * {@link PackageManifest} interface to provide common manifest functionality.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="courseStructure" type="courseType"/>
 * }</pre>
 *
 * <p>The <code>Cmi5Manifest</code> includes the following components:</p>
 * <ul>
 *   <li>A <code>Course</code> element defining the course metadata.</li>
 *   <li>An optional <code>Objectives</code> element listing the objectives for the course.</li>
 *   <li>A sequence of <code>Block</code> and <code>AU</code> (Assignable Unit) elements defining the course structure.</li>
 * </ul>
 */
@Data
public class Cmi5Manifest implements PackageManifest {

  /**
   * The course element, representing the main course metadata.
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
  @JacksonXmlProperty(localName = "course")
  private Course course;

  /**
   * The "objectives" element, representing the "objectives" of the course.
   *
   * <pre>{@code
   * <xs:element name="objectives" type="objectivesType" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "objectives")
  private Objectives objectives;

  /**
   * A list of blocks within the course structure. Each block can contain nested blocks or
   * assignable units (AUs).
   *
   * <pre>{@code
   * <xs:element name="block" type="blockType"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "block", useWrapping = false)
  @JacksonXmlProperty(localName = "block")
  private List<Block> blocks;

  /**
   * A list of assignable units (AUs) within the course structure.
   *
   * <pre>{@code
   * <xs:element name="au" type="auType"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "au", useWrapping = false)
  @JacksonXmlProperty(localName = "au")
  private List<AU> assignableUnits;

  /**
   * Returns the title of the course. If the title is not present, returns null.
   *
   * @return the title of the course
   */
  @Override
  public String getTitle() {
    return Optional.ofNullable(course)
        .map(Course::getTitle)
        .map(TextType::getStrings)
        .filter(strings -> !strings.isEmpty())
        .map(strings -> strings.get(0))
        .map(LangString::getValue)
        .orElse(null);
  }

  /**
   * Returns the description of the course. If the description is not present, returns null.
   *
   * @return the description of the course
   */
  @Override
  public String getDescription() {
    return Optional.ofNullable(course)
        .map(Course::getDescription)
        .map(TextType::getStrings)
        .filter(strings -> !strings.isEmpty())
        .map(strings -> strings.get(0))
        .map(LangString::getValue)
        .orElse(null);
  }

  /**
   * Returns the launch URL for the course. If the course does not have any assignable units, returns
   * null.
   *
   * @return the launch URL for the course
   */
  @Override
  public String getLaunchUrl() {
    return Optional.ofNullable(assignableUnits)
        .filter(units -> !units.isEmpty())
        .map(units -> units.get(0))
        .map(AU::getUrl)
        .orElse(null);
  }

  /**
   * Returns the identifier for the course. If the course does not have an identifier, returns null.
   *
   * @return the identifier for the course
   */
  @Override
  public String getIdentifier() {
    return Optional.ofNullable(course)
        .map(Course::getId)
        .orElse(null);
  }

  /**
   * Returns null, as the CMI5 manifest does not include a version number.
   * @return null
   */
  @Override
  public String getVersion() {
    return null;
  }
}