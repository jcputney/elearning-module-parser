/*
 * Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for the EncodingDetector utility class.
 */
class EncodingDetectorTest {

  @Test
  void testUTF8BOMDetection() throws IOException {
    // UTF-8 BOM followed by XML content
    byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    String xmlContent = "<?xml version=\"1.0\"?><root>Test</root>";
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bom);
    baos.write(xmlContent.getBytes(StandardCharsets.UTF_8));
    
    InputStream input = new ByteArrayInputStream(baos.toByteArray());
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(StandardCharsets.UTF_8, result.getCharset());
  }

  @Test
  void testUTF16BEBOMDetection() throws IOException {
    // UTF-16 BE BOM
    byte[] bom = new byte[]{(byte) 0xFE, (byte) 0xFF};
    String xmlContent = "<?xml version=\"1.0\"?><root>Test</root>";
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bom);
    baos.write(xmlContent.getBytes(StandardCharsets.UTF_16BE));
    
    InputStream input = new ByteArrayInputStream(baos.toByteArray());
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(StandardCharsets.UTF_16BE, result.getCharset());
  }

  @Test
  void testUTF16LEBOMDetection() throws IOException {
    // UTF-16 LE BOM
    byte[] bom = new byte[]{(byte) 0xFF, (byte) 0xFE};
    String xmlContent = "<?xml version=\"1.0\"?><root>Test</root>";
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bom);
    baos.write(xmlContent.getBytes(StandardCharsets.UTF_16LE));
    
    InputStream input = new ByteArrayInputStream(baos.toByteArray());
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(StandardCharsets.UTF_16LE, result.getCharset());
  }

  @Test
  void testXMLDeclarationEncodingDetection() throws IOException {
    // XML with encoding declaration
    String xmlContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><root>Test</root>";
    
    InputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.ISO_8859_1));
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(StandardCharsets.ISO_8859_1, result.getCharset());
  }

  @Test
  void testXMLDeclarationUTF16Encoding() throws IOException {
    // XML with UTF-16 encoding declaration
    // Note: When encoding to UTF-16 without BOM, Java uses UTF-16BE by default
    String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-16\"?><root>Test</root>";
    
    InputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_16));
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    // The actual result will be UTF-16BE because that's what Java encoded it as
    assertEquals(StandardCharsets.UTF_16BE, result.getCharset());
  }

  @Test
  void testDefaultToUTF8WhenNoEncodingFound() throws IOException {
    // XML without encoding declaration or BOM
    String xmlContent = "<?xml version=\"1.0\"?><root>Test</root>";
    
    InputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(StandardCharsets.UTF_8, result.getCharset());
  }

  @Test
  void testUnicodeContent() throws IOException {
    // XML with Unicode characters
    String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>测试 テスト тест</root>";
    
    InputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(StandardCharsets.UTF_8, result.getCharset());
    
    // Verify content can be read correctly
    byte[] buffer = new byte[1024];
    int bytesRead = result.getInputStream().read(buffer);
    String readContent = new String(buffer, 0, bytesRead, result.getCharset());
    assertEquals(xmlContent, readContent);
  }

  @Test
  void testEmptyInputStream() throws IOException {
    InputStream input = new ByteArrayInputStream(new byte[0]);
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    // Should default to UTF-8 for empty stream
    assertEquals(StandardCharsets.UTF_8, result.getCharset());
  }

  @Test
  void testUTF32BEBOMDetection() throws IOException {
    // UTF-32 BE BOM
    byte[] bom = new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
    String xmlContent = "<?xml version=\"1.0\"?><root>Test</root>";
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bom);
    baos.write(xmlContent.getBytes(Charset.forName("UTF-32BE")));
    
    InputStream input = new ByteArrayInputStream(baos.toByteArray());
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(Charset.forName("UTF-32BE"), result.getCharset());
  }

  @Test
  void testUTF32LEBOMDetection() throws IOException {
    // UTF-32 LE BOM
    byte[] bom = new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
    String xmlContent = "<?xml version=\"1.0\"?><root>Test</root>";
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(bom);
    baos.write(xmlContent.getBytes(Charset.forName("UTF-32LE")));
    
    InputStream input = new ByteArrayInputStream(baos.toByteArray());
    EncodingDetector.EncodingAwareInputStream result = EncodingDetector.detectEncoding(input);
    
    assertNotNull(result);
    assertEquals(Charset.forName("UTF-32LE"), result.getCharset());
  }
}