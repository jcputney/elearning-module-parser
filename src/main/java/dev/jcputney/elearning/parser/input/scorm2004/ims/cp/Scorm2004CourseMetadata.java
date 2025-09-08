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

package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
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

  public Scorm2004CourseMetadata() {
  }

  public String getSchema() {
    return this.schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getSchemaVersion() {
    return this.schemaVersion;
  }

  public void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public LOM getLom() {
    return this.lom;
  }

  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  public void setLom(LOM lom) {
    this.lom = lom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004CourseMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSchema(), that.getSchema())
        .append(getSchemaVersion(), that.getSchemaVersion())
        .append(getLocation(), that.getLocation())
        .append(getLom(), that.getLom())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSchema())
        .append(getSchemaVersion())
        .append(getLocation())
        .append(getLom())
        .toHashCode();
  }
}