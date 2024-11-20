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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import lombok.Data;

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
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004SubMetadata implements LoadableMetadata {

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
}