package dev.jcputney.elearning.parser.input.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.adl.cp.CompletionThreshold;
import dev.jcputney.elearning.parser.input.scorm2004.adl.navigation.NavigationInterface;
import dev.jcputney.elearning.parser.input.scorm2004.adl.navigation.Presentation;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ConstrainChoiceConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.RollupConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random.RandomizationControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRules;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.AuxiliaryResources;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.ControlMode;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.DeliveryControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.LimitConditions;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingCollection;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRule;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRules;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The `SequencingUsageDetector` class provides functionalities for analyzing and detecting
 * sequencing levels and indicators in SCORM 2004 manifests and associated components.
 * <p>
 * The methods in this class evaluate various aspects of SCORM manifests, including namespaces,
 * schema locations, sequencing collections, organizations, resources, and items, to determine
 * sequencing-related features. The class operates as a utility and follows a structured approach to
 * sequencing detection, updating the provided indicators and detection states accordingly.
 * <p>
 * The primary detection process involves inspecting components of SCORM 2004 manifests and
 * interpreting the sequencing structures, yielding a result that classifies the overall sequencing
 * level and the identified indicators.
 */
public final class SequencingUsageDetector {

  /**
   * Private constructor for the SequencingUsageDetector class.
   * <p>
   * This constructor is intentionally declared as private to prevent instantiation of the class.
   * The SequencingUsageDetector class is designed to only provide static methods or utility
   * functionality, and as such, it is not intended to be instantiated.
   */
  private SequencingUsageDetector() {
  }

  /**
   * Detects the sequencing support level and relevant indicators within a given SCORM 2004
   * manifest.
   * <p>
   * This method analyzes the manifest and its components to determine various sequencing indicators
   * and assigns an appropriate sequencing level based on the presence of sequencing content or
   * structures in the manifest.
   *
   * @param manifest the SCORM 2004 manifest to be analyzed; can be null
   * @return a {@code Result} object containing the determined sequencing level and the set of
   * detected sequencing indicators
   */
  public static Result detect(Scorm2004Manifest manifest) {
    EnumSet<SequencingIndicator> indicators = EnumSet.noneOf(SequencingIndicator.class);
    if (manifest == null) {
      return new Result(SequencingLevel.NONE, indicators);
    }

    DetectionState state = new DetectionState();

    detectNamespaceIndicators(manifest, indicators);
    detectSchemaLocationIndicators(manifest.getSchemaLocation(), indicators);
    inspectSequencingCollection(manifest.getSequencingCollection(), indicators, state);
    inspectOrganizations(manifest.getOrganizations(), indicators, state);
    inspectResources(manifest.getResources(), indicators, state);

    SequencingLevel level = determineLevel(state);
    return new Result(level, indicators);
  }

  /**
   * Detects and identifies namespace indicators in the provided SCORM 2004 manifest and updates the
   * given set of sequencing indicators accordingly.
   *
   * @param manifest the SCORM 2004 manifest object containing namespace URI information
   * @param indicators the set of sequencing indicators to be updated based on detected namespaces
   */
  private static void detectNamespaceIndicators(Scorm2004Manifest manifest,
      EnumSet<SequencingIndicator> indicators) {
    if (hasText(manifest.getImsssNamespaceUri())) {
      indicators.add(SequencingIndicator.IMSSS_NAMESPACE);
    }
    if (hasText(manifest.getAdlseqNamespaceUri())) {
      indicators.add(SequencingIndicator.ADLSEQ_NAMESPACE);
    }
    if (hasText(manifest.getAdlnavNamespaceUri())) {
      indicators.add(SequencingIndicator.ADLNAV_NAMESPACE);
    }
  }

  /**
   * Detects specific schema location indicators within the provided schema location string and adds
   * them to the provided set of sequencing indicators.
   *
   * @param schemaLocation the schema location string to inspect for known indicators
   * @param indicators the set of sequencing indicators to which detected indicators will be added
   */
  private static void detectSchemaLocationIndicators(String schemaLocation,
      EnumSet<SequencingIndicator> indicators) {
    if (containsIgnoreCase(schemaLocation, "imsss")) {
      indicators.add(SequencingIndicator.SCHEMA_LOCATION_IMSSS);
    }
    if (containsIgnoreCase(schemaLocation, "adlseq")) {
      indicators.add(SequencingIndicator.SCHEMA_LOCATION_ADLSEQ);
    }
    if (containsIgnoreCase(schemaLocation, "adlnav")) {
      indicators.add(SequencingIndicator.SCHEMA_LOCATION_ADLNAV);
    }
  }

  /**
   * Inspects a given sequencing collection, evaluates its sequencing list, and updates the provided
   * indicators and state accordingly.
   *
   * @param collection the SequencingCollection object to be inspected; if null or empty, processing
   * will be skipped
   * @param indicators the set of SequencingIndicator to be updated based on the inspection
   * @param state the current DetectionState that may be affected during the inspection process
   */
  private static void inspectSequencingCollection(SequencingCollection collection,
      EnumSet<SequencingIndicator> indicators,
      DetectionState state) {
    if (collection == null || !isNotEmpty(collection.getSequencingList())) {
      return;
    }
    indicators.add(SequencingIndicator.SEQUENCING_COLLECTION);
    collection
        .getSequencingList()
        .stream()
        .filter(Objects::nonNull)
        .forEach(seq -> inspectSequencing(seq, state, indicators, false, true));
  }

  /**
   * Inspects the provided SCORM 2004 organizations object to evaluate sequencing and objectives
   * information, updating the specified indicators and detection state.
   *
   * @param organizations the SCORM 2004 organizations object to inspect; can contain a list of
   * organizations.
   * @param indicators an EnumSet to collect sequencing indicators; updated during inspection.
   * @param state the current state of detection operations, used and updated during the process.
   */
  private static void inspectOrganizations(Scorm2004Organizations organizations,
      EnumSet<SequencingIndicator> indicators,
      DetectionState state) {
    if (organizations == null || !isNotEmpty(organizations.getOrganizationList())) {
      return;
    }
    for (Scorm2004Organization organization : organizations.getOrganizationList()) {
      if (organization == null) {
        continue;
      }
      if (organization.getSequencing() != null) {
        indicators.add(SequencingIndicator.ORGANIZATION_SEQUENCING);
        inspectSequencing(organization.getSequencing(), state, indicators, true, false);
      }
      if (organization.isObjectivesGlobalToSystem()) {
        indicators.add(SequencingIndicator.ORGANIZATION_OBJECTIVES_GLOBAL);
        state.organizationObjectivesGlobal = true;
      }
      inspectItems(organization.getItems(), indicators, state);
    }
  }

  /**
   * Inspects the provided Scorm2004Resources to detect specific conditions based on the sequencing
   * indicators and the state of the detection process.
   *
   * @param resources The Scorm2004Resources object containing the list of resources to inspect.
   * @param indicators A set of sequencing indicators to update based on the inspection of the
   * resources.
   * @param state The DetectionState object to update with specific findings during resource
   * inspection.
   */
  private static void inspectResources(Scorm2004Resources resources,
      EnumSet<SequencingIndicator> indicators,
      DetectionState state) {
    if (resources == null || !isNotEmpty(resources.getResourceList())) {
      return;
    }
    int scoCount = 0;
    for (Scorm2004Resource resource : resources.getResourceList()) {
      if (resource != null && resource.getScormType() == ScormType.SCO) {
        scoCount++;
      }
    }
    if (scoCount > 0) {
      indicators.add(SequencingIndicator.RESOURCE_SCO);
    }
    if (scoCount > 1) {
      state.hasMultipleSCOs = true;
    }
  }

  /**
   * Determines the sequencing level based on the provided DetectionState.
   *
   * @param state the DetectionState object containing information about sequencing content,
   * organizational objectives, multiple SCOs, and minimal sequencing.
   * @return the corresponding SequencingLevel based on the attributes of the provided state.
   */
  private static SequencingLevel determineLevel(DetectionState state) {
    if (state.hasSequencingContent || state.organizationObjectivesGlobal) {
      return SequencingLevel.FULL;
    }
    if (state.hasMultipleSCOs) {
      return SequencingLevel.MULTI;
    }
    if (state.hasMinimalSequencing) {
      return SequencingLevel.MINIMAL;
    }
    return SequencingLevel.NONE;
  }

  /**
   * Inspects a collection of SCORM 2004 items and processes each item based on the specified
   * sequencing indicators and detection state. Applies recursive inspection for child items.
   *
   * @param scormItems the collection of SCORM 2004 items to inspect; must not be null or empty
   * @param indicators the set of sequencing indicators used during the inspection process; must not
   * be null
   * @param state the current detection state to be applied during the inspection process; must not
   * be null
   */
  private static void inspectItems(Collection<? extends Scorm2004Item> scormItems,
      EnumSet<SequencingIndicator> indicators, DetectionState state) {
    if (scormItems == null || scormItems.isEmpty()) {
      return;
    }

    for (Scorm2004Item item : scormItems) {
      if (item == null) {
        continue;
      }
      inspectSingleItem(item, indicators, state);
      // Recurse into children
      inspectItems(Objects.requireNonNullElse(item.getItems(), List.of()), indicators, state);
    }
  }

  /**
   * Inspect a single SCORM 2004 item and add relevant indicators.
   */
  private static void inspectSingleItem(Scorm2004Item item,
      EnumSet<SequencingIndicator> indicators, DetectionState state) {
    if (!item.isVisible()) {
      indicators.add(SequencingIndicator.ITEM_IS_VISIBLE_FALSE);
    }
    if (!hasText(item.getIdentifierRef())) {
      indicators.add(SequencingIndicator.ITEM_NO_IDENTIFIER_REF);
    }

    List<Scorm2004Item> children = item.getItems();
    boolean hasChildren = children != null && !children.isEmpty();
    if (hasChildren) {
      indicators.add(SequencingIndicator.ITEM_HAS_CHILDREN);
    }

    if (hasCompletionThreshold(item.getCompletionThreshold())) {
      indicators.add(SequencingIndicator.COMPLETION_THRESHOLD);
    }
    if (hasPresentationControls(item.getPresentation())) {
      indicators.add(SequencingIndicator.PRESENTATION_CONTROLS);
    }

    Sequencing sequencing = item.getSequencing();
    if (sequencing != null) {
      inspectSequencing(sequencing, state, indicators, true, false);
    }
  }

  /**
   * Inspects a given sequencing object to analyze its properties and determine corresponding
   * sequencing indicators. Based on the analysis, updates the provided indicators and detection
   * state.
   *
   * @param sequencing the sequencing object to inspect; if null, no operation will be performed
   * @param state the detection state object to be updated with sequencing content and minimal
   * sequencing information
   * @param indicators a set of sequencing indicators to be updated based on the analysis of the
   * sequencing object
   * @param attachedToActivity a boolean indicating whether the sequencing object is attached to an
   * activity
   * @param fromCollection a boolean indicating whether the sequencing object originates from a
   * collection
   */
  private static void inspectSequencing(Sequencing sequencing, DetectionState state,
      EnumSet<SequencingIndicator> indicators, boolean attachedToActivity, boolean fromCollection) {
    if (sequencing == null) {
      return;
    }

    // Context indicators
    addContextIndicators(indicators, attachedToActivity, fromCollection);

    // Cache frequently used properties
    final String id = sequencing.getId();
    final String idRef = sequencing.getIdRef();
    final ControlMode controlMode = sequencing.getControlMode();
    final DeliveryControls deliveryControls = sequencing.getDeliveryControls();
    final RollupConsiderations rollupConsiderations = sequencing.getRollupConsiderations();
    final ConstrainChoiceConsiderations constrainChoiceConsiderations = sequencing.getConstrainChoiceConsiderations();
    final SequencingRules sequencingRules = sequencing.getSequencingRules();

    final boolean hasId = hasText(id);
    final boolean hasIdRef = hasText(idRef);
    final boolean hasRules = hasSequencingRules(sequencingRules);
    final boolean hasLimits = hasLimitConditions(sequencing.getLimitConditions());
    final boolean hasAux = hasAuxiliaryResources(sequencing.getAuxiliaryResources());
    final boolean hasRollupRulesFlag = hasRollupRules(sequencing.getRollupRules());
    final boolean hasObjectivesFlag = hasObjectives(sequencing.getObjectives());
    final boolean hasAdlObjectivesFlag = hasObjectives(sequencing.getAdlObjectives());
    final boolean hasRandomization = hasRandomizationControls(
        sequencing.getRandomizationControls());
    final boolean hasDelivery = deliveryControls != null;
    final boolean hasRollupConsiderations = rollupConsiderations != null;
    final boolean hasConstrainChoice = constrainChoiceConsiderations != null;

    if (hasId) {
      indicators.add(SequencingIndicator.SEQUENCING_ID);
    }

    boolean anyFeature = false;
    boolean advancedFeature = false;

    // Control mode
    if (controlMode != null) {
      indicators.add(SequencingIndicator.SEQUENCING_CONTROL_MODE);
      anyFeature = true;
      if (!isMinimalControlMode(controlMode)) {
        advancedFeature = true;
      }
    }

    // Rules and feature groups (unified handling)
    anyFeature |= evaluateFeature(hasRules, indicators, SequencingIndicator.SEQUENCING_RULES);
    advancedFeature |= hasRules;

    anyFeature |= evaluateFeature(hasLimits, indicators,
        SequencingIndicator.SEQUENCING_LIMIT_CONDITIONS);
    advancedFeature |= hasLimits;

    anyFeature |= evaluateFeature(hasAux, indicators,
        SequencingIndicator.SEQUENCING_AUXILIARY_RESOURCES);
    advancedFeature |= hasAux;

    anyFeature |= evaluateFeature(hasRollupRulesFlag, indicators,
        SequencingIndicator.SEQUENCING_ROLLUP_RULES);
    advancedFeature |= hasRollupRulesFlag;

    anyFeature |= evaluateFeature(hasObjectivesFlag, indicators,
        SequencingIndicator.SEQUENCING_OBJECTIVES);
    advancedFeature |= hasObjectivesFlag;

    anyFeature |= evaluateFeature(hasAdlObjectivesFlag, indicators,
        SequencingIndicator.SEQUENCING_ADL_OBJECTIVES);
    advancedFeature |= hasAdlObjectivesFlag;

    anyFeature |= evaluateFeature(hasRandomization, indicators,
        SequencingIndicator.SEQUENCING_RANDOMIZATION);
    advancedFeature |= hasRandomization;

    if (hasDelivery) {
      indicators.add(SequencingIndicator.SEQUENCING_DELIVERY_CONTROLS);
      anyFeature = true;
      if (!isMinimalDeliveryControls(deliveryControls)) {
        advancedFeature = true;
      }
    }

    if (hasRollupConsiderations) {
      updateFeatureFlagsFor(indicators, SequencingIndicator.SEQUENCING_ROLLUP_CONSIDERATIONS);
      anyFeature = true;
      advancedFeature = true;
    }

    if (hasConstrainChoice) {
      updateFeatureFlagsFor(indicators, SequencingIndicator.SEQUENCING_CONSTRAIN_CHOICE);
      anyFeature = true;
      advancedFeature = true;
    }

    // Collection/Activity outcomes
    if (fromCollection && hasId) {
      registerCollectionSequence(state, id, advancedFeature, anyFeature);
    }

    if (attachedToActivity) {
      updateActivityFlags(state, advancedFeature, anyFeature);
    }

    // Handle IDRef usage
    if (hasIdRef) {
      indicators.add(SequencingIndicator.SEQUENCING_IDREF);
      resolveIdRefImpact(state, idRef, advancedFeature);
    }
  }

  /**
   * Evaluates the presence of a feature and updates the feature flags accordingly.
   *
   * @param present a boolean indicating whether the feature is present
   * @param indicators an EnumSet of SequencingIndicator representing current feature flags
   * @param indicator a SequencingIndicator to be added or processed in the feature flags
   * @return true if the feature is present, otherwise false
   */
  private static boolean evaluateFeature(boolean present, EnumSet<SequencingIndicator> indicators,
      SequencingIndicator indicator) {
    if (!present) {
      return false;
    }
    updateFeatureFlagsFor(indicators, indicator);
    return true;
  }

  /**
   * Adds context-specific sequencing indicators to the provided set based on the specified
   * parameters.
   *
   * @param indicators the set of sequencing indicators to which context indicators will be added
   * @param attachedToActivity determines if the context is attached to an activity, indicating
   * activity sequencing should be applied
   * @param fromCollection determines if the context is derived from a collection, indicating
   * collection sequencing definition should be applied
   */
  private static void addContextIndicators(EnumSet<SequencingIndicator> indicators,
      boolean attachedToActivity, boolean fromCollection) {
    if (attachedToActivity) {
      indicators.add(SequencingIndicator.ACTIVITY_SEQUENCING);
    } else if (fromCollection) {
      indicators.add(SequencingIndicator.COLLECTION_SEQUENCING_DEFINITION);
    }
  }

  /**
   * Updates the provided set of sequencing indicators by adding the specified indicator.
   *
   * @param indicators the set of sequencing indicators to be updated
   * @param indicator the sequencing indicator to be added to the set
   */
  private static void updateFeatureFlagsFor(EnumSet<SequencingIndicator> indicators,
      SequencingIndicator indicator) {
    indicators.add(indicator);
  }

  /**
   * Registers a collection sequence based on its feature presence.
   * <p>
   * Depending on the presence of advanced or basic features in the collection sequence, the method
   * adds its identifier to the appropriate list within the given detection state.
   *
   * @param state the DetectionState object that keeps track of collection sequences
   * @param id the unique identifier of the collection sequence to register
   * @param advancedFeaturePresent whether the collection sequence has advanced features present
   * @param anyFeaturePresent whether the collection sequence has any features present
   */
  private static void registerCollectionSequence(DetectionState state, String id,
      boolean advancedFeaturePresent, boolean anyFeaturePresent) {
    if (advancedFeaturePresent) {
      state.collectionSequencesWithContent.add(id);
    } else if (anyFeaturePresent) {
      state.collectionSequencesMinimal.add(id);
    }
  }

  /**
   * Updates the activity flags in the provided DetectionState object based on the presence of
   * advanced features and any features.
   *
   * @param state the DetectionState object whose flags are to be updated
   * @param advancedFeaturePresent a boolean indicating if advanced features are present
   * @param anyFeaturePresent a boolean indicating if any features are present
   */
  private static void updateActivityFlags(DetectionState state, boolean advancedFeaturePresent,
      boolean anyFeaturePresent) {
    if (advancedFeaturePresent) {
      state.hasSequencingContent = true;
    } else if (anyFeaturePresent) {
      state.hasMinimalSequencing = true;
    }
  }

  /**
   * Resolves the impact of an ID reference on the detection state by updating specific flags based
   * on predefined conditions.
   *
   * @param state Represents the current detection state, which includes collections of sequences
   * and flags indicating specific properties.
   * @param idRef The identifier reference being evaluated against the collections in the detection
   * state.
   * @param advancedFeaturePresent Indicates whether advanced features are enabled, which may
   * influence the resolution logic.
   */
  private static void resolveIdRefImpact(DetectionState state, String idRef,
      boolean advancedFeaturePresent) {
    if (state.collectionSequencesWithContent.contains(idRef)) {
      state.hasSequencingContent = true;
    } else if (state.collectionSequencesMinimal.contains(idRef) && !advancedFeaturePresent) {
      state.hasMinimalSequencing = true;
    }
  }

  /**
   * Determines if the given RandomizationControls object has any randomization settings enabled.
   *
   * @param controls the RandomizationControls object to evaluate; it may be null
   * @return true if randomization settings are enabled in the provided controls object, false
   * otherwise
   */
  private static boolean hasRandomizationControls(RandomizationControls controls) {
    if (controls == null) {
      return false;
    }
    Integer selectCount = controls.getSelectCount();
    RandomizationTiming randomizationTiming = controls.getRandomizationTiming();
    RandomizationTiming selectionTiming = controls.getSelectionTiming();
    return (selectCount != null && selectCount > 0)
        || controls.isReorderChildren()
        || (randomizationTiming != null && randomizationTiming != RandomizationTiming.NEVER)
        || (selectionTiming != null && selectionTiming != RandomizationTiming.NEVER);
  }

  /**
   * Determines if the provided Scorm2004Objectives object has objectives defined. The method checks
   * whether the primary objective is not null or if the list of additional objectives is not
   * empty.
   *
   * @param objectives the Scorm2004Objectives object to inspect; may be null
   * @return true if the Scorm2004Objectives object contains a non-null primary objective or a
   * non-empty list of additional objectives; false otherwise
   */
  private static boolean hasObjectives(Scorm2004Objectives objectives) {
    if (objectives == null) {
      return false;
    }
    return objectives.getPrimaryObjective() != null
        || isNotEmpty(objectives.getObjectiveList());
  }

  /**
   * Determines if the provided RollupRules object has at least one significant rule or condition
   * defined. This method evaluates the list of rollup rules and other attributes within the
   * RollupRules object to decide whether meaningful rollup configurations exist.
   *
   * @param rollupRules the RollupRules object to inspect; may be null
   * @return true if the RollupRules object contains meaningful configurations (e.g., non-empty
   * rollup rule list, unsatisfied objective, unsatisfied progress or completion criteria, or a
   * non-standard objective measure weight); false otherwise
   */
  private static boolean hasRollupRules(RollupRules rollupRules) {
    if (rollupRules == null) {
      return false;
    }
    if (isNotEmpty(rollupRules.getRollupRuleList())) {
      return true;
    }
    return !rollupRules.isRollupObjectiveSatisfied()
        || !rollupRules.isRollupProgressCompletion()
        || rollupRules.getObjectiveMeasureWeight() != 1.0d;
  }

  /**
   * Checks if the provided AuxiliaryResources object has at least one auxiliary resource defined.
   * The method evaluates whether the AuxiliaryResources object is not null and whether its list of
   * auxiliary resources is not empty.
   *
   * @param auxiliaryResources the AuxiliaryResources object to inspect; may be null
   * @return true if the AuxiliaryResources object is not null and its list of auxiliary resources
   * is not empty, false otherwise
   */
  private static boolean hasAuxiliaryResources(AuxiliaryResources auxiliaryResources) {
    return auxiliaryResources != null && isNotEmpty(auxiliaryResources.getAuxiliaryResourceList());
  }

  /**
   * Determines if the provided LimitConditions object includes at least one defined limit. This
   * method checks various properties within the LimitConditions object to verify whether any
   * constraints or limits are configured. If the LimitConditions object is null, it automatically
   * returns false.
   *
   * @param limitConditions the LimitConditions object to inspect; may be null
   * @return true if at least one limit condition is defined in the LimitConditions object, false
   * otherwise
   */
  private static boolean hasLimitConditions(LimitConditions limitConditions) {
    if (limitConditions == null) {
      return false;
    }
    return limitConditions.getAttemptLimit() != null
        || hasValue(limitConditions.getAttemptAbsoluteDurationLimit())
        || hasValue(limitConditions.getAttemptExperiencedDurationLimit())
        || hasValue(limitConditions.getActivityAbsoluteDurationLimit())
        || hasValue(limitConditions.getActivityExperiencedDurationLimit())
        || hasValue(limitConditions.getBeginTimeLimit())
        || hasValue(limitConditions.getEndTimeLimit());
  }

  /**
   * Checks if the provided Duration value is not null and not zero.
   *
   * @param value the Duration object to check; may be null
   * @return true if the value is not null and not zero, false otherwise
   */
  private static boolean hasValue(Duration value) {
    return value != null && !value.isZero();
  }

  /**
   * Checks if the provided Instant value is not null.
   *
   * @param value the Instant object to check; may be null
   * @return true if the value is not null, false otherwise
   */
  private static boolean hasValue(Instant value) {
    return value != null;
  }

  /**
   * Determines if the provided SequencingRules object contains at least one type of sequencing
   * rule. The method checks for the presence of precondition rules, postcondition rules, or exit
   * condition rules within the specified SequencingRules object.
   *
   * @param sequencingRules the SequencingRules object to inspect; may be null
   * @return true if the SequencingRules object contains at least one type of sequencing rule
   * (precondition, postcondition, or exit condition); false otherwise
   */
  private static boolean hasSequencingRules(SequencingRules sequencingRules) {
    if (sequencingRules == null) {
      return false;
    }
    return hasRuleList(sequencingRules.getPreConditionRules())
        || hasRuleList(sequencingRules.getPostConditionRules())
        || hasRuleList(sequencingRules.getExitConditionRules());
  }

  /**
   * Checks if the given list of SequencingRule objects is not empty. A list is considered not empty
   * if it is not null and contains at least one SequencingRule element.
   *
   * @param rules the list of SequencingRule objects to check; may be null
   * @return true if the list is not null and contains at least one SequencingRule, false otherwise
   */
  private static boolean hasRuleList(List<SequencingRule> rules) {
    return isNotEmpty(rules);
  }

  /**
   * Checks if the provided CompletionThreshold object is not null.
   *
   * @param completionThreshold the CompletionThreshold object to check; may be null.
   * @return true if the CompletionThreshold object is not null, false otherwise.
   */
  private static boolean hasCompletionThreshold(CompletionThreshold completionThreshold) {
    return completionThreshold != null;
  }

  /**
   * Determines if the given Presentation object has configured presentation controls. A
   * presentation is considered to have controls if its NavigationInterface object is not null and
   * contains a non-empty list of elements specifying which LMS UI components should be hidden.
   *
   * @param presentation the Presentation object to inspect; may be null
   * @return true if the Presentation has configured presentation controls, false otherwise
   */
  private static boolean hasPresentationControls(Presentation presentation) {
    if (presentation == null) {
      return false;
    }
    NavigationInterface navigationInterface = presentation.getNavigationInterface();
    return navigationInterface != null && isNotEmpty(navigationInterface.getHideLMSUI());
  }

  /**
   * Determines if the given ControlMode represents a minimal control mode configuration. A minimal
   * control mode is defined by specific boolean flags and conditions within the ControlMode
   * object.
   *
   * @param controlMode the ControlMode object to inspect; may be null
   * @return true if the ControlMode satisfies the minimal control mode criteria, false otherwise
   */
  private static boolean isMinimalControlMode(ControlMode controlMode) {
    if (controlMode == null) {
      return true;
    }
    if (!controlMode.isChoice()) {
      return false;
    }
    if (!controlMode.isChoiceExit()) {
      return false;
    }
    if (controlMode.isForwardOnly()) {
      return false;
    }
    if (!controlMode.isUseCurrentAttemptObjectiveInfo()) {
      return false;
    }
    return controlMode.isUseCurrentAttemptProgressInfo();
  }

  /**
   * Determines if the given DeliveryControls object represents a minimal delivery controls
   * configuration. A minimal configuration is defined by specific combinations of boolean
   * properties within the DeliveryControls object. If the object is null, it is considered to
   * satisfy the minimal delivery controls criteria.
   *
   * @param deliveryControls the DeliveryControls object to evaluate; may be null
   * @return true if the DeliveryControls object meets the minimal configuration criteria, false
   * otherwise
   */
  private static boolean isMinimalDeliveryControls(DeliveryControls deliveryControls) {
    if (deliveryControls == null) {
      return true;
    }
    boolean defaultCombination = deliveryControls.isTracked()
        && !deliveryControls.isCompletionSetByContent()
        && !deliveryControls.isObjectiveSetByContent();
    boolean storylineCombination = deliveryControls.isTracked()
        && deliveryControls.isCompletionSetByContent()
        && deliveryControls.isObjectiveSetByContent();
    return defaultCombination || storylineCombination;
  }

  /**
   * Checks if the given haystack string contains the needle string, ignoring case. This method
   * ensures case-insensitive comparison by converting the haystack to lowercase and verifying if it
   * contains the needle. If either the haystack is null, empty, or only contains whitespace, it
   * will return false.
   *
   * @param haystack the string to search within; may be null or empty
   * @param needle the string to search for; must not be null
   * @return true if the haystack contains the needle ignoring case, false otherwise
   */
  private static boolean containsIgnoreCase(String haystack, String needle) {
    return hasText(haystack) && haystack
        .toLowerCase()
        .contains(needle);
  }

  /**
   * Checks if the provided string has non-blank text. A string is considered to have text if it is
   * not null and contains non-whitespace characters.
   *
   * @param value the string to check; may be null
   * @return true if the string is not null and contains non-whitespace text, false otherwise
   */
  private static boolean hasText(String value) {
    return value != null && !value.isBlank();
  }

  /**
   * Checks if the provided list is not empty. A list is considered not empty if it is not null and
   * contains at least one element.
   *
   * @param value the list to check; may be null
   * @return true if the list is not null and contains at least one element, false otherwise
   */
  private static boolean isNotEmpty(List<?> value) {
    return value != null && !value.isEmpty();
  }

  /**
   * Enum representing different types of sequencing indicators used in SCORM 2004 manifests. These
   * indicators are used to detect and classify various sequencing-related elements and attributes
   * within a manifest or its items.
   * <p>
   * Each indicator has a boolean property `strong` that specifies whether it is considered a
   * "strong" indicator. Strong indicators typically represent critical sequencing considerations or
   * elements tied to SCORM sequencing behaviors.
   * <p>
   * This enum is closely tied to SCORM 2004 sequencing models and provides a structured approach to
   * detecting and analyzing sequencing configurations.
   */
  public enum SequencingIndicator {
    /**
     * Represents the IMS Simple Sequencing (IMSSS) namespace indicator used in SCORM 2004
     * manifests. This sequencing indicator is associated with elements and attributes defined
     * within the IMSSS namespace. It helps identify and classify sequencing-related configurations
     * tied to the IMSSS standard. This indicator is not considered a "strong" sequencing
     * indicator.
     */
    IMSSS_NAMESPACE(false),
    /**
     * Represents the ADL Sequencing (ADLSEQ) namespace indicator used in SCORM 2004 manifests. This
     * sequencing indicator is associated with elements and attributes defined within the ADLSEQ
     * namespace. It helps identify and classify sequencing-related configurations tied to the
     * ADLSEQ standard. This indicator is not considered a "strong" sequencing indicator.
     */
    ADLSEQ_NAMESPACE(false),
    /**
     * Represents the ADL Navigation (ADLNAV) namespace indicator used in SCORM 2004 manifests. This
     * sequencing indicator is associated with elements and attributes defined within the ADLNAV
     * namespace. It helps identify and classify navigation-related configurations tied to the
     * ADLNAV standard. This indicator is not considered a "strong" sequencing indicator.
     */
    ADLNAV_NAMESPACE(false),
    /**
     * Represents the schema location for IMS Simple Sequencing (IMSSS) as a sequencing indicator.
     * This constant is used to define whether the schema for IMSSS is applicable in the context of
     * sequencing and navigation rules in SCORM-compliant content.
     * <p>
     * The value indicates whether this sequencing indicator is strong, which affects its
     * application.
     */
    SCHEMA_LOCATION_IMSSS(false),
    /**
     * Represents the schema location for the ADL Simple Sequencing (ADLSEQ) namespace. This
     * constant is used to identify whether the ADLSEQ schema is applicable within the SCORM 2004
     * sequencing and navigation context.
     * <p>
     * The value of this field determines schema validation behaviors related to the ADLSEQ
     * namespace.
     *
     * @see SequencingIndicator#isStrong()
     */
    SCHEMA_LOCATION_ADLSEQ(false),
    /**
     * A constant indicating the schema location for SCORM ADL Navigation (ADLNAV). This variable is
     * used to represent whether navigation rules in the ADLNAV namespace should be processed with
     * high precedence (strong) or not.
     * <p>
     * It defines the behavior and enforceability of ADLNAV-related sequencing and navigation
     * elements in SCORM-compliant content, based on the specified configuration.
     *
     * @see SequencingIndicator
     */
    SCHEMA_LOCATION_ADLNAV(false),
    /**
     * Represents an indicator for sequencing collection within the SCORM sequencing rules. It is
     * used to denote whether a particular sequencing behavior is part of a collection.
     * <p>
     * The value of this variable is a boolean, where: - `true` indicates that the sequencing
     * collection is enabled or considered strong. - `false` indicates that the sequencing
     * collection is disabled or not considered strong.
     */
    SEQUENCING_COLLECTION(false),
    /**
     * Indicates the sequencing definition for a collection within the SCORM IMS Simple Sequencing
     * (IMSSS) framework. This constant is part of the sequencing configuration for collections as
     * defined in SCORM standards.
     * <p>
     * COLLECTION_SEQUENCING_DEFINITION can specify whether the sequencing settings for a collection
     * should impose strong or weak sequencing behaviors to control the flow and progress of
     * learners through the content.
     */
    COLLECTION_SEQUENCING_DEFINITION(false),
    /**
     * Indicates whether sequencing settings associated with an organization level are applied in a
     * SCORM content package. This enumeration element is used to manage and enforce sequencing
     * rules, objectives, and conditions at the organization level as defined by SCORM 2004
     * standards.
     * <p>
     * The boolean value indicates the strength of the sequencing: - {@code true} indicates that the
     * sequencing is applied with a higher priority or stricter enforcement. - {@code false}
     * indicates that sequencing at this level is optional or less restrictive.
     * <p>
     * This constant is part of the `SequencingIndicator` enumeration and helps define sequencing
     * behaviors in SCORM manifests at an organizational scope.
     */
    ORGANIZATION_SEQUENCING(true),
    /**
     * Represents a constant used to indicate whether the objectives associated with an organization
     * in a SCORM-based sequencing context are globally shared across the entire package. This is
     * typically defined as part of the SCORM IMS Simple Sequencing (IMSSS) schema.
     * <p>
     * The value of this constant determines whether objectives at the organization level are
     * applied globally to all child elements, influencing sequencing and navigation behavior.
     * <p>
     * When set to true, it signifies that the objectives are globally significant, allowing their
     * use across multiple contexts within the SCORM content structure.
     */
    ORGANIZATION_OBJECTIVES_GLOBAL(true),
    /**
     * Represents the indicator for activity sequencing in the SCORM 2004 specification. This
     * variable is used to signify whether specific sequencing rules apply at the activity level
     * within the SCORM content structure.
     * <p>
     * ACTIVITY_SEQUENCING holds a boolean value that specifies the state of sequencing enforcement
     * for activities. When set to true, it indicates that sequencing rules are enabled and enforced
     * for the activity.
     */
    ACTIVITY_SEQUENCING(true),
    /**
     * Represents an identifier used in sequencing definitions within the SCORM IMS Simple
     * Sequencing (IMSSS) schema. This identifier is used to uniquely reference a specific
     * sequencing definition in a SCORM manifest.
     * <p>
     * The sequencing identifier is integral to defining relationships and rules for content
     * sequencing, including activity navigation, sequencing rules, objectives, and randomization
     * controls. It ensures that sequencing behaviors are consistently applied and understood within
     * the SCORM package.
     * <p>
     * This constant is set to indicate whether the identifier is strong (true) or not (false).
     */
    SEQUENCING_ID(false),
    /**
     * Represents a reference to an existing sequencing definition in the IMS Simple Sequencing
     * (IMSSS) specification.
     * <p>
     * SEQUENCING_IDREF is used to reference a sequencing definition by its unique identifier,
     * allowing for reusability and inheritance of sequencing behaviors within SCORM content. It
     * facilitates modularity by enabling activities or content nodes to inherit sequencing rules or
     * behaviors defined elsewhere.
     * <p>
     * This constant is configured with strong enforcement of its reference validity.
     */
    SEQUENCING_IDREF(true),
    /**
     * Represents the sequencing control mode setting within the SCORM IMS Simple Sequencing (IMSSS)
     * model.
     * <p>
     * This constant is used to define the behavior of a sequencing control mode for activities.
     * Sequencing control modes specify how navigation and sequencing behaviors are applied to a
     * given activity during the learning process.
     * <p>
     * The value of this constant indicates whether the specific sequencing control mode is enabled
     * or disabled. A value of {@code true} signifies that the sequencing control mode is active for
     * the associated activity.
     * <p>
     * This field is part of the SCORM sequencing definition and plays a critical role in
     * determining the flow and navigation possibilities for learners within a SCORM-conformant
     * content package.
     */
    SEQUENCING_CONTROL_MODE(true),
    /**
     * Represents the sequencing rules defined within the IMS Simple Sequencing (IMSSS) framework.
     * <p>
     * Sequencing rules are used to enforce conditions and constraints for content navigation and
     * progression within SCORM-compliant learning activities. These rules dictate when specific
     * actions, such as delivering or skipping a learning activity, can be executed based on defined
     * conditions.
     * <p>
     * This variable encapsulates the configuration or presence of sequencing rules as part of the
     * overall sequencing strategy within SCORM content.
     * <p>
     * The boolean value associated with the variable indicates whether certain sequencing
     * conditions are considered 'strong' or required to enforce sequencing behaviors.
     */
    SEQUENCING_RULES(true),
    /**
     * Represents the sequencing limit conditions as defined in the SCORM IMS Simple Sequencing
     * (IMSSS) schema.
     * <p>
     * This indicator specifies whether limit conditions such as time limits or attempt constraints
     * are enforced during sequencing and navigation of SCORM content.
     * <p>
     * The presence of this constant allows for configuration or validation of sequencing behaviors
     * in compliance with the SCORM 2004 specification.
     */
    SEQUENCING_LIMIT_CONDITIONS(true),
    /**
     * Represents auxiliary resources used in SCORM sequencing configurations.
     * <p>
     * This variable indicates whether specific auxiliary resources are defined or considered within
     * the sequencing process. Auxiliary resources can include additional information or metadata
     * used to facilitate or augment the progression of learners through learning activities.
     * <p>
     * This configuration plays a role in determining the behavior of the sequencing engine based on
     * the SCORM IMS Simple Sequencing (IMSSS) standard, ensuring that the behavior conforms to
     * defined rules and conditions for resource usage.
     * <p>
     * The value is typically used in conjunction with other sequencing definitions to ensure proper
     * navigation and learning path enforcement in SCORM-compliant learning systems.</p>
     */
    SEQUENCING_AUXILIARY_RESOURCES(true),
    /**
     * Represents the sequencing rollup rules defined within the SCORM IMS Simple Sequencing (IMSSS)
     * specification.
     * <p>
     * Sequencing rollup rules specify how the completion and success status of child activities are
     * aggregated or "rolled up" to determine the status of a parent activity. These rules are
     * critical in controlling how activity completion is evaluated based on learner progress
     * through hierarchical structures of activities.
     * <p>
     * This constant is used to identify and manage the sequencing rollup rules in SCORM-compliant
     * e-learning content, ensuring consistent behavior in Learning Management Systems (LMS).
     * <p>
     * The strong parameter indicates the rule's strictness or dependency on associated conditions.
     */
    SEQUENCING_ROLLUP_RULES(true),
    /**
     * Represents sequencing objectives within the SCORM IMS Simple Sequencing (IMSSS) schema. This
     * variable signifies whether sequencing objectives are strongly enforced during the sequencing
     * process.
     * <p>
     * The objectives define specific learning outcomes, which are used to tailor navigation and
     * delivery based on learner progress or performance. When set to {@code true}, sequencing
     * objectives are treated as a strong indicator for controlling the flow of activities.
     */
    SEQUENCING_OBJECTIVES(true),
    /**
     * Represents the SCORM sequencing indicator for ADL objectives.
     * <p>
     * This variable determines whether sequencing behavior is applied to the ADL objectives defined
     * in a SCORM package. Sequencing for ADL objectives defines specific rules for handling the
     * progression and evaluation of learner objectives during the course navigation.
     * <p>
     * A value of {@code true} indicates that the sequencing behavior is enforced for ADL
     * objectives, while {@code false} disables this behavior.
     */
    SEQUENCING_ADL_OBJECTIVES(true),
    /**
     * Represents the randomization sequencing indicator used in SCORM sequencing definitions. This
     * indicator specifies whether randomization is applied to the sequencing behavior of a given
     * activity or learning resource.
     * <p>
     * When enabled, the content may use a randomized sequence to guide the learner's progression.
     * This is controlled by the `strong` property of the sequencing indicator.
     * <p>
     * SEQUENCING_RANDOMIZATION reflects compliance with the SCORM IMS Simple Sequencing (IMSSS)
     * standard and is part of the broader set of indicators that define adaptive learning and
     * navigation rules.
     */
    SEQUENCING_RANDOMIZATION(true),
    /**
     * Represents the delivery controls applicable to SCORM sequencing behavior.
     * <p>
     * SEQUENCING_DELIVERY_CONTROLS is used to specify default or optional control settings that
     * affect the delivery behavior of a SCORM activity during execution and navigation. This
     * includes how the activity's sequencing and navigation rules are applied in relation to other
     * defined sequencing elements.
     * <p>
     * The boolean value associated with this variable indicates whether delivery controls are
     * strongly applied (true) or optional/conditionally applied (false).
     * <p>
     * This field is a constant within the SCORM sequencing context.
     */
    SEQUENCING_DELIVERY_CONTROLS(true),
    /**
     * Represents the sequencing rollup considerations used in the SCORM 2004 IMS Simple Sequencing
     * (IMSSS) model. This constant is a boolean flag indicating whether specific rollup
     * considerations are applied during the evaluation of activity sequencing.
     * <p>
     * Rollup considerations determine how completion or success status is rolled up from child
     * activities to parent activities based on defined rules. It can influence the control flow and
     * organizational behavior of SCORM-compliant e-learning content.
     * <p>
     * When true, sequencing rollup considerations are taken into account.
     */
    SEQUENCING_ROLLUP_CONSIDERATIONS(true),
    /**
     * Represents the specific sequencing indicator for constraining choices within a SCORM
     * sequencing context. This variable determines whether constraints are applied to the learner’s
     * ability to navigate between activities.
     *
     * <ul>
     * <li>If the value is {@code true}, navigation constraints are enforced, ensuring
     *     that learners follow the defined sequencing rules.</li>
     * <li>If the value is {@code false}, navigation constraints are relaxed, allowing
     *     learners more freedom in activity selection.</li>
     * </ul>
     */
    SEQUENCING_CONSTRAIN_CHOICE(true),
    /**
     * A static instance representing the "Presentation Controls" sequencing indicator within SCORM
     * 2004 sequencing definitions.
     * <p>
     * This variable is used to specify whether the sequencing system should account for choices and
     * constraints related to presentation controls. It indicates whether presentation control
     * behaviors are strong or weak based on its internal configuration.
     * <p>
     * In the context of SCORM, presentation controls define rules and conditions that determine the
     * visibility or interaction capabilities of individual activities within a learning sequence.
     */
    PRESENTATION_CONTROLS(false),
    /**
     * Represents the completion threshold setting used in SCORM sequencing.
     * <p>
     * The completion threshold identifies a specific condition that determines whether a learning
     * activity is considered complete. It is based on SCORM's `completionThresholdType` schema
     * definition, specifying attributes such as: - Whether completion is determined by specific
     * measures (e.g., progress). - Minimum progress required for completion (e.g., a fractional
     * value between 0.0 and 1.0). - Weight applied to progress in completion calculations.
     * <p>
     * This setting is utilized to evaluate learner progress and ensure compliance with defined
     * completion criteria for activities in a SCORM package.
     */
    COMPLETION_THRESHOLD(false),
    /**
     * Represents a constant value indicating that an item is not visible.
     * <p>
     * Used within the SCORM IMS Simple Sequencing (IMSSS) context to specify that a specific
     * sequencing or navigation element has its visibility explicitly set to false.
     */
    ITEM_IS_VISIBLE_FALSE(false),
    /**
     * Represents a flag or indicator to specify that an item does not have an identifier reference
     * in the sequencing process of SCORM or similar e-learning standards.
     * <p>
     * This constant is used to explicitly denote the absence of a valid item reference, which may
     * affect the sequencing behavior for learning paths or navigation within the content
     * structure.
     * <p>
     * The value is defined as {@code false}, indicating that the item is not associated with an
     * identifier reference.
     */
    ITEM_NO_IDENTIFIER_REF(false),
    /**
     * Marker indicating whether the associated SCORM item contains child items. This variable is
     * used as part of the sequencing and hierarchical structure definition within SCORM-compliant
     * content packages.
     * <p>
     * The presence of this marker helps determine navigational and sequencing behaviors based on
     * the hierarchical relationships between items.
     * <p>
     * A value of {@code false} implies that the associated item does not contain any child items
     * and is likely to be treated as a terminal node in the sequencing hierarchy.
     */
    ITEM_HAS_CHILDREN(false),
    /**
     * Represents a sequencing control option related to Resources in the SCORM sequencing model.
     * This sequencing indicator evaluates if a Sharable Content Object (SCO) adheres to specified
     * sequencing behaviors within the context of an activity tree.
     * <p>
     * Sequencing control options are used to govern various aspects of activity execution such as
     * access, navigation, and delivery in SCORM content.
     * <p>
     * This indicator helps define whether certain behaviors or conditions are strictly enforced.
     * The boolean parameter determines the "strength" or rigidity of the rule applied.
     */
    RESOURCE_SCO(false);

    /**
     * Indicates whether the sequencing indicator is in a "strong" state.
     * <p>
     * This variable determines the level or strength of the sequencing configuration applied within
     * the context of the sequencing rules or behaviors defined in SCORM IMS Simple Sequencing
     * (IMSSS) or related specifications.
     * <p>
     * When set to {@code true}, the sequencing configuration is enforced with high priority or
     * strict adherence to the defined rules. Otherwise, when {@code false}, the configuration may
     * allow for more flexibility or a less strict interpretation of the sequencing behavior.
     */
    private final boolean strong;

    /**
     * Constructs a SequencingIndicator with the specified strength.
     *
     * @param strong a boolean indicating whether the sequencing indicator is strong
     */
    SequencingIndicator(boolean strong) {
      this.strong = strong;
    }

    /**
     * Indicates whether the sequencing indicator is strong.
     *
     * @return true if the sequencing indicator is strong; false otherwise
     */
    public boolean isStrong() {
      return strong;
    }
  }

  /**
   * The SequencingLevel enum represents different levels of sequencing that can be applied within a
   * given context. The levels define the granularity or comprehensiveness of sequencing.
   * <p>
   * Enum Constants: - NONE: Indicates that no sequencing is applied. - MINIMAL: Represents a
   * minimal level of sequencing. - MULTI: Denotes a multi-layered or intermediate level of
   * sequencing. - FULL: Represents the highest level of sequencing with full granularity.
   */
  public enum SequencingLevel {
    /**
     * Represents the absence of any sequencing being applied. This is the default or baseline level
     * within the SequencingLevel enum, indicating that no specific sequencing rules, constraints,
     * or granularity are enforced in the given context.
     */
    NONE,
    /**
     * Represents a minimal level of sequencing within the SequencingLevel enum. This level applies
     * a basic or limited set of sequencing rules, ensuring only essential constraints are enforced
     * in the given context.
     */
    MINIMAL,
    /**
     * Denotes a multi-SCO or intermediate level of sequencing within the SequencingLevel enum. This
     * level provides more granularity compared to minimal sequencing, enabling multiple layers of
     * sequencing rules or constraints to be applied within the given context.
     */
    MULTI,
    /**
     * Represents the highest level of sequencing with full granularity. This level applies the most
     * comprehensive set of sequencing rules and constraints, ensuring complete sequencing within
     * the given context.
     */
    FULL
  }

  /**
   * Represents the internal state used for tracking sequencing-related properties during the
   * detection process in a SCORM 2004 manifest.
   * <p>
   * This class is used internally by the {@code SequencingUsageDetector} to accumulate and evaluate
   * characteristics of SCORM sequencing and organizational elements.
   * <p>
   * The state includes: - Sequences with content-related properties. - Minimal sequencing
   * characteristics. - Flags for global objectives and multi-SCO conditions.
   */
  private static final class DetectionState {

    /**
     * Represents a collection of sequence identifiers that are associated with content-related
     * properties found during the SCORM 2004 sequencing detection process.
     * <p>
     * This set is used to track sequences that have specific content settings or configurations,
     * such as those influencing runtime behavior or organizational structures.
     */
    private final Set<String> collectionSequencesWithContent = new HashSet<>();

    /**
     * Represents a collection of sequence identifiers associated with minimal sequencing properties
     * detected during the SCORM 2004 sequencing analysis process.
     * <p>
     * This set is used to track sequences that meet the least set of criteria necessary for
     * sequencing functionality within a SCORM manifest. It helps to identify minimal
     * characteristics necessary for ensuring correct runtime behavior and organizational
     * structure.
     */
    private final Set<String> collectionSequencesMinimal = new HashSet<>();

    /**
     * Indicates whether the detection process has identified the presence of sequencing content
     * with specific properties or configurations.
     * <p>
     * This field is used to signal the existence of content-related sequencing elements during the
     * SCORM 2004 sequencing evaluation. Its value is updated as the detection process analyzes
     * organizational and sequencing elements in the manifest.
     */
    private boolean hasSequencingContent;

    /**
     * Indicates whether the organization objectives in the SCORM manifest are global to the
     * system.
     * <p>
     * This field is used during the SCORM 2004 sequencing detection process to determine if the
     * objectives defined at the organizational level apply globally across all content in the
     * manifest. The value is updated as the analysis processes organizational and sequencing
     * elements, providing insight into the scope of objectives within the SCORM structure.
     */
    private boolean organizationObjectivesGlobal;

    /**
     * Indicates whether minimal sequencing properties have been identified during the SCORM 2004
     * sequencing detection process.
     * <p>
     * This field is used to determine if the detection process has found sequences that meet the
     * minimal criteria necessary for proper sequencing functionality within a SCORM manifest. It
     * represents a flag that helps evaluate compliance with the most basic sequencing rules or
     * requirements.
     */
    private boolean hasMinimalSequencing;

    /**
     * Indicates whether there are multiple Shareable Content Objects (SCOs) associated with the
     * current detection state.
     * <p>
     * SCOs are the smallest units of SCORM learning content that can be launched and tracked
     * individually. Multiple SCOs in a course signify that the course is structured as a collection
     * of several distinct learning objects rather than a single unit.
     * <p>
     * This variable helps to define and manage content sequencing behavior and ensures appropriate
     * navigation rules are applied in SCORM-conformant environments.
     */
    private boolean hasMultipleSCOs;
  }

  /**
   * Represents the result of a sequencing detection process. The result includes a sequencing level
   * and a set of indicators that provide detailed information about the detected sequencing
   * configuration.
   */
  @SuppressWarnings("ClassCanBeRecord")
  public static final class Result {

    /**
     * Represents the sequencing level detected as part of the sequencing detection process. The
     * sequencing level is derived from the {@link SequencingLevel} enum and indicates the
     * granularity or comprehensiveness of sequencing applied.
     */
    private final SequencingLevel level;

    /**
     * Represents a set of sequencing indicators derived from the sequencing detection process. Each
     * indicator provides detailed information about specific aspects of the detected sequencing
     * configuration.
     */
    private final Set<SequencingIndicator> indicators;

    /**
     * Constructs an instance of the {@code Result} class with the specified sequencing level and a
     * set of sequencing indicators.
     *
     * @param level the sequencing level that represents the detected granularity or
     * comprehensiveness of sequencing applied
     * @param indicators the set of sequencing indicators that provide detailed information about
     * specific aspects of the detected sequencing configuration
     */
    private Result(SequencingLevel level, EnumSet<SequencingIndicator> indicators) {
      this.level = level;
      this.indicators = indicators.isEmpty()
          ? Set.of()
          : Set.copyOf(indicators);
    }

    /**
     * Determines if the sequencing level is set to full granularity.
     *
     * @return true if the sequencing level is {@code SequencingLevel.FULL}, otherwise false
     */
    public boolean hasSequencing() {
      return level == SequencingLevel.FULL;
    }

    /**
     * Determines if any sequencing configuration is applied.
     *
     * @return true if the sequencing level is not {@code SequencingLevel.NONE}, otherwise false
     */
    public boolean hasAnySequencing() {
      return level != SequencingLevel.NONE;
    }

    /**
     * Retrieves the sequencing level detected as part of the sequencing detection process.
     *
     * @return the sequencing level, represented by an instance of {@link SequencingLevel},
     * indicating the granularity or comprehensiveness of sequencing applied
     */
    public SequencingLevel getLevel() {
      return level;
    }

    /**
     * Retrieves the set of sequencing indicators derived from the sequencing detection process.
     * Each indicator provides detailed information about specific aspects of the detected
     * sequencing configuration.
     *
     * @return a set of {@link SequencingIndicator} representing the detected sequencing indicators;
     * if no indicators are detected, an empty set is returned
     */
    public Set<SequencingIndicator> getIndicators() {
      return indicators;
    }
  }
}
