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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class SequencingUsageDetector {

  private SequencingUsageDetector() {
  }

  public static Result detect(Scorm2004Manifest manifest) {
    EnumSet<SequencingIndicator> indicators = EnumSet.noneOf(SequencingIndicator.class);
    if (manifest == null) {
      return new Result(SequencingLevel.NONE, indicators);
    }

    DetectionState state = new DetectionState();

    if (hasText(manifest.getImsssNamespaceUri())) {
      indicators.add(SequencingIndicator.IMSSS_NAMESPACE);
    }
    if (hasText(manifest.getAdlseqNamespaceUri())) {
      indicators.add(SequencingIndicator.ADLSEQ_NAMESPACE);
    }
    if (hasText(manifest.getAdlnavNamespaceUri())) {
      indicators.add(SequencingIndicator.ADLNAV_NAMESPACE);
    }

    String schemaLocation = manifest.getSchemaLocation();
    if (containsIgnoreCase(schemaLocation, "imsss")) {
      indicators.add(SequencingIndicator.SCHEMA_LOCATION_IMSSS);
    }
    if (containsIgnoreCase(schemaLocation, "adlseq")) {
      indicators.add(SequencingIndicator.SCHEMA_LOCATION_ADLSEQ);
    }
    if (containsIgnoreCase(schemaLocation, "adlnav")) {
      indicators.add(SequencingIndicator.SCHEMA_LOCATION_ADLNAV);
    }

    SequencingCollection collection = manifest.getSequencingCollection();
    if (collection != null && isNotEmpty(collection.getSequencingList())) {
      indicators.add(SequencingIndicator.SEQUENCING_COLLECTION);
      collection
          .getSequencingList()
          .stream()
          .filter(Objects::nonNull)
          .forEach(seq -> inspectSequencing(seq, indicators, false, true, state));
    }

    Scorm2004Organizations organizations = manifest.getOrganizations();
    if (organizations != null && isNotEmpty(organizations.getOrganizationList())) {
      for (Scorm2004Organization organization : organizations.getOrganizationList()) {
        if (organization == null) {
          continue;
        }
        if (organization.getSequencing() != null) {
          indicators.add(SequencingIndicator.ORGANIZATION_SEQUENCING);
          inspectSequencing(organization.getSequencing(), indicators, true, false, state);
        }
        if (organization.isObjectivesGlobalToSystem()) {
          indicators.add(SequencingIndicator.ORGANIZATION_OBJECTIVES_GLOBAL);
          state.organizationObjectivesGlobal = true;
        }
        inspectItems(organization.getItems(), indicators, state);
      }
    }

    Scorm2004Resources resources = manifest.getResources();
    if (resources != null && isNotEmpty(resources.getResourceList())) {
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
        state.hasMultipleScos = true;
      }
    }

    SequencingLevel level;
    if (state.hasSequencingContent || state.organizationObjectivesGlobal) {
      level = SequencingLevel.FULL;
    } else if (state.hasMultipleScos) {
      level = SequencingLevel.MULTI;
    } else if (state.hasMinimalSequencing) {
      level = SequencingLevel.MINIMAL;
    } else {
      level = SequencingLevel.NONE;
    }

    return new Result(level, indicators);
  }

  private static void inspectItems(
      List<Scorm2004Item> items,
      EnumSet<SequencingIndicator> indicators,
      DetectionState state
  ) {
    if (!isNotEmpty(items)) {
      return;
    }

    for (Scorm2004Item item : items) {
      if (item == null) {
        continue;
      }

      if (!item.isVisible()) {
        indicators.add(SequencingIndicator.ITEM_IS_VISIBLE_FALSE);
      }
      if (!hasText(item.getIdentifierRef())) {
        indicators.add(SequencingIndicator.ITEM_NO_IDENTIFIER_REF);
      }
      if (isNotEmpty(item.getItems())) {
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
        inspectSequencing(sequencing, indicators, true, false, state);
      }

      inspectItems(item.getItems(), indicators, state);
    }
  }

  private static void inspectSequencing(
      Sequencing sequencing,
      EnumSet<SequencingIndicator> indicators,
      boolean attachedToActivity,
      boolean fromCollection,
      DetectionState state
  ) {
    if (sequencing == null) {
      return;
    }

    if (attachedToActivity) {
      indicators.add(SequencingIndicator.ACTIVITY_SEQUENCING);
    } else if (fromCollection) {
      indicators.add(SequencingIndicator.COLLECTION_SEQUENCING_DEFINITION);
    }

    String sequenceId = null;
    if (hasText(sequencing.getId())) {
      indicators.add(SequencingIndicator.SEQUENCING_ID);
      sequenceId = sequencing.getId();
    }

    boolean hasAnyFeature = false;
    boolean hasAdvancedFeature = false;

    if (sequencing.getControlMode() != null) {
      indicators.add(SequencingIndicator.SEQUENCING_CONTROL_MODE);
      hasAnyFeature = true;
      if (!isMinimalControlMode(sequencing.getControlMode())) {
        hasAdvancedFeature = true;
      }
    }

    SequencingRules sequencingRules = sequencing.getSequencingRules();
    if (hasSequencingRules(sequencingRules)) {
      indicators.add(SequencingIndicator.SEQUENCING_RULES);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (hasLimitConditions(sequencing.getLimitConditions())) {
      indicators.add(SequencingIndicator.SEQUENCING_LIMIT_CONDITIONS);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (hasAuxiliaryResources(sequencing.getAuxiliaryResources())) {
      indicators.add(SequencingIndicator.SEQUENCING_AUXILIARY_RESOURCES);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (hasRollupRules(sequencing.getRollupRules())) {
      indicators.add(SequencingIndicator.SEQUENCING_ROLLUP_RULES);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (hasObjectives(sequencing.getObjectives())) {
      indicators.add(SequencingIndicator.SEQUENCING_OBJECTIVES);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (hasObjectives(sequencing.getAdlObjectives())) {
      indicators.add(SequencingIndicator.SEQUENCING_ADL_OBJECTIVES);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (hasRandomizationControls(sequencing.getRandomizationControls())) {
      indicators.add(SequencingIndicator.SEQUENCING_RANDOMIZATION);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (sequencing.getDeliveryControls() != null) {
      indicators.add(SequencingIndicator.SEQUENCING_DELIVERY_CONTROLS);
      hasAnyFeature = true;
      if (!isMinimalDeliveryControls(sequencing.getDeliveryControls())) {
        hasAdvancedFeature = true;
      }
    }

    RollupConsiderations rollupConsiderations = sequencing.getRollupConsiderations();
    if (rollupConsiderations != null) {
      indicators.add(SequencingIndicator.SEQUENCING_ROLLUP_CONSIDERATIONS);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    ConstrainChoiceConsiderations constrainChoiceConsiderations = sequencing.getConstrainChoiceConsiderations();
    if (constrainChoiceConsiderations != null) {
      indicators.add(SequencingIndicator.SEQUENCING_CONSTRAIN_CHOICE);
      hasAnyFeature = true;
      hasAdvancedFeature = true;
    }

    if (fromCollection && hasText(sequenceId)) {
      if (hasAdvancedFeature) {
        state.collectionSequencesWithContent.add(sequenceId);
      } else if (hasAnyFeature) {
        state.collectionSequencesMinimal.add(sequenceId);
      }
    }

    if (attachedToActivity) {
      if (hasAdvancedFeature) {
        state.hasSequencingContent = true;
      } else if (hasAnyFeature) {
        state.hasMinimalSequencing = true;
      }
    }

    if (hasText(sequencing.getIdRef())) {
      indicators.add(SequencingIndicator.SEQUENCING_IDREF);
      if (state.collectionSequencesWithContent.contains(sequencing.getIdRef())) {
        state.hasSequencingContent = true;
      } else if (state.collectionSequencesMinimal.contains(sequencing.getIdRef())
          && !hasAdvancedFeature) {
        state.hasMinimalSequencing = true;
      }
    }
  }

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

  private static boolean hasObjectives(Scorm2004Objectives objectives) {
    if (objectives == null) {
      return false;
    }
    return objectives.getPrimaryObjective() != null
        || isNotEmpty(objectives.getObjectiveList());
  }

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

  private static boolean hasAuxiliaryResources(AuxiliaryResources auxiliaryResources) {
    return auxiliaryResources != null && isNotEmpty(auxiliaryResources.getAuxiliaryResourceList());
  }

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

  private static boolean hasValue(Duration value) {
    return value != null && !value.isZero();
  }

  private static boolean hasValue(Instant value) {
    return value != null;
  }

  private static boolean hasSequencingRules(SequencingRules sequencingRules) {
    if (sequencingRules == null) {
      return false;
    }
    return hasRuleList(sequencingRules.getPreConditionRules())
        || hasRuleList(sequencingRules.getPostConditionRules())
        || hasRuleList(sequencingRules.getExitConditionRules());
  }

  private static boolean hasRuleList(List<SequencingRule> rules) {
    return isNotEmpty(rules);
  }

  private static boolean hasCompletionThreshold(CompletionThreshold completionThreshold) {
    return completionThreshold != null;
  }

  private static boolean hasPresentationControls(Presentation presentation) {
    if (presentation == null) {
      return false;
    }
    NavigationInterface navigationInterface = presentation.getNavigationInterface();
    return navigationInterface != null && isNotEmpty(navigationInterface.getHideLMSUI());
  }

  private static boolean hasDeliveryControls(DeliveryControls deliveryControls) {
    if (deliveryControls == null) {
      return false;
    }
    return !deliveryControls.isTracked()
        || deliveryControls.isCompletionSetByContent()
        || deliveryControls.isObjectiveSetByContent();
  }

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

  private static boolean containsIgnoreCase(String haystack, String needle) {
    return hasText(haystack) && haystack
        .toLowerCase()
        .contains(needle);
  }

  private static boolean hasText(String value) {
    return value != null && !value.isBlank();
  }

  private static boolean isNotEmpty(List<?> value) {
    return value != null && !value.isEmpty();
  }

  public enum SequencingIndicator {
    IMSSS_NAMESPACE(false),
    ADLSEQ_NAMESPACE(false),
    ADLNAV_NAMESPACE(false),
    SCHEMA_LOCATION_IMSSS(false),
    SCHEMA_LOCATION_ADLSEQ(false),
    SCHEMA_LOCATION_ADLNAV(false),
    SEQUENCING_COLLECTION(false),
    COLLECTION_SEQUENCING_DEFINITION(false),
    ORGANIZATION_SEQUENCING(true),
    ORGANIZATION_OBJECTIVES_GLOBAL(true),
    ACTIVITY_SEQUENCING(true),
    SEQUENCING_ID(false),
    SEQUENCING_IDREF(true),
    SEQUENCING_CONTROL_MODE(true),
    SEQUENCING_RULES(true),
    SEQUENCING_LIMIT_CONDITIONS(true),
    SEQUENCING_AUXILIARY_RESOURCES(true),
    SEQUENCING_ROLLUP_RULES(true),
    SEQUENCING_OBJECTIVES(true),
    SEQUENCING_ADL_OBJECTIVES(true),
    SEQUENCING_RANDOMIZATION(true),
    SEQUENCING_DELIVERY_CONTROLS(true),
    SEQUENCING_ROLLUP_CONSIDERATIONS(true),
    SEQUENCING_CONSTRAIN_CHOICE(true),
    PRESENTATION_CONTROLS(false),
    COMPLETION_THRESHOLD(false),
    ITEM_IS_VISIBLE_FALSE(false),
    ITEM_NO_IDENTIFIER_REF(false),
    ITEM_HAS_CHILDREN(false),
    RESOURCE_SCO(false);

    private final boolean strong;

    SequencingIndicator(boolean strong) {
      this.strong = strong;
    }

    public boolean isStrong() {
      return strong;
    }
  }

  public enum SequencingLevel {
    NONE,
    MINIMAL,
    MULTI,
    FULL
  }

  private static final class DetectionState {

    private final Set<String> collectionSequencesWithContent = new HashSet<>();
    private final Set<String> collectionSequencesMinimal = new HashSet<>();
    private boolean hasSequencingContent;
    private boolean organizationObjectivesGlobal;
    private boolean hasMinimalSequencing;
    private boolean hasMultipleScos;
  }

  public static final class Result {

    private final SequencingLevel level;
    private final Set<SequencingIndicator> indicators;

    private Result(SequencingLevel level, EnumSet<SequencingIndicator> indicators) {
      this.level = level;
      this.indicators = indicators.isEmpty()
          ? Set.of()
          : Set.copyOf(indicators);
    }

    public boolean hasSequencing() {
      return level == SequencingLevel.FULL;
    }

    public boolean hasAnySequencing() {
      return level != SequencingLevel.NONE;
    }

    public SequencingLevel getLevel() {
      return level;
    }

    public Set<SequencingIndicator> getIndicators() {
      return indicators;
    }
  }
}
