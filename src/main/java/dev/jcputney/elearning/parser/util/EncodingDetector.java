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

/**
 * Utility class for detecting character encoding of XML files. Supports BOM (Byte Order Mark)
 * detection and XML declaration parsing.
 */
public class EncodingDetector {

  private static final Pattern XML_ENCODING_PATTERN =
      Pattern.compile("encoding\\s*=\\s*['\"]([^'\"]+)['\"]", Pattern.CASE_INSENSITIVE);

  private static final int BUFFER_SIZE = 8192;

  /**
   * Detects the encoding of an XML input stream. First checks for BOM, then falls back to XML
   * declaration parsing.
   *
   * @param inputStream The input stream to analyze
   * @return A wrapped input stream with detected encoding
   * @throws IOException if an I/O error occurs
   */
  public static EncodingAwareInputStream detectEncoding(InputStream inputStream)
      throws IOException {
    if (!inputStream.markSupported()) {
      inputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
    }

    // Use PushbackInputStream to handle BOM removal
    PushbackInputStream pushbackStream = new PushbackInputStream(inputStream, 4);

    // First try BOM detection
    BomDetectionResult bomResult = detectBOMWithStream(pushbackStream);
    if (bomResult.charset != null) {
      // Return the stream with BOM already consumed
      return new EncodingAwareInputStream(bomResult.stream, bomResult.charset);
    }

    // No BOM found, try XML declaration
    // Wrap the pushbackStream for mark/reset support
    InputStream markableStream = pushbackStream.markSupported()
        ? pushbackStream
        : new BufferedInputStream(pushbackStream, BUFFER_SIZE);

    markableStream.mark(BUFFER_SIZE);
    Charset xmlDeclCharset = detectFromXmlDeclaration(markableStream);
    if (xmlDeclCharset != null) {
      markableStream.reset();
      return new EncodingAwareInputStream(markableStream, xmlDeclCharset);
    }

    // Default to UTF-8
    markableStream.reset();
    return new EncodingAwareInputStream(markableStream, StandardCharsets.UTF_8);
  }

  /**
   * Detects Byte Order Mark (BOM) in the input stream and returns the stream with BOM consumed.
   *
   * @param pushbackStream The pushback input stream to check
   * @return The detection result with charset and stream
   * @throws IOException if an I/O error occurs
   */
  private static BomDetectionResult detectBOMWithStream(PushbackInputStream pushbackStream)
      throws IOException {
    byte[] bom = new byte[4];
    int bytesRead = pushbackStream.read(bom);

    // UTF-32 BE BOM: 00 00 FE FF (check this first as it requires 4 bytes)
    if (bytesRead >= 4 && bom[0] == 0x00 && bom[1] == 0x00 &&
        bom[2] == (byte) 0xFE && bom[3] == (byte) 0xFF) {
      // All 4 bytes consumed
      return new BomDetectionResult(Charset.forName("UTF-32BE"), pushbackStream);
    }

    // UTF-8 BOM: EF BB BF
    if (bytesRead >= 3 && bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
      // BOM detected and consumed, push back any extra bytes
      if (bytesRead > 3) {
        pushbackStream.unread(bom[3]);
      }
      return new BomDetectionResult(StandardCharsets.UTF_8, pushbackStream);
    }

    // UTF-16 BE BOM: FE FF
    if (bytesRead >= 2 && bom[0] == (byte) 0xFE && bom[1] == (byte) 0xFF) {
      // BOM detected and consumed, push back extra bytes
      if (bytesRead > 2) {
        pushbackStream.unread(bom, 2, bytesRead - 2);
      }
      return new BomDetectionResult(StandardCharsets.UTF_16BE, pushbackStream);
    }

    // UTF-16 LE BOM: FF FE (or UTF-32 LE BOM: FF FE 00 00)
    if (bytesRead >= 2 && bom[0] == (byte) 0xFF && bom[1] == (byte) 0xFE) {
      if (bytesRead >= 4 && bom[2] == 0x00 && bom[3] == 0x00) {
        // UTF-32 LE BOM: FF FE 00 00 - all 4 bytes consumed
        return new BomDetectionResult(Charset.forName("UTF-32LE"), pushbackStream);
      }
      // UTF-16 LE BOM - push back extra bytes
      if (bytesRead > 2) {
        pushbackStream.unread(bom, 2, bytesRead - 2);
      }
      return new BomDetectionResult(StandardCharsets.UTF_16LE, pushbackStream);
    }

    // No BOM found, push back all bytes
    if (bytesRead > 0) {
      pushbackStream.unread(bom, 0, bytesRead);
    }

    return new BomDetectionResult(null, pushbackStream);
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
              String encodingName = matcher
                  .group(1)
                  .trim();
              try {
                return Charset.forName(encodingName);
              } catch (Exception e) {
                // Unknown encoding in XML declaration
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
   * Result of BOM detection containing the charset and the stream with BOM consumed.
   */
  private record BomDetectionResult(Charset charset, InputStream stream) {

  }

  /**
   * Wrapper class that holds an input stream and its detected encoding.
   */
  public record EncodingAwareInputStream(InputStream inputStream, Charset charset) {

  }
}