package dev.jcputney.elearning.parser.output;

import dev.jcputney.elearning.parser.enums.ModuleType;
import java.util.Optional;

/**
 * Represents the core metadata for an eLearning module, providing common fields shared across
 * various module types, such as SCORM, AICC, or xAPI.
 * <p>
 * Each module type can extend this class to include additional fields specific to that type.
 * </p>
 */
public abstract class ModuleMetadata {

  private final String title;
  private final String description;
  private final String version;
  private final String identifier;
  private final ModuleType moduleType;
  private final String launchUrl;
  private final boolean xapiEnabled;

  /**
   * Protected constructor for initializing ModuleMetadata via subclass builders.
   *
   * @param builder The builder instance containing values for initializing core metadata fields.
   */
  protected ModuleMetadata(Builder<?> builder) {
    this.title = builder.title;
    this.description = builder.description;
    this.version = builder.version;
    this.identifier = builder.identifier;
    this.moduleType = builder.moduleType;
    this.launchUrl = builder.launchUrl;
    this.xapiEnabled = builder.xapiEnabled;
  }

  /**
   * Gets the title of the module.
   *
   * @return The module title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets the description of the module.
   *
   * @return The module description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the version of the module.
   *
   * @return The module version.
   */
  public String getVersion() {
    return version;
  }

  /**
   * Gets the unique identifier of the module.
   *
   * @return The module identifier.
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Gets the type of the module, such as SCORM, AICC, or xAPI.
   *
   * @return The module type as an enum value.
   */
  public ModuleType getModuleType() {
    return moduleType;
  }

  /**
   * Gets whether xAPI is enabled for the module.
   *
   * @return True if xAPI is enabled, false otherwise.
   */
  public boolean isXapiEnabled() {
    return xapiEnabled;
  }

  /**
   * Gets the launch URL of the module, if specified.
   *
   * @return An Optional containing the launch URL, or empty if not specified.
   */
  public Optional<String> getLaunchUrl() {
    return Optional.ofNullable(launchUrl);
  }

  /**
   * Base builder class for constructing a ModuleMetadata object with common fields.
   * <p>
   * This builder uses a self-referential generic type to allow chaining in subclasses. Each
   * subclass should override the {@link #self()} method to return the specific builder type.
   * </p>
   *
   * @param <T> The type of the builder, enabling chaining for subclasses.
   */
  protected abstract static class Builder<T extends Builder<T>> {

    private String title;
    private String description;
    private String version;
    private String identifier;
    private ModuleType moduleType;
    private String launchUrl;
    private boolean xapiEnabled = false;

    /**
     * Sets the title of the module.
     *
     * @param title The title to set.
     * @return The builder instance.
     */
    public T title(String title) {
      this.title = title;
      return self();
    }

    /**
     * Sets the description of the module.
     *
     * @param description The description to set.
     * @return The builder instance.
     */
    public T description(String description) {
      this.description = description;
      return self();
    }

    /**
     * Sets the version of the module.
     *
     * @param version The version to set.
     * @return The builder instance.
     */
    public T version(String version) {
      this.version = version;
      return self();
    }

    /**
     * Sets the identifier of the module.
     *
     * @param identifier The identifier to set.
     * @return The builder instance.
     */
    public T identifier(String identifier) {
      this.identifier = identifier;
      return self();
    }

    /**
     * Sets the module type, such as SCORM, AICC, or xAPI.
     *
     * @param moduleType The module type to set.
     * @return The builder instance.
     */
    public T moduleType(ModuleType moduleType) {
      this.moduleType = moduleType;
      return self();
    }

    /**
     * Sets the launch URL of the module.
     *
     * @param launchUrl The launch URL to set.
     * @return The builder instance.
     */
    public T launchUrl(String launchUrl) {
      this.launchUrl = launchUrl;
      return self();
    }

    /**
     * Sets whether xAPI is enabled for the module.
     *
     * @param xapiEnabled True if xAPI is enabled, false otherwise.
     * @return The builder instance.
     */
    public T xapiEnabled(boolean xapiEnabled) {
      this.xapiEnabled = xapiEnabled;
      return self();
    }

    /**
     * Builds the ModuleMetadata instance. This method should be overridden by subclasses to return
     * the specific type of ModuleMetadata.
     *
     * @return A ModuleMetadata instance.
     */
    public abstract ModuleMetadata build();

    /**
     * Method for subclasses to return the correct builder type, enabling method chaining.
     *
     * @return The builder instance.
     */
    protected abstract T self();
  }
}