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

package dev.jcputney.elearning.parser.input.lom.types;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an entity contributing to the lifecycle of the learning object in the Learning Object
 * Metadata (LOM). This class is part of the lifecycle element and provides detailed information
 * about the contributors, their roles, and the dates associated with their contributions.
 *
 * <p>The following schema snippet defines the structure of this element:
 * <pre>{@code
 * <xsd:complexType name="centityType">
 * 		<xsd:sequence>
 * 			<xsd:element ref="vcard"/>
 * 		</xsd:sequence>
 * 	</xsd:complexType>
 * }</pre>
 *
 * <p>Example XML representation:
 * <pre>{@code
 * <contributeEntity vcard="BEGIN:VCARD
 * VERSION:2.1
 * FN:Jane Doe
 * ORG:Example Organization
 * TEL:+123456789
 * END:VCARD"/>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ContributeEntity implements Serializable {

  /**
   * The VCARD representation of the entity contributing to the lifecycle of the learning object.
   * Example:
   * <pre>{@code
   * <![CDATA[BEGIN:VCARD
   * VERSION:2.1
   * FN:Jane Doe
   * ORG:Example Organization
   * TEL:+123456789
   * END:VCARD]]>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "vcard")
  private String vCard;

  /**
   * Default constructor for the ContributeEntity class.
   */
  @SuppressWarnings("unused")
  public ContributeEntity() {
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

    ContributeEntity that = (ContributeEntity) o;

    return new EqualsBuilder()
        .append(vCard, that.vCard)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(vCard)
        .toHashCode();
  }
}
