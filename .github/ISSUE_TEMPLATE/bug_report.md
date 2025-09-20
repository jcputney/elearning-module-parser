---
name: Bug report
about: Create a report to help us improve
labels: bug
---

## Summary
Clear, concise description of the bug.

## Environment
- Library version: (e.g., 0.0.17)
- Java version: (e.g., 17.0.11)
- OS: (e.g., macOS 14, Ubuntu 22.04, Windows 11)
- File access backend: (ZipFileAccess, LocalFileAccess, S3 v1/v2, custom)

## Affected Module(s)
- Type: (SCORM 1.2, SCORM 2004 2nd/3rd/4th, AICC, cmi5)
- Can you share a minimal module or manifest snippet? (attach if possible)

## Steps to Reproduce
1. …
2. …
3. …

Example code/command:
```java
try (ZipFileAccess zfa = new ZipFileAccess("path/to/module.zip")) {
  ModuleParserFactory f = new DefaultModuleParserFactory(zfa);
  ModuleParser<?> p = f.getParser();
  p.parse();
}
```

## Expected Behavior
What you expected to happen.

## Actual Behavior
What actually happened (include stack trace and logs if available).

```
<stack trace / error>
```

## Additional Context
Any other context, configuration, or recent changes that might be relevant.

