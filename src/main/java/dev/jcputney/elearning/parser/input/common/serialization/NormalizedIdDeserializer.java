/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.input.common.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

/**
 * Deserializer that normalizes XML identifier values ({@code xsd:ID} / {@code xsd:IDREF}) the way a
 * validating XML processor does for tokenized attribute types: leading and trailing whitespace is
 * stripped and every internal run of whitespace is collapsed to a single space.
 * <p>
 * A non-validating parser only applies CDATA attribute-value normalization (tab/CR/LF become a
 * single space, but sequences are neither collapsed nor trimmed), so a manifest authored with
 * whitespace-padded identifiers — e.g. {@code identifier="   CASETEST   "} referenced by
 * {@code default="CASETEST"} — would otherwise fail to resolve here even though it is conformant.
 * The ADL SCORM 2004 CTS ships exactly such packages (CM-07e, OB-02a, OB-02b) to verify this
 * handling.
 * </p>
 * <p>
 * Apply this <strong>only</strong> to attributes that are XML ID/IDREF identifiers used for identity
 * and reference resolution. Do not apply it to free-text values (titles, hrefs, parameters), whose
 * surrounding whitespace may be significant.
 * </p>
 */
public class NormalizedIdDeserializer extends JsonDeserializer<String> {

  /**
   * Default constructor for the deserializer.
   */
  public NormalizedIdDeserializer() {
    // Default constructor
  }

  /**
   * Normalizes an identifier string per XML tokenized-type rules.
   *
   * @param value the raw identifier value (may be {@code null})
   * @return the value with leading/trailing whitespace stripped and internal whitespace runs
   * collapsed to a single space, or {@code null} if the input was {@code null}
   */
  public static String normalize(String value) {
    if (value == null) {
      return null;
    }
    return value
        .replaceAll("\\s+", " ")
        .trim();
  }

  /**
   * Deserializes a JSON/XML attribute value into a normalized identifier string.
   *
   * @param parser the JsonParser to read the value from
   * @param context context for the deserialization process
   * @return the normalized identifier value
   * @throws IOException if there's an issue reading from the parser
   * @throws IllegalArgumentException if the parser is null
   */
  @Override
  public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    if (parser == null) {
      throw new IllegalArgumentException("JsonParser cannot be null");
    }
    return normalize(parser.getValueAsString());
  }
}
