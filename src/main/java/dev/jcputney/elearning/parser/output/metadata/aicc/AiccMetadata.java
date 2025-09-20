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

import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

  private final List<String> assignableUnitIds = new ArrayList<>();
  private final List<String> assignableUnitNames = new ArrayList<>();
  private final Map<String, List<String>> prerequisitesGraph = new LinkedHashMap<>();
  private final List<String> objectiveIds = new ArrayList<>();
  private final Map<String, List<String>> objectivesByAu = new LinkedHashMap<>();
  private Integer prerequisitesEdgeCount;
  private Integer objectivesRelationCount;

  public AiccMetadata() {
  }

  /**
   * Creates a new AiccMetadata instance with standard AICC metadata components.
   *
   * @param manifest The AICC manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new AiccMetadata instance.
   */
  public static AiccMetadata create(AiccManifest manifest, boolean xapiEnabled) {
    AiccMetadata metadata = new AiccMetadata();
    metadata.manifest = manifest;
    metadata.moduleType = ModuleType.AICC;
    metadata.moduleEditionType = ModuleEditionType.AICC;
    metadata.xapiEnabled = xapiEnabled;

    // Add AICC-specific fields
    List<AssignableUnit> assignableUnits = manifest.getAssignableUnits();
    if (assignableUnits != null && !assignableUnits.isEmpty()) {
      // Add assignable unit IDs
      metadata.assignableUnitIds.addAll(assignableUnits
          .stream()
          .map(AssignableUnit::getSystemId)
          .toList());

      // Add assignable unit names
      metadata.assignableUnitNames.addAll(assignableUnits
          .stream()
          .map(AssignableUnit::getFileName)
          .toList());
    }

    // Optional: prerequisites (.pre) summary
    List<Map<String, String>> preRows = manifest.getPrerequisitesTable();
    if (preRows != null && !preRows.isEmpty()) {
      metadata.prerequisitesEdgeCount = preRows.size();
      Map<String, List<String>> graph = buildPrerequisitesGraph(preRows);
      if (!graph.isEmpty()) {
        metadata.prerequisitesGraph.putAll(graph);
      }
    }

    // Optional: objectives relation (.ort) summary
    List<Map<String, String>> ortRows = manifest.getObjectivesRelationTable();
    if (ortRows != null && !ortRows.isEmpty()) {
      metadata.objectivesRelationCount = ortRows.size();

      String objectiveKey = findKeyContaining(ortRows.get(0), "objective");
      if (objectiveKey != null) {
        List<String> objectiveIds = new ArrayList<>(extractUniqueValues(ortRows, objectiveKey));
        metadata.objectiveIds.addAll(objectiveIds);
      }

      String auKey = firstNonNullKey(ortRows.get(0),
          "au_system_id", "system_id", "au_id", "au", "systemid");
      if (objectiveKey != null && auKey != null) {
        Map<String, List<String>> byAu = new LinkedHashMap<>();
        for (Map<String, String> row : ortRows) {
          String au = value(row, auKey);
          String obj = value(row, objectiveKey);
          if (au == null || au.isBlank() || obj == null || obj.isBlank()) {
            continue;
          }
          byAu
              .computeIfAbsent(au, k -> new ArrayList<>())
              .add(obj);
        }
        // de-duplicate lists while preserving order
        byAu.replaceAll((k, v) -> new ArrayList<>(new LinkedHashSet<>(v)));
        if (!byAu.isEmpty()) {
          metadata.objectivesByAu.putAll(byAu);
        }
      }
    }

    return metadata;
  }

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
    for (Map<String, String> row : rows) {
      String from = value(row, fromKey);
      String to = value(row, toKey);
      if (from == null || from.isBlank() || to == null || to.isBlank()) {
        continue;
      }
      graph
          .computeIfAbsent(from, k -> new ArrayList<>())
          .add(to);
    }
    // de-duplicate edges per node
    graph.replaceAll((k, v) -> new ArrayList<>(new LinkedHashSet<>(v)));
    return graph;
  }

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

  private static String firstNonNullKey(Map<String, String> row, String... candidates) {
    if (row == null || candidates == null) {
      return null;
    }
    for (String cand : candidates) {
      if (cand == null) {
        continue;
      }
      String key = findKey(row, cand);
      if (key != null) {
        return key;
      }
    }
    return null;
  }

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

  private static String value(Map<String, String> row, String key) {
    if (row == null || key == null) {
      return null;
    }
    String v = row.get(key);
    return v != null ? v.trim() : null;
  }

  public List<String> getAssignableUnitIds() {
    return List.copyOf(assignableUnitIds);
  }

  public List<String> getAssignableUnitNames() {
    return List.copyOf(assignableUnitNames);
  }

  public Integer getPrerequisitesEdgeCount() {
    return prerequisitesEdgeCount;
  }

  public Map<String, List<String>> getPrerequisitesGraph() {
    return Map.copyOf(prerequisitesGraph);
  }

  public Integer getObjectivesRelationCount() {
    return objectivesRelationCount;
  }

  public List<String> getObjectiveIds() {
    return List.copyOf(objectiveIds);
  }

  public Map<String, List<String>> getObjectivesByAu() {
    return Map.copyOf(objectivesByAu);
  }
}
