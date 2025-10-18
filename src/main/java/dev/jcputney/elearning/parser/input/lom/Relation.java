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

package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Kind;
import dev.jcputney.elearning.parser.input.lom.types.Resource;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the relationship information about a learning object in a Learning Object Metadata
 * (LOM) document. Relations describe connections between the current learning object and other
 * resources.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="relation">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="kind"/>
 *     <group ref="resource"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:relation"/>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Relation implements Serializable {

  /**
   * The kind of relationship, represented as a source-value pair, specifying the type of connection
   * between the current learning object and another resource.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="kind">
   *   <complexContent>
   *     <extension base="kindVocab">
   *       <attributeGroup ref="ag:kind"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "kind")
  private SourceValuePair<Kind> kind;

  /**
   * The resource information that describes the target of the relationship. A resource can include
   * identifiers and descriptions for the related learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="resource">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="identifier"/>
   *     <group ref="description"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:resource"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "resource", useWrapping = false)
  @JacksonXmlProperty(localName = "resource")
  private List<Resource> resource;

  /**
   * Default constructor for the Relation class. Initializes a new instance of the Relation object
   * with no specific properties or behaviors. This constructor is primarily provided for object
   * creation without parameter initialization.
   */
  public Relation() {
    // no-op
  }

  /**
   * Retrieves the kind of relationship represented as a source-value pair, which specifies the type
   * of connection between the current learning object and another resource.
   *
   * @return a {@code SourceValuePair<Kind>} object representing the kind of relationship.
   */
  public SourceValuePair<Kind> getKind() {
    return this.kind;
  }

  /**
   * Sets the kind of relationship, represented as a source-value pair, that specifies the type of
   * connection between the current learning object and another resource.
   *
   * @param kind a {@code SourceValuePair<Kind>} object representing the type of relationship
   * between the learning object and the related resource.
   */
  public void setKind(SourceValuePair<Kind> kind) {
    this.kind = kind;
  }

  /**
   * Retrieves the list of related resources associated with the relationship.
   *
   * @return a {@code List<Resource>} containing the related resources.
   */
  public List<Resource> getResource() {
    return this.resource;
  }

  /**
   * Sets the list of related resources associated with the relationship.
   *
   * @param resource a {@code List<Resource>} representing the related resources to be set
   */
  public void setResource(List<Resource> resource) {
    this.resource = resource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Relation relation)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getKind(), relation.getKind())
        .append(getResource(), relation.getResource())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getKind())
        .append(getResource())
        .toHashCode();
  }
}