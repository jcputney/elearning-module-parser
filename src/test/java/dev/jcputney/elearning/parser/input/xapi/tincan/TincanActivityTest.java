package dev.jcputney.elearning.parser.input.xapi.tincan;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TincanActivityTest {

  @Test
  void shouldDeserializeFromXml() throws Exception {
    // Using the standard TinCan format per official XSD schema
    String xml = """
        <activity id="http://example.com/activity/test"
                  type="http://adlnet.gov/expapi/activities/course">
          <name lang="en-US">Test Activity</name>
          <description lang="en-US">Test description</description>
          <launch lang="en-US">index.html</launch>
        </activity>
        """;

    XmlMapper mapper = new XmlMapper();
    TincanActivity activity = mapper.readValue(xml, TincanActivity.class);

    assertThat(activity.getId()).isEqualTo("http://example.com/activity/test");
    assertThat(activity.getType()).isEqualTo("http://adlnet.gov/expapi/activities/course");
    assertThat(activity.getName()).isEqualTo("Test Activity");
    assertThat(activity.getDescription()).isEqualTo("Test description");
    assertThat(activity.getLaunch()).isEqualTo("index.html");
  }
}
