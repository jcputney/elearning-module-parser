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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Contribute;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.Status;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LifeCycle implements Serializable {

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
}