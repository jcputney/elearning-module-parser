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

package dev.jcputney.elearning.parser.util;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 * Utility class for parsing JSON objects and extracting fields and custom metadata.
 * <p>
 * This class provides methods to extract required and optional fields from a JSON object, as well
 * as custom metadata stored in the "customData" section of the JSON.
 * </p>
 */
public class JsonUtils {

  private JsonUtils() {
    // Utility class, it should not be instantiated
  }

  /**
   * Extracts a required field from the JSON object. Throws a ModuleParsingException if the field is
   * not found.
   *
   * @param jsonObject The parsed JSON object representing the manifest.
   * @param fieldName The name of the field to extract.
   * @return The value of the field as a string.
   * @throws ModuleParsingException If the field is missing or empty.
   */
  public static String extractRequiredField(JSONObject jsonObject, String fieldName)
      throws ModuleParsingException {
    if (jsonObject.has(fieldName) && !jsonObject.getString(fieldName).isEmpty()) {
      return jsonObject.getString(fieldName);
    }
    throw new ModuleParsingException("Missing required field: " + fieldName);
  }

  /**
   * Extracts a field from the JSON object and returns a default value if the field is missing.
   *
   * @param jsonObject The parsed JSON object representing the manifest.
   * @param fieldName The name of the field to extract.
   * @param defaultValue The default value to return if the field is not found.
   * @return The value of the field or the default value.
   */
  public static String extractField(JSONObject jsonObject, String fieldName, String defaultValue) {
    return jsonObject.optString(fieldName, defaultValue);
  }

  /**
   * Extracts a list of strings from the JSON array associated with the given field.
   *
   * @param jsonObject The parsed JSON object representing the manifest.
   * @param fieldName The name of the field representing a list.
   * @return A list of strings extracted from the JSON array.
   */
  public static List<String> extractListField(JSONObject jsonObject, String fieldName) {
    if (jsonObject.has(fieldName)) {
      return jsonObject.getJSONArray(fieldName).toList().stream()
          .map(Object::toString)
          .collect(Collectors.toList());
    }
    return List.of();  // Return an empty list if the field is not found
  }

  /**
   * Extracts custom metadata from the JSON object, returning key-value pairs from the "customData"
   * section.
   *
   * @param jsonObject The parsed JSON object representing the metadata.
   * @return A map of custom metadata.
   */
  public static Map<String, String> extractCustomData(JSONObject jsonObject) {
    Map<String, String> customData = Map.of();  // Default empty map
    if (jsonObject.has("customData")) {
      JSONObject customJson = jsonObject.getJSONObject("customData");
      customJson.keySet().forEach(key -> {
        customData.put(key, customJson.getString(key));
      });
    }
    return customData;
  }

}
