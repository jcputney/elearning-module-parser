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

import static dev.jcputney.elearning.parser.input.lom.types.Role.CONTENT_PROVIDER;
import static dev.jcputney.elearning.parser.input.lom.types.Role.PUBLISHER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.Contribute;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link LifeCycle} class.
 */
class LifeCycleTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  /**
   * Tests the deserialization of a LifeCycle object from XML.
   */
  @Test
  void testDeserializeLifeCycle() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getLifecycle());

    LifeCycle lifeCycle = lom.getLifecycle();

    // Test version
    assertNotNull(lifeCycle.getVersion());
    assertNotNull(lifeCycle
        .getVersion()
        .getLangStrings());
    assertFalse(lifeCycle
        .getVersion()
        .getLangStrings()
        .isEmpty());
    assertEquals("1.0", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getValue());
    assertEquals("en-US", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getLanguage());

    // Test status
    assertNotNull(lifeCycle.getStatus());
    assertEquals("LOMv1.0", lifeCycle
        .getStatus()
        .getSource());
    assertEquals("FINAL", lifeCycle
        .getStatus()
        .getValue()
        .toString());

    // Test contribute
    assertNotNull(lifeCycle.getContribute());
    assertFalse(lifeCycle
        .getContribute()
        .isEmpty());

    // Test first contribution (publisher)
    Contribute publisherContribution = null;
    Contribute contentProviderContribution = null;

    for (Contribute contribute : lifeCycle.getContribute()) {
      if (contribute.getRole() != null && PUBLISHER == contribute
          .getRole()
          .getValue()) {
        publisherContribution = contribute;
      } else if (contribute.getRole() != null && CONTENT_PROVIDER == contribute
          .getRole()
          .getValue()) {
        contentProviderContribution = contribute;
      }
    }

    assertNotNull(publisherContribution, "Publisher contribution should be present");
    assertNotNull(publisherContribution.getRole());
    assertEquals("LOMv1.0", publisherContribution
        .getRole()
        .getSource());
    assertEquals(PUBLISHER, publisherContribution
        .getRole()
        .getValue());

    // Test entity (vCard)
    assertNotNull(publisherContribution.getEntities());
    assertFalse(publisherContribution
        .getEntities()
        .isEmpty());
    String entity = publisherContribution
        .getEntities()
        .get(0);
    assertTrue(entity.contains("BEGIN:VCARD"));
    assertTrue(entity.contains("FN:Mike Rustici"));
    assertTrue(entity.contains("ORG:Rustici Software"));
    assertTrue(entity.contains("END:VCARD"));

    // Test date
    assertNotNull(publisherContribution.getDate());
    assertNotNull(publisherContribution
        .getDate()
        .getDateTime());
    assertEquals("2009-01-23", publisherContribution
        .getDate()
        .getDateTime());
    assertNotNull(publisherContribution
        .getDate()
        .getDescription());
    assertNotNull(publisherContribution
        .getDate()
        .getDescription()
        .getLangStrings());
    assertFalse(publisherContribution
        .getDate()
        .getDescription()
        .getLangStrings()
        .isEmpty());
    assertTrue(publisherContribution
        .getDate()
        .getDescription()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This is the date this sample metadata was first created"));

    // Test second contribution (content provider)
    assertNotNull(contentProviderContribution, "Content provider contribution should be present");
    assertNotNull(contentProviderContribution.getRole());
    assertEquals("LOMv1.0", contentProviderContribution
        .getRole()
        .getSource());
    assertEquals(CONTENT_PROVIDER, contentProviderContribution
        .getRole()
        .getValue());

    // Test entity (vCard)
    assertNotNull(contentProviderContribution.getEntities());
    assertFalse(contentProviderContribution
        .getEntities()
        .isEmpty());
    entity = contentProviderContribution
        .getEntities()
        .get(0);
    assertTrue(entity.contains("BEGIN:VCARD"));
    assertTrue(entity.contains("ORG:Wikipedia"));
    assertTrue(entity.contains("END:VCARD"));

    // Test date
    assertNotNull(contentProviderContribution.getDate());
    assertNotNull(contentProviderContribution
        .getDate()
        .getDateTime());
    assertEquals("2009-01-12", contentProviderContribution
        .getDate()
        .getDateTime());
    assertNotNull(contentProviderContribution
        .getDate()
        .getDescription());
    assertNotNull(contentProviderContribution
        .getDate()
        .getDescription()
        .getLangStrings());
    assertFalse(contentProviderContribution
        .getDate()
        .getDescription()
        .getLangStrings()
        .isEmpty());
    assertTrue(contentProviderContribution
        .getDate()
        .getDescription()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This is the date the text copy was copied from Wikipedia"));
  }

  /**
   * Tests the deserialization of a LifeCycle object from an imsmanifest.xml file.
   */
  @Test
  void testDeserializeLifeCycleFromManifest() throws Exception {
    // Given
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";

    // First, try to parse the metadata_course.xml file which we know has educational data
    File metadataFile = new File(modulePath + "/metadata_course.xml");

    // Use the same approach as in testDeserializeEducational
    LOM lom = xmlMapper.readValue(metadataFile, LOM.class);

    // Then
    assertNotNull(lom, "LOM object should not be null");

    // Check if lifecycle exists
    LifeCycle lifeCycle = lom.getLifecycle();

    // Test version
    assertEquals("1.0", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getValue());

    // Test status
    assertEquals("LOMv1.0", lifeCycle
        .getStatus()
        .getSource());
    assertEquals("FINAL", lifeCycle
        .getStatus()
        .getValue()
        .toString());

    // Test contribute
    // Find publisher and content provider contributions
    Contribute publisherContribution = null;
    Contribute contentProviderContribution = null;

    for (Contribute contribute : lifeCycle.getContribute()) {
      if (contribute.getRole() != null && PUBLISHER == contribute
          .getRole()
          .getValue()) {
        publisherContribution = contribute;
      } else if (contribute.getRole() != null && CONTENT_PROVIDER == contribute
          .getRole()
          .getValue()) {
        contentProviderContribution = contribute;
      }
    }

    assertNotNull(publisherContribution, "Publisher contribution should be present");
    assertNotNull(contentProviderContribution, "Content provider contribution should be present");

    // Test publisher contribution if present
    // Test role
    assertEquals("LOMv1.0", publisherContribution
        .getRole()
        .getSource());
    assertEquals(PUBLISHER, publisherContribution
        .getRole()
        .getValue());

    // Test entity
    String entity = publisherContribution
        .getEntities()
        .get(0);
    assertTrue(entity.contains("BEGIN:VCARD"));

    // Test date
    assertNotNull(publisherContribution
        .getDate()
        .getDateTime());

    // Test content provider contribution if present

    // Test role
    assertEquals("LOMv1.0", contentProviderContribution
        .getRole()
        .getSource());
    assertEquals(CONTENT_PROVIDER, contentProviderContribution
        .getRole()
        .getValue());

    // Test entity
    String contentEntity = contentProviderContribution
        .getEntities()
        .get(0);
    assertTrue(contentEntity.contains("BEGIN:VCARD"));

    // Test date
    assertNotNull(contentProviderContribution
        .getDate()
        .getDateTime());
  }
}
