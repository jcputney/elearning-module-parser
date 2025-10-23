package dev.jcputney.elearning.parser.input.xapi.tincan;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TincanManifestTest {

  @Test
  void shouldDeserializeFromXml() throws Exception {
    // Using the standard TinCan format per official XSD schema
    String xml = """
        <?xml version="1.0" encoding="utf-8" ?>
        <tincan xmlns="http://projecttincan.com/tincan.xsd">
          <activities>
            <activity id="http://example.com/activity/1"
                      type="http://adlnet.gov/expapi/activities/course">
              <name lang="en-US">Test Course</name>
              <description lang="en-US">A test course</description>
              <launch lang="en-US">index.html</launch>
            </activity>
          </activities>
        </tincan>
        """;

    XmlMapper mapper = new XmlMapper();
    TincanManifest manifest = mapper.readValue(xml, TincanManifest.class);

    assertThat(manifest.getActivities()).hasSize(1);
    assertThat(manifest.getTitle()).isEqualTo("Test Course");
    assertThat(manifest.getDescription()).isEqualTo("A test course");
    assertThat(manifest.getIdentifier()).isEqualTo("http://example.com/activity/1");
    assertThat(manifest.getLaunchUrl()).isEqualTo("index.html");
  }

  @Test
  void shouldReturnNullForMissingVersion() {
    TincanManifest manifest = new TincanManifest();
    assertThat(manifest.getVersion()).isNull();
  }

  @Test
  void shouldReturnZeroDuration() {
    TincanManifest manifest = new TincanManifest();
    assertThat(manifest.getDuration()).isEqualTo(Duration.ZERO);
  }
}
