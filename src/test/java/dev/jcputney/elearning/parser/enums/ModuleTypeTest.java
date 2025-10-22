package dev.jcputney.elearning.parser.enums;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ModuleTypeTest {

  @Test
  void shouldContainXapiType() {
    assertThat(ModuleType.XAPI).isNotNull();
  }

  @Test
  void shouldHaveAllExpectedTypes() {
    assertThat(ModuleType.values()).containsExactlyInAnyOrder(
        ModuleType.SCORM_12,
        ModuleType.SCORM_2004,
        ModuleType.AICC,
        ModuleType.CMI5,
        ModuleType.XAPI
    );
  }
}
