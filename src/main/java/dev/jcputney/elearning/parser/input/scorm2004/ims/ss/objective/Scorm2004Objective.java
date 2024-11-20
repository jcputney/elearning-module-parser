/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.util.List;
import lombok.Data;

/**
 * Represents an individual learning objective within the SCORM IMS Simple Sequencing schema.
 * "Objectives" specify criteria such as minimum required performance, satisfaction requirements,
 * and mappings to global "objectives" for tracking learner progress.
 */
@Data
public class Scorm2004Objective {

  /**
   * A unique identifier for the "objective", provided as a URI. This identifier distinguishes the
   * "objective" and allows it to be referenced in sequencing and navigation rules.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String objectiveID;

  /**
   * The minimum normalized measure required to consider this "objective" as satisfied. This value
   * represents the minimum level of achievement, typically as a decimal between 0 and 1, where 1.0
   * represents full satisfaction.
   *
   * <p>If not specified, the objective may be satisfied based on other criteria or defaults.</p>
   */
  @JacksonXmlProperty(localName = "minNormalizedMeasure", namespace = IMSSS.NAMESPACE_URI)
  private Double minNormalizedMeasure;

  /**
   * A list of mappings to global "objectives". These mappings associate this local "objective" with
   * broader goals or learning objectives across multiple activities or sequences.
   *
   * <p>Each mapping is represented by an instance of {@link Scorm2004ObjectiveMapping}, allowing
   * control over whether specific aspects, such as satisfaction and measure, are shared between the
   * local and global objectives.</p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "mapInfo", namespace = IMSSS.NAMESPACE_URI)
  private List<Scorm2004ObjectiveMapping> mapInfo;
}
