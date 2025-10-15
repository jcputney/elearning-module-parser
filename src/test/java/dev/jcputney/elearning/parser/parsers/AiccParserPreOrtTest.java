package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AiccParserPreOrtTest {

  private static Path fixtureRoot() {
    return Path
        .of("src", "test", "resources", "modules", "aicc", "pre-ort")
        .toAbsolutePath();
  }

  @Test
  void parsesOptionalPreAndOrtAndSurfacesSummaries() throws Exception {
    var access = new LocalFileAccess(fixtureRoot().toString());
    var parser = new AiccParser(access);

    AiccMetadata metadata = parser.parse();

    // Basic validations
    assertEquals("AICC With PRE/ORT", metadata.getTitle());
    assertEquals("default.htm", metadata.getLaunchUrl());

    // AU details from base AICC metadata
    var auIds = metadata.getAssignableUnitIds();
    assertTrue(auIds.contains("A1"));
    assertTrue(auIds.contains("A2"));

    // Prerequisites summary
    int edgeCount = metadata.getPrerequisitesEdgeCount();
    assertEquals(1, edgeCount);

    // Graph is optional but should be present for our headers
    Map<String, List<String>> graph = metadata.getPrerequisitesGraph();
    assertTrue(graph.containsKey("A2"));
    assertEquals(List.of("A1"), graph.get("A2"));

    // Objectives summary
    int relationCount = metadata.getObjectivesRelationCount();
    assertEquals(2, relationCount);

    List<String> objectiveIds = metadata.getObjectiveIds();
    assertTrue(objectiveIds.contains("OBJ1"));

    Map<String, List<String>> byAu = metadata.getObjectivesByAu();
    assertEquals(List.of("OBJ1"), byAu.get("A1"));
    assertEquals(List.of("OBJ1"), byAu.get("A2"));

    assertTrue(metadata.requiresLevel2());
    assertTrue(metadata.requiresLevel3());
    assertFalse(metadata.requiresLevel4());
    assertTrue(metadata
        .getParsedPrerequisites()
        .isEmpty());

    var objectiveMetadata = metadata.getObjectiveMetadata();
    assertEquals(1, objectiveMetadata.size());
    assertEquals(List.of("A1", "A2"), objectiveMetadata
        .get(0)
        .getAssociatedAuIds());
  }
}
