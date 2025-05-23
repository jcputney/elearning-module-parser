import static org.junit.jupiter.api.Assertions.*;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import java.time.Duration;
import org.junit.jupiter.api.Test;

/** Tests for {@link Scorm12Metadata}. */
class Scorm12MetadataTest {

  private static class TestScorm12Manifest extends dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest {
    private final String title;
    private final String description;
    private final String launchUrl;
    private final String identifier;
    private final String version;
    private final Duration duration;

    TestScorm12Manifest(String title, String description, String launchUrl,
                        String identifier, String version, Duration duration) {
      this.title = title;
      this.description = description;
      this.launchUrl = launchUrl;
      this.identifier = identifier;
      this.version = version;
      this.duration = duration;
    }

    @Override public String getTitle() { return title; }
    @Override public String getDescription() { return description; }
    @Override public String getLaunchUrl() { return launchUrl; }
    @Override public String getIdentifier() { return identifier; }
    @Override public String getVersion() { return version; }
    @Override public Duration getDuration() { return duration; }
  }

  @Test
  void createPopulatesBasicMetadata() {
    TestScorm12Manifest manifest = new TestScorm12Manifest(
        "t", "d", "l", "i", "v", Duration.ofMinutes(1));

    Scorm12Metadata metadata = Scorm12Metadata.create(manifest, false);

    assertEquals(ModuleType.SCORM_12, metadata.getModuleType());
    assertFalse(metadata.isXapiEnabled());
    assertSame(manifest, metadata.getManifest());
    assertEquals("t", metadata.getMetadata("title", String.class).orElseThrow());
  }
}
