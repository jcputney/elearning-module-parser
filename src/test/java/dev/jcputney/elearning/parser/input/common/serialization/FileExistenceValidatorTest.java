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
