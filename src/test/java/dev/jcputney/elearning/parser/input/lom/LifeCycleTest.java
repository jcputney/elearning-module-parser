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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.Contribute;
import dev.jcputney.elearning.parser.input.lom.types.Role;
import dev.jcputney.elearning.parser.input.lom.types.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link LifeCycle} class.
 */
class LifeCycleTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
  }

  @Test
  void testDeserializeEmptyLifeCycle() throws Exception {
    // Given
    String xml = "<lifeCycle xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></lifeCycle>";

    // When
    LifeCycle lifeCycle = xmlMapper.readValue(xml, LifeCycle.class);

    // Then
    assertNotNull(lifeCycle);
    assertNull(lifeCycle.getVersion());
    assertNull(lifeCycle.getStatus());
    assertNull(lifeCycle.getContribute());
    assertNull(lifeCycle.getCustomElements());
  }

  @Test
  void testDeserializeLifeCycleWithVersion() throws Exception {
    // Given
    String xml = "<lifeCycle xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <version>"
        + "    <string language=\"en\">1.0.0</string>"
        + "  </version>"
        + "</lifeCycle>";

    // When
    LifeCycle lifeCycle = xmlMapper.readValue(xml, LifeCycle.class);

    // Then
    assertNotNull(lifeCycle);
    assertNotNull(lifeCycle.getVersion());
    assertNotNull(lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0));
    assertEquals("en", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("1.0.0", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getValue());
  }

  @Test
  void testDeserializeLifeCycleWithStatus() throws Exception {
    // Given
    String xml = "<lifeCycle xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <status>"
        + "    <source>LOMv1.0</source>"
        + "    <value>final</value>"
        + "  </status>"
        + "</lifeCycle>";

    // When
    LifeCycle lifeCycle = xmlMapper.readValue(xml, LifeCycle.class);

    // Then
    assertNotNull(lifeCycle);
    assertNotNull(lifeCycle.getStatus());
    assertEquals("LOMv1.0", lifeCycle
        .getStatus()
        .getSource());
    assertEquals(Status.FINAL, lifeCycle
        .getStatus()
        .getValue());
  }

  @Test
  void testDeserializeLifeCycleWithContribute() throws Exception {
    // Given
    String xml = "<lifeCycle xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <contribute>"
        + "    <role>"
        + "      <source>LOMv1.0</source>"
        + "      <value>author</value>"
        + "    </role>"
        + "    <entity>John Doe</entity>"
        + "    <date>"
        + "      <dateTime>2023-01-15</dateTime>"
        + "    </date>"
        + "  </contribute>"
        + "</lifeCycle>";

    // When
    LifeCycle lifeCycle = xmlMapper.readValue(xml, LifeCycle.class);

    // Then
    assertNotNull(lifeCycle);
    assertNotNull(lifeCycle.getContribute());
    assertEquals(1, lifeCycle
        .getContribute()
        .size());

    Contribute contribute = lifeCycle
        .getContribute()
        .get(0);
    assertNotNull(contribute.getRole());
    assertEquals("LOMv1.0", contribute
        .getRole()
        .getSource());
    assertEquals(Role.AUTHOR, contribute
        .getRole()
        .getValue());

    assertNotNull(contribute.getEntities());
    assertEquals(1, contribute
        .getEntities()
        .size());
    assertEquals("John Doe", contribute
        .getEntities()
        .get(0));

    assertNotNull(contribute.getDate());
    assertEquals("2023-01-15", contribute
        .getDate()
        .getDateTime());
  }

  @Test
  void testDeserializeLifeCycleWithMultipleContributions() throws Exception {
    // Given
    String xml = "<lifeCycle xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <contribute>"
        + "    <role>"
        + "      <source>LOMv1.0</source>"
        + "      <value>author</value>"
        + "    </role>"
        + "    <entity>John Doe</entity>"
        + "    <date>"
        + "      <dateTime>2023-01-15</dateTime>"
        + "    </date>"
        + "  </contribute>"
        + "  <contribute>"
        + "    <role>"
        + "      <source>LOMv1.0</source>"
        + "      <value>publisher</value>"
        + "    </role>"
        + "    <entity>ACME Publishing</entity>"
        + "    <date>"
        + "      <dateTime>2023-02-20</dateTime>"
        + "    </date>"
        + "  </contribute>"
        + "</lifeCycle>";

    // When
    LifeCycle lifeCycle = xmlMapper.readValue(xml, LifeCycle.class);

    // Then
    assertNotNull(lifeCycle);
    assertNotNull(lifeCycle.getContribute());
    assertEquals(2, lifeCycle
        .getContribute()
        .size());

    // First contribution
    Contribute contribute1 = lifeCycle
        .getContribute()
        .get(0);
    assertNotNull(contribute1.getRole());
    assertEquals("LOMv1.0", contribute1
        .getRole()
        .getSource());
    assertEquals(Role.AUTHOR, contribute1
        .getRole()
        .getValue());
    assertNotNull(contribute1.getEntities());
    assertEquals(1, contribute1
        .getEntities()
        .size());
    assertEquals("John Doe", contribute1
        .getEntities()
        .get(0));
    assertNotNull(contribute1.getDate());
    assertEquals("2023-01-15", contribute1
        .getDate()
        .getDateTime());

    // Second contribution
    Contribute contribute2 = lifeCycle
        .getContribute()
        .get(1);
    assertNotNull(contribute2.getRole());
    assertEquals("LOMv1.0", contribute2
        .getRole()
        .getSource());
    assertEquals(Role.PUBLISHER, contribute2
        .getRole()
        .getValue());
    assertNotNull(contribute2.getEntities());
    assertEquals(1, contribute2
        .getEntities()
        .size());
    assertEquals("ACME Publishing", contribute2
        .getEntities()
        .get(0));
    assertNotNull(contribute2.getDate());
    assertEquals("2023-02-20", contribute2
        .getDate()
        .getDateTime());
  }

  @Test
  void testDeserializeLifeCycleWithAllFields() throws Exception {
    // Given
    String xml = "<lifeCycle xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <version>"
        + "    <string language=\"en\">1.0.0</string>"
        + "  </version>"
        + "  <status>"
        + "    <source>LOMv1.0</source>"
        + "    <value>final</value>"
        + "  </status>"
        + "  <contribute>"
        + "    <role>"
        + "      <source>LOMv1.0</source>"
        + "      <value>author</value>"
        + "    </role>"
        + "    <entity>John Doe</entity>"
        + "    <date>"
        + "      <dateTime>2023-01-15</dateTime>"
        + "    </date>"
        + "  </contribute>"
        + "</lifeCycle>";

    // When
    LifeCycle lifeCycle = xmlMapper.readValue(xml, LifeCycle.class);

    // Then
    assertNotNull(lifeCycle);

    // Check version
    assertNotNull(lifeCycle.getVersion());
    assertNotNull(lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0));
    assertEquals("en", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("1.0.0", lifeCycle
        .getVersion()
        .getLangStrings()
        .get(0)
        .getValue());

    // Check status
    assertNotNull(lifeCycle.getStatus());
    assertEquals("LOMv1.0", lifeCycle
        .getStatus()
        .getSource());
    assertEquals(Status.FINAL, lifeCycle
        .getStatus()
        .getValue());

    // Check contribute
    assertNotNull(lifeCycle.getContribute());
    assertEquals(1, lifeCycle
        .getContribute()
        .size());
    Contribute contribute = lifeCycle
        .getContribute()
        .get(0);
    assertNotNull(contribute.getRole());
    assertEquals("LOMv1.0", contribute
        .getRole()
        .getSource());
    assertEquals(Role.AUTHOR, contribute
        .getRole()
        .getValue());
    assertNotNull(contribute.getEntities());
    assertEquals(1, contribute
        .getEntities()
        .size());
    assertEquals("John Doe", contribute
        .getEntities()
        .get(0));
    assertNotNull(contribute.getDate());
    assertEquals("2023-01-15", contribute
        .getDate()
        .getDateTime());
  }
}