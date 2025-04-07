/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.input.lom.types;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
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

  /**
   * Default constructor for the ContributeMeta class.
   */
  public ContributeMeta() {
    // Default constructor
  }
}