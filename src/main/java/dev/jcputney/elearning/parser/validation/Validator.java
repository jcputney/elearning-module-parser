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

package dev.jcputney.elearning.parser.validation;

/**
 * Functional interface for validators that check module manifests for issues.
 * Validators should be stateless and return immutable ValidationResult objects.
 *
 * @param <T> The type of object being validated (e.g., ScormManifest, AiccCourse)
 */
@FunctionalInterface
public interface Validator<T> {
    /**
     * Validates the target object and returns any issues found.
     *
     * @param target The object to validate
     * @return ValidationResult containing any errors or warnings
     */
    ValidationResult validate(T target);
}
