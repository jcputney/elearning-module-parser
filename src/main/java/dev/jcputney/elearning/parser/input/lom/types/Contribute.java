package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a contribution to the lifecycle of the learning object in the Learning Object Metadata
 * (LOM). This class is part of the lifecycle element and provides detailed information about the
 * contributors, their roles, and the dates associated with their contributions.
 *
 * <p>The following schema snippet defines the structure of this element:
 * <pre>{@code
 * <xs:complexType name="contribute">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="role"/>
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
 *     <value>publisher</value>
 *   </role>
 *   <entity><![CDATA[BEGIN:VCARD
 * VERSION:2.1
 * FN:John Doe
 * END:VCARD]]></entity>
 *   <date>
 *     <dateTime>2023-05-01</dateTime>
 *     <description>
 *       <string language="en">The date when the content was published.</string>
 *     </description>
 *   </date>
 * </contribute>
 * }</pre>
 */
@Data
public class Contribute {

  /**
   * The role of the contributor, specifying their relationship to the learning object. Example
   * roles include "publisher", "creator", or "validator".
   *
   * <p>Schema Definition:
   * <pre>{@code
   * <xs:complexType name="role">
   *   <xs:complexContent>
   *     <xs:extension base="roleVocab">
   *       <xs:attributeGroup ref="ag:role"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "role", useWrapping = false)
  @JacksonXmlProperty(localName = "role")
  private SourceValuePair<Role> role;

  /**
   * A list of entities contributing to the learning object, typically represented as vCard data.
   * Example:
   * <pre>{@code
   * <![CDATA[BEGIN:VCARD
   * VERSION:2.1
   * FN:Jane Doe
   * ORG:Example Organization
   * TEL:+123456789
   * END:VCARD]]>
   * }</pre>
   *
   * <p>Schema Definition:
   * <pre>{@code
   * <xs:complexType name="entity">
   *   <xs:simpleContent>
   *     <xs:extension base="VCard">
   *       <xs:attributeGroup ref="ag:entity"/>
   *     </xs:extension>
   *   </xs:simpleContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "entity", useWrapping = false)
  @JacksonXmlProperty(localName = "entity")
  private List<String> entities;

  /**
   * The date associated with the contribution, such as when the contribution was made or its
   * significance.
   *
   * <p>This includes an optional description that provides additional details about the date.
   *
   * <p>Schema Definition:
   * <pre>{@code
   * <xs:complexType name="date">
   *   <xs:complexContent>
   *     <xs:extension base="DateTime">
   *       <xs:attributeGroup ref="ag:date"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "date")
  private Date date;
}