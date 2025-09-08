/* Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.lom.properties.YesNoType;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SCORM 1.2 parser.
 */
public class Scorm12ParserTest {

  @Test
  void testParseScorm12Course_ContentPackagingOneFilePerSCO_SCORM12()
      throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ContentPackagingOneFilePerSCO_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - CP One File Per SCO", manifest.getTitle());
    assertEquals("Playing/Playing.html", manifest.getLaunchUrl());
    assertEquals(4, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());
    assertEquals(4, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .stream()
        .filter(item -> item.getItems() != null && !item
            .getItems()
            .isEmpty())
        .filter(item -> !item
            .getIdentifier()
            .isEmpty())
        .filter(item -> item.getIdentifierRef() == null || item
            .getIdentifierRef()
            .isEmpty())
        .filter(item -> item.getTitle() != null && !item
            .getTitle()
            .isEmpty())
        .count());
    assertEquals(19, manifest
        .getResources()
        .getResourceList()
        .size());

    // Verify that sizeOnDisk is calculated (should be greater than 0 for a real module)
    assertTrue(metadata.getSizeOnDisk() > 0, "Module size should be calculated and greater than 0");
  }

  @Test
  void testParseScorm12Course_ContentPackagingSingleSCO_SCORM12()
      throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ContentPackagingSingleSCO_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - CP Single SCO", manifest.getTitle());
    assertEquals("shared/launchpage.html", manifest.getLaunchUrl());
    assertEquals(1, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());
    assertEquals(1, manifest
        .getResources()
        .getResourceList()
        .size());
    assertEquals(39, manifest
        .getResources()
        .getResourceList()
        .get(0)
        .getFiles()
        .stream()
        .filter(file -> file.getHref() != null && !file
            .getHref()
            .isEmpty())
        .count());
  }

  @Test
  void testParseScorm12Course_RuntimeBasicCalls_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/RuntimeBasicCalls_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - Run-time Basic Calls", manifest.getTitle());
    assertEquals("shared/launchpage.html", manifest.getLaunchUrl());
    assertEquals(1, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());
    assertEquals(1, manifest
        .getResources()
        .getResourceList()
        .size());
    assertEquals(39, manifest
        .getResources()
        .getResourceList()
        .get(0)
        .getFiles()
        .stream()
        .filter(file -> file.getHref() != null && !file
            .getHref()
            .isEmpty())
        .count());
  }

  @Test
  void testParseScorm12Course_RuntimeMinimumCalls_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/RuntimeMinimumCalls_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - Minimum Run-time Calls", manifest.getTitle());
    assertEquals("Playing/Playing.html", manifest.getLaunchUrl());
    assertEquals(4, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());
    assertEquals(4, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .stream()
        .filter(item -> item.getItems() != null && !item
            .getItems()
            .isEmpty())
        .filter(item -> !item
            .getIdentifier()
            .isEmpty())
        .filter(item -> item.getIdentifierRef() == null || item
            .getIdentifierRef()
            .isEmpty())
        .filter(item -> item.getTitle() != null && !item
            .getTitle()
            .isEmpty())
        .count());
    assertEquals(19, manifest
        .getResources()
        .getResourceList()
        .size());
  }

  @Test
  void testParseScorm12Course_ContentPackagingWithMetadata_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ContentPackagingWithMetadata_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    LOM lom = manifest
        .getMetadata()
        .getLom();
    assertNotNull(lom);

    assertEquals("Catalog", lom
        .getGeneral()
        .getCatalogEntries()
        .get(0)
        .getCatalog());
    assertEquals(YesNoType.YES,
        lom
            .getTechnical()
            .getPackageProperties()
            .getBehavior()
            .getAlwaysFlowToFirstSco());
    assertEquals("SCORM 1.2 With Metadata", manifest.getTitle());
    assertEquals("index.html", manifest.getLaunchUrl());
    assertEquals(1, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());
    assertEquals(1, manifest
        .getResources()
        .getResourceList()
        .size());
  }

  @Test
  void testParseScorm12Course_Prerequisites_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/PrerequisitesTest_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Prerequisites Test Course", manifest.getTitle());
    assertEquals("module1.html", manifest.getLaunchUrl());

    // Verify all 4 items are parsed
    var items = manifest
        .getOrganizations()
        .getDefault()
        .getItems();
    assertEquals(4, items.size());

    // Check Module 1 - no prerequisites (empty element)
    var module1 = items.get(0);
    assertEquals("module1", module1.getIdentifier());
    assertNotNull(module1.getPrerequisites());
    assertNull(module1
        .getPrerequisites()
        .getValue()); // Empty element results in null value
    assertEquals("aicc_script", module1
        .getPrerequisites()
        .getType());
    assertEquals(80.0, module1.getMasteryScore());
    assertNotNull(module1.getMaxTimeAllowed());
    assertEquals(9000L, module1
        .getMaxTimeAllowed()
        .getSeconds()); // 2:30:00

    // Check Module 2 - requires module1
    var module2 = items.get(1);
    assertEquals("module2", module2.getIdentifier());
    assertNotNull(module2.getPrerequisites());
    assertEquals("module1", module2
        .getPrerequisites()
        .getValue());
    assertEquals("aicc_script", module2
        .getPrerequisites()
        .getType());
    assertEquals("custom_data_for_module2", module2.getDataFromLMS());

    // Check Module 3 - requires module1 AND module2
    var module3 = items.get(2);
    assertEquals("module3", module3.getIdentifier());
    assertNotNull(module3.getPrerequisites());
    assertEquals("module1 AND module2", module3
        .getPrerequisites()
        .getValue());
    assertEquals("aicc_script", module3
        .getPrerequisites()
        .getType());
    assertNotNull(module3.getTimeLimitAction());
    assertEquals("EXIT_MESSAGE", module3
        .getTimeLimitAction()
        .name());

    // Check Module 4 - complex prerequisites
    var module4 = items.get(3);
    assertEquals("module4", module4.getIdentifier());
    assertNotNull(module4.getPrerequisites());
    assertEquals("(module1 AND module2) OR module3", module4
        .getPrerequisites()
        .getValue());
    assertEquals("aicc_script", module4
        .getPrerequisites()
        .getType());
    assertEquals(90.0, module4.getMasteryScore());

    // Verify prerequisites are accessible through metadata interface
    @SuppressWarnings("unchecked")
    var prerequisitesMap = (Map<String, String>) metadata
        .getMetadata("scorm12.prerequisites")
        .orElse(null);
    assertNotNull(prerequisitesMap);
    assertEquals(3, prerequisitesMap.size()); // module2, module3, module4 have prerequisites
    assertEquals("module1", prerequisitesMap.get("module2"));
    assertEquals("module1 AND module2", prerequisitesMap.get("module3"));
    assertEquals("(module1 AND module2) OR module3", prerequisitesMap.get("module4"));

    // Verify mastery scores are accessible
    @SuppressWarnings("unchecked")
    var masteryScoresMap = (Map<String, Double>) metadata
        .getMetadata("scorm12.masteryScores")
        .orElse(null);
    assertNotNull(masteryScoresMap);
    assertEquals(2, masteryScoresMap.size()); // module1 and module4 have mastery scores
    assertEquals(80.0, masteryScoresMap.get("module1"));
    assertEquals(90.0, masteryScoresMap.get("module4"));

    // Verify custom data is accessible
    @SuppressWarnings("unchecked")
    var customDataMap = (Map<String, String>) metadata
        .getMetadata("scorm12.customData")
        .orElse(null);
    assertNotNull(customDataMap);
    assertEquals(1, customDataMap.size()); // only module2 has custom data
    assertEquals("custom_data_for_module2", customDataMap.get("module2"));
  }

  private void assertCommonFields(Scorm12Manifest manifest) {
    assertNotNull(manifest);
    assertTrue(StringUtils.isEmpty(manifest.getDescription()));

    assertEquals("ADL SCORM", manifest
        .getMetadata()
        .getSchema());
    assertEquals("1.2", manifest
        .getMetadata()
        .getSchemaVersion());

    assertTrue(manifest
        .getResources()
        .getResourceList()
        .stream()
        .allMatch(
            resource -> resource.getIdentifier() != null && !resource
                .getIdentifier()
                .isEmpty()));
    assertTrue(manifest
        .getResources()
        .getResourceList()
        .stream()
        .allMatch(resource -> resource.getScormType() != null));
    assertTrue(manifest
        .getResources()
        .getResourceList()
        .stream()
        .filter(resource -> !resource
            .getIdentifier()
            .equals("common_files"))
        .allMatch(resource -> resource.getHref() != null && !resource
            .getHref()
            .isEmpty()));
  }
}
