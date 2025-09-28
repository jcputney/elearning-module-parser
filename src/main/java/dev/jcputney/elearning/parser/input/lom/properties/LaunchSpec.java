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
 * <p>Represents the <strong>launchSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="launchSpec">
 *   <xs:all>
 *     <xs:element name="sco" type="launchType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="player" type="launchType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="wrapScoWindowWithApi" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LaunchSpec implements Serializable {

  /**
   * The SCO (Sharable Content Object) launch type.
   */
  @JacksonXmlProperty(localName = "sco")
  private LaunchType sco;

  /**
   * The player launch type.
   */
  @JacksonXmlProperty(localName = "player")
  private LaunchType player;

  /**
   * Indicates whether to wrap the SCO window with API.
   */
  @JacksonXmlProperty(localName = "wrapScoWindowWithApi")
  private YesNoType wrapScoWindowWithApi;

  public LaunchSpec(LaunchType sco, LaunchType player, YesNoType wrapScoWindowWithApi) {
    this.sco = sco;
    this.player = player;
    this.wrapScoWindowWithApi = wrapScoWindowWithApi;
  }

  public LaunchSpec() {
    // no-op
  }

  public LaunchType getSco() {
    return this.sco;
  }

  public void setSco(LaunchType sco) {
    this.sco = sco;
  }

  public LaunchType getPlayer() {
    return this.player;
  }

  public void setPlayer(LaunchType player) {
    this.player = player;
  }

  public YesNoType getWrapScoWindowWithApi() {
    return this.wrapScoWindowWithApi;
  }

  public void setWrapScoWindowWithApi(YesNoType wrapScoWindowWithApi) {
    this.wrapScoWindowWithApi = wrapScoWindowWithApi;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LaunchSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSco(), that.getSco())
        .append(getPlayer(), that.getPlayer())
        .append(getWrapScoWindowWithApi(), that.getWrapScoWindowWithApi())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSco())
        .append(getPlayer())
        .append(getWrapScoWindowWithApi())
        .toHashCode();
  }
}