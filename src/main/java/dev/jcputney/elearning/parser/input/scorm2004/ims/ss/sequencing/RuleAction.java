/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the action to perform if the conditions specified in a sequencing rule are met. Rule
 * actions determine the sequencing behavior for the activity, such as enabling, disabling, hiding,
 * or skipping an activity.
 *
 * <p>Actions are only executed if the conditions in the rule's {@link RuleConditions} are
 * met.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RuleAction implements Serializable {

  /**
   * The action to be taken if the conditions are met. Possible actions include:
   * <ul>
   *   <li><strong>skip:</strong> Skip the activity and proceed to the next available activity.</li>
   *   <li><strong>disabled:</strong> Disable the activity, making it unavailable to the learner.</li>
   *   <li><strong>hiddenFromChoice:</strong> Hide the activity from the learner's choice menu.</li>
   *   <li><strong>stopForwardTraversal:</strong> Prevent the learner from progressing forward beyond the activity.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("action")
  private String action;

  /**
   * Default constructor for the RuleAction class.
   * <p>
   * This constructor initializes the RuleAction object without any specific values. It is primarily
   * used to create an instance of RuleAction with default settings.
   */
  public RuleAction() {
    // no-op
  }

  /**
   * Retrieves the action to be performed if the conditions specified in the rule are met.
   *
   * @return the action associated with the rule, such as "skip", "disabled", "hiddenFromChoice", or
   * "stopForwardTraversal"
   */
  public String getAction() {
    return this.action;
  }

  /**
   * Sets the action to be performed if the conditions specified in the sequencing rule are met.
   *
   * @param action the action to set. Possible values include "skip", "disabled",
   * "hiddenFromChoice", or "stopForwardTraversal", which determine the behavior of the rule.
   */
  public void setAction(String action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RuleAction that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getAction(), that.getAction())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getAction())
        .toHashCode();
  }
}
