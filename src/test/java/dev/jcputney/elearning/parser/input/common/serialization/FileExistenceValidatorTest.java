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

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.config.FileExistenceValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FileExistenceValidator}.
 */
class FileExistenceValidatorTest {

  @AfterEach
  void cleanup() {
    // Clear any system properties set during tests
    System.clearProperty("elearning.parser.validateFileExists");
  }

  @Test
  void isEnabled_defaultsToFalse_whenNoConfigurationSet() {
    assertThat(FileExistenceValidator.isEnabled()).isFalse();
  }

  @Test
  void isEnabled_returnsTrue_whenSystemPropertySet() {
    System.setProperty("elearning.parser.validateFileExists", "true");
    assertThat(FileExistenceValidator.isEnabled()).isTrue();
  }

  @Test
  void isEnabled_returnsFalse_whenSystemPropertySetToFalse() {
    System.setProperty("elearning.parser.validateFileExists", "false");
    assertThat(FileExistenceValidator.isEnabled()).isFalse();
  }
}
