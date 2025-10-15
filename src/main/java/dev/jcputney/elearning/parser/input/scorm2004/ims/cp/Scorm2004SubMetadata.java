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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm2004SubMetadata implements LoadableMetadata {

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

  public Scorm2004SubMetadata() {
    // no-op
  }

  /**
   * Retrieves the location of the external metadata file referenced by the
   * <code>adlcp:location</code> element.
   *
   * @return the location of the external metadata file as a string
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * Sets the location of the external metadata file. The location is represented by the
   * <code>adlcp:location</code> element and typically specifies the path or URL to the metadata
   * file.
   *
   * @param location the location of the external metadata file as a string
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Retrieves the inline metadata represented as a LOM (Learning Object Metadata) element.
   *
   * @return the LOM instance containing inline metadata
   */
  public LOM getLom() {
    return this.lom;
  }

  /**
   * Sets the inline metadata represented as a LOM (Learning Object Metadata) element.
   *
   * @param lom the LOM instance containing inline metadata
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  public void setLom(LOM lom) {
    this.lom = lom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004SubMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getLocation(), that.getLocation())
        .append(getLom(), that.getLom())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLocation())
        .append(getLom())
        .toHashCode();
  }
}