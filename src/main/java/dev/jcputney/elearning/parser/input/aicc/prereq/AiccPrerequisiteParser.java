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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * A utility class for parsing AICC prerequisite expressions into structured representations. The
 * class provides functionality to tokenize, validate, and process prerequisite strings into a
 * format suitable for further evaluation or manipulation.
 * <p>
 * This class uses methods to convert the input expression into tokens, rearrange them into postfix
 * notation, construct an Abstract Syntax Tree (AST), and extract identifiers from the expression.
 * The class supports operators such as "AND", "OR", and "NOT", as well as optional markers for
 * dependencies.
 */
public final class AiccPrerequisiteParser {

  /**
   * Represents the precedence value assigned to the logical "NOT" operator used in parsing AICC
   * prerequisite expressions. The "NOT" operator has the highest precedence among logical operators
   * in this context.
   * <p>
   * This constant is used to determine the processing order of operators during expression parsing
   * and evaluation, particularly in methods implementing the shunting-yard algorithm and expression
   * tree construction.
   */
  private static final int PRECEDENCE_NOT = 3;

  /**
   * Represents the precedence value for the logical "AND" operator in an AICC prerequisite
   * expression. The "AND" operator has a lower precedence than "NOT" but a higher precedence than
   * "OR".
   * <p>
   * This constant is used in methods that evaluate or transform prerequisite expressions, such as
   * determining token precedence and converting expressions to postfix notation.
   */
  private static final int PRECEDENCE_AND = 2;

  /**
   * Represents the precedence value associated with the logical "OR" operator in the context of
   * parsing AICC prerequisite expressions. This value is used to determine the order of operations
   * when processing logical expressions.
   */
  private static final int PRECEDENCE_OR = 1;

  private AiccPrerequisiteParser() {
    // Utility class
  }

  /**
   * Parses a raw prerequisite expression.
   *
   * @param rawExpression the raw expression from the course structure file
   * @return an optional parsed expression; empty if the input is blank or cannot be parsed
   */
  public static Optional<AiccPrerequisiteExpression> parse(String rawExpression) {
    if (StringUtils.isBlank(rawExpression)) {
      return Optional.empty();
    }

    String trimmed = rawExpression.trim();
    try {
      List<AiccPrerequisiteToken> tokens = tokenize(trimmed);
      if (tokens.isEmpty()) {
        return Optional.of(AiccPrerequisiteExpression
            .minimal(trimmed, !trimmed.contains("*")));
      }

      List<AiccPrerequisiteToken> postfix = toPostfix(tokens);
      AiccPrerequisiteNode ast = buildAst(postfix);
      List<String> referenced = collectIdentifiers(tokens, false);
      List<String> optional = collectIdentifiers(tokens, true);
      boolean mandatory = optional.isEmpty();

      return Optional.of(new AiccPrerequisiteExpression(trimmed, mandatory, referenced, optional,
          tokens, postfix, ast));
    } catch (Exception ignored) {
      // Expression parsing is best-effort; fall back to raw string when we cannot parse.
      return Optional.of(AiccPrerequisiteExpression
          .minimal(trimmed, !trimmed.contains("*")));
    }
  }

  /**
   * Tokenizes an input string into a list of AiccPrerequisiteToken objects, which represent
   * individual components of an AICC prerequisite expression. This method parses identifiers,
   * operators, parentheses, and optional flags while preserving the structure of the input.
   *
   * @param input the raw input string representing an AICC prerequisite expression
   * @return a list of AiccPrerequisiteToken objects parsed from the input string
   */
  private static List<AiccPrerequisiteToken> tokenize(String input) {
    List<AiccPrerequisiteToken> tokens = new ArrayList<>();
    StringBuilder buffer = new StringBuilder();
    boolean optionalPending = false;

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      switch (c) {
        case ' ', '\t', '\r', '\n':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = false;
          break;
        case '*':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = true;
          break;
        case '(':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = false;
          tokens.add(new AiccPrerequisiteToken(AiccPrerequisiteTokenType.LEFT_PAREN, "("));
          break;
        case ')':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = false;
          tokens.add(new AiccPrerequisiteToken(AiccPrerequisiteTokenType.RIGHT_PAREN, ")"));
          break;
        case ',', ';', '&':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = false;
          tokens.add(operatorToken("AND"));
          break;
        case '|':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = false;
          tokens.add(operatorToken("OR"));
          break;
        case '!', '~':
          flushBufferedToken(tokens, buffer, optionalPending);
          optionalPending = false;
          tokens.add(operatorToken("NOT"));
          break;
        default:
          buffer.append(c);
          break;
      }
    }

    flushBufferedToken(tokens, buffer, optionalPending);
    return tokens;
  }

  /**
   * Flushes the contents of the string buffer into the tokens list as a parsed AICC prerequisite
   * token. This method processes and clears the current buffer, determining whether the buffer
   * content is an operator ("AND", "OR", "NOT") or an identifier. If the identifier starts with
   * '*', it is marked as optional.
   *
   * @param tokens the list of AICC prerequisite tokens to which the parsed token will be added
   * @param buffer a mutable string representation of the current token being processed
   * @param optionalPending indicates whether the next identifier should be treated as optional
   */
  private static void flushBufferedToken(List<AiccPrerequisiteToken> tokens, StringBuilder buffer,
      boolean optionalPending) {
    if (buffer.isEmpty()) {
      return;
    }
    String raw = buffer
        .toString()
        .trim();
    buffer.setLength(0);
    if (raw.isEmpty()) {
      return;
    }

    String upper = raw.toUpperCase(Locale.ROOT);
    switch (upper) {
      case "AND" -> tokens.add(operatorToken("AND"));
      case "OR" -> tokens.add(operatorToken("OR"));
      case "NOT" -> tokens.add(operatorToken("NOT"));
      default -> {
        boolean optional = optionalPending || raw.startsWith("*");
        String identifier = stripLeadingOptional(raw);
        if (!identifier.isEmpty()) {
          tokens.add(new AiccPrerequisiteToken(AiccPrerequisiteTokenType.IDENTIFIER, identifier,
              optional, false));
        }
      }
    }
  }

  private static AiccPrerequisiteToken operatorToken(String op) {
    boolean unary = "NOT".equals(op);
    return new AiccPrerequisiteToken(AiccPrerequisiteTokenType.OPERATOR, op, false, unary);
  }

  private static String stripLeadingOptional(String raw) {
    String cleaned = raw.strip();
    if (cleaned.startsWith("*")) {
      cleaned = cleaned.substring(1);
    }
    if ((cleaned.startsWith("\"") && cleaned.endsWith("\""))
        || (cleaned.startsWith("'") && cleaned.endsWith("'"))) {
      cleaned = cleaned.substring(1, cleaned.length() - 1);
    }
    return cleaned.trim();
  }

  /**
   * Converts a list of AiccPrerequisiteToken objects from infix notation to postfix notation using
   * the shunting-yard algorithm. The postfix notation is used for easier parsing and evaluation of
   * expressions later in the process.
   *
   * @param tokens the list of AiccPrerequisiteToken objects in infix notation. This may include
   * identifiers, operators, and parentheses.
   * @return a list of AiccPrerequisiteToken objects converted to postfix notation.
   */
  private static List<AiccPrerequisiteToken> toPostfix(List<AiccPrerequisiteToken> tokens) {
    List<AiccPrerequisiteToken> output = new ArrayList<>();
    Deque<AiccPrerequisiteToken> stack = new ArrayDeque<>();
    for (AiccPrerequisiteToken token : tokens) {
      switch (token.getType()) {
        case IDENTIFIER -> output.add(token);
        case OPERATOR -> handleOperator(token, output, stack);
        case LEFT_PAREN -> stack.push(token);
        case RIGHT_PAREN -> handleRightParenthesis(output, stack);
        default -> {
          // no-op
        }
      }
    }
    flushStackToOutput(output, stack);
    return output;
  }

  /**
   * Handles an operator token during the shunting-yard algorithm. It moves operators from the stack
   * to the output queue if they have higher or equal precedence.
   *
   * @param token the operator token to handle
   * @param output the output list of tokens in postfix notation
   * @param stack the operator stack
   */
  private static void handleOperator(
      AiccPrerequisiteToken token,
      List<AiccPrerequisiteToken> output,
      Deque<AiccPrerequisiteToken> stack) {
    while (!stack.isEmpty()) {
      AiccPrerequisiteToken top = stack.peek();
      if (top.getType() == AiccPrerequisiteTokenType.OPERATOR && precedence(top) >= precedence(
          token)) {
        output.add(stack.pop());
      } else {
        break;
      }
    }
    stack.push(token);
  }

  /**
   * Handles a right parenthesis token. It moves operators from the stack to the output queue until
   * a left parenthesis is found.
   *
   * @param output the output list of tokens in postfix notation
   * @param stack the operator stack
   */
  private static void handleRightParenthesis(
      List<AiccPrerequisiteToken> output, Deque<AiccPrerequisiteToken> stack) {
    while (!stack.isEmpty() && stack
        .peek()
        .getType() != AiccPrerequisiteTokenType.LEFT_PAREN) {
      output.add(stack.pop());
    }
    // Pop the matching left parenthesis
    if (!stack.isEmpty() && stack
        .peek()
        .getType() == AiccPrerequisiteTokenType.LEFT_PAREN) {
      stack.pop();
    }
  }

  /**
   * Flushes the remaining tokens from the stack to the output queue at the end of the shunting-yard
   * algorithm, ignoring any remaining parentheses.
   *
   * @param output the output list of tokens in postfix notation
   * @param stack the operator stack
   */
  private static void flushStackToOutput(
      List<AiccPrerequisiteToken> output, Deque<AiccPrerequisiteToken> stack) {
    while (!stack.isEmpty()) {
      AiccPrerequisiteToken token = stack.pop();
      if (token.getType() != AiccPrerequisiteTokenType.LEFT_PAREN) {
        output.add(token);
      }
    }
  }

  /**
   * Determines the precedence of a given AICC prerequisite token. Returns 0 if the token is not an
   * operator. For tokens that are logical operators, precedence is returned based on the operator
   * type: "NOT" has the highest precedence, followed by "AND", and then "OR".
   *
   * @param token the token whose precedence is to be determined. Must not be null. If the token
   * type is not {@code AiccPrerequisiteTokenType.OPERATOR}, the result will be 0.
   * @return the precedence value of the token. Values are defined by constants:
   * {@code PRECEDENCE_NOT} for "NOT", {@code PRECEDENCE_AND} for "AND", {@code PRECEDENCE_OR} for
   * "OR". Returns 0 for non-operator tokens or unrecognized operators.
   */
  private static int precedence(AiccPrerequisiteToken token) {
    if (!token
        .getType()
        .equals(AiccPrerequisiteTokenType.OPERATOR)) {
      return 0;
    }
    String op = token
        .getValue()
        .toUpperCase(Locale.ROOT);
    return switch (op) {
      case "NOT" -> PRECEDENCE_NOT;
      case "AND" -> PRECEDENCE_AND;
      case "OR" -> PRECEDENCE_OR;
      default -> 0;
    };
  }

  /**
   * Constructs an abstract syntax tree (AST) from a list of prerequisite tokens in postfix
   * notation. The AST represents the logical structure of the prerequisite expression, with nodes
   * corresponding to operators and leaves corresponding to identifiers.
   *
   * @param postfixTokens a list of {@code AiccPrerequisiteToken} objects in postfix notation. Each
   * token represents a component of the prerequisite expression, such as an identifier or logical
   * operator.
   * @return the root node of the constructed {@code AiccPrerequisiteNode} AST, or {@code null} if
   * the input is empty, invalid, or cannot form a valid expression tree.
   */
  private static AiccPrerequisiteNode buildAst(List<AiccPrerequisiteToken> postfixTokens) {
    if (postfixTokens.isEmpty()) {
      return null;
    }
    Deque<AiccPrerequisiteNode> stack = new ArrayDeque<>();
    for (AiccPrerequisiteToken token : postfixTokens) {
      if (token.getType() != AiccPrerequisiteTokenType.OPERATOR) {
        stack.push(AiccPrerequisiteNode.leaf(token.getValue()));
        continue;
      }

      String opValue = token
          .getValue()
          .toUpperCase(Locale.ROOT);
      AiccPrerequisiteNodeType type;
      try {
        type = AiccPrerequisiteNodeType.valueOf(opValue);
      } catch (IllegalArgumentException e) {
        // Unknown operator
        return null;
      }

      switch (type) {
        case NOT -> {
          if (stack.isEmpty()) {
            return null;
          }
          AiccPrerequisiteNode child = stack.pop();
          stack.push(new AiccPrerequisiteNode(type, opValue, List.of(child)));
        }
        case AND, OR -> {
          if (stack.size() < 2) {
            return null;
          }
          AiccPrerequisiteNode right = stack.pop();
          AiccPrerequisiteNode left = stack.pop();
          stack.push(new AiccPrerequisiteNode(type, opValue, List.of(left, right)));
        }
        default -> {
          // All operators should be handled. This case is for unexpected enum values.
          return null;
        }
      }
    }

    if (stack.size() != 1) {
      // A valid expression must resolve to a single root node.
      return null;
    }
    return stack.pop();
  }

  /**
   * Collects and returns a list of unique identifier values from a list of
   * {@code AiccPrerequisiteToken} objects, based on the specified criteria. If {@code optionalOnly}
   * is {@code true}, only optional identifiers are included in the result. Otherwise, all
   * identifier tokens are collected.
   *
   * @param tokens the list of {@code AiccPrerequisiteToken} objects to process. Each token
   * represents a component of a prerequisite expression. The method filters this list to include
   * only identifier tokens that meet the criteria defined by {@code optionalOnly}.
   * @param optionalOnly a boolean flag that determines whether only optional identifiers are
   * collected. If {@code true}, only tokens marked as optional are included. If {@code false}, all
   * identifier tokens are collected, regardless of their optional status.
   * @return a list of unique identifier strings collected from the input tokens. The order of
   * identifiers in the returned list corresponds to their first occurrence in the input list,
   * preserving insertion order.
   */
  private static List<String> collectIdentifiers(List<AiccPrerequisiteToken> tokens,
      boolean optionalOnly) {
    Set<String> ids = new LinkedHashSet<>();
    for (AiccPrerequisiteToken token : tokens) {
      if (token.getType() != AiccPrerequisiteTokenType.IDENTIFIER
          || (optionalOnly && !token.isOptional())) {
        continue;
      }
      // include optional identifiers when collecting all referenced IDs
      ids.add(token.getValue());
    }
    return new ArrayList<>(ids);
  }
}
