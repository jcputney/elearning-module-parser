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

package dev.jcputney.elearning.parser.validators.rules;

import dev.jcputney.elearning.parser.validation.ValidationResult;

/**
 * Interface for individual validation rules that can be composed together.
 * Each rule encapsulates a single validation concern with spec traceability.
 *
 * <p>Rules are designed to be:</p>
 * <ul>
 *   <li>Testable - each rule can be tested independently</li>
 *   <li>Reusable - common rules can be shared across validators</li>
 *   <li>Traceable - each rule references the specification it enforces</li>
 *   <li>Composable - rules are combined in validators via merge()</li>
 * </ul>
 *
 * @param <T> The manifest type this rule validates
 */
public interface ValidationRule<T> {
  /**
   * Validates the manifest according to this rule.
   *
   * @param manifest The manifest to validate (must not be null)
   * @return ValidationResult with any issues found (never null)
   * @throws IllegalArgumentException if manifest is null
   */
  ValidationResult validate(T manifest);

  /**
   * Human-readable name of this rule for logging and debugging.
   *
   * @return Rule name (e.g., "Resource Reference Validation")
   */
  String getRuleName();

  /**
   * Specification reference for traceability and documentation.
   * Used to trace validation back to spec requirements.
   *
   * @return Spec reference (e.g., "SCORM 1.2 CAM Section 2.3.4")
   */
  String getSpecReference();
}
