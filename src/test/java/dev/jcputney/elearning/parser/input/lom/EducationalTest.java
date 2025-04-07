/*
 * Copyright (c) 2025. Jonathan Putney
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.Context;
import dev.jcputney.elearning.parser.input.lom.types.Difficulty;
import dev.jcputney.elearning.parser.input.lom.types.IntendedEndUserRole;
import dev.jcputney.elearning.parser.input.lom.types.InteractivityLevel;
import dev.jcputney.elearning.parser.input.lom.types.InteractivityType;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.LearningResourceType;
import dev.jcputney.elearning.parser.input.lom.types.LomDuration;
import dev.jcputney.elearning.parser.input.lom.types.SemanticDensity;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EducationalTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
  }

  @Test
  void testDeserializeEmptyEducational() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNull(educational.getInteractivityType());
    assertNull(educational.getLearningResourceType());
    assertNull(educational.getInteractivityLevel());
    assertNull(educational.getSemanticDensity());
    assertNull(educational.getIntendedEndUserRole());
    assertNull(educational.getContext());
    assertNull(educational.getTypicalAgeRange());
    assertNull(educational.getDifficulty());
    assertNull(educational.getTypicalLearningTime());
    assertNull(educational.getDescriptions());
    assertNull(educational.getLanguages());
  }

  @Test
  void testDeserializeEducationalWithInteractivityType() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <interactivityType>"
        + "    <source>LOMv1.0</source>"
        + "    <value>active</value>"
        + "  </interactivityType>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getInteractivityType());
    assertEquals("LOMv1.0", educational.getInteractivityType().getSource());
    assertEquals(InteractivityType.ACTIVE, educational.getInteractivityType().getValue());
  }

  @Test
  void testDeserializeEducationalWithLearningResourceType() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <learningResourceType>"
        + "    <source>LOMv1.0</source>"
        + "    <value>exercise</value>"
        + "  </learningResourceType>"
        + "  <learningResourceType>"
        + "    <source>LOMv1.0</source>"
        + "    <value>simulation</value>"
        + "  </learningResourceType>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getLearningResourceType());
    assertEquals(2, educational.getLearningResourceType().size());

    SourceValuePair<LearningResourceType> type1 = educational.getLearningResourceType().get(0);
    assertEquals("LOMv1.0", type1.getSource());
    assertEquals(LearningResourceType.EXERCISE, type1.getValue());

    SourceValuePair<LearningResourceType> type2 = educational.getLearningResourceType().get(1);
    assertEquals("LOMv1.0", type2.getSource());
    assertEquals(LearningResourceType.SIMULATION, type2.getValue());
  }

  @Test
  void testDeserializeEducationalWithInteractivityLevel() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <interactivityLevel>"
        + "    <source>LOMv1.0</source>"
        + "    <value>medium</value>"
        + "  </interactivityLevel>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getInteractivityLevel());
    assertEquals("LOMv1.0", educational.getInteractivityLevel().getSource());
    assertEquals(InteractivityLevel.MEDIUM, educational.getInteractivityLevel().getValue());
  }

  @Test
  void testDeserializeEducationalWithSemanticDensity() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <semanticDensity>"
        + "    <source>LOMv1.0</source>"
        + "    <value>high</value>"
        + "  </semanticDensity>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getSemanticDensity());
    assertEquals("LOMv1.0", educational.getSemanticDensity().getSource());
    assertEquals(SemanticDensity.HIGH, educational.getSemanticDensity().getValue());
  }

  @Test
  void testDeserializeEducationalWithIntendedEndUserRole() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <intendedEndUserRole>"
        + "    <source>LOMv1.0</source>"
        + "    <value>learner</value>"
        + "  </intendedEndUserRole>"
        + "  <intendedEndUserRole>"
        + "    <source>LOMv1.0</source>"
        + "    <value>teacher</value>"
        + "  </intendedEndUserRole>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getIntendedEndUserRole());
    assertEquals(2, educational.getIntendedEndUserRole().size());

    SourceValuePair<IntendedEndUserRole> role1 = educational.getIntendedEndUserRole().get(0);
    assertEquals("LOMv1.0", role1.getSource());
    assertEquals(IntendedEndUserRole.LEARNER, role1.getValue());

    SourceValuePair<IntendedEndUserRole> role2 = educational.getIntendedEndUserRole().get(1);
    assertEquals("LOMv1.0", role2.getSource());
    assertEquals(IntendedEndUserRole.TEACHER, role2.getValue());
  }

  @Test
  void testDeserializeEducationalWithContext() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <context>"
        + "    <source>LOMv1.0</source>"
        + "    <value>school</value>"
        + "  </context>"
        + "  <context>"
        + "    <source>LOMv1.0</source>"
        + "    <value>higherEducation</value>"
        + "  </context>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getContext());
    assertEquals(2, educational.getContext().size());

    SourceValuePair<Context> context1 = educational.getContext().get(0);
    assertEquals("LOMv1.0", context1.getSource());
    assertEquals(Context.SCHOOL, context1.getValue());

    SourceValuePair<Context> context2 = educational.getContext().get(1);
    assertEquals("LOMv1.0", context2.getSource());
    assertEquals(Context.HIGHER_EDUCATION, context2.getValue());
  }

  @Test
  void testDeserializeEducationalWithTypicalAgeRange() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <typicalAgeRange>"
        + "    <string language=\"en\">18-22</string>"
        + "    <string language=\"fr\">18-22</string>"
        + "  </typicalAgeRange>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getTypicalAgeRange());
    assertNotNull(educational.getTypicalAgeRange().getLangStrings());
    assertEquals(2, educational.getTypicalAgeRange().getLangStrings().size());

    List<LangString> langStrings = educational.getTypicalAgeRange().getLangStrings();
    assertEquals("en", langStrings.get(0).getLanguage());
    assertEquals("18-22", langStrings.get(0).getValue());
    assertEquals("fr", langStrings.get(1).getLanguage());
    assertEquals("18-22", langStrings.get(1).getValue());
  }

  @Test
  void testDeserializeEducationalWithDifficulty() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <difficulty>"
        + "    <source>LOMv1.0</source>"
        + "    <value>medium</value>"
        + "  </difficulty>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getDifficulty());
    assertEquals("LOMv1.0", educational.getDifficulty().getSource());
    assertEquals(Difficulty.MEDIUM, educational.getDifficulty().getValue());
  }

  @Test
  void testDeserializeEducationalWithTypicalLearningTime() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <typicalLearningTime>"
        + "    <duration>PT1H30M</duration>"
        + "    <description>"
        + "      <string language=\"en\">One and a half hours</string>"
        + "    </description>"
        + "  </typicalLearningTime>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getTypicalLearningTime());
    assertEquals(java.time.Duration.parse("PT1H30M"), educational.getTypicalLearningTime().getDuration());
    assertNotNull(educational.getTypicalLearningTime().getDescription());
    assertNotNull(educational.getTypicalLearningTime().getDescription().getLangString());
    assertEquals("en", educational.getTypicalLearningTime().getDescription().getLangString().getLanguage());
    assertEquals("One and a half hours", educational.getTypicalLearningTime().getDescription().getLangString().getValue());
  }

  @Test
  void testDeserializeEducationalWithDescriptions() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <description>"
        + "    <string language=\"en\">This is an educational description</string>"
        + "    <string language=\"fr\">C'est une description éducative</string>"
        + "  </description>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getDescriptions());
    assertNotNull(educational.getDescriptions().getLangStrings());
    assertEquals(2, educational.getDescriptions().getLangStrings().size());

    List<LangString> langStrings = educational.getDescriptions().getLangStrings();
    assertEquals("en", langStrings.get(0).getLanguage());
    assertEquals("This is an educational description", langStrings.get(0).getValue());
    assertEquals("fr", langStrings.get(1).getLanguage());
    assertEquals("C'est une description éducative", langStrings.get(1).getValue());
  }

  @Test
  void testDeserializeEducationalWithLanguages() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <language>en</language>"
        + "  <language>fr</language>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);
    assertNotNull(educational.getLanguages());
    assertEquals(2, educational.getLanguages().size());
    assertEquals("en", educational.getLanguages().get(0));
    assertEquals("fr", educational.getLanguages().get(1));
  }

  @Test
  void testDeserializeCompleteEducational() throws Exception {
    // Given
    String xml = "<educational xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <interactivityType>"
        + "    <source>LOMv1.0</source>"
        + "    <value>active</value>"
        + "  </interactivityType>"
        + "  <learningResourceType>"
        + "    <source>LOMv1.0</source>"
        + "    <value>exercise</value>"
        + "  </learningResourceType>"
        + "  <interactivityLevel>"
        + "    <source>LOMv1.0</source>"
        + "    <value>medium</value>"
        + "  </interactivityLevel>"
        + "  <semanticDensity>"
        + "    <source>LOMv1.0</source>"
        + "    <value>medium</value>"
        + "  </semanticDensity>"
        + "  <intendedEndUserRole>"
        + "    <source>LOMv1.0</source>"
        + "    <value>learner</value>"
        + "  </intendedEndUserRole>"
        + "  <context>"
        + "    <source>LOMv1.0</source>"
        + "    <value>higherEducation</value>"
        + "  </context>"
        + "  <typicalAgeRange>"
        + "    <string language=\"en\">18-22</string>"
        + "  </typicalAgeRange>"
        + "  <difficulty>"
        + "    <source>LOMv1.0</source>"
        + "    <value>medium</value>"
        + "  </difficulty>"
        + "  <typicalLearningTime>"
        + "    <duration>PT1H30M</duration>"
        + "    <description>"
        + "      <string language=\"en\">One and a half hours</string>"
        + "    </description>"
        + "  </typicalLearningTime>"
        + "  <description>"
        + "    <string language=\"en\">This is an educational description</string>"
        + "  </description>"
        + "  <language>en</language>"
        + "</educational>";

    // When
    Educational educational = xmlMapper.readValue(xml, Educational.class);

    // Then
    assertNotNull(educational);

    // Check interactivityType
    assertNotNull(educational.getInteractivityType());
    assertEquals("LOMv1.0", educational.getInteractivityType().getSource());
    assertEquals(InteractivityType.ACTIVE, educational.getInteractivityType().getValue());

    // Check learningResourceType
    assertNotNull(educational.getLearningResourceType());
    assertEquals(1, educational.getLearningResourceType().size());
    assertEquals("LOMv1.0", educational.getLearningResourceType().get(0).getSource());
    assertEquals(LearningResourceType.EXERCISE, educational.getLearningResourceType().get(0).getValue());

    // Check interactivityLevel
    assertNotNull(educational.getInteractivityLevel());
    assertEquals("LOMv1.0", educational.getInteractivityLevel().getSource());
    assertEquals(InteractivityLevel.MEDIUM, educational.getInteractivityLevel().getValue());

    // Check semanticDensity
    assertNotNull(educational.getSemanticDensity());
    assertEquals("LOMv1.0", educational.getSemanticDensity().getSource());
    assertEquals(SemanticDensity.MEDIUM, educational.getSemanticDensity().getValue());

    // Check intendedEndUserRole
    assertNotNull(educational.getIntendedEndUserRole());
    assertEquals(1, educational.getIntendedEndUserRole().size());
    assertEquals("LOMv1.0", educational.getIntendedEndUserRole().get(0).getSource());
    assertEquals(IntendedEndUserRole.LEARNER, educational.getIntendedEndUserRole().get(0).getValue());

    // Check context
    assertNotNull(educational.getContext());
    assertEquals(1, educational.getContext().size());
    assertEquals("LOMv1.0", educational.getContext().get(0).getSource());
    assertEquals(Context.HIGHER_EDUCATION, educational.getContext().get(0).getValue());

    // Check typicalAgeRange
    assertNotNull(educational.getTypicalAgeRange());
    assertEquals(1, educational.getTypicalAgeRange().getLangStrings().size());
    assertEquals("en", educational.getTypicalAgeRange().getLangStrings().get(0).getLanguage());
    assertEquals("18-22", educational.getTypicalAgeRange().getLangStrings().get(0).getValue());

    // Check difficulty
    assertNotNull(educational.getDifficulty());
    assertEquals("LOMv1.0", educational.getDifficulty().getSource());
    assertEquals(Difficulty.MEDIUM, educational.getDifficulty().getValue());

    // Check typicalLearningTime
    assertNotNull(educational.getTypicalLearningTime());
    assertEquals(java.time.Duration.parse("PT1H30M"), educational.getTypicalLearningTime().getDuration());
    assertNotNull(educational.getTypicalLearningTime().getDescription());
    assertEquals("One and a half hours", educational.getTypicalLearningTime().getDescription().getLangString().getValue());

    // Check descriptions
    assertNotNull(educational.getDescriptions());
    assertEquals(1, educational.getDescriptions().getLangStrings().size());
    assertEquals("en", educational.getDescriptions().getLangStrings().get(0).getLanguage());
    assertEquals("This is an educational description", educational.getDescriptions().getLangStrings().get(0).getValue());

    // Check languages
    assertNotNull(educational.getLanguages());
    assertEquals(1, educational.getLanguages().size());
    assertEquals("en", educational.getLanguages().get(0));
  }
}
