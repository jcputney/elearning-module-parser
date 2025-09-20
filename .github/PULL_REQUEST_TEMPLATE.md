# Pull Request Template

## Summary
- What does this change do and why? Keep it concise.

## Related Issues
- Closes #<issue-number> (or) Relates to #<issue-number>

## Type of Change
- [ ] Bug fix
- [ ] Feature
- [ ] Refactor/tech debt
- [ ] Documentation
- [ ] Build/CI
- [ ] Other

## What Changed
- Brief bullet list of key code changes and impacted packages (e.g., `dev.jcputney.elearning.parser.input.scorm2004.*`).

## How to Test
- Commands:
  - `./mvnw clean verify` (runs unit + integration tests)
  - `./mvnw test jacoco:report` (coverage at `target/site/jacoco/index.html`)
- Include any reproduction steps or sample module paths under `src/test/resources/modules/**`.

## Screenshots / Logs (optional)
- Add relevant output, stack traces, or images to illustrate the change.

## Breaking Changes
- Does this change affect public API or behavior? If yes, describe migration notes.

## Checklist
- [ ] Ran `./mvnw clean verify` locally with no failures
- [ ] Added/updated tests (positive, negative, edge cases)
- [ ] Updated README/docs as needed
- [ ] Followed `.editorconfig` (2-space indent, 100-char lines) and naming conventions
- [ ] No secrets or large binaries committed; sample content is minimal
- [ ] Preserved existing license headers and notices
- [ ] Clear commit messages and linked issues

## Additional Context (optional)
- Notes for reviewers, trade-offs, or follow-ups.

