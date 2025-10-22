package dev.jcputney.elearning.parser.impl.detector;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
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
  void shouldDetectXapiWithTincanXml() {
    when(mockFileAccess.fileExists("tincan.xml")).thenReturn(true);

    assertThat(detector.detect(mockFileAccess)).isEqualTo(ModuleType.XAPI);
  }

  @Test
  void shouldReturnNullWhenNoTincanXml() {
    when(mockFileAccess.fileExists("tincan.xml")).thenReturn(false);

    assertThat(detector.detect(mockFileAccess)).isNull();
  }

  @Test
  void shouldReturnNullForEmptyDirectory() {
    when(mockFileAccess.fileExists("tincan.xml")).thenReturn(false);

    assertThat(detector.detect(mockFileAccess)).isNull();
  }

  @Test
  void shouldHaveCorrectPriority() {
    assertThat(detector.getPriority()).isEqualTo(40);
  }
}
