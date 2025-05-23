import static org.junit.jupiter.api.Assertions.*;

import dev.jcputney.elearning.parser.output.metadata.CompositeMetadata;
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests for {@link CompositeMetadata}. */
class CompositeMetadataTest {

  @Test
  void addComponentsAndQuery() {
    SimpleMetadata comp1 = new SimpleMetadata().addMetadata("a", 1);
    SimpleMetadata comp2 = new SimpleMetadata().addMetadata("b", 2);

    CompositeMetadata composite = new CompositeMetadata();
    composite.addComponent(comp1).addComponent(comp2);

    assertEquals(1, composite.getMetadata("a", Integer.class).orElseThrow());
    assertEquals(2, composite.getMetadata("b", Integer.class).orElseThrow());
    assertTrue(composite.hasMetadata("a"));
    assertFalse(composite.hasMetadata("c"));
  }

  @Test
  void equalityAndHashCode() {
    SimpleMetadata c = new SimpleMetadata().addMetadata("k", "v");
    CompositeMetadata one = new CompositeMetadata().addComponent(c);
    CompositeMetadata two = new CompositeMetadata().addComponent(new SimpleMetadata().addMetadata("k", "v"));

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }
}
