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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.LaunchMethod;
import dev.jcputney.elearning.parser.input.cmi5.types.MoveOn;
import dev.jcputney.elearning.parser.input.cmi5.types.ReferencesObjectives;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import dev.jcputney.elearning.parser.input.common.PercentType;
import dev.jcputney.elearning.parser.input.common.PercentTypeDeserializer;
import dev.jcputney.elearning.parser.input.common.PercentTypeSerializer;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an Assignable Unit (AU) in a CMI5 course structure. An AU is a distinct learning
 * activity or resource that can be assigned to learners.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="auType">
 *   <xs:sequence>
 *     <xs:element name="title" type="textType"/>
 *     <xs:element name="description" type="textType"/>
 *     <xs:element name="objectives" type="referencesObjectivesType" minOccurs="0"/>
 *     <xs:element name="url">
 *       <xs:simpleType>
 *         <xs:restriction base="xs:anyURI">
 *           <xs:minLength value="1"/>
 *         </xs:restriction>
 *       </xs:simpleType>
 *     </xs:element>
 *     <xs:element name="launchParameters" minOccurs="0"/>
 *     <xs:element name="entitlementKey" minOccurs="0"/>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 *   <xs:attribute name="id" type="xs:anyURI" use="required"/>
 *   <xs:attribute name="moveOn" default="NotApplicable">
 *     <xs:simpleType>
 *       <xs:restriction base="xs:string">
 *         <xs:enumeration value="NotApplicable"/>
 *         <xs:enumeration value="Passed"/>
 *         <xs:enumeration value="Completed"/>
 *         <xs:enumeration value="CompletedAndPassed"/>
 *         <xs:enumeration value="CompletedOrPassed"/>
 *       </xs:restriction>
 *     </xs:simpleType>
 *   </xs:attribute>
 *   <xs:attribute name="masteryScore" use="optional">
 *     <xs:simpleType>
 *       <xs:restriction base="xs:decimal">
 *         <xs:minInclusive value="0"/>
 *         <xs:maxInclusive value="1"/>
 *       </xs:restriction>
 *     </xs:simpleType>
 *   </xs:attribute>
 *   <xs:attribute name="launchMethod" default="AnyWindow">
 *     <xs:simpleType>
 *       <xs:restriction base="xs:string">
 *         <xs:enumeration value="AnyWindow"/>
 *         <xs:enumeration value="OwnWindow"/>
 *       </xs:restriction>
 *     </xs:simpleType>
 *   </xs:attribute>
 *   <xs:attribute name="activityType" use="optional" type="xs:string"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class AU implements Serializable {

  /**
   * The title of the Assignable Unit (AU).
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;

  /**
   * The description of the Assignable Unit (AU).
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * Objectives referenced by the Assignable Unit.
   *
   * <pre>{@code
   * <xs:element name="objectives" type="referencesObjectivesType" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "objectives")
  private ReferencesObjectives objectives;

  /**
   * The URL for launching the Assignable Unit.
   *
   * <pre>{@code
   * <xs:element name="url">
   *   <xs:simpleType>
   *     <xs:restriction base="xs:anyURI">
   *       <xs:minLength value="1"/>
   *     </xs:restriction>
   *   </xs:simpleType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "url")
  private String url;

  /**
   * Optional launch parameters for the AU.
   *
   * <pre>{@code
   * <xs:element name="launchParameters" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "launchParameters")
  private String launchParameters;

  /**
   * Optional entitlement key for the AU.
   *
   * <pre>{@code
   * <xs:element name="entitlementKey" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "entitlementKey")
  private String entitlementKey;

  /**
   * The ID of the Assignable Unit, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("id")
  private String id;

  /**
   * Specifies the condition required to move on from the AU. Default is "NotApplicable".
   *
   * <pre>{@code
   * <xs:attribute name="moveOn" default="NotApplicable">
   *   <xs:simpleType>
   *     <xs:restriction base="xs:string">
   *       <xs:enumeration value="NotApplicable"/>
   *       <xs:enumeration value="Passed"/>
   *       <xs:enumeration value="Completed"/>
   *       <xs:enumeration value="CompletedAndPassed"/>
   *       <xs:enumeration value="CompletedOrPassed"/>
   *     </xs:restriction>
   *   </xs:simpleType>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("moveOn")
  private MoveOn moveOn;

  /**
   * The mastery score required for the AU, represented as a decimal between 0 and 1.
   *
   * <pre>{@code
   * <xs:attribute name="masteryScore" use="optional">
   *   <xs:simpleType>
   *     <xs:restriction base="xs:decimal">
   *       <xs:minInclusive value="0"/>
   *       <xs:maxInclusive value="1"/>
   *     </xs:restriction>
   *   </xs:simpleType>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonSerialize(using = PercentTypeSerializer.class)
  @JsonDeserialize(using = PercentTypeDeserializer.class)
  @JsonProperty("masteryScore")
  private PercentType masteryScore;

  /**
   * Specifies the launch method for the AU, defaulting to "AnyWindow".
   *
   * <pre>{@code
   * <xs:attribute name="launchMethod" default="AnyWindow">
   *   <xs:simpleType>
   *     <xs:restriction base="xs:string">
   *       <xs:enumeration value="AnyWindow"/>
   *       <xs:enumeration value="OwnWindow"/>
   *     </xs:restriction>
   *   </xs:simpleType>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("launchMethod")
  private LaunchMethod launchMethod;

  /**
   * Specifies the type of activity represented by the AU.
   *
   * <pre>{@code
   * <xs:attribute name="activityType" use="optional" type="xs:string"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("activityType")
  private String activityType;

  /**
   * Constructs a new instance of AU with the specified parameters.
   *
   * @param title the title of the AU, represented as a {@link TextType}, which supports
   * multi-language localized text
   * @param description the description of the AU, represented as a {@link TextType}, which supports
   * multi-language localized text
   * @param objectives the objectives associated with the AU, represented as a
   * {@link ReferencesObjectives} object
   * @param url the URL associated with the AU, represented as a {@link String}
   * @param launchParameters the launch parameters for the AU, represented as a {@link String}
   * @param entitlementKey the entitlement key for the AU, represented as a {@link String}
   * @param id the unique identifier for the AU, represented as a {@link String}
   * @param moveOn the {@link MoveOn} enum value representing the conditions that must be met to
   * move on to the next activity
   * @param masteryScore the mastery score for the AU, represented as a {@link PercentType}, which
   * is a percentage value constrained between 0 and 1, inclusive
   * @param launchMethod the {@link LaunchMethod} enum value specifying the method used to launch
   * the activity
   * @param activityType the activity type associated with the AU, represented as a {@link String}
   */
  public AU(TextType title, TextType description, ReferencesObjectives objectives, String url,
      String launchParameters, String entitlementKey, String id, MoveOn moveOn,
      PercentType masteryScore, LaunchMethod launchMethod, String activityType) {
    this.title = title;
    this.description = description;
    this.objectives = objectives;
    this.url = url;
    this.launchParameters = launchParameters;
    this.entitlementKey = entitlementKey;
    this.id = id;
    this.moveOn = moveOn;
    this.masteryScore = masteryScore;
    this.launchMethod = launchMethod;
    this.activityType = activityType;
  }

  /**
   * Default constructor for the AU class.
   *
   * Initializes a new instance of the AU class without setting any field values. This constructor
   * performs no operations and is intended for scenarios where an instance of AU is required but
   * specific initialization is not necessary.
   */
  public AU() {
    // no-op
  }

  /**
   * Retrieves the title of the current instance.
   *
   * @return the title as a {@link TextType} representing localized text.
   */
  public TextType getTitle() {
    return this.title;
  }

  /**
   * Updates the title for the current instance.
   *
   * @param title the new title, represented as a {@link TextType}, which supports multi-language
   * localized text
   */
  public void setTitle(TextType title) {
    this.title = title;
  }

  /**
   * Retrieves the description of the current instance.
   *
   * @return the description as a {@link TextType}, which represents localized text.
   */
  public TextType getDescription() {
    return this.description;
  }

  /**
   * Updates the description for the current instance.
   *
   * @param description the new description, represented as a {@link TextType}, which supports
   * multi-language localized text
   */
  public void setDescription(TextType description) {
    this.description = description;
  }

  /**
   * Retrieves the objectives of the current instance.
   *
   * @return a {@link ReferencesObjectives} object representing the objectives associated with the
   * current instance.
   */
  public ReferencesObjectives getObjectives() {
    return this.objectives;
  }

  /**
   * Updates the objectives for the current instance.
   *
   * @param objectives the new {@link ReferencesObjectives} object representing the objectives
   * associated with the current instance.
   */
  public void setObjectives(ReferencesObjectives objectives) {
    this.objectives = objectives;
  }

  /**
   * Retrieves the URL associated with the current instance.
   *
   * @return the URL as a {@link String}.
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * Updates the URL for the current instance.
   *
   * @param url the new URL, represented as a String.
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Retrieves the launch parameters associated with the current instance.
   *
   * @return a {@link String} representing the launch parameters.
   */
  public String getLaunchParameters() {
    return this.launchParameters;
  }

  /**
   * Updates the launch parameters for the current instance.
   *
   * @param launchParameters the new launch parameters, represented as a {@link String}.
   */
  public void setLaunchParameters(String launchParameters) {
    this.launchParameters = launchParameters;
  }

  /**
   * Retrieves the entitlement key associated with the current instance.
   *
   * @return a {@link String} representing the entitlement key.
   */
  public String getEntitlementKey() {
    return this.entitlementKey;
  }

  /**
   * Sets the entitlement key for the current instance.
   *
   * @param entitlementKey the new entitlement key, represented as a {@link String}.
   */
  public void setEntitlementKey(String entitlementKey) {
    this.entitlementKey = entitlementKey;
  }

  /**
   * Retrieves the unique identifier associated with the current instance.
   *
   * @return a {@link String} representing the unique identifier.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Updates the unique identifier for the current instance.
   *
   * @param id the new unique identifier, represented as a {@link String}.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Retrieves the {@link MoveOn} value associated with the current instance.
   *
   * @return the {@link MoveOn} enum value representing the conditions for moving on to the next
   * activity.
   */
  public MoveOn getMoveOn() {
    return this.moveOn;
  }

  /**
   * Updates the {@link MoveOn} value for the current instance.
   *
   * @param moveOn the new {@link MoveOn} enum value representing the conditions that must be met to
   * move on to the next activity.
   */
  public void setMoveOn(MoveOn moveOn) {
    this.moveOn = moveOn;
  }

  /**
   * Retrieves the mastery score associated with the current instance.
   *
   * @return a {@link PercentType} representing the mastery score, which is defined as a percentage
   * value constrained between 0 and 1, inclusive.
   */
  public PercentType getMasteryScore() {
    return this.masteryScore;
  }

  /**
   * Updates the mastery score for the current instance.
   *
   * @param masteryScore the new mastery score, represented as a {@link PercentType}, which is a
   * percentage value constrained between 0 and 1, inclusive.
   */
  public void setMasteryScore(PercentType masteryScore) {
    this.masteryScore = masteryScore;
  }

  /**
   * Retrieves the launch method associated with the current instance.
   *
   * @return the {@link LaunchMethod}, an enum representing the method used to launch the activity.
   */
  public LaunchMethod getLaunchMethod() {
    return this.launchMethod;
  }

  /**
   * Sets the {@link LaunchMethod} for the current instance.
   *
   * @param launchMethod the {@link LaunchMethod} enum value specifying the method used to launch
   * the activity. It determines whether the activity can launch in any window or must be launched
   * in its own window.
   */
  public void setLaunchMethod(LaunchMethod launchMethod) {
    this.launchMethod = launchMethod;
  }

  /**
   * Retrieves the activity type associated with the current instance.
   *
   * @return a {@link String} representing the activity type.
   */
  public String getActivityType() {
    return this.activityType;
  }

  /**
   * Updates the activity type for the current instance.
   *
   * @param activityType the new activity type, represented as a {@link String}.
   */
  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AU au)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getTitle(), au.getTitle())
        .append(getDescription(), au.getDescription())
        .append(getObjectives(), au.getObjectives())
        .append(getUrl(), au.getUrl())
        .append(getLaunchParameters(), au.getLaunchParameters())
        .append(getEntitlementKey(), au.getEntitlementKey())
        .append(getId(), au.getId())
        .append(getMoveOn(), au.getMoveOn())
        .append(getMasteryScore(), au.getMasteryScore())
        .append(getLaunchMethod(), au.getLaunchMethod())
        .append(getActivityType(), au.getActivityType())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTitle())
        .append(getDescription())
        .append(getObjectives())
        .append(getUrl())
        .append(getLaunchParameters())
        .append(getEntitlementKey())
        .append(getId())
        .append(getMoveOn())
        .append(getMasteryScore())
        .append(getLaunchMethod())
        .append(getActivityType())
        .toHashCode();
  }
}
