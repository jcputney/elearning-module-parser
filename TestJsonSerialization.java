import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.ModuleParser;

public class TestJsonSerialization {
    public static void main(String[] args) throws Exception {
        String modulePath = "src/test/resources/modules/zips/scorm2004.zip";
        ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
                new ZipFileAccess(modulePath));
        ModuleParser<?> parser = parserFactory.getParser();
        
        // Parse metadata
        ModuleMetadata<?> metadata = parser.parse();
        System.out.println("Original metadata type: " + metadata.getClass().getName());
        System.out.println("Module type: " + metadata.getModuleType());
        
        // Serialize to JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        String json = mapper.writeValueAsString(metadata);
        System.out.println("\nSerialized JSON (first 500 chars):");
        System.out.println(json.substring(0, Math.min(500, json.length())));
        
        // Parse JSON to check module type
        JsonNode jsonNode = mapper.readTree(json);
        ModuleType moduleType = ModuleType.valueOf(jsonNode.get("moduleType").asText());
        System.out.println("\nModule type from JSON: " + moduleType);
        
        // Try to deserialize back to Java object
        try {
            ModuleMetadata<?> result = mapper.readValue(json, Scorm2004Metadata.class);
            System.out.println("\nDeserialized metadata type: " + result.getClass().getName());
            System.out.println("Deserialized equals original: " + metadata.equals(result));
        } catch (Exception e) {
            System.err.println("\nDeserialization failed:");
            e.printStackTrace();
        }
    }
}