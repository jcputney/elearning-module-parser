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

package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import java.util.List;
import lombok.Data;

/**
 * Represents a single resource within the content package, typically corresponding to a physical
 * file or collection of files that can be delivered within an LMS.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004Resource {

  /**
   * The unique identifier for this resource, which allows it to be referenced by items.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String identifier;

  /**
   * Specifies the type of resource, such as "webcontent".
   */
  @JacksonXmlProperty(isAttribute = true)
  private String type = "webcontent";

  /**
   * The URL or path to the main entry point file for this resource.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String href;

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  private String base;

  /**
   * Specifies the SCORM type (e.g., "sco" or "asset") to define if this resource is trackable or a
   * static asset.
   */
  @JacksonXmlProperty(namespace = ADLCP.NAMESPACE_URI, isAttribute = true)
  private ScormType scormType;

  /**
   * Metadata associated with this resource, providing details such as author, creation date, and
   * other descriptive information relevant to the resource.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * A list of files associated with this resource, representing the physical files that are part of
   * the learning object.
   */
  @JacksonXmlElementWrapper(localName = "file", useWrapping = false)
  @JacksonXmlProperty(localName = "file", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004File> files;

  /**
   * A list of dependencies that this resource requires. Dependencies define other resources that
   * must be available for this resource to function correctly.
   */
  @JacksonXmlElementWrapper(localName = "dependency", useWrapping = false)
  @JacksonXmlProperty(localName = "dependency", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Dependency> dependencies;
}
