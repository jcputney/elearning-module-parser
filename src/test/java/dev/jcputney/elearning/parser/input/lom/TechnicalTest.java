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
import dev.jcputney.elearning.parser.input.lom.properties.PackageProperties;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.LomDuration;
import dev.jcputney.elearning.parser.input.lom.types.OrComposite;
import dev.jcputney.elearning.parser.input.lom.types.Requirement;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Technical} class.
 */
class TechnicalTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
  }

  @Test
  void testDeserializeEmptyTechnical() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNull(technical.getFormat());
    assertNull(technical.getSize());
    assertNull(technical.getLocation());
    assertNull(technical.getRequirements());
    assertNull(technical.getInstallationRemarks());
    assertNull(technical.getOtherPlatformRequirements());
    assertNull(technical.getDuration());
    assertNull(technical.getPackageProperties());
    assertNull(technical.getCustomElements());
  }

  @Test
  void testDeserializeTechnicalWithFormat() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <format>text/html</format>"
        + "  <format>application/pdf</format>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNotNull(technical.getFormat());
    assertEquals(2, technical.getFormat().size());
    assertEquals("text/html", technical.getFormat().get(0));
    assertEquals("application/pdf", technical.getFormat().get(1));
  }

  @Test
  void testDeserializeTechnicalWithSize() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <size>1024</size>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNotNull(technical.getSize());
    assertEquals(1024, technical.getSize());
  }

  @Test
  void testDeserializeTechnicalWithLocation() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <location>http://example.com/resource1</location>"
        + "  <location>http://example.com/resource2</location>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNotNull(technical.getLocation());
    assertEquals(2, technical.getLocation().size());
    assertEquals("http://example.com/resource1", technical.getLocation().get(0));
    assertEquals("http://example.com/resource2", technical.getLocation().get(1));
  }

  @Test
  void testDeserializeTechnicalWithInstallationRemarks() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <installationRemarks>"
        + "    <string language=\"en\">Extract the zip file and run setup.exe</string>"
        + "  </installationRemarks>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNotNull(technical.getInstallationRemarks());
    assertNotNull(technical.getInstallationRemarks().getLangString());
    assertEquals("en", technical.getInstallationRemarks().getLangString().getLanguage());
    assertEquals("Extract the zip file and run setup.exe",
        technical.getInstallationRemarks().getLangString().getValue());
  }

  @Test
  void testDeserializeTechnicalWithOtherPlatformRequirements() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <otherPlatformRequirements>"
        + "    <string language=\"en\">Requires Java Runtime Environment 8 or higher</string>"
        + "  </otherPlatformRequirements>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNotNull(technical.getOtherPlatformRequirements());
    assertNotNull(technical.getOtherPlatformRequirements().getLangString());
    assertEquals("en", technical.getOtherPlatformRequirements().getLangString().getLanguage());
    assertEquals("Requires Java Runtime Environment 8 or higher",
        technical.getOtherPlatformRequirements().getLangString().getValue());
  }

  @Test
  void testDeserializeTechnicalWithDuration() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <duration>"
        + "    <duration>PT2H30M</duration>"
        + "  </duration>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);
    assertNotNull(technical.getDuration());
    assertEquals(java.time.Duration.parse("PT2H30M"), technical.getDuration().getDuration());
  }

  @Test
  void testDeserializeTechnicalWithMultipleFields() throws Exception {
    // Given
    String xml = "<technical xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <format>text/html</format>"
        + "  <size>1024</size>"
        + "  <location>http://example.com/resource</location>"
        + "  <installationRemarks>"
        + "    <string language=\"en\">Extract the zip file and run setup.exe</string>"
        + "  </installationRemarks>"
        + "  <duration>"
        + "    <duration>PT2H30M</duration>"
        + "  </duration>"
        + "</technical>";

    // When
    Technical technical = xmlMapper.readValue(xml, Technical.class);

    // Then
    assertNotNull(technical);

    // Check format
    assertNotNull(technical.getFormat());
    assertEquals(1, technical.getFormat().size());
    assertEquals("text/html", technical.getFormat().get(0));

    // Check size
    assertNotNull(technical.getSize());
    assertEquals(1024, technical.getSize());

    // Check location
    assertNotNull(technical.getLocation());
    assertEquals(1, technical.getLocation().size());
    assertEquals("http://example.com/resource", technical.getLocation().get(0));

    // Check installation remarks
    assertNotNull(technical.getInstallationRemarks());
    assertNotNull(technical.getInstallationRemarks().getLangString());
    assertEquals("en", technical.getInstallationRemarks().getLangString().getLanguage());
    assertEquals("Extract the zip file and run setup.exe",
        technical.getInstallationRemarks().getLangString().getValue());

    // Check duration
    assertNotNull(technical.getDuration());
    assertEquals(java.time.Duration.parse("PT2H30M"), technical.getDuration().getDuration());
  }
}