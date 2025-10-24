package dev.jcputney.elearning.parser.parsers;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ParseResult;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.output.metadata.xapi.XapiMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

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

    when(mockFileAccess.listFiles("")).thenReturn(List.of("tincan.xml", "index.html"));
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    when(mockFileAccess.getFileContents("tincan.xml")).thenReturn(inputStream);

    XapiParser parser = new XapiParser(mockFileAccess);
    XapiMetadata metadata = (XapiMetadata) parser.parseAndValidate().metadata();

    assertThat(metadata).isNotNull();
    assertThat(metadata.getModuleType()).isEqualTo(ModuleType.XAPI);
    assertThat(metadata.getTitle()).isEqualTo("Test Course");
    assertThat(metadata.getIdentifier()).isEqualTo("http://example.com/activity/1");
    assertThat(metadata.getLaunchUrl()).isEqualTo("index.html");
  }

  @Test
  void shouldThrowExceptionWhenFileNotFound() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("index.html"));

    XapiParser parser = new XapiParser(mockFileAccess);
    assertThatThrownBy(() -> parser.parseAndValidate())
        .isInstanceOf(ModuleParsingException.class)
        .hasMessageContaining("tincan.xml not found");
  }

  @Test
  void shouldThrowExceptionForInvalidXml() throws Exception {
    String invalidXml = "not valid xml";
    InputStream inputStream = new ByteArrayInputStream(invalidXml.getBytes());
    when(mockFileAccess.getFileContents("tincan.xml")).thenReturn(inputStream);

    XapiParser parser = new XapiParser(mockFileAccess);
    assertThatThrownBy(() -> parser.parseAndValidate())
        .isInstanceOf(ModuleParsingException.class);
  }

  @Test
  void shouldParseStandardTincanFormatWithLangAttributes() throws Exception {
    // This is the standard TinCan format per the official XSD schema
    // where name, description, and launch have lang attributes directly on the element
    String xml = """
        <?xml version="1.0" encoding="utf-8"?>
        <tincan xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://projecttincan.com/tincan.xsd">
            <activities>
                <activity id="http://5c0JPCccFz1_course_id" type="http://adlnet.gov/expapi/activities/course">
                    <name lang="und">Arctic Wolf Managed Security Awareness</name>
                    <description lang="und">Test description</description>
                    <launch lang="und">index_lms.html</launch>
                </activity>
            </activities>
        </tincan>
        """;

    when(mockFileAccess.listFiles("")).thenReturn(List.of("tincan.xml", "index_lms.html"));
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
    when(mockFileAccess.getFileContents("tincan.xml")).thenReturn(inputStream);

    XapiParser parser = new XapiParser(mockFileAccess);
    XapiMetadata metadata = (XapiMetadata) parser.parseAndValidate().metadata();

    assertThat(metadata).isNotNull();
    assertThat(metadata.getModuleType()).isEqualTo(ModuleType.XAPI);
    assertThat(metadata.getTitle()).isEqualTo("Arctic Wolf Managed Security Awareness");
    assertThat(metadata.getIdentifier()).isEqualTo("http://5c0JPCccFz1_course_id");
    assertThat(metadata.getLaunchUrl()).isEqualTo("index_lms.html");
  }
}
