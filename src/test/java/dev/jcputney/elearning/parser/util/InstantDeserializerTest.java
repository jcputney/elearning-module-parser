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

package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link InstantDeserializer} to ensure proper parsing of ISO 8601 date-time strings into
 * {@link Instant} objects.
 */
class InstantDeserializerTest {

  private final InstantDeserializer deserializer = new InstantDeserializer();
  private final DeserializationContext context = null; // Not used in the deserializer

  /**
   * Tests that valid ISO 8601 date-time strings are parsed correctly.
   *
   * @param input the ISO 8601 date-time string
   * @throws IOException if parsing fails
   */
  @ParameterizedTest
  @ValueSource(strings = {
      "2023-05-01T10:15:30Z",
      "2023-05-01T10:15:30+00:00",
      "2023-05-01T10:15:30.123Z",
      "2023-05-01T10:15:30.123456789Z"
  })
  void deserialize_validIso8601_returnsInstant(String input) throws IOException {
    // Arrange
    JsonParser parser = new TestJsonParser(input);

    // Act
    Instant result = deserializer.deserialize(parser, context);

    // Assert
    assertEquals(Instant.parse(input), result);
  }

  /**
   * Tests that null or empty strings are parsed as Instant.EPOCH.
   *
   * @param input the null or empty input string
   * @throws IOException if parsing fails
   */
  @ParameterizedTest
  @NullAndEmptySource
  void deserialize_nullOrEmpty_returnsEpoch(String input) throws IOException {
    // Arrange
    JsonParser parser = new TestJsonParser(input);

    // Act
    Instant result = deserializer.deserialize(parser, context);

    // Assert
    assertEquals(Instant.EPOCH, result);
  }

  /**
   * Tests that invalid date-time formats throw an IOException.
   *
   * @param input the invalid format input string
   */
  @ParameterizedTest
  @ValueSource(strings = {
      "not-a-date",
      "2023-05-01",
      "10:15:30",
      "2023/05/01T10:15:30Z",
      "2023-05-01 10:15:30"
  })
  void deserialize_invalidFormat_throwsIOException(String input) {
    // Arrange
    JsonParser parser = new TestJsonParser(input);

    // Act & Assert
    assertThrows(IOException.class, () -> deserializer.deserialize(parser, context));
  }

  /**
   * Tests that a null parser throws an IllegalArgumentException.
   */
  @Test
  void deserialize_nullParser_throwsIllegalArgumentException() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(null, context));
  }

  /**
   * A simple implementation of JsonParser for testing purposes.
   */
  private static class TestJsonParser extends JsonParser {

    private final String text;

    public TestJsonParser(String text) {
      this.text = text;
    }

    @Override
    public String getText() throws IOException {
      return text;
    }

    @Override
    public ObjectCodec getCodec() {
      return null;
    }

    @Override
    public void setCodec(ObjectCodec c) {
      // Do nothing
    }

    @Override
    public Version version() {
      return Version.unknownVersion();
    }

    @Override
    public JsonStreamContext getParsingContext() {
      return null;
    }

    @Override
    public JsonLocation getCurrentLocation() {
      return JsonLocation.NA;
    }

    // Stub implementations of abstract methods
    @Override
    public JsonToken nextToken() throws IOException {
      return null;
    }

    @Override
    public JsonToken getCurrentToken() {
      return null;
    }

    @Override
    public void clearCurrentToken() {
    }

    @Override
    public JsonToken getLastClearedToken() {
      return null;
    }

    @Override
    public boolean hasCurrentToken() {
      return false;
    }

    @Override
    public boolean hasToken(JsonToken t) {
      return false;
    }

    @Override
    public boolean hasTokenId(int id) {
      return false;
    }

    @Override
    public int getCurrentTokenId() {
      return 0;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public boolean isClosed() {
      return false;
    }

    @Override
    public JsonParser skipChildren() throws IOException {
      return null;
    }

    @Override
    public String getCurrentName() throws IOException {
      return null;
    }

    @Override
    public void overrideCurrentName(String name) {
    }

    @Override
    public String getValueAsString() throws IOException {
      return null;
    }

    @Override
    public String getValueAsString(String defaultValue) throws IOException {
      return null;
    }

    @Override
    public int getValueAsInt() throws IOException {
      return 0;
    }

    @Override
    public int getValueAsInt(int defaultValue) throws IOException {
      return 0;
    }

    @Override
    public long getValueAsLong() throws IOException {
      return 0;
    }

    @Override
    public long getValueAsLong(long defaultValue) throws IOException {
      return 0;
    }

    @Override
    public double getValueAsDouble() throws IOException {
      return 0;
    }

    @Override
    public double getValueAsDouble(double defaultValue) throws IOException {
      return 0;
    }

    @Override
    public boolean getValueAsBoolean() throws IOException {
      return false;
    }

    @Override
    public boolean getValueAsBoolean(boolean defaultValue) throws IOException {
      return false;
    }

    @Override
    public Object getEmbeddedObject() throws IOException {
      return null;
    }

    @Override
    public byte[] getBinaryValue() throws IOException {
      return new byte[0];
    }

    @Override
    public int getTextLength() throws IOException {
      return 0;
    }

    @Override
    public int getTextOffset() throws IOException {
      return 0;
    }

    @Override
    public char[] getTextCharacters() throws IOException {
      return new char[0];
    }

    @Override
    public boolean hasTextCharacters() {
      return false;
    }

    @Override
    public Number getNumberValue() throws IOException {
      return null;
    }

    @Override
    public NumberType getNumberType() throws IOException {
      return null;
    }

    @Override
    public int getIntValue() throws IOException {
      return 0;
    }

    @Override
    public long getLongValue() throws IOException {
      return 0;
    }

    @Override
    public java.math.BigInteger getBigIntegerValue() throws IOException {
      return null;
    }

    @Override
    public float getFloatValue() throws IOException {
      return 0;
    }

    @Override
    public double getDoubleValue() throws IOException {
      return 0;
    }

    @Override
    public java.math.BigDecimal getDecimalValue() throws IOException {
      return null;
    }

    @Override
    public Object getObjectId() throws IOException {
      return null;
    }

    @Override
    public Object getTypeId() throws IOException {
      return null;
    }

    @Override
    public byte[] getBinaryValue(Base64Variant bv) throws IOException {
      return new byte[0];
    }

    @Override
    public JsonLocation getTokenLocation() {
      return JsonLocation.NA;
    }

    @Override
    public JsonToken nextValue() throws IOException {
      return nextToken();
    }
  }
}
