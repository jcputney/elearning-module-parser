package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.util.List;
import lombok.Data;

/**
 * Represents the <organizations> element in a SCORM 1.2 manifest file.
 * <p>
 * The <organizations> element defines a collection of organizations within the content package.
 * Each organization represents a hierarchical structure of learning resources and is identified by
 * a unique identifier.
 * </p>
 *
 * <pre>{@code
 * <xsd:complexType name="organizationsType">
 *    <xsd:sequence>
 *       <xsd:element ref="organization" minOccurs="0" maxOccurs="unbounded"/>
 *       <xsd:group ref="grp.any"/>
 *    </xsd:sequence>
 *    <xsd:attributeGroup ref="attr.default"/>
 *    <xsd:anyAttribute namespace="##other" processContents="strict"/>
 * </xsd:complexType>
 * }</pre>
 */
@Data
public class Scorm12Organizations {

  /**
   * The default organization identifier within the content package. Defines which organization to
   * use if multiple organizations are present.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "default", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String defaultOrganization;

  /**
   * A list of organizations within the content package, each representing a structured hierarchy of
   * learning items.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "organization", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Organization> organizationList;

  /**
   * Retrieves an organization by its unique identifier.
   *
   * @param id The unique identifier for the organization.
   * @return The organization with the specified identifier, or null if not found.
   */
  public Scorm12Organization getOrganizationById(String id) {
    return organizationList.stream()
        .filter(org -> org.getIdentifier().equals(id))
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves the default organization for the content package.
   *
   * @return The default organization, or null if not found.
   */
  public Scorm12Organization getDefaultOrganization() {
    return getOrganizationById(defaultOrganization);
  }

}
