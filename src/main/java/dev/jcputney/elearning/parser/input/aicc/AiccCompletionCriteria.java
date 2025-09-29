package dev.jcputney.elearning.parser.input.aicc;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the completion criteria for AICC (Aviation Industry CBT Committee) standards. This
 * class encapsulates various properties and rules used to determine the completion status of an
 * AICC-based training module.
 * <p>
 * The class provides fields for standard completion attributes such as action, lesson status, and
 * result status, as well as a collection for additional custom rules.
 * <p>
 * This is a serializable class and overrides the {@code equals} and {@code hashCode} methods to
 * ensure proper equality checks and object usage in hash-based collections.
 */
public class AiccCompletionCriteria implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * A map that holds additional custom rules for defining completion criteria. Each entry in the
   * map represents a key-value pair where the key is a string identifying the custom rule and the
   * value is a string representing the specific configuration or detail of that rule.
   * <p>
   * This map is used to extend the default completion criteria with extra, user-defined rules,
   * allowing for greater flexibility and adaptability to specific AICC-based training
   * requirements.
   * <p>
   * Keys in this map should be unique and non-blank. The map preserves the insertion order of
   * rules.
   */
  private final Map<String, String> additionalRules = new LinkedHashMap<>();

  /**
   * Represents the completion action for a specific AICC completion criteria. This variable
   * typically determines the conditions or operations associated with the completion status of a
   * learning module or activity.
   */
  private String completionAction;

  /**
   * Represents the status of lesson completion in the AICC completion criteria. This variable
   * typically indicates the current state of lesson completion for a particular training or
   * educational activity.
   */
  private String completionLessonStatus;

  /**
   * Represents the completion result status within the AICC (Aviation Industry Computer-Based
   * Training Committee) completion criteria context. This field indicates the status of the result
   * associated with a completion event.
   */
  private String completionResultStatus;

  /**
   * Constructs a new instance of the AiccCompletionCriteria class. This is a no-argument
   * constructor that initializes an instance with default or unconfigured state. It does not
   * perform any specific actions.
   * <p>
   * The AiccCompletionCriteria class provides mechanisms for managing and defining the criteria
   * used for determining the completion status in AICC-compliant learning environments. These
   * criteria typically include actions, lesson statuses, result statuses, and additional rule
   * definitions.
   */
  public AiccCompletionCriteria() {
    // no-op
  }

  /**
   * Retrieves the completion action associated with the AICC completion criteria.
   *
   * @return the completion action as a String
   */
  public String getCompletionAction() {
    return this.completionAction;
  }

  /**
   * Sets the completion action associated with the AICC completion criteria.
   *
   * @param completionAction the completion action as a String; defines the action that represents
   * the completion criteria
   */
  public void setCompletionAction(String completionAction) {
    this.completionAction = completionAction;
  }

  /**
   * Retrieves the completion lesson status associated with the AICC completion criteria.
   *
   * @return the completion lesson status as a String
   */
  public String getCompletionLessonStatus() {
    return this.completionLessonStatus;
  }

  /**
   * Sets the completion lesson status associated with the AICC completion criteria.
   *
   * @param completionLessonStatus the completion lesson status as a String; defines the status that
   * represents the completion criteria for a lesson
   */
  public void setCompletionLessonStatus(String completionLessonStatus) {
    this.completionLessonStatus = completionLessonStatus;
  }

  /**
   * Retrieves the completion result status associated with the AICC completion criteria.
   *
   * @return the completion result status as a String
   */
  public String getCompletionResultStatus() {
    return this.completionResultStatus;
  }

  /**
   * Sets the completion result status associated with the AICC completion criteria.
   *
   * @param completionResultStatus the completion result status as a String; defines the result
   * status that represents the completion criteria
   */
  public void setCompletionResultStatus(String completionResultStatus) {
    this.completionResultStatus = completionResultStatus;
  }

  /**
   * Retrieves the additional rules associated with the AICC completion criteria.
   *
   * @return a map containing the additional rules as key-value pairs, where the key and value
   * represent the rule identifier and its corresponding value, respectively.
   */
  public Map<String, String> getAdditionalRules() {
    return this.additionalRules;
  }

  /**
   * Adds a new rule to the collection of additional rules. The rule is defined as a key-value pair.
   * If the provided key is null or blank, the method does nothing.
   *
   * @param key the identifier for the rule to be added. Must not be null or blank.
   * @param value the corresponding value for the given key. Can be null.
   */
  public void putAdditionalRule(String key, String value) {
    if (key == null || key.isBlank()) {
      return;
    }
    this.additionalRules.put(key, value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccCompletionCriteria that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCompletionAction(), that.getCompletionAction())
        .append(getCompletionLessonStatus(), that.getCompletionLessonStatus())
        .append(getCompletionResultStatus(), that.getCompletionResultStatus())
        .append(getAdditionalRules(), that.getAdditionalRules())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCompletionAction())
        .append(getCompletionLessonStatus())
        .append(getCompletionResultStatus())
        .append(getAdditionalRules())
        .toHashCode();
  }
}
