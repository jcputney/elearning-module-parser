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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The root element of a Learning Object Metadata (LOM) document. This is the entry point for
 * parsing a LOM document. The LOM can be used in a variety of contexts, such as SCORM package or a
 * standalone XML file. The LOM is a standard for describing learning objects and is maintained by
 * the IEEE Learning Technology Standards Committee.
 *
 * <pre>{@code
 * <xs:complexType name="lom">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="general"/>
 *     <xs:group ref="lifeCycle"/>
 *     <xs:group ref="metaMetadata"/>
 *     <xs:group ref="technical"/>
 *     <xs:group ref="educational"/>
 *     <xs:group ref="rights"/>
 *     <xs:group ref="relation"/>
 *     <xs:group ref="annotation"/>
 *     <xs:group ref="classification"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:lom"/>
 * </xs:complexType>
 * }</pre>
 */
@JacksonXmlRootElement(localName = "lom", namespace = LOM.NAMESPACE_URI)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LOM implements Serializable {

  /**
   * The namespace URI for the LOM schema.
   */
  public static final String NAMESPACE_URI = "http://ltsc.ieee.org/xsd/LOM";

  /**
   * The general information about the learning object.
   *
   * <pre>{@code
   * <xs:group ref="general">
   *   <xs:sequence>
   *     <xs:element name="identifier" type="identifier" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="title" type="title" minOccurs="0"/>
   *     <xs:element name="language" type="language" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="description" type="description" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="keyword" type="keyword" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="coverage" type="coverage" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="structure" type="structure" minOccurs="0"/>
   *     <xs:element name="aggregationLevel" type="aggregationLevel" minOccurs="0"/>
   *   </xs:sequence>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "general", namespace = NAMESPACE_URI)
  private General general;

  /**
   * The lifecycle information about the learning object.
   *
   * <pre>{@code
   * <xs:group ref="lifeCycle">
   *   <xs:sequence>
   *     <xs:element name="version" type="version" minOccurs="0"/>
   *     <xs:element name="status" type="status" minOccurs="0"/>
   *     <xs:element name="contribute" type="contribute" minOccurs="0" maxOccurs="unbounded"/>
   *   </xs:sequence>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "lifeCycle", namespace = NAMESPACE_URI)
  private LifeCycle lifecycle;

  /**
   * The meta-metadata information about the learning object.
   *
   * <pre>{@code
   * <xs:group ref="metaMetadata">
   *   <xs:sequence>
   *     <xs:element name="identifier" type="identifier" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="contribute" type="contributeMeta" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="metadataSchema" type="metadataSchema" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="language" type="language" minOccurs="0"/>
   *   </xs:sequence>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "metaMetadata", namespace = NAMESPACE_URI)
  private MetaMetadata metaMetadata;

  /**
   * The technical information about the learning object.
   *
   * <pre>{@code
   * <xs:group ref="technical">
   *   <xs:sequence>
   *     <xs:element name="format" type="format" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="size" type="size" minOccurs="0"/>
   *     <xs:element name="location" type="location" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="requirement" type="requirement" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="installationRemarks" type="installationRemarks" minOccurs="0"/>
   *     <xs:element name="otherPlatformRequirements" type="otherPlatformRequirements" minOccurs="0"/>
   *     <xs:element name="duration" type="duration" minOccurs="0"/>
   *   </xs:sequence>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "technical", namespace = NAMESPACE_URI)
  private Technical technical;

  /**
   * The educational information about the learning object.
   *
   * <pre>{@code
   * <xs:group ref="educational">
   *   <xs:sequence>
   *     <xs:element name="interactivityType" type="interactivityType" minOccurs="0"/>
   *     <xs:element name="learningResourceType" type="learningResourceType" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="interactivityLevel" type="interactivityLevel" minOccurs="0"/>
   *     <xs:element name="semanticDensity" type="semanticDensity" minOccurs="0"/>
   *     <xs:element name="intendedEndUserRole" type="intendedEndUserRole" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="context" type="context" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="typicalAgeRange" type="typicalAgeRange" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="difficulty" type="difficulty" minOccurs="0"/>
   *     <xs:element name="typicalLearningTime" type="typicalLearningTime" minOccurs="0"/>
   *     <xs:element name="description" type="description" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="language" type="language" minOccurs="0" maxOccurs="unbounded"/>
   *   </xs:sequence>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "educational", namespace = NAMESPACE_URI)
  private Educational educational;

  /**
   * Represents the rights information about the learning object.
   * <pre>{@code
   * <xs:complexType name="rights">
   *   <xs:choice minOccurs="0" maxOccurs="unbounded">
   *     <xs:element name="cost" type="cost" minOccurs="0"/>
   *     <xs:element name="copyrightAndOtherRestrictions" type="copyrightAndOtherRestrictions" minOccurs="0"/>
   *     <xs:element name="description" type="description" minOccurs="0" maxOccurs="unbounded"/>
   *   </xs:choice>
   *   <xs:attributeGroup ref="ag:rights"/>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "rights", namespace = NAMESPACE_URI)
  private Rights rights;

  /**
   * Represents a related resource in the context of a relation.
   * <pre>{@code
   * <xs:complexType name="resource">
   *   <xs:choice minOccurs="0" maxOccurs="unbounded">
   *     <xs:element name="identifier" type="identifier" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="description" type="description" minOccurs="0"/>
   *   </xs:choice>
   *   <xs:attributeGroup ref="ag:resource"/>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "relation", useWrapping = false, namespace = NAMESPACE_URI)
  @JacksonXmlProperty(localName = "relation")
  private List<Relation> relations;

  /**
   * Represents an annotation about the learning object.
   * <pre>{@code
   * <xs:complexType name="annotation">
   *   <xs:choice minOccurs="0" maxOccurs="unbounded">
   *     <xs:element name="entity" type="entity" minOccurs="0"/>
   *     <xs:element name="date" type="date" minOccurs="0"/>
   *     <xs:element name="description" type="description" minOccurs="0"/>
   *   </xs:choice>
   *   <xs:attributeGroup ref="ag:annotation"/>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "annotation", useWrapping = false, namespace = NAMESPACE_URI)
  @JacksonXmlProperty(localName = "annotation")
  private List<Annotation> annotations;

  /**
   * Represents a classification of the learning object.
   * <pre>{@code
   * <xs:complexType name="classification">
   *   <xs:choice minOccurs="0" maxOccurs="unbounded">
   *     <xs:element name="purpose" type="purpose" minOccurs="0"/>
   *     <xs:element name="taxonPath" type="taxonPath" minOccurs="0" maxOccurs="unbounded"/>
   *     <xs:element name="description" type="description" minOccurs="0"/>
   *     <xs:element name="keyword" type="keyword" minOccurs="0" maxOccurs="unbounded"/>
   *   </xs:choice>
   *   <xs:attributeGroup ref="ag:classification"/>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "classification", useWrapping = false, namespace = NAMESPACE_URI)
  @JacksonXmlProperty(localName = "classification")
  private List<Classification> classifications;

  public LOM() {
    // no-op
  }

  /**
   * Get the title of the learning object.
   *
   * @return the title of the learning object
   */
  @JsonIgnore
  public String getTitle() {
    return Optional
        .ofNullable(general)
        .map(General::getTitle)
        .map(UnboundLangString::getLangStrings)
        .filter(titles -> !titles.isEmpty())
        .map(titles -> titles
            .get(0)
            .getValue())
        .orElse(null);
  }

  /**
   * Get the description of the learning object.
   *
   * @return the description of the learning object
   */
  @JsonIgnore
  public String getDescription() {
    return Optional
        .ofNullable(general)
        .map(General::getDescription)
        .map(UnboundLangString::getLangStrings)
        .filter(descriptions -> !descriptions.isEmpty())
        .map(descriptions -> descriptions
            .get(0)
            .getValue())
        .orElse(null);
  }

  /**
   * Retrieves the general information of the learning object.
   *
   * @return the general information of the learning object
   */
  public General getGeneral() {
    return this.general;
  }

  /**
   * Sets the general information for the learning object.
   *
   * @param general the general information to set
   */
  public void setGeneral(General general) {
    this.general = general;
  }

  /**
   * Retrieves the lifecycle information of the learning object.
   *
   * @return the lifecycle information associated with the learning object
   */
  public LifeCycle getLifecycle() {
    return this.lifecycle;
  }

  /**
   * Sets the lifecycle information for the learning object.
   *
   * @param lifecycle the lifecycle information to set
   */
  public void setLifecycle(LifeCycle lifecycle) {
    this.lifecycle = lifecycle;
  }

  /**
   * Retrieves the meta-metadata information of the learning object.
   *
   * @return the meta-metadata information associated with the learning object
   */
  public MetaMetadata getMetaMetadata() {
    return this.metaMetadata;
  }

  /**
   * Sets the meta-metadata information for the learning object.
   *
   * @param metaMetadata the meta-metadata information to set
   */
  public void setMetaMetadata(MetaMetadata metaMetadata) {
    this.metaMetadata = metaMetadata;
  }

  /**
   * Retrieves the technical information of the learning object.
   *
   * @return the technical information associated with the learning object
   */
  public Technical getTechnical() {
    return this.technical;
  }

  /**
   * Sets the technical information for the learning object.
   *
   * @param technical the technical information to set
   */
  public void setTechnical(Technical technical) {
    this.technical = technical;
  }

  /**
   * Retrieves the educational information of the learning object.
   *
   * @return the educational information associated with the learning object
   */
  public Educational getEducational() {
    return this.educational;
  }

  /**
   * Sets the educational information for the learning object.
   *
   * @param educational the educational information to set
   */
  public void setEducational(Educational educational) {
    this.educational = educational;
  }

  /**
   * Retrieves the rights information of the learning object.
   *
   * @return the rights information associated with the learning object
   */
  public Rights getRights() {
    return this.rights;
  }

  /**
   * Sets the rights information for the learning object.
   *
   * @param rights the rights information to set
   */
  public void setRights(Rights rights) {
    this.rights = rights;
  }

  /**
   * Retrieves the list of relations associated with the learning object. Relations provide
   * information about connections between the current learning object and other resources.
   *
   * @return a list of {@code Relation} objects representing the relationships of the learning
   * object
   */
  public List<Relation> getRelations() {
    return this.relations;
  }

  /**
   * Sets the list of relations associated with the learning object. Relations provide information
   * about connections between the current learning object and other resources.
   *
   * @param relations a list of {@code Relation} objects representing the relationships of the
   * learning object
   */
  public void setRelations(List<Relation> relations) {
    this.relations = relations;
  }

  /**
   * Retrieves the list of annotations associated with the learning object. Annotations provide
   * additional comments, instructions, or explanations related to the learning object.
   *
   * @return a list of {@code Annotation} objects representing the associated annotations
   */
  public List<Annotation> getAnnotations() {
    return this.annotations;
  }

  /**
   * Sets the list of annotations associated with the learning object. Annotations provide
   * additional comments, instructions, or explanations related to the learning object.
   *
   * @param annotations a list of {@code Annotation} objects representing the annotations to
   * associate with the learning object
   */
  public void setAnnotations(List<Annotation> annotations) {
    this.annotations = annotations;
  }

  /**
   * Retrieves the classifications associated with the learning object.
   *
   * @return a list of {@code Classification} objects representing the classifications of the
   * learning object
   */
  public List<Classification> getClassifications() {
    return this.classifications;
  }

  /**
   * Sets the classifications associated with the learning object.
   *
   * @param classifications a list of {@code Classification} objects representing the
   * classifications to associate with the learning object
   */
  public void setClassifications(List<Classification> classifications) {
    this.classifications = classifications;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LOM lom)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getGeneral(), lom.getGeneral())
        .append(getLifecycle(), lom.getLifecycle())
        .append(getMetaMetadata(), lom.getMetaMetadata())
        .append(getTechnical(), lom.getTechnical())
        .append(getEducational(), lom.getEducational())
        .append(getRights(), lom.getRights())
        .append(getRelations(), lom.getRelations())
        .append(getAnnotations(), lom.getAnnotations())
        .append(getClassifications(), lom.getClassifications())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getGeneral())
        .append(getLifecycle())
        .append(getMetaMetadata())
        .append(getTechnical())
        .append(getEducational())
        .append(getRights())
        .append(getRelations())
        .append(getAnnotations())
        .append(getClassifications())
        .toHashCode();
  }
}
