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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.common.serialization.DurationHHMMSSDeserializer;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12ADLCP;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.adl.Scorm12Prerequisites;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.TimeLimitAction;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an item within a SCORM 1.2 organization.
 *
 * <p>The <code>Item</code> element is used to define the structure of the content and the
 * relationships between the resources and items in the SCORM package.</p>
 *
 * <p>Schema Snippet:</p>
 * <pre>{@code
 * <xsd:element name="item">
 *   <xsd:complexType>
 *     <xsd:sequence>
 *       <xsd:element ref="title" minOccurs="0"/>
 *       <xsd:element ref="metadata" minOccurs="0"/>
 *       <xsd:element ref="item" minOccurs="0" maxOccurs="unbounded"/>
 *     </xsd:sequence>
 *     <xsd:attribute name="identifier" type="xsd:ID" use="required"/>
 *     <xsd:attribute name="identifierref" type="xsd:IDREF" use="optional"/>
 *     <xsd:attribute name="isvisible" type="xsd:boolean" default="true"/>
 *     <xsd:anyAttribute namespace="##other" processContents="lax"/>
 *   </xsd:complexType>
 * </xsd:element>
 * }</pre>
 *
 * <p>Example Usage in imsmanifest.xml:</p>
 * <pre>{@code
 * <item identifier="item_1" identifierref="res_1">
 *   <title>Introduction to Golf</title>
 *   <metadata>
 *     <schema>ADL SCORM</schema>
 *     <schemaversion>1.2</schemaversion>
 *   </metadata>
 *   <item identifier="item_1a" identifierref="res_1a">
 *     <title>Golf Basics</title>
 *   </item>
 * </item>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm12Item implements Serializable {

  /**
   * The unique identifier for this item within the organization.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "identifier", required = true)
  private String identifier;

  /**
   * References to a resource within the manifest that this item represents. This is an optional
   * attribute.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  @JsonProperty(value = "identifierref")
  private String identifierRef;

  /**
   * Specifies whether this item is visible in the navigation tree. Defaults to <code>true</code>.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "isvisible")
  @JsonProperty(value = "isvisible")
  private Boolean isVisible;

  /**
   * Querystring parameters that should be passed to an associated SCO or Asset on launch. Useful
   * for varying the behavior of shared resources based on the item from which they are referenced.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "parameters")
  private String parameters;

  /**
   * The maximum amount of time allowed for this resource to be completed.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "maxtimeallowed", namespace = Scorm12ADLCP.NAMESPACE_URI)
  @JsonDeserialize(using = DurationHHMMSSDeserializer.class)
  @JsonProperty("maxtimeallowed")
  private Duration maxTimeAllowed;

  /**
   * Represents the masteryScore element, defined as a decimal with a minimum of 0 and a maximum of
   * 100.
   */
  @JacksonXmlProperty(localName = "masteryScore", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private Double masteryScore;

  /**
   * Represents the prerequisites attribute, which is a list of identifiers that must be completed
   * before this item can be accessed. The list can be separated by "AND" or "OR" operators.
   */
  @JacksonXmlProperty(localName = "prerequisites", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private Scorm12Prerequisites prerequisites;

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
  @JacksonXmlProperty(localName = "timeLimitAction", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private TimeLimitAction timeLimitAction;

  /**
   * Represents the dataFromLMS element, defined as a string with no restrictions. This value should
   * be passed to the LMS when the item is launched, in the `cmi.launch_data` parameter.
   */
  @JacksonXmlProperty(localName = "dataFromLMS", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private String dataFromLMS;

  /**
   * The title of the item, describing its content or purpose.
   */
  @JacksonXmlProperty(localName = "title", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String title;

  /**
   * Metadata providing additional descriptive information about the item. This element is
   * optional.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm12Manifest.NAMESPACE_URI)
  private Scorm12Metadata metadata;

  /**
   * Child items of this item, allowing for a hierarchical structure. This is an optional element.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "item", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Item> items;

  /**
   * Constructs a new Scorm12Item with the specified properties.
   *
   * @param identifier A unique identifier for the SCORM 1.2 item.
   * @param identifierRef An identifier reference that points to a resource associated with this
   * item.
   * @param isVisible A Boolean indicating whether the item is visible. If true, the item is
   * visible; otherwise, it is hidden.
   * @param parameters A string representing additional parameters associated with the item.
   * @param maxTimeAllowed A Duration object specifying the maximum amount of time allowed for this
   * item.
   * @param masteryScore A Double representing the mastery score for the item, indicating the
   * minimum score required to achieve mastery.
   * @param prerequisites An instance of Scorm12Prerequisites representing the prerequisites
   * required for this item.
   * @param timeLimitAction A TimeLimitAction specifying the action to take when the time limit is
   * reached. Possible values include EXIT_MESSAGE, EXIT_NO_MESSAGE, CONTINUE_MESSAGE,
   * CONTINUE_NO_MESSAGE, and UNKNOWN.
   * @param dataFromLMS A string containing data retrieved from the Learning Management System
   * (LMS).
   * @param title A string specifying the title of the item.
   * @param metadata An instance of Scorm12Metadata containing metadata associated with the item.
   * @param items A list of Scorm12Item objects representing child items associated with this SCORM
   * 1.2 item.
   */
  public Scorm12Item(String identifier, String identifierRef, Boolean isVisible, String parameters,
      Duration maxTimeAllowed, Double masteryScore, Scorm12Prerequisites prerequisites,
      TimeLimitAction timeLimitAction, String dataFromLMS, String title, Scorm12Metadata metadata,
      List<Scorm12Item> items) {
    this.identifier = identifier;
    this.identifierRef = identifierRef;
    this.isVisible = isVisible;
    this.parameters = parameters;
    this.maxTimeAllowed = maxTimeAllowed;
    this.masteryScore = masteryScore;
    this.prerequisites = prerequisites;
    this.timeLimitAction = timeLimitAction;
    this.dataFromLMS = dataFromLMS;
    this.title = title;
    this.metadata = metadata;
    this.items = items;
  }

  /**
   * Default constructor for the Scorm12Item class. Constructs an instance of Scorm12Item with no
   * initialized values. This constructor is primarily used when no initial properties are
   * provided.
   */
  public Scorm12Item() {
    // no-op
  }

  /**
   * Retrieves the identifier of the SCORM 1.2 item.
   *
   * @return A string representing the identifier of the item.
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier for the SCORM 1.2 item.
   *
   * @param identifier A string representing the unique identifier to be assigned to the item.
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the identifier reference of the SCORM 1.2 item.
   *
   * @return A string representing the identifier reference of the item.
   */
  public String getIdentifierRef() {
    return this.identifierRef;
  }

  /**
   * Sets the identifier reference for the SCORM 1.2 item.
   *
   * @param identifierRef A string representing the identifier reference to be assigned to the
   * item.
   */
  public void setIdentifierRef(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  /**
   * Retrieves the visibility status of this item.
   *
   * @return A Boolean indicating whether the item is visible. Returns true if the item is visible,
   * otherwise false.
   */
  public Boolean getIsVisible() {
    return this.isVisible;
  }

  /**
   * Retrieves the parameters associated with this SCORM 1.2 item.
   *
   * @return A string representing the parameters of the item.
   */
  public String getParameters() {
    return this.parameters;
  }

  /**
   * Sets the parameters associated with this SCORM 1.2 item.
   *
   * @param parameters A string representing the parameters to be set for this item.
   */
  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  /**
   * Retrieves the maximum time allowed for the SCORM 1.2 item.
   *
   * @return A Duration object representing the maximum time allowed for this item.
   */
  public Duration getMaxTimeAllowed() {
    return this.maxTimeAllowed;
  }

  /**
   * Sets the maximum time allowed for this SCORM 1.2 item.
   *
   * @param maxTimeAllowed A Duration object representing the maximum amount of time allowed for the
   * completion of this item.
   */
  public void setMaxTimeAllowed(Duration maxTimeAllowed) {
    this.maxTimeAllowed = maxTimeAllowed;
  }

  /**
   * Retrieves the mastery score of the SCORM 1.2 item.
   *
   * @return A Double representing the mastery score of the item.
   */
  public Double getMasteryScore() {
    return this.masteryScore;
  }

  /**
   * Sets the mastery score for the SCORM 1.2 item.
   *
   * @param masteryScore A Double value representing the mastery score to be assigned to the item.
   * This indicates the minimum score required to achieve mastery.
   */
  public void setMasteryScore(Double masteryScore) {
    this.masteryScore = masteryScore;
  }

  /**
   * Retrieves the prerequisites associated with this SCORM 1.2 item.
   *
   * @return A Scorm12Prerequisites object representing the prerequisites for the item.
   */
  public Scorm12Prerequisites getPrerequisites() {
    return this.prerequisites;
  }

  /**
   * Sets the prerequisites for the SCORM 1.2 item.
   *
   * @param prerequisites An instance of {@code Scorm12Prerequisites} representing the prerequisites
   * required for this item.
   */
  public void setPrerequisites(Scorm12Prerequisites prerequisites) {
    this.prerequisites = prerequisites;
  }

  /**
   * Retrieves the time limit action associated with this SCORM 1.2 item.
   *
   * @return A {@code TimeLimitAction} representing the action to be taken when the time limit is
   * reached.
   */
  public TimeLimitAction getTimeLimitAction() {
    return this.timeLimitAction;
  }

  /**
   * Sets the time limit action for this SCORM 1.2 item.
   *
   * @param timeLimitAction An instance of {@code TimeLimitAction} representing the action to be
   * performed when the time limit is reached. Possible values include: {@code EXIT_MESSAGE},
   * {@code EXIT_NO_MESSAGE}, {@code CONTINUE_MESSAGE}, {@code CONTINUE_NO_MESSAGE}, and
   * {@code UNKNOWN}.
   */
  public void setTimeLimitAction(TimeLimitAction timeLimitAction) {
    this.timeLimitAction = timeLimitAction;
  }

  /**
   * Retrieves the data from the Learning Management System (LMS) associated with this SCORM 1.2
   * item.
   *
   * @return A string representing the data obtained from the LMS for this item.
   */
  public String getDataFromLMS() {
    return this.dataFromLMS;
  }

  /**
   * Sets the data from the Learning Management System (LMS) for this SCORM 1.2 item.
   *
   * @param dataFromLMS A string containing the data retrieved from the LMS to be associated with
   * this item.
   */
  public void setDataFromLMS(String dataFromLMS) {
    this.dataFromLMS = dataFromLMS;
  }

  /**
   * Retrieves the title of the SCORM 1.2 item.
   *
   * @return A string representing the title of the item.
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title for the SCORM 1.2 item.
   *
   * @param title A string representing the title to be assigned to the item.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the metadata associated with this SCORM 1.2 item.
   *
   * @return A Scorm12Metadata object representing the metadata of the item.
   */
  public Scorm12Metadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata associated with this SCORM 1.2 item.
   *
   * @param metadata An instance of {@code Scorm12Metadata} representing the metadata to be assigned
   * to the item.
   */
  public void setMetadata(Scorm12Metadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the list of child SCORM 1.2 items associated with the current item.
   *
   * @return A list of {@code Scorm12Item} representing the child items.
   */
  public List<Scorm12Item> getItems() {
    return this.items;
  }

  /**
   * Sets the list of child SCORM 1.2 items associated with this item.
   *
   * @param items A list of {@code Scorm12Item} objects representing the child items to be
   * associated with this item.
   */
  public void setItems(List<Scorm12Item> items) {
    this.items = items;
  }

  /**
   * Sets the visibility state for the SCORM 1.2 item.
   *
   * @param visible A Boolean value indicating the visibility state to be set for this item. If
   * true, the item is marked as visible; if false, it is marked as hidden.
   */
  public void setVisible(Boolean visible) {
    isVisible = visible;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Item that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifier(), that.getIdentifier())
        .append(getIdentifierRef(), that.getIdentifierRef())
        .append(getIsVisible(), that.getIsVisible())
        .append(getParameters(), that.getParameters())
        .append(getMaxTimeAllowed(), that.getMaxTimeAllowed())
        .append(getMasteryScore(), that.getMasteryScore())
        .append(getPrerequisites(), that.getPrerequisites())
        .append(getTimeLimitAction(), that.getTimeLimitAction())
        .append(getDataFromLMS(), that.getDataFromLMS())
        .append(getTitle(), that.getTitle())
        .append(getMetadata(), that.getMetadata())
        .append(getItems(), that.getItems())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getIdentifierRef())
        .append(getIsVisible())
        .append(getParameters())
        .append(getMaxTimeAllowed())
        .append(getMasteryScore())
        .append(getPrerequisites())
        .append(getTimeLimitAction())
        .append(getDataFromLMS())
        .append(getTitle())
        .append(getMetadata())
        .append(getItems())
        .toHashCode();
  }
}
