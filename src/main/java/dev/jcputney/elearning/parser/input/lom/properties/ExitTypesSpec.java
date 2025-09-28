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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>exitTypesSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="exitTypesSpec">
 *   <xs:all>
 *     <xs:element name="normal" type="exitActionType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="suspend" type="exitActionType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="timeout" type="exitActionType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="logout" type="exitActionType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ExitTypesSpec implements Serializable {

  /**
   * The exit action type for normal exit.
   */
  @JacksonXmlProperty(localName = "normal")
  private ExitActionType normal;

  /**
   * The exit action type for suspend exit.
   */
  @JacksonXmlProperty(localName = "suspend")
  private ExitActionType suspend;

  /**
   * The exit action type for timeout exit.
   */
  @JacksonXmlProperty(localName = "timeout")
  private ExitActionType timeout;

  /**
   * The exit action type for logout exit.
   */
  @JacksonXmlProperty(localName = "logout")
  private ExitActionType logout;

  public ExitTypesSpec(ExitActionType normal, ExitActionType suspend, ExitActionType timeout,
      ExitActionType logout) {
    this.normal = normal;
    this.suspend = suspend;
    this.timeout = timeout;
    this.logout = logout;
  }

  public ExitTypesSpec() {
    // no-op
  }

  public ExitActionType getNormal() {
    return this.normal;
  }

  public void setNormal(ExitActionType normal) {
    this.normal = normal;
  }

  public ExitActionType getSuspend() {
    return this.suspend;
  }

  public void setSuspend(ExitActionType suspend) {
    this.suspend = suspend;
  }

  public ExitActionType getTimeout() {
    return this.timeout;
  }

  public void setTimeout(ExitActionType timeout) {
    this.timeout = timeout;
  }

  public ExitActionType getLogout() {
    return this.logout;
  }

  public void setLogout(ExitActionType logout) {
    this.logout = logout;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ExitTypesSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getNormal(), that.getNormal())
        .append(getSuspend(), that.getSuspend())
        .append(getTimeout(), that.getTimeout())
        .append(getLogout(), that.getLogout())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getNormal())
        .append(getSuspend())
        .append(getTimeout())
        .append(getLogout())
        .toHashCode();
  }
}