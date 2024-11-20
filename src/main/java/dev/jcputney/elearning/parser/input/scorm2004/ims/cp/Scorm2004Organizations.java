package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import java.util.List;
import lombok.Data;

/**
 * Represents the hierarchical structure of organizations in the content package. Organizations
 * define the arrangement of items within the package.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004Organizations {

  /**
   * The default organization identifier within the content package. Defines which organization to
   * use if multiple organizations are present.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "default", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private String defaultOrganization;

  /**
   * A list of organizations within the content package, each representing a structured hierarchy of
   * learning items.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "organization", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Organization> organizationList;

  /**
   * Retrieves an organization by its unique identifier.
   *
   * @param id The unique identifier for the organization.
   * @return The organization with the specified identifier, or null if not found.
   */
  public Scorm2004Organization getOrganizationById(String id) {
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
  public Scorm2004Organization getDefaultOrganization() {
    return getOrganizationById(defaultOrganization);
  }
}