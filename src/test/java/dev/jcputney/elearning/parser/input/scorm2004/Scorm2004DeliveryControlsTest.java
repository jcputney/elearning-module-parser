package dev.jcputney.elearning.parser.input.scorm2004;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.DeliveryControls;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import org.junit.jupiter.api.Test;

class Scorm2004DeliveryControlsTest {

  @Test
  void surfacesDeliveryControlsWithOverrides() throws Exception {
    ModuleMetadata<?> metadata = parseModule(
        "src/test/resources/modules/scorm2004/RuntimeBasicCalls_SCORM20043rdEdition"
    );

    assertThat(metadata).isInstanceOf(Scorm2004Metadata.class);

    Scorm2004Metadata scorm2004 = (Scorm2004Metadata) metadata;

    DeliveryControls controls = scorm2004
        .getActivityDeliveryControls()
        .get("item_1");

    assertThat(controls).isNotNull();
    assertThat(controls.isTracked()).isTrue();
    assertThat(controls.isCompletionSetByContent()).isTrue();
    assertThat(controls.isObjectiveSetByContent()).isTrue();
    assertThat(scorm2004.getActivitiesOverridingDeliveryControlDefaults()).contains("item_1");
    assertThat(scorm2004.overridesDeliveryControlDefaults("item_1")).isTrue();
  }

  @Test
  void surfacesDefaultDeliveryControlsWhenNotSpecified() throws Exception {
    ModuleMetadata<?> metadata = parseModule(
        "src/test/resources/modules/scorm2004/ContentPackagingSingleSCO_SCORM20043rdEdition"
    );

    assertThat(metadata).isInstanceOf(Scorm2004Metadata.class);

    Scorm2004Metadata scorm2004 = (Scorm2004Metadata) metadata;

    DeliveryControls controls = scorm2004
        .getActivityDeliveryControls()
        .get("item_1");

    assertThat(controls).isNotNull();
    assertThat(controls.isTracked()).isTrue();
    assertThat(controls.isCompletionSetByContent()).isFalse();
    assertThat(controls.isObjectiveSetByContent()).isFalse();
    assertThat(scorm2004.getActivitiesOverridingDeliveryControlDefaults()).doesNotContain("item_1");
    assertThat(scorm2004.overridesDeliveryControlDefaults("item_1")).isFalse();
  }

  private ModuleMetadata<?> parseModule(String modulePath) throws Exception {
    LocalFileAccess access = new LocalFileAccess(modulePath);
    ModuleParserFactory factory = new DefaultModuleParserFactory(access);
    return factory.parseModule();
  }
}
