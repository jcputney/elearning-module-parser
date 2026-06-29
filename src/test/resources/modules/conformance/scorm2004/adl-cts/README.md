# ADL SCORM 2004 4th Edition CTS conformance corpus

These directories are content packages copied **verbatim** from the official ADL SCORM 2004 4th
Edition Test Suite (the canonical conformance contract). They drive
`Scorm2004CtsConformanceTest`, which runs each package through `parseAndValidate()` and asserts it
validates with **no ERROR-severity issues**.

## Provenance

Source: `SCORM-2004-4ed-Test-Suite/software_development/TestSuite/LMSRTE/Courses/LMSTestPackage_*`

Each `LMSTestPackage_<id>/` folder here is a byte-for-byte copy of the corresponding source folder.

## This is a curated subset (15 of 189)

The full ADL CTS has **189** `LMSTestPackage_*` packages. This corpus is a deliberately small,
representative slice — **not** full coverage — spanning the CTS category prefixes:

| Prefix | Meaning (CTS area)            | Included here          |
|--------|-------------------------------|------------------------|
| CM     | Content Management / packaging | CM-01, CM-05, CM-08    |
| CO     | Completion / objectives        | CO-01, CO-06           |
| CT     | Control modes / sequencing     | CT-01, CT-02           |
| RU     | Rollup rules                   | RU-09, RU-16           |
| OB     | Objectives                     | OB-04, OB-06           |
| SX     | Sequencing exceptions          | SX-02, SX-05           |
| MS     | Multi-SCO                      | MS-01, MS-04           |

## Scope: manifest conformance, not on-disk assets

The CTS packages ship only their `imsmanifest.xml`. Their SCO/asset files (`resources/`, `common/`,
`includes/`) are assembled from a shared depot (`Courses/SCODepot/`, `Courses/scripts/`,
`Courses/includes/`) at deploy time, so the packages are **not** self-contained on disk. Some
manifest `href`s (e.g. `common/LMSTest.jar`, `common/lmsrtefunctions.js`) do not resolve to files in
the source tree at all.

That is intentional and fine for this test: physical file-existence validation is **OFF by default**
(`FileExistenceValidator` / `-Delearning.parser.validateFileExists=true`), so `parseAndValidate()`
runs only the manifest-internal structural rules. This corpus therefore exercises **manifest
conformance**, not asset presence. Do **not** enable file-existence validation against this corpus
without first assembling the shared depot files into each package.

## How to expand to all 189 packages

The test discovers packages by listing this directory at runtime, so expansion is a copy step — no
code change required.

```sh
SRC="<repo>/SCORM-2004-4ed-Test-Suite/software_development/TestSuite/LMSRTE/Courses"
DEST="<repo>/elearning-module-parser/src/test/resources/modules/conformance/scorm2004/adl-cts"

for s in "$SRC"/LMSTestPackage_*; do
  cp -R "$s" "$DEST/$(basename "$s")"
done
```

Then re-run `mvn -Dtest=Scorm2004CtsConformanceTest test`. Any package that produces an
ERROR-severity issue is a genuine conformance finding to investigate (real parser gap vs. an
edge-case package), not a flaky test.

> Note on size: the full 189-package copy is still manifest-only (no large binaries), so it stays a
> lightweight test fixture.
