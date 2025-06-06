package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ScormVersionDetectorTest {

  private static Stream<Arguments> provideManifestData() {
    return Stream.of(
        Arguments.of("""
            <?xml version="1.0" encoding="UTF-8"?>
            <manifest>
              <metadata>
                <schema>ADL SCORM</schema>
                <schemaversion>1.2</schemaversion>
              </metadata>
            </manifest>
            """, ModuleType.SCORM_12),
        Arguments.of("""
            <?xml version="1.0" encoding="UTF-8"?>
            <manifest>
              <metadata>
                <schema>ADL SCORM</schema>
                <schemaversion>2004 4th Edition</schemaversion>
              </metadata>
            </manifest>
            """, ModuleType.SCORM_2004),
        Arguments.of("""
            <?xml version="1.0" encoding="UTF-8"?>
            <manifest xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_v1p3">
              <metadata>
                <schema>Some Other Schema</schema>
                <schemaversion>1.0</schemaversion>
              </metadata>
            </manifest>
            """, ModuleType.SCORM_2004),
        Arguments.of("""
            <?xml version="1.0" encoding="UTF-8"?>
            <manifest>
              <metadata>
                <schema>Some Other Schema</schema>
                <schemaversion>1.0</schemaversion>
              </metadata>
            </manifest>
            """, ModuleType.SCORM_12),
        Arguments.of("""
            <?xml version="1.0" encoding="UTF-8"?>
            <manifest>
              <metadata>
                <!-- No schema or schemaversion elements -->
              </metadata>
            </manifest>
            """, ModuleType.SCORM_12)
    );
  }

  @Test
  void detectScormVersionWithNullFileAccessThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> ScormVersionDetector.detectScormVersion(null));
  }

  @ParameterizedTest
  @MethodSource("provideManifestData")
  void detectScormVersionWithVariousManifests(String manifestContent, ModuleType expectedType)
      throws Exception {
    MockFileAccess fileAccess = new MockFileAccess("root/path");
    fileAccess.setFileContents(manifestContent);

    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(expectedType, result);
  }

  private static class MockFileAccess implements FileAccess {

    private final String rootPath;
    private final Map<String, String> fileContents = new HashMap<>();

    MockFileAccess(String rootPath) {
      this.rootPath = rootPath;
    }

    @Override
    public String getRootPath() {
      return rootPath;
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return fileContents.containsKey(path);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      return Collections.emptyList();
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      String contents = fileContents.getOrDefault(path, "");
      return new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
    }

    void setFileContents(String contents) {
      fileContents.put(Scorm12Parser.MANIFEST_FILE, contents);
    }
  }
}