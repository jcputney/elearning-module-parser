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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.ReferencesObjectives;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a block within a CMI5 course structure. Blocks can contain nested blocks or AUs
 * (Assignable Units) and have associated objectives.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="blockType">
 *   <xs:sequence>
 *     <xs:element name="title" type="textType"/>
 *     <xs:element name="description" type="textType"/>
 *     <xs:element name="objectives" type="referencesObjectivesType" minOccurs="0"/>
 *     <xs:choice minOccurs="1" maxOccurs="unbounded">
 *       <xs:element name="au" type="auType"/>
 *       <xs:element name="block" type="blockType"/>
 *     </xs:choice>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 *   <xs:attribute name="id" type="xs:anyURI" use="required"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Block implements Serializable {

  /**
   * The title of the block, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;

  /**
   * The description of the block, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * The objectives for the block, if specified. This references existing objectives defined in the
   * course.
   *
   * <pre>{@code
   * <xs:element name="objectives" type="referencesObjectivesType" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "objectives")
  private ReferencesObjectives objectives;

  /**
   * Nested assignable units (AUs) within the block.
   *
   * <pre>{@code
   * <xs:element name="au" type="auType"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "au")
  private List<AU> assignableUnits;

  /**
   * Nested blocks within this block, allowing for hierarchical structure.
   *
   * <pre>{@code
   * <xs:element name="block" type="blockType"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "block")
  private List<Block> nestedBlocks;

  /**
   * The unique identifier for the block, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("id")
  private String id;

  public Block(TextType title, TextType description, ReferencesObjectives objectives,
      List<AU> assignableUnits,
      List<Block> nestedBlocks, String id) {
    this.title = title;
    this.description = description;
    this.objectives = objectives;
    this.assignableUnits = assignableUnits;
    this.nestedBlocks = nestedBlocks;
    this.id = id;
  }

  public Block() {
    // no-op
  }

  /**
   * Retrieves the title of the block.
   *
   * @return the title of the block as a {@link TextType} object
   */
  public TextType getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the block.
   *
   * @param title the title to set, represented as a {@link TextType} object
   */
  public void setTitle(TextType title) {
    this.title = title;
  }

  /**
   * Retrieves the description of the block.
   *
   * @return the description of the block as a {@link TextType} object
   */
  public TextType getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the block.
   *
   * @param description the description to set, represented as a {@link TextType} object
   */
  public void setDescription(TextType description) {
    this.description = description;
  }

  /**
   * Retrieves the objectives associated with the block.
   *
   * @return the objectives of the block as a {@link ReferencesObjectives} object
   */
  public ReferencesObjectives getObjectives() {
    return this.objectives;
  }

  /**
   * Sets the objectives associated with the block.
   *
   * @param objectives the objectives to set, represented as a {@link ReferencesObjectives} object
   */
  public void setObjectives(ReferencesObjectives objectives) {
    this.objectives = objectives;
  }

  /**
   * Retrieves a list of assignable units associated with the block.
   *
   * @return a list of assignable units as a {@link List} of {@code AU} objects
   */
  public List<AU> getAssignableUnits() {
    return this.assignableUnits;
  }

  /**
   * Sets the list of assignable units associated with the block.
   *
   * @param assignableUnits the list of assignable units to set, represented as a {@link List} of
   * {@code AU} objects
   */
  public void setAssignableUnits(List<AU> assignableUnits) {
    this.assignableUnits = assignableUnits;
  }

  /**
   * Retrieves the nested blocks associated with this block.
   *
   * @return a list of nested blocks as a {@link List} of {@code Block} objects
   */
  public List<Block> getNestedBlocks() {
    return this.nestedBlocks;
  }

  /**
   * Sets the list of nested blocks associated with this block.
   *
   * @param nestedBlocks the list of nested blocks to set, represented as a {@link List} of
   * {@code Block} objects
   */
  public void setNestedBlocks(List<Block> nestedBlocks) {
    this.nestedBlocks = nestedBlocks;
  }

  /**
   * Retrieves the unique identifier of the block.
   *
   * @return the unique identifier of the block as a {@code String}
   */
  public String getId() {
    return this.id;
  }

  /**
   * Sets the unique identifier of the block.
   *
   * @param id the unique identifier to set, represented as a String
   */
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Block block)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getTitle(), block.getTitle())
        .append(getDescription(), block.getDescription())
        .append(getObjectives(), block.getObjectives())
        .append(getAssignableUnits(), block.getAssignableUnits())
        .append(getNestedBlocks(), block.getNestedBlocks())
        .append(getId(), block.getId())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTitle())
        .append(getDescription())
        .append(getObjectives())
        .append(getAssignableUnits())
        .append(getNestedBlocks())
        .append(getId())
        .toHashCode();
  }
}
