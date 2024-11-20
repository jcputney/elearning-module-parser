package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import lombok.Data;

/**
 * Represents metadata information about the content package in SCORM manifest.
 * <p>
 * This class is used to capture metadata details that help describe the overall package and its
 * associated content. The metadata can include schema and schema version information, as well as a
 * reference to an external metadata file.
 * <pre>{@code
 * <metadata>
 *   <schema>ADL SCORM</schema>
 *   <schemaversion>2004 3rd Edition</schemaversion>
 *   <adlcp:location>metadata_course.xml</adlcp:location>
 * </metadata>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004CourseMetadata implements LoadableMetadata {

  /**
   * The schema used in the metadata description. Defines the structure and versioning information
   * for interpreting the content package.
   * <pre>{@code
   * <schema>ADL SCORM</schema>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "schema", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private String schema;

  /**
   * The version of the schema used. This helps LMS systems interpret the specific content structure
   * and requirements.
   * <pre>{@code
   * <schemaversion>2004 3rd Edition</schemaversion>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "schemaversion", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private String schemaVersion;

  /**
   * The location of the external metadata file, referenced using the <code>adlcp:location</code>
   * element. This allows the manifest to link to a separate file that contains detailed metadata
   * for the course.
   * <pre>{@code
   * <adlcp:location>metadata_course.xml</adlcp:location>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "location", namespace = ADLCP.NAMESPACE_URI)
  private String location;

  /**
   * Inline metadata represented as a LOM element. This provides detailed information about the
   * course, such as the title, description, and other relevant details.
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  private LOM lom;
}