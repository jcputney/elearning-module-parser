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

package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Context;
import dev.jcputney.elearning.parser.input.lom.types.Difficulty;
import dev.jcputney.elearning.parser.input.lom.types.IntendedEndUserRole;
import dev.jcputney.elearning.parser.input.lom.types.InteractivityLevel;
import dev.jcputney.elearning.parser.input.lom.types.InteractivityType;
import dev.jcputney.elearning.parser.input.lom.types.LearningResourceType;
import dev.jcputney.elearning.parser.input.lom.types.LomDuration;
import dev.jcputney.elearning.parser.input.lom.types.SemanticDensity;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the educational information about a learning object in a Learning Object Metadata
 * (LOM) document. Educational information includes details about the interactivity type, intended
 * user roles, and other pedagogical aspects of the learning object.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="educational">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="interactivityType"/>
 *     <group ref="learningResourceType"/>
 *     <group ref="interactivityLevel"/>
 *     <group ref="semanticDensity"/>
 *     <group ref="intendedEndUserRole"/>
 *     <group ref="context"/>
 *     <group ref="typicalAgeRange"/>
 *     <group ref="difficulty"/>
 *     <group ref="typicalLearningTime"/>
 *     <group ref="descriptionUnbounded"/>
 *     <group ref="languageId"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:educational"/>
 * </complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Educational {

  /**
   * The interactivity type of the learning object, represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="interactivityType">
   *   <complexContent>
   *     <extension base="interactivityTypeVocab">
   *       <attributeGroup ref="ag:interactivityType"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "interactivityType", useWrapping = false)
  @JacksonXmlProperty(localName = "interactivityType")
  private SourceValuePair<InteractivityType> interactivityType;

  /**
   * The types of learning resources associated with the learning object, represented as a list of
   * source-value pairs.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="learningResourceType">
   *   <complexContent>
   *     <extension base="learningResourceTypeVocab">
   *       <attributeGroup ref="ag:learningResourceType"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "learningResourceType", useWrapping = false)
  @JacksonXmlProperty(localName = "learningResourceType")
  private List<SourceValuePair<LearningResourceType>> learningResourceType;

  /**
   * The interactivity level of the learning object, represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="interactivityLevel">
   *   <complexContent>
   *     <extension base="interactivityLevelVocab">
   *       <attributeGroup ref="ag:interactivityLevel"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "interactivityLevel", useWrapping = false)
  @JacksonXmlProperty(localName = "interactivityLevel")
  private SourceValuePair<InteractivityLevel> interactivityLevel;

  /**
   * The semantic density of the learning object, represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="semanticDensity">
   *   <complexContent>
   *     <extension base="semanticDensityVocab">
   *       <attributeGroup ref="ag:semanticDensity"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "semanticDensity", useWrapping = false)
  @JacksonXmlProperty(localName = "semanticDensity")
  private SourceValuePair<SemanticDensity> semanticDensity;

  /**
   * The intended end-user roles for the learning object, represented as a list of source-value
   * pairs.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="intendedEndUserRole">
   *   <complexContent>
   *     <extension base="intendedEndUserRoleVocab">
   *       <attributeGroup ref="ag:intendedEndUserRole"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "intendedEndUserRole", useWrapping = false)
  @JacksonXmlProperty(localName = "intendedEndUserRole")
  private List<SourceValuePair<IntendedEndUserRole>> intendedEndUserRole;

  /**
   * The context in which the learning object is intended to be used, represented as a list of
   * source-value pairs.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="context">
   *   <complexContent>
   *     <extension base="contextVocab">
   *       <attributeGroup ref="ag:context"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "context", useWrapping = false)
  @JacksonXmlProperty(localName = "context")
  private List<SourceValuePair<Context>> context;

  /**
   * The typical age range of the intended audience, represented as a list of language-specific
   * strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="typicalAgeRange">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:typicalAgeRange"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "typicalAgeRange")
  private UnboundLangString typicalAgeRange;

  /**
   * The difficulty level of the learning object, represented as a source-value pair.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="difficulty">
   *   <complexContent>
   *     <extension base="difficultyVocab">
   *       <attributeGroup ref="ag:difficulty"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "difficulty", useWrapping = false)
  @JacksonXmlProperty(localName = "difficulty")
  private SourceValuePair<Difficulty> difficulty;

  /**
   * The typical learning time required to complete the learning object, represented as a duration.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="typicalLearningTime">
   *   <complexContent>
   *     <extension base="Duration">
   *       <attributeGroup ref="ag:typicalLearningTime"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "typicalLearningTime")
  private LomDuration typicalLearningTime;

  /**
   * Descriptions of the learning object, represented as a list of language-specific strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="description">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:description"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private UnboundLangString descriptions;

  /**
   * The language of the learning object, represented as a list of language codes.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="language">
   *   <simpleContent>
   *     <extension base="LanguageId">
   *       <attributeGroup ref="ag:language"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "language", useWrapping = false)
  @JacksonXmlProperty(localName = "language")
  private List<String> languages;
}