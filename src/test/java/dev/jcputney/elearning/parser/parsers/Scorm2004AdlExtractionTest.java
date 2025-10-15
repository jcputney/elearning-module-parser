package dev.jcputney.elearning.parser.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Scorm2004AdlExtractionTest {

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
  void extractsAdlcpAdlnavAndControlModeFields() throws Exception {
    String path = "src/test/resources/modules/scorm2004/AdlAttributes_SCORM20043rdEdition";
    LocalFileAccess access = new LocalFileAccess(path);
    ModuleParserFactory factory = new DefaultModuleParserFactory(access);
    ModuleMetadata<?> metadata = factory.parseModule();
    Scorm2004Metadata scorm2004 = (Scorm2004Metadata) metadata;

    // timeLimitActions
    Map<String, String> timeLimitActions = scorm2004.getTimeLimitActions();
    assertThat(timeLimitActions).containsEntry("item1", "EXIT_MESSAGE");

    // dataFromLMS
    Map<String, String> dataFromLms = scorm2004.getDataFromLms();
    assertThat(dataFromLms).containsEntry("item1", "Launch Data");

    // completionThresholds
    Map<String, Map<String, Object>> completionThresholds = scorm2004.getCompletionThresholds();
    assertThat(completionThresholds).containsKey("item1");
    Map<String, Object> ct = completionThresholds.get("item1");
    assertThat(ct.get("completedByMeasure")).isEqualTo(true);
    assertThat(new BigDecimal(ct
        .get("minProgressMeasure")
        .toString()))
        .isEqualByComparingTo(new BigDecimal("0.7500"));
    assertThat(new BigDecimal(ct
        .get("progressWeight")
        .toString()))
        .isEqualByComparingTo(new BigDecimal("1.0"));

    // hideLMSUI
    Map<String, List<String>> hideLmsUi = scorm2004.getHideLmsUi();
    assertThat(hideLmsUi.get("item1")).containsExactlyInAnyOrder("EXIT", "CONTINUE");

    // controlModes
    Map<String, Map<String, Boolean>> controlModes = scorm2004.getControlModes();
    assertThat(controlModes).containsKey("item1");
    Map<String, Boolean> cm = controlModes.get("item1");
    assertThat(cm.get("flow")).isTrue();
    assertThat(cm.get("choice")).isFalse();
    assertThat(cm.get("forwardOnly")).isFalse();
    assertThat(cm.get("choiceExit")).isFalse();
  }
}
