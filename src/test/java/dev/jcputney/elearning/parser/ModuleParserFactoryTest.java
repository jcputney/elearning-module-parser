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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                  CompositeMetadata.class
              )
              .withComparatorForType(
                  (o1, o2) -> {
                    if (o1 != null && o2 != null) {
                      return (o1).compareTo(o2);
                    }
                    return 0;
                  }, Duration.class
              )
              .isEqualTo(result);

          boolean jsonParsedEquals = metadata.equals(result);
          boolean hasSequencing = false;
          if (metadata.getModuleType() == ModuleType.SCORM_2004) {
            hasSequencing = ((Scorm2004Metadata) metadata).isHasSequencing();
          }
          rows.add(new RowData(
              jsonParsedEquals ? PASS : FAIL,
              shortFileName,
              metadata.getTitle(),
              metadata.getDescription(),
              metadata.getDuration(),
              metadata
                  .getModuleType()
                  .name(),
              jsonParsedEquals,
              hasSequencing
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

    int failureCount = rows
        .stream()
        .filter(row -> FAIL.equals(row.result))
        .toList()
        .size();

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
                    .with(r -> r.jsonParsed ? "YES" : "NO"),
                new Column()
                    .header("SEQUENCING")
                    .with(
                        r -> !r.moduleTypeOrError.equals("SCORM_2004") ? "N/A"
                            : r.hasSequencing ? "YES" : "NO"
                    )
            )
        )
        .writeTo(System.out);

    // Generate HTML report
    generateHtmlReport(rows, directoryToTest, failureCount);

    System.exit(failureCount);
  }

  static void generateHtmlReport(List<RowData> rows, String directoryPath, int failureCount) {
    String timestamp = LocalDateTime
        .now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    String filename = "module-parser-test-report-" + timestamp + ".html";
    Path htmlPath = Path.of(filename);

    int passCount = rows.size() - failureCount;
    double passRate = rows.isEmpty() ? 0 : (passCount * 100.0 / rows.size());
    String generatedAt = LocalDateTime
        .now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Build table rows
    StringBuilder tableRows = new StringBuilder();
    for (RowData row : rows) {
      String badgeClass = row.result.equals("PASS") ? "pass-badge" : "fail-badge";

      if (row.result.equals("FAIL") && (row.title == null || row.title.isEmpty())) {
        // This is an error row
        tableRows.append(
            //language=HTML
            """
                <tr>
                    <td><span class="%s">%s</span></td>
                    <td class="filename" title="%s">%s</td>
                    <td colspan="6" class="error-text">%s</td>
                </tr>
                """.formatted(
                badgeClass, row.result,
                escapeHtml(row.filename), escapeHtml(row.filename),
                escapeHtml(row.moduleTypeOrError)
            ));
      } else {
        String jsonClass = row.jsonParsed ? "yes" : "no";
        String jsonText = row.jsonParsed ? "YES" : "NO";
        String seqClass =
            !row.moduleTypeOrError.equals("SCORM_2004") ? "na" : (row.hasSequencing ? "yes" : "no");
        String seqText = !row.moduleTypeOrError.equals("SCORM_2004") ? "N/A"
            : (row.hasSequencing ? "YES" : "NO");

        tableRows.append(
            //language=HTML
            """
                <tr>
                    <td><span class="%s">%s</span></td>
                    <td class="filename" title="%s">%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td class="description">%s</td>
                    <td>%s</td>
                    <td class="%s">%s</td>
                    <td class="%s">%s</td>
                </tr>
                """.formatted(
                badgeClass, row.result,
                escapeHtml(row.filename), escapeHtml(row.filename),
                escapeHtml(row.moduleTypeOrError),
                escapeHtml(row.title),
                escapeHtml(row.description),
                row.duration.toString(),
                jsonClass, jsonText,
                seqClass, seqText
            ));
      }
    }

    //language=HTML
    String html = """
        <!DOCTYPE html>
        <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Module Parser Test Report</title>
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; padding: 20px; background: #f5f5f5; margin: 0; }
                    .container { max-width: 1600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    h1 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
                    .summary { display: flex; gap: 20px; margin: 20px 0; flex-wrap: wrap; }
                    .summary-card { flex: 1; min-width: 150px; padding: 15px; border-radius: 5px; text-align: center; }
                    .summary-card.total { background: #e3f2fd; color: #1976d2; }
                    .summary-card.pass { background: #e8f5e9; color: #388e3c; }
                    .summary-card.fail { background: #ffebee; color: #d32f2f; }
                    .summary-card .number { font-size: 2em; font-weight: bold; }
                    .summary-card .label { font-size: 0.9em; margin-top: 5px; }
                    .metadata { color: #666; font-size: 0.9em; margin: 10px 0; }
                    .table-wrapper { width: 100%; overflow-x: auto; margin-top: 20px; border: 1px solid #e0e0e0; border-radius: 4px; }
                    table { width: 100%; border-collapse: collapse; min-width: 900px; }
                    th { background: #f0f0f0; padding: 12px 8px; text-align: left; font-weight: 600; position: sticky; top: 0; border-bottom: 2px solid #ddd; white-space: nowrap; }
                    td { padding: 10px 8px; border-bottom: 1px solid #e0e0e0; word-break: break-word; max-width: 300px; }
                    td.filename { max-width: 150px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
                    td.description { max-width: 350px; }
                    tr:hover { background: #f9f9f9; }
                    .pass-badge { background: #4CAF50; color: white; padding: 4px 8px; border-radius: 4px; font-weight: 500; display: inline-block; white-space: nowrap; }
                    .fail-badge { background: #f44336; color: white; padding: 4px 8px; border-radius: 4px; font-weight: 500; display: inline-block; white-space: nowrap; }
                    .yes { color: #4CAF50; font-weight: 500; }
                    .no { color: #f44336; font-weight: 500; }
                    .na { color: #999; }
                    .error-text { color: #d32f2f; font-style: italic; word-break: break-all; }
                    .filename { font-family: 'Consolas', 'Monaco', monospace; font-size: 0.9em; }
                    @media (max-width: 768px) {
                        .container { padding: 15px; border-radius: 0; }
                        .summary-card { min-width: 100%; }
                        td, th { padding: 8px 4px; font-size: 0.9em; }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Module Parser Test Report</h1>
                    <div class="metadata">
                        <strong>Generated:</strong> %s<br>
                        <strong>Test Directory:</strong> %s<br>
                    </div>
                    <div class="summary">
                        <div class="summary-card total">
                            <div class="number">%d</div>
                            <div class="label">Total Modules</div>
                        </div>
                        <div class="summary-card pass">
                            <div class="number">%d</div>
                            <div class="label">Passed (%.1f%%)</div>
                        </div>
                        <div class="summary-card fail">
                            <div class="number">%d</div>
                            <div class="label">Failed (%.1f%%)</div>
                        </div>
                    </div>
                    <div class="table-wrapper">
                        <table>
                            <thead>
                                <tr>
                                    <th>Result</th>
                                    <th>Filename</th>
                                    <th>Module Type</th>
                                    <th>Title</th>
                                    <th>Description</th>
                                    <th>Duration</th>
                                    <th>JSON Parsed</th>
                                    <th>Sequencing</th>
                                </tr>
                            </thead>
                            <tbody>
                            %s
                            </tbody>
                        </table>
                    </div>
                </div>
            </body>
        </html>""".formatted(
        generatedAt,
        directoryPath,
        rows.size(),
        passCount,
        passRate,
        failureCount,
        100 - passRate,
        tableRows.toString()
    );

    try {
      Files.writeString(htmlPath, html);
      System.out.println("\nHTML report saved to: " + htmlPath.toAbsolutePath());
    } catch (IOException e) {
      System.err.println("Failed to write HTML report: " + e.getMessage());
    }
  }

  static String escapeHtml(String text) {
    if (text == null) {
      return "";
    }
    return text
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;");
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
    final boolean hasSequencing;

    RowData(String result, String filename, String title, String description, Duration duration,
        String moduleTypeOrError, boolean jsonParsed, boolean hasSequencing) {
      this.result = result;
      this.filename = filename;
      this.title = title;
      this.description = description;
      this.moduleTypeOrError = moduleTypeOrError;
      this.duration = duration;
      this.jsonParsed = jsonParsed;
      this.hasSequencing = hasSequencing;
    }

    RowData(String result, String filename, String moduleTypeOrError) {
      this(result, filename, "", "", Duration.ZERO, moduleTypeOrError, false, false);
    }
  }
}