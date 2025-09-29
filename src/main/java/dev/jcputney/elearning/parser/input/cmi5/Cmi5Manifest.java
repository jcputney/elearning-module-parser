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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.cmi5.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
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
   * The 'objectives' element, representing the objectives of the course.
   *
   * <pre>{@code
   * <xs:element name="objectives" type="objectivesType" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "objectives")
  private ObjectivesList objectives;

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

  public Cmi5Manifest(Course course, ObjectivesList objectives, List<Block> blocks,
      List<AU> assignableUnits) {
    this.course = course;
    this.objectives = objectives;
    this.blocks = blocks;
    this.assignableUnits = assignableUnits;
  }

  public Cmi5Manifest() {
    // no-op
  }

  /**
   * Returns the title of the course. If the title is not present, it returns null.
   *
   * @return the title of the course
   */
  @Override
  @JsonIgnore
  public String getTitle() {
    return Optional
        .ofNullable(course)
        .map(Course::getTitle)
        .map(TextType::getStrings)
        .filter(strings -> !strings.isEmpty())
        .map(strings -> strings.get(0))
        .map(LangString::getValue)
        .orElse(null);
  }

  /**
   * Returns the description of the course. If the description is not present, it returns null.
   *
   * @return the description of the course
   */
  @Override
  @JsonIgnore
  public String getDescription() {
    return Optional
        .ofNullable(course)
        .map(Course::getDescription)
        .map(TextType::getStrings)
        .filter(strings -> !strings.isEmpty())
        .map(strings -> strings.get(0))
        .map(LangString::getValue)
        .orElse(null);
  }

  /**
   * Returns the launch URL for the course. First checks for assignable units at the root level, and
   * if none are found, looks for assignable units inside the first block.
   *
   * @return the launch URL for the course, or null if no assignable units are found
   */
  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    // First, try to get the URL from root-level AUs
    String rootLevelUrl = Optional
        .ofNullable(assignableUnits)
        .filter(units -> !units.isEmpty())
        .map(units -> units.get(0))
        .map(AU::getUrl)
        .orElse(null);

    // If no root-level AUs, try to get the URL from the first block's AUs
    if (rootLevelUrl == null) {
      return Optional
          .ofNullable(blocks)
          .filter(blockList -> !blockList.isEmpty())
          .map(blockList -> blockList.get(0))
          .map(Block::getAssignableUnits)
          .filter(units -> !units.isEmpty())
          .map(units -> units.get(0))
          .map(AU::getUrl)
          .orElse(null);
    }

    return rootLevelUrl;
  }

  /**
   * Returns the identifier for the course. If the course does not have an identifier, it returns
   * null.
   *
   * @return the identifier for the course
   */
  @Override
  @JsonIgnore
  public String getIdentifier() {
    return Optional
        .ofNullable(course)
        .map(Course::getId)
        .orElse(null);
  }

  /**
   * Returns the version of the course. If the version is not available, it returns null.
   *
   * @return the version of the course, or null if not available
   */
  @Override
  @JsonIgnore
  public String getVersion() {
    return null;
  }

  /**
   * Returns the duration of the course. The default duration is represented as zero if not
   * explicitly defined.
   *
   * @return the duration of the course, represented as a {@link Duration} object
   */
  @Override
  @JsonIgnore
  public Duration getDuration() {
    return Duration.ZERO;
  }

  /**
   * Retrieves the course associated with this manifest.
   *
   * @return the {@code Course} object associated with this manifest, or null if no course is set
   */
  public Course getCourse() {
    return this.course;
  }

  /**
   * Sets the course associated with the manifest.
   *
   * @param course the {@code Course} object to associate with this manifest
   */
  public void setCourse(Course course) {
    this.course = course;
  }

  /**
   * Retrieves the list of objectives associated with this manifest.
   *
   * @return an {@code ObjectivesList} containing the course objectives, or null if no objectives
   * are defined
   */
  public ObjectivesList getObjectives() {
    return this.objectives;
  }

  /**
   * Sets the list of objectives for this manifest.
   *
   * @param objectives the {@code ObjectivesList} containing the objectives to associate with this
   * manifest
   */
  public void setObjectives(ObjectivesList objectives) {
    this.objectives = objectives;
  }

  /**
   * Retrieves the list of blocks associated with this manifest.
   *
   * @return a {@code List} of {@code Block} objects representing the blocks in the manifest, or an
   * empty list if no blocks are defined
   */
  public List<Block> getBlocks() {
    return this.blocks;
  }

  /**
   * Sets the list of blocks for the Cmi5 manifest.
   *
   * @param blocks the {@code List} of {@code Block} objects to associate with this manifest
   */
  public void setBlocks(List<Block> blocks) {
    this.blocks = blocks;
  }

  /**
   * Retrieves the list of assignable units associated with this manifest.
   *
   * @return a {@code List} of {@code AU} objects representing the assignable units, or an empty
   * list if no assignable units are defined
   */
  public List<AU> getAssignableUnits() {
    return this.assignableUnits;
  }

  /**
   * Sets the list of assignable units for the Cmi5 manifest.
   *
   * @param assignableUnits the list of {@code AU} objects to be associated with this manifest
   */
  public void setAssignableUnits(List<AU> assignableUnits) {
    this.assignableUnits = assignableUnits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Cmi5Manifest that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCourse(), that.getCourse())
        .append(getObjectives(), that.getObjectives())
        .append(getBlocks(), that.getBlocks())
        .append(getAssignableUnits(), that.getAssignableUnits())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCourse())
        .append(getObjectives())
        .append(getBlocks())
        .append(getAssignableUnits())
        .toHashCode();
  }
}
