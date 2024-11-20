package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a dependency within SCORM 1.2 resource.
 *
 * <p>A dependency specifies a resource that is required by the parent resource. This is commonly
 * used to define external files, such as images, scripts, or other assets, that must be loaded for
 * the parent resource to function correctly.</p>
 *
 * <p>Schema Snippet:
 * <pre>{@code
 * <xsd:element name="dependency">
 *   <xsd:complexType>
 *     <xsd:attribute name="identifierref" type="xsd:IDREF" use="required"/>
 *   </xsd:complexType>
 * </xsd:element>
 * }</pre></p>
 *
 * <p>Example Usage in imsmanifest.xml:
 * <pre>{@code
 * <resource identifier="resource_1" type="webcontent" adlcp:scormType="sco">
 *   <file href="index.html"/>
 *   <dependency identifierref="resource_2"/>
 * </resource>
 * }</pre></p>
 */
@Data
public class Scorm12Dependency {

  /**
   * A reference to the identifier of the required resource. This is an IDREF pointing to another
   * resource within the same manifest file.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  @JsonProperty(value = "identifierref", required = true)
  private String identifierRef;
}