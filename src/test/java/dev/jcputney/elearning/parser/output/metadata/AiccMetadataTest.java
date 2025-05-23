import static org.junit.jupiter.api.Assertions.*;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests for {@link AiccMetadata}. */
class AiccMetadataTest {

  private static class TestAiccManifest extends dev.jcputney.elearning.parser.input.aicc.AiccManifest {
    private final String title;
    private final String description;
    private final String launchUrl;
    private final String identifier;
    private final String version;
    private final Duration duration;
    private final List<AssignableUnit> units;

    TestAiccManifest(String title, String description, String launchUrl,
                     String identifier, String version, Duration duration,
                     List<AssignableUnit> units) {
      this.title = title;
      this.description = description;
      this.launchUrl = launchUrl;
      this.identifier = identifier;
      this.version = version;
      this.duration = duration;
      this.units = units;
    }

    @Override public String getTitle() { return title; }
    @Override public String getDescription() { return description; }
    @Override public String getLaunchUrl() { return launchUrl; }
    @Override public String getIdentifier() { return identifier; }
    @Override public String getVersion() { return version; }
    @Override public Duration getDuration() { return duration; }
    @Override public List<AssignableUnit> getAssignableUnits() { return units; }
  }

  @Test
  void createAddsAssignableUnitMetadata() {
    AssignableUnit au1 = AssignableUnit.builder()
        .systemId("id1")
        .fileName("file1")
        .commandLine("cmd")
        .coreVendor("vendor")
        .build();
    AssignableUnit au2 = AssignableUnit.builder()
        .systemId("id2")
        .fileName("file2")
        .commandLine("cmd")
        .coreVendor("vendor")
        .build();
    TestAiccManifest manifest = new TestAiccManifest(
        "t", "d", "l", "i", "v", Duration.ZERO,
        List.of(au1, au2));

    AiccMetadata metadata = AiccMetadata.create(manifest, true);

    assertEquals(ModuleType.AICC, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
    assertSame(manifest, metadata.getManifest());

    @SuppressWarnings("unchecked")
    List<String> ids = (List<String>) metadata.getMetadata("assignableUnitIds").orElseThrow();
    assertEquals(List.of("id1", "id2"), ids);
    @SuppressWarnings("unchecked")
    List<String> names = (List<String>) metadata.getMetadata("assignableUnitNames").orElseThrow();
    assertEquals(List.of("file1", "file2"), names);
  }
}
