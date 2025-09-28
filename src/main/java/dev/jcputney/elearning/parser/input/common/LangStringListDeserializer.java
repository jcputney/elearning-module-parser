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

package dev.jcputney.elearning.parser.input.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for lists of LangString objects to ensure that they are properly deserialized
 * from XML. This deserializer is used to prevent ClassCastException issues when deserializing
 * LangString objects from XML.
 */
public class LangStringListDeserializer extends JsonDeserializer<List<LangString>> {

  /**
   * Default constructor for the LangStringListDeserializer class.
   */
  public LangStringListDeserializer() {
    // no-op
  }

  /**
   * Deserializes JSON input into a list of LangString objects. The method processes JSON structures
   * such as single values, arrays, or objects containing specific node types (e.g., "string" or
   * "langstring") and converts them into the LangString data model.
   *
   * @param p the {@link JsonParser} used to read the JSON content
   * @param context the deserialization context
   * @return a list of LangString objects deserialized from the JSON input
   * @throws IOException if an I/O error occurs during the deserialization process
   */
  @Override
  public List<LangString> deserialize(JsonParser p, DeserializationContext context)
      throws IOException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);
    List<LangString> result = new ArrayList<>();

    if (node instanceof ObjectNode objectNode) {
      if (objectNode.has("string")) {
        node = objectNode.get("string");
      } else if (objectNode.has("langstring")) {
        node = objectNode.get("langstring");
      }
    }

    if (node instanceof ValueNode valueNode) {
      LangString langString = new LangString(valueNode
          .asText()
          .trim());
      result.add(langString);
    } else if (node.isArray()) {
      ArrayNode arrayNode = (ArrayNode) node;
      for (JsonNode elementNode : arrayNode) {
        LangString langString = mapper.treeToValue(elementNode, LangString.class);
        result.add(langString);
      }
    } else {
      // Handle a single element case
      LangString langString = mapper.treeToValue(node, LangString.class);
      result.add(langString);
    }

    return result;
  }
}