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

package dev.jcputney.elearning.parser.validation;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ValidationIssueTest {

    @Test
    void testCreateErrorIssue() {
        ValidationIssue issue = ValidationIssue.error(
            "TEST_CODE",
            "Test message",
            "test/location"
        );

        assertThat(issue.severity()).isEqualTo(ValidationIssue.Severity.ERROR);
        assertThat(issue.code()).isEqualTo("TEST_CODE");
        assertThat(issue.message()).isEqualTo("Test message");
        assertThat(issue.location()).isEqualTo("test/location");
        assertThat(issue.suggestedFix()).isNull();
    }

    @Test
    void testCreateWarningIssue() {
        ValidationIssue issue = ValidationIssue.warning(
            "WARN_CODE",
            "Warning message",
            "warn/location"
        );

        assertThat(issue.severity()).isEqualTo(ValidationIssue.Severity.WARNING);
    }

    @Test
    void testCreateIssueWithSuggestion() {
        ValidationIssue issue = new ValidationIssue(
            ValidationIssue.Severity.ERROR,
            "CODE",
            "message",
            "location",
            "Try this fix"
        );

        assertThat(issue.suggestedFix()).isEqualTo("Try this fix");
    }
}
