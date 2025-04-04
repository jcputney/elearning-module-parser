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

package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import static lombok.AccessLevel.PRIVATE;

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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a single item within an organization, typically mapping to a learning object or
 * resource. Items define the hierarchical structure within an organization.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Item {

  /**
   * The unique identifier for this item within the organization.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("identifier")
  private String identifier;
  /**
   * References a resource within the manifest that this item represents.
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
  @Default
  private boolean isVisible = true;
  /**
   * Querystring parameters that should be passed to an associated SCO or Asset on launch. Useful
   * for varying the behavior of shared resources based on the item from which they are referenced.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("parameters")
  private String parameters;
  /**
   * The title of this item, providing a descriptive label for the learning object.
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

  /**
   * Default constructor for the Scorm2004Item class.
   */
  @SuppressWarnings("unused")
  public Scorm2004Item() {
    // Default constructor
  }
}
