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

package dev.jcputney.elearning.parser.input.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for PercentType, ensuring the value is within the range of 0 to 1.
 */
public class PercentTypeDeserializer extends JsonDeserializer<PercentType> {

  @Override
  public PercentType deserialize(JsonParser p, DeserializationContext context)
      throws IOException {
    String value = p.getText();
    try {
      return new PercentType(new BigDecimal(value));
    } catch (IllegalArgumentException e) {
      throw new IOException("Invalid PercentType value: " + value, e);
    }
  }
}