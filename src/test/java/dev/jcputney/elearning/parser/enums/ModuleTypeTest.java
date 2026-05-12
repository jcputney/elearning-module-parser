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
