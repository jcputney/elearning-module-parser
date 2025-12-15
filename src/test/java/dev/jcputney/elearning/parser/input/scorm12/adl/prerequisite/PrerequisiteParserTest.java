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

package dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link PrerequisiteParser} class.
 */
class PrerequisiteParserTest {

  /**
   * Tests parsing a simple item reference.
   */
  @Test
  void parseSimpleItemReference() {
    PrerequisiteExpression result = PrerequisiteParser.parse("ITEM-001");

    assertInstanceOf(ItemReference.class, result);
    assertEquals("ITEM-001", ((ItemReference) result).identifier());
  }

  /**
   * Tests parsing an item reference with underscores.
   */
  @Test
  void parseItemReferenceWithUnderscores() {
    PrerequisiteExpression result = PrerequisiteParser.parse("my_item_123");

    assertInstanceOf(ItemReference.class, result);
    assertEquals("my_item_123", ((ItemReference) result).identifier());
  }

  /**
   * Tests parsing an item reference with periods.
   */
  @Test
  void parseItemReferenceWithPeriods() {
    PrerequisiteExpression result = PrerequisiteParser.parse("item.1.2.3");

    assertInstanceOf(ItemReference.class, result);
    assertEquals("item.1.2.3", ((ItemReference) result).identifier());
  }

  /**
   * Tests parsing a simple AND expression.
   */
  @Test
  void parseSimpleAnd() {
    PrerequisiteExpression result = PrerequisiteParser.parse("ITEM-001 & ITEM-002");

    assertInstanceOf(AndExpression.class, result);
    AndExpression and = (AndExpression) result;
    assertEquals(2, and.operands().size());
    assertInstanceOf(ItemReference.class, and.operands().get(0));
    assertInstanceOf(ItemReference.class, and.operands().get(1));
    assertEquals("ITEM-001", ((ItemReference) and.operands().get(0)).identifier());
    assertEquals("ITEM-002", ((ItemReference) and.operands().get(1)).identifier());
  }

  /**
   * Tests parsing a multi-operand AND expression.
   */
  @Test
  void parseMultiOperandAnd() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A & B & C & D");

    assertInstanceOf(AndExpression.class, result);
    AndExpression and = (AndExpression) result;
    assertEquals(4, and.operands().size());
  }

  /**
   * Tests parsing a simple OR expression.
   */
  @Test
  void parseSimpleOr() {
    PrerequisiteExpression result = PrerequisiteParser.parse("ITEM-001 | ITEM-002");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertEquals(2, or.operands().size());
    assertInstanceOf(ItemReference.class, or.operands().get(0));
    assertInstanceOf(ItemReference.class, or.operands().get(1));
  }

  /**
   * Tests parsing a multi-operand OR expression.
   */
  @Test
  void parseMultiOperandOr() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A | B | C | D");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertEquals(4, or.operands().size());
  }

  /**
   * Tests parsing a NOT expression.
   */
  @Test
  void parseNot() {
    PrerequisiteExpression result = PrerequisiteParser.parse("~ITEM-001");

    assertInstanceOf(NotExpression.class, result);
    NotExpression not = (NotExpression) result;
    assertInstanceOf(ItemReference.class, not.operand());
    assertEquals("ITEM-001", ((ItemReference) not.operand()).identifier());
  }

  /**
   * Tests parsing a double NOT expression.
   */
  @Test
  void parseDoubleNot() {
    PrerequisiteExpression result = PrerequisiteParser.parse("~~ITEM-001");

    assertInstanceOf(NotExpression.class, result);
    NotExpression outer = (NotExpression) result;
    assertInstanceOf(NotExpression.class, outer.operand());
    NotExpression inner = (NotExpression) outer.operand();
    assertInstanceOf(ItemReference.class, inner.operand());
  }

  /**
   * Tests that AND has higher precedence than OR.
   * Expression: A | B & C should parse as A | (B & C)
   */
  @Test
  void parseAndHasHigherPrecedenceThanOr() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A | B & C");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertEquals(2, or.operands().size());
    assertInstanceOf(ItemReference.class, or.operands().get(0));
    assertInstanceOf(AndExpression.class, or.operands().get(1));

    AndExpression and = (AndExpression) or.operands().get(1);
    assertEquals(2, and.operands().size());
  }

  /**
   * Tests that NOT has higher precedence than AND.
   * Expression: ~A & B should parse as (~A) & B
   */
  @Test
  void parseNotHasHigherPrecedenceThanAnd() {
    PrerequisiteExpression result = PrerequisiteParser.parse("~A & B");

    assertInstanceOf(AndExpression.class, result);
    AndExpression and = (AndExpression) result;
    assertEquals(2, and.operands().size());
    assertInstanceOf(NotExpression.class, and.operands().get(0));
    assertInstanceOf(ItemReference.class, and.operands().get(1));
  }

  /**
   * Tests parsing a parenthesized expression.
   */
  @Test
  void parseParenthesizedExpression() {
    PrerequisiteExpression result = PrerequisiteParser.parse("(A & B)");

    assertInstanceOf(AndExpression.class, result);
  }

  /**
   * Tests that parentheses can override precedence.
   * Expression: (A | B) & C should parse as (A | B) & C
   */
  @Test
  void parseParenthesesOverridePrecedence() {
    PrerequisiteExpression result = PrerequisiteParser.parse("(A | B) & C");

    assertInstanceOf(AndExpression.class, result);
    AndExpression and = (AndExpression) result;
    assertEquals(2, and.operands().size());
    assertInstanceOf(OrExpression.class, and.operands().get(0));
    assertInstanceOf(ItemReference.class, and.operands().get(1));
  }

  /**
   * Tests parsing nested parentheses.
   */
  @Test
  void parseNestedParentheses() {
    PrerequisiteExpression result = PrerequisiteParser.parse("((A & B) | C)");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertInstanceOf(AndExpression.class, or.operands().get(0));
    assertInstanceOf(ItemReference.class, or.operands().get(1));
  }

  /**
   * Tests parsing a complex expression with mixed operators and parentheses.
   */
  @Test
  void parseComplexExpression() {
    PrerequisiteExpression result = PrerequisiteParser.parse("(A & B) | (C & ~D)");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertEquals(2, or.operands().size());

    // First operand: (A & B)
    assertInstanceOf(AndExpression.class, or.operands().get(0));
    AndExpression and1 = (AndExpression) or.operands().get(0);
    assertEquals(2, and1.operands().size());

    // Second operand: (C & ~D)
    assertInstanceOf(AndExpression.class, or.operands().get(1));
    AndExpression and2 = (AndExpression) or.operands().get(1);
    assertEquals(2, and2.operands().size());
    assertInstanceOf(NotExpression.class, and2.operands().get(1));
  }

  /**
   * Tests parsing expressions with extra whitespace.
   */
  @Test
  void parseExpressionWithExtraWhitespace() {
    PrerequisiteExpression result = PrerequisiteParser.parse("  A  &  B  |  C  ");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertEquals(2, or.operands().size());
  }

  /**
   * Tests parsing expressions without whitespace around operators.
   */
  @Test
  void parseExpressionWithoutWhitespace() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A&B|C");

    assertInstanceOf(OrExpression.class, result);
    OrExpression or = (OrExpression) result;
    assertEquals(2, or.operands().size());
  }

  // Error handling tests

  /**
   * Tests that null or empty input returns a ParseError.
   */
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "\t", "\n"})
  void parseNullOrEmptyReturnsParseError(String input) {
    PrerequisiteExpression result = PrerequisiteParser.parse(input);

    assertInstanceOf(ParseError.class, result);
    ParseError error = (ParseError) result;
    assertNotNull(error.errorMessage());
  }

  /**
   * Tests that missing closing parenthesis returns a ParseError.
   */
  @Test
  void parseMissingClosingParenthesisReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("(A & B");

    assertInstanceOf(ParseError.class, result);
    ParseError error = (ParseError) result;
    assertEquals("Missing closing parenthesis", error.errorMessage());
  }

  /**
   * Tests that missing operand after AND returns a ParseError.
   */
  @Test
  void parseMissingOperandAfterAndReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A &");

    assertInstanceOf(ParseError.class, result);
  }

  /**
   * Tests that missing operand after OR returns a ParseError.
   */
  @Test
  void parseMissingOperandAfterOrReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A |");

    assertInstanceOf(ParseError.class, result);
  }

  /**
   * Tests that missing operand after NOT returns a ParseError.
   */
  @Test
  void parseMissingOperandAfterNotReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("~");

    assertInstanceOf(ParseError.class, result);
  }

  /**
   * Tests that invalid characters return a ParseError.
   */
  @Test
  void parseInvalidCharacterReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A @ B");

    assertInstanceOf(ParseError.class, result);
    ParseError error = (ParseError) result;
    assertNotNull(error.errorMessage());
  }

  /**
   * Tests that trailing content after a valid expression returns a ParseError.
   */
  @Test
  void parseTrailingContentReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("A & B )");

    assertInstanceOf(ParseError.class, result);
    ParseError error = (ParseError) result;
    assertNotNull(error.errorMessage());
  }

  /**
   * Tests that empty parentheses return a ParseError.
   */
  @Test
  void parseEmptyParenthesesReturnsParseError() {
    PrerequisiteExpression result = PrerequisiteParser.parse("()");

    assertInstanceOf(ParseError.class, result);
  }

  /**
   * Tests the toString() method of parsed expressions.
   */
  @Test
  void testToString() {
    PrerequisiteExpression and = PrerequisiteParser.parse("A & B");
    assertNotNull(and.toString());

    PrerequisiteExpression or = PrerequisiteParser.parse("A | B");
    assertNotNull(or.toString());

    PrerequisiteExpression not = PrerequisiteParser.parse("~A");
    assertNotNull(not.toString());

    PrerequisiteExpression item = PrerequisiteParser.parse("A");
    assertNotNull(item.toString());

    PrerequisiteExpression error = PrerequisiteParser.parse("");
    assertNotNull(error.toString());
  }
}
