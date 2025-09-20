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

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a SCORM 2004 Activity Tree.
 * <p>
 * An ActivityTree is a hierarchical structure of ActivityNodes that represents the organization of
 * learning activities in a SCORM 2004 content package. It is built from the manifest and provides
 * methods to navigate and manipulate the tree.
 * </p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ActivityTree implements Serializable {

  /**
   * The root node of the activity tree.
   */
  private ActivityNode root;

  /**
   * A map for a quick lookup of nodes by their identifier.
   */
  private Map<String, ActivityNode> nodeMap = new HashMap<>();

  public ActivityTree() {
  }

  /**
   * Creates an ActivityTree with the specified root node.
   *
   * @param root The root node of the tree
   */
  private ActivityTree(ActivityNode root) {
    this.root = root;
    addToNodeMap(root);
  }

  /**
   * Builds an ActivityTree from a SCORM 2004 manifest.
   *
   * @param manifest The manifest to build the tree from
   * @return The built ActivityTree, or empty if no default organization is found
   */
  public static ActivityTree buildFromManifest(Scorm2004Manifest manifest) {
    Scorm2004Organizations organizations = manifest.getOrganizations();
    if (organizations == null) {
      return null;
    }

    Scorm2004Organization defaultOrg = organizations.getDefault();
    if (defaultOrg == null) {
      return null;
    }

    ActivityNode rootNode = ActivityNode.fromOrganization(defaultOrg);
    ActivityTree tree = new ActivityTree(rootNode);

    // Process items in the default organization
    List<Scorm2004Item> items = defaultOrg.getItems();
    if (items != null) {
      for (Scorm2004Item item : items) {
        tree.processItem(item, rootNode);
      }
    }

    return tree;
  }

  /**
   * Gets a node by its identifier.
   *
   * @param identifier The identifier of the node to get
   * @return An Optional containing the node or empty if not found
   */
  public ActivityNode getNodeByIdentifier(String identifier) {
    return nodeMap.get(identifier);
  }

  /**
   * Gets all leaf nodes in the tree.
   *
   * @return A list of all leaf nodes
   */
  public List<ActivityNode> getLeafNodes() {
    List<ActivityNode> leafNodes = new ArrayList<>();
    collectLeafNodes(root, leafNodes);
    return leafNodes;
  }

  /**
   * Gets the root node of the tree.
   *
   * @return The root node
   */
  public ActivityNode getRoot() {
    return root;
  }

  public void setRoot(ActivityNode root) {
    this.root = root;
  }

  public Map<String, ActivityNode> getNodeMap() {
    return this.nodeMap;
  }

  public void setNodeMap(Map<String, ActivityNode> nodeMap) {
    this.nodeMap = nodeMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ActivityTree that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRoot(), that.getRoot())
        .append(getNodeMap(), that.getNodeMap())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRoot())
        .append(getNodeMap())
        .toHashCode();
  }

  /**
   * Recursively processes an item and its children, adding them to the tree.
   *
   * @param item The item to process
   * @param parent The parent node
   */
  private void processItem(Scorm2004Item item, ActivityNode parent) {
    ActivityNode node = ActivityNode.fromItem(item, parent);
    parent.addChild(node);
    addToNodeMap(node);

    List<Scorm2004Item> children = item.getItems();
    if (children != null) {
      for (Scorm2004Item child : children) {
        processItem(child, node);
      }
    }
  }

  /**
   * Adds a node to the node map for a quick lookup by identifier.
   *
   * @param node The node to add
   */
  private void addToNodeMap(ActivityNode node) {
    nodeMap.put(node.getIdentifier(), node);
  }

  /**
   * Recursively collects all leaf nodes in the tree.
   *
   * @param node The current node
   * @param leafNodes The list to add leaf nodes to
   */
  private void collectLeafNodes(ActivityNode node, List<ActivityNode> leafNodes) {
    if (node.isLeaf()) {
      leafNodes.add(node);
    } else {
      for (ActivityNode child : node.getChildren()) {
        collectLeafNodes(child, leafNodes);
      }
    }
  }
}