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

package dev.jcputney.elearning.parser.output.scorm12;

import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.List;
import java.util.Optional;

/**
 * Represents metadata for a SCORM 1.2 eLearning module, including SCORM 1.2-specific fields such as
 * mastery score, prerequisites, and custom data.
 * <p>
 * This class extends the base {@link ModuleMetadata} class with additional fields specific to SCORM
 * 1.2, providing metadata that describes the learning module content and requirements.
 * </p>
 */
public class Scorm12Metadata extends ModuleMetadata {

  private final Double masteryScore;
  private final String prerequisites;
  private final List<String> customData;

  private Scorm12Metadata(Builder builder) {
    super(builder);
    this.masteryScore = builder.masteryScore;
    this.prerequisites = builder.prerequisites;
    this.customData = builder.customData;
  }

  /**
   * Gets the mastery score required to complete the module.
   *
   * @return An Optional containing the mastery score, or empty if not specified.
   */
  public Optional<Double> getMasteryScore() {
    return Optional.ofNullable(masteryScore);
  }

  /**
   * Gets the prerequisites for the module, if specified.
   *
   * @return An Optional containing the prerequisites, or empty if not specified.
   */
  public Optional<String> getPrerequisites() {
    return Optional.ofNullable(prerequisites);
  }

  /**
   * Gets the list of custom data entries associated with the module.
   *
   * @return A list of custom data strings, or an empty list if none are present.
   */
  public List<String> getCustomData() {
    return customData;
  }

  /**
   * Builder for constructing instances of {@link Scorm12Metadata}.
   * <p>
   * This builder provides methods for setting SCORM 1.2-specific fields, in addition to the core
   * fields inherited from {@link ModuleMetadata}.
   * </p>
   */
  public static class Builder extends ModuleMetadata.Builder<Builder> {

    private Double masteryScore;
    private String prerequisites;
    private List<String> customData = List.of();  // Default to an empty list

    /**
     * Sets the mastery score for the module.
     *
     * @param masteryScore The mastery score required to complete the module.
     * @return The builder instance.
     */
    public Builder masteryScore(Double masteryScore) {
      this.masteryScore = masteryScore;
      return this;
    }

    /**
     * Sets the prerequisites for the module.
     *
     * @param prerequisites The prerequisites for the module.
     * @return The builder instance.
     */
    public Builder prerequisites(String prerequisites) {
      this.prerequisites = prerequisites;
      return this;
    }

    /**
     * Sets the list of custom data entries associated with the module.
     *
     * @param customData A list of custom data strings.
     * @return The builder instance.
     */
    public Builder customData(List<String> customData) {
      this.customData = customData != null ? customData : List.of();
      return this;
    }

    /**
     * Builds and returns a {@link Scorm12Metadata} instance with the specified properties.
     *
     * @return A new instance of Scorm12Metadata.
     */
    @Override
    public Scorm12Metadata build() {
      return new Scorm12Metadata(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}