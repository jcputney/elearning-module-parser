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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a single sequencing rule within a set of pre-condition, exit-condition, or
 * post-condition rules. Each rule consists of a set of conditions and an action.
 */
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SequencingRule implements Serializable {

  /**
   * The conditions that must be met for the rule to apply. These conditions specify criteria such
   * as activity completion status, attempt count, or objective status that control whether the
   * rule's action should be executed.
   */
  @JacksonXmlProperty(localName = "ruleConditions", namespace = IMSSS.NAMESPACE_URI)
  private RuleConditions ruleConditions;
  /**
   * The action to perform if the rule's conditions are met. Actions control the sequencing
   * behavior, such as enabling, disabling, or hiding an activity, or advancing to the next
   * activity.
   */
  @JacksonXmlProperty(localName = "ruleAction", namespace = IMSSS.NAMESPACE_URI)
  private RuleAction ruleAction;
}
