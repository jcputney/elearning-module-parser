/* Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.PackageManifest;
import java.io.Serial;
import java.time.Duration;
import java.util.Objects;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ModuleMetadata} to ensure proper initialization and delegation to the manifest.
 */
class ModuleMetadataTest {

  /**
   * Tests that the constructor with xapiEnabled=true initializes the fields correctly.
   */
  @Test
  void constructor_withXapiEnabledTrue_initializesFields() {
    // Arrange
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", "http://example.com/launch",
        "test-id", "1.0", Duration.ofMinutes(30));
    ModuleType moduleType = ModuleType.SCORM_12;
    boolean xapiEnabled = true;

    // Act
    TestModuleMetadata metadata = new TestModuleMetadata(manifest, moduleType, xapiEnabled);

    // Assert
    assertSame(manifest, metadata.getManifest());
    assertSame(moduleType, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
  }

  /**
   * Tests that the constructor with xapiEnabled=false initializes the fields correctly.
   */
  @Test
  void constructor_withXapiEnabledFalse_initializesFields() {
    // Arrange
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", "http://example.com/launch",
        "test-id", "1.0", Duration.ofMinutes(30));
    ModuleType moduleType = ModuleType.SCORM_12;
    boolean xapiEnabled = false;

    // Act
    TestModuleMetadata metadata = new TestModuleMetadata(manifest, moduleType, xapiEnabled);

    // Assert
    assertSame(manifest, metadata.getManifest());
    assertSame(moduleType, metadata.getModuleType());
    assertFalse(metadata.isXapiEnabled());
  }

  /**
   * Tests that the getTitle method delegates to the manifest.
   */
  @Test
  void getTitle_delegatesToManifest() {
    // Arrange
    String expectedTitle = "Test Title";
    TestPackageManifest manifest = new TestPackageManifest(
        expectedTitle, "Test Description", "http://example.com/launch",
        "test-id", "1.0", Duration.ofMinutes(30));
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);

    // Act
    String actualTitle = metadata.getTitle();

    // Assert
    assertEquals(expectedTitle, actualTitle);
  }

  /**
   * Tests that the getDescription method delegates to the manifest.
   */
  @Test
  void getDescription_delegatesToManifest() {
    // Arrange
    String expectedDescription = "Test Description";
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", expectedDescription, "http://example.com/launch",
        "test-id", "1.0", Duration.ofMinutes(30));
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);

    // Act
    String actualDescription = metadata.getDescription();

    // Assert
    assertEquals(expectedDescription, actualDescription);
  }

  /**
   * Tests that the getLaunchUrl method delegates to the manifest.
   */
  @Test
  void getLaunchUrl_delegatesToManifest() {
    // Arrange
    String expectedLaunchUrl = "http://example.com/launch";
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", expectedLaunchUrl,
        "test-id", "1.0", Duration.ofMinutes(30));
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);

    // Act
    String actualLaunchUrl = metadata.getLaunchUrl();

    // Assert
    assertEquals(expectedLaunchUrl, actualLaunchUrl);
  }

  /**
   * Tests that the getIdentifier method delegates to the manifest.
   */
  @Test
  void getIdentifier_delegatesToManifest() {
    // Arrange
    String expectedIdentifier = "test-id";
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", "http://example.com/launch",
        expectedIdentifier, "1.0", Duration.ofMinutes(30));
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);

    // Act
    String actualIdentifier = metadata.getIdentifier();

    // Assert
    assertEquals(expectedIdentifier, actualIdentifier);
  }

  /**
   * Tests that the getVersion method delegates to the manifest.
   */
  @Test
  void getVersion_delegatesToManifest() {
    // Arrange
    String expectedVersion = "1.0";
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", "http://example.com/launch",
        "test-id", expectedVersion, Duration.ofMinutes(30));
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);

    // Act
    String actualVersion = metadata.getVersion();

    // Assert
    assertEquals(expectedVersion, actualVersion);
  }

  /**
   * Tests that the getDuration method delegates to the manifest.
   */
  @Test
  void getDuration_delegatesToManifest() {
    // Arrange
    Duration expectedDuration = Duration.ofMinutes(30);
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", "http://example.com/launch",
        "test-id", "1.0", expectedDuration);
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);

    // Act
    Duration actualDuration = metadata.getDuration();

    // Assert
    assertEquals(expectedDuration, actualDuration);
  }

  /**
   * Tests that setSizeOnDisk properly updates the value.
   */
  @Test
  void setSizeOnDisk_updatesValue() {
    // Arrange
    TestPackageManifest manifest = new TestPackageManifest(
        "Test Title", "Test Description", "http://example.com/launch",
        "test-id", "1.0", Duration.ofMinutes(30));
    TestModuleMetadata metadata = new TestModuleMetadata(
        manifest, ModuleType.SCORM_12, false);
    long expectedSize = 1024L * 1024L; // 1MB

    // Act
    metadata.setSizeOnDisk(expectedSize);
    long actualSize = metadata.getSizeOnDisk();

    // Assert
    assertEquals(expectedSize, actualSize);
  }

  /**
   * A concrete implementation of PackageManifest for testing purposes.
   */
  private static final class TestPackageManifest implements PackageManifest {

    @Serial
    private static final long serialVersionUID = 0L;
    private final String title;
    private final String description;
    private final String launchUrl;
    private final String identifier;
    private final String version;
    private final Duration duration;

    /**
     *
     */
    private TestPackageManifest(String title, String description, String launchUrl,
        String identifier, String version,
        Duration duration) {
      this.title = title;
      this.description = description;
      this.launchUrl = launchUrl;
      this.identifier = identifier;
      this.version = version;
      this.duration = duration;
    }

    @Override
    public String getTitle() {
      return title;
    }

    @Override
    public String getDescription() {
      return description;
    }

    @Override
    public String getLaunchUrl() {
      return launchUrl;
    }

    @Override
    public String getIdentifier() {
      return identifier;
    }

    @Override
    public String getVersion() {
      return version;
    }

    @Override
    public Duration getDuration() {
      return duration;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj == null || obj.getClass() != this.getClass()) {
        return false;
      }
      var that = (TestPackageManifest) obj;
      return Objects.equals(this.title, that.title) &&
          Objects.equals(this.description, that.description) &&
          Objects.equals(this.launchUrl, that.launchUrl) &&
          Objects.equals(this.identifier, that.identifier) &&
          Objects.equals(this.version, that.version) &&
          Objects.equals(this.duration, that.duration);
    }

    @Override
    public int hashCode() {
      return Objects.hash(title, description, launchUrl, identifier, version, duration);
    }

    @Override
    public String toString() {
      return "TestPackageManifest[" +
          "title=" + title + ", " +
          "description=" + description + ", " +
          "launchUrl=" + launchUrl + ", " +
          "identifier=" + identifier + ", " +
          "version=" + version + ", " +
          "duration=" + duration + ']';
    }


  }

  /**
   * A concrete implementation of ModuleMetadata for testing purposes.
   */
  private static class TestModuleMetadata extends ModuleMetadata<TestPackageManifest> {
    // No additional fields or methods needed for testing

    protected TestModuleMetadata() {
      super();
    }

    public TestModuleMetadata(TestPackageManifest manifest, ModuleType moduleType,
        boolean xapiEnabled) {
      super(manifest, moduleType, ModuleEditionType.fromModuleType(moduleType, null), xapiEnabled);
    }

    @Override
    public TestPackageManifest getManifest() {
      return manifest;
    }

    @Override
    public boolean hasMultipleLaunchableUnits() {
      return false;
    }

    @Override
    public String getManifestFile() {
      return "test-manifest.xml";
    }

  }
}
