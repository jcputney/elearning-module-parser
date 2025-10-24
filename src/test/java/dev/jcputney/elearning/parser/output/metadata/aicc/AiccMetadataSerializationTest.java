package dev.jcputney.elearning.parser.output.metadata.aicc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class AiccMetadataSerializationTest {

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  private static Path complexFixtureRoot() {
    return Path
        .of("src", "test", "resources", "modules", "aicc", "complex")
        .toAbsolutePath();
  }

  private static JsonNode findByAssignableUnit(JsonNode array, String assignableUnitId) {
    if (array == null || !array.isArray()) {
      return null;
    }
    for (JsonNode element : array) {
      if (assignableUnitId.equals(element
          .path("assignableUnitId")
          .asText())) {
        return element;
      }
    }
    return null;
  }

  private static JsonNode findObjective(JsonNode array, String objectiveId) {
    if (array == null || !array.isArray()) {
      return null;
    }
    for (JsonNode element : array) {
      if (objectiveId.equals(element
          .path("id")
          .asText())) {
        return element;
      }
    }
    return null;
  }

  private static List<String> toList(JsonNode node) {
    if (node == null || !node.isArray()) {
      return List.of();
    }
    List<String> values = new ArrayList<>();
    for (JsonNode element : node) {
      values.add(element.asText());
    }
    return values;
  }

  @Test
  void serializesDerivedAiccFields() throws Exception {
    var parser = new AiccParser(new LocalFileAccess(complexFixtureRoot().toString()));
    AiccMetadata metadata = parser.parseOnly();

    String json = MAPPER.writeValueAsString(metadata);
    JsonNode root = MAPPER.readTree(json);

    JsonNode childrenNode = root.get("assignableUnitChildren");
    assertNotNull(childrenNode);
    metadata
        .getAssignableUnitChildren()
        .forEach((key, value) -> assertEquals(value, toList(childrenNode.get(key))));

    JsonNode prerequisitesNode = root.get("parsedPrerequisites");
    assertNotNull(prerequisitesNode);
    List<AiccPrerequisite> prerequisites = metadata.getParsedPrerequisites();
    assertEquals(prerequisites.size(), prerequisitesNode.size());
    for (AiccPrerequisite prerequisite : prerequisites) {
      JsonNode prerequisiteNode = findByAssignableUnit(prerequisitesNode,
          prerequisite.getAssignableUnitId());
      assertNotNull(prerequisiteNode);
      String rawExpression = prerequisiteNode.hasNonNull("rawExpression")
          ? prerequisiteNode
          .get("rawExpression")
          .asText()
          : null;
      assertEquals(prerequisite.getRawExpression(), rawExpression);
      assertEquals(prerequisite.isMandatory(), prerequisiteNode
          .get("mandatory")
          .asBoolean());
      assertEquals(prerequisite.getTokens(), toList(prerequisiteNode.get("tokens")));
      assertEquals(prerequisite.getPostfixTokens(), toList(prerequisiteNode.get("postfixTokens")));
    }

    JsonNode graphNode = root.get("prerequisitesGraph");
    assertNotNull(graphNode);
    metadata
        .getPrerequisitesGraph()
        .forEach((key, value) -> assertEquals(value, toList(graphNode.get(key))));

    JsonNode objectiveIdsNode = root.get("objectiveIds");
    assertEquals(metadata.getObjectiveIds(), toList(objectiveIdsNode));

    JsonNode objectivesByAuNode = root.get("objectivesByAu");
    assertNotNull(objectivesByAuNode);
    metadata
        .getObjectivesByAu()
        .forEach((key, value) -> assertEquals(value, toList(objectivesByAuNode.get(key))));

    JsonNode objectiveMetadataNode = root.get("objectiveMetadata");
    assertEquals(metadata
        .getObjectiveMetadata()
        .size(), objectiveMetadataNode.size());
    metadata
        .getObjectiveMetadata()
        .forEach(obj -> {
          JsonNode jsonObj = findObjective(objectiveMetadataNode, obj.getId());
          assertNotNull(jsonObj);
          assertEquals(obj.getAssociatedAuIds(), toList(jsonObj.get("associatedAuIds")));
        });

    assertEquals(metadata.requiresLevel2(), root
        .get("requiresLevel2")
        .asBoolean());
    assertEquals(metadata.requiresLevel3(), root
        .get("requiresLevel3")
        .asBoolean());
    assertEquals(metadata.requiresLevel4(), root
        .get("requiresLevel4")
        .asBoolean());
  }
}
