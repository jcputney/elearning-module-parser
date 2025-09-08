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

package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a file within SCORM 1.2 resource.
 *
 * <p>The <code>File</code> element is used to reference physical files that are part of a
 * resource. Each file can also contain metadata providing additional descriptive information.</p>
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
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12File implements Serializable {

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

  public Scorm12File() {
  }

  public String getHref() {
    return this.href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public Scorm12Metadata getMetadata() {
    return this.metadata;
  }

  public void setMetadata(Scorm12Metadata metadata) {
    this.metadata = metadata;
  }

  public boolean isExists() {
    return this.exists;
  }

  public void setExists(boolean exists) {
    this.exists = exists;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12File that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isExists(), that.isExists())
        .append(getHref(), that.getHref())
        .append(getMetadata(), that.getMetadata())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getHref())
        .append(getMetadata())
        .append(isExists())
        .toHashCode();
  }
}
