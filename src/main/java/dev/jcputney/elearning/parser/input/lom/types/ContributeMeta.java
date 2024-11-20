package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents the metadata-specific contribution information in the Learning Object Metadata (LOM).
 * This class is part of the meta-metadata element, which contains details about the metadata itself
 * rather than the learning object.
 *
 * <p>The following schema snippet defines the structure of this element:
 * <pre>{@code
 * <xs:complexType name="contributeMeta">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="roleMeta"/>
 *     <xs:group ref="entityUnbounded"/>
 *     <xs:group ref="date"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:contribute"/>
 * </xs:complexType>
 * }</pre>
 *
 * <p>Example XML representation:
 * <pre>{@code
 * <contribute>
 *   <role>
 *     <source>LOMv1.0</source>
 *     <value>creator</value>
 *   </role>
 *   <entity><![CDATA[BEGIN:VCARD
 * VERSION:2.1
 * FN:John Doe
 * END:VCARD]]></entity>
 *   <date>
 *     <dateTime>2023-05-01</dateTime>
 *     <description>
 *       <string language="en">Date of metadata creation.</string>
 *     </description>
 *   </date>
 * </contribute>
 * }</pre>
 */
@Data
public class ContributeMeta {

  /**
   * The role of the contributor, specifying their relationship to the metadata. Example: "creator",
   * "validator", etc.
   */
  @JacksonXmlElementWrapper(localName = "role", useWrapping = false)
  @JacksonXmlProperty(localName = "role")
  private SourceValuePair<RoleMeta> role;

  /**
   * A list of entities contributing to the metadata, typically represented as vCard data. Example:
   * <pre>{@code
   * <![CDATA[BEGIN:VCARD
   * VERSION:2.1
   * FN:John Doe
   * END:VCARD]]>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "entity", useWrapping = false)
  @JacksonXmlProperty(localName = "entity")
  private List<String> entities;

  /**
   * The date associated with the contribution, including optional descriptions.
   */
  @JacksonXmlProperty(localName = "date")
  private Date date;
}