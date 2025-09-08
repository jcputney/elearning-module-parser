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

package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single objective reference.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ObjectiveReference implements Serializable {

  /**
   * The ID reference to the objective, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="idref" type="xs:anyURI"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("idref")
  private String idref;

  public ObjectiveReference() {
  }

  public String getIdref() {
    return this.idref;
  }

  public void setIdref(String idref) {
    this.idref = idref;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ObjectiveReference that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdref(), that.getIdref())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdref())
        .toHashCode();
  }
}
