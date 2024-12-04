package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.output.scorm2004.Scorm2004Metadata;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SCORM 2004 parser.
 */
public class Scorm2004ParserTest {

  @Test
  void testParseContentPackagingMetadataSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));

    Scorm2004Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm2004Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    parser.loadExternalMetadata(manifest);

    assertNotNull(manifest.getMetadata().getLom().getGeneral());
    assertEquals(manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().size(), 2);
    assertEquals(manifest.getMetadata().getLom().getGeneral().getKeywords().size(), 3);
    assertEquals("Golf Explained - Metadata Example", manifest.getOrganizations().getDefaultOrganization().getTitle());
    assertEquals("Golf Explained", manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().get(0).getValue());
    assertEquals("Explicó Golf", manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().get(1).getValue());
    assertEquals("Golf Explained - Metadata Example", manifest.getTitle());
    assertEquals(39, manifest.getResources().getResourceList().get(0).getFiles().size());
  }

  @Test
  void testParseContentPackagingOneFilePerSCOSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);
    parser.loadExternalMetadata(manifest);
    assertNull(manifest.getMetadata().getLom());
    assertEquals("Golf Explained - CP One File Per SCO", manifest.getOrganizations().getDefaultOrganization().getTitle());
  }
}
