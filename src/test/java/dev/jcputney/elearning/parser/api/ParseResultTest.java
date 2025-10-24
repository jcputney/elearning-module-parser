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

package dev.jcputney.elearning.parser.api;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ParseResultTest {

  @Test
  void isValid_withNoErrors_returnsTrue() {
    ValidationResult validation = ValidationResult.valid();
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void isValid_withErrors_returnsFalse() {
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.error("CODE", "message", "location")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.isValid()).isFalse();
  }

  @Test
  void hasErrors_withErrors_returnsTrue() {
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.error("CODE", "message", "location")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.hasErrors()).isTrue();
  }

  @Test
  void hasWarnings_withWarnings_returnsTrue() {
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.warning("CODE", "message", "location")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.hasWarnings()).isTrue();
  }

  @Test
  void accessors_returnCorrectValues() {
    ValidationResult validation = ValidationResult.valid();
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.validation()).isEqualTo(validation);
    assertThat(result.metadata()).isEqualTo(metadata);
  }

  @Test
  void hasErrors_withNoErrors_returnsFalse() {
    ValidationResult validation = ValidationResult.valid();
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void hasWarnings_withNoWarnings_returnsFalse() {
    ValidationResult validation = ValidationResult.valid();
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.hasWarnings()).isFalse();
  }

  @Test
  void isValid_withWarningsButNoErrors_returnsTrue() {
    // Critical test: warnings don't invalidate a module
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.warning("WARN1", "warning message", "location")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasWarnings()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void isValid_withMultipleErrors_returnsFalse() {
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.error("ERR1", "first error", "location1"),
        ValidationIssue.error("ERR2", "second error", "location2"),
        ValidationIssue.error("ERR3", "third error", "location3")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
  }

  @Test
  void hasWarnings_withMultipleWarnings_returnsTrue() {
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.warning("WARN1", "first warning", "location1"),
        ValidationIssue.warning("WARN2", "second warning", "location2")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.hasWarnings()).isTrue();
    assertThat(result.hasErrors()).isFalse();
    assertThat(result.isValid()).isTrue();
  }

  @Test
  void isValid_withBothErrorsAndWarnings_returnsFalse() {
    ValidationResult validation = ValidationResult.of(
        ValidationIssue.error("ERR1", "error message", "location1"),
        ValidationIssue.warning("WARN1", "warning message", "location2"),
        ValidationIssue.warning("WARN2", "another warning", "location3")
    );
    @SuppressWarnings("unchecked")
    ModuleMetadata<PackageManifest> metadata = Mockito.mock(ModuleMetadata.class);

    ParseResult<PackageManifest> result = new ParseResult<>(validation, metadata);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.hasWarnings()).isTrue();
  }
}
