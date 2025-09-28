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

package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLNav;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the presentation settings for the navigation controls. This class contains additional
 * settings that may define whether the navigation interface should be shown or hidden under certain
 * conditions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Presentation implements Serializable {

  /**
   * Contains settings for the navigation interface within the LMS. This allows the LMS to control
   * which navigation components are displayed to the learner.
   */
  @JacksonXmlProperty(localName = "navigationInterface", namespace = ADLNav.NAMESPACE_URI)
  private NavigationInterface navigationInterface;

  public Presentation() {
    // no-op
  }

  public NavigationInterface getNavigationInterface() {
    return this.navigationInterface;
  }

  public void setNavigationInterface(NavigationInterface navigationInterface) {
    this.navigationInterface = navigationInterface;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Presentation that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getNavigationInterface(), that.getNavigationInterface())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getNavigationInterface())
        .toHashCode();
  }
}
