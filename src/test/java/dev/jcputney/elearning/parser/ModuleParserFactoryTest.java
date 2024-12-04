/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
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
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm2004Parser.class, parser.getClass());
  }

  @Test
  void getScorm12Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm12.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm12Parser.class, parser.getClass());
  }

  @Test
  void getAiccParser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/aicc.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(AiccParser.class, parser.getClass());
  }

  @Test
  void getCmi5Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/cmi5.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Cmi5Parser.class, parser.getClass());
  }

  @Test
  void parseModule() {
  }
}