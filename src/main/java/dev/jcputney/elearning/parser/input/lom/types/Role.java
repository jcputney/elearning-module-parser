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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>role</code> in a LOM Contribute element,
 * specifying the role of the entity contributing to the resource. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "roleType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "author"/>
 * 			<xs:enumeration value = "publisher"/>
 * 			<xs:enumeration value = "unknown"/>
 * 			<xs:enumeration value = "initiator"/>
 * 			<xs:enumeration value = "terminator"/>
 * 			<xs:enumeration value = "validator"/>
 * 			<xs:enumeration value = "editor"/>
 * 			<xs:enumeration value = "graphical designer"/>
 * 			<xs:enumeration value = "technical implementer"/>
 * 			<xs:enumeration value = "content provider"/>
 * 			<xs:enumeration value = "technical validator"/>
 * 			<xs:enumeration value = "educational validator"/>
 * 			<xs:enumeration value = "script writer"/>
 * 			<xs:enumeration value = "instructional designer"/>
 * 			<xs:enumeration value = "subject matter expert"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Role {
  /**
   * The "author" value specifies that the entity is the author of the resource.
   */
  @JsonProperty("author")
  AUTHOR,

  /**
   * The "publisher" value specifies that the entity is the publisher of the resource.
   */
  @JsonProperty("publisher")
  PUBLISHER,

  /**
   * The "unknown" value specifies that the role of the entity is unknown.
   */
  @JsonProperty("unknown")
  @JsonEnumDefaultValue
  UNKNOWN,

  /**
   * The "initiator" value specifies that the entity initiated the creation of the resource.
   */
  @JsonProperty("initiator")
  INITIATOR,

  /**
   * The "terminator" value specifies that the entity terminated the creation of the resource.
   */
  @JsonProperty("terminator")
  TERMINATOR,

  /**
   * The "validator" value specifies that the entity validated the resource.
   */
  @JsonProperty("validator")
  VALIDATOR,

  /**
   * The "editor" value specifies that the entity edited the resource.
   */
  @JsonProperty("editor")
  EDITOR,

  /**
   * The "graphical designer" value specifies that the entity designed the graphics for the
   * resource.
   */
  @JsonProperty("graphical designer")
  GRAPHICAL_DESIGNER,

  /**
   * The "technical implementer" value specifies that the entity implemented the technical aspects
   * of the resource.
   */
  @JsonProperty("technical implementer")
  TECHNICAL_IMPLEMENTER,

  /**
   * The "content provider" value specifies that the entity provided the content for the resource.
   */
  @JsonProperty("content provider")
  CONTENT_PROVIDER,

  /**
   * The "technical validator" value specifies that the entity validated the technical aspects of
   * the resource.
   */
  @JsonProperty("technical validator")
  TECHNICAL_VALIDATOR,

  /**
   * The "educational validator" value specifies that the entity validated the educational aspects
   * of the resource.
   */
  @JsonProperty("educational validator")
  EDUCATIONAL_VALIDATOR,

  /**
   * The "script writer" value specifies that the entity wrote the script for the resource.
   */
  @JsonProperty("script writer")
  SCRIPT_WRITER,

  /**
   * The "instructional designer" value specifies that the entity designed the instructional aspects
   * of the resource.
   */
  @JsonProperty("instructional designer")
  INSTRUCTIONAL_DESIGNER,

  /**
   * The "subject-matter expert" value specifies that the entity is a subject-matter expert for the
   * resource.
   */
  @JsonProperty("subject matter expert")
  SUBJECT_MATTER_EXPERT
}
