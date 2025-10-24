package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for FileUtils utility class.
 */
public class FileUtilsTest {

  @Test
  void findFileIgnoreCase_exactMatch() {
    List<String> files = Arrays.asList("manifest.xml", "index.html", "styles.css");
    String result = FileUtils.findFileIgnoreCase(files, "manifest.xml");
    assertEquals("manifest.xml", result);
  }

  @Test
  void findFileIgnoreCase_caseInsensitive() {
    List<String> files = Arrays.asList("MANIFEST.XML", "index.html");
    String result = FileUtils.findFileIgnoreCase(files, "manifest.xml");
    assertEquals("MANIFEST.XML", result);
  }

  @Test
  void findFileIgnoreCase_exactPathMatch() {
    // Exact path matching (not basename matching)
    List<String> files = Arrays.asList("testClose/imsmanifest.xml", "testClose/index.html");
    String result = FileUtils.findFileIgnoreCase(files, "testClose/imsmanifest.xml");
    assertEquals("testClose/imsmanifest.xml", result);
  }

  @Test
  void findFileIgnoreCase_doesNotMatchBasename() {
    // Should NOT match based on basename alone
    List<String> files = Arrays.asList("dir1/dir2/file.txt", "dir1/other.txt");
    String result = FileUtils.findFileIgnoreCase(files, "file.txt");
    assertNull(result, "Should not match basename when full path is different");
  }

  @Test
  void findFileIgnoreCase_notFound() {
    List<String> files = Arrays.asList("manifest.xml", "index.html");
    String result = FileUtils.findFileIgnoreCase(files, "missing.txt");
    assertNull(result);
  }

  @Test
  void findFileIgnoreCase_emptyList() {
    List<String> files = Arrays.asList();
    String result = FileUtils.findFileIgnoreCase(files, "file.txt");
    assertNull(result);
  }
}
