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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.xapi.types.LangString;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the root element of a TinCan/xAPI manifest file.
 *
 * <p>Implements {@link PackageManifest} to provide common manifest functionality.
 * All PackageManifest methods delegate to the first activity in the manifest.</p>
 *
 * <p>Example XML:</p>
 * <pre>{@code
 * <?xml version="1.0" encoding="utf-8" ?>
 * <tincan xmlns="http://projecttincan.com/tincan.xsd">
 *   <activities>
 *     <activity id="http://example.com/activity/1"
 *               type="http://adlnet.gov/expapi/activities/course">
 *       <name>Course Name</name>
 *       <description lang="en-US">Course description</description>
 *       <launch lang="en-us">index.html</launch>
 *     </activity>
 *   </activities>
 * </tincan>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
@JacksonXmlRootElement(localName = "tincan")
public class TincanManifest implements PackageManifest {

  /**
   * The list of activities in the manifest.
   * TinCan packages typically contain a single activity.
   */
  @JacksonXmlElementWrapper(localName = "activities")
  @JacksonXmlProperty(localName = "activity")
  private List<TincanActivity> activities;

  /**
   * Default no-argument constructor.
   */
  public TincanManifest() {
  }

  /**
   * Constructor with activities list.
   *
   * @param activities the list of activities
   */
  public TincanManifest(List<TincanActivity> activities) {
    this.activities = activities;
  }

  /**
   * Returns the title from the first activity.
   *
   * @return the activity name, or null if no activities
   */
  @Override
  @JsonIgnore
  public String getTitle() {
    return Optional.ofNullable(activities)
        .filter(list -> !list.isEmpty())
        .map(list -> list.get(0))
        .map(TincanActivity::getName)
        .orElse(null);
  }

  /**
   * Returns the description from the first activity.
   *
   * @return the activity description, or null if no activities
   */
  @Override
  @JsonIgnore
  public String getDescription() {
    return Optional.ofNullable(activities)
        .filter(list -> !list.isEmpty())
        .map(list -> list.get(0))
        .map(TincanActivity::getDescription)
        .map(desc -> desc.getStrings())
        .filter(strings -> !strings.isEmpty())
        .map(strings -> strings.get(0))
        .map(LangString::getValue)
        .orElse(null);
  }

  /**
   * Returns the launch URL from the first activity.
   *
   * @return the launch URL, or null if no activities
   */
  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    return Optional.ofNullable(activities)
        .filter(list -> !list.isEmpty())
        .map(list -> list.get(0))
        .map(TincanActivity::getLaunch)
        .map(launch -> launch.getStrings())
        .filter(strings -> !strings.isEmpty())
        .map(strings -> strings.get(0))
        .map(LangString::getValue)
        .orElse(null);
  }

  /**
   * Returns the activity ID from the first activity.
   *
   * @return the activity ID, or null if no activities
   */
  @Override
  @JsonIgnore
  public String getIdentifier() {
    return Optional.ofNullable(activities)
        .filter(list -> !list.isEmpty())
        .map(list -> list.get(0))
        .map(TincanActivity::getId)
        .orElse(null);
  }

  /**
   * Returns null as TinCan manifests do not include version information.
   *
   * @return null
   */
  @Override
  @JsonIgnore
  public String getVersion() {
    return null;
  }

  /**
   * Returns zero duration as TinCan manifests do not include duration.
   *
   * @return Duration.ZERO
   */
  @Override
  @JsonIgnore
  public Duration getDuration() {
    return Duration.ZERO;
  }

  /**
   * Gets the list of activities in this TinCan package.
   *
   * @return the list of activities
   */
  public List<TincanActivity> getActivities() {
    return activities;
  }

  /**
   * Sets the list of activities in this TinCan package.
   *
   * @param activities the list of activities
   */
  public void setActivities(List<TincanActivity> activities) {
    this.activities = activities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TincanManifest that)) {
      return false;
    }
    return new EqualsBuilder()
        .append(getActivities(), that.getActivities())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getActivities())
        .toHashCode();
  }
}
