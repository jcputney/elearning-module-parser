/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.aicc.prereq;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an AICC prerequisite expression used in specifying conditions for course
 * prerequisites. This class encapsulates raw expressions, metadata about required and optional
 * course components, and parsed details of the expression syntax and structure to facilitate
 * evaluation and validation.
 *
 * <ul>
 *   <li><b>Raw Expression:</b> The original prerequisite expression in string form.</li>
 *   <li><b>Mandatory:</b> Whether the prerequisite is a mandatory condition.</li>
 *   <li><b>Referenced AU IDs:</b> A list of course component identifiers referenced in the expression.</li>
 *   <li><b>Optional AU IDs:</b> A list of course components marked as optional within the expression.</li>
 *   <li><b>Tokens:</b> A set of tokens obtained from parsing the prerequisite expression in infix form.</li>
 *   <li><b>Postfix Tokens:</b> A set of tokens in postfix form for evaluation purposes.</li>
 *   <li><b>Abstract Syntax Tree (AST):</b> A representation of the expression's structure for conditional evaluation.</li>
 * </ul>
 *
 * This class is immutable and hence thread-safe. All member lists are defensively copied during construction
 * to ensure immutability.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class AiccPrerequisiteExpression implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Represents the raw prerequisite expression as a string. This field stores the original,
   * unprocessed prerequisite expression provided as input. It is used as the basis for parsing and
   * analyzing the expression into tokens, AST nodes, or other structures.
   * <p>
   * The raw expression is immutable and preserved in its original form for reference or debugging
   * purposes while processing the prerequisite logic.
   */
  private final String rawExpression;

  /**
   * Indicates whether the prerequisite expression is marked as mandatory. A mandatory expression
   * must always be satisfied for associated logic or requirements to be fulfilled.
   */
  private final boolean mandatory;

  /**
   * A list of unique identifiers (AU IDs) that are explicitly referenced in the raw prerequisite
   * expression for an AICC (Aviation Industry CBT Committee) course or module. The identifiers
   * typically correspond to specific course components or learning units.
   * <p>
   * This field is immutable and is used to track dependencies or relationships between various
   * elements within the prerequisite structure. It plays a role in evaluating and resolving the
   * logical structure of a prerequisite expression.
   */
  private final List<String> referencedAuIds;

  /**
   * Represents a list of optional AU (Assignable Unit) identifiers that can be included in the
   * evaluation of an AICC prerequisite expression.
   * <p>
   * This field holds identifiers that are not strictly required for the expression to be valid but
   * may provide additional checks or contextual relevance when evaluating the prerequisite logic.
   * <p>
   * It is immutable and initialized during the construction of the AiccPrerequisiteExpression
   * object, ensuring consistency of the expression's state.
   */
  private final List<String> optionalAuIds;

  /**
   * A collection of tokens parsed from an AICC prerequisite expression.
   * <p>
   * Each token in the list represents an individual component of the expression, such as operators,
   * identifiers, or parentheses. The tokens are used to facilitate the processing and evaluation of
   * the prerequisite logic.
   * <p>
   * This list is immutable after initialization, ensuring thread safety.
   */
  private final List<AiccPrerequisiteToken> tokens;

  /**
   * A list of {@link AiccPrerequisiteToken} objects representing a postfix (Reverse Polish
   * Notation) representation of the parsed tokens of an AICC prerequisite expression.
   * <p>
   * This field serves as an intermediary data structure for evaluating or processing the parsed
   * expression by storing tokens in a postfix order, which eliminates the need for parenthesis and
   * simplifies computation by following operator precedence rules.
   * <p>
   * Immutability of the list ensures thread-safety and consistency during operations.
   */
  private final List<AiccPrerequisiteToken> postfixTokens;

  /**
   * Represents the abstract syntax tree (AST) for an AICC prerequisite expression.
   * <p>
   * This field stores the hierarchical structure of the prerequisite expression, which is
   * represented as a tree of {@code AiccPrerequisiteNode} instances. The AST is used to analyze and
   * process the logical relationships and dependencies within the prerequisite expression.
   * <p>
   * The tree nodes encapsulate components such as logical operators and identifiers, enabling
   * evaluation or transformation of the expression.
   * <p>
   * This field is immutable to ensure thread safety and consistent processing results.
   */
  private final AiccPrerequisiteNode ast;

  /**
   * Constructs an instance of AiccPrerequisiteExpression with the specified parameters.
   *
   * @param rawExpression the raw prerequisite expression as a string
   * @param mandatory a boolean indicating whether the prerequisite expression is mandatory
   * @param referencedAuIds a list of strings representing the referenced AU (Assignable Unit) IDs
   * in the prerequisite expression; can be null
   * @param optionalAuIds a list of strings representing the optional AU (Assignable Unit) IDs in
   * the prerequisite expression; can be null
   * @param tokens a list of AiccPrerequisiteToken objects representing the tokens in the
   * expression; can be null
   * @param postfixTokens a list of AiccPrerequisiteToken objects representing the tokens in postfix
   * notation; can be null
   * @param ast the abstract syntax tree (AST) representing the prerequisite expression, as an
   * AiccPrerequisiteNode instance; can be null
   */
  public AiccPrerequisiteExpression(String rawExpression, boolean mandatory,
      List<String> referencedAuIds, List<String> optionalAuIds,
      List<AiccPrerequisiteToken> tokens, List<AiccPrerequisiteToken> postfixTokens,
      AiccPrerequisiteNode ast) {
    this.rawExpression = rawExpression;
    this.mandatory = mandatory;
    this.referencedAuIds = referencedAuIds == null ? List.of() : List.copyOf(referencedAuIds);
    this.optionalAuIds = optionalAuIds == null ? List.of() : List.copyOf(optionalAuIds);
    this.tokens = tokens == null ? List.of() : List.copyOf(tokens);
    this.postfixTokens = postfixTokens == null ? List.of() : List.copyOf(postfixTokens);
    this.ast = ast;
  }

  /**
   * Creates a minimal instance of the AiccPrerequisiteExpression by initializing only the essential
   * fields.
   *
   * @param rawExpression the raw prerequisite expression as a string
   * @param mandatory a boolean indicating whether the prerequisite expression is mandatory
   * @return an instance of AiccPrerequisiteExpression with minimal configuration
   */
  public static AiccPrerequisiteExpression minimal(String rawExpression, boolean mandatory) {
    return new AiccPrerequisiteExpression(rawExpression, mandatory, List.of(), List.of(),
        List.of(), List.of(), null);
  }

  /**
   * Retrieves the raw prerequisite expression as provided during the creation of this instance.
   *
   * @return the raw prerequisite expression as a string
   */
  public String getRawExpression() {
    return this.rawExpression;
  }

  /**
   * Determines whether the prerequisite expression is mandatory.
   *
   * @return true if the prerequisite is mandatory, false otherwise
   */
  public boolean isMandatory() {
    return this.mandatory;
  }

  /**
   * Retrieves the list of referenced AU (Assignable Unit) IDs that are part of the prerequisite
   * expression.
   *
   * @return a list of strings, each representing a referenced AU ID.
   */
  public List<String> getReferencedAuIds() {
    return this.referencedAuIds;
  }

  /**
   * Retrieves the list of optional AU (Assignable Unit) IDs that are part of the prerequisite
   * expression.
   *
   * @return a list of strings, each representing an optional AU ID.
   */
  public List<String> getOptionalAuIds() {
    return this.optionalAuIds;
  }

  /**
   * Retrieves the list of tokens that are part of the AICC prerequisite expression.
   *
   * @return a list of {@link AiccPrerequisiteToken} objects representing the tokens in the
   * expression.
   */
  public List<AiccPrerequisiteToken> getTokens() {
    return this.tokens;
  }

  /**
   * Retrieves the list of tokens that represent the postfix notation of the AICC prerequisite
   * expression. Postfix notation is a mathematical notation in which operators follow their
   * operands. This list represents the parsed expression in a format suitable for evaluation or
   * further processing.
   *
   * @return a list of {@link AiccPrerequisiteToken} objects, each representing a token in the
   * postfix notation of the prerequisite expression.
   */
  public List<AiccPrerequisiteToken> getPostfixTokens() {
    return this.postfixTokens;
  }

  /**
   * Retrieves the abstract syntax tree (AST) representation of the AICC prerequisite expression.
   * The AST is a hierarchical structure of {@link AiccPrerequisiteNode} objects that defines the
   * logical composition of the prerequisite expression, including its operators and components.
   *
   * @return the root node of the abstract syntax tree, represented as an instance of
   * {@link AiccPrerequisiteNode}.
   */
  public AiccPrerequisiteNode getAst() {
    return this.ast;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccPrerequisiteExpression that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isMandatory(), that.isMandatory())
        .append(getRawExpression(), that.getRawExpression())
        .append(getReferencedAuIds(), that.getReferencedAuIds())
        .append(getOptionalAuIds(), that.getOptionalAuIds())
        .append(getTokens(), that.getTokens())
        .append(getPostfixTokens(), that.getPostfixTokens())
        .append(getAst(), that.getAst())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRawExpression())
        .append(isMandatory())
        .append(getReferencedAuIds())
        .append(getOptionalAuIds())
        .append(getTokens())
        .append(getPostfixTokens())
        .append(getAst())
        .toHashCode();
  }

  @Override
  public String toString() {
    return "AiccPrerequisiteExpression{"
        + "rawExpression='" + this.rawExpression + '\''
        + ", mandatory=" + this.mandatory
        + ", referencedAuIds=" + this.referencedAuIds
        + ", optionalAuIds=" + this.optionalAuIds
        + ", tokens=" + this.tokens
        + '}';
  }
}
