/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
public final class LaunchSpec implements Serializable {

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

  /**
   * Constructs a new instance of the {@code LaunchSpec} class.
   *
   * @param sco the launch type for the Sharable Content Object (SCO), specified as an instance of
   * {@code LaunchType}
   * @param player the launch type for the player, specified as an instance of {@code LaunchType}
   * @param wrapScoWindowWithApi a {@code YesNoType} value indicating whether the SCO window should
   * be wrapped with an API
   */
  public LaunchSpec(LaunchType sco, LaunchType player, YesNoType wrapScoWindowWithApi) {
    this.sco = sco;
    this.player = player;
    this.wrapScoWindowWithApi = wrapScoWindowWithApi;
  }

  /**
   * Constructs a new instance of the {@code LaunchSpec} class with no initial values.
   * <p>
   * This default constructor performs no additional initialization and is intended for use in
   * scenarios where the instance will be configured post-construction.
   */
  public LaunchSpec() {
    // no-op
  }

  /**
   * Retrieves the SCO (Sharable Content Object) launch type.
   *
   * @return an instance of {@code LaunchType} representing the SCO launch type, or {@code null} if
   * not set.
   */
  public LaunchType getSco() {
    return this.sco;
  }

  /**
   * Sets the SCO (Sharable Content Object) launch type.
   *
   * @param sco the SCO launch type to be set; an instance of {@code LaunchType} representing how
   * the SCO should be launched
   */
  public void setSco(LaunchType sco) {
    this.sco = sco;
  }

  /**
   * Retrieves the player launch type.
   *
   * @return an instance of {@code LaunchType} representing the player launch type, or {@code null}
   * if not set.
   */
  public LaunchType getPlayer() {
    return this.player;
  }

  /**
   * Sets the player launch type.
   *
   * @param player the player launch type to be set; an instance of {@code LaunchType} representing
   * how the player should be launched
   */
  public void setPlayer(LaunchType player) {
    this.player = player;
  }

  /**
   * Retrieves the value that indicates whether the SCO window should be wrapped with an API.
   *
   * @return an instance of {@code YesNoType} representing the configuration for wrapping the SCO
   * window with an API.
   */
  public YesNoType getWrapScoWindowWithApi() {
    return this.wrapScoWindowWithApi;
  }

  /**
   * Sets the value that specifies whether the SCO (Sharable Content Object) window should be
   * wrapped with an API.
   *
   * @param wrapScoWindowWithApi an instance of {@code YesNoType} that determines whether the SCO
   * window should be wrapped with an API. Acceptable values are {@code YES}, {@code NO},
   * {@code TRUE}, or {@code FALSE}.
   */
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