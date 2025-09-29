/*
 * Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Class representing the root element of the ScormEnginePackageProperties XML.</p>
 *
 * <p>The following schema snippet is the root declaration of the XSD:</p>
 * <pre>{@code
 * <xs:element name="ScormEnginePackageProperties">
 *   <xs:complexType>
 *     <xs:all>
 *       <xs:element name="controls" type="controlsSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="appearance" type="appearanceSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="appearence" type="appearanceSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="behavior" type="behaviorSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="rsop" type="rsopSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="heuristics" type="heuristicSpec" minOccurs="0" maxOccurs="1" />
 *     </xs:all>
 *   </xs:complexType>
 * </xs:element>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class PackageProperties implements Serializable {

  /**
   * The namespace URI for the ScormEnginePackageProperties XML.
   */
  public static final String NAMESPACE_URI = "http://www.scorm.com/xsd/ScormEnginePackageProperties";

  /**
   * The "controls" specification.
   */
  @JacksonXmlProperty(localName = "controls", namespace = NAMESPACE_URI)
  private ControlsSpec controls;

  /**
   * Single field handling both "appearance" and the misspelled "appearence".
   */
  @JacksonXmlProperty(localName = "appearance", namespace = NAMESPACE_URI)
  @JsonAlias("appearence") // Handle the misspelled "appearence" element
  private AppearanceSpec appearance;

  /**
   * The behavior specification.
   */
  @JacksonXmlProperty(localName = "behavior", namespace = NAMESPACE_URI)
  private BehaviorSpec behavior;

  /**
   * The RSOP (Run-Time State of the Package) specification.
   */
  @JacksonXmlProperty(localName = "rsop", namespace = NAMESPACE_URI)
  private RsopSpec rsop;

  /**
   * The "heuristics" specification.
   */
  @JacksonXmlProperty(localName = "heuristics", namespace = NAMESPACE_URI)
  private HeuristicSpec heuristics;

  public PackageProperties(ControlsSpec controls, AppearanceSpec appearance, BehaviorSpec behavior,
      RsopSpec rsop, HeuristicSpec heuristics) {
    this.controls = controls;
    this.appearance = appearance;
    this.behavior = behavior;
    this.rsop = rsop;
    this.heuristics = heuristics;
  }

  public PackageProperties() {
    // no-op
  }

  /**
   * Retrieves the control specifications associated with this package.
   *
   * @return the {@code ControlsSpec} instance containing control-related configurations.
   */
  public ControlsSpec getControls() {
    return this.controls;
  }

  /**
   * Updates the control specifications for the current package configuration.
   *
   * @param controls the {@code ControlsSpec} instance providing the control-related configurations
   * to set
   */
  public void setControls(ControlsSpec controls) {
    this.controls = controls;
  }

  /**
   * Retrieves the appearance specifications associated with this package.
   *
   * @return the {@code AppearanceSpec} instance containing appearance-related configurations.
   */
  public AppearanceSpec getAppearance() {
    return this.appearance;
  }

  /**
   * Updates the appearance configuration for the current package.
   *
   * @param appearance the {@code AppearanceSpec} instance providing the appearance-related
   * configurations to set
   */
  public void setAppearance(AppearanceSpec appearance) {
    this.appearance = appearance;
  }

  /**
   * Retrieves the behavior specifications associated with this package.
   *
   * @return the {@code BehaviorSpec} instance containing behavior-related configurations.
   */
  public BehaviorSpec getBehavior() {
    return this.behavior;
  }

  /**
   * Updates the behavior configuration for the current package.
   *
   * @param behavior the {@code BehaviorSpec} instance containing behavior-related configurations to
   * set
   */
  public void setBehavior(BehaviorSpec behavior) {
    this.behavior = behavior;
  }

  /**
   * Retrieves the RSOP (Resource Synchronization Operational Policy) specification associated with
   * this package.
   *
   * @return the {@code RsopSpec} instance containing resource synchronization policy
   * configurations.
   */
  public RsopSpec getRsop() {
    return this.rsop;
  }

  /**
   * Updates the RSOP (Resource Synchronization Operational Policy) configuration for the current
   * package.
   *
   * @param rsop the {@code RsopSpec} instance containing the resource synchronization policy
   * configurations to set
   */
  public void setRsop(RsopSpec rsop) {
    this.rsop = rsop;
  }

  /**
   * Retrieves the heuristic specifications associated with this package.
   *
   * @return the {@code HeuristicSpec} instance containing heuristic-related configurations.
   */
  public HeuristicSpec getHeuristics() {
    return this.heuristics;
  }

  /**
   * Updates the heuristic configuration for the current package.
   *
   * @param heuristics the {@code HeuristicSpec} instance containing heuristic-related
   * configurations to set
   */
  public void setHeuristics(HeuristicSpec heuristics) {
    this.heuristics = heuristics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof PackageProperties that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getControls(), that.getControls())
        .append(getAppearance(), that.getAppearance())
        .append(getBehavior(), that.getBehavior())
        .append(getRsop(), that.getRsop())
        .append(getHeuristics(), that.getHeuristics())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getControls())
        .append(getAppearance())
        .append(getBehavior())
        .append(getRsop())
        .append(getHeuristics())
        .toHashCode();
  }
}