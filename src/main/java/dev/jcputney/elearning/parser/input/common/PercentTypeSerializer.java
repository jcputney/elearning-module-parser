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

package dev.jcputney.elearning.parser.input.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * A custom serializer for the {@link PercentType} class, enabling its conversion to a JSON string
 * representation during serialization.
 * <p>
 * This serializer ensures that the {@code PercentType} object is serialized as the string
 * representation of its value, while handling null values gracefully.
 * <p>
 * The intended use is to handle serialization of percentage values represented as decimals between
 * 0 and 1 within a JSON structure.
 */
public class PercentTypeSerializer extends JsonSerializer<PercentType> {

  /**
   * Serializes a {@link PercentType} object into its JSON string representation. Writes the value
   * as a string if it is non-null; otherwise, writes a JSON null.
   *
   * @param value the {@code PercentType} object to serialize; may be null
   * @param gen the {@code JsonGenerator} used to generate the JSON output
   * @param serializers the {@code SerializerProvider} that can provide serializers for serializing
   * objects
   * @throws IOException if an error occurs during JSON serialization
   */
  @Override
  public void serialize(PercentType value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value != null) {
      gen.writeString(value
          .value()
          .toString());
    } else {
      gen.writeNull();
    }
  }
}
