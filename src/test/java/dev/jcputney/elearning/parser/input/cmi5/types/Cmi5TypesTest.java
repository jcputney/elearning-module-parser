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

package dev.jcputney.elearning.parser.input.cmi5.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CMI5 types classes.
 */
public class Cmi5TypesTest {

  public static final String EN_US = "en-US";
  public static final String IDREF1 = "https://example.com/objective1";
  public static final String IDREF2 = "https://example.com/objective2";

  @Test
  void testLangString() {
    // Create a LangString instance
    LangString langString = LangString.builder()
        .value("Test Value")
        .lang(EN_US)
        .build();

    // Verify the properties
    assertNotNull(langString);
    assertEquals("Test Value", langString.getValue());
    assertEquals(EN_US, langString.getLang());

    // Test default constructor
    LangString defaultLangString = new LangString();
    assertNotNull(defaultLangString);
  }

  @Test
  void testTextType() {
    // Create a list of LangString instances
    List<LangString> strings = new ArrayList<>();
    strings.add(LangString.builder().value("Test Value 1").lang(EN_US).build());
    strings.add(LangString.builder().value("Test Value 2").lang("fr-FR").build());

    // Create a TextType instance
    TextType textType = TextType.builder()
        .strings(strings)
        .build();

    // Verify the properties
    assertNotNull(textType);
    assertNotNull(textType.getStrings());
    assertEquals(2, textType.getStrings().size());
    assertEquals("Test Value 1", textType.getStrings().get(0).getValue());
    assertEquals(EN_US, textType.getStrings().get(0).getLang());
    assertEquals("Test Value 2", textType.getStrings().get(1).getValue());
    assertEquals("fr-FR", textType.getStrings().get(1).getLang());

    // Test default constructor
    TextType defaultTextType = new TextType();
    assertNotNull(defaultTextType);
  }

  @Test
  void testObjectiveReference() {
    // Create an ObjectiveReference instance
    ObjectiveReference objectiveReference = ObjectiveReference.builder()
        .idref(IDREF1)
        .build();

    // Verify the properties
    assertNotNull(objectiveReference);
    assertEquals(IDREF1, objectiveReference.getIdref());

    // Test default constructor
    ObjectiveReference defaultObjectiveReference = new ObjectiveReference();
    assertNotNull(defaultObjectiveReference);
  }

  @Test
  void testReferencesObjectives() {
    // Create a list of ObjectiveReference instances
    List<ObjectiveReference> objectives = new ArrayList<>();
    objectives.add(ObjectiveReference.builder().idref(IDREF1).build());
    objectives.add(ObjectiveReference.builder().idref(IDREF2).build());

    // Create a ReferencesObjectives instance
    ReferencesObjectives referencesObjectives = ReferencesObjectives.builder()
        .objectives(objectives)
        .build();

    // Verify the properties
    assertNotNull(referencesObjectives);
    assertNotNull(referencesObjectives.getObjectives());
    assertEquals(2, referencesObjectives.getObjectives().size());
    assertEquals(IDREF1, referencesObjectives.getObjectives().get(0).getIdref());
    assertEquals(IDREF2, referencesObjectives.getObjectives().get(1).getIdref());

    // Test default constructor
    ReferencesObjectives defaultReferencesObjectives = new ReferencesObjectives();
    assertNotNull(defaultReferencesObjectives);
  }

  @Test
  void testObjective() {
    // Create a TextType for title
    List<LangString> titleStrings = new ArrayList<>();
    titleStrings.add(LangString.builder().value("Objective Title").lang(EN_US).build());
    TextType title = TextType.builder().strings(titleStrings).build();

    // Create a TextType for description
    List<LangString> descriptionStrings = new ArrayList<>();
    descriptionStrings.add(LangString.builder().value("Objective Description").lang(EN_US).build());
    TextType description = TextType.builder().strings(descriptionStrings).build();

    // Create an Objective instance
    Objective objective = Objective.builder()
        .id("https://example.com/objective")
        .title(title)
        .description(description)
        .build();

    // Verify the properties
    assertNotNull(objective);
    assertEquals("https://example.com/objective", objective.getId());
    assertNotNull(objective.getTitle());
    assertEquals(1, objective.getTitle().getStrings().size());
    assertEquals("Objective Title", objective.getTitle().getStrings().get(0).getValue());
    assertNotNull(objective.getDescription());
    assertEquals(1, objective.getDescription().getStrings().size());
    assertEquals("Objective Description",
        objective.getDescription().getStrings().get(0).getValue());

    // Test default constructor
    Objective defaultObjective = new Objective();
    assertNotNull(defaultObjective);
  }
}