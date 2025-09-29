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
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Educational implements Serializable {

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

  public Educational() {
    // no-op
  }

  /**
   * Retrieves the interactivity type of the learning resource. The interactivity type specifies the
   * degree of interactivity, which can be active, expositive, mixed, or unknown.
   *
   * @return a SourceValuePair containing the interactivity type of the learning resource
   */
  public SourceValuePair<InteractivityType> getInteractivityType() {
    return this.interactivityType;
  }

  /**
   * Sets the interactivity type for the learning resource. The interactivity type specifies the
   * degree of interactivity, which can be active, expositive, mixed, or unknown.
   *
   * @param interactivityType a SourceValuePair containing the interactivity type of the learning
   * resource
   */
  public void setInteractivityType(
      SourceValuePair<InteractivityType> interactivityType) {
    this.interactivityType = interactivityType;
  }

  /**
   * Retrieves the list of learning resource types associated with this educational resource. A
   * learning resource type specifies the primary nature or format of the resource, such as
   * exercise, simulation, lecture, etc.
   *
   * @return a list of SourceValuePair objects representing the learning resource types
   */
  public List<SourceValuePair<LearningResourceType>> getLearningResourceType() {
    return this.learningResourceType;
  }

  /**
   * Sets the list of learning resource types associated with this educational resource. A learning
   * resource type specifies the primary nature or format of the resource, such as exercise,
   * simulation, lecture, etc.
   *
   * @param learningResourceType a list of SourceValuePair objects representing the learning
   * resource types
   */
  public void setLearningResourceType(
      List<SourceValuePair<LearningResourceType>> learningResourceType) {
    this.learningResourceType = learningResourceType;
  }

  /**
   * Retrieves the interactivity level of the learning resource. The interactivity level specifies
   * the degree of interactivity, which can range from very low to very high.
   *
   * @return a SourceValuePair containing the interactivity level of the learning resource
   */
  public SourceValuePair<InteractivityLevel> getInteractivityLevel() {
    return this.interactivityLevel;
  }

  /**
   * Sets the interactivity level for the learning resource. The interactivity level defines the
   * degree of interaction that the resource promotes, which can range from very low to very high.
   *
   * @param interactivityLevel a SourceValuePair containing the interactivity level of the learning
   * resource
   */
  public void setInteractivityLevel(
      SourceValuePair<InteractivityLevel> interactivityLevel) {
    this.interactivityLevel = interactivityLevel;
  }

  /**
   * Retrieves the semantic density of the learning resource. Semantic density refers to the level
   * of complexity or information richness in the content of the learning resource.
   *
   * @return a SourceValuePair containing the semantic density of the learning resource
   */
  public SourceValuePair<SemanticDensity> getSemanticDensity() {
    return this.semanticDensity;
  }

  /**
   * Sets the semantic density for the learning resource. Semantic density refers to the level of
   * complexity or information richness in the content of the learning resource.
   *
   * @param semanticDensity a SourceValuePair containing the semantic density of the learning
   * resource
   */
  public void setSemanticDensity(
      SourceValuePair<SemanticDensity> semanticDensity) {
    this.semanticDensity = semanticDensity;
  }

  /**
   * Retrieves the list of intended end user roles associated with this educational resource. The
   * intended end user role specifies the role of the individual for whom the resource is designed,
   * such as teacher, author, learner, or manager.
   *
   * @return a list of SourceValuePair objects representing the intended end user roles
   */
  public List<SourceValuePair<IntendedEndUserRole>> getIntendedEndUserRole() {
    return this.intendedEndUserRole;
  }

  /**
   * Sets the list of intended end user roles associated with this educational resource. The
   * intended end user role specifies the role of the individual for whom the resource is designed,
   * such as teacher, author, learner, or manager.
   *
   * @param intendedEndUserRole a list of SourceValuePair objects representing the intended end user
   * roles
   */
  public void setIntendedEndUserRole(
      List<SourceValuePair<IntendedEndUserRole>> intendedEndUserRole) {
    this.intendedEndUserRole = intendedEndUserRole;
  }

  /**
   * Retrieves the list of contexts associated with this educational resource. The context specifies
   * the environment or setting in which the learning object is intended to be used, such as school,
   * higher education, training, or other.
   *
   * @return a list of SourceValuePair objects representing the contexts of the educational resource
   */
  public List<SourceValuePair<Context>> getContext() {
    return this.context;
  }

  /**
   * Sets the context for the learning object. The context specifies the environment or setting for
   * which the learning object is intended, such as school, higher education, training, or other
   * contexts.
   *
   * @param context a list of SourceValuePair objects representing the contexts applicable to the
   * learning object
   */
  public void setContext(
      List<SourceValuePair<Context>> context) {
    this.context = context;
  }

  /**
   * Retrieves the typical age range associated with the educational resource. The typical age range
   * specifies the intended age group for which the resource is designed or most suitable.
   *
   * @return an UnboundLangString representing the typical age range of the educational resource
   */
  public UnboundLangString getTypicalAgeRange() {
    return this.typicalAgeRange;
  }

  /**
   * Sets the typical age range for the educational resource. The typical age range specifies the
   * intended age group for which the resource is designed or most suitable.
   *
   * @param typicalAgeRange an UnboundLangString representing the age range for the educational
   * resource
   */
  public void setTypicalAgeRange(UnboundLangString typicalAgeRange) {
    this.typicalAgeRange = typicalAgeRange;
  }

  /**
   * Retrieves the difficulty level of the learning resource. The difficulty represents the level of
   * challenge associated with the learning object.
   *
   * @return a SourceValuePair containing the difficulty of the learning resource
   */
  public SourceValuePair<Difficulty> getDifficulty() {
    return this.difficulty;
  }

  /**
   * Sets the difficulty level of the educational resource. The difficulty represents the level of
   * challenge associated with the learning object.
   *
   * @param difficulty a SourceValuePair containing the difficulty level of the educational
   * resource
   */
  public void setDifficulty(
      SourceValuePair<Difficulty> difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Retrieves the typical learning time associated with the educational resource. The typical
   * learning time indicates the amount of time generally required to complete the learning activity
   * represented by the resource.
   *
   * @return the typical learning time as a LomDuration object
   */
  public LomDuration getTypicalLearningTime() {
    return this.typicalLearningTime;
  }

  /**
   * Sets the typical learning time for the educational resource. The typical learning time
   * indicates the approximate amount of time generally required to complete the learning activity
   * represented by the resource.
   *
   * @param typicalLearningTime the typical learning time as a LomDuration object
   */
  public void setTypicalLearningTime(LomDuration typicalLearningTime) {
    this.typicalLearningTime = typicalLearningTime;
  }

  /**
   * Retrieves the descriptions of the educational resource. The descriptions provide multilingual
   * text details that describe the resource.
   *
   * @return an UnboundLangString containing the descriptions of the educational resource
   */
  public UnboundLangString getDescriptions() {
    return this.descriptions;
  }

  /**
   * Sets the value of descriptions.
   *
   * @param descriptions the UnboundLangString object to set as the descriptions
   */
  public void setDescriptions(UnboundLangString descriptions) {
    this.descriptions = descriptions;
  }

  /**
   * Retrieves the list of languages.
   *
   * @return a list of strings representing the languages
   */
  public List<String> getLanguages() {
    return this.languages;
  }

  /**
   * Sets the list of languages.
   *
   * @param languages the list of languages to be set
   */
  public void setLanguages(List<String> languages) {
    this.languages = languages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Educational that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getInteractivityType(),
            that.getInteractivityType())
        .append(getLearningResourceType(), that.getLearningResourceType())
        .append(getInteractivityLevel(), that.getInteractivityLevel())
        .append(getSemanticDensity(), that.getSemanticDensity())
        .append(getIntendedEndUserRole(), that.getIntendedEndUserRole())
        .append(getContext(), that.getContext())
        .append(getTypicalAgeRange(), that.getTypicalAgeRange())
        .append(getDifficulty(), that.getDifficulty())
        .append(getTypicalLearningTime(), that.getTypicalLearningTime())
        .append(getDescriptions(), that.getDescriptions())
        .append(getLanguages(), that.getLanguages())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getInteractivityType())
        .append(getLearningResourceType())
        .append(getInteractivityLevel())
        .append(getSemanticDensity())
        .append(getIntendedEndUserRole())
        .append(getContext())
        .append(getTypicalAgeRange())
        .append(getDifficulty())
        .append(getTypicalLearningTime())
        .append(getDescriptions())
        .append(getLanguages())
        .toHashCode();
  }
}