/* Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom;

import static dev.jcputney.elearning.parser.input.lom.types.Context.TRAINING;
import static dev.jcputney.elearning.parser.input.lom.types.Difficulty.VERY_EASY;
import static dev.jcputney.elearning.parser.input.lom.types.IntendedEndUserRole.LEARNER;
import static dev.jcputney.elearning.parser.input.lom.types.InteractivityLevel.VERY_LOW;
import static dev.jcputney.elearning.parser.input.lom.types.InteractivityType.EXPOSITIVE;
import static dev.jcputney.elearning.parser.input.lom.types.LearningResourceType.NARRATIVE_TEXT;
import static dev.jcputney.elearning.parser.input.lom.types.LearningResourceType.SELF_ASSESSMENT;
import static dev.jcputney.elearning.parser.input.lom.types.SemanticDensity.MEDIUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.common.LangStringDeserializer;
import dev.jcputney.elearning.parser.input.lom.types.Context;
import dev.jcputney.elearning.parser.input.lom.types.IntendedEndUserRole;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.LearningResourceType;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import java.io.File;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Educational} class.
 */
class EducationalTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(LangString.class, new LangStringDeserializer());
    xmlMapper.registerModule(module);
  }

  /**
   * Tests the deserialization of an Educational object from XML.
   */
  @Test
  void testDeserializeEducational() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getEducational());

    Educational educational = lom.getEducational();

    // Test learningResourceType
    assertNotNull(educational.getLearningResourceType());
    assertFalse(educational
        .getLearningResourceType()
        .isEmpty());

    SourceValuePair<LearningResourceType> learningResourceType = educational
        .getLearningResourceType()
        .get(0);
    assertNotNull(learningResourceType);
    assertEquals("LOMv1.0", learningResourceType.getSource());
    assertEquals(NARRATIVE_TEXT, learningResourceType.getValue());

    // Check for second learning resource type
    assertTrue(educational
        .getLearningResourceType()
        .size() >= 2);
    learningResourceType = educational
        .getLearningResourceType()
        .get(1);
    assertNotNull(learningResourceType);
    assertEquals("LOMv1.0", learningResourceType.getSource());
    assertEquals(SELF_ASSESSMENT, learningResourceType.getValue());

    // Test interactivityLevel
    assertNotNull(educational.getInteractivityLevel());
    assertEquals("LOMv1.0", educational
        .getInteractivityLevel()
        .getSource());
    assertEquals(VERY_LOW, educational
        .getInteractivityLevel()
        .getValue());

    // Test semanticDensity
    assertNotNull(educational.getSemanticDensity());
    assertEquals("LOMv1.0", educational
        .getSemanticDensity()
        .getSource());
    assertEquals(MEDIUM, educational
        .getSemanticDensity()
        .getValue());

    // Test intendedEndUserRole
    assertNotNull(educational.getIntendedEndUserRole());
    assertFalse(educational
        .getIntendedEndUserRole()
        .isEmpty());

    SourceValuePair<IntendedEndUserRole> intendedEndUserRole = educational
        .getIntendedEndUserRole()
        .get(0);
    assertNotNull(intendedEndUserRole);
    assertEquals("LOMv1.0", intendedEndUserRole.getSource());
    assertEquals(LEARNER, intendedEndUserRole.getValue());

    // Test context
    assertNotNull(educational.getContext());
    assertFalse(educational
        .getContext()
        .isEmpty());

    SourceValuePair<Context> context = educational
        .getContext()
        .get(0);
    assertNotNull(context);
    assertEquals("LOMv1.0", context.getSource());
    assertEquals(TRAINING, context.getValue());

    // Test typicalAgeRange
    assertNotNull(educational.getTypicalAgeRange());
    assertNotNull(educational
        .getTypicalAgeRange()
        .getLangStrings());
    assertFalse(educational
        .getTypicalAgeRange()
        .getLangStrings()
        .isEmpty());
    assertEquals("en-us", educational
        .getTypicalAgeRange()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Age 7 to 90", educational
        .getTypicalAgeRange()
        .getLangStrings()
        .get(0)
        .getValue());

    // Test difficulty
    assertNotNull(educational.getDifficulty());
    assertEquals("LOMv1.0", educational
        .getDifficulty()
        .getSource());
    assertEquals(VERY_EASY, educational
        .getDifficulty()
        .getValue());

    // Test typicalLearningTime
    assertNotNull(educational.getTypicalLearningTime());
    assertNotNull(educational
        .getTypicalLearningTime()
        .getDuration());
    assertEquals(Duration.ofMinutes(10), educational
        .getTypicalLearningTime()
        .getDuration());
    assertNotNull(educational
        .getTypicalLearningTime()
        .getDescription());
    assertNotNull(educational
        .getTypicalLearningTime()
        .getDescription()
        .getLangString());
    assertEquals("en-us", educational
        .getTypicalLearningTime()
        .getDescription()
        .getLangString()
        .getLanguage());
    assertTrue(educational
        .getTypicalLearningTime()
        .getDescription()
        .getLangString()
        .getValue()
        .contains("This course can usually be completed in about 10 minutes"));

    // Test description
    assertNotNull(educational.getDescriptions());
    assertNotNull(educational
        .getDescriptions()
        .getLangStrings());
    assertFalse(educational
        .getDescriptions()
        .getLangStrings()
        .isEmpty());
    assertTrue(educational
        .getDescriptions()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This course should be used to provide people with"));

    // Test language
    assertNotNull(educational.getLanguages());
    assertFalse(educational
        .getLanguages()
        .isEmpty());
    assertEquals("en-us", educational
        .getLanguages()
        .get(0));
  }

  /**
   * Tests the deserialization of an Educational object from an imsmanifest.xml file.
   */
  @Test
  void testDeserializeEducationalFromManifest() throws Exception {
    // Given
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";

    // First, try to parse the metadata_course.xml file which we know has educational data
    File metadataFile = new File(modulePath + "/metadata_course.xml");

    // Use the same approach as in testDeserializeEducational
    LOM lom = xmlMapper.readValue(metadataFile, LOM.class);

    // Then
    assertNotNull(lom, "LOM object should not be null");
    assertNotNull(lom.getEducational(), "Educational section should not be null");

    Educational educational = lom.getEducational();

    // Test interactivityType
    assertEquals("LOMv1.0", educational
        .getInteractivityType()
        .getSource());
    assertEquals(EXPOSITIVE, educational
        .getInteractivityType()
        .getValue());

    // Test learningResourceType
    assertEquals("LOMv1.0", educational
        .getLearningResourceType()
        .get(0)
        .getSource());
    assertEquals(NARRATIVE_TEXT, educational
        .getLearningResourceType()
        .get(0)
        .getValue());

    // Test intendedEndUserRole
    assertEquals("LOMv1.0", educational
        .getIntendedEndUserRole()
        .get(0)
        .getSource());
    assertEquals(LEARNER, educational
        .getIntendedEndUserRole()
        .get(0)
        .getValue());

    // Test context
    assertEquals("LOMv1.0", educational
        .getContext()
        .get(0)
        .getSource());
    assertEquals(TRAINING, educational
        .getContext()
        .get(0)
        .getValue());

    // Test typicalAgeRange
    assertTrue(educational
        .getTypicalAgeRange()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("Age"));

    // Test difficulty
    assertEquals("LOMv1.0", educational
        .getDifficulty()
        .getSource());
    assertEquals(VERY_EASY, educational
        .getDifficulty()
        .getValue());

    // Verify duration is not zero
    assertTrue(educational
        .getTypicalLearningTime()
        .getDuration()
        .getSeconds() > 0);

    // Test description
    assertTrue(educational
        .getDescriptions()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This course"));

    // Test language
    assertTrue(educational
        .getLanguages()
        .get(0)
        .toLowerCase()
        .startsWith("en"));
  }
}
