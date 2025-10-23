package dev.jcputney.elearning.parser;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.jcputney.elearning.parser.api.ModuleParser;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class JsonSerializationTest {

  static ObjectMapper createConfiguredObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

  @Test
  void testScorm2004JsonSerialization()
      throws IOException, ModuleDetectionException, ModuleException {
    String modulePath = "src/test/resources/modules/zips/scorm2004.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertThat(parser).isInstanceOf(Scorm2004Parser.class);

    // Parse metadata
    ModuleMetadata<?> metadata = parser.parse();
    assertThat(metadata).isInstanceOf(Scorm2004Metadata.class);
    assertThat(metadata.getModuleType()).isEqualTo(ModuleType.SCORM_2004);

    // Serialize to JSON
    ObjectMapper mapper = createConfiguredObjectMapper();
    String json = mapper.writeValueAsString(metadata);
    System.out.println("Serialized JSON:");
    System.out.println(json);

    // Parse JSON to check module type
    JsonNode jsonNode = mapper.readTree(json);
    ModuleType moduleType = ModuleType.valueOf(jsonNode
        .get("moduleType")
        .asText());
    assertThat(moduleType).isEqualTo(ModuleType.SCORM_2004);

    // Deserialize back to Java object
    ModuleMetadata<?> result = mapper.readValue(json, Scorm2004Metadata.class);

    // Check basic equality
    assertThat(result.getModuleType()).isEqualTo(metadata.getModuleType());
    assertThat(result.getTitle()).isEqualTo(metadata.getTitle());
    assertThat(result.getDescription()).isEqualTo(metadata.getDescription());

    // Check if objects are equal
    assertThat(metadata).isEqualTo(result);
  }
}