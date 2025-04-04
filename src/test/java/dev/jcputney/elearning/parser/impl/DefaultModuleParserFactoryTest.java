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

package dev.jcputney.elearning.parser.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.ModuleParser;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleTypeDetector;
import dev.jcputney.elearning.parser.api.ModuleTypeDetectorPlugin;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the DefaultModuleParserFactory class.
 */
class DefaultModuleParserFactoryTest {

  private MockFileAccess fileAccess;
  private MockModuleTypeDetector moduleTypeDetector;
  private DefaultModuleParserFactory factory;

  @BeforeEach
  void setUp() {
    fileAccess = new MockFileAccess();
    moduleTypeDetector = new MockModuleTypeDetector();
    factory = new DefaultModuleParserFactory(fileAccess, moduleTypeDetector);
  }

  @Test
  void constructor_withNullFileAccess_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> new DefaultModuleParserFactory(null, moduleTypeDetector));
  }

  @Test
  void constructor_withNullModuleTypeDetector_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> new DefaultModuleParserFactory(fileAccess, null));
  }

  @Test
  void constructor_withFileAccessOnly_createsDefaultModuleTypeDetector() {
    DefaultModuleParserFactory factory = new DefaultModuleParserFactory(fileAccess);
    assertNotNull(factory);
  }

  @Test
  void registerParser_withNullModuleType_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.registerParser(null, (fa) -> new MockModuleParser()));
  }

  @Test
  void registerParser_withNullParserFactory_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> factory.registerParser(ModuleType.SCORM_12, null));
  }

  @Test
  void registerParser_validParameters_registersParser() {
    Function<FileAccess, ModuleParser<?>> parserFactory = (fa) -> new MockModuleParser();
    factory.registerParser(ModuleType.SCORM_12, parserFactory);
    // Successful registration doesn't throw an exception
  }

  @Test
  void unregisterParser_withNullModuleType_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> factory.unregisterParser(null));
  }

  @Test
  void unregisterParser_existingParser_returnsTrue() {
    // The default constructor already registers parsers for all module types
    boolean result = factory.unregisterParser(ModuleType.SCORM_12);
    assertTrue(result);
  }

  @Test
  void unregisterParser_nonExistingParser_returnsFalse() {
    // First unregister the parser
    factory.unregisterParser(ModuleType.SCORM_12);
    // Then try to unregister it again
    boolean result = factory.unregisterParser(ModuleType.SCORM_12);
    assertFalse(result);
  }

  @Test
  void getParser_scorm12_returnsScorm12Parser() throws ModuleDetectionException {
    moduleTypeDetector.setModuleType(ModuleType.SCORM_12);
    ModuleParser<?> parser = factory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm12Parser.class, parser.getClass());
  }

  @Test
  void getParser_scorm2004_returnsScorm2004Parser() throws ModuleDetectionException {
    moduleTypeDetector.setModuleType(ModuleType.SCORM_2004);
    ModuleParser<?> parser = factory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm2004Parser.class, parser.getClass());
  }

  @Test
  void getParser_aicc_returnsAiccParser() throws ModuleDetectionException {
    moduleTypeDetector.setModuleType(ModuleType.AICC);
    ModuleParser<?> parser = factory.getParser();
    assertNotNull(parser);
    assertEquals(AiccParser.class, parser.getClass());
  }

  @Test
  void getParser_cmi5_returnsCmi5Parser() throws ModuleDetectionException {
    moduleTypeDetector.setModuleType(ModuleType.CMI5);
    ModuleParser<?> parser = factory.getParser();
    assertNotNull(parser);
    assertEquals(Cmi5Parser.class, parser.getClass());
  }

  @Test
  void getParser_detectionException_throwsModuleDetectionException() {
    moduleTypeDetector.setThrowException(true);
    assertThrows(ModuleDetectionException.class, () -> factory.getParser());
  }

  @Test
  void getParser_unregisteredModuleType_throwsModuleDetectionException() {
    // First unregister the parser
    factory.unregisterParser(ModuleType.SCORM_12);
    // Then try to get a parser for that module type
    moduleTypeDetector.setModuleType(ModuleType.SCORM_12);
    assertThrows(ModuleDetectionException.class, () -> factory.getParser());
  }

  @Test
  void parseModule_validModule_returnsMetadata()
      throws ModuleDetectionException, ModuleParsingException {
    // Register a mock parser that returns a predefined metadata object
    Function<FileAccess, ModuleParser<?>> parserFactory = (fa) -> new MockModuleParser();
    factory.unregisterParser(ModuleType.SCORM_12);
    factory.registerParser(ModuleType.SCORM_12, parserFactory);

    moduleTypeDetector.setModuleType(ModuleType.SCORM_12);
    ModuleMetadata<?> metadata = factory.parseModule();
    assertNotNull(metadata);
    assertEquals(ModuleType.SCORM_12, metadata.getModuleType());
    assertEquals("Mock Title", metadata.getTitle());
  }

  @Test
  void parseModule_detectionException_throwsModuleDetectionException() {
    moduleTypeDetector.setThrowException(true);
    assertThrows(ModuleDetectionException.class, () -> factory.parseModule());
  }

  @Test
  void parseModule_parsingException_throwsModuleParsingException() {
    // Register a mock parser that throws an exception
    Function<FileAccess, ModuleParser<?>> parserFactory = (fa) -> {
      MockModuleParser parser = new MockModuleParser();
      parser.setThrowException(true);
      return parser;
    };
    factory.unregisterParser(ModuleType.SCORM_12);
    factory.registerParser(ModuleType.SCORM_12, parserFactory);

    moduleTypeDetector.setModuleType(ModuleType.SCORM_12);
    assertThrows(ModuleParsingException.class, () -> factory.parseModule());
  }

  // Mock implementations for testing
  private static class MockFileAccess implements FileAccess {

    @Override
    public String getRootPath() {
      return "";
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return true;
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      return Collections.emptyList();
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      return new ByteArrayInputStream(new byte[0]);
    }
  }

  private static class MockModuleTypeDetectorPlugin implements ModuleTypeDetectorPlugin {

    @Override
    public ModuleType detect(FileAccess fileAccess) throws ModuleDetectionException {
      return null;
    }

    @Override
    public int getPriority() {
      return 0;
    }

    @Override
    public String getName() {
      return "MockDetectorPlugin";
    }
  }

  private static class MockModuleTypeDetector implements ModuleTypeDetector {

    private final List<ModuleTypeDetectorPlugin> plugins = new ArrayList<>();
    private ModuleType moduleType = ModuleType.SCORM_12;
    private boolean throwException = false;

    public void setModuleType(ModuleType moduleType) {
      this.moduleType = moduleType;
    }

    public void setThrowException(boolean throwException) {
      this.throwException = throwException;
    }

    @Override
    public void registerPlugin(ModuleTypeDetectorPlugin plugin) {
      if (plugin == null) {
        throw new IllegalArgumentException("Plugin cannot be null");
      }
      plugins.add(plugin);
    }

    @Override
    public boolean unregisterPlugin(ModuleTypeDetectorPlugin plugin) {
      if (plugin == null) {
        throw new IllegalArgumentException("Plugin cannot be null");
      }
      return plugins.remove(plugin);
    }

    @Override
    public List<ModuleTypeDetectorPlugin> getPlugins() {
      return Collections.unmodifiableList(plugins);
    }

    @Override
    public ModuleType detectModuleType() throws ModuleDetectionException {
      if (throwException) {
        throw new ModuleDetectionException("Mock detection exception");
      }
      return moduleType;
    }
  }

  private static class MockPackageManifest implements PackageManifest {

    @Override
    public String getTitle() {
      return "Mock Title";
    }

    @Override
    public String getDescription() {
      return "Mock Description";
    }

    @Override
    public String getLaunchUrl() {
      return "mock/launch.html";
    }

    @Override
    public String getIdentifier() {
      return "mock-identifier";
    }

    @Override
    public String getVersion() {
      return "1.0";
    }

    @Override
    public Duration getDuration() {
      return Duration.ZERO;
    }
  }

  private static class MockModuleMetadata extends ModuleMetadata<PackageManifest> {

    public MockModuleMetadata(PackageManifest manifest) {
      super(manifest, ModuleType.SCORM_12, false);
    }
  }

  private static class MockModuleParser implements ModuleParser<PackageManifest> {

    private boolean throwException = false;

    public void setThrowException(boolean throwException) {
      this.throwException = throwException;
    }

    @Override
    public ModuleMetadata<PackageManifest> parse() throws ModuleParsingException {
      if (throwException) {
        throw new ModuleParsingException("Mock parsing exception");
      }
      return new MockModuleMetadata(new MockPackageManifest());
    }
  }
}
