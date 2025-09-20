package dev.jcputney.elearning.parser.parsers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Scorm2004XsdValidationTest {

  private String originalProp;

  @BeforeEach
  void enableValidation() {
    originalProp = System.getProperty("elearning.parser.scorm2004.validateXsd");
    System.setProperty("elearning.parser.scorm2004.validateXsd", "true");
  }

  @AfterEach
  void restoreValidation() {
    if (originalProp == null) {
      System.clearProperty("elearning.parser.scorm2004.validateXsd");
    } else {
      System.setProperty("elearning.parser.scorm2004.validateXsd", originalProp);
    }
  }

  @Test
  void validMinimalManifest_passesValidationAndParses() throws Exception {
    String path = "src/test/resources/modules/scorm2004/MinimalValid_SCORM20043rdEdition";
    LocalFileAccess access = new LocalFileAccess(path);
    ModuleParserFactory factory = new DefaultModuleParserFactory(access);
    ModuleMetadata<?> metadata = factory.parseModule();

    assertThat(metadata).isInstanceOf(Scorm2004Metadata.class);
    assertThat(metadata.getLaunchUrl()).isEqualTo("index.html");
  }

  @Test
  void invalidManifest_missingOrganizationIdentifier_failsValidation() {
    String path = "src/test/resources/modules/scorm2004/InvalidMissingOrgIdentifier_SCORM20043rdEdition";
    LocalFileAccess access = new LocalFileAccess(path);
    ModuleParserFactory factory = new DefaultModuleParserFactory(access);

    ModuleParsingException ex = assertThrows(ModuleParsingException.class, factory::parseModule);
    assertThat(ex.getMessage()).contains("XSD validation failed");
  }
}

