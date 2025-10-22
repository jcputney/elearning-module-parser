package dev.jcputney.elearning.parser.output.metadata.xapi;

import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanActivity;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class XapiMetadataTest {

  @Test
  void shouldCreateWithCorrectModuleType() {
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    activity.setName("Test");

    TincanManifest manifest = new TincanManifest(List.of(activity));
    XapiMetadata metadata = new XapiMetadata(manifest);

    assertThat(metadata.getModuleType()).isEqualTo(ModuleType.XAPI);
    assertThat(metadata.getModuleEditionType()).isEqualTo(ModuleEditionType.XAPI);
    assertThat(metadata.isXapiEnabled()).isTrue();
  }

  @Test
  void shouldNotHaveMultipleLaunchableUnits() {
    TincanActivity activity = new TincanActivity();
    TincanManifest manifest = new TincanManifest(List.of(activity));
    XapiMetadata metadata = new XapiMetadata(manifest);

    assertThat(metadata.hasMultipleLaunchableUnits()).isFalse();
  }

  @Test
  void shouldDelegateToManifest() {
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    activity.setName("Test Course");

    TincanManifest manifest = new TincanManifest(List.of(activity));
    XapiMetadata metadata = new XapiMetadata(manifest);

    assertThat(metadata.getTitle()).isEqualTo("Test Course");
    assertThat(metadata.getIdentifier()).isEqualTo("http://example.com/activity/1");
  }
}
