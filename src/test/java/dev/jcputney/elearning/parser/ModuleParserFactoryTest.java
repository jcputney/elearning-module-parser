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

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.Styler;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.ZipFileAccess;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import dev.jcputney.elearning.parser.parsers.Cmi5Parser;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ModuleParserFactoryTest {

  // ANSI escape code constants
  private static final String RESET = "\u001B[0m";
  private static final String GREEN = "\u001B[32m";
  private static final String RED = "\u001B[31m";
  private static final String PASS = "PASS";
  private static final String FAIL = "FAIL";

  // Simple container class to hold each row's data
  public static class RowData {
    final String result;    // PASS / FAIL (with color)
    final String filename;  // relative path of the zip file
    final String title;
    final String description;
    final String moduleTypeOrError;

    RowData(String result, String filename, String title, String description, String moduleTypeOrError) {
      this.result = result;
      this.filename = filename;
      this.title = title;
      this.description = description;
      this.moduleTypeOrError = moduleTypeOrError;
    }

    RowData(String result, String filename, String moduleTypeOrError) {
      this(result, filename, "", "", moduleTypeOrError);
    }
  }

  public static void main(String[] args) {
    String directoryToTest = args.length > 0 ? args[0] : "./";
    Path basePath = Path.of(directoryToTest);

    // We'll collect the results in a list of RowData, then print them at the end
    List<RowData> rows = new ArrayList<>();

    Styler styler = new Styler() {
      @Override
      public List<String> styleCell(Column column, int row, int col, List<String> data) {
        String color = switch (data.get(0).trim()) {
          case PASS -> GREEN;
          case FAIL -> RED;
          default -> "";
        };
        return data.stream().map(line -> color + line + RESET).collect(Collectors.toList());
      }
    };

    // Recursively walk the directory to find .zip files
    try (Stream<Path> pathStream = Files.walk(basePath)) {
      List<Path> zipFiles = pathStream
          .filter(Files::isRegularFile)
          .filter(path -> path.getFileName().toString().endsWith(".zip"))
          .sorted((p1, p2) -> p1.toString().compareToIgnoreCase(p2.toString()))
          .toList();

      // Process each .zip file
      for (Path zipFilePath : zipFiles) {
        String shortFileName = basePath.relativize(zipFilePath).toString();

        try {
          // Create the parser factory & parser
          ModuleParserFactory parserFactory =
              new ModuleParserFactory(new ZipFileAccess(zipFilePath.toString()));
          ModuleParser<?> parser = parserFactory.getParser();

          // Parse metadata
          ModuleMetadata<?> metadata = parser.parse();

          // PASS row
          rows.add(new RowData(
              PASS,
              shortFileName,
              metadata.getTitle(),
              metadata.getDescription(),
              metadata.getModuleType().name()
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
              e.getCause() != null ? e.getCause().getMessage() : e.getMessage()
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
                new Column().header("RESULT")
                    .with(r -> r.result),
                new Column().header("FILENAME")
                    .with(r -> r.filename),
                new Column().header("MODULE TYPE")
                    .with(r -> r.moduleTypeOrError),
                new Column().header("TITLE")
                    .with(r -> r.title),
                new Column().header("DESCRIPTION")
                    .with(r -> r.description)
            )
        )
        .writeTo(System.out);
  }

  @Test
  void getScorm2004Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm2004.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm2004Parser.class, parser.getClass());
  }

  @Test
  void getScorm12Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/scorm12.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Scorm12Parser.class, parser.getClass());
  }

  @Test
  void getAiccParser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/aicc.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(AiccParser.class, parser.getClass());
  }

  @Test
  void getCmi5Parser() throws IOException, ModuleDetectionException {
    String modulePath = "src/test/resources/modules/zips/cmi5.zip";
    ModuleParserFactory parserFactory = new ModuleParserFactory(new ZipFileAccess(modulePath));
    ModuleParser<?> parser = parserFactory.getParser();
    assertNotNull(parser);
    assertEquals(Cmi5Parser.class, parser.getClass());
  }

  @Test
  void parseModule() {
  }
}