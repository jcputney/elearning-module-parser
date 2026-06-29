/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.conformance;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.api.ModuleParserFactory;
import dev.jcputney.elearning.parser.api.ParseResult;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.factory.DefaultModuleParserFactory;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Conformance test that runs the official ADL SCORM 2004 4th Edition CTS (Conformance Test Suite)
 * content packages through {@link dev.jcputney.elearning.parser.api.ModuleParser#parseAndValidate()}
 * and asserts they validate cleanly (no ERROR-severity issues). These packages are the canonical
 * "known-valid" corpus published by ADL, so any ERROR they produce points to a real conformance gap
 * in this parser rather than a problem with the package.
 * <p>
 * <strong>Curated subset.</strong> The fixtures under
 * {@code src/test/resources/modules/conformance/scorm2004/adl-cts/} are a deliberately small,
 * representative slice (15 packages) spanning the CTS category prefixes CM, CO, CT, RU, OB, SX, and
 * MS — <em>not</em> the full corpus of 189 packages. The packages are copied verbatim from
 * {@code SCORM-2004-4ed-Test-Suite/software_development/TestSuite/LMSRTE/Courses/LMSTestPackage_*}.
 * See {@code adl-cts/README.md} for the exact provenance and the steps to expand this to all 189
 * packages.
 * </p>
 * <p>
 * <strong>Scope.</strong> The CTS packages ship only their {@code imsmanifest.xml}; their shared SCO
 * and asset files (the {@code resources/}, {@code common/}, {@code includes/} content) are assembled
 * from a shared depot at deploy time and are not self-contained on disk. That is fine here because
 * physical file-existence validation is OFF by default (see
 * {@link dev.jcputney.elearning.parser.config.FileExistenceValidator}); {@code parseAndValidate()}
 * runs only the manifest-internal structural rules. This test therefore exercises manifest
 * conformance, not on-disk asset presence.
 * </p>
 */
@DisplayName("SCORM 2004 4th Edition ADL CTS conformance")
class Scorm2004CtsConformanceTest {

  /**
   * Root directory of the curated ADL CTS corpus, relative to the module root (matches the path
   * convention used by the other parser tests).
   */
  private static final Path ADL_CTS_DIR =
      Paths.get("src/test/resources/modules/conformance/scorm2004/adl-cts");

  /**
   * Supplies one argument per ADL CTS package directory. Discovered by scanning the corpus directory
   * at runtime, so dropping additional {@code LMSTestPackage_*} folders into {@link #ADL_CTS_DIR}
   * automatically extends the parameterized run with no code change.
   *
   * @return a stream of package directories to validate
   * @throws IOException if the corpus directory cannot be listed
   */
  static Stream<Path> adlCtsPackages() throws IOException {
    try (Stream<Path> entries = Files.list(ADL_CTS_DIR)) {
      return entries
          .filter(Files::isDirectory)
          .sorted()
          // Collect eagerly so the directory stream can close before the test consumes it.
          .collect(Collectors.toList())
          .stream();
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("adlCtsPackages")
  @DisplayName("ADL CTS package validates with no ERROR-severity issues")
  void adlCtsPackageValidatesCleanly(Path packageDir) throws Exception {
    LocalFileAccess fileAccess = new LocalFileAccess(packageDir.toString());
    ModuleParserFactory factory = new DefaultModuleParserFactory(fileAccess);
    ParseResult<?> result = factory.getParser().parseAndValidate();

    ModuleMetadata<?> metadata = result.metadata();
    String name = packageDir.getFileName().toString();

    // Surface (but do not fail on) any WARNING-severity issues for visibility.
    List<ValidationIssue> warnings = result.validation().getWarnings();
    if (!warnings.isEmpty()) {
      System.out.printf("[ADL CTS] %s produced %d warning(s):%n", name, warnings.size());
      warnings.forEach(w ->
          System.out.printf("    WARN %s @ %s: %s%n", w.code(), w.location(), w.message()));
    }

    // The canonical ADL packages must validate without ERROR-severity issues.
    List<ValidationIssue> errors = result.validation().getErrors();
    assertThat(errors)
        .as("ADL CTS package '%s' is canonical and must produce no ERROR-severity "
            + "validation issues, but found: %s", name, errors)
        .isEmpty();

    // Module type and edition must be detected as SCORM 2004 4th Edition.
    assertThat(metadata.getModuleType())
        .as("module type for '%s'", name)
        .isEqualTo(ModuleType.SCORM_2004);
    assertThat(metadata.getModuleEditionType())
        .as("edition for '%s'", name)
        .isEqualTo(ModuleEditionType.SCORM_2004_4TH_EDITION);

    // Core metadata must resolve.
    assertThat(metadata.getTitle())
        .as("title for '%s'", name)
        .isNotBlank();
    assertThat(metadata.getLaunchUrl())
        .as("launch URL for '%s'", name)
        .isNotBlank();
  }

  /**
   * Negative control: proves the conformance assertions above can actually fail. Without this, a bug
   * that made every package "pass" would go unnoticed. The fixture is a real ADL CM-01 manifest with
   * its {@code organizations/@default} pointed at a non-existent organization id; the parser falls
   * back to the first organization for metadata, so title/launch still resolve while the structural
   * validator flags the dangling default reference.
   * <p>
   * This case targets the manifest-internal structural validator (the default, shipping
   * configuration), so it pins XSD validation OFF for the duration. That keeps it deterministic
   * regardless of how the suite is invoked: with XSD ON the bundled schema rejects the dangling
   * {@code default} IDREF earlier (a {@code cvc-id.1} failure that throws before structural
   * validation runs), which is correct behavior but a different code path than the one under test.
   */
  @Nested
  @DisplayName("Negative control")
  class NegativeCases {

    private static final String XSD_PROP = "elearning.parser.scorm2004.validateXsd";

    private static final Path BROKEN_DEFAULT_ORG = Paths.get(
        "src/test/resources/modules/conformance/scorm2004/adl-cts-invalid/CM-01-broken-default-org");

    private String originalXsdProp;

    @BeforeEach
    void disableXsdValidation() {
      originalXsdProp = System.getProperty(XSD_PROP);
      System.setProperty(XSD_PROP, "false");
    }

    @AfterEach
    void restoreXsdValidation() {
      if (originalXsdProp == null) {
        System.clearProperty(XSD_PROP);
      } else {
        System.setProperty(XSD_PROP, originalXsdProp);
      }
    }

    @Test
    @DisplayName("broken default-organization reference reports SCORM2004_INVALID_DEFAULT_ORG")
    void brokenDefaultOrgReportsError() throws Exception {
      LocalFileAccess fileAccess = new LocalFileAccess(BROKEN_DEFAULT_ORG.toString());
      ModuleParserFactory factory = new DefaultModuleParserFactory(fileAccess);
      ParseResult<?> result = factory.getParser().parseAndValidate();

      assertThat(result.validation().hasErrors())
          .as("broken default-org fixture should fail validation")
          .isTrue();

      List<String> errorCodes = result.validation().getErrors().stream()
          .map(ValidationIssue::code)
          .collect(Collectors.toList());
      assertThat(errorCodes)
          .as("expected the dangling default-organization reference to be reported")
          .contains("SCORM2004_INVALID_DEFAULT_ORG");
    }
  }
}
