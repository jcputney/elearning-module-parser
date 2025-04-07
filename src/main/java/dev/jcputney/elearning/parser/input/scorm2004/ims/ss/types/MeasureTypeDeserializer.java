/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for MeasureType, ensuring the value is within the range of -1 to 1 and has at
 * least four decimal digits.
 */
public class MeasureTypeDeserializer extends JsonDeserializer<MeasureType> {

  /**
   * Default constructor for the MeasureTypeDeserializer class.
   */
  @SuppressWarnings("unused")
  public MeasureTypeDeserializer() {
    // Default constructor
  }

  /**
   * Deserialize a JSON string into a MeasureType object.
   *
   * @param p the JsonParser
   * @param context the DeserializationContext
   * @return a MeasureType object
   * @throws IOException if the value is not valid
   */
  @Override
  public MeasureType deserialize(JsonParser p, DeserializationContext context)
      throws IOException {
    String value = p.getText();
    try {
      return new MeasureType(new BigDecimal(value));
    } catch (IllegalArgumentException e) {
      throw new IOException("Invalid MeasureType value: " + value, e);
    }
  }
}