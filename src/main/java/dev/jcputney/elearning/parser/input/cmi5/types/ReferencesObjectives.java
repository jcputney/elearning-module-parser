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

package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a list of objectives referenced by an AU or block in a CMI5 course structure.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="referencesObjectivesType">
 *   <xs:sequence>
 *     <xs:element name="objective" maxOccurs="unbounded">
 *       <xs:complexType>
 *         <xs:attribute name="idref" type="xs:anyURI"/>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ReferencesObjectives implements Serializable {

  /**
   * A list of referenced objectives, each represented by an {@link ObjectiveReference}.
   *
   * <pre>{@code
   * <xs:element name="objective" maxOccurs="unbounded">
   *   <xs:complexType>
   *     <xs:attribute name="idref" type="xs:anyURI"/>
   *   </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective")
  private List<ObjectiveReference> objectives;

  public ReferencesObjectives(List<ObjectiveReference> objectives) {
    this.objectives = objectives;
  }

  public ReferencesObjectives() {
    // no-op
  }

  /**
   * Retrieves the list of objectives referenced by this instance.
   *
   * @return a list of {@link ObjectiveReference} objects representing the objectives.
   */
  public List<ObjectiveReference> getObjectives() {
    return this.objectives;
  }

  /**
   * Sets the list of objectives for this instance.
   *
   * @param objectives a list of {@link ObjectiveReference} objects representing the objectives to
   * be associated with this instance.
   */
  public void setObjectives(List<ObjectiveReference> objectives) {
    this.objectives = objectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ReferencesObjectives that)) {
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
