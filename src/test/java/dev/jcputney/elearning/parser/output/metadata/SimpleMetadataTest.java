import static org.junit.jupiter.api.Assertions.*;

import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Tests for {@link SimpleMetadata}. */
class SimpleMetadataTest {

  @Test
  void addAndRetrieveMetadata() {
    SimpleMetadata metadata = new SimpleMetadata();
    metadata.addMetadata("key1", "value1");
    Map<String, Object> more = new HashMap<>();
    more.put("key2", 5);
    metadata.addAllMetadata(more);

    assertTrue(metadata.getMetadata("key1").isPresent());
    assertEquals("value1", metadata.getMetadata("key1").get());
    assertEquals(5, metadata.getMetadata("key2", Integer.class).orElseThrow());
    assertFalse(metadata.getMetadata("missing").isPresent());
    assertTrue(metadata.hasMetadata("key1"));
    assertFalse(metadata.hasMetadata("missing"));
  }

  @Test
  void equalityAndHashCode() {
    SimpleMetadata meta1 = new SimpleMetadata();
    meta1.addMetadata("k", 1);
    SimpleMetadata meta2 = new SimpleMetadata();
    meta2.addMetadata("k", 1);

    assertEquals(meta1, meta2);
    assertEquals(meta1.hashCode(), meta2.hashCode());
  }
}
