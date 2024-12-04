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

package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AiccCourse {

  @JsonProperty(value = "Course", required = true)
  private Course course;

  @JsonProperty(value = "Course_Behavior", required = true)
  private CourseBehavior courseBehavior;

  @JsonProperty(value = "Course_Description", required = true)
  private Map<String, String> courseDescription;

  public String getCourseDescription() {
    return courseDescription != null ? courseDescription.entrySet()
        .stream()
        .findFirst()
        .orElseThrow(() -> new ManifestParseException("Course description is empty"))
        .getKey() : null;
  }

  @Data
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class Course {

    @JsonProperty(value = "Course_Creator", required = true)
    private String courseCreator;

    @JsonProperty(value = "Course_ID", required = true)
    private String courseId;

    @JsonProperty(value = "Course_Title", required = true)
    private String courseTitle;

    @JsonProperty(value = "Course_System", required = true)
    private String courseSystem;

    @JsonProperty(value = "Level", required = true)
    private String level;

    @JsonProperty(value = "Max_Fields_CST", required = true)
    private String maxFieldsCst;

    @JsonProperty(value = "Max_Fields_ORT")
    private String maxFieldsOrt;

    @JsonProperty(value = "Total_AUs", required = true)
    private String totalAus;

    @JsonProperty(value = "Total_Blocks", required = true)
    private String totalBlocks;

    @JsonProperty(value = "Version", required = true)
    private String version;

    @JsonProperty(value = "Total_Complex_Obj")
    private String totalComplexObj;

    @JsonProperty(value = "Total_Objectives")
    private String totalObjectives;
  }

  @Data
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class CourseBehavior {

    @JsonProperty(value = "Max_Normal", required = true)
    private String maxNormal;
  }
}
