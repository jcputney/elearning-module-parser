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

package dev.jcputney.elearning.parser.output.metadata.cmi5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Block;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.input.cmi5.ObjectivesList;
import dev.jcputney.elearning.parser.input.xapi.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.Objective;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Cmi5Metadata} to ensure proper creation and metadata extraction from cmi5
 * manifests.
 */
class Cmi5MetadataTest {

  /**
   * Tests creating a Cmi5Metadata instance with a manifest that has assignable units.
   */
  @Test
  void create_withAssignableUnits_addsAssignableUnitMetadata() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    AU au1 = createAU("au1", "url1");
    AU au2 = createAU("au2", "url2");
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);
    manifest.setAssignableUnits(List.of(au1, au2));

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, true);

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
    assertEquals(manifest, metadata.getManifest());

    List<String> assignableUnitIds = metadata.getAssignableUnitIds();
    assertEquals(2, assignableUnitIds.size());
    assertEquals("au1", assignableUnitIds.get(0));
    assertEquals("au2", assignableUnitIds.get(1));

    List<String> assignableUnitUrls = metadata.getAssignableUnitUrls();
    assertEquals(2, assignableUnitUrls.size());
    assertEquals("url1", assignableUnitUrls.get(0));
    assertEquals("url2", assignableUnitUrls.get(1));
  }

  /**
   * Tests creating a Cmi5Metadata instance with a manifest that has blocks.
   */
  @Test
  void create_withBlocks_addsBlockMetadata() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    Block block1 = createBlock("block1");
    Block block2 = createBlock("block2");
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);
    manifest.setBlocks(List.of(block1, block2));

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, false);

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());
    assertFalse(metadata.isXapiEnabled());
    assertEquals(manifest, metadata.getManifest());

    List<String> blockIds = metadata.getBlockIds();
    assertEquals(2, blockIds.size());
    assertEquals("block1", blockIds.get(0));
    assertEquals("block2", blockIds.get(1));
  }

  /**
   * Tests creating a Cmi5Metadata instance with a manifest that has objectives.
   */
  @Test
  void create_withObjectives_addsObjectiveMetadata() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    Objective objective1 = createObjective("objective1");
    Objective objective2 = createObjective("objective2");
    ObjectivesList objectives = new ObjectivesList(List.of(objective1, objective2));
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);
    manifest.setObjectives(objectives);

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, true);

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
    assertEquals(manifest, metadata.getManifest());

    List<String> objectiveIds = metadata.getObjectiveIds();
    assertEquals(2, objectiveIds.size());
    assertEquals("objective1", objectiveIds.get(0));
    assertEquals("objective2", objectiveIds.get(1));
  }

  /**
   * Tests creating a Cmi5Metadata instance with a manifest that has no assignable units, blocks, or
   * objectives.
   */
  @Test
  void create_withNoAssignableUnitsBlocksOrObjectives_addsNoMetadata() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, true);

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
    assertEquals(manifest, metadata.getManifest());
  }

  /**
   * Tests creating a Cmi5Metadata instance with a manifest that has empty lists for assignable
   * units, blocks, and objectives.
   */
  @Test
  void create_withEmptyLists_addsNoMetadata() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    ObjectivesList objectives = new ObjectivesList(Collections.emptyList());
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);
    manifest.setAssignableUnits(Collections.emptyList());
    manifest.setBlocks(Collections.emptyList());
    manifest.setObjectives(objectives);

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, true);

    // Assert
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
    assertEquals(manifest, metadata.getManifest());

    // Verify no metadata was added
    assertTrue(metadata
        .getAssignableUnitIds()
        .isEmpty());
    assertTrue(metadata
        .getAssignableUnitUrls()
        .isEmpty());
    assertTrue(metadata
        .getBlockIds()
        .isEmpty());
    assertTrue(metadata
        .getObjectiveIds()
        .isEmpty());
  }

  /**
   * Creates a simple TextType with a single language string.
   *
   * @param value the text value
   * @return a TextType with the given value
   */
  private TextType createTextType(String value) {
    LangString langString = new LangString(value, "en-US");
    return new TextType(List.of(langString));
  }

  /**
   * Creates a simple Course with the given ID, title, and description.
   *
   * @param id the course ID
   * @param title the course title
   * @param description the course description
   * @return a Course with the given values
   */
  private Course createCourse(String id, String title, String description) {
    Course course = new Course();
    course.setId(id);
    course.setTitle(createTextType(title));
    course.setDescription(createTextType(description));
    return course;
  }

  /**
   * Creates a simple AU with the given ID and URL.
   *
   * @param id the AU ID
   * @param url the AU URL
   * @return an AU with the given values
   */
  private AU createAU(String id, String url) {
    AU au = new AU();
    au.setId(id);
    au.setUrl(url);
    return au;
  }

  /**
   * Creates a simple Block with the given ID.
   *
   * @param id the block ID
   * @return a Block with the given ID
   */
  private Block createBlock(String id) {
    Block block = new Block();
    block.setId(id);
    return block;
  }

  /**
   * Creates a simple Objective with the given ID.
   *
   * @param id the objective ID
   * @return an Objective with the given ID
   */
  private Objective createObjective(String id) {
    Objective objective = new Objective();
    objective.setId(id);
    return objective;
  }

  /**
   * Tests that hasMultipleLaunchableUnits() always returns false for cmi5,
   * even with no AUs.
   */
  @Test
  void hasMultipleLaunchableUnits_withNoAUs_returnsFalse() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, false);

    // Assert
    assertFalse(metadata.hasMultipleLaunchableUnits());
  }

  /**
   * Tests that hasMultipleLaunchableUnits() always returns false for cmi5,
   * even with a single AU.
   */
  @Test
  void hasMultipleLaunchableUnits_withSingleAU_returnsFalse() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    AU au = createAU("au1", "url1");
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);
    manifest.setAssignableUnits(List.of(au));

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, false);

    // Assert
    assertFalse(metadata.hasMultipleLaunchableUnits());
  }

  /**
   * Tests that hasMultipleLaunchableUnits() always returns false for cmi5,
   * even with multiple AUs. This is because cmi5 is designed as a single-launch
   * standard from a navigation perspective.
   */
  @Test
  void hasMultipleLaunchableUnits_withMultipleAUs_returnsFalse() {
    // Arrange
    Course course = createCourse("course-id", "Test Course", "Test Description");
    AU au1 = createAU("au1", "url1");
    AU au2 = createAU("au2", "url2");
    AU au3 = createAU("au3", "url3");
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);
    manifest.setAssignableUnits(List.of(au1, au2, au3));

    // Act
    Cmi5Metadata metadata = Cmi5Metadata.create(manifest, false);

    // Assert
    assertFalse(metadata.hasMultipleLaunchableUnits());
  }
}
