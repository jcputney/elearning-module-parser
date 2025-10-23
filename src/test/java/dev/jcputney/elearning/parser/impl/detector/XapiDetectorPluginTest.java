package dev.jcputney.elearning.parser.impl.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class XapiDetectorPluginTest {

  @Mock
  private FileAccess mockFileAccess;

  private XapiDetectorPlugin detector;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    detector = new XapiDetectorPlugin();
  }

  @Test
  void shouldDetectXapiWithTincanXml() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("tincan.xml", "index.html"));

    assertThat(detector.detect(mockFileAccess)).isEqualTo(ModuleType.XAPI);
  }

  @Test
  void shouldReturnNullWhenNoTincanXml() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("index.html"));

    assertThat(detector.detect(mockFileAccess)).isNull();
  }

  @Test
  void shouldReturnNullForEmptyDirectory() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of());

    assertThat(detector.detect(mockFileAccess)).isNull();
  }

  @Test
  void shouldDetectXapiWithDifferentCasing() throws Exception {
    when(mockFileAccess.listFiles("")).thenReturn(List.of("TINCAN.XML", "index.html"));

    assertThat(detector.detect(mockFileAccess)).isEqualTo(ModuleType.XAPI);
  }

  @Test
  void shouldHaveCorrectPriority() {
    assertThat(detector.getPriority()).isEqualTo(40);
  }
}
