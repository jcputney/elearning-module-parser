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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.xapi.types.SimpleLangString;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an activity element in a TinCan manifest.
 *
 * <p>According to the official TinCan XSD schema, activities use the langstring format
 * where elements have text content and an optional {@code lang} attribute:</p>
 *
 * <p>Example XML:</p>
 * <pre>{@code
 * <activity id="http://example.com/activity/1"
 *           type="http://adlnet.gov/expapi/activities/course">
 *   <name lang="en-US">Course Name</name>
 *   <description lang="en-US">Course description</description>
 *   <launch lang="en-US">index.html</launch>
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
   * May appear multiple times for different languages.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "name")
  private List<SimpleLangString> names;

  /**
   * The description of the activity with language support (optional).
   * May appear multiple times for different languages.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "description")
  private List<SimpleLangString> descriptions;

  /**
   * The launch URL with language support (required).
   * May appear multiple times for different languages.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "launch")
  private List<SimpleLangString> launches;

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
   * @param names the activity names (may be multiple for different languages)
   * @param descriptions the activity descriptions (may be multiple for different languages)
   * @param launches the launch URLs (may be multiple for different languages)
   */
  public TincanActivity(String id, String type, List<SimpleLangString> names,
      List<SimpleLangString> descriptions, List<SimpleLangString> launches) {
    this.id = id;
    this.type = type;
    this.names = names;
    this.descriptions = descriptions;
    this.launches = launches;
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
   * Gets the list of activity names.
   *
   * @return the list of activity names
   */
  public List<SimpleLangString> getNames() {
    return names;
  }

  /**
   * Sets the list of activity names.
   *
   * @param names the list of activity names
   */
  public void setNames(List<SimpleLangString> names) {
    this.names = names;
  }

  /**
   * Gets the first activity name value (for convenience).
   *
   * @return the first name value, or null if no names
   */
  @JsonIgnore
  public String getName() {
    return names != null && !names.isEmpty() && names.get(0) != null
        ? names.get(0).getValue()
        : null;
  }

  /**
   * Gets the list of activity descriptions.
   *
   * @return the list of activity descriptions
   */
  public List<SimpleLangString> getDescriptions() {
    return descriptions;
  }

  /**
   * Sets the list of activity descriptions.
   *
   * @param descriptions the list of activity descriptions
   */
  public void setDescriptions(List<SimpleLangString> descriptions) {
    this.descriptions = descriptions;
  }

  /**
   * Gets the first activity description value (for convenience).
   *
   * @return the first description value, or null if no descriptions
   */
  @JsonIgnore
  public String getDescription() {
    return descriptions != null && !descriptions.isEmpty() && descriptions.get(0) != null
        ? descriptions.get(0).getValue()
        : null;
  }

  /**
   * Gets the list of launch URLs.
   *
   * @return the list of launch URLs
   */
  public List<SimpleLangString> getLaunches() {
    return launches;
  }

  /**
   * Sets the list of launch URLs.
   *
   * @param launches the list of launch URLs
   */
  public void setLaunches(List<SimpleLangString> launches) {
    this.launches = launches;
  }

  /**
   * Gets the first launch URL value (for convenience).
   *
   * @return the first launch URL value, or null if no launches
   */
  @JsonIgnore
  public String getLaunch() {
    return launches != null && !launches.isEmpty() && launches.get(0) != null
        ? launches.get(0).getValue()
        : null;
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
        .append(getNames(), that.getNames())
        .append(getDescriptions(), that.getDescriptions())
        .append(getLaunches(), that.getLaunches())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getId())
        .append(getType())
        .append(getNames())
        .append(getDescriptions())
        .append(getLaunches())
        .toHashCode();
  }
}
