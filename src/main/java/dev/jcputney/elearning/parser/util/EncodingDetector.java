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
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for detecting character encoding of XML files. Supports BOM (Byte Order Mark)
 * detection and XML declaration parsing.
 */
public final class EncodingDetector {

  /**
   * A regular expression pattern to match the encoding declaration in an XML header. This pattern
   * is case-insensitive and searches for the `encoding` attribute within an XML declaration,
   * capturing the value enclosed in single or double quotes.
   * <p>
   * Example matches: - encoding='UTF-8' - encoding="ISO-8859-1"
   * <p>
   * Used primarily to detect the character encoding specified in an XML document.
   */
  private static final Pattern XML_ENCODING_PATTERN =
      Pattern.compile("encoding\\s*=\\s*['\"]([^'\"]+)['\"]", Pattern.CASE_INSENSITIVE);

  /**
   * Defines the size of the buffer used for reading input streams in the encoding detection
   * process. This constant helps optimize performance by specifying an appropriate buffer size for
   * data handling.
   */
  private static final int BUFFER_SIZE = 8192;

  /**
   * Represents the Windows-1252 character set (also known as "Western European" or "Windows Latin
   * 1") used for encoding text. This charset is commonly used in legacy applications and file
   * formats where extended Latin character support is required.
   * <p>
   * It is a static and final Charset instance initialized with the standard name "windows-1252".
   * This field provides a centralized and reusable reference to the Windows-1252 charset within the
   * EncodingDetector class.
   * <p>
   * In the context of encoding detection, it may be utilized to verify or handle text content
   * encoded using this specific character set.
   */
  private static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

  /**
   * Represents the UTF-32 Big Endian (UTF-32 BE) encoding Byte Order Mark (BOM). This byte array
   * serves as a signature to indicate that the text following it is encoded in UTF-32 with a
   * big-endian byte order.
   */
  private static final byte[] UTF32_BE = new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF};

  /**
   * Represents the UTF-8 Byte Order Mark (BOM) as a byte array. The BOM is comprised of the three
   * bytes: 0xEF, 0xBB, and 0xBF, which indicate that the text stream is encoded in UTF-8.
   * <p>
   * This constant is used to detect the presence of a UTF-8 BOM in data streams and to
   * differentiate between text encoded with and without the BOM marker.
   */
  private static final byte[] UTF8 = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

  /**
   * Represents the Byte Order Mark (BOM) for UTF-16 Big Endian encoding. The BOM is used to signal
   * the endianness of the encoding when reading text data. For UTF-16 Big Endian, the BOM is
   * represented by the byte sequence 0xFE, 0xFF.
   */
  private static final byte[] UTF16_BE = new byte[]{(byte) 0xFE, (byte) 0xFF};

  /**
   * Represents the UTF-16 Little Endian (UTF-16 LE) Byte Order Mark (BOM) as a byte array. The BOM
   * is used to indicate that the text stream is encoded in UTF-16 LE format. This constant is
   * primarily used for encoding detection purposes.
   */
  private static final byte[] UTF16_LE = new byte[]{(byte) 0xFF, (byte) 0xFE};

  /**
   * A byte array representing the suffix used to identify UTF-32 Little Endian (UTF-32 LE)
   * encoding. This suffix consists of two null bytes (0x00, 0x00) and is typically used in encoding
   * detection to confirm the byte order.
   */
  private static final byte[] UTF32_LE_SUFFIX = new byte[]{0x00, 0x00};

  /**
   * Represents the maximum value of a 7-bit ASCII character as defined by the standard ASCII table.
   * The value is 0x80 (128 in decimal), corresponding to the upper bound for valid ASCII characters
   * (0-127). This constant is used to identify and distinguish ASCII characters from extended
   * character sets.
   */
  private static final int ASCII_MAX = 0x80;        // 0xxxxxxx

  /**
   * Represents the minimum valid value for a 2-byte lead in UTF-8 encoding.
   * <p>
   * The value ensures that overlong encodings, such as those starting with C0 or C1, are avoided.
   * It is used as a lower bound check for determining the validity of 2-byte UTF-8 sequences.
   */
  private static final int LEAD_MIN_2 = 0xC2;       // valid 2-byte lead starts at C2 (avoid overlongs: C0/C1)

  /**
   * Represents the maximum lead byte value allowed for two-byte encodings in the context of
   * encoding detection. This value is inclusive.
   * <p>
   * LEAD_MAX_2 is used for validating or processing character encodings where a lead byte is
   * expected to fall within a specific range, particularly for encodings that involve two-byte
   * sequences.
   */
  private static final int LEAD_MAX_2 = 0xDF;       // inclusive

  /**
   * Represents the maximum inclusive value for the third range of lead bytes used in the detection
   * of encoding or character classification. This value is used to signify the upper bound for
   * specific lead-byte comparisons during encoding analysis.
   */
  private static final int LEAD_MAX_3 = 0xEF;       // inclusive

  /**
   * The upper bound for a UTF-8 lead byte used in enforcing encoding limits, specifically for the
   * four-byte sequences as per RFC 3629. In UTF-8 encoding, this value represents the highest
   * possible valid lead byte for sequences requiring four bytes.
   */
  private static final int LEAD_MAX_4 = 0xF4;       // RFC 3629 upper bound

  /**
   * Defines the maximum number of bytes to be scanned when attempting to detect the encoding from
   * an XML declaration.
   * <p>
   * This value is used as a limit to ensure that only the initial portion of the input stream is
   * analyzed for an XML declaration, optimizing performance and preventing unnecessary reading of
   * large streams.
   */
  private static final int MAX_DECLARATION_SCAN_BYTES = 200;

  /**
   * A collection of encoding declarations commonly found in text or document metadata. This array
   * represents potential character sets that may be detected or processed when analyzing input
   * streams or document content.
   * <p>
   * The encodings included are: - UTF-8: A widely used encoding for Unicode text. - ISO-8859-1: A
   * single-byte character encoding covering Western European languages. - UTF-16: A variable-width
   * encoding for Unicode, often used with a BOM. - UTF-16BE: Big-endian representation of
   * UTF-16-encoded text. - UTF-16LE: Little-endian representation of UTF-16-encoded text.
   * <p>
   * This collection is immutable and serves as a reference point for encoding detection logic
   * within the application.
   */
  private static final String[] CANDIDATE_DECLARATIONS = {"UTF-8", "ISO-8859-1", "UTF-16",
      "UTF-16BE", "UTF-16LE"};

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

  /**
   * Detects the likely character encoding of the provided input stream. The method analyzes the
   * input stream's data to determine if it contains characters with high-bit values or if it
   * appears to be UTF-8 encoded. If no high-bit characters are detected or the content resembles
   * UTF-8 encoding, UTF-8 is returned as the default encoding. Otherwise, Windows-1252 is assumed.
   *
   * @param inputStream The input stream to analyze for likely encoding detection.
   * @return The detected Charset, either UTF-8 or Windows-1252.
   * @throws IOException If an I/O error occurs while reading the input stream.
   */
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

    if (!hasHighBit || isPlausibleUtf8(buffer, bytesRead)) {
      return StandardCharsets.UTF_8;
    }

    return WINDOWS_1252;
  }

  /**
   * Checks if the given byte array slice appears to represent UTF-8-encoded text by analyzing its
   * byte patterns.
   *
   * @param buffer The byte array to be analyzed.
   * @param length The number of bytes (from start of buffer) to analyze.
   * @return true if the byte array looks like it is UTF-8-encoded, false otherwise.
   */
  private static boolean isPlausibleUtf8(byte[] buffer, int length) {
    int i = 0;
    while (i < length) {
      int b = buffer[i] & 0xFF;

      if (b < ASCII_MAX) {
        i++;
        continue;
      }

      if (b < LEAD_MIN_2) {
        // Continuation byte or overlong sequence prefix (C0/C1) cannot start a new sequence
        return false;
      }

      int expectedContinuationBytes;
      if (b <= LEAD_MAX_2) {
        expectedContinuationBytes = 1; // 110xxxxx
      } else if (b <= LEAD_MAX_3) {
        expectedContinuationBytes = 2; // 1110xxxx
      } else if (b <= LEAD_MAX_4) {
        expectedContinuationBytes = 3; // 11110xxx
      } else {
        // > F4 or invalid range
        return false;
      }

      int remaining = length - i - 1;
      if (remaining < expectedContinuationBytes) {
        // Not enough bytes in the sample to validate fully; assume UTF-8 to avoid false negatives
        return true;
      }

      if (!areValidContinuationBytes(buffer, i + 1, expectedContinuationBytes)) {
        return false;
      }

      i += expectedContinuationBytes + 1;
    }

    return true;
  }

  /**
   * Validates that the specified slice of a byte array contains only valid UTF-8 continuation
   * bytes. A valid UTF-8 continuation byte must have the most significant two bits set to `10`.
   *
   * @param buffer The byte array to validate.
   * @param start The starting index within the array from which to begin validation.
   * @param count The number of bytes to validate, starting from the specified index.
   * @return true if all the specified bytes are valid UTF-8 continuation bytes, false otherwise.
   */
  private static boolean areValidContinuationBytes(byte[] buffer, int start, int count) {
    for (int idx = 0; idx < count; idx++) {
      int c = buffer[start + idx] & 0xFF;
      if ((c & 0xC0) != 0x80) {
        return false;
      }
    }
    return true;
  }

  /**
   * Validates whether the content of the provided input stream is valid for the given character
   * set. The method reads a portion of the input stream and attempts to decode it using the
   * specified charset. If the content is successfully decoded, it is considered valid for the
   * charset.
   *
   * @param inputStream The input stream whose content is to be validated.
   * @param charset The character set to validate the content against. A null value will result in a
   * false return.
   * @return true if the content of the input stream is valid for the charset, false if it cannot be
   * decoded or if the charset is null.
   * @throws IOException If an I/O error occurs while reading the input stream.
   */
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
    byte[] bomBuffer = new byte[4];
    int readCount = pushbackStream.read(bomBuffer);

    // UTF-32 BE BOM: 00 00 FE FF (check first as it requires 4 bytes)
    if (readCount >= 4 && hasPrefix(bomBuffer, UTF32_BE)) {
      return new BomDetectionResult(Charset.forName("UTF-32BE"), pushbackStream);
    }

    // UTF-8 BOM: EF BB BF
    if (readCount >= 3 && hasPrefix(bomBuffer, UTF8)) {
      unreadExtraBytes(pushbackStream, bomBuffer, readCount, 3);
      return new BomDetectionResult(StandardCharsets.UTF_8, pushbackStream);
    }

    // UTF-16 BE BOM: FE FF
    if (readCount >= 2 && hasPrefix(bomBuffer, UTF16_BE)) {
      unreadExtraBytes(pushbackStream, bomBuffer, readCount, 2);
      return new BomDetectionResult(StandardCharsets.UTF_16BE, pushbackStream);
    }

    // UTF-16 LE BOM: FF FE (or UTF-32 LE BOM: FF FE 00 00)
    if (readCount >= 2 && hasPrefix(bomBuffer, UTF16_LE)) {
      if (readCount >= 4 && bomBuffer[2] == UTF32_LE_SUFFIX[0]
          && bomBuffer[3] == UTF32_LE_SUFFIX[1]) {
        // UTF-32 LE BOM: FF FE 00 00 - all 4 bytes consumed
        return new BomDetectionResult(Charset.forName("UTF-32LE"), pushbackStream);
      }
      unreadExtraBytes(pushbackStream, bomBuffer, readCount, 2);
      return new BomDetectionResult(StandardCharsets.UTF_16LE, pushbackStream);
    }

    // No BOM found, push back all bytes
    if (readCount > 0) {
      pushbackStream.unread(bomBuffer, 0, readCount);
    }
    return new BomDetectionResult(null, pushbackStream);
  }

  /**
   * Checks whether the provided byte array {@code buffer} starts with the specified byte array
   * {@code prefix}.
   *
   * @param buffer The byte array to check. This represents the main data to be analyzed. Must not
   * be null and should have a length greater than or equal to {@code prefix}.
   * @param prefix The byte array that represents the prefix to be compared against. Must not be
   * null and should have a valid length to perform the comparison.
   * @return {@code true} if {@code buffer} starts with the content of {@code prefix}, {@code false}
   * otherwise.
   */
  private static boolean hasPrefix(byte[] buffer, byte[] prefix) {
    if (buffer == null || prefix == null || buffer.length < prefix.length) {
      return false;
    }
    for (int i = 0; i < prefix.length; i++) {
      if (buffer[i] != prefix[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Rewinds any extra bytes read from the stream that were not consumed, so they can be re-read
   * later. If the number of extra bytes to be unread is greater than zero, the unread bytes are
   * pushed back into the stream using the unread method of PushbackInputStream.
   *
   * @param stream The pushback input stream to which the extra bytes should be unread. Must not be
   * null.
   * @param buffer The buffer containing the bytes read from the stream. Must not be null.
   * @param readCount The total number of bytes read from the stream into the buffer.
   * @param consumedLength The number of bytes that were actually processed or consumed.
   * @throws IOException If an I/O error occurs while trying to unread bytes into the stream.
   */
  private static void unreadExtraBytes(PushbackInputStream stream, byte[] buffer, int readCount,
      int consumedLength)
      throws IOException {
    int extra = readCount - consumedLength;
    if (extra > 0) {
      // For 1 extra byte, unread(int) is equivalent, but unread(byte[],off,len) is consistent
      stream.unread(buffer, consumedLength, extra);
    }
  }

  /**
   * Attempts to detect the character encoding of an XML document by analyzing its XML declaration.
   * This method reads a portion of the input stream and checks for a declared encoding within the
   * XML declaration using a series of candidate encodings.
   *
   * @param inputStream The input stream containing the XML content. Must not be null and should
   * contain enough data for the XML declaration to be analyzed.
   * @return The detected Charset based on the encoding specified in the XML declaration, or null if
   * no encoding can be determined.
   * @throws IOException If an I/O error occurs while reading the input stream.
   */
  private static Charset detectFromXmlDeclaration(InputStream inputStream) throws IOException {
    byte[] previewBuffer = new byte[BUFFER_SIZE];
    int previewBytesRead = inputStream.read(previewBuffer);
    if (previewBytesRead <= 0) {
      return null;
    }

    int scanLength = Math.min(previewBytesRead, MAX_DECLARATION_SCAN_BYTES);
    for (String candidateEncoding : CANDIDATE_DECLARATIONS) {
      try {
        String xmlStart = new String(previewBuffer, 0, scanLength, candidateEncoding);
        Charset parsed = parseEncodingFromXmlDeclaration(xmlStart);
        if (parsed != null) {
          return parsed;
        }
      } catch (UnsupportedEncodingException | IllegalArgumentException e) {
        // Unknown candidateEncoding; try the next one.
      }
    }
    return null;
  }

  /**
   * Parses the character encoding from the provided XML declaration string, if available. If the
   * string contains an XML declaration with a specified encoding attribute, this method extracts
   * and returns the corresponding Charset. If no encoding is found, it defaults to UTF-8 as
   * recommended by the XML specification.
   *
   * @param xmlStart The beginning of the XML file as a string, which may contain an XML
   * declaration. Must not be null but can be empty. If no declaration or encoding is detected, this
   * method falls back to returning UTF-8.
   * @return The detected Charset based on the XML declaration or null if the input is invalid or
   * the encoding cannot be determined.
   */
  private static Charset parseEncodingFromXmlDeclaration(String xmlStart) {
    if (xmlStart == null || !xmlStart.contains("<?xml")) {
      return null;
    }
    int declEnd = xmlStart.indexOf("?>");
    if (declEnd <= 0) {
      return null;
    }

    String declaration = xmlStart.substring(0, declEnd + 2);
    Matcher matcher = XML_ENCODING_PATTERN.matcher(declaration);
    if (matcher.find()) {
      String encodingName = matcher
          .group(1)
          .trim();
      try {
        return Charset.forName(encodingName);
      } catch (IllegalArgumentException ex) {
        // Unknown encoding specified; fall through to default behavior below.
      }
    }
    // XML declaration found but no encoding specified; default per XML spec recommendation.
    return StandardCharsets.UTF_8;
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
