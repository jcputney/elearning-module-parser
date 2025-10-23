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

package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.input.common.serialization.DurationHHMMSSDeserializer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the AICC manifest for a course.
 *
 * <p>This class is used to parse the AICC manifest file and extract information about the course,
 * assignable units, descriptors, and course structures.</p>
 *
 * <p>It also provides methods to retrieve the title, description, launch URL, identifier, version,
 * and duration of the course.</p>
 */
public final class AiccManifest implements PackageManifest {

  /**
   * A predefined array of strings containing known attribute keys. These keys are used to represent
   * various attribute identifiers such as mastery scores, time limits, and requirement
   * specifications. The array is immutable and contains commonly used attribute key variants.
   */
  private static final String[] KNOWN_ATTRIBUTE_KEYS =
      {"CA", "CL", "CR", "MASTERY_SCORE", "MS", "MASTERYSCORE", "MT", "MAXTIME", "MAX_TIME",
          "MAX_TIME_ALLOWED", "TIME_LIMIT_ACTION", "TL", "TLA", "MANDATORY", "OPTIONAL", "REQ",
          "REQUIRED"};

  /**
   * An array of strings representing the keys that are deemed mandatory for specific operations or
   * configurations within the application. These keys indicate essential attributes or parameters
   * that must be present to ensure proper functioning or adherence to expected behavior.
   */
  private static final String[] MANDATORY_KEYS = {"MANDATORY", "REQUIRED", "REQ"};

  /**
   * An array of string constants representing optional keys used for specific configuration or
   * processing. The values in this array signify keys that are not mandatory but can be used to
   * provide additional or alternative data as needed, based on the implementation context.
   */
  private static final String[] OPTIONAL_KEYS = {"OPTIONAL"};
  /**
   * Course information for the AICC manifest.
   */
  private AiccCourse course;

  /**
   * List of assignable units in the AICC manifest.
   */
  private List<AssignableUnit> assignableUnits;

  /**
   * List of descriptors in the AICC manifest.
   */
  private List<Descriptor> descriptors;

  /**
   * List of course structures in the AICC manifest.
   */
  private List<CourseStructure> courseStructures;

  /**
   * Optional prerequisites table (.pre) rows, if present in the package. Each row is a
   * case-insensitive map of column name to value.
   */
  private List<Map<String, String>> prerequisitesTable;

  /**
   * Optional objective relations table (.ort) rows, if present in the package. Each row is a
   * case-insensitive map of column name to value.
   */
  private List<Map<String, String>> objectivesRelationTable;

  /**
   * The launch URL for the AICC manifest.
   */
  private String launchUrl;

  /**
   * Default constructor for the AiccManifest class. This constructor is primarily used by
   * frameworks and test methods that require an instance of the AiccManifest class without
   * additional initialization or configuration logic.
   */
  public AiccManifest() {
    // no-op
  }

  /**
   * Constructs an AiccManifest object that maps the relationship between the AICC course, its
   * assignable units, descriptors, and course structures, and derives the launch URL of the root
   * assignable unit.
   *
   * @param course the AICC course to which the manifest corresponds
   * @param assignableUnits a list of assignable units associated with the course
   * @param descriptors a list of descriptors providing metadata for assignable units
   * @param courseStructures a list of course structures defining the hierarchical relationships and
   * structure of the course
   * @throws ModuleParsingException if the root assignable unit or its ID cannot be determined, or
   * if there is inconsistent data in the manifest mappings
   */
  public AiccManifest(AiccCourse course, List<AssignableUnit> assignableUnits,
      List<Descriptor> descriptors, List<CourseStructure> courseStructures)
      throws ModuleParsingException {
    this.course = course;
    this.assignableUnits = assignableUnits;
    this.descriptors = descriptors;
    this.courseStructures = courseStructures;

    Map<String, AssignableUnit> assignableUnitsById = assignableUnits
        .stream()
        .filter(Objects::nonNull)
        .filter(au -> StringUtils.isNotBlank(au.getSystemId()))
        .collect(Collectors.toMap(AssignableUnit::getSystemId, au -> au, (a, b) -> a,
            LinkedHashMap::new));

    Map<String, Descriptor> descriptorsById = descriptors
        .stream()
        .filter(Objects::nonNull)
        .filter(descriptor -> StringUtils.isNotBlank(descriptor.getSystemId()))
        .collect(Collectors.toMap(Descriptor::getSystemId, descriptor -> descriptor, (a, b) -> a,
            LinkedHashMap::new));

    descriptorsById.forEach((id, descriptor) -> {
      AssignableUnit unit = assignableUnitsById.get(id);
      if (unit != null) {
        unit.setDescriptor(descriptor);
      }
    });

    Map<String, CourseStructure> courseStructureByMember = courseStructures
        .stream()
        .filter(Objects::nonNull)
        .filter(cs -> StringUtils.isNotBlank(cs.getMember()))
        .collect(Collectors.toMap(cs -> cs
                .getMember()
                .trim(), cs -> cs, (existing, replacement) -> existing,
            LinkedHashMap::new));

    for (Map.Entry<String, CourseStructure> entry : courseStructureByMember.entrySet()) {
      AssignableUnit unit = assignableUnitsById.get(entry.getKey());
      if (unit != null) {
        applyCourseStructureData(unit, entry.getValue());
      }
    }

    normalizeAssignableUnits(assignableUnits, this.course);

    CourseStructure root = courseStructures
        .stream()
        .filter(cs -> cs
            .getBlock()
            .equalsIgnoreCase("ROOT"))
        .findFirst()
        .orElse(null);

    if (root == null) {
      root = courseStructures.get(0);
    }
    String rootAssignableUnitId = root.getMember();
    if (rootAssignableUnitId == null || rootAssignableUnitId.isEmpty()) {
      ValidationResult result = ValidationResult.of(
          ValidationIssue.error("AICC_NO_ROOT_AU", "No root assignable unit found", "CourseStructure")
      );
      throw result.toException("Failed to build AICC manifest");
    }

    AssignableUnit rootAssignableUnit = assignableUnits
        .stream()
        .filter(au -> au
            .getSystemId()
            .equals(rootAssignableUnitId))
        .findFirst()
        .orElseThrow(() -> {
          ValidationResult result = ValidationResult.of(
              ValidationIssue.error("AICC_AU_NOT_FOUND",
                  "No assignable unit found with ID: " + rootAssignableUnitId,
                  "AssignableUnit list")
          );
          return result.toException("Failed to build AICC manifest");
        });

    this.launchUrl = rootAssignableUnit.getFileName();
  }

  /**
   * Applies course structure data to the given assignable unit. This method configures the
   * assignable unit's properties such as prerequisites, mastery score, maximum time allowed, time
   * limit action, and completion criteria based on the provided course structure.
   *
   * @param assignableUnit The assignable unit to which course structure data will be applied. If
   * null, the method will return without processing.
   * @param courseStructure The course structure containing data that will be used to configure the
   * assignable unit. If null, the method will return without processing.
   */
  private static void applyCourseStructureData(AssignableUnit assignableUnit,
      CourseStructure courseStructure) {
    if (assignableUnit == null || courseStructure == null) {
      return;
    }
    String prerequisites = courseStructure.getPrerequisites();
    if (StringUtils.isNotBlank(prerequisites)) {
      assignableUnit.setPrerequisitesExpression(prerequisites.trim());
    }
    Map<String, String> attributes = courseStructure.getAttributes();
    if (attributes.isEmpty()) {
      return;
    }
    AiccCompletionCriteria completionCriteria = createCompletionCriteria(attributes);
    String mastery = firstAttribute(attributes, "MASTERY_SCORE", "MS", "MASTERYSCORE");
    if (StringUtils.isBlank(assignableUnit.getMasteryScore()) && StringUtils.isNotBlank(mastery)) {
      assignableUnit.setMasteryScore(mastery.trim());
    }
    String maxTime = firstAttribute(attributes, "MT", "MAXTIME", "MAX_TIME", "MAX_TIME_ALLOWED");
    if (StringUtils.isNotBlank(maxTime)) {
      assignableUnit.setMaxTimeAllowed(maxTime.trim());
    }
    String timeLimitAction = firstAttribute(attributes, "TIME_LIMIT_ACTION", "TL", "TLA");
    if (StringUtils.isNotBlank(timeLimitAction)) {
      assignableUnit.setTimeLimitAction(timeLimitAction.trim());
    }
    Boolean mandatory = extractMandatoryFlag(attributes);
    if (mandatory != null) {
      assignableUnit.setPrerequisitesMandatoryOverride(mandatory);
    }
    if (completionCriteria != null) {
      populateAdditionalRules(completionCriteria, attributes);
      assignableUnit.setCompletionCriteria(completionCriteria);
    }
  }

  /**
   * Creates an instance of {@code AiccCompletionCriteria} based on the provided attributes. The
   * method extracts specific attribute values (CA, CL, CR) from the input map, and uses them to set
   * the completion action, lesson status, and result status in the {@code AiccCompletionCriteria}
   * object. If none of the specified attributes are present or non-blank, the method returns
   * {@code null}.
   *
   * @param attributes A map of attribute key-value pairs from which the completion criteria
   * attributes (CA, CL, CR) are extracted. The keys are expected to be non-null and
   * case-insensitive.
   * @return An {@code AiccCompletionCriteria} object populated with the related completion
   * attributes if any of the specified attributes (CA, CL, CR) are non-blank. Returns {@code null}
   * if none of the attributes are found or contain valid values.
   */
  private static AiccCompletionCriteria createCompletionCriteria(Map<String, String> attributes) {
    String ca = firstAttribute(attributes, "CA");
    String cl = firstAttribute(attributes, "CL");
    String cr = firstAttribute(attributes, "CR");
    if (StringUtils.isNotBlank(ca) || StringUtils.isNotBlank(cl) || StringUtils.isNotBlank(cr)) {
      AiccCompletionCriteria completionCriteria = new AiccCompletionCriteria();
      if (StringUtils.isNotBlank(ca)) {
        completionCriteria.setCompletionAction(ca.trim());
      }
      if (StringUtils.isNotBlank(cl)) {
        completionCriteria.setCompletionLessonStatus(cl.trim());
      }
      if (StringUtils.isNotBlank(cr)) {
        completionCriteria.setCompletionResultStatus(cr.trim());
      }
      return completionCriteria;
    }
    return null;
  }

  /**
   * Populates additional rules for the specified {@code AiccCompletionCriteria} object by iterating
   * over the given attributes. Any attribute key that is not found in the known attribute keys is
   * added as an additional rule.
   *
   * @param completionCriteria The {@code AiccCompletionCriteria} object where additional rules will
   * be stored. Must not be null.
   * @param attributes A map of attribute key-value pairs. The keys represent the attribute names,
   * and the values represent their corresponding values. Attributes with keys that are not
   * recognized as known attribute keys will be added as additional rules to the
   * {@code completionCriteria}.
   */
  private static void populateAdditionalRules(AiccCompletionCriteria completionCriteria,
      Map<String, String> attributes) {
    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      String key = entry.getKey();
      if (key == null) {
        continue;
      }
      if (!Strings.CI.equalsAny(key, KNOWN_ATTRIBUTE_KEYS)) {
        completionCriteria.putAdditionalRule(key, entry.getValue());
      }
    }
  }

  /**
   * Normalizes the provided list of assignable units by applying defaults from the given course's
   * behavior. If the assignable unit already contains specific values for mastery score, max time
   * allowed, or time limit action, those values will remain unchanged.
   *
   * @param assignableUnits The list of assignable units to be normalized. If null or empty, no
   * operation is performed.
   * @param course The AICC course providing default behavior configurations for normalization.
   */
  private static void normalizeAssignableUnits(List<AssignableUnit> assignableUnits,
      AiccCourse course) {
    if (assignableUnits == null || assignableUnits.isEmpty()) {
      return;
    }
    AiccCourse.CourseBehavior behavior = course == null ? null : course.getCourseBehavior();
    String defaultMastery = behavior == null ? null : behavior.getMasteryScore();
    String defaultMaxTimeAllowed = behavior == null ? null : behavior.getMaxTimeAllowed();
    String defaultTimeLimitAction = behavior == null ? null : behavior.getTimeLimitAction();

    for (AssignableUnit assignableUnit : assignableUnits) {
      if (assignableUnit == null) {
        continue;
      }
      finalizeAssignableUnit(assignableUnit, defaultMastery, defaultMaxTimeAllowed,
          defaultTimeLimitAction);
    }
  }

  /**
   * Finalizes the configuration of an {@code AssignableUnit}. If the {@code AssignableUnit} does
   * not have specific values set for mastery score, maximum time allowed, or time limit action,
   * this method assigns default values to those fields and normalizes them as necessary.
   *
   * @param assignableUnit The {@code AssignableUnit} object to be finalized and normalized. Cannot
   * be null; expected to have its properties set partially or fully before this method is called.
   * @param defaultMasteryScore The default mastery score to use if the {@code AssignableUnit} does
   * not have a specific mastery score set.
   * @param defaultMaxTimeAllowed The default maximum time allowed to use if the
   * {@code AssignableUnit} does not have a specific maximum time allowed set.
   * @param defaultTimeLimitAction The default time limit action to use if the
   * {@code AssignableUnit} does not have a specific time limit action set.
   */
  private static void finalizeAssignableUnit(AssignableUnit assignableUnit,
      String defaultMasteryScore, String defaultMaxTimeAllowed, String defaultTimeLimitAction) {
    String resolvedMastery = StringUtils.isNotBlank(assignableUnit.getMasteryScore())
        ? assignableUnit.getMasteryScore()
        : defaultMasteryScore;
    assignableUnit.setMasteryScoreNormalized(normalizeMasteryScore(resolvedMastery));
    if (StringUtils.isBlank(assignableUnit.getMasteryScore()) && resolvedMastery != null) {
      assignableUnit.setMasteryScore(resolvedMastery.trim());
    }

    String resolvedMaxTime = StringUtils.isNotBlank(assignableUnit.getMaxTimeAllowed())
        ? assignableUnit.getMaxTimeAllowed()
        : defaultMaxTimeAllowed;
    assignableUnit.setMaxTimeAllowedNormalized(parseDuration(resolvedMaxTime));
    if (StringUtils.isBlank(assignableUnit.getMaxTimeAllowed()) && resolvedMaxTime != null) {
      assignableUnit.setMaxTimeAllowed(resolvedMaxTime.trim());
    }

    String resolvedTimeAction = StringUtils.isNotBlank(assignableUnit.getTimeLimitAction())
        ? assignableUnit.getTimeLimitAction()
        : defaultTimeLimitAction;
    assignableUnit.setTimeLimitActionNormalized(parseTimeLimitAction(resolvedTimeAction));
    if (StringUtils.isBlank(assignableUnit.getTimeLimitAction()) && resolvedTimeAction != null) {
      assignableUnit.setTimeLimitAction(resolvedTimeAction.trim());
    }
  }

  /**
   * Normalizes a raw mastery score string into a standardized {@code Double} format. The raw input
   * can be in various formats, such as a percentage (e.g., "85%") or a decimal (e.g., "0.85"). This
   * method strips any unnecessary characters like '%' and adjusts the value appropriately if it
   * appears to be a percentage.
   *
   * @param rawMastery The raw mastery score as a {@code String}. It can include optional percentage
   * symbols and may contain extraneous whitespace. If null or blank, the method returns
   * {@code null}.
   * @return The normalized mastery score as a {@code Double}. Returns {@code null} if the input is
   * invalid or cannot be parsed as a number. If the input is a valid percentage greater than 1, the
   * result is scaled to a decimal (e.g., "85%" becomes 0.85). For valid decimals less than or equal
   * to 1, the same value is returned unchanged.
   */
  public static Double normalizeMasteryScore(String rawMastery) {
    if (StringUtils.isBlank(rawMastery)) {
      return null;
    }
    String cleaned = rawMastery.trim();
    if (cleaned.endsWith("%")) {
      cleaned = cleaned
          .substring(0, cleaned.length() - 1)
          .trim();
    }
    try {
      double value = Double.parseDouble(cleaned);
      if (value > 1d) {
        return value / 100d;
      }
      return value;
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Parses a given string to produce a {@code Duration} object. The method attempts to interpret
   * the input string in different formats and returns the corresponding {@code Duration}. If the
   * input string is blank or cannot be parsed, it returns null.
   *
   * @param raw the string representation of the duration to be parsed; may be in various formats,
   * such as HH:mm:ss or ISO-8601
   * @return a {@code Duration} object if the string is successfully parsed; otherwise null if the
   * input is blank or invalid
   */
  private static Duration parseDuration(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    String trimmed = raw.trim();
    try {
      return DurationHHMMSSDeserializer.parseDuration(trimmed);
    } catch (IllegalArgumentException e) {
      try {
        return Duration.parse(trimmed);
      } catch (Exception ignored) {
        return null;
      }
    }
  }

  /**
   * Parses a raw input string containing time limit actions, processes it, and returns a list of
   * uppercase action strings.
   *
   * @param raw the raw input string containing time limit actions, separated by semicolons or
   * commas. If the input is blank or null, an empty list is returned.
   * @return a list of processed and uppercase action strings, extracted from the input string.
   */
  private static List<String> parseTimeLimitAction(String raw) {
    if (StringUtils.isBlank(raw)) {
      return List.of();
    }
    String sanitized = raw.replace(';', ',');
    String[] parts = sanitized.split(",");
    List<String> actions = new ArrayList<>();
    for (String part : parts) {
      String trimmed = part.trim();
      if (trimmed.isEmpty()) {
        continue;
      }
      actions.add(trimmed.toUpperCase(Locale.ROOT));
    }
    return actions;
  }

  /**
   * Retrieves the value of the first non-blank attribute from the provided map corresponding to the
   * given keys. The keys are processed in order and matched after being trimmed and converted to
   * uppercase.
   *
   * @param attributes the map of attribute key-value pairs. Null or empty map will return null.
   * @param keys the array of keys to search for in the map. Null keys are ignored.
   * @return the value of the first matched key that has a non-blank value, or null if no such
   * key-value pair is found.
   */
  private static String firstAttribute(Map<String, String> attributes, String... keys) {
    if (attributes == null || attributes.isEmpty() || keys == null) {
      return null;
    }
    for (String key : keys) {
      if (key == null) {
        continue;
      }
      String value = attributes.get(key
          .trim()
          .toUpperCase(Locale.ROOT));
      if (StringUtils.isNotBlank(value)) {
        return value;
      }
    }
    return null;
  }

  /**
   * Extracts a boolean "mandatory" flag from the given attributes map based on specific key names.
   * The method checks for keys such as "MANDATORY", "REQUIRED", or "REQ" to determine if the flag
   * is affirmative. If such keys are not found, it checks for keys like "OPTIONAL" and negates the
   * result of its affirmative check.
   *
   * @param attributes a map of key-value pairs that may contain flags indicating mandatory or
   * optional status
   * @return a Boolean indicating whether the mandatory flag is present and affirmative, false if
   * negated by an "OPTIONAL" flag, or null if no relevant attributes are found
   */
  private static Boolean extractMandatoryFlag(Map<String, String> attributes) {
    boolean noAttributes = attributes == null || attributes.isEmpty();
    if (noAttributes) {
      return null;
    }

    Boolean mandatory = readAffirmativeFlag(attributes, MANDATORY_KEYS);
    if (mandatory != null) {
      return mandatory;
    }

    Boolean optional = readAffirmativeFlag(attributes, OPTIONAL_KEYS);
    if (optional != null) {
      return !optional;
    }

    return null;
  }

  /**
   * Reads an affirmative flag from the given attributes map using the specified keys. The method
   * retrieves the first attribute value matching the provided keys and determines if it represents
   * an affirmative value.
   *
   * @param attributes the map containing attribute key-value pairs to search for the value
   * @param keys one or more keys to look for in the attributes map
   * @return a Boolean indicating if the value represents an affirmative flag, or null if no value
   * is found
   */
  private static Boolean readAffirmativeFlag(Map<String, String> attributes, String... keys) {
    String value = firstAttribute(attributes, keys);
    return StringUtils.isNotBlank(value) ? parseAffirmative(value) : null;
  }

  /**
   * Parses a string value to determine if it represents an affirmative response.
   *
   * @param value the string value to be parsed, may be null
   * @return true if the value represents an affirmative response (e.g., "yes", "true", "1"), or
   * does not clearly indicate a negative response; false otherwise
   */
  private static boolean parseAffirmative(String value) {
    if (value == null) {
      return false;
    }
    String normalized = value
        .trim()
        .toLowerCase(Locale.ROOT);
    if (normalized.isEmpty()) {
      return false;
    }
    return switch (normalized) {
      case "y", "yes", "true", "1", "required", "mandatory" -> true;
      case "n", "no", "false", "0", "optional" -> false;
      default -> !normalized.startsWith("n") && !normalized.startsWith("0");
    };
  }

  /**
   * Retrieves the title of the course.
   *
   * @return the title of the course as a String.
   */
  @Override
  @JsonIgnore
  public String getTitle() {
    return this.course
        .getCourse()
        .getCourseTitle();
  }

  /**
   * Retrieves the description of the course.
   * <p>
   * Follows this priority order:
   * <ol>
   *   <li>Root AU descriptor description (from .des file)</li>
   *   <li>Course description (from .crs file [Course_Description] section)</li>
   * </ol>
   * <p>
   * Most AICC authoring tools (including Articulate Storyline) store the primary description
   * in the descriptor file (.des), which can contain multi-line text. The [Course_Description]
   * section in the .crs file is often less detailed or may be parsed incorrectly when it
   * contains special characters like colons.
   *
   * @return the course description as a string
   */
  @Override
  @JsonIgnore
  public String getDescription() {
    // Try to get description from root AU's descriptor first (.des file)
    if (assignableUnits != null && !assignableUnits.isEmpty()) {
      // Find the root assignable unit (the one that matches launchUrl)
      AssignableUnit rootAU = assignableUnits
          .stream()
          .filter(au -> au != null && au.getFileName() != null &&
              au
                  .getFileName()
                  .equals(this.launchUrl))
          .findFirst()
          .orElse(assignableUnits.get(0)); // Fallback to first AU if no match

      if (rootAU != null && rootAU.getDescriptor() != null) {
        String descriptorDescription = rootAU
            .getDescriptor()
            .getDescription();
        if (StringUtils.isNotBlank(descriptorDescription)) {
          return descriptorDescription;
        }
      }
    }

    // Fallback to course description from .crs file
    return this.course.getCourseDescription();
  }

  /**
   * Retrieves the identifier of the course.
   *
   * @return the course identifier as a String
   */
  @Override
  @JsonIgnore
  public String getIdentifier() {
    return this.course
        .getCourse()
        .getCourseId();
  }

  /**
   * Retrieves the version information of the course.
   *
   * @return the version of the course as a string.
   */
  @Override
  @JsonIgnore
  public String getVersion() {
    return this.course
        .getCourse()
        .getVersion();
  }

  /**
   * Retrieves the duration associated with this instance.
   *
   * @return a {@code Duration} object representing the duration, which will always return
   * {@code Duration.ZERO}.
   */
  @Override
  @JsonIgnore
  public Duration getDuration() {
    return Duration.ZERO;
  }

  /**
   * Retrieves the course object associated with this instance.
   *
   * @return an AiccCourse object representing the course.
   */
  public AiccCourse getCourse() {
    return this.course;
  }

  /**
   * Sets the course for the current instance.
   *
   * @param course the AICC course to be set
   */
  public void setCourse(AiccCourse course) {
    this.course = course;
  }

  /**
   * Retrieves the list of assignable units.
   *
   * @return a list of AssignableUnit objects representing the assignable units.
   */
  public List<AssignableUnit> getAssignableUnits() {
    return this.assignableUnits;
  }

  /**
   * Sets the list of assignable units.
   *
   * @param assignableUnits the list of {@code AssignableUnit} objects to be assigned
   */
  public void setAssignableUnits(List<AssignableUnit> assignableUnits) {
    this.assignableUnits = assignableUnits;
  }

  /**
   * Retrieves the list of descriptors.
   *
   * @return a list of Descriptor objects representing the stored descriptors.
   */
  public List<Descriptor> getDescriptors() {
    return this.descriptors;
  }

  /**
   * Sets the list of descriptors.
   *
   * @param descriptors the list of Descriptor objects to be set
   */
  public void setDescriptors(List<Descriptor> descriptors) {
    this.descriptors = descriptors;
  }

  /**
   * Retrieves the list of course structures.
   *
   * @return a list of CourseStructure objects representing the course structures.
   */
  public List<CourseStructure> getCourseStructures() {
    return this.courseStructures;
  }

  /**
   * Sets the list of course structures.
   *
   * @param courseStructures the list of CourseStructure objects to be assigned
   */
  public void setCourseStructures(List<CourseStructure> courseStructures) {
    this.courseStructures = courseStructures;
  }

  /**
   * Retrieves the launch URL.
   *
   * @return the launch URL as a String
   */
  @Override
  public String getLaunchUrl() {
    return this.launchUrl;
  }

  /**
   * Sets the launch URL for this object.
   *
   * @param launchUrl the URL to be set as the launch URL
   */
  public void setLaunchUrl(String launchUrl) {
    this.launchUrl = launchUrl;
  }

  /**
   * Retrieves the prerequisites table containing a list of maps where each map represents a set of
   * prerequisite details with key-value pairs.
   *
   * @return a list of maps, where each map contains prerequisite details as key-value pairs.
   */
  public List<Map<String, String>> getPrerequisitesTable() {
    return this.prerequisitesTable;
  }

  /**
   * Sets the prerequisites table with the provided list of maps containing prerequisite details.
   *
   * @param prerequisitesTable the list of maps where each map represents a prerequisite. Each map
   * should contain key-value pairs with string data related to the prerequisites.
   */
  public void setPrerequisitesTable(List<Map<String, String>> prerequisitesTable) {
    this.prerequisitesTable = prerequisitesTable;
  }

  /**
   * Retrieves the objectives relation table.
   *
   * @return a list of maps where each map contains key-value pairs representing the objectives
   * relations.
   */
  public List<Map<String, String>> getObjectivesRelationTable() {
    return this.objectivesRelationTable;
  }

  /**
   * Sets the objectives relation table with the provided list of mappings.
   *
   * @param objectivesRelationTable a list of maps where each map represents a relationship between
   * objectives. The keys and values in the map are expected to be string representations of the
   * relevant data.
   */
  public void setObjectivesRelationTable(List<Map<String, String>> objectivesRelationTable) {
    this.objectivesRelationTable = objectivesRelationTable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccManifest that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCourse(), that.getCourse())
        .append(getAssignableUnits(), that.getAssignableUnits())
        .append(getDescriptors(), that.getDescriptors())
        .append(getCourseStructures(), that.getCourseStructures())
        .append(getLaunchUrl(), that.getLaunchUrl())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCourse())
        .append(getAssignableUnits())
        .append(getDescriptors())
        .append(getCourseStructures())
        .append(getLaunchUrl())
        .toHashCode();
  }
}
