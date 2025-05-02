/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.Styler;
import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.input.aicc.Descriptor;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.CompositeMetadata;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import dev.jcputney.elearning.parser.output.metadata.cmi5.Cmi5Metadata;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ModuleParserFactoryTest {

  // ANSI escape code constants
  private static final String RESET = "\u001B[0m";
  private static final String GREEN = "\u001B[32m";
  private static final String RED = "\u001B[31m";
  private static final String PASS = "PASS";
  private static final String FAIL = "FAIL";

  public static void main(String[] args) {
    String directoryToTest = args.length > 0 ? args[0] : "./";
    Path basePath = Path.of(directoryToTest);

    // We'll collect the results in a list of RowData, then print them at the end
    List<RowData> rows = new ArrayList<>();

    Styler styler = new Styler() {
      @Override
      public List<String> styleCell(Column column, int row, int col, List<String> data) {
        String color = switch (data
            .get(0)
            .trim()) {
          case PASS -> GREEN;
          case FAIL -> RED;
          default -> "";
        };
        return data
            .stream()
            .map(line -> color + line + RESET)
            .collect(Collectors.toList());
      }
    };

    ObjectMapper mapper = createConfiguredObjectMapper();

    // Recursively walk the directory to find .zip files
    try (Stream<Path> pathStream = Files.walk(basePath)) {
      List<Path> zipFiles = pathStream
          .filter(Files::isRegularFile)
          .filter(path -> path
              .getFileName()
              .toString()
              .endsWith(".zip"))
          .sorted((p1, p2) -> p1
              .toString()
              .compareToIgnoreCase(p2.toString()))
          .toList();

      // Process each .zip file
      for (Path zipFilePath : zipFiles) {
        String shortFileName = basePath
            .relativize(zipFilePath)
            .toString();

        try {
          // Create the parser factory and parser
          ModuleParserFactory parserFactory =
              new DefaultModuleParserFactory(new ZipFileAccess(zipFilePath.toString()));
          ModuleParser<?> parser = parserFactory.getParser();

          // Parse metadata
          ModuleMetadata<?> metadata = parser.parse();

          String json = mapper.writeValueAsString(metadata);
          JsonNode jsonNode = mapper.readTree(json);
          ModuleType moduleType = ModuleType.valueOf(jsonNode
              .get("moduleType")
              .asText());
          ModuleMetadata<?> result = switch (moduleType) {
            case SCORM_12 -> mapper.readValue(json, Scorm12Metadata.class);
            case SCORM_2004 -> mapper.readValue(json, Scorm2004Metadata.class);
            case AICC -> mapper.readValue(json, AiccMetadata.class);
            case CMI5 -> mapper.readValue(json, Cmi5Metadata.class);
          };

          Assertions
              .assertThat(metadata)
              .usingRecursiveComparison()
              .ignoringFieldsOfTypes(
                  CompositeMetadata.class,
                  Descriptor.class
              )
              .withComparatorForType(
                  (o1, o2) -> {
                    if (o1 instanceof Duration && o2 instanceof Duration) {
                      return (o1).compareTo(o2);
                    }
                    return 0;
                  }, Duration.class
              )
              .ignoringFields(
                  "manifest.launchUrl"
              )
              .isEqualTo(result);

          // PASS row
          rows.add(new RowData(
              PASS,
              shortFileName,
              metadata.getTitle(),
              metadata.getDescription(),
              metadata.getDuration(),
              metadata
                  .getModuleType()
                  .name(),
              metadata.equals(result)
          ));

        } catch (IOException e) {
          // I/O Failure
          rows.add(new RowData(
              FAIL,
              shortFileName,
              e.getMessage()
          ));
        } catch (ModuleDetectionException e) {
          // Could not detect a valid SCORM module
          rows.add(new RowData(
              FAIL,
              shortFileName,
              e.getMessage()
          ));
        } catch (ModuleParsingException e) {
          // Detected module but parsing failed
          rows.add(new RowData(
              FAIL,
              shortFileName,
              e.getCause() != null ? e
                  .getCause()
                  .getMessage() : e.getMessage()
          ));
        }
      }

    } catch (IOException e) {
      throw new RuntimeException("Failed to walk files in directory: " + directoryToTest, e);
    }

    // Build and print the ASCII table using freva/ascii-table
    AsciiTable.builder()
        // Use a built-in border style
        .border(AsciiTable.BASIC_ASCII)
        .styler(styler)
        .data(
            rows, Arrays.asList(
                new Column()
                    .header("RESULT")
                    .with(r -> r.result),
                new Column()
                    .header("FILENAME")
                    .with(r -> r.filename),
                new Column()
                    .header("MODULE TYPE")
                    .with(r -> r.moduleTypeOrError),
                new Column()
                    .header("TITLE")
                    .with(r -> r.title),
                new Column()
                    .header("DESCRIPTION")
                    .with(r -> r.description),
                new Column()
                    .header("DURATION")
                    .with(r -> r.duration.toString()),
                new Column()
                    .header("JSON PARSED")
                    .with(r -> r.jsonParsed ? "YES" : "NO")
            )
        )
        .writeTo(System.out);
  }

  static ObjectMapper createConfiguredObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

  @Test
  void getScorm2004Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm2004.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm2004Parser.class, parser.getClass());
  }

  @Test
  void getScorm12Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm12.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm12Parser.class, parser.getClass());
  }

  @Test
  void getAiccParser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/aicc.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(AiccParser.class, parser.getClass());
  }

  @Test
  void getCmi5Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/cmi5.zip";
    ModuleParserFactory parserFactory = new DefaultModuleParserFactory(
        new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Cmi5Parser.class, parser.getClass());
  }

  @Test
  void parseModule() {
  }

  // Simple container class to hold each row's data
  public static class RowData {

    final String result;    // PASS / FAIL (with color)
    final String filename;  // relative path of the zip file
    final String title;
    final String description;
    final Duration duration;
    final String moduleTypeOrError;
    final boolean jsonParsed;

    RowData(String result, String filename, String title, String description, Duration duration,
        String moduleTypeOrError, boolean jsonParsed) {
      this.result = result;
      this.filename = filename;
      this.title = title;
      this.description = description;
      this.moduleTypeOrError = moduleTypeOrError;
      this.duration = duration;
      this.jsonParsed = jsonParsed;
    }

    RowData(String result, String filename, String moduleTypeOrError) {
      this(result, filename, "", "", Duration.ZERO, moduleTypeOrError, false);
    }
  }
}