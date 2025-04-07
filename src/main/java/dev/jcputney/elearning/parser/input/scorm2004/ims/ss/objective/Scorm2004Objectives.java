/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the learning objectives for an activity within the SCORM IMS Simple Sequencing (IMSSS)
 * schema. Objectives define specific learning goals or criteria that are associated with a learning
 * activity and may contribute to the activity’s completion and satisfaction status.
 *
 * <p>The objectives for an activity are classified into a primary objective, which contributes
 * to rollup and tracking of learner progress, and additional objectives that may serve as
 * supporting or secondary goals.</p>
 *
 * <p>Objectives typically include settings such as the minimum measure required for success,
 * satisfaction criteria, and mappings to global objectives. These settings allow for detailed
 * control over how progress is assessed and reported in the LMS.</p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004
 * standards for sequencing and navigation.</p>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Objectives {

  /**
   * The primary objective of the activity. This objective typically contributes to the rollup of
   * progress and satisfaction for the activity, affecting the learner’s overall progress in the
   * sequence.
   *
   * <p>The primary objective is usually a required learning goal that must be achieved
   * to consider the activity successfully completed.</p>
   */
  @JacksonXmlProperty(localName = "primaryObjective", namespace = IMSSS.NAMESPACE_URI)
  private Scorm2004Objective primaryObjective;
  /**
   * A list of additional objectives for the activity. These objectives may serve as supplementary
   * goals that are tracked but do not necessarily affect the rollup of progress or satisfaction.
   *
   * <p>Each objective in this list is represented by an instance of {@link Scorm2004Objective},
   * allowing for fine-grained tracking and evaluation of learner performance.</p>
   */
  @JacksonXmlElementWrapper(localName = "objective", useWrapping = false)
  @JacksonXmlProperty(localName = "objective", namespace = IMSSS.NAMESPACE_URI)
  private List<Scorm2004Objective> objectiveList;

  /**
   * Default constructor for the Scorm2004Objectives class.
   */
  @SuppressWarnings("unused")
  public Scorm2004Objectives() {
    // Default constructor
  }
}
