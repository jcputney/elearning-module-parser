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
package dev.jcputney.elearning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.jcputney.elearning.parser.api.ModuleParser;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ModuleParserFactoryTest {

  @Test
  void getScorm2004Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm2004.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm2004Parser.class, parser.getClass());
  }

  @Test
  void getScorm12Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm12.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm12Parser.class, parser.getClass());
  }

  @Test
  void getAiccParser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/aicc.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(AiccParser.class, parser.getClass());
  }

  @Test
  void getCmi5Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/cmi5.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Cmi5Parser.class, parser.getClass());
  }
}
