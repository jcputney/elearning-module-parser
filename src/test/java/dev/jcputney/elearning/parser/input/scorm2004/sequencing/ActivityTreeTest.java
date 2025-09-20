/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2024-2025. Jonathan Putney
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm2004.sequencing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ActivityTree class.
 */
public class ActivityTreeTest {

  /**
   * Tests building an ActivityTree from a SCORM 2004 manifest.
   */
  @Test
  void testBuildActivityTree()
      throws IOException, javax.xml.stream.XMLStreamException, dev.jcputney.elearning.parser.exception.ModuleParsingException {
    // Parse a SCORM 2004 manifest
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Build an ActivityTree from the manifest
    ActivityTree tree = manifest.buildActivityTree();

    // Verify that the ActivityTree was built successfully
    assertTrue(tree != null, "ActivityTree should be present");

    ActivityNode root = tree.getRoot();

    // Verify the root node
    assertNotNull(root, "Root node should not be null");
    assertEquals("golf_sample_default_org", root.getIdentifier(),
        "Root node identifier should match");
    assertEquals("Golf Explained - CP One File Per SCO", root.getTitle(),
        "Root node title should match");
    assertFalse(root.isLeaf(), "Root node should not be a leaf");
    assertTrue(root.isVisible(), "Root node should be visible");

    // Verify the children
    List<ActivityNode> children = root.getChildren();
    assertFalse(children.isEmpty(), "Root node should have children");

    // Verify the first child
    ActivityNode firstChild = children.get(0);
    assertNotNull(firstChild, "First child should not be null");
    assertEquals("playing_item", firstChild.getIdentifier(), "First child identifier should match");
    assertEquals("Playing the Game", firstChild.getTitle(), "First child title should match");
    assertFalse(firstChild.isLeaf(), "First child should not be a leaf");
    assertTrue(firstChild.isVisible(), "First child should be visible");

    // Verify the first grandchild
    List<ActivityNode> grandchildren = firstChild.getChildren();
    assertFalse(grandchildren.isEmpty(), "First child should have children");
    ActivityNode firstGrandchild = grandchildren.get(0);
    assertNotNull(firstGrandchild, "First grandchild should not be null");
    assertEquals("playing_playing_item", firstGrandchild.getIdentifier(),
        "First grandchild identifier should match");
    assertEquals("How to Play", firstGrandchild.getTitle(), "First grandchild title should match");
    assertTrue(firstGrandchild.isLeaf(), "First grandchild should be a leaf");
    assertTrue(firstGrandchild.isVisible(), "First grandchild should be visible");
    assertNotNull(firstGrandchild.getResourceIdentifier(),
        "First grandchild should have a resource identifier");
    assertEquals("playing_playing_resource", firstGrandchild.getResourceIdentifier(),
        "First grandchild resource identifier should match");

    // Verify that the node map contains all nodes
    ActivityNode rootByIdOpt = tree.getNodeByIdentifier("golf_sample_default_org");
    assertNotNull(rootByIdOpt, "Root node should be in the node map");
    assertEquals(root, rootByIdOpt, "Root node from node map should match");

    ActivityNode firstChildByIdOpt = tree.getNodeByIdentifier("playing_item");
    assertNotNull(firstChildByIdOpt, "First child should be in the node map");
    assertEquals(firstChild, firstChildByIdOpt, "First child from node map should match");

    ActivityNode firstGrandchildByIdOpt = tree.getNodeByIdentifier("playing_playing_item");
    assertNotNull(firstGrandchildByIdOpt, "First grandchild should be in the node map");
    assertEquals(firstGrandchild, firstGrandchildByIdOpt,
        "First grandchild from node map should match");

    // Verify leaf nodes
    List<ActivityNode> leafNodes = tree.getLeafNodes();
    assertFalse(leafNodes.isEmpty(), "Tree should have leaf nodes");
    assertTrue(leafNodes.contains(firstGrandchild),
        "Leaf nodes should contain the first grandchild");
  }
}
