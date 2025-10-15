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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single organization within the content package. Each organization may contain
 * multiple items structured hierarchically.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm2004Organization implements Serializable {

  /**
   * The unique identifier for this organization, used to distinguish it from other organizations
   * within the same manifest.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("identifier")
  private String identifier;

  /**
   * The structure of this organization, defining the hierarchical arrangement of items within the
   * organization. The structure is typically a tree-like arrangement of items, but is not typically
   * used in any meaningful way.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("structure")
  private String structure = "hierarchical";

  /**
   * The title of this organization providing a descriptive label for the learning structure it
   * represents.
   */
  @JacksonXmlProperty(localName = "title", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private String title;

  /**
   * A list of items within this organization, representing a hierarchical structure of learning
   * objects.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "item", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Item> items;

  /**
   * The default for the adlcp:objectivesGlobalToSystem attribute for items in this organization. If
   * true, objectives defined in this organization are considered global to the system. If false,
   * objectives are considered local to the organization.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "objectivesGlobalToSystem", namespace = ADLSeq.NAMESPACE_URI)
  @JsonProperty("objectivesGlobalToSystem")
  private boolean objectivesGlobalToSystem = false;

  /**
   * The default for the adlcp:sharedDataGlobalToSystem attribute for items in this organization. If
   * true, "sharedData" defined in this organization is considered global to the system. If false,
   * "sharedData" is considered local to the organization.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "sharedDataGlobalToSystem", namespace = ADLCP.NAMESPACE_URI)
  @JsonProperty("sharedDataGlobalToSystem")
  private boolean sharedDataGlobalToSystem = false;

  /**
   * Metadata associated with this organization, providing details such as creation date, and other
   * descriptive information relevant to the organization.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * The sequencing element for this organization, defining control modes, delivery controls, and
   * sequencing rules.
   */
  @JacksonXmlProperty(localName = "sequencing", namespace = IMSSS.NAMESPACE_URI)
  private Sequencing sequencing;

  public Scorm2004Organization() {
    // no-op
  }

  /**
   * Retrieves the identifier for the organization.
   *
   * @return the identifier as a string
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier for the SCORM 2004 organization.
   *
   * @param identifier the identifier to be set for the organization
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the structure of the SCORM 2004 organization.
   *
   * @return the structure as a string
   */
  public String getStructure() {
    return this.structure;
  }

  /**
   * Sets the structure for the SCORM 2004 organization.
   *
   * @param structure the structure to be set for the organization
   */
  public void setStructure(String structure) {
    this.structure = structure;
  }

  /**
   * Retrieves the title of the SCORM 2004 organization.
   *
   * @return the title as a string
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title for the SCORM 2004 organization.
   *
   * @param title the title to be set for the organization
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the list of SCORM 2004 items associated with the organization.
   *
   * @return a list of Scorm2004Item objects representing the items in the organization
   */
  public List<Scorm2004Item> getItems() {
    return this.items;
  }

  /**
   * Sets the list of SCORM 2004 items associated with the organization.
   *
   * @param items the list of Scorm2004Item objects to be set for the organization
   */
  public void setItems(List<Scorm2004Item> items) {
    this.items = items;
  }

  /**
   * Determines whether the objectives in the SCORM 2004 organization are global to the system.
   *
   * @return true if the objectives are global to the system, false otherwise
   */
  public boolean isObjectivesGlobalToSystem() {
    return this.objectivesGlobalToSystem;
  }

  /**
   * Sets whether the objectives in the SCORM 2004 organization are global to the system.
   *
   * @param objectivesGlobalToSystem a boolean value where {@code true} indicates that the
   * objectives are global to the system, and {@code false} indicates that they are not.
   */
  public void setObjectivesGlobalToSystem(boolean objectivesGlobalToSystem) {
    this.objectivesGlobalToSystem = objectivesGlobalToSystem;
  }

  /**
   * Determines whether the shared data within the SCORM 2004 organization is global to the system.
   *
   * @return true if the shared data is global to the system, false otherwise
   */
  public boolean isSharedDataGlobalToSystem() {
    return this.sharedDataGlobalToSystem;
  }

  /**
   * Sets whether the shared data in the SCORM 2004 organization is global to the system.
   *
   * @param sharedDataGlobalToSystem a boolean value where {@code true} indicates that the shared
   * data is global to the system, and {@code false} indicates that it is not.
   */
  public void setSharedDataGlobalToSystem(boolean sharedDataGlobalToSystem) {
    this.sharedDataGlobalToSystem = sharedDataGlobalToSystem;
  }

  /**
   * Retrieves the SCORM 2004 metadata associated with the organization.
   *
   * @return an instance of {@link Scorm2004SubMetadata} representing the metadata
   */
  public Scorm2004SubMetadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata for the SCORM 2004 organization.
   *
   * @param metadata an instance of {@link Scorm2004SubMetadata} representing the metadata to be
   * associated with the organization
   */
  public void setMetadata(Scorm2004SubMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the sequencing associated with the SCORM 2004 organization.
   *
   * @return an instance of {@link Sequencing} representing the sequencing configuration
   */
  public Sequencing getSequencing() {
    return this.sequencing;
  }

  /**
   * Sets the sequencing configuration for the SCORM 2004 organization.
   *
   * @param sequencing an instance of {@link Sequencing} representing the sequencing configuration
   * to be associated with the organization
   */
  public void setSequencing(Sequencing sequencing) {
    this.sequencing = sequencing;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Organization that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isObjectivesGlobalToSystem(), that.isObjectivesGlobalToSystem())
        .append(isSharedDataGlobalToSystem(), that.isSharedDataGlobalToSystem())
        .append(getIdentifier(), that.getIdentifier())
        .append(getStructure(), that.getStructure())
        .append(getTitle(), that.getTitle())
        .append(getItems(), that.getItems())
        .append(getMetadata(), that.getMetadata())
        .append(getSequencing(), that.getSequencing())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getStructure())
        .append(getTitle())
        .append(getItems())
        .append(isObjectivesGlobalToSystem())
        .append(isSharedDataGlobalToSystem())
        .append(getMetadata())
        .append(getSequencing())
        .toHashCode();
  }
}
