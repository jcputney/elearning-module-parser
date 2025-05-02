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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>appearanceSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="appearanceSpec">
 *   <xs:all>
 *     <xs:element name="displayStage" type="displayStageSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="courseStructureWidth" type="xs:nonNegativeInteger" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AppearanceSpec {

  /**
   * The display stage element of the appearance specification. Per XSD, this is a complex type, so
   * we map it to DisplayStageSpec.
   */
  @JacksonXmlProperty(localName = "displayStage")
  private DisplayStageSpec displayStage;
  /**
   * The course structure width element of the appearance specification. Per XSD, this is
   * xs:nonNegativeInteger, so we map it to BigInteger.
   */
  @JacksonXmlProperty(localName = "courseStructureWidth")
  private BigInteger courseStructureWidth;

  /**
   * Default constructor for the AppearanceSpec class.
   */
  @SuppressWarnings("unused")
  public AppearanceSpec() {
    // Default constructor
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AppearanceSpec that = (AppearanceSpec) o;

    return new EqualsBuilder()
        .append(displayStage, that.displayStage)
        .append(courseStructureWidth, that.courseStructureWidth)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(displayStage)
        .append(courseStructureWidth)
        .toHashCode();
  }
}
