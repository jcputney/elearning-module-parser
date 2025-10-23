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

import dev.jcputney.elearning.parser.config.FileExistenceValidator;
import dev.jcputney.elearning.parser.config.ModuleSizeCalculator;

/**
 * Configuration options for module parsing behavior.
 * Controls validation strictness and other parsing settings.
 */
public class ParserOptions {
    private boolean strictMode = true;

    /**
     * Creates parser options with default settings (strict mode enabled).
     */
    public ParserOptions() {}

    /**
     * Sets whether to use strict validation mode.
     * In strict mode, parsing fails on any validation errors.
     * In lenient mode, parsing continues despite errors.
     *
     * @param strict true for strict mode, false for lenient
     * @return this ParserOptions instance for method chaining
     */
    public ParserOptions setStrictMode(boolean strict) {
        this.strictMode = strict;
        return this;
    }

    /**
     * Checks if strict validation mode is enabled.
     *
     * @return true if strict mode is enabled
     */
    public boolean isStrictMode() {
        return strictMode;
    }

    /**
     * Creates parser options with strict mode enabled.
     * Parsing will fail on any validation errors.
     *
     * @return ParserOptions configured for strict mode
     */
    public static ParserOptions strict() {
        return new ParserOptions().setStrictMode(true);
    }

    /**
     * Creates parser options with lenient mode enabled.
     * Parsing will continue despite validation errors.
     *
     * @return ParserOptions configured for lenient mode
     */
    public static ParserOptions lenient() {
        return new ParserOptions().setStrictMode(false);
    }

    // ========== Backward compatibility methods ==========
    // These methods provide compatibility with the old ParserOptions API
    // for file existence validation and module size calculation

    /**
     * Creates default parser options.
     * Provides backward compatibility with old API.
     *
     * @return Default parser options
     */
    public static ParserOptions defaults() {
        return new ParserOptions();
    }

    /**
     * Whether to validate that all files referenced in the manifest actually exist.
     * This delegates to the FileExistenceValidator configuration.
     *
     * @return true if file existence validation is enabled
     */
    public boolean shouldValidateFileExists() {
        return FileExistenceValidator.isEnabled();
    }

    /**
     * Whether to calculate the total size of all files in the module.
     * This delegates to the ModuleSizeCalculator configuration.
     *
     * @return true if module size calculation is enabled
     */
    public boolean shouldCalculateModuleSize() {
        return ModuleSizeCalculator.isEnabled();
    }

    /**
     * Creates parser options optimized for batch processing.
     * This is equivalent to defaults() in the new API.
     *
     * @return Parser options for batch processing
     */
    public static ParserOptions forBatchProcessing() {
        return new ParserOptions();
    }

    /**
     * Creates a builder for ParserOptions.
     * Returns a builder that ignores old configuration options.
     *
     * @return Builder instance
     * @deprecated Use constructor or factory methods instead
     */
    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for backward compatibility.
     * Ignores old configuration options (calculateModuleSize, validateFileExists).
     *
     * @deprecated Use constructor or factory methods instead
     */
    @Deprecated
    public static class Builder {
        private Builder() {}

        /**
         * Ignored - kept for backward compatibility.
         * @param calculate ignored
         * @return this builder
         * @deprecated No longer used
         */
        @Deprecated
        public Builder calculateModuleSize(boolean calculate) {
            return this;
        }

        /**
         * Ignored - kept for backward compatibility.
         * @param validate ignored
         * @return this builder
         * @deprecated No longer used
         */
        @Deprecated
        public Builder validateFileExists(boolean validate) {
            return this;
        }

        /**
         * Builds the ParserOptions.
         * @return new ParserOptions instance
         */
        public ParserOptions build() {
            return new ParserOptions();
        }
    }
}
