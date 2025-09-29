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
import dev.jcputney.elearning.parser.input.scorm2004.ADLNav;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.adl.cp.ADLData;
import dev.jcputney.elearning.parser.input.scorm2004.adl.cp.CompletionThreshold;
import dev.jcputney.elearning.parser.input.scorm2004.adl.navigation.Presentation;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.TimeLimitAction;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single item within an organization, typically mapping to a learning object or
 * resource. Items define the hierarchical structure within an organization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Item implements Serializable {

  /**
   * The unique identifier for this item within the organization.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("identifier")
  private String identifier;

  /**
   * References to a resource within the manifest that this item represents.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  @JsonProperty("identifierref")
  private String identifierRef;

  /**
   * The structure of this item, defining the hierarchical arrangement of items within the
   * organization. The structure is typically a tree-like arrangement of items, but is not typically
   * used in any meaningful way.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "isvisible")
  @JsonProperty("isvisible")
  private boolean isVisible = true;

  /**
   * Querystring parameters that should be passed to an associated SCO or Asset on launch. Useful
   * for varying the behavior of shared resources based on the item from which they are referenced.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("parameters")
  private String parameters;

  /**
   * The title of this item providing a descriptive label for the learning object.
   */
  @JacksonXmlProperty(localName = "title", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private String title;

  /**
   * A list of items within this item, representing a hierarchical structure of learning objects.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "item", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Item> items;

  /**
   * The data element for this item, containing one or more map elements.
   */
  @JacksonXmlProperty(localName = "data", namespace = ADLCP.NAMESPACE_URI)
  private ADLData data;

  /**
   * The completion threshold for this item, specifying the minimum progress required to mark the
   * content as complete.
   */
  @JacksonXmlProperty(localName = "completionThreshold", namespace = ADLCP.NAMESPACE_URI)
  private CompletionThreshold completionThreshold;

  /**
   * The sequencing element for this item, defining rules, objectives, and rollup behaviors.
   */
  @JacksonXmlProperty(localName = "sequencing", namespace = IMSSS.NAMESPACE_URI)
  private Sequencing sequencing;

  /**
   * Metadata associated with this item, providing details such as
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * Navigation presentation rules for this item, defining how the item should be presented to the
   * learner.
   */
  @JacksonXmlProperty(localName = "presentation", namespace = ADLNav.NAMESPACE_URI)
  private Presentation presentation;

  /**
   * Represents the masteryScore element, defined as a decimal with a minimum of 0 and a maximum of
   * 100.
   */
  @JacksonXmlProperty(localName = "masteryScore", namespace = ADLCP.NAMESPACE_URI)
  private Double masteryScore;

  /**
   * Represents the prerequisites attribute, which is a list of identifiers that must be completed
   * before this item can be accessed. The list can be separated by "AND" or "OR" operators.
   */
  @JacksonXmlProperty(localName = "prerequisites", namespace = ADLCP.NAMESPACE_URI)
  private String prerequisites;

  /**
   * Represents the timeLimitAction element with enumerated values: "exit,message", "exit,no
   * message", "continue,message", "continue,no message".
   * <p>Schema definition:</p>
   * <pre>{@code
   * <xs:attribute name="timeLimitAction" type="adlcp:timeLimitActionType" use="optional"/>
   * <xs:simpleType name="timeLimitActionType">
   *     <xs:restriction base="xs:string">
   *         <xs:enumeration value="exit,message"/>
   *         <xs:enumeration value="exit,no message"/>
   *         <xs:enumeration value="continue,message"/>
   *         <xs:enumeration value="continue,no message"/>
   *     </xs:restriction>
   * </xs:simpleType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "timeLimitAction", namespace = ADLCP.NAMESPACE_URI)
  private TimeLimitAction timeLimitAction;

  /**
   * Represents the dataFromLMS element, defined as a string with no restrictions. This value should
   * be passed to the LMS when the item is launched, in the `cmi.launch_data` parameter.
   */
  @JacksonXmlProperty(localName = "dataFromLMS", namespace = ADLCP.NAMESPACE_URI)
  private String dataFromLMS;

  public Scorm2004Item() {
    // no-op
  }

  /**
   * Retrieves the identifier of the current SCORM 2004 item.
   *
   * @return the identifier of this SCORM 2004 item as a {@code String}
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier for this SCORM 2004 item.
   *
   * @param identifier the unique identifier to be set for this item
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the identifier reference for this SCORM 2004 item.
   *
   * @return the identifier reference as a {@code String}
   */
  public String getIdentifierRef() {
    return this.identifierRef;
  }

  /**
   * Sets the identifier reference for this SCORM 2004 item.
   *
   * @param identifierRef the identifier reference to be set for this item
   */
  public void setIdentifierRef(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  /**
   * Determines the visibility status of this SCORM 2004 item.
   *
   * @return true if the item is visible, false otherwise
   */
  public boolean isVisible() {
    return this.isVisible;
  }

  /**
   * Sets the visibility status for this SCORM 2004 item.
   *
   * @param visible a boolean value where {@code true} makes the item visible, and {@code false}
   * makes it invisible
   */
  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  /**
   * Retrieves the parameters associated with this SCORM 2004 item.
   *
   * @return the parameters of this SCORM 2004 item as a {@code String}
   */
  public String getParameters() {
    return this.parameters;
  }

  /**
   * Sets the parameters associated with this SCORM 2004 item.
   *
   * @param parameters the parameters to be set as a {@code String}
   */
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  /**
   * Retrieves the title of the SCORM 2004 item.
   *
   * @return the title of this SCORM 2004 item as a {@code String}
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title for this SCORM 2004 item.
   *
   * @param title the title of the SCORM 2004 item
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the list of child items associated with this SCORM 2004 item.
   *
   * @return a {@code List} of {@code Scorm2004Item} objects representing the child items
   */
  public List<Scorm2004Item> getItems() {
    return this.items;
  }

  /**
   * Sets the list of child items associated with this SCORM 2004 item.
   *
   * @param items a {@code List} of {@code Scorm2004Item} objects representing the child items
   */
  public void setItems(List<Scorm2004Item> items) {
    this.items = items;
  }

  /**
   * Retrieves the ADLData associated with this SCORM 2004 item.
   *
   * @return the ADLData object containing the data map elements and configurations
   */
  public ADLData getData() {
    return this.data;
  }

  /**
   * Sets the ADLData object associated with this SCORM 2004 item.
   *
   * @param data the ADLData object containing data map elements and configurations
   */
  public void setData(ADLData data) {
    this.data = data;
  }

  /**
   * Retrieves the completion threshold for this SCORM 2004 item.
   *
   * @return the {@code CompletionThreshold} object containing the completion threshold settings for
   * this item
   */
  public CompletionThreshold getCompletionThreshold() {
    return this.completionThreshold;
  }

  /**
   * Sets the completion threshold for this SCORM 2004 item.
   *
   * @param completionThreshold the {@code CompletionThreshold} object containing the completion
   * threshold settings to be applied to this item
   */
  public void setCompletionThreshold(CompletionThreshold completionThreshold) {
    this.completionThreshold = completionThreshold;
  }

  /**
   * Retrieves the sequencing settings associated with this SCORM 2004 item.
   *
   * @return the {@code Sequencing} object defining the sequencing rules and behaviors for this item
   */
  public Sequencing getSequencing() {
    return this.sequencing;
  }

  /**
   * Sets the sequencing settings for this SCORM 2004 item.
   *
   * @param sequencing the {@code Sequencing} object containing the sequencing rules and behaviors
   * to be applied to this item
   */
  public void setSequencing(Sequencing sequencing) {
    this.sequencing = sequencing;
  }

  /**
   * Retrieves the metadata associated with this SCORM 2004 item.
   *
   * @return the {@code Scorm2004SubMetadata} object that encapsulates the metadata for this SCORM
   * 2004 item, which may include inline metadata or an external reference to a metadata file.
   */
  public Scorm2004SubMetadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata associated with this SCORM 2004 item.
   *
   * @param metadata the {@code Scorm2004SubMetadata} object containing the metadata for this SCORM
   * 2004 item, which may include inline metadata or an external reference to a metadata file
   */
  public void setMetadata(Scorm2004SubMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the presentation settings associated with this SCORM 2004 item.
   *
   * @return the {@code Presentation} object containing the navigation control settings, or null if
   * no presentation settings are defined
   */
  public Presentation getPresentation() {
    return this.presentation;
  }

  /**
   * Sets the presentation settings associated with this SCORM 2004 item.
   *
   * @param presentation the {@code Presentation} object containing the navigation control settings
   * for this item
   */
  public void setPresentation(Presentation presentation) {
    this.presentation = presentation;
  }

  /**
   * Retrieves the mastery score for this SCORM 2004 item.
   *
   * @return the mastery score as a {@code Double}, or null if no mastery score is defined
   */
  public Double getMasteryScore() {
    return this.masteryScore;
  }

  /**
   * Sets the mastery score for this SCORM 2004 item.
   *
   * @param masteryScore the mastery score to be set as a {@code Double}
   */
  public void setMasteryScore(Double masteryScore) {
    this.masteryScore = masteryScore;
  }

  /**
   * Retrieves the prerequisites for this SCORM 2004 item.
   *
   * @return the prerequisites as a {@code String}
   */
  public String getPrerequisites() {
    return this.prerequisites;
  }

  /**
   * Sets the prerequisites for this SCORM 2004 item.
   *
   * @param prerequisites the prerequisites to be set, represented as a {@code String}
   */
  public void setPrerequisites(String prerequisites) {
    this.prerequisites = prerequisites;
  }

  /**
   * Retrieves the time limit action associated with this SCORM 2004 item. The
   * {@code TimeLimitAction} specifies how the system should behave when a time limit is exceeded,
   * such as exiting or continuing the activity with or without a message.
   *
   * @return the {@code TimeLimitAction} associated with this SCORM 2004 item, or
   * {@code TimeLimitAction.UNKNOWN} if no action is defined.
   */
  public TimeLimitAction getTimeLimitAction() {
    return this.timeLimitAction;
  }

  /**
   * Sets the time limit action for this SCORM 2004 item. The {@code TimeLimitAction} specifies how
   * the system should behave when the time limit is exceeded, such as exiting or continuing the
   * activity with or without a message.
   *
   * @param timeLimitAction the {@code TimeLimitAction} to be set for this SCORM 2004 item, which
   * defines the system's behavior on exceeding the time limit
   */
  public void setTimeLimitAction(TimeLimitAction timeLimitAction) {
    this.timeLimitAction = timeLimitAction;
  }

  /**
   * Retrieves data associated with this SCORM 2004 item from the LMS.
   *
   * @return the data from the LMS as a {@code String}
   */
  public String getDataFromLMS() {
    return this.dataFromLMS;
  }

  /**
   * Sets the data associated with this SCORM 2004 item from the LMS.
   *
   * @param dataFromLMS the data from the LMS to be set as a {@code String}
   */
  public void setDataFromLMS(String dataFromLMS) {
    this.dataFromLMS = dataFromLMS;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Item that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isVisible(), that.isVisible())
        .append(getIdentifier(), that.getIdentifier())
        .append(getIdentifierRef(), that.getIdentifierRef())
        .append(getParameters(), that.getParameters())
        .append(getTitle(), that.getTitle())
        .append(getItems(), that.getItems())
        .append(getData(), that.getData())
        .append(getCompletionThreshold(), that.getCompletionThreshold())
        .append(getSequencing(), that.getSequencing())
        .append(getMetadata(), that.getMetadata())
        .append(getPresentation(), that.getPresentation())
        .append(getMasteryScore(), that.getMasteryScore())
        .append(getPrerequisites(), that.getPrerequisites())
        .append(getTimeLimitAction(), that.getTimeLimitAction())
        .append(getDataFromLMS(), that.getDataFromLMS())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getIdentifierRef())
        .append(isVisible())
        .append(getParameters())
        .append(getTitle())
        .append(getItems())
        .append(getData())
        .append(getCompletionThreshold())
        .append(getSequencing())
        .append(getMetadata())
        .append(getPresentation())
        .append(getMasteryScore())
        .append(getPrerequisites())
        .append(getTimeLimitAction())
        .append(getDataFromLMS())
        .toHashCode();
  }
}
