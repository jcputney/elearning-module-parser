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
package dev.jcputney.elearning.parser.exception;

import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ModuleParsingExceptionTest {

    @Test
    void testExceptionFromValidationResult() {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CODE1", "First error", "location1"),
            ValidationIssue.error("CODE2", "Second error", "location2")
        );

        ModuleParsingException ex = new ModuleParsingException("Parse failed", result);

        assertThat(ex.getMessage()).contains("Parse failed");
        assertThat(ex.getMessage()).contains("2 error(s) found");
        assertThat(ex.getMessage()).contains("CODE1");
        assertThat(ex.getMessage()).contains("CODE2");
        assertThat(ex.getValidationResult()).isEqualTo(result);
    }

    @Test
    void testExceptionViaToException() {
        ValidationResult result = ValidationResult.of(
            ValidationIssue.error("CODE", "Error message", "location")
        );

        ModuleParsingException ex = result.toException("Context message");

        assertThat(ex.getMessage()).contains("Context message");
        assertThat(ex.getValidationResult()).isEqualTo(result);
    }
}
