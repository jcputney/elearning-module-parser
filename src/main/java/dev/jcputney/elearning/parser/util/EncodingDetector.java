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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
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

  private static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

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

    // First honour the declaration if present and valid for the underlying bytes
    markableStream.mark(BUFFER_SIZE);
    Charset xmlDeclCharset = detectFromXmlDeclaration(markableStream);
    markableStream.reset();
    if (xmlDeclCharset != null) {
      markableStream.mark(BUFFER_SIZE);
      boolean declaredEncodingMatchesContent = isContentValidForCharset(markableStream,
          xmlDeclCharset);
      markableStream.reset();
      if (declaredEncodingMatchesContent) {
        return new EncodingAwareInputStream(markableStream, xmlDeclCharset);
      }
    }

    // Fall back to heuristics (UTF-8 vs Windows-1252)
    markableStream.mark(BUFFER_SIZE);
    Charset heuristic = detectLikelyEncoding(markableStream);
    markableStream.reset();
    return new EncodingAwareInputStream(markableStream, heuristic);
  }

  private static Charset detectLikelyEncoding(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = inputStream.read(buffer);

    if (bytesRead <= 0) {
      return StandardCharsets.UTF_8;
    }

    boolean hasHighBit = false;
    for (int i = 0; i < bytesRead; i++) {
      if ((buffer[i] & 0x80) != 0) {
        hasHighBit = true;
        break;
      }
    }

    if (!hasHighBit || looksLikeUtf8(buffer, bytesRead)) {
      return StandardCharsets.UTF_8;
    }

    return WINDOWS_1252;
  }

  private static boolean looksLikeUtf8(byte[] buffer, int length) {
    int i = 0;
    while (i < length) {
      int b = buffer[i] & 0xFF;
      if (b < 0x80) {
        i++;
        continue;
      }

      if (b < 0xC2) {
        return false; // Continuation byte or overlong sequence indicator
      }

      int expectedContinuationBytes;
      if (b < 0xE0) {
        expectedContinuationBytes = 1;
      } else if (b < 0xF0) {
        expectedContinuationBytes = 2;
      } else if (b <= 0xF4) {
        expectedContinuationBytes = 3;
      } else {
        return false;
      }

      if (i + expectedContinuationBytes >= length) {
        // Not enough bytes in the sample to validate fully; assume UTF-8 to avoid false negatives
        return true;
      }

      for (int j = 1; j <= expectedContinuationBytes; j++) {
        int continuation = buffer[i + j] & 0xFF;
        if ((continuation & 0xC0) != 0x80) {
          return false;
        }
      }

      i += expectedContinuationBytes + 1;
    }

    return true;
  }

  private static boolean isContentValidForCharset(InputStream inputStream, Charset charset)
      throws IOException {
    if (charset == null) {
      return false;
    }

    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = inputStream.read(buffer);

    if (bytesRead <= 0) {
      return true;
    }

    CharsetDecoder decoder = charset
        .newDecoder()
        .onMalformedInput(CodingErrorAction.REPORT)
        .onUnmappableCharacter(CodingErrorAction.REPORT);

    try {
      decoder.decode(ByteBuffer.wrap(buffer, 0, bytesRead));
      return true;
    } catch (CharacterCodingException e) {
      return false;
    }
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
