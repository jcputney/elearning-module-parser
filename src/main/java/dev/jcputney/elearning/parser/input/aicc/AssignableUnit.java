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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AssignableUnit {

  @JsonProperty(value = "System_ID", required = true)
  private String systemId;

  @JsonProperty(value = "Command_Line", required = true)
  private String commandLine;

  @JsonProperty(value = "File_Name", required = true)
  private String fileName;

  @JsonProperty(value = "Core_Vendor", required = true)
  private String coreVendor;

  @JsonProperty(value = "Type")
  private String type;

  @JsonProperty(value = "Max_Score")
  private String maxScore;

  @JsonProperty(value = "Mastery_Score")
  private String masteryScore;

  @JsonProperty(value = "Max_Time_Allowed")
  private String maxTimeAllowed;

  @JsonProperty(value = "Time_Limit_Action")
  private String timeLimitAction;

  @JsonProperty(value = "System_Vendor")
  private String systemVendor;

  @JsonProperty(value = "web_launch")
  private String webLaunch;

  @JsonProperty(value = "AU_Password")
  private String auPassword;

  @JsonIgnore
  @Setter
  private Descriptor descriptor;
}

