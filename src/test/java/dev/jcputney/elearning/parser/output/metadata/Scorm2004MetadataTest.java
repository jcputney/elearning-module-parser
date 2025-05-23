import static org.junit.jupiter.api.Assertions.*;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Tests for {@link Scorm2004Metadata}. */
class Scorm2004MetadataTest {

  private static class TestScorm2004Manifest extends dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest {
    private final String title;
    private final String description;
    private final String launchUrl;
    private final String identifier;
    private final String version;
    private final Duration duration;
    private final Scorm2004Organizations orgs;

    TestScorm2004Manifest(String title, String description, String launchUrl,
                          String identifier, String version, Duration duration,
                          Scorm2004Organizations orgs) {
      this.title = title;
      this.description = description;
      this.launchUrl = launchUrl;
      this.identifier = identifier;
      this.version = version;
      this.duration = duration;
      this.orgs = orgs;
    }

    @Override public String getTitle() { return title; }
    @Override public String getDescription() { return description; }
    @Override public String getLaunchUrl() { return launchUrl; }
    @Override public String getIdentifier() { return identifier; }
    @Override public String getVersion() { return version; }
    @Override public Duration getDuration() { return duration; }
    @Override public Scorm2004Organizations getOrganizations() { return orgs; }
  }

  @Test
  void createExtractsGlobalObjectiveIds() {
    Scorm2004ObjectiveMapping mapping = Scorm2004ObjectiveMapping.builder()
        .targetObjectiveID("obj1").build();
    Scorm2004Objective objective = Scorm2004Objective.builder()
        .mapInfo(List.of(mapping)).build();
    Scorm2004Objectives objectives = Scorm2004Objectives.builder()
        .objectiveList(List.of(objective)).build();
    Sequencing sequencing = Sequencing.builder().objectives(objectives).build();
    Scorm2004Item item = Scorm2004Item.builder().sequencing(sequencing).build();
    Scorm2004Organization org = Scorm2004Organization.builder()
        .identifier("o1").items(List.of(item)).build();
    Scorm2004Organizations orgs = Scorm2004Organizations.builder()
        .defaultOrganization("o1").organizationList(List.of(org)).build();

    TestScorm2004Manifest manifest = new TestScorm2004Manifest(
        "t", "d", "l", "i", "v", Duration.ZERO, orgs);

    Scorm2004Metadata metadata = Scorm2004Metadata.create(manifest, true);

    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
    assertSame(manifest, metadata.getManifest());

    @SuppressWarnings("unchecked")
    Set<String> ids = (Set<String>) metadata.getMetadata("globalObjectiveIds").orElseThrow();
    assertEquals(Set.of("obj1"), ids);
  }
}
