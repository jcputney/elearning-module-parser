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
 * <p>Represents the <strong>scoExitActionSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="scoExitActionSpec">
 *   <xs:all>
 *     <xs:element name="satisfied" type="exitTypesSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="notSatisfied" type="exitTypesSpec" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ScoExitActionSpec implements Serializable {

  /**
   * The action to take when the SCO is satisfied.
   */
  @JacksonXmlProperty(localName = "satisfied")
  private ExitTypesSpec satisfied;

  /**
   * The action to take when the SCO is not satisfied.
   */
  @JacksonXmlProperty(localName = "notSatisfied")
  private ExitTypesSpec notSatisfied;

  public ScoExitActionSpec(ExitTypesSpec satisfied, ExitTypesSpec notSatisfied) {
    this.satisfied = satisfied;
    this.notSatisfied = notSatisfied;
  }

  public ScoExitActionSpec() {
    // no-op
  }

  /**
   * Retrieves the action to take when the SCO is satisfied.
   *
   * @return the {@code ExitTypesSpec} representing the satisfied action, or null if not defined
   */
  public ExitTypesSpec getSatisfied() {
    return this.satisfied;
  }

  /**
   * Updates the action to take when the SCO is satisfied.
   *
   * @param satisfied an {@code ExitTypesSpec} object representing the set of exit actions to be
   * taken when the SCO is satisfied
   */
  public void setSatisfied(ExitTypesSpec satisfied) {
    this.satisfied = satisfied;
  }

  /**
   * Retrieves the action to take when the SCO is not satisfied.
   *
   * @return the {@code ExitTypesSpec} representing the not satisfied action, or null if not defined
   */
  public ExitTypesSpec getNotSatisfied() {
    return this.notSatisfied;
  }

  /**
   * Sets the action to take when the SCO is not satisfied.
   *
   * @param notSatisfied an {@code ExitTypesSpec} object representing the set of exit actions to be
   * taken when the SCO is not satisfied
   */
  public void setNotSatisfied(ExitTypesSpec notSatisfied) {
    this.notSatisfied = notSatisfied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ScoExitActionSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSatisfied(), that.getSatisfied())
        .append(getNotSatisfied(), that.getNotSatisfied())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSatisfied())
        .append(getNotSatisfied())
        .toHashCode();
  }
}