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
 * Enum representing the possible values for <code>type</code> in a learning resource, specifying
 * the type of learning resource. The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "learningResourceType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "exercise"/>
 * 			<xs:enumeration value = "simulation"/>
 * 			<xs:enumeration value = "questionnaire"/>
 * 			<xs:enumeration value = "diagram"/>
 * 			<xs:enumeration value = "figure"/>
 * 			<xs:enumeration value = "graph"/>
 * 			<xs:enumeration value = "index"/>
 * 			<xs:enumeration value = "slide"/>
 * 			<xs:enumeration value = "table"/>
 * 			<xs:enumeration value = "narrative text"/>
 * 			<xs:enumeration value = "exam"/>
 * 			<xs:enumeration value = "experiment"/>
 * 			<xs:enumeration value = "problem statement"/>
 * 			<xs:enumeration value = "self assessment"/>
 * 			<xs:enumeration value = "lecture"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum LearningResourceType {
  @JsonProperty("exercise")
  EXERCISE,

  @JsonProperty("simulation")
  SIMULATION,

  @JsonProperty("questionnaire")
  QUESTIONNAIRE,

  @JsonProperty("diagram")
  DIAGRAM,

  @JsonProperty("figure")
  FIGURE,

  @JsonProperty("graph")
  GRAPH,

  @JsonProperty("index")
  INDEX,

  @JsonProperty("slide")
  SLIDE,

  @JsonProperty("table")
  TABLE,

  @JsonProperty("narrative text")
  NARRATIVE_TEXT,

  @JsonProperty("exam")
  EXAM,

  @JsonProperty("experiment")
  EXPERIMENT,

  @JsonProperty("problem statement")
  PROBLEM_STATEMENT,

  @JsonProperty("self assessment")
  SELF_ASSESSMENT,

  @JsonProperty("lecture")
  LECTURE,

  @JsonEnumDefaultValue
  UNKNOWN
}
