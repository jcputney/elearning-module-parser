package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import lombok.Data;

/**
 * Represents metadata for a SCORM element, which can either be inline metadata using a LOM element
 * or an external reference to a metadata file.
 * <p>LOM Example:</p>
 * <pre>{@code
 * <metadata>
 *   <lom>
 *     <general>
 *       <description>
 *         <string language="en-us">Metadata description here.</string>
 *       </description>
 *     </general>
 *   </lom>
 * </metadata>
 * }</pre>
 * <p>External Metadata Example:</p>
 * <pre>{@code
 * <metadata>
 *   <adlcp:location>metadata.xml</adlcp:location>
 * </metadata>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004SubMetadata implements LoadableMetadata {

  /**
   * The location of the external metadata file, referenced using the <code>adlcp:location</code>
   * element.
   */
  @JacksonXmlProperty(localName = "location", namespace = ADLCP.NAMESPACE_URI)
  private String location;

  /**
   * Inline metadata represented as a LOM element.
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  private LOM lom;
}