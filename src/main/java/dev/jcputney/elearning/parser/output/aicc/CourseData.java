/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.output.aicc;

import java.util.Optional;

/**
 * Represents core course data parsed from an AICC .crs file.
 */
public class CourseData {

  private final String id;
  private final String version;
  private final String credit;
  private final String timeLimitAction;

  public CourseData(String id, String version, String credit, String timeLimitAction) {
    this.id = id;
    this.version = version;
    this.credit = credit;
    this.timeLimitAction = timeLimitAction;
  }

  public String getId() {
    return id;
  }

  public Optional<String> getVersion() {
    return Optional.ofNullable(version);
  }

  public Optional<String> getCredit() {
    return Optional.ofNullable(credit);
  }

  public Optional<String> getTimeLimitAction() {
    return Optional.ofNullable(timeLimitAction);
  }
}
