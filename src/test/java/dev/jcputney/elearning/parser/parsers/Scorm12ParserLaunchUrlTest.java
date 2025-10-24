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

package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import org.junit.jupiter.api.Test;

/**
 * Tests for edge cases related to launch URL determination in the SCORM 1.2 parser.
 */
class Scorm12ParserLaunchUrlTest {

  @Test
  void testLaunchUrlFromNestedItem() throws ModuleException {
    String modulePath = "src/test/resources/modules/scorm12/LaunchUrlFromNestedItem_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parseOnly();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify that the manifest was parsed correctly
    assertEquals("Launch URL From Nested Item Test", manifest.getTitle());

    // The key test: verify that the launch URL is correctly determined from the nested item
    assertEquals("content/topic1.html", manifest.getLaunchUrl());

    // Verify that the organizations were parsed correctly
    assertEquals(1, manifest
        .getOrganizations()
        .getOrganizationList()
        .size());
    assertEquals("default_org", manifest
        .getOrganizations()
        .getDefault()
        .getIdentifier());

    // Verify that the items were parsed correctly
    assertEquals(2, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());

    // Verify that the nested items were parsed correctly
    var module1 = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(0);
    assertEquals("module_1", module1.getIdentifier());
    assertEquals(1, module1
        .getItems()
        .size());

    var chapter11 = module1
        .getItems()
        .get(0);
    assertEquals("chapter_1_1", chapter11.getIdentifier());
    assertEquals(1, chapter11
        .getItems()
        .size());

    var section111 = chapter11
        .getItems()
        .get(0);
    assertEquals("section_1_1_1", section111.getIdentifier());
    assertEquals(1, section111
        .getItems()
        .size());

    var topic1111 = section111
        .getItems()
        .get(0);
    assertEquals("topic_1_1_1_1", topic1111.getIdentifier());
    assertEquals("resource_1", topic1111.getIdentifierRef());

    // Verify that the resources were parsed correctly
    assertEquals(2, manifest
        .getResources()
        .getResourceList()
        .size());
    assertEquals("content/topic1.html", manifest
        .getResources()
        .getResourceList()
        .get(0)
        .getHref());
  }

  @Test
  void testMultipleItemsWithIdentifierRef() throws ModuleException {
    String modulePath = "src/test/resources/modules/scorm12/MultipleItemsWithIdentifierRef_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parseOnly();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify that the manifest was parsed correctly
    assertEquals("Multiple Items With IdentifierRef Test", manifest.getTitle());

    // The key test: verify that the launch URL is correctly determined from the first item with identifierRef
    assertEquals("content/item1.html", manifest.getLaunchUrl());

    // Verify that the organizations were parsed correctly
    assertEquals(1, manifest
        .getOrganizations()
        .getOrganizationList()
        .size());
    assertEquals("default_org", manifest
        .getOrganizations()
        .getDefault()
        .getIdentifier());

    // Verify that the items were parsed correctly
    assertEquals(3, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());

    // Verify that the items have the correct identifierRef values
    var item1 = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(0);
    assertEquals("item_1", item1.getIdentifier());
    assertEquals("resource_1", item1.getIdentifierRef());

    var item2 = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(1);
    assertEquals("item_2", item2.getIdentifier());
    assertEquals("resource_2", item2.getIdentifierRef());

    var item3 = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(2);
    assertEquals("item_3", item3.getIdentifier());
    assertEquals("resource_3", item3.getIdentifierRef());

    // Verify that the resources were parsed correctly
    assertEquals(3, manifest
        .getResources()
        .getResourceList()
        .size());
    assertEquals("content/item1.html", manifest
        .getResources()
        .getResourceList()
        .get(0)
        .getHref());
    assertEquals("content/item2.html", manifest
        .getResources()
        .getResourceList()
        .get(1)
        .getHref());
    assertEquals("content/item3.html", manifest
        .getResources()
        .getResourceList()
        .get(2)
        .getHref());
  }

  @Test
  void testItemWithNonExistentResourceId() throws ModuleException {
    String modulePath = "src/test/resources/modules/scorm12/ItemWithNonExistentResourceId_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));

    Scorm12Metadata metadata = parser.parseOnly();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify that the manifest was parsed correctly
    assertEquals("Item With Non-Existent Resource ID Test", manifest.getTitle());

    // The key test: verify that the launch URL is correctly determined from the second item
    // since the first item references a non-existent resource
    assertEquals("content/item2.html", manifest.getLaunchUrl());

    // Verify that the organizations were parsed correctly
    assertEquals(1, manifest
        .getOrganizations()
        .getOrganizationList()
        .size());
    assertEquals("default_org", manifest
        .getOrganizations()
        .getDefault()
        .getIdentifier());

    // Verify that the items were parsed correctly
    assertEquals(2, manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .size());

    // Verify that the items have the correct identifierRef values
    var item1 = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(0);
    assertEquals("item_1", item1.getIdentifier());
    assertEquals("non_existent_resource", item1.getIdentifierRef());

    var item2 = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(1);
    assertEquals("item_2", item2.getIdentifier());
    assertEquals("resource_1", item2.getIdentifierRef());

    // Verify that the resources were parsed correctly
    assertEquals(1, manifest
        .getResources()
        .getResourceList()
        .size());
    assertEquals("content/item2.html", manifest
        .getResources()
        .getResourceList()
        .get(0)
        .getHref());
  }
}
