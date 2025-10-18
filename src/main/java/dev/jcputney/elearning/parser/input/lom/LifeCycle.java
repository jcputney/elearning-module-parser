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
import dev.jcputney.elearning.parser.input.lom.types.Contribute;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.Status;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the lifecycle information about a learning object in a Learning Object Metadata (LOM)
 * document. The lifecycle category provides metadata describing the history and current state of
 * the learning object and those who have contributed to its creation and maintenance.
 *
 * <p>The lifecycle element typically includes information such as the version of the object, its
 * status, and the roles and contributions of various entities involved in the object's lifecycle.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="lifeCycle">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="version"/>
 *     <group ref="status"/>
 *     <group ref="contribute"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:lifeCycle"/>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class LifeCycle implements Serializable {

  /**
   * The version information about the learning object. This indicates the current version of the
   * learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="version">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:version"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "version")
  private UnboundLangString version;

  /**
   * The status of the learning object, indicating its stage in the lifecycle (e.g., draft, final,
   * revised).
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="status">
   *   <complexContent>
   *     <extension base="statusVocab">
   *       <attributeGroup ref="ag:status"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "status", useWrapping = false)
  @JacksonXmlProperty(localName = "status")
  private SourceValuePair<Status> status;

  /**
   * The list of contributions made to the learning object. Each contribution includes information
   * about the role of the contributor, their entity (e.g., name or organization), and the date of
   * the contribution.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="contribute">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="role"/>
   *     <group ref="entityUnbounded"/>
   *     <group ref="date"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:contribute"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "contribute", useWrapping = false)
  @JacksonXmlProperty(localName = "contribute")
  private List<Contribute> contribute;

  /**
   * A placeholder for custom elements that extend the lifecycle information. This allows for
   * additional metadata to be included that is not part of the standard schema.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="customElements">
   *   <complexContent>
   *     <extension base="xs:anyType"/>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "customElements", useWrapping = false)
  @JacksonXmlProperty(localName = "customElements")
  private List<Object> customElements;

  /**
   * Default constructor for the LifeCycle class. Initializes a new instance of the LifeCycle object
   * with no predefined values or configurations.
   */
  public LifeCycle() {
    // no-op
  }

  /**
   * Retrieves the version as an object of type {@code UnboundLangString}.
   *
   * @return the {@code UnboundLangString} object representing the version information
   */
  public UnboundLangString getVersion() {
    return this.version;
  }

  /**
   * Sets the version information for the lifecycle object.
   *
   * @param version the {@code UnboundLangString} object representing the version details to set
   */
  public void setVersion(UnboundLangString version) {
    this.version = version;
  }

  /**
   * Retrieves the current status of the lifecycle object as a {@code SourceValuePair<Status>}.
   *
   * @return the {@code SourceValuePair<Status>} object representing the status, which includes the
   * source of the status and its corresponding value.
   */
  public SourceValuePair<Status> getStatus() {
    return this.status;
  }

  /**
   * Sets the status information for the lifecycle object.
   *
   * @param status the {@code SourceValuePair<Status>} object representing the status information to
   * be set. This includes the source of the status and its corresponding value, indicating the
   * state of the learning object (e.g., draft, final, revised, or unavailable).
   */
  public void setStatus(
      SourceValuePair<Status> status) {
    this.status = status;
  }

  /**
   * Retrieves the list of contributions associated with the lifecycle object.
   *
   * @return a {@code List} of {@code Contribute} objects representing the contributions.
   */
  public List<Contribute> getContribute() {
    return this.contribute;
  }

  /**
   * Sets the list of contributions associated with the lifecycle object.
   *
   * @param contribute a {@code List} of {@code Contribute} objects representing the contributions
   * to be associated with the lifecycle object.
   */
  public void setContribute(List<Contribute> contribute) {
    this.contribute = contribute;
  }

  /**
   * Retrieves the list of custom elements associated with the lifecycle object.
   *
   * @return a {@code List} of {@code Object} elements representing the custom attributes or
   * extensions.
   */
  public List<Object> getCustomElements() {
    return this.customElements;
  }

  /**
   * Sets the list of custom elements associated with the lifecycle object.
   *
   * @param customElements a {@code List} of {@code Object} elements representing the custom
   * attributes or extensions to be associated with the lifecycle object.
   */
  public void setCustomElements(List<Object> customElements) {
    this.customElements = customElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LifeCycle lifeCycle)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getVersion(), lifeCycle.getVersion())
        .append(getStatus(), lifeCycle.getStatus())
        .append(getContribute(), lifeCycle.getContribute())
        .append(getCustomElements(), lifeCycle.getCustomElements())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getVersion())
        .append(getStatus())
        .append(getContribute())
        .append(getCustomElements())
        .toHashCode();
  }
}