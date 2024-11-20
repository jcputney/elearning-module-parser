package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12ADLCP;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import lombok.Data;

/**
 * Represents the metadata element in SCORM 1.2.
 *
 * <p>The <code>Metadata</code> element provides descriptive information about an item, resource,
 * or manifest. It can include inline metadata or reference an external metadata file.</p>
 *
 * <p>Schema Snippet:
 * <pre>{@code
 * <xsd:element name="metadata">
 *   <xsd:complexType>
 *     <xsd:sequence>
 *       <xsd:element name="schema" type="xsd:string" minOccurs="0"/>
 *       <xsd:element name="schemaversion" type="xsd:string" minOccurs="0"/>
 *       <xsd:element ref="lom:lom" minOccurs="0"/>
 *       <xsd:element ref="adlcp:location" minOccurs="0"/>
 *     </xsd:sequence>
 *     <xsd:anyAttribute namespace="##other" processContents="lax"/>
 *   </xsd:complexType>
 * </xsd:element>
 * }</pre></p>
 *
 * <p>Example Usage in imsmanifest.xml:
 * <pre>{@code
 * <metadata>
 *   <schema>ADL SCORM</schema>
 *   <schemaversion>1.2</schemaversion>
 *   <lom:lom xmlns:lom="http://ltsc.ieee.org/xsd/LOM">
 *     <general>
 *       <title>
 *         <string language="en">Introduction to Golf</string>
 *       </title>
 *     </general>
 *   </lom:lom>
 * </metadata>
 * }</pre></p>
 */
@Data
public class Scorm12Metadata implements LoadableMetadata {

  /**
   * The schema used in the metadata description, such as "ADL SCORM". This element is optional.
   */
  @JacksonXmlProperty(localName = "schema", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String schema;

  /**
   * The version of the schema used, such as "1.2". This element is optional.
   */
  @JacksonXmlProperty(localName = "schemaversion", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String schemaVersion;

  /**
   * Inline metadata in the form of a Learning Object Metadata (LOM) element. This element is
   * optional.
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  private LOM lom;

  /**
   * A reference to an external metadata file, provided as a URI. This element is optional.
   */
  @JacksonXmlProperty(localName = "location", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private String location;
}
