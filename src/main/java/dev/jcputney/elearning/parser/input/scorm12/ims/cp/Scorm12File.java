/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import lombok.Data;

/**
 * Represents a file within SCORM 1.2 resource.
 *
 * <p>The <code>File</code> element is used to reference physical files that are part of a
 * resource.
 * Each file can also contain metadata providing additional descriptive information.</p>
 *
 * <p>Schema Snippet:</p>
 * <pre>{@code
 * <xsd:element name="file">
 *   <xsd:complexType>
 *     <xsd:sequence>
 *       <xsd:element ref="metadata" minOccurs="0"/>
 *     </xsd:sequence>
 *     <xsd:attribute name="href" type="xsd:anyURI" use="required"/>
 *   </xsd:complexType>
 * </xsd:element>
 * }</pre>
 *
 * <p>Example Usage in imsmanifest.xml:</p>
 * <pre>{@code
 * <file href="index.html">
 *   <metadata>
 *     <schema>ADL SCORM</schema>
 *     <schemaversion>1.2</schemaversion>
 *   </metadata>
 * </file>
 * }</pre>
 */
@Data
public class Scorm12File {

  /**
   * The path to the file relative to the resource's base. This is typically a URI pointing to a
   * specific file within the SCORM package.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "href")
  @JsonProperty(value = "href", required = true)
  private String href;

  /**
   * Metadata providing additional descriptive information about the file. This element is
   * optional.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm12Manifest.NAMESPACE_URI)
  private Scorm12Metadata metadata;

  /**
   * Specifies whether the file exists in the content package. This is not parsed from the manifest
   * but is set during processing.
   */
  private boolean exists = false;
}