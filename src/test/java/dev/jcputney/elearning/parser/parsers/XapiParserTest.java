package dev.jcputney.elearning.parser.parsers;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.metadata.xapi.XapiMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class XapiParserTest {

  @Mock
  private FileAccess mockFileAccess;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldParseValidTincanManifest() throws Exception {
    String xml = """
        <?xml version="1.0" encoding="utf-8" ?>
        <tincan xmlns="http://projecttincan.com/tincan.xsd">
          <activities>
            <activity id="http://example.com/activity/1"
                      type="http://adlnet.gov/expapi/activities/course">
              <name>Test Course</name>
              <description>
                <langstring lang="en-US">A test course</langstring>
              </description>
              <launch>
                <langstring lang="en-us">index.html</langstring>
              </launch>
            </activity>
          </activities>
        </tincan>
        """;

    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    when(mockFileAccess.getFileContents("tincan.xml")).thenReturn(inputStream);

    XapiParser parser = new XapiParser(mockFileAccess);
    XapiMetadata metadata = parser.parse();

    assertThat(metadata).isNotNull();
    assertThat(metadata.getModuleType()).isEqualTo(ModuleType.XAPI);
    assertThat(metadata.getTitle()).isEqualTo("Test Course");
    assertThat(metadata.getIdentifier()).isEqualTo("http://example.com/activity/1");
    assertThat(metadata.getLaunchUrl()).isEqualTo("index.html");
  }

  @Test
  void shouldThrowExceptionWhenFileNotFound() throws Exception {
    when(mockFileAccess.getFileContents("tincan.xml")).thenReturn(null);

    XapiParser parser = new XapiParser(mockFileAccess);
    assertThatThrownBy(() -> parser.parse())
        .isInstanceOf(ModuleParsingException.class)
        .hasMessageContaining("tincan.xml not found");
  }

  @Test
  void shouldThrowExceptionForInvalidXml() throws Exception {
    String invalidXml = "not valid xml";
    InputStream inputStream = new ByteArrayInputStream(invalidXml.getBytes());
    when(mockFileAccess.getFileContents("tincan.xml")).thenReturn(inputStream);

    XapiParser parser = new XapiParser(mockFileAccess);
    assertThatThrownBy(() -> parser.parse())
        .isInstanceOf(ModuleParsingException.class);
  }
}
