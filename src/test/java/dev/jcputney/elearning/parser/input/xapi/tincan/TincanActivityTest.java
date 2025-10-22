package dev.jcputney.elearning.parser.input.xapi.tincan;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TincanActivityTest {

  @Test
  void shouldDeserializeFromXml() throws Exception {
    String xml = """
        <activity id="http://example.com/activity/test"
                  type="http://adlnet.gov/expapi/activities/course">
          <name>Test Activity</name>
          <description>
            <langstring lang="en-US">Test description</langstring>
          </description>
          <launch>
            <langstring lang="en-us">index.html</langstring>
          </launch>
        </activity>
        """;

    XmlMapper mapper = new XmlMapper();
    TincanActivity activity = mapper.readValue(xml, TincanActivity.class);

    assertThat(activity.getId()).isEqualTo("http://example.com/activity/test");
    assertThat(activity.getType()).isEqualTo("http://adlnet.gov/expapi/activities/course");
    assertThat(activity.getName()).isEqualTo("Test Activity");
  }
}
