package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import lombok.Data;

/**
 * Represents a file element within a resource, specifying a particular physical file in the content
 * package.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004File {

  /**
   * The URL or path to the file within the content package.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String href;

  /**
   * Metadata associated with this file, providing details such as file size, creation date, and
   * other descriptive information relevant to the file.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * Specifies whether the file exists in the content package. This is not parsed from the manifest
   * but is set during processing.
   */
  private boolean exists = false;
}
