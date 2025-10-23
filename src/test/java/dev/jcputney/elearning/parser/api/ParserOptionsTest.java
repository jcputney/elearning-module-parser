package dev.jcputney.elearning.parser.api;

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
}
