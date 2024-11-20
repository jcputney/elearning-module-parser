package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.util.List;
import lombok.Data;

/**
 * Represents the <resources> element in a SCORM 1.2 manifest file.
 * <p>
 * The <resources> element is a container for all <resource> elements in the manifest, defining the
 * resources used by the content package.
 * </p>
 *
 * <pre>{@code
 * <xsd:complexType name="resourcesType">
 *    <xsd:sequence>
 *        <xsd:element ref="resource" minOccurs="0" maxOccurs="unbounded"/>
 *        <xsd:group ref="grp.any"/>
 *    </xsd:sequence>
 *    <xsd:attributeGroup ref="attr.base"/>
 *    <xsd:anyAttribute namespace="##other" processContents="strict"/>
 * </xsd:complexType>
 * }</pre>
 */
@Data
public class Scorm12Resources {

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  private String base;

  /**
   * A list of all <resource> elements defined within the <resources> element.
   * <p>
   * Each <resource> specifies a particular resource (e.g., SCO, asset, or other content) in the
   * package.
   * </p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "resource", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Resource> resources;

}
