/* Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom;

import static dev.jcputney.elearning.parser.input.lom.types.Name.MS_INTERNET_EXPLORER;
import static dev.jcputney.elearning.parser.input.lom.types.Type.BROWSER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.OrComposite;
import dev.jcputney.elearning.parser.input.lom.types.Requirement;
import java.io.File;
import java.time.Duration;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Technical} class.
 */
class TechnicalTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  /**
   * Tests the deserialization of a Technical object from XML.
   */
  @Test
  void testDeserializeTechnical() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getTechnical());

    Technical technical = lom.getTechnical();

    // Test format
    assertNotNull(technical.getFormat());
    assertFalse(technical
        .getFormat()
        .isEmpty());
    assertTrue(technical
        .getFormat()
        .contains("text/html"));
    assertTrue(technical
        .getFormat()
        .contains("image/jpeg"));
    assertTrue(technical
        .getFormat()
        .contains("application/x-javascript"));
    assertTrue(technical
        .getFormat()
        .contains("image/png"));
    assertTrue(technical
        .getFormat()
        .contains("text/css"));

    // Test size
    assertNotNull(technical.getSize());
    assertEquals(516096, technical.getSize());

    // Test location
    assertNotNull(technical.getLocation());
    assertFalse(technical
        .getLocation()
        .isEmpty());
    assertEquals("http://www.scorm.com", technical
        .getLocation()
        .get(0));

    // Test requirement
    assertNotNull(technical.getRequirements());
    assertFalse(technical
        .getRequirements()
        .isEmpty());

    Requirement requirement = technical
        .getRequirements()
        .get(0);
    assertNotNull(requirement);
    assertNotNull(requirement.getOrCompositeList());
    assertFalse(requirement
        .getOrCompositeList()
        .isEmpty());

    OrComposite orComposite = requirement
        .getOrCompositeList()
        .get(0);
    assertNotNull(orComposite);
    assertNotNull(orComposite.getType());
    assertEquals("LOMv1.0", orComposite
        .getType()
        .getSource());
    assertEquals("BROWSER", orComposite
        .getType()
        .getValue()
        .toString());

    assertNotNull(orComposite.getName());
    assertEquals("LOMv1.0", orComposite
        .getName()
        .getSource());
    assertEquals(MS_INTERNET_EXPLORER, orComposite
        .getName()
        .getValue());

    assertEquals("5.0", orComposite.getMinimumVersion());
    assertEquals("7.0", orComposite.getMaximumVersion());

    // Test installationRemarks
    assertNotNull(technical.getInstallationRemarks());
    assertNotNull(technical
        .getInstallationRemarks()
        .getLangString());
    assertEquals("en-us", technical
        .getInstallationRemarks()
        .getLangString()
        .getLanguage());
    assertEquals("Nothing to it, just put the file out there.",
        technical
            .getInstallationRemarks()
            .getLangString()
            .getValue());

    // Test otherPlatformRequirements
    assertNotNull(technical.getOtherPlatformRequirements());
    assertNotNull(technical
        .getOtherPlatformRequirements()
        .getLangString());
    assertEquals("en-us", technical
        .getOtherPlatformRequirements()
        .getLangString()
        .getLanguage());
    assertTrue(technical
        .getOtherPlatformRequirements()
        .getLangString()
        .getValue()
        .contains("This course has been tested in Firefox and IE"));

    // Test duration
    assertNotNull(technical.getDuration());
    assertNotNull(technical
        .getDuration()
        .getDuration());
    assertEquals(Duration.ofMinutes(10), technical
        .getDuration()
        .getDuration());
    assertNotNull(technical
        .getDuration()
        .getDescription());
    assertNotNull(technical
        .getDuration()
        .getDescription()
        .getLangString());
    assertEquals("en-us", technical
        .getDuration()
        .getDescription()
        .getLangString()
        .getLanguage());
    assertTrue(technical
        .getDuration()
        .getDescription()
        .getLangString()
        .getValue()
        .contains("This field isn't really applicable to the course as a whole"));
  }

  /**
   * Tests the deserialization of a Technical object from an inline LOM in the manifest.
   */
  @Test
  void testDeserializeTechnicalFromManifest() throws Exception {
    // Given
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";

    // First, try to parse the metadata_course.xml file which we know has educational data
    File metadataFile = new File(modulePath + "/metadata_course.xml");

    // Use the same approach as in testDeserializeEducational
    LOM lom = xmlMapper.readValue(metadataFile, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getTechnical());

    Technical technical = lom.getTechnical();

    // Test format
    assertEquals("text/html", technical
        .getFormat()
        .get(0));

    // Test size
    assertEquals(516096, technical.getSize());

    // Test location
    assertEquals("http://www.scorm.com", technical
        .getLocation()
        .get(0));

    var requirement = technical
        .getRequirements()
        .get(0);
    assertNotNull(requirement);
    assertNotNull(requirement.getOrCompositeList());
    assertFalse(requirement
        .getOrCompositeList()
        .isEmpty());

    var orComposite = requirement
        .getOrCompositeList()
        .get(0);
    assertNotNull(orComposite);
    assertNotNull(orComposite.getType());
    assertEquals("LOMv1.0", orComposite
        .getType()
        .getSource());
    assertEquals(BROWSER, orComposite
        .getType()
        .getValue());
    assertNotNull(orComposite.getName());
    assertEquals("LOMv1.0", orComposite
        .getName()
        .getSource());
    assertEquals(MS_INTERNET_EXPLORER, orComposite
        .getName()
        .getValue());
    assertEquals("5.0", orComposite.getMinimumVersion());
    assertEquals("7.0", orComposite.getMaximumVersion());

    // Test installationRemarks
    assertNotNull(technical.getInstallationRemarks());
    assertNotNull(technical
        .getInstallationRemarks()
        .getLangString());
    assertEquals("en-us", technical
        .getInstallationRemarks()
        .getLangString()
        .getLanguage());
    assertEquals("Nothing to it, just put the file out there.",
        technical
            .getInstallationRemarks()
            .getLangString()
            .getValue());

    // Test otherPlatformRequirements
    assertNotNull(technical.getOtherPlatformRequirements());
    assertNotNull(technical
        .getOtherPlatformRequirements()
        .getLangString());
    assertEquals("en-us", technical
        .getOtherPlatformRequirements()
        .getLangString()
        .getLanguage());
    assertTrue(technical
        .getOtherPlatformRequirements()
        .getLangString()
        .getValue()
        .contains("This course has been tested in Firefox and IE"));

    // Test duration
    assertNotNull(technical.getDuration());
    assertNotNull(technical
        .getDuration()
        .getDuration());
    assertEquals(Duration.ofMinutes(10), technical
        .getDuration()
        .getDuration());
    assertNotNull(technical
        .getDuration()
        .getDescription());
    assertNotNull(technical
        .getDuration()
        .getDescription()
        .getLangString());
    assertEquals("en-us", technical
        .getDuration()
        .getDescription()
        .getLangString()
        .getLanguage());
    assertTrue(technical
        .getDuration()
        .getDescription()
        .getLangString()
        .getValue()
        .contains("This field isn't really applicable to the course as a whole"));
  }
}
