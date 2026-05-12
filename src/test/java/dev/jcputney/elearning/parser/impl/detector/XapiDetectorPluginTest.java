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

package dev.jcputney.elearning.parser.impl.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class XapiDetectorPluginTest {

  @Mock
  private FileAccess mockFileAccess;

  private XapiDetectorPlugin detector;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    detector = new XapiDetectorPlugin();
  }

  @Test
  void shouldDetectXapiWithTincanXml() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("tincan.xml", "index.html"));

    assertThat(detector.detect(mockFileAccess)).isEqualTo(ModuleType.XAPI);
  }

  @Test
  void shouldReturnNullWhenNoTincanXml() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("index.html"));

    assertThat(detector.detect(mockFileAccess)).isNull();
  }

  @Test
  void shouldReturnNullForEmptyDirectory() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of());

    assertThat(detector.detect(mockFileAccess)).isNull();
  }

  @Test
  void shouldDetectXapiWithDifferentCasing() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("TINCAN.XML", "index.html"));

    assertThat(detector.detect(mockFileAccess)).isEqualTo(ModuleType.XAPI);
  }

  @Test
  void shouldHaveCorrectPriority() {
    assertThat(detector.getPriority()).isEqualTo(40);
  }
}
