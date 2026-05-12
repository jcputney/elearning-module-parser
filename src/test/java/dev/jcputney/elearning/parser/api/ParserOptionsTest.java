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

package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.util.XmlParsingUtils;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ParserOptionsTest {

    @Test
    void testDefaultIsStrict() {
        ParserOptions options = new ParserOptions();

        assertThat(options.isStrictMode()).isTrue();
    }

    @Test
    void testSetStrictMode() {
        ParserOptions options = new ParserOptions()
            .setStrictMode(false);

        assertThat(options.isStrictMode()).isFalse();
    }

    @Test
    void testStrictFactory() {
        ParserOptions options = ParserOptions.strict();

        assertThat(options.isStrictMode()).isTrue();
    }

    @Test
    void testLenientFactory() {
        ParserOptions options = ParserOptions.lenient();

        assertThat(options.isStrictMode()).isFalse();
    }

    @Test
    void testFluentChaining() {
        ParserOptions options = new ParserOptions()
            .setStrictMode(false)
            .setStrictMode(true);

        assertThat(options.isStrictMode()).isTrue();
    }

    @Test
    void testMaxManifestSizeDefaultIsNull() {
        ParserOptions options = new ParserOptions();
        assertThat(options.getMaxManifestSize()).isNull();
    }

    @Test
    void testMaxManifestSizeExplicitValue() {
        ParserOptions options = new ParserOptions()
            .setMaxManifestSize(10_000_000L);

        assertThat(options.getMaxManifestSize()).isEqualTo(10_000_000L);
        assertThat(options.getResolvedMaxManifestSize()).isEqualTo(10_000_000L);
    }

    @Test
    void testMaxManifestSizeNullFallsBackToSystemDefault() {
        ParserOptions options = new ParserOptions();

        assertThat(options.getResolvedMaxManifestSize())
            .isEqualTo(XmlParsingUtils.getMaxXmlSize());
    }

    @Test
    void testMaxManifestSizeChaining() {
        ParserOptions options = new ParserOptions()
            .setStrictMode(false)
            .setMaxManifestSize(5_000_000L);

        assertThat(options.isStrictMode()).isFalse();
        assertThat(options.getResolvedMaxManifestSize()).isEqualTo(5_000_000L);
    }
}
