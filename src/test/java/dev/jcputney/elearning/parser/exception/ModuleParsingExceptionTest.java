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
