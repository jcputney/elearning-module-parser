# Repository Guidelines

This repository is a Java 17+ Maven library for parsing eLearning modules (SCORM 1.2/2004, AICC, cmi5). Use the Maven wrapper and keep changes focused and well‑tested.

## Project Structure & Module Organization
- Source: `src/main/java/dev/jcputney/elearning/parser/**`
- Resources: `src/main/resources`
- Tests: `src/test/java/**`
- Test data: `src/test/resources/modules/**` (formats and sample zips)
- Build output: `target/`

## Build, Test, and Development Commands
- Build: `./mvnw clean compile` — compile sources.
- Unit tests: `./mvnw test` — run JUnit 5 and jqwik.
- Integration/verify: `./mvnw verify` — includes failsafe phase.
- Coverage: `./mvnw test jacoco:report` — see `target/site/jacoco/index.html`.
- Benchmarks (optional): `./mvnw clean test-compile exec:java -P benchmark`.

## Coding Style & Naming Conventions
- Java 17; follow `.editorconfig` (2‑space indent, max line length 100).
- Packages under `dev.jcputney.elearning.parser.*`.
- Class names in PascalCase; methods/fields in camelCase; constants in UPPER_SNAKE_CASE.
- Prefer immutable value types and clear nullability; avoid introducing new dependencies without discussion.
- Keep public API stable; add deprecations before breaking changes.

## Testing Guidelines
- Frameworks: JUnit 5, jqwik (property-based), Mockito, Testcontainers (as needed).
- Place tests mirroring package structure; name classes `*Test` (e.g., `ModuleParserFactoryTest`).
- Include positive, negative, and edge cases; add integration tests using fixtures in `src/test/resources/modules`.
- Ensure coverage remains meaningful; check JaCoCo report locally.

## Commit & Pull Request Guidelines
- Commits: concise, imperative subject (e.g., "Add SCORM 2004 rollup rules"); group related changes; reference issues (`#123`) when applicable.
- PRs: clear description of intent, summary of changes, tests added/updated, reproducible steps, and any screenshots/logs for failures.
- CI must pass; update README/docs if behavior or usage changes.

## Security & Configuration Tips
- Do not commit secrets or large binaries; keep sample modules minimal.
- Use `./mvnw` to ensure consistent builds; respect versions centralized in `pom.xml` properties.
- Preserve existing license headers and notices in touched files.

## Agent-Specific Instructions
- Keep changes scoped; avoid broad refactors.
- Do not modify release or publishing configuration unless requested.
- Follow these guidelines for any files within this repository.

