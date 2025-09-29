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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>debugSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="debugSpec">
 *   <xs:all>
 *     <xs:element name="controlAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="controlDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="runtimeAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="runtimeDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="sequencingAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="sequencingDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="sequencingSimple" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="lookaheadAudit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="lookaheadDetailed" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="includeTimestamps" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DebugSpec implements Serializable {

  /**
   * The audit level for control.
   */
  @JacksonXmlProperty(localName = "controlAudit")
  private YesNoType controlAudit;

  /**
   * The detailed level for control.
   */
  @JacksonXmlProperty(localName = "controlDetailed")
  private YesNoType controlDetailed;

  /**
   * The audit level for runtime.
   */
  @JacksonXmlProperty(localName = "runtimeAudit")
  private YesNoType runtimeAudit;

  /**
   * The detailed level for runtime.
   */
  @JacksonXmlProperty(localName = "runtimeDetailed")
  private YesNoType runtimeDetailed;

  /**
   * The audit level for sequencing.
   */
  @JacksonXmlProperty(localName = "sequencingAudit")
  private YesNoType sequencingAudit;

  /**
   * The detailed level for sequencing.
   */
  @JacksonXmlProperty(localName = "sequencingDetailed")
  private YesNoType sequencingDetailed;

  /**
   * The simple level for sequencing.
   */
  @JacksonXmlProperty(localName = "sequencingSimple")
  private YesNoType sequencingSimple;

  /**
   * The audit level for lookahead.
   */
  @JacksonXmlProperty(localName = "lookaheadAudit")
  private YesNoType lookaheadAudit;

  /**
   * The detailed level for lookahead.
   */
  @JacksonXmlProperty(localName = "lookaheadDetailed")
  private YesNoType lookaheadDetailed;

  /**
   * Indicates whether to include timestamps.
   */
  @JacksonXmlProperty(localName = "includeTimestamps")
  private YesNoType includeTimestamps;

  public DebugSpec() {
    // no-op
  }

  /**
   * Retrieves the control audit setting for the debug specification.
   *
   * @return the current value of controlAudit, which is of type {@link YesNoType}.
   */
  public YesNoType getControlAudit() {
    return this.controlAudit;
  }

  /**
   * Sets the control audit setting for the debug specification.
   *
   * @param controlAudit the control audit setting to be set, which is of type {@link YesNoType}
   */
  public void setControlAudit(YesNoType controlAudit) {
    this.controlAudit = controlAudit;
  }

  /**
   * Retrieves the control detailed setting for the debug specification.
   *
   * @return the current value of the controlDetailed field, which is of type {@link YesNoType}.
   */
  public YesNoType getControlDetailed() {
    return this.controlDetailed;
  }

  /**
   * Sets the control detailed setting for the debug specification.
   *
   * @param controlDetailed the control detailed setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setControlDetailed(YesNoType controlDetailed) {
    this.controlDetailed = controlDetailed;
  }

  /**
   * Retrieves the runtime audit setting for the debug specification.
   *
   * @return the current value of the runtimeAudit field, which is of type {@link YesNoType}.
   */
  public YesNoType getRuntimeAudit() {
    return this.runtimeAudit;
  }

  /**
   * Sets the runtime audit setting for the debug specification.
   *
   * @param runtimeAudit the runtime audit setting to be set, which is of type {@link YesNoType}
   */
  public void setRuntimeAudit(YesNoType runtimeAudit) {
    this.runtimeAudit = runtimeAudit;
  }

  /**
   * Retrieves the runtime detailed setting for the debug specification.
   *
   * @return the current value of the runtimeDetailed field, which is of type {@link YesNoType}.
   */
  public YesNoType getRuntimeDetailed() {
    return this.runtimeDetailed;
  }

  /**
   * Sets the runtime detailed setting for the debug specification.
   *
   * @param runtimeDetailed the runtime detailed setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setRuntimeDetailed(YesNoType runtimeDetailed) {
    this.runtimeDetailed = runtimeDetailed;
  }

  /**
   * Retrieves the sequencing audit setting for the debug specification.
   *
   * @return the current value of the sequencingAudit field, which is of type {@link YesNoType}.
   */
  public YesNoType getSequencingAudit() {
    return this.sequencingAudit;
  }

  /**
   * Sets the sequencing audit setting for the debug specification.
   *
   * @param sequencingAudit the sequencing audit setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setSequencingAudit(YesNoType sequencingAudit) {
    this.sequencingAudit = sequencingAudit;
  }

  /**
   * Retrieves the sequencing detailed setting for the debug specification.
   *
   * @return the current value of the sequencingDetailed field, which is of type {@link YesNoType}.
   */
  public YesNoType getSequencingDetailed() {
    return this.sequencingDetailed;
  }

  /**
   * Sets the sequencing detailed setting for the debug specification.
   *
   * @param sequencingDetailed the sequencing detailed setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setSequencingDetailed(YesNoType sequencingDetailed) {
    this.sequencingDetailed = sequencingDetailed;
  }

  /**
   * Retrieves the sequencing simple setting for the debug specification.
   *
   * @return the current value of the sequencingSimple field, which is of type {@link YesNoType}.
   */
  public YesNoType getSequencingSimple() {
    return this.sequencingSimple;
  }

  /**
   * Sets the sequencing simple setting for the debug specification.
   *
   * @param sequencingSimple the sequencing simple setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setSequencingSimple(YesNoType sequencingSimple) {
    this.sequencingSimple = sequencingSimple;
  }

  /**
   * Retrieves the lookahead audit setting for the debug specification.
   *
   * @return the current value of the lookaheadAudit field, which is of type {@link YesNoType}.
   */
  public YesNoType getLookaheadAudit() {
    return this.lookaheadAudit;
  }

  /**
   * Sets the lookahead audit setting for the debug specification.
   *
   * @param lookaheadAudit the lookahead audit setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setLookaheadAudit(YesNoType lookaheadAudit) {
    this.lookaheadAudit = lookaheadAudit;
  }

  /**
   * Retrieves the lookahead detailed setting for the debug specification.
   *
   * @return the current value of the lookaheadDetailed field, which is of type {@link YesNoType}.
   */
  public YesNoType getLookaheadDetailed() {
    return this.lookaheadDetailed;
  }

  /**
   * Sets the lookahead detailed setting for the debug specification.
   *
   * @param lookaheadDetailed the lookahead detailed setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setLookaheadDetailed(YesNoType lookaheadDetailed) {
    this.lookaheadDetailed = lookaheadDetailed;
  }

  /**
   * Retrieves the include timestamps setting for the debug specification.
   *
   * @return the current value of the includeTimestamps field, which is of type {@link YesNoType}.
   */
  public YesNoType getIncludeTimestamps() {
    return this.includeTimestamps;
  }

  /**
   * Sets the include timestamps setting for the debug specification.
   *
   * @param includeTimestamps the include timestamps setting to be set, which is of type
   * {@link YesNoType}
   */
  public void setIncludeTimestamps(YesNoType includeTimestamps) {
    this.includeTimestamps = includeTimestamps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DebugSpec debugSpec)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getControlAudit(), debugSpec.getControlAudit())
        .append(getControlDetailed(), debugSpec.getControlDetailed())
        .append(getRuntimeAudit(), debugSpec.getRuntimeAudit())
        .append(getRuntimeDetailed(), debugSpec.getRuntimeDetailed())
        .append(getSequencingAudit(), debugSpec.getSequencingAudit())
        .append(getSequencingDetailed(), debugSpec.getSequencingDetailed())
        .append(getSequencingSimple(), debugSpec.getSequencingSimple())
        .append(getLookaheadAudit(), debugSpec.getLookaheadAudit())
        .append(getLookaheadDetailed(), debugSpec.getLookaheadDetailed())
        .append(getIncludeTimestamps(), debugSpec.getIncludeTimestamps())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getControlAudit())
        .append(getControlDetailed())
        .append(getRuntimeAudit())
        .append(getRuntimeDetailed())
        .append(getSequencingAudit())
        .append(getSequencingDetailed())
        .append(getSequencingSimple())
        .append(getLookaheadAudit())
        .append(getLookaheadDetailed())
        .append(getIncludeTimestamps())
        .toHashCode();
  }
}
