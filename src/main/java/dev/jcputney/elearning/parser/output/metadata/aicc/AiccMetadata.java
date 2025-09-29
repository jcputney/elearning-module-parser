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

package dev.jcputney.elearning.parser.output.metadata.aicc;

import static dev.jcputney.elearning.parser.input.aicc.AiccManifest.normalizeMasteryScore;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.input.aicc.CourseStructure;
import dev.jcputney.elearning.parser.input.aicc.prereq.AiccPrerequisiteExpression;
import dev.jcputney.elearning.parser.input.aicc.prereq.AiccPrerequisiteParser;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents metadata for an AICC eLearning module, including AICC-specific fields such as course
 * structure, assignable units, prerequisites, objectives, credit type, time limit actions, and
 * objective relationships.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class to provide metadata specific to AICC
 * modules, enabling structured storage of AICC format details.
 * </p>
 */
public class AiccMetadata extends BaseModuleMetadata<AiccManifest> {

  /**
   * A constant representing the delimiter character used in legacy systems. This character is
   * utilized as a separator to parse or format data in older formats or applications where
   * compatibility with historical data structures is required. The chosen delimiter is the pipe
   * character ('|').
   */
  private static final char LEGACY_DELIMITER = '|';

  /**
   * A constant that defines the standard delimiter character used for separating values or elements
   * within a data structure or format. The delimiter is defined as a semicolon (';') in this
   * context. It is typically used to ensure a consistent and predictable way of parsing or
   * formatting data across various operations.
   */
  private static final char STANDARD_DELIMITER = ';';

  /**
   * Constant representing the character used to separate keys and values in a key-value pair.
   * Typically used in parsing or generating strings that represent key-value mappings.
   */
  private static final char KEY_VALUE_SEPARATOR = '=';

  /**
   * A regular expression pattern representing the delimiter used for splitting strings. The
   * delimiter defined is a semicolon (";").
   */
  private static final String DELIMITER_REGEX = ";"; // used in split

  /**
   * A list that holds the IDs of units that can be assigned. This list is immutable after
   * initialization, ensuring that the IDs once added cannot be modified or removed.
   */
  private final List<String> assignableUnitIds = new ArrayList<>();

  /**
   * A list of unit names that can be assigned for a specific purpose or function. This list is
   * immutable, ensuring that the references cannot be reassigned.
   */
  private final List<String> assignableUnitNames = new ArrayList<>();

  /**
   * A map that associates a unique identifier of an assignable unit (as a string) with a list of
   * its child identifiers. The map preserves the insertion order of the entries.
   * <p>
   * This data structure is used to manage and organize relationships between parent and child
   * units, where each parent unit can have multiple children. The keys represent the parent units,
   * and the values are lists containing the identifiers of the corresponding child units.
   */
  private final Map<String, List<String>> assignableUnitChildren = new LinkedHashMap<>();

  /**
   * A list containing parsed prerequisites for a course or module. This represents the
   * prerequisites parsed from an AICC (Aviation Industry Computer-Based Training Committee) format,
   * encapsulated as instances of {@code AiccPrerequisite}.
   * <p>
   * The list is immutable once initialized, as it is declared as a final field. Any modifications
   * to the elements or structure of this list should be handled by modifying its contents
   * directly.
   */
  private final List<AiccPrerequisite> parsedPrerequisites = new ArrayList<>();

  /**
   * A list that holds instances of AiccObjectiveMetadata, representing metadata associated with
   * AICC objectives. This list is immutable and cannot be modified directly after initialization.
   */
  private final List<AiccObjectiveMetadata> objectiveMetadata = new ArrayList<>();

  /**
   * A data structure to represent a graph where the keys are course names and the values are lists
   * of course prerequisites for the respective key.
   * <p>
   * This graph uses a {@code LinkedHashMap} to maintain the insertion order of courses, ensuring
   * predictable traversal order.
   * <p>
   * The keys in the map represent a course, and the associated value is a list of strings, each
   * string being the name of a prerequisite course.
   */
  private final Map<String, List<String>> prerequisitesGraph = new LinkedHashMap<>();

  /**
   * A list of objective identifiers.
   * <p>
   * This variable stores a collection of unique string identifiers representing specific
   * objectives. These identifiers are used to reference and manage objectives throughout the
   * application. Once initialized, this list cannot be reassigned due to being declared as final.
   */
  private final List<String> objectiveIds = new ArrayList<>();

  /**
   * A map that associates a string key representing an "Au" (likely indicating a specific entity or
   * identifier) with a list of objectives represented as strings. The map maintains the order of
   * insertion due to the usage of LinkedHashMap.
   * <p>
   * This variable is defined as final, meaning its reference cannot be reassigned. However,
   * contents within the map (e.g., entries, values) can still be modified if not explicitly handled
   * otherwise.
   */
  private final Map<String, List<String>> objectivesByAu = new LinkedHashMap<>();

  /**
   * Represents the count of edges in a graph that signify prerequisites in the context of a task,
   * course, or process dependency structure. This variable typically stores the number of directed
   * edges in a directed acyclic graph (DAG) representing prerequisite relationships.
   */
  private Integer prerequisitesEdgeCount;

  /**
   * Represents the count of relationships or associations between objectives. This variable stores
   * the number of links or dependencies that exist among the defined objectives in a specific
   * context.
   */
  private Integer objectivesRelationCount;

  /**
   * A boolean variable indicating whether Level 2 is required. This flag determines if certain
   * operations, features, or conditions depend on the activation or inclusion of Level 2
   * functionality.
   */
  private boolean requiresLevel2;

  /**
   * Indicates whether the current operation or feature requires Level 3 access. This flag
   * determines if a higher level of authorization or privileges is needed.
   */
  private boolean requiresLevel3;

  /**
   * Indicates whether Level 4 is required for the current operation or condition.
   * <p>
   * This variable is a flag that determines if the process, task, or feature must adhere to or
   * operate under Level 4 requirements. When set to true, Level 4 is necessary; otherwise, it is
   * not required.
   */
  private boolean requiresLevel4;

  /**
   * Creates and initializes an instance of AiccMetadata based on the provided AICC manifest.
   *
   * @param manifest the AICC manifest from which metadata will be extracted
   * @param xapiEnabled a boolean indicating whether xAPI is enabled
   * @return a populated instance of AiccMetadata containing metadata extracted from the manifest
   */
  public static AiccMetadata create(AiccManifest manifest, boolean xapiEnabled) {
    if (manifest == null) {
      throw new IllegalArgumentException("Manifest cannot be null");
    }
    AiccMetadata metadata = new AiccMetadata();
    metadata.manifest = manifest;
    metadata.moduleType = ModuleType.AICC;
    metadata.moduleEditionType = ModuleEditionType.AICC;
    metadata.xapiEnabled = xapiEnabled;

    int declaredLevel = parseDeclaredLevel(manifest);
    metadata.requiresLevel2 = declaredLevel >= 2;
    metadata.requiresLevel3 = declaredLevel >= 3;
    metadata.requiresLevel4 = declaredLevel >= 4;

    List<AssignableUnit> assignableUnits = manifest.getAssignableUnits();
    populateAssignableUnits(metadata, assignableUnits);

    Map<String, List<String>> childMap = buildChildMap(manifest.getCourseStructures());
    if (!childMap.isEmpty()) {
      metadata.assignableUnitChildren.putAll(childMap);
    }

    List<Map<String, String>> prerequisiteRows = manifest.getPrerequisitesTable();
    populatePrerequisitesSummary(metadata, prerequisiteRows);

    List<Map<String, String>> objectiveRelationRows = manifest.getObjectivesRelationTable();
    populateObjectivesRelationSummary(metadata, objectiveRelationRows);

    populateAssignableUnitPrerequisites(metadata, assignableUnits);
    populateObjectiveMetadata(metadata, objectiveRelationRows);

    if (!metadata.parsedPrerequisites.isEmpty()) {
      metadata.requiresLevel2 = true;
    }
    if (!metadata.objectiveMetadata.isEmpty()) {
      metadata.requiresLevel3 = true;
    }

    return metadata;
  }

  /**
   * Populates the AiccMetadata object with assignable unit IDs and names extracted from the
   * provided list of assignable units.
   *
   * @param metadata the AiccMetadata object to update with assignable unit data
   * @param assignableUnits the list of AssignableUnit objects containing system IDs and file names
   */
  private static void populateAssignableUnits(AiccMetadata metadata,
      List<AssignableUnit> assignableUnits) {
    if (assignableUnits == null || assignableUnits.isEmpty()) {
      return;
    }
    metadata.assignableUnitIds.addAll(
        assignableUnits
            .stream()
            .map(AssignableUnit::getSystemId)
            .toList()
    );
    metadata.assignableUnitNames.addAll(
        assignableUnits
            .stream()
            .map(AssignableUnit::getFileName)
            .toList()
    );
  }

  /**
   * Populates the prerequisites summary for the provided AiccMetadata object. Updates the metadata
   * with the total count of prerequisite edges and constructs a directed graph representation of
   * the prerequisites from the provided data.
   *
   * @param metadata the AiccMetadata instance to be updated with prerequisite details
   * @param prerequisiteRows a list of maps representing prerequisite relationships where each map
   * defines a single prerequisite link
   */
  private static void populatePrerequisitesSummary(AiccMetadata metadata,
      List<Map<String, String>> prerequisiteRows) {
    if (prerequisiteRows == null || prerequisiteRows.isEmpty()) {
      return;
    }
    metadata.prerequisitesEdgeCount = prerequisiteRows.size();
    metadata.requiresLevel2 = true;

    Map<String, List<String>> graph = buildPrerequisitesGraph(prerequisiteRows);
    if (!graph.isEmpty()) {
      metadata.prerequisitesGraph.putAll(graph);
    }
  }

  /**
   * Populates the objectives relationship summary for the provided AiccMetadata instance. Updates
   * the metadata with the total count of objective relations, adjusts level requirements, and maps
   * objectives to associated assignable units (AUs) based on the provided data.
   *
   * @param metadata the AiccMetadata instance to be updated with objective relation details
   * @param objectiveRelationRows a list of maps representing objective relationship data where each
   * map defines a single objective-to-AU relationship
   */
  private static void populateObjectivesRelationSummary(AiccMetadata metadata,
      List<Map<String, String>> objectiveRelationRows) {
    if (objectiveRelationRows == null || objectiveRelationRows.isEmpty()) {
      return;
    }
    metadata.objectivesRelationCount = objectiveRelationRows.size();
    metadata.requiresLevel3 = true;

    String objectiveKey = findKeyContaining(objectiveRelationRows.get(0), "objective");
    if (objectiveKey != null) {
      List<String> ids = new ArrayList<>(extractUniqueValues(objectiveRelationRows, objectiveKey));
      metadata.objectiveIds.addAll(ids);
    }

    String auKey = firstNonNullKey(objectiveRelationRows.get(0),
        "au_system_id", "system_id", "au_id", "au", "systemid");
    if (objectiveKey == null || auKey == null) {
      return;
    }

    Map<String, List<String>> byAu = new LinkedHashMap<>();
    buildRelationMap(objectiveRelationRows, objectiveKey, auKey, byAu);
    if (!byAu.isEmpty()) {
      metadata.objectivesByAu.putAll(byAu);
    }
  }

  /**
   * Builds a mapping of assignable units (AUs) to their associated objectives based on the provided
   * relationship data. Each entry in the result map (`byAu`) associates an AU identifier with a
   * list of unique objective identifiers, ensuring no duplicate objectives are present in the
   * list.
   *
   * @param objectiveRelationRows a list of maps where each map represents a row containing
   * relationships between objectives and assignable units
   * @param objectiveKey the key in each map corresponding to the objective identifier
   * @param auKey the key in each map corresponding to the assignable unit identifier
   * @param byAu the map to populate with entries mapping AU identifiers to unique lists of
   * associated objective identifiers
   */
  private static void buildRelationMap(List<Map<String, String>> objectiveRelationRows,
      String objectiveKey,
      String auKey, Map<String, List<String>> byAu) {
    for (Map<String, String> row : objectiveRelationRows) {
      String au = value(row, auKey);
      String obj = value(row, objectiveKey);
      if (au == null || au.isBlank() || obj == null || obj.isBlank()) {
        continue;
      }
      byAu
          .computeIfAbsent(au, k -> new ArrayList<>())
          .add(obj);
    }
    byAu.replaceAll((k, v) -> new ArrayList<>(new LinkedHashSet<>(v)));
  }

  /**
   * Constructs a directed graph representation of prerequisite relationships from the provided list
   * of rows. Each row specifies a relationship between a "from" node and a "to" node. The method
   * identifies the keys for the "from" and "to" nodes using a heuristic approach based on common
   * column names, and then builds the graph accordingly.
   *
   * @param rows a list of maps where each map represents a row defining a prerequisite relationship
   * with key-value pairs
   * @return a map representing the directed graph of prerequisite relationships, where each key is
   * a node, and the value is a list of nodes it points to
   */
  private static Map<String, List<String>> buildPrerequisitesGraph(List<Map<String, String>> rows) {
    Map<String, List<String>> graph = new LinkedHashMap<>();
    if (rows == null || rows.isEmpty()) {
      return graph;
    }
    // Heuristic: try common from/to column names
    String fromKey = firstNonNullKey(rows.get(0),
        "source", "from", "pre_from", "pre_source", "pre_member", "preid", "pre_id");
    String toKey = firstNonNullKey(rows.get(0),
        "target", "to", "post_to", "post_member", "postid", "post_id", "target_member");
    if (fromKey == null || toKey == null) {
      return graph;
    }
    buildRelationMap(rows, toKey, fromKey, graph);
    return graph;
  }

  /**
   * Extracts a unique set of non-blank values associated with a specified key from a list of maps.
   * Iterates over the provided list of rows, retrieves the value corresponding to the key, and adds
   * it to the result set if it is neither null nor blank. The result preserves the insertion
   * order.
   *
   * @param rows the list of maps representing rows of data, where each map contains key-value
   * pairs
   * @param key the key for which values will be extracted from each map in the list of rows
   * @return a set of unique, non-blank values corresponding to the specified key, in insertion
   * order
   */
  private static Set<String> extractUniqueValues(List<Map<String, String>> rows, String key) {
    Set<String> set = new LinkedHashSet<>();
    for (Map<String, String> row : rows) {
      String val = value(row, key);
      if (val != null && !val.isBlank()) {
        set.add(val);
      }
    }
    return set;
  }

  /**
   * Finds the first key in the provided map whose name contains the specified needle string,
   * ignoring case sensitivity. If no match is found, or if either input is null or empty, the
   * method will return null.
   *
   * @param row the map to search for a key containing the specified needle; may be null or empty
   * @param needle the string to search for within the keys of the map; may be null
   * @return the first key in the map containing the needle string (case-insensitive), or null if no
   * match is found or input is invalid
   */
  private static String findKeyContaining(Map<String, String> row, String needle) {
    if (row == null || row.isEmpty() || needle == null) {
      return null;
    }
    final String target = needle.toLowerCase(Locale.ROOT);
    return row
        .keySet()
        .stream()
        .filter(Objects::nonNull)
        .filter(k -> k
            .toLowerCase(Locale.ROOT)
            .contains(target))
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves the first key from the provided map that matches any of the specified candidate keys.
   * Returns null if the map or candidates are null, or if no match is found.
   *
   * @param row the map from which to retrieve a key; may be null
   * @param candidates a list of candidate keys to search for in the map; may be null
   * @return the first key from the map matching the candidates, or null if no match is found
   */
  private static String firstNonNullKey(Map<String, String> row, String... candidates) {
    if (row == null || candidates == null) {
      return null;
    }
    for (String candidate : candidates) {
      if (candidate == null) {
        continue;
      }
      String key = findKey(row, candidate);
      if (key != null) {
        return key;
      }
    }
    return null;
  }

  /**
   * Searches for a key in the provided map that matches the specified name, ignoring case
   * sensitivity.
   *
   * @param row the map containing the keys to search through
   * @param name the name to search for in the map's keys
   * @return the matching key if found, or null if no match exists
   */
  private static String findKey(Map<String, String> row, String name) {
    String target = name.toLowerCase(Locale.ROOT);
    for (String k : row.keySet()) {
      if (k != null && k
          .toLowerCase(Locale.ROOT)
          .equals(target)) {
        return k;
      }
    }
    return null;
  }

  /**
   * Retrieves the value associated with the specified key from the provided map, trims it, and
   * returns the result. If the map or key is null, or if the key does not exist in the map, null is
   * returned.
   *
   * @param row the map from which the value is to be retrieved
   * @param key the key whose associated value is to be retrieved and trimmed
   * @return the trimmed value associated with the key, or null if none exists or if the map/key is
   * null
   */
  private static String value(Map<String, String> row, String key) {
    if (row == null || key == null) {
      return null;
    }
    String v = row.get(key);
    return v != null ? v.trim() : null;
  }

  /**
   * Parses and retrieves the declared level value from the provided AiccManifest object. If the
   * manifest or required fields are missing, or if the level cannot be parsed as an integer, the
   * method returns a default value of 1.
   *
   * @param manifest the AiccManifest object containing course and level data
   * @return the parsed declared level as an integer, or 1 if the level is not present, blank, or
   * invalid
   */
  private static int parseDeclaredLevel(AiccManifest manifest) {
    if (manifest == null || manifest.getCourse() == null
        || manifest
        .getCourse()
        .getCourse() == null) {
      return 1;
    }
    String level = manifest
        .getCourse()
        .getCourse()
        .getLevel();
    if (StringUtils.isBlank(level)) {
      return 1;
    }
    try {
      return Integer.parseInt(level.trim());
    } catch (NumberFormatException e) {
      return 1;
    }
  }

  /**
   * Builds a map where the keys are parent blocks and the values are lists of corresponding child
   * members extracted from the provided list of course structures. This method also processes
   * attributes of the course structures to include additional children if applicable.
   *
   * @param courseStructures the list of CourseStructure objects containing the data to build the
   * map. Each CourseStructure should have a parent block and member information. If the list is
   * null or empty, an empty map will be returned.
   * @return a map where each parent block maps to a list of its child members. If no valid entries
   * are found, returns an empty map.
   */
  private static Map<String, List<String>> buildChildMap(List<CourseStructure> courseStructures) {
    Map<String, LinkedHashSet<String>> map = new LinkedHashMap<>();
    if (courseStructures == null) {
      return Map.of();
    }
    for (CourseStructure structure : courseStructures) {
      if (structure == null) {
        continue;
      }
      String parent = structure.getBlock();
      String member = structure.getMember();
      if (StringUtils.isNotBlank(parent) && StringUtils.isNotBlank(member)
          && !"ROOT".equalsIgnoreCase(parent)) {
        map
            .computeIfAbsent(parent.trim(), k -> new LinkedHashSet<>())
            .add(member.trim());
      }
      addChildrenFromAttributes(map, structure);
    }
    if (map.isEmpty()) {
      return Map.of();
    }
    Map<String, List<String>> resolved = new LinkedHashMap<>();
    map.forEach((key, value) -> resolved.put(key, new ArrayList<>(value)));
    return resolved;
  }

  /**
   * Processes the attributes of the given {@code CourseStructure} and adds children entries to the
   * provided map based on specific attribute keys.
   *
   * @param map a map where the key is a parent element and the value is a set of child elements to
   * be updated
   * @param structure the {@code CourseStructure} object containing attributes and hierarchical
   * relationship data
   */
  private static void addChildrenFromAttributes(Map<String, LinkedHashSet<String>> map,
      CourseStructure structure) {
    if (structure == null) {
      return;
    }
    Map<String, String> attributes = structure.getAttributes();
    if (attributes.isEmpty()) {
      return;
    }
    String parent = StringUtils.isNotBlank(structure.getMember())
        ? structure
        .getMember()
        .trim()
        : structure.getBlock();
    addChildren(map, parent, attributes.get("UR"));
    addChildren(map, parent, attributes.get("RT"));
  }

  /**
   * Adds children elements to the specified parent in the given map. The children are parsed from
   * the rawChildren input and added to the LinkedHashSet associated with the parent key in the map.
   * If the parent or rawChildren is blank, the method will not perform any operation.
   *
   * @param map the map where the parent-child relationships will be stored. The key is the parent,
   * and the value is a LinkedHashSet containing the children.
   * @param parent the parent element to which the children will be associated. This value is
   * trimmed before use.
   * @param rawChildren a comma-separated string of children to be added under the parent. Each
   * child is processed and trimmed before being added.
   */
  private static void addChildren(Map<String, LinkedHashSet<String>> map, String parent,
      String rawChildren) {
    if (StringUtils.isBlank(parent) || StringUtils.isBlank(rawChildren)) {
      return;
    }
    List<String> children = splitList(rawChildren);
    if (children.isEmpty()) {
      return;
    }
    LinkedHashSet<String> set = map.computeIfAbsent(parent.trim(), k -> new LinkedHashSet<>());
    for (String child : children) {
      if (StringUtils.isNotBlank(child)) {
        set.add(child.trim());
      }
    }
  }

  /**
   * Splits the input string into a list of trimmed, non-empty substrings based on specified
   * delimiters. The delimiters used for splitting are pipe (|), semicolon (;), and comma (,). If
   * the input string is null, empty, or contains only whitespace, an empty list is returned.
   *
   * @param raw the input string to be split; may contain delimiters (|, ;, ,) separating values
   * @return a list of trimmed, non-empty substrings obtained after splitting the input string
   */
  private static List<String> splitList(String raw) {
    if (StringUtils.isBlank(raw)) {
      return List.of();
    }
    String[] parts = raw.split("[|;,]");
    List<String> values = new ArrayList<>();
    for (String part : parts) {
      String trimmed = part.trim();
      if (!trimmed.isEmpty()) {
        values.add(trimmed);
      }
    }
    return values;
  }

  /**
   * Populates the prerequisites for a list of assignable units and updates the metadata with the
   * parsed prerequisite information. This method processes each assignable unit in the list, parses
   * the "prerequisites" expression, and updates the metadata with the "prerequisites" details. It
   * also determines if level 2 requirements are needed based on the assignable unit's properties.
   *
   * @param metadata the {@code AiccMetadata} object to be updated with prerequisite information.
   * @param assignableUnits the list of {@code AssignableUnit} objects to process and extract
   * prerequisite information from.
   */
  private static void populateAssignableUnitPrerequisites(AiccMetadata metadata,
      List<AssignableUnit> assignableUnits) {
    if (assignableUnits == null || assignableUnits.isEmpty()) {
      return;
    }
    for (AssignableUnit unit : assignableUnits) {
      if (unit == null) {
        continue;
      }
      Optional<AiccPrerequisiteExpression> expression = AiccPrerequisiteParser.parse(
          unit.getPrerequisitesExpression());
      expression.ifPresent(expr -> {
        unit.setPrerequisiteModel(expr);
        List<String> tokens = AiccPrerequisite.toTokenValues(expr.getTokens());
        List<String> postfix = AiccPrerequisite.toTokenValues(expr.getPostfixTokens());
        boolean mandatory = unit.getPrerequisitesMandatoryOverride() != null
            ? unit.getPrerequisitesMandatoryOverride()
            : expr.isMandatory();
        metadata.parsedPrerequisites.add(new AiccPrerequisite(unit.getSystemId(), expr, mandatory,
            expr.getReferencedAuIds(), expr.getOptionalAuIds(), tokens, postfix,
            unit.getCompletionCriteria()));
      });
      if (expression.isPresent() || unit.getCompletionCriteria() != null
          || unit.getMasteryScoreNormalized() != null
          || unit.getMaxTimeAllowedNormalized() != null
          || !unit
          .getTimeLimitActionNormalized()
          .isEmpty()) {
        metadata.requiresLevel2 = true;
      }
    }
  }

  /**
   * Populates the objective metadata for the given AICC metadata object based on the provided list
   * of objective-related rows.
   *
   * @param metadata the AICC metadata object to populate with objective metadata
   * @param ortRows a list of maps where each map represents a row of objective-related data
   */
  private static void populateObjectiveMetadata(AiccMetadata metadata,
      List<Map<String, String>> ortRows) {
    List<AiccObjectiveMetadata> objectives = buildObjectiveMetadataDetails(ortRows);
    if (objectives.isEmpty()) {
      return;
    }
    metadata.objectiveMetadata.addAll(objectives);
    metadata.requiresLevel3 = true;
    boolean advanced = objectives
        .stream()
        .anyMatch(obj -> Boolean.TRUE.equals(obj.getSatisfiedByMeasure())
            || obj.getProgressMeasureWeight() != null);
    if (advanced) {
      metadata.requiresLevel4 = true;
    }
  }

  /**
   * Builds a list of AiccObjectiveMetadata objects based on the provided rows of data.
   * <p>
   * The method processes the given list of maps, extracts relevant keys and values for objective
   * metadata, and organizes them into a structured list of AiccObjectiveMetadata objects using a
   * builder pattern. This involves interpreting and transforming various fields such as objective
   * IDs, descriptions, associations, measures, weights, and status mappings.
   *
   * @param rows a list of maps where each map represents a row of key-value pairs containing
   * metadata information. The keys are expected to contain identifiers for objectives,
   * descriptions, associated AU IDs, satisfaction conditions, minimum measures, weights, and status
   * details.
   * @return a list of constructed AiccObjectiveMetadata objects. If the input list is null, empty,
   * or contains no valid data, an empty list is returned.
   */
  private static List<AiccObjectiveMetadata> buildObjectiveMetadataDetails(
      List<Map<String, String>> rows) {
    if (rows == null || rows.isEmpty()) {
      return List.of();
    }

    // Phase 1: derive header keys once
    Map<String, String> header = rows.get(0);
    String objectiveKey = findKeyContaining(header, "objective");
    if (objectiveKey == null) {
      return List.of();
    }
    String descriptionKey = findKeyContaining(header, "description");
    String auKey = firstNonNullKey(header, "au_system_id", "system_id", "au_id", "au", "member");
    String satisfiedKey = firstNonNullKey(header, "satisfied_by_measure", "satisfied",
        "measure_satisfied");
    String minMeasureKey = firstNonNullKey(header, "min_normalized_measure", "min_measure",
        "minimum_measure", "objective_minimum");
    String weightKey = firstNonNullKey(header, "progress_measure_weight", "weight",
        "measure_weight");
    String statusKey = firstNonNullKey(header, "status_map", "statusmap", "status");

    // Phase 2: process rows, accumulate into structures keyed by objective id
    Map<String, ObjectiveAccumulator> accumulators = new LinkedHashMap<>();

    for (Map<String, String> row : rows) {
      processObjectiveRow(
          row,
          objectiveKey,
          descriptionKey,
          auKey,
          satisfiedKey,
          minMeasureKey,
          weightKey,
          statusKey,
          accumulators
      );
    }

    List<AiccObjectiveMetadata> results = new ArrayList<>(accumulators.size());
    for (ObjectiveAccumulator accumulator : accumulators.values()) {
      results.add(accumulator.toMetadata());
    }
    return results;
  }

  /**
   * Processes a single objective row, extracting relevant fields and populating objective
   * accumulators.
   *
   * @param row the map representing a single row of objective data; typically key-value pairs where
   * keys correspond to column names
   * @param objectiveIdKey the key to identify the "objective ID" field in the row
   * @param descriptionKey the key to identify the "description" field in the row
   * @param auKey the key to identify the "assignable units (AUs)" field in the row
   * @param satisfiedKey the key to identify the "satisfied by measure" field in the row
   * @param minMeasureKey the key to identify the "minimum measure" field in the row
   * @param weightKey the key to identify the "progress measure weight" field in the row
   * @param statusKey the key to identify the "status" field in the row
   * @param accumulators the data structure used to accumulate all processed objective data, where
   * results are added
   */
  private static void processObjectiveRow(
      Map<String, String> row,
      String objectiveIdKey,
      String descriptionKey,
      String auKey,
      String satisfiedKey,
      String minMeasureKey,
      String weightKey,
      String statusKey,
      Map<String, ObjectiveAccumulator> accumulators
  ) {
    if (row == null || row.isEmpty()) {
      return;
    }

    String id = trimOrNull(value(row, objectiveIdKey));
    if (StringUtils.isBlank(id)) {
      return;
    }

    ObjectiveAccumulator accumulator =
        accumulators.computeIfAbsent(id, ObjectiveAccumulator::new);

    applyIfPresent(row, descriptionKey, accumulator::updateDescription);

    applyIfPresent(row, auKey, aus -> accumulator.addAssociatedAus(splitList(aus)));

    applyIfPresent(row, satisfiedKey,
        s -> accumulator.updateSatisfiedByMeasure(parseBooleanValue(s)));

    applyIfPresent(row, minMeasureKey, m -> {
      Double normalized = normalizeMasteryScore(m);
      if (normalized != null) {
        accumulator.updateMinNormalizedMeasure(normalized);
      }
    });

    applyIfPresent(row, weightKey, w -> {
      Double weightValue = firstNonNull(normalizeMasteryScore(w), parseDouble(w));
      if (weightValue != null) {
        accumulator.updateProgressMeasureWeight(weightValue);
      }
    });

    applyIfPresent(row, statusKey, raw -> {
      Map<String, String> parsed = parseStatusMap(raw);
      if (!parsed.isEmpty()) {
        accumulator.mergeStatus(parsed);
      }
    });
  }

  /**
   * Trims the input string, removing any leading or trailing whitespace. If the input string is
   * null, returns null.
   *
   * @param s the string to be trimmed, may be null
   * @return the trimmed string, or null if the input string is null
   */
  private static String trimOrNull(String s) {
    return s == null ? null : s.trim();
  }

  /**
   * Returns the first non-null value among the two provided parameters. If the first parameter is
   * non-null, it is returned; otherwise, the second parameter is returned.
   *
   * @param <T> the type of the parameters
   * @param a the first parameter to check for nullity
   * @param b the second parameter to return if the first is null
   * @return the first non-null parameter, or null if both parameters are null
   */
  private static <T> T firstNonNull(T a, T b) {
    return a != null ? a : b;
  }

  /**
   * Processes a value from the provided map associated with the given key using the specified
   * consumer only if the key is not null and the corresponding value is not blank.
   *
   * @param row the map containing the key-value pairs to process
   * @param key the key to search for in the map
   * @param consumer the consumer to apply to the value if it is present and not blank
   */
  private static void applyIfPresent(Map<String, String> row, String key,
      java.util.function.Consumer<String> consumer) {
    if (key == null) {
      return;
    }
    String val = value(row, key);
    if (StringUtils.isNotBlank(val)) {
      consumer.accept(val);
    }
  }

  /**
   * Parses the provided string into a Double. If the string is null, blank, or cannot be converted
   * to a valid Double, the method returns null.
   *
   * @param raw the string to be parsed into a Double
   * @return the parsed Double value, or null if the string is null, blank, or not a valid
   * representation of a Double
   */
  private static Double parseDouble(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    try {
      return Double.parseDouble(raw.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Parses a raw string value to determine its boolean representation.
   * <p>
   * The method normalizes the input string by trimming whitespace and converting it to lowercase.
   * Based on predefined mappings, it returns a Boolean value or null if the input does not match
   * recognized Boolean representations.
   *
   * @param rawValue the raw string value to be parsed. Can be null or empty; such values will
   * result in a null return.
   * @return a Boolean value (true/false) based on the input string, or null if the input cannot be
   * mapped to a recognized Boolean representation.
   */
  private static Boolean parseBooleanValue(String rawValue) {
    if (StringUtils.isBlank(rawValue)) {
      return null;
    }
    String normalized = rawValue
        .trim()
        .toLowerCase(Locale.ROOT);
    return switch (normalized) {
      case "y", "yes", "true", "t", "1", "on" -> Boolean.TRUE;
      case "n", "no", "false", "f", "0", "off" -> Boolean.FALSE;
      default -> null;
    };
  }

  /**
   * Parses the given raw input string into a map of status key-value pairs. The input string is
   * processed to normalize delimiters and split into individual key-value pair entries. Each entry
   * is validated and then added to the resulting map.
   *
   * @param rawInput the raw input string containing delimited key-value pairs to be parsed. It may
   * include legacy delimiters which will be normalized.
   * @return a map of key-value pairs parsed from the input string. If the input is blank or
   * contains no valid pairs, an empty map is returned.
   */
  private static Map<String, String> parseStatusMap(String rawInput) {
    Map<String, String> statusMap = new LinkedHashMap<>();
    if (StringUtils.isBlank(rawInput)) {
      return statusMap;
    }

    String normalized = rawInput.replace(LEGACY_DELIMITER, STANDARD_DELIMITER);
    // limit = 0 drops trailing empty strings, keeping behavior consistent for trailing delimiters
    String[] entries = normalized.split(DELIMITER_REGEX, 0);

    for (String entry : entries) {
      putIfValidPair(statusMap, entry);
    }

    return statusMap;
  }

  /**
   * Adds a key-value pair to the target map if the given entry is valid. The entry must be
   * non-null, non-empty, and contain a valid key-value pair separated by the defined separator.
   * Both the key and value must be non-empty after trimming.
   *
   * @param target the map where the valid key-value pair will be added
   * @param entry a string containing the key-value pair separated by a specific character
   */
  private static void putIfValidPair(Map<String, String> target, String entry) {
    if (entry == null) {
      return;
    }
    String trimmed = entry.trim();
    if (trimmed.isEmpty()) {
      return;
    }

    int sepIndex = trimmed.indexOf(KEY_VALUE_SEPARATOR);
    if (sepIndex < 0) {
      return;
    }

    String key = trimmed
        .substring(0, sepIndex)
        .trim();
    String value = trimmed
        .substring(sepIndex + 1)
        .trim();

    if (!key.isEmpty() && !value.isEmpty()) {
      target.put(key, value);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(requiresLevel2, that.requiresLevel2)
        .append(requiresLevel3, that.requiresLevel3)
        .append(requiresLevel4, that.requiresLevel4)
        .append(getAssignableUnitIds(), that.getAssignableUnitIds())
        .append(getAssignableUnitNames(), that.getAssignableUnitNames())
        .append(getAssignableUnitChildren(), that.getAssignableUnitChildren())
        .append(getParsedPrerequisites(), that.getParsedPrerequisites())
        .append(getObjectiveMetadata(), that.getObjectiveMetadata())
        .append(getPrerequisitesGraph(), that.getPrerequisitesGraph())
        .append(getObjectiveIds(), that.getObjectiveIds())
        .append(getObjectivesByAu(), that.getObjectivesByAu())
        .append(getPrerequisitesEdgeCount(), that.getPrerequisitesEdgeCount())
        .append(getObjectivesRelationCount(), that.getObjectivesRelationCount())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(getAssignableUnitIds())
        .append(getAssignableUnitNames())
        .append(getAssignableUnitChildren())
        .append(getParsedPrerequisites())
        .append(getObjectiveMetadata())
        .append(getPrerequisitesGraph())
        .append(getObjectiveIds())
        .append(getObjectivesByAu())
        .append(getPrerequisitesEdgeCount())
        .append(getObjectivesRelationCount())
        .append(requiresLevel2)
        .append(requiresLevel3)
        .append(requiresLevel4)
        .toHashCode();
  }

  /**
   * Retrieves a list of assignable unit IDs.
   *
   * @return an immutable list of assignable unit IDs.
   */
  public List<String> getAssignableUnitIds() {
    return List.copyOf(assignableUnitIds);
  }

  /**
   * Retrieves a list of unit names that can be assigned.
   *
   * @return an unmodifiable list of assignable unit names
   */
  public List<String> getAssignableUnitNames() {
    return List.copyOf(assignableUnitNames);
  }

  /**
   * Retrieves the count of prerequisite edges.
   *
   * @return the number of prerequisite edges as an Integer
   */
  public Integer getPrerequisitesEdgeCount() {
    return prerequisitesEdgeCount;
  }

  /**
   * Retrieves a map representing the prerequisites graph of courses or tasks. Each key in the map
   * corresponds to a course or task, and its associated value is a list of prerequisites required
   * to complete the key task or course.
   *
   * @return an unmodifiable map where keys are strings representing individual items (e.g., courses
   * or tasks) and values are lists of strings representing the prerequisites for each item.
   */
  public Map<String, List<String>> getPrerequisitesGraph() {
    return Map.copyOf(prerequisitesGraph);
  }

  /**
   * Retrieves the count of objectives relations.
   *
   * @return the number of objectives relations as an Integer
   */
  public Integer getObjectivesRelationCount() {
    return objectivesRelationCount;
  }

  /**
   * Retrieves the list of objective IDs.
   *
   * @return a list containing the objective IDs as immutable strings
   */
  public List<String> getObjectiveIds() {
    return List.copyOf(objectiveIds);
  }

  /**
   * Retrieves a map containing objectives categorized by assessment units.
   *
   * @return an unmodifiable map where the keys are assessment unit identifiers and the values are
   * lists of objectives associated with each assessment unit.
   */
  public Map<String, List<String>> getObjectivesByAu() {
    return Map.copyOf(objectivesByAu);
  }

  /**
   * Retrieves a map representing the assignable unit children.
   *
   * @return an unmodifiable map where the keys are strings representing the parent units and the
   * values are lists of strings representing their respective child units.
   */
  public Map<String, List<String>> getAssignableUnitChildren() {
    return Map.copyOf(assignableUnitChildren);
  }

  /**
   * Retrieves a list of parsed prerequisites.
   *
   * @return an unmodifiable list containing the parsed prerequisites.
   */
  public List<AiccPrerequisite> getParsedPrerequisites() {
    return List.copyOf(parsedPrerequisites);
  }

  /**
   * Retrieves a list of AiccObjectiveMetadata objects representing the objective metadata.
   *
   * @return an immutable list of AiccObjectiveMetadata objects
   */
  public List<AiccObjectiveMetadata> getObjectiveMetadata() {
    return List.copyOf(objectiveMetadata);
  }

  /**
   * Determines if the level 2 requirement is needed.
   *
   * @return true if level 2 is required, false otherwise.
   */
  @JsonProperty("requiresLevel2")
  public boolean requiresLevel2() {
    return requiresLevel2;
  }

  /**
   * Determines whether level 3 access is required.
   *
   * @return true if level 3 access is required, false otherwise.
   */
  @JsonProperty("requiresLevel3")
  public boolean requiresLevel3() {
    return requiresLevel3;
  }

  /**
   * Indicates whether level 4 access or requirements are necessary.
   *
   * @return true if level 4 is required, otherwise false
   */
  @JsonProperty("requiresLevel4")
  public boolean requiresLevel4() {
    return requiresLevel4;
  }

  /**
   * The ObjectiveAccumulator class is a helper utility designed to accumulate and manage data
   * related to objectives, such as associated attributes, metadata, and measures of progress or
   * satisfaction.
   * <p>
   * This class is intended for internal use and offers methods for updating and transforming
   * objective-related data into a structured format.
   */
  private static final class ObjectiveAccumulator {

    private final String id;
    private final LinkedHashSet<String> associatedAus = new LinkedHashSet<>();
    private final LinkedHashMap<String, String> statusMap = new LinkedHashMap<>();
    private String description;
    private Boolean satisfiedByMeasure;
    private Double minNormalizedMeasure;
    private Double progressMeasureWeight;

    private ObjectiveAccumulator(String id) {
      this.id = id;
    }

    /**
     * Updates the description of the objective after trimming the input value. If the provided
     * value is null, the description will be set to null.
     *
     * @param value the new description of the objective, may be null
     */
    void updateDescription(String value) {
      this.description = trimOrNull(value);
    }

    /**
     * Adds a list of associated assignable units (AUs) to the current set of associated AUs. If the
     * provided list is null or empty, no changes will be made.
     *
     * @param aus the list of associated assignable units to add, may be null or empty
     */
    void addAssociatedAus(List<String> aus) {
      if (aus == null || aus.isEmpty()) {
        return;
      }
      associatedAus.addAll(aus);
    }

    /**
     * Updates the satisfaction state of the objective based on the given measure. If the provided
     * value is null, satisfaction will be set to null.
     *
     * @param value the new satisfaction state of the objective, may be null
     */
    void updateSatisfiedByMeasure(Boolean value) {
      this.satisfiedByMeasure = value;
    }

    /**
     * Updates the minimum normalized measure of the objective. If the provided value is null, the
     * minimum normalized measure will be set to null.
     *
     * @param value the new minimum normalized measure, may be null
     */
    void updateMinNormalizedMeasure(Double value) {
      this.minNormalizedMeasure = value;
    }

    /**
     * Updates the progress measure weight for the objective. If the provided value is null, the
     * progress measure weight will be set to null.
     *
     * @param value the new progress measure weight, may be null
     */
    void updateProgressMeasureWeight(Double value) {
      this.progressMeasureWeight = value;
    }

    /**
     * Merges the provided status mappings into the existing status map of the objective
     * accumulator. If the input map is null or empty, no operation is performed.
     *
     * @param statuses a map containing status keys and their corresponding values to be merged with
     * the existing status map; may be null or empty
     */
    void mergeStatus(Map<String, String> statuses) {
      if (statuses != null && !statuses.isEmpty()) {
        statusMap.putAll(statuses);
      }
    }

    /**
     * Converts the accumulated objective data into an instance of {@code AiccObjectiveMetadata}.
     * The method creates immutable copies of the associated AUs and status map to ensure that the
     * returned metadata object is self-contained and independent of the
     * {@code ObjectiveAccumulator}'s internal state.
     *
     * @return an {@code AiccObjectiveMetadata} object that holds the converted representation of
     * the accumulated objective data, including its ID, description, associated assignable units,
     * satisfaction criteria, progress measure weight, and objective statuses.
     */
    AiccObjectiveMetadata toMetadata() {
      List<String> aus = associatedAus.isEmpty()
          ? List.of()
          : new ArrayList<>(associatedAus);
      Map<String, String> statuses = statusMap.isEmpty()
          ? Map.of()
          : new LinkedHashMap<>(statusMap);
      return new AiccObjectiveMetadata(id, description, aus, satisfiedByMeasure,
          minNormalizedMeasure, progressMeasureWeight, statuses);
    }
  }

}
