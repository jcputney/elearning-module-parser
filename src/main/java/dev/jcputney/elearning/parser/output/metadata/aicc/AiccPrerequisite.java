package dev.jcputney.elearning.parser.output.metadata.aicc;

import dev.jcputney.elearning.parser.input.aicc.AiccCompletionCriteria;
import dev.jcputney.elearning.parser.input.aicc.prereq.AiccPrerequisiteExpression;
import dev.jcputney.elearning.parser.input.aicc.prereq.AiccPrerequisiteToken;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Enriched prerequisite details for an assignable unit, including parsed tokens and completion
 * rules.
 */
public final class AiccPrerequisite implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Represents the unique identifier of the assignable unit associated with the prerequisite. This
   * identifier is used to reference a specific assignable unit within a set of prerequisites and
   * conditions. It is immutable once set.
   */
  private final String assignableUnitId;

  /**
   * Represents the original, unprocessed prerequisite expression in string format. This expression
   * defines the conditions under which the associated assignable unit is considered as meeting its
   * prerequisites.
   * <p>
   * It is stored as provided, without interpretation or transformation.
   */
  private final String rawExpression;

  /**
   * Indicates whether the prerequisite condition is mandatory. A value of {@code true} means the
   * prerequisite condition must be met. A value of {@code false} allows the prerequisite condition
   * to be optional.
   */
  private final boolean mandatory;

  /**
   * A list of strings representing the IDs of referenced assignable units (AU) that are
   * prerequisites for this AICC (Aviation Industry Computer-Based Training Committee) structure.
   * Each ID in this list denotes an AU that must be met or completed as a condition in the
   * prerequisite evaluation.
   * <p>
   * This field is immutable and provides a way to track or reference specific AUs required for
   * achieving a prerequisite setup for the AICC configuration.
   */
  private final List<String> referencedAuIds;

  /**
   * A list of optional assignable unit identifiers associated with the prerequisite. This list
   * contains the IDs of assignable units that are not required for completion but may play a role
   * in the prerequisite logic or conditions.
   * <p>
   * This field is immutable and is populated during the instantiation of the
   * {@code AiccPrerequisite} object.
   */
  private final List<String> optionalAuIds;

  /**
   * Represents a list of token strings associated with the AICC prerequisite configuration. The
   * tokens are used to define or process prerequisite rules in textual format. This field is
   * immutable and cannot be modified after assignment.
   */
  private final List<String> tokens;

  /**
   * A list of string values representing the postfix tokens for the prerequisite expression. These
   * tokens are used to evaluate the logical or mathematical structure of the prerequisite in
   * postfix notation. The list is immutable, ensuring the integrity of the tokens throughout the
   * lifecycle of the object.
   */
  private final List<String> postfixTokens;

  /**
   * Represents a parsed and evaluated prerequisite expression associated with an AICC course. This
   * field encapsulates the logical structure and content of a prerequisite condition, enabling
   * validation and evaluation of course prerequisites.
   * <p>
   * The expression is derived from the raw prerequisite string provided in the AICC course
   * configuration. It includes parsed tokens, postfix representations, and an abstract syntax tree
   * (AST) to support execution functions such as prerequisite verification.
   * <p>
   * This field is immutable and thread-safe, ensuring the integrity of the data it represents.
   */
  private final AiccPrerequisiteExpression expression;

  /**
   * Stores the completion criteria for an AICC-based training module. This variable holds an
   * instance of {@link AiccCompletionCriteria}, which defines the rules and attributes necessary to
   * determine whether a training module has been successfully completed.
   * <p>
   * Used within the {@code AiccPrerequisite} class to associate prerequisite rules with specific
   * completion criteria. It encapsulates properties such as completion actions, lesson status,
   * result status, and custom rules defined in an {@link AiccCompletionCriteria} object.
   * <p>
   * Being a final field, it ensures the immutability of the reference once initialized.
   */
  private final AiccCompletionCriteria completionCriteria;

  public AiccPrerequisite(String assignableUnitId, AiccPrerequisiteExpression expression,
      boolean mandatory, List<String> referencedAuIds, List<String> optionalAuIds,
      List<String> tokens, List<String> postfixTokens,
      AiccCompletionCriteria completionCriteria) {
    this.assignableUnitId = assignableUnitId;
    this.expression = expression;
    this.rawExpression = expression == null ? null : expression.getRawExpression();
    this.mandatory = mandatory;
    this.referencedAuIds = referencedAuIds == null ? List.of() : List.copyOf(referencedAuIds);
    this.optionalAuIds = optionalAuIds == null ? List.of() : List.copyOf(optionalAuIds);
    this.tokens = tokens == null ? List.of() : List.copyOf(tokens);
    this.postfixTokens = postfixTokens == null ? List.of() : List.copyOf(postfixTokens);
    this.completionCriteria = completionCriteria;
  }

  /**
   * Converts a list of {@code AiccPrerequisiteToken} objects to a list of trimmed, non-null, and
   * non-empty string values extracted from the tokens.
   *
   * @param tokens the list of {@code AiccPrerequisiteToken} objects; may be null or empty
   * @return a list of non-null, non-empty, trimmed string values derived from the tokens. Returns
   * an empty list if the input list is null or contains no valid values.
   */
  public static List<String> toTokenValues(List<AiccPrerequisiteToken> tokens) {
    if (tokens == null || tokens.isEmpty()) {
      return List.of();
    }
    return tokens
        .stream()
        .map(token -> token == null ? null : token.getValue())
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toList();
  }

  /**
   * Retrieves the unique identifier for the assignable unit.
   *
   * @return the unique identifier of the assignable unit as a String.
   */
  public String getAssignableUnitId() {
    return this.assignableUnitId;
  }

  /**
   * Retrieves the raw expression associated with the prerequisite.
   *
   * @return the raw expression as a String.
   */
  public String getRawExpression() {
    return this.rawExpression;
  }

  /**
   * Determines whether the prerequisite is mandatory.
   *
   * @return {@code true} if the prerequisite is mandatory; {@code false} otherwise.
   */
  public boolean isMandatory() {
    return this.mandatory;
  }

  /**
   * Retrieves the list of referenced assignable unit (AU) IDs associated with the prerequisite.
   *
   * @return a list of Strings representing the referenced AU IDs. The returned list may be empty if
   * no referenced AU IDs are specified.
   */
  public List<String> getReferencedAuIds() {
    return this.referencedAuIds;
  }

  /**
   * Retrieves the list of optional assignable unit (AU) IDs associated with the prerequisite.
   *
   * @return a list of Strings representing the optional AU IDs. The returned list may be empty if
   * no optional AU IDs are specified.
   */
  public List<String> getOptionalAuIds() {
    return this.optionalAuIds;
  }

  /**
   * Retrieves the list of tokens associated with the prerequisite.
   *
   * @return a list of strings representing the tokens. The returned list may be empty if no tokens
   * are specified.
   */
  public List<String> getTokens() {
    return this.tokens;
  }

  /**
   * Retrieves the list of postfix tokens associated with the prerequisite.
   *
   * @return a list of strings representing the postfix tokens. The returned list may be empty if no
   * postfix tokens are specified.
   */
  public List<String> getPostfixTokens() {
    return this.postfixTokens;
  }

  /**
   * Retrieves the prerequisite expression associated with this instance.
   *
   * @return an instance of {@code AiccPrerequisiteExpression} representing the prerequisite
   * expression.
   */
  public AiccPrerequisiteExpression getExpression() {
    return this.expression;
  }

  /**
   * Retrieves the completion criteria associated with the prerequisite.
   *
   * @return an instance of {@code AiccCompletionCriteria} representing the completion criteria.
   */
  public AiccCompletionCriteria getCompletionCriteria() {
    return this.completionCriteria;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccPrerequisite that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isMandatory(), that.isMandatory())
        .append(getAssignableUnitId(), that.getAssignableUnitId())
        .append(getRawExpression(), that.getRawExpression())
        .append(getReferencedAuIds(), that.getReferencedAuIds())
        .append(getOptionalAuIds(), that.getOptionalAuIds())
        .append(getTokens(), that.getTokens())
        .append(getPostfixTokens(), that.getPostfixTokens())
        .append(getExpression(), that.getExpression())
        .append(getCompletionCriteria(), that.getCompletionCriteria())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getAssignableUnitId())
        .append(getRawExpression())
        .append(isMandatory())
        .append(getReferencedAuIds())
        .append(getOptionalAuIds())
        .append(getTokens())
        .append(getPostfixTokens())
        .append(getExpression())
        .append(getCompletionCriteria())
        .toHashCode();
  }
}
