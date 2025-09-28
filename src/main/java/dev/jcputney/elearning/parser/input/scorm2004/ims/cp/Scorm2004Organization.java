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
public class Scorm2004Organization implements Serializable {

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

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getStructure() {
    return this.structure;
  }

  public void setStructure(String structure) {
    this.structure = structure;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Scorm2004Item> getItems() {
    return this.items;
  }

  public void setItems(List<Scorm2004Item> items) {
    this.items = items;
  }

  public boolean isObjectivesGlobalToSystem() {
    return this.objectivesGlobalToSystem;
  }

  public void setObjectivesGlobalToSystem(boolean objectivesGlobalToSystem) {
    this.objectivesGlobalToSystem = objectivesGlobalToSystem;
  }

  public boolean isSharedDataGlobalToSystem() {
    return this.sharedDataGlobalToSystem;
  }

  public void setSharedDataGlobalToSystem(boolean sharedDataGlobalToSystem) {
    this.sharedDataGlobalToSystem = sharedDataGlobalToSystem;
  }

  public Scorm2004SubMetadata getMetadata() {
    return this.metadata;
  }

  public void setMetadata(Scorm2004SubMetadata metadata) {
    this.metadata = metadata;
  }

  public Sequencing getSequencing() {
    return this.sequencing;
  }

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
