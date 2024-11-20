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

package dev.jcputney.elearning.parser.output.cmi5;

import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.List;
import java.util.Map;

/**
 * Represents metadata for a cmi5 module, including cmi5-specific fields such as prerequisites,
 * dependencies, assignable units, custom data, and xAPI support.
 * <p>
 * This class extends the base {@link ModuleMetadata} class to provide cmi5-specific information for
 * LMS tracking and reporting, including a list of Assignable Units (AUs) and custom data fields.
 * </p>
 */
public class Cmi5Metadata extends ModuleMetadata {

  private final List<AssignableUnit> assignableUnits;
  private final Map<String, String> customData;
  private final String cmi5Version;
  private final List<String> prerequisites;
  private final List<String> dependencies;

  /**
   * Private constructor used by the Builder to create a Cmi5Metadata instance.
   *
   * @param builder The builder instance used to initialize Cmi5Metadata fields.
   */
  private Cmi5Metadata(Builder builder) {
    super(builder);
    this.assignableUnits = builder.assignableUnits;
    this.customData = builder.customData;
    this.cmi5Version = builder.cmi5Version;
    this.prerequisites = builder.prerequisites;
    this.dependencies = builder.dependencies;
  }

  /**
   * Gets the list of assignable units (AUs) in this cmi5 module.
   *
   * @return A list of {@link AssignableUnit} objects, or an empty list if none are specified.
   */
  public List<AssignableUnit> getAssignableUnits() {
    return assignableUnits;
  }

  /**
   * Gets the custom metadata associated with this cmi5 module.
   *
   * @return A map of custom data, or an empty map if no custom data is available.
   */
  public Map<String, String> getCustomData() {
    return customData;
  }

  /**
   * Gets the version of the cmi5 specification used by this module.
   *
   * @return The cmi5 version string.
   */
  public String getCmi5Version() {
    return cmi5Version;
  }

  /**
   * Gets the list of prerequisites for this cmi5 module, indicating which modules or conditions
   * must be completed before this module can be accessed.
   *
   * @return A list of prerequisites as strings, or an empty list if there are no prerequisites.
   */
  public List<String> getPrerequisites() {
    return prerequisites;
  }

  /**
   * Gets the list of dependencies for this cmi5 module, indicating resources or files required for
   * this module to function properly.
   *
   * @return A list of dependencies as strings, or an empty list if there are no dependencies.
   */
  public List<String> getDependencies() {
    return dependencies;
  }

  /**
   * Builder for constructing instances of {@link Cmi5Metadata}.
   * <p>
   * This builder provides methods for setting cmi5-specific fields, in addition to the core fields
   * inherited from {@link ModuleMetadata}.
   * </p>
   */
  public static class Builder extends ModuleMetadata.Builder<Builder> {

    private List<AssignableUnit> assignableUnits = List.of(); // Default to an empty list
    private Map<String, String> customData = Map.of(); // Default to an empty map
    private String cmi5Version;
    private List<String> prerequisites = List.of(); // Default to an empty list
    private List<String> dependencies = List.of(); // Default to an empty list

    /**
     * Sets the list of assignable units for the cmi5 module.
     *
     * @param assignableUnits A list of {@link AssignableUnit} objects.
     * @return The builder instance.
     */
    public Builder assignableUnits(List<AssignableUnit> assignableUnits) {
      this.assignableUnits = assignableUnits != null ? assignableUnits : List.of();
      return this;
    }

    /**
     * Sets the custom data for the cmi5 module.
     *
     * @param customData A map of custom metadata.
     * @return The builder instance.
     */
    public Builder customData(Map<String, String> customData) {
      this.customData = customData != null ? customData : Map.of();
      return this;
    }

    /**
     * Sets the cmi5 version for the module.
     *
     * @param cmi5Version The cmi5 version string.
     * @return The builder instance.
     */
    public Builder cmi5Version(String cmi5Version) {
      this.cmi5Version = cmi5Version;
      return this;
    }

    /**
     * Sets the prerequisites for the cmi5 module, indicating modules or conditions that must be
     * completed before this module can be accessed.
     *
     * @param prerequisites A list of prerequisites as strings.
     * @return The builder instance.
     */
    public Builder prerequisites(List<String> prerequisites) {
      this.prerequisites = prerequisites != null ? prerequisites : List.of();
      return this;
    }

    /**
     * Sets the dependencies for the cmi5 module, indicating resources or files required for this
     * module to function properly.
     *
     * @param dependencies A list of dependencies as strings.
     * @return The builder instance.
     */
    public Builder dependencies(List<String> dependencies) {
      this.dependencies = dependencies != null ? dependencies : List.of();
      return this;
    }

    /**
     * Builds and returns a {@link Cmi5Metadata} instance with the specified properties.
     *
     * @return A new instance of {@link Cmi5Metadata}.
     */
    @Override
    public Cmi5Metadata build() {
      return new Cmi5Metadata(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}