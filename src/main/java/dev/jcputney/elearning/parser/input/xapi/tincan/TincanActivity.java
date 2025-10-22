/*
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
 */

package dev.jcputney.elearning.parser.input.xapi.tincan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an activity element in a TinCan manifest.
 *
 * <p>Example XML:</p>
 * <pre>{@code
 * <activity id="http://example.com/activity/1"
 *           type="http://adlnet.gov/expapi/activities/course">
 *   <name>Course Name</name>
 *   <description lang="en-US">Course description</description>
 *   <launch lang="en-us">index.html</launch>
 * </activity>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class TincanActivity {

  /**
   * The globally unique IRI identifier for the activity (required).
   */
  @JacksonXmlProperty(isAttribute = true, localName = "id")
  private String id;

  /**
   * The activity type URI (optional).
   * Common value: "http://adlnet.gov/expapi/activities/course"
   */
  @JacksonXmlProperty(isAttribute = true, localName = "type")
  private String type;

  /**
   * The name/title of the activity (required).
   */
  @JacksonXmlProperty(localName = "name")
  private String name;

  /**
   * The description of the activity with language support (optional).
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * The launch URL with language support (required).
   */
  @JacksonXmlProperty(localName = "launch")
  private TextType launch;

  /**
   * Default no-argument constructor.
   */
  public TincanActivity() {
  }

  /**
   * Constructor with all fields.
   *
   * @param id the activity IRI
   * @param type the activity type URI
   * @param name the activity name
   * @param description the activity description
   * @param launch the launch URL
   */
  public TincanActivity(String id, String type, String name, TextType description,
      TextType launch) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.launch = launch;
  }

  /**
   * Gets the activity ID (IRI).
   *
   * @return the activity ID
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the activity ID (IRI).
   *
   * @param id the activity ID
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the activity type (IRI).
   *
   * @return the activity type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the activity type (IRI).
   *
   * @param type the activity type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets the activity name.
   *
   * @return the activity name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the activity name.
   *
   * @param name the activity name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the activity description.
   *
   * @return the activity description
   */
  public TextType getDescription() {
    return description;
  }

  /**
   * Sets the activity description.
   *
   * @param description the activity description
   */
  public void setDescription(TextType description) {
    this.description = description;
  }

  /**
   * Gets the launch URL for this activity.
   *
   * @return the launch URL
   */
  public TextType getLaunch() {
    return launch;
  }

  /**
   * Sets the launch URL for this activity.
   *
   * @param launch the launch URL
   */
  public void setLaunch(TextType launch) {
    this.launch = launch;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TincanActivity that)) {
      return false;
    }
    return new EqualsBuilder()
        .append(getId(), that.getId())
        .append(getType(), that.getType())
        .append(getName(), that.getName())
        .append(getDescription(), that.getDescription())
        .append(getLaunch(), that.getLaunch())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getId())
        .append(getType())
        .append(getName())
        .append(getDescription())
        .append(getLaunch())
        .toHashCode();
  }
}
