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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for detecting character encoding of XML files.
 * Supports BOM (Byte Order Mark) detection and XML declaration parsing.
 */
@Slf4j
public class EncodingDetector {
  
  private static final Pattern XML_ENCODING_PATTERN = 
      Pattern.compile("encoding\\s*=\\s*['\"]([^'\"]+)['\"]", Pattern.CASE_INSENSITIVE);
  
  private static final int BUFFER_SIZE = 8192;
  
  /**
   * Detects the encoding of an XML input stream.
   * First checks for BOM, then falls back to XML declaration parsing.
   * 
   * @param inputStream The input stream to analyze
   * @return A wrapped input stream with detected encoding
   * @throws IOException if an I/O error occurs
   */
  public static EncodingAwareInputStream detectEncoding(InputStream inputStream) throws IOException {
    if (!inputStream.markSupported()) {
      inputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
    }
    
    inputStream.mark(BUFFER_SIZE);
    
    // First try BOM detection
    Charset bomCharset = detectBOM(inputStream);
    if (bomCharset != null) {
      log.debug(LogMarkers.XML_VERBOSE, "Detected BOM encoding: {}", bomCharset);
      return new EncodingAwareInputStream(inputStream, bomCharset);
    }
    
    // Reset and try XML declaration
    inputStream.reset();
    inputStream.mark(BUFFER_SIZE);
    
    Charset xmlDeclCharset = detectFromXmlDeclaration(inputStream);
    if (xmlDeclCharset != null) {
      log.debug(LogMarkers.XML_VERBOSE, "Detected XML declaration encoding: {}", xmlDeclCharset);
      inputStream.reset();
      return new EncodingAwareInputStream(inputStream, xmlDeclCharset);
    }
    
    // Default to UTF-8
    log.debug(LogMarkers.XML_VERBOSE, "No encoding detected, defaulting to UTF-8");
    inputStream.reset();
    return new EncodingAwareInputStream(inputStream, StandardCharsets.UTF_8);
  }
  
  /**
   * Detects Byte Order Mark (BOM) in the input stream.
   * 
   * @param inputStream The input stream to check
   * @return The detected charset or null if no BOM found
   * @throws IOException if an I/O error occurs
   */
  private static Charset detectBOM(InputStream inputStream) throws IOException {
    PushbackInputStream pushbackStream = new PushbackInputStream(inputStream, 4);
    byte[] bom = new byte[4];
    int bytesRead = pushbackStream.read(bom);
    
    if (bytesRead >= 3) {
      // UTF-8 BOM: EF BB BF
      if (bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
        // Don't push back the BOM - skip it
        if (bytesRead > 3) {
          pushbackStream.unread(bom[3]);
        }
        return StandardCharsets.UTF_8;
      }
      
      // UTF-16 BE BOM: FE FF
      if (bytesRead >= 2 && bom[0] == (byte) 0xFE && bom[1] == (byte) 0xFF) {
        // Push back extra bytes
        if (bytesRead > 2) {
          pushbackStream.unread(bom, 2, bytesRead - 2);
        }
        return StandardCharsets.UTF_16BE;
      }
      
      // UTF-16 LE BOM: FF FE
      if (bytesRead >= 2 && bom[0] == (byte) 0xFF && bom[1] == (byte) 0xFE) {
        if (bytesRead >= 4 && bom[2] == 0x00 && bom[3] == 0x00) {
          // UTF-32 LE BOM: FF FE 00 00
          return Charset.forName("UTF-32LE");
        }
        // Push back extra bytes for UTF-16 LE
        if (bytesRead > 2) {
          pushbackStream.unread(bom, 2, bytesRead - 2);
        }
        return StandardCharsets.UTF_16LE;
      }
      
      // UTF-32 BE BOM: 00 00 FE FF
      if (bytesRead >= 4 && bom[0] == 0x00 && bom[1] == 0x00 && 
          bom[2] == (byte) 0xFE && bom[3] == (byte) 0xFF) {
        return Charset.forName("UTF-32BE");
      }
    }
    
    // No BOM found, push back all bytes
    if (bytesRead > 0) {
      pushbackStream.unread(bom, 0, bytesRead);
    }
    
    return null;
  }
  
  /**
   * Detects encoding from XML declaration.
   * 
   * @param inputStream The input stream to analyze
   * @return The detected charset or null if not found
   * @throws IOException if an I/O error occurs
   */
  private static Charset detectFromXmlDeclaration(InputStream inputStream) throws IOException {
    // Read enough bytes to find the XML declaration
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = inputStream.read(buffer);
    
    if (bytesRead <= 0) {
      return null;
    }
    
    // Try common encodings to read the XML declaration
    String[] testEncodings = {"UTF-8", "ISO-8859-1", "UTF-16", "UTF-16BE", "UTF-16LE"};
    
    for (String encoding : testEncodings) {
      try {
        String xmlStart = new String(buffer, 0, Math.min(bytesRead, 200), encoding);
        
        // Look for XML declaration
        if (xmlStart.contains("<?xml")) {
          int declEnd = xmlStart.indexOf("?>");
          if (declEnd > 0) {
            String declaration = xmlStart.substring(0, declEnd + 2);
            Matcher matcher = XML_ENCODING_PATTERN.matcher(declaration);
            
            if (matcher.find()) {
              String encodingName = matcher.group(1).trim();
              try {
                return Charset.forName(encodingName);
              } catch (Exception e) {
                log.warn("Unknown encoding in XML declaration: {}", encodingName);
              }
            }
            
            // XML declaration found but no encoding specified
            return StandardCharsets.UTF_8;
          }
        }
      } catch (Exception e) {
        // Try next encoding
      }
    }
    
    return null;
  }
  
  /**
   * Wrapper class that holds an input stream and its detected encoding.
   */
  public static class EncodingAwareInputStream {
    private final InputStream inputStream;
    private final Charset charset;
    
    public EncodingAwareInputStream(InputStream inputStream, Charset charset) {
      this.inputStream = inputStream;
      this.charset = charset;
    }
    
    public InputStream getInputStream() {
      return inputStream;
    }
    
    public Charset getCharset() {
      return charset;
    }
  }
}