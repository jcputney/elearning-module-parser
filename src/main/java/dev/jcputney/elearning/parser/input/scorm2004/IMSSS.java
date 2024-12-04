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

package dev.jcputney.elearning.parser.input.scorm2004;

/**
 * Represents the SCORM IMS Simple Sequencing (IMSSS) schema, which defines the sequencing and
 * navigation rules for SCORM-compliant content. This schema is designed to control the progression
 * of learners through SCORM packages based on defined conditions and constraints.
 *
 * <p>The IMSSS schema includes elements for setting control modes, defining rollup and sequencing
 * rules, establishing limit conditions, and configuring objectives, delivery, and randomization
 * controls.</p>
 *
 * <p>The IMSSS namespace is specified by {@link #NAMESPACE_URI}, following the SCORM 2004
 * standards.</p>
 *
 * @see <a href="https://www.imsglobal.org">IMS Global Learning Consortium</a>
 */
public class IMSSS {

  /**
   * The XML namespace URI for SCORM IMS Simple Sequencing (imsss).
   */
  public static final String NAMESPACE_URI = "http://www.imsglobal.org/xsd/imsss";

}
