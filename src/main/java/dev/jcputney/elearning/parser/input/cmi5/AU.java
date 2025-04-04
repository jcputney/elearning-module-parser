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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.LaunchMethod;
import dev.jcputney.elearning.parser.input.cmi5.types.MoveOn;
import dev.jcputney.elearning.parser.input.cmi5.types.ReferencesObjectives;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import dev.jcputney.elearning.parser.input.common.PercentType;
import dev.jcputney.elearning.parser.input.common.PercentTypeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AU {

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
   * Default constructor for the AU class.
   */
  public AU() {
    // Default constructor
  }
}
