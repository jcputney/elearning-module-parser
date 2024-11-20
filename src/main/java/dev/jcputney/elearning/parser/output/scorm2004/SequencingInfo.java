package dev.jcputney.elearning.parser.output.scorm2004;

import java.util.List;

/**
 * Represents generic sequencing information for eLearning modules, encapsulating rules, navigation
 * flow, and objectives to support multiple eLearning standards (SCORM, xAPI, AICC, etc.).
 * <p>
 * This class is designed to be flexible, allowing for various rule types, objective tracking, and
 * navigation configurations that may apply to different module types beyond SCORM.
 * </p>
 */
public class SequencingInfo {

  private final List<String> preConditionRules;
  private final List<String> postConditionRules;
  private final List<String> exitConditionRules;
  private final List<String> objectiveIds;
  private final boolean flow;
  private final boolean forwardOnly;

  /**
   * Constructs a SequencingInfo object using the Builder pattern.
   * <p>
   * This constructor is private; instances should be created using {@link SequencingInfo.Builder}.
   * </p>
   *
   * @param builder The builder instance with all fields set.
   */
  private SequencingInfo(Builder builder) {
    this.preConditionRules = builder.preConditionRules;
    this.postConditionRules = builder.postConditionRules;
    this.exitConditionRules = builder.exitConditionRules;
    this.objectiveIds = builder.objectiveIds;
    this.flow = builder.flow;
    this.forwardOnly = builder.forwardOnly;
  }

  /**
   * Retrieves the list of pre-condition rules. These rules determine if an activity should begin
   * based on the learner's prior progress or conditions.
   *
   * @return List of pre-condition rule identifiers or expressions.
   */
  public List<String> getPreConditionRules() {
    return preConditionRules;
  }

  /**
   * Retrieves the list of post-condition rules. These rules specify conditions that apply once an
   * activity is completed, potentially affecting the next steps in sequencing.
   *
   * @return List of post-condition rule identifiers or expressions.
   */
  public List<String> getPostConditionRules() {
    return postConditionRules;
  }

  /**
   * Retrieves the list of exit-condition rules. These rules define conditions that apply when an
   * activity ends, controlling what occurs upon exiting or leaving the activity.
   *
   * @return List of exit-condition rule identifiers or expressions.
   */
  public List<String> getExitConditionRules() {
    return exitConditionRules;
  }

  /**
   * Retrieves the list of objective IDs associated with this sequencing information. Objectives can
   * be used to track learning goals or completion metrics.
   *
   * @return List of objective identifiers.
   */
  public List<String> getObjectiveIds() {
    return objectiveIds;
  }

  /**
   * Indicates if the module supports linear flow through activities. If true, activities are
   * presented in a defined order; if false, learners may access activities in a non-linear
   * fashion.
   *
   * @return True if linear flow is enabled; false otherwise.
   */
  public boolean isFlow() {
    return flow;
  }

  /**
   * Indicates if the module allows only forward navigation through content. If true, learners
   * cannot revisit previous content once they progress to the next activity.
   *
   * @return True if only forward navigation is allowed; false otherwise.
   */
  public boolean isForwardOnly() {
    return forwardOnly;
  }

  /**
   * Builder class for constructing instances of SequencingInfo.
   * <p>
   * This builder pattern allows for flexible construction of SequencingInfo, enabling different
   * module types to selectively specify rules, objectives, and navigation settings.
   * </p>
   */
  public static class Builder {

    private List<String> preConditionRules;
    private List<String> postConditionRules;
    private List<String> exitConditionRules;
    private List<String> objectiveIds;
    private boolean flow;
    private boolean forwardOnly;

    /**
     * Sets the pre-condition rules for the sequence, defining prerequisites for starting an
     * activity.
     *
     * @param rules List of pre-condition rules as identifiers or expressions.
     * @return The Builder instance, for method chaining.
     */
    public Builder preConditionRules(List<String> rules) {
      this.preConditionRules = rules;
      return this;
    }

    /**
     * Sets the post-condition rules for the sequence, specifying conditions that apply after
     * activity completion.
     *
     * @param rules List of post-condition rules as identifiers or expressions.
     * @return The Builder instance, for method chaining.
     */
    public Builder postConditionRules(List<String> rules) {
      this.postConditionRules = rules;
      return this;
    }

    /**
     * Sets the exit-condition rules for the sequence, defining conditions that apply when an
     * activity ends.
     *
     * @param rules List of exit-condition rules as identifiers or expressions.
     * @return The Builder instance, for method chaining.
     */
    public Builder exitConditionRules(List<String> rules) {
      this.exitConditionRules = rules;
      return this;
    }

    /**
     * Sets the objective identifiers associated with this sequence, which are used for tracking
     * progress toward specific learning goals or outcomes.
     *
     * @param objectiveIds List of objective identifiers.
     * @return The Builder instance, for method chaining.
     */
    public Builder objectiveIds(List<String> objectiveIds) {
      this.objectiveIds = objectiveIds;
      return this;
    }

    /**
     * Sets the flow property for this sequence, defining if content is presented in a linear
     * order.
     *
     * @param flow True if linear flow is enabled; false otherwise.
     * @return The Builder instance, for method chaining.
     */
    public Builder flow(boolean flow) {
      this.flow = flow;
      return this;
    }

    /**
     * Sets the forward-only navigation control for this sequence, limiting learners to only forward
     * progression.
     *
     * @param forwardOnly True if only forward navigation is allowed; false otherwise.
     * @return The Builder instance, for method chaining.
     */
    public Builder forwardOnly(boolean forwardOnly) {
      this.forwardOnly = forwardOnly;
      return this;
    }

    /**
     * Builds and returns a SequencingInfo instance with the specified settings.
     *
     * @return A new SequencingInfo instance.
     */
    public SequencingInfo build() {
      return new SequencingInfo(this);
    }
  }
}
