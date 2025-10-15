package dev.jcputney.elearning.parser.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import org.junit.jupiter.api.Test;

class Scorm2004ParserTest {

  @Test
  void resolvesLaunchUrlFromDefaultOrganizationItem() throws Exception {
    String path = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    LocalFileAccess access = new LocalFileAccess(path);
    ModuleParserFactory factory = new DefaultModuleParserFactory(access);
    ModuleMetadata<?> metadata = factory.parseModule();

    assertThat(metadata).isInstanceOf(Scorm2004Metadata.class);
    // First launchable item under default org points to Playing/Playing.html
    assertThat(metadata.getLaunchUrl()).isEqualTo("Playing/Playing.html");
  }

  @Test
  void detectsEditionFromSchemaVersion_2nd_3rd_4th() throws Exception {
    // 2nd Edition (CAM 1.3)
    LocalFileAccess access2 = new LocalFileAccess(
        "src/test/resources/modules/scorm2004/ContentPackagingSingleSCO_SCORM20042ndEdition");
    ModuleParserFactory factory2 = new DefaultModuleParserFactory(access2);
    ModuleMetadata<?> metadata2 = factory2.parseModule();
    assertThat(metadata2).isInstanceOf(Scorm2004Metadata.class);
    assertThat(metadata2.getModuleEditionType()).isEqualTo(
        ModuleEditionType.SCORM_2004_2ND_EDITION);

    // 3rd Edition
    LocalFileAccess access3 = new LocalFileAccess(
        "src/test/resources/modules/scorm2004/ContentPackagingSingleSCO_SCORM20043rdEdition");
    ModuleParserFactory factory3 = new DefaultModuleParserFactory(access3);
    ModuleMetadata<?> metadata3 = factory3.parseModule();
    assertThat(metadata3).isInstanceOf(Scorm2004Metadata.class);
    assertThat(metadata3.getModuleEditionType()).isEqualTo(
        ModuleEditionType.SCORM_2004_3RD_EDITION);

    // 4th Edition
    LocalFileAccess access4 = new LocalFileAccess(
        "src/test/resources/modules/scorm2004/SequencingPostTestRollup4thEd_SCORM20044thEdition");
    ModuleParserFactory factory4 = new DefaultModuleParserFactory(access4);
    ModuleMetadata<?> metadata4 = factory4.parseModule();
    assertThat(metadata4).isInstanceOf(Scorm2004Metadata.class);
    assertThat(metadata4.getModuleEditionType()).isEqualTo(
        ModuleEditionType.SCORM_2004_4TH_EDITION);
  }

  @Test
  void detects2ndEditionFromAdlcpNamespaceWhenSchemaVersionMissing() throws Exception {
    String path =
        "src/test/resources/modules/scorm2004/ContentPackagingNoSchemaVersion_SCORM20042ndEdition";
    LocalFileAccess access = new LocalFileAccess(path);
    ModuleParserFactory factory = new DefaultModuleParserFactory(access);
    ModuleMetadata<?> metadata = factory.parseModule();

    assertThat(metadata).isInstanceOf(Scorm2004Metadata.class);
    assertThat(metadata.getModuleEditionType()).isEqualTo(ModuleEditionType.SCORM_2004_2ND_EDITION);
  }
}
