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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a file element within a resource, specifying a particular physical file in the content
 * package.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004File implements Serializable {

  /**
   * The URL or path to the file within the content package.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("href")
  private String href;

  /**
   * Metadata associated with this file, providing details such as file size, creation date, and
   * other descriptive information relevant to the file.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * Specifies whether the file exists in the content package. This is not parsed from the manifest
   * but is set during processing.
   */
  private boolean exists = false;

  public Scorm2004File() {
    // no-op
  }

  /**
   * Retrieves the URL or path to the file within the content package.
   *
   * @return the href representing the location of the file in the content package
   */
  public String getHref() {
    return this.href;
  }

  /**
   * Sets the URL or path to the file within the content package.
   *
   * @param href the href representing the location of the file in the content package
   */
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Retrieves the metadata associated with this file. The metadata provides details such as file
   * size, creation date, and other descriptive information relevant to the file.
   *
   * @return the metadata of type {@code Scorm2004SubMetadata} associated with this file
   */
  public Scorm2004SubMetadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata associated with this file. The metadata provides details such as file size,
   * creation date, and other descriptive information relevant to the file.
   *
   * @param metadata the metadata object containing detailed information about the file
   */
  public void setMetadata(Scorm2004SubMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Checks whether the file exists in the content package.
   *
   * @return true if the file exists, false otherwise
   */
  public boolean isExists() {
    return this.exists;
  }

  /**
   * Sets the existence status of the file.
   *
   * @param exists a boolean value where {@code true} indicates that the file exists, and
   * {@code false} indicates that it does not exist.
   */
  public void setExists(boolean exists) {
    this.exists = exists;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004File that)) {
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
