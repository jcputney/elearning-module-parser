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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import static dev.jcputney.elearning.parser.input.scorm2004.IMSSS.NAMESPACE_URI;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a collection of sequencing elements within the SCORM IMS Simple Sequencing schema. A
 * sequencing collection groups multiple {@link Sequencing} definitions, each of which can specify
 * rules, objectives, and rollup behaviors for a learning activity.
 */
@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SequencingCollection {

  /**
   * A list of {@link Sequencing} elements within the sequencing collection. Each sequencing element
   * defines navigation, rollup, and tracking settings for a specific set of learning activities.
   *
   * <p>These sequencing definitions are used by the LMS to control the flow of activities,
   * based on rules defined for completion, satisfaction, and objectives.</p>
   */
  @JacksonXmlElementWrapper(localName = "sequencing", useWrapping = false)
  @JacksonXmlProperty(localName = "sequencing", namespace = NAMESPACE_URI)
  private List<Sequencing> sequencingList;
}
