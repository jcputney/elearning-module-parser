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

package dev.jcputney.elearning.parser.input.cmi5;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.Objective;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the objectives section of a CMI5 course structure, containing a list of defined
 * objectives.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="objectivesType">
 *   <xs:sequence>
 *     <xs:element name="objective" minOccurs="1" maxOccurs="unbounded">
 *       <xs:complexType>
 *         <xs:all>
 *           <xs:element name="title" type="textType"/>
 *           <xs:element name="description" type="textType"/>
 *         </xs:all>
 *         <xs:attribute name="id" type="xs:anyURI" use="required"/>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ObjectivesList implements Serializable {

  /**
   * A list of defined objectives, each represented by an {@link Objective}.
   *
   * <pre>{@code
   * <xs:element name="objective" minOccurs="1" maxOccurs="unbounded">
   *   <xs:complexType>
   *     <xs:all>
   *       <xs:element name="title" type="textType"/>
   *       <xs:element name="description" type="textType"/>
   *     </xs:all>
   *     <xs:attribute name="id" type="xs:anyURI" use="required"/>
   *   </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective")
  private List<Objective> objectives;

  /**
   * Constructs an instance of {@code ObjectivesList} with a specified list of objectives.
   *
   * @param objectives the list of objectives to initialize this instance with, where each objective
   * is represented by an {@link Objective}
   */
  public ObjectivesList(List<Objective> objectives) {
    this.objectives = objectives;
  }

  /**
   * Constructs an instance of {@code ObjectivesList} with no predefined objectives. This
   * no-argument constructor initializes an empty ObjectivesList instance.
   */
  public ObjectivesList() {
    // no-op
  }

  /**
   * Retrieves the list of objectives defined within this instance.
   *
   * @return a list of {@link Objective} objects representing the defined objectives
   */
  public List<Objective> getObjectives() {
    return this.objectives;
  }

  /**
   * Sets the list of objectives for this instance. Each objective represents a specific goal with a
   * title, description, and unique identifier.
   *
   * @param objectives the list of {@link Objective} objects to set, where each object defines a
   * goal with its associated metadata
   */
  public void setObjectives(List<Objective> objectives) {
    this.objectives = objectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ObjectivesList that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getObjectives(), that.getObjectives())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getObjectives())
        .toHashCode();
  }
}
