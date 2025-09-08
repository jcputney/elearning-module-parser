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

package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>courseStructure</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="courseStructure">
 *   <xs:all>
 *     <xs:element name="block" type="xs:string" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="member" type="xs:string" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class CourseStructure implements Serializable {

  /**
   * The block element of the course structure.
   */
  @JsonProperty(value = "block", required = true)
  private String block;
  /**
   * The member element of the course structure.
   */
  @JsonProperty(value = "member", required = true)
  private String member;

  public CourseStructure(String block, String member) {
    this.block = block;
    this.member = member;
  }

  public CourseStructure() {
  }

  public String getBlock() {
    return this.block;
  }

  public void setBlock(String block) {
    this.block = block;
  }

  public String getMember() {
    return this.member;
  }

  public void setMember(String member) {
    this.member = member;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CourseStructure that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getBlock(), that.getBlock())
        .append(getMember(), that.getMember())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getBlock())
        .append(getMember())
        .toHashCode();
  }
}
