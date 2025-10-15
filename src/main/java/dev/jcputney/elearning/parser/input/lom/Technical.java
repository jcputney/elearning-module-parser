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
import dev.jcputney.elearning.parser.input.lom.properties.PackageProperties;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.LomDuration;
import dev.jcputney.elearning.parser.input.lom.types.OrComposite;
import dev.jcputney.elearning.parser.input.lom.types.Requirement;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the technical information about a learning object in a Learning Object Metadata (LOM)
 * document. Technical information includes details about formats, size, location, requirements, and
 * other platform-specific details.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="technical">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="format"/>
 *     <group ref="size"/>
 *     <group ref="location"/>
 *     <group ref="requirement"/>
 *     <group ref="installationRemarks"/>
 *     <group ref="otherPlatformRequirements"/>
 *     <group ref="duration"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:technical"/>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Technical implements Serializable {

  /**
   * The list of MIME types representing the formats used by the learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="format">
   *   <simpleContent>
   *     <extension base="MimeType">
   *       <attributeGroup ref="ag:format"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "format", useWrapping = false)
  @JacksonXmlProperty(localName = "format")
  private List<String> format;

  /**
   * The size of the learning object, in bytes.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="size">
   *   <simpleContent>
   *     <extension base="Size">
   *       <attributeGroup ref="ag:size"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "size")
  private Integer size;

  /**
   * The list of locations where the learning object can be accessed. Each location is represented
   * as a URL or file path.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="location">
   *   <simpleContent>
   *     <extension base="CharacterString">
   *       <attributeGroup ref="ag:location"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "location", useWrapping = false)
  @JacksonXmlProperty(localName = "location")
  private List<String> location;

  /**
   * The list of requirements for the learning object, each represented as an {@link OrComposite}.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="requirement">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="orComposite"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:requirement"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "requirement", useWrapping = false)
  @JacksonXmlProperty(localName = "requirement")
  private List<Requirement> requirements;

  /**
   * Remarks or instructions about how to install the learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="installationRemarks">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:installationRemarks"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "installationRemarks")
  private SingleLangString installationRemarks;

  /**
   * Additional platform requirements, represented as a single {@link LangString}.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="otherPlatformRequirements">
   *   <complexContent>
   *     <extension base="LangString">
   *       <attributeGroup ref="ag:otherPlatformRequirements"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "otherPlatformRequirements")
  private SingleLangString otherPlatformRequirements;

  /**
   * The duration metadata of the learning object, including the duration value and an optional
   * description.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="duration">
   *   <complexContent>
   *     <extension base="Duration">
   *       <attributeGroup ref="ag:duration"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "duration")
  private LomDuration duration;

  /**
   * Represents the package properties contained within the `Technical` class.
   * <p>
   * This attribute maps to the `ScormEnginePackageProperties` element in the corresponding XML
   * schema as defined by the provided namespace URI. The `PackageProperties` instance encapsulates
   * various configuration details such as controls, appearance, behavior, RSOP (Run-Time State of
   * the Package), and heuristics related to SCORM (Sharable Content Object Reference Model) engine
   * packages.
   * <p>
   * The field is annotated with `@JacksonXmlProperty` to define how it should be serialized or
   * deserialized when working with XML representations. It utilizes the custom namespace defined by
   * `PackageProperties.NAMESPACE_URI`.
   */
  @JacksonXmlProperty(localName = "ScormEnginePackageProperties", namespace = PackageProperties.NAMESPACE_URI)
  private PackageProperties packageProperties;

  /**
   * A placeholder for custom elements that extend the technical information. This allows for
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

  public Technical(List<String> format, Integer size, List<String> location,
      List<Requirement> requirements, SingleLangString installationRemarks,
      SingleLangString otherPlatformRequirements, LomDuration duration,
      PackageProperties packageProperties, List<Object> customElements) {
    this.format = format;
    this.size = size;
    this.location = location;
    this.requirements = requirements;
    this.installationRemarks = installationRemarks;
    this.otherPlatformRequirements = otherPlatformRequirements;
    this.duration = duration;
    this.packageProperties = packageProperties;
    this.customElements = customElements;
  }

  public Technical() {
    // no-op
  }

  /**
   * Retrieves the list of formats associated with this instance.
   *
   * @return a List of Strings representing the formats.
   */
  public List<String> getFormat() {
    return this.format;
  }

  /**
   * Sets the list of formats associated with this instance.
   *
   * @param format a List of Strings representing the formats to be set
   */
  public void setFormat(List<String> format) {
    this.format = format;
  }

  /**
   * Retrieves the size associated with this instance.
   *
   * @return an Integer representing the size.
   */
  public Integer getSize() {
    return this.size;
  }

  /**
   * Sets the size associated with this instance.
   *
   * @param size the size to be set, represented as an Integer
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Retrieves the list of locations associated with this instance.
   *
   * @return a List of Strings representing the locations.
   */
  public List<String> getLocation() {
    return this.location;
  }

  /**
   * Sets the list of location identifiers for this instance.
   *
   * @param location a List of Strings representing the locations to be set
   */
  public void setLocation(List<String> location) {
    this.location = location;
  }

  /**
   * Retrieves the list of requirements associated with this instance.
   *
   * @return a List of {@link Requirement} objects representing the requirements.
   */
  public List<Requirement> getRequirements() {
    return this.requirements;
  }

  /**
   * Sets the list of requirements associated with this instance.
   *
   * @param requirements a List of {@link Requirement} objects representing the requirements to be
   * set
   */
  public void setRequirements(List<Requirement> requirements) {
    this.requirements = requirements;
  }

  /**
   * Retrieves the installation remarks associated with this instance.
   *
   * @return a {@link SingleLangString} representing the installation remarks.
   */
  public SingleLangString getInstallationRemarks() {
    return this.installationRemarks;
  }

  /**
   * Sets the installation remarks associated with this instance.
   *
   * @param installationRemarks a {@link SingleLangString} object representing the installation
   * remarks to be set
   */
  public void setInstallationRemarks(SingleLangString installationRemarks) {
    this.installationRemarks = installationRemarks;
  }

  /**
   * Retrieves the other platform requirements associated with this instance.
   *
   * @return a {@link SingleLangString} object representing the other platform requirements.
   */
  public SingleLangString getOtherPlatformRequirements() {
    return this.otherPlatformRequirements;
  }

  /**
   * Sets the other platform requirements associated with this instance.
   *
   * @param otherPlatformRequirements a {@link SingleLangString} object representing the other
   * platform requirements to be set
   */
  public void setOtherPlatformRequirements(
      SingleLangString otherPlatformRequirements) {
    this.otherPlatformRequirements = otherPlatformRequirements;
  }

  /**
   * Retrieves the duration metadata associated with this instance.
   *
   * @return a {@link LomDuration} object representing the duration metadata, including its value in
   * ISO 8601 format and an optional description.
   */
  public LomDuration getDuration() {
    return this.duration;
  }

  /**
   * Sets the duration metadata for this instance. The duration is typically represented in ISO 8601
   * format (e.g., "PT10M" for 10 minutes) and may include an optional description for additional
   * context.
   *
   * @param duration a {@link LomDuration} object representing the duration metadata, including its
   * value in ISO 8601 format and an optional description
   */
  public void setDuration(LomDuration duration) {
    this.duration = duration;
  }

  /**
   * Retrieves the package properties associated with this instance.
   *
   * @return a {@link PackageProperties} object representing the package properties.
   */
  public PackageProperties getPackageProperties() {
    return this.packageProperties;
  }

  /**
   * Sets the package properties associated with this instance.
   *
   * @param packageProperties a {@link PackageProperties} object representing the package properties
   * to be set
   */
  public void setPackageProperties(PackageProperties packageProperties) {
    this.packageProperties = packageProperties;
  }

  /**
   * Retrieves the list of custom elements associated with this instance.
   *
   * @return a List of Objects representing the custom elements.
   */
  public List<Object> getCustomElements() {
    return this.customElements;
  }

  /**
   * Sets the list of custom elements for this instance.
   *
   * @param customElements a List of Objects representing the custom elements to be set
   */
  public void setCustomElements(List<Object> customElements) {
    this.customElements = customElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Technical technical)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getFormat(), technical.getFormat())
        .append(getSize(), technical.getSize())
        .append(getLocation(), technical.getLocation())
        .append(getRequirements(), technical.getRequirements())
        .append(getInstallationRemarks(), technical.getInstallationRemarks())
        .append(getOtherPlatformRequirements(), technical.getOtherPlatformRequirements())
        .append(getDuration(), technical.getDuration())
        .append(getPackageProperties(), technical.getPackageProperties())
        .append(getCustomElements(), technical.getCustomElements())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getFormat())
        .append(getSize())
        .append(getLocation())
        .append(getRequirements())
        .append(getInstallationRemarks())
        .append(getOtherPlatformRequirements())
        .append(getDuration())
        .append(getPackageProperties())
        .append(getCustomElements())
        .toHashCode();
  }
}