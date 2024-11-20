package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12ADLCP;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.adl.Scorm12Prerequisites;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.TimeLimitAction;
import java.util.List;
import lombok.Data;

/**
 * Represents an item within a SCORM 1.2 organization.
 *
 * <p>The <code>Item</code> element is used to define the structure of the content and the
 * relationships between the resources and items in the SCORM package.</p>
 *
 * <p>Schema Snippet:
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
 * }</pre></p>
 *
 * <p>Example Usage in imsmanifest.xml:
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
 * }</pre></p>
 */
@Data
public class Scorm12Item {

  /**
   * The unique identifier for this item within the organization.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "identifier", required = true)
  private String identifier;

  /**
   * References a resource within the manifest that this item represents. This is an optional
   * attribute.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  private String identifierRef;

  /**
   * Specifies whether this item is visible in the navigation tree. Defaults to <code>true</code>.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "isvisible")
  private Boolean isVisible;

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
}
