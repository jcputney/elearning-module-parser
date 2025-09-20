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
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a node in the SCORM 2004 Activity Tree.
 * <p>
 * An ActivityNode can represent either an organization or an item in the SCORM 2004 manifest. It
 * contains references to its parent node and children, as well as the sequencing information
 * associated with the node.
 * </p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ActivityNode implements Serializable {

  /**
   * The unique identifier for this activity node.
   */
  private String identifier;

  /**
   * The title or name of this activity node.
   */
  private String title;

  /**
   * The parent node of this activity node in the activity tree.
   */
  private ActivityNode parent;

  /**
   * The list of child nodes of this activity node.
   */
  private List<ActivityNode> children = new ArrayList<>();

  /**
   * The sequencing information associated with this activity node.
   */
  private Sequencing sequencing;

  /**
   * Indicates whether this node is a leaf node (has no children).
   */
  private boolean isLeaf;

  /**
   * The identifier of the resource associated with this activity node.
   */
  private String resourceIdentifier;

  /**
   * Indicates whether this activity node is visible in the activity tree.
   */
  private boolean isVisible;

  public ActivityNode() {
  }

  /**
   * Constructor for ActivityNode.
   *
   * @param identifier The identifier of the node
   * @param title The title of the node
   * @param parent The parent node
   * @param sequencing The sequencing information
   * @param isLeaf Whether this node is a leaf node
   * @param resourceIdentifier The identifier of the associated resource
   * @param isVisible Whether this node is visible
   */
  private ActivityNode(String identifier, String title, ActivityNode parent, Sequencing sequencing,
      boolean isLeaf, String resourceIdentifier, boolean isVisible) {
    this.identifier = identifier;
    this.title = title;
    this.parent = parent;
    this.sequencing = sequencing;
    this.isLeaf = isLeaf;
    this.resourceIdentifier = resourceIdentifier;
    this.isVisible = isVisible;
  }

  /**
   * Creates an ActivityNode from a Scorm2004Organization.
   *
   * @param organization The organization to create the node from
   * @return The created ActivityNode
   */
  public static ActivityNode fromOrganization(Scorm2004Organization organization) {
    return new ActivityNode(
        organization.getIdentifier(),
        organization.getTitle(),
        null,
        organization.getSequencing(),
        false,
        null,
        true
    );
  }

  /**
   * Creates an ActivityNode from a Scorm2004Item.
   *
   * @param item The item to create the node from
   * @param parent The parent node
   * @return The created ActivityNode
   */
  public static ActivityNode fromItem(Scorm2004Item item, ActivityNode parent) {
    boolean isLeaf = item.getItems() == null || item
        .getItems()
        .isEmpty();
    return new ActivityNode(
        item.getIdentifier(),
        item.getTitle(),
        parent,
        item.getSequencing(),
        isLeaf,
        item.getIdentifierRef(),
        item.isVisible()
    );
  }

  /**
   * Adds a child node to this node.
   *
   * @param child The child node to add
   */
  public void addChild(ActivityNode child) {
    children.add(child);
  }

  /**
   * Gets the children of this node.
   *
   * @return An unmodifiable list of child nodes
   */
  public List<ActivityNode> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public void setChildren(List<ActivityNode> children) {
    this.children = children;
  }

  /**
   * Gets the parent of this node.
   *
   * @return The parent node, or null if this is the root node
   */
  public ActivityNode getParent() {
    return parent;
  }

  public void setParent(ActivityNode parent) {
    this.parent = parent;
  }

  /**
   * Gets the sequencing information for this node.
   *
   * @return The sequencing information
   */
  public Sequencing getSequencing() {
    return sequencing;
  }

  public void setSequencing(Sequencing sequencing) {
    this.sequencing = sequencing;
  }

  /**
   * Gets the resource identifier for this node.
   *
   * @return An Optional containing the resource identifier or empty if none is defined
   */
  public String getResourceIdentifier() {
    return resourceIdentifier;
  }

  public void setResourceIdentifier(String resourceIdentifier) {
    this.resourceIdentifier = resourceIdentifier;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isLeaf() {
    return this.isLeaf;
  }

  public void setLeaf(boolean leaf) {
    isLeaf = leaf;
  }

  public boolean isVisible() {
    return this.isVisible;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ActivityNode that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isLeaf(), that.isLeaf())
        .append(isVisible(), that.isVisible())
        .append(getIdentifier(), that.getIdentifier())
        .append(getTitle(), that.getTitle())
        .append(getParent(), that.getParent())
        .append(getChildren(), that.getChildren())
        .append(getSequencing(), that.getSequencing())
        .append(getResourceIdentifier(), that.getResourceIdentifier())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getTitle())
        .append(getParent())
        .append(getChildren())
        .append(getSequencing())
        .append(isLeaf())
        .append(getResourceIdentifier())
        .append(isVisible())
        .toHashCode();
  }
}
