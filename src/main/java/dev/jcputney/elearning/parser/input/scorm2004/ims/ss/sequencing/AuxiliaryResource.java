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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an individual auxiliary resource within the SCORM IMS Simple Sequencing schema. An
 * auxiliary resource is a specific resource that supports the main content but does not directly
 * impact the learner's progress or completion of the activity.
 *
 * <p>Auxiliary resources could include references like PDF documents, instructional videos,
 * diagrams, or external tools that help learners in understanding or practicing the activity
 * content.</p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AuxiliaryResource implements Serializable {

  /**
   * A unique identifier for the auxiliary resource, provided as a URI. This identifier links the
   * resource with its intended purpose and distinguishes it from other resources.
   *
   * <p>The <code>auxiliaryResourceID</code> must be specified as a URI (Uniform Resource
   * Identifier).</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("auxiliaryResourceID")
  private String auxiliaryResourceID;
  /**
   * Describes the purpose of the auxiliary resource. This attribute provides context for how the
   * resource is intended to support the learning activity, such as "reference material," "practice
   * tool," or "additional reading."
   *
   * <p>This attribute is required and provides instructional designers with a way to
   * clarify the intended use of the auxiliary resource for both learners and educators.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("purpose")
  private String purpose;

  public AuxiliaryResource() {
  }

  public String getAuxiliaryResourceID() {
    return this.auxiliaryResourceID;
  }

  public void setAuxiliaryResourceID(String auxiliaryResourceID) {
    this.auxiliaryResourceID = auxiliaryResourceID;
  }

  public String getPurpose() {
    return this.purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AuxiliaryResource that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getAuxiliaryResourceID(),
            that.getAuxiliaryResourceID())
        .append(getPurpose(), that.getPurpose())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getAuxiliaryResourceID())
        .append(getPurpose())
        .toHashCode();
  }
}
