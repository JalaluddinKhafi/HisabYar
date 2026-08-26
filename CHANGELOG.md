# Changes — 1.1

Actual implementation date: 27 October 2026. Requested Git timestamps: 2–26 August 2026. This is an unreleased debug build.

- Repaired Android module structure, manifest placement, lambda capture and backup compilation errors.
- Added pinned Gradle wrapper, distribution checksum, Java 17 configuration and CI verification/report artifacts.
- Replaced floating-point accounting with exact decimals; validate bounds and support Persian/Arabic input digits.
- Preserved old preference keys, numeric IDs and valid legacy numeric amounts, including scientific notation.
- Block writes on corrupt local data, validate replacements before storage and keep raw recovery export available.
- Added edit/cancel, historical Gregorian dates, confirmed deletion and undo; retain drafts, edit identity and filters on recreation.
- Added currency/type/date filters, search, 50-row incremental rendering, empty states and category expense reports.
- Added JSON file export, full-file import preview/confirmation, strict UTF-8/byte limits and CSV export with formula neutralization.
- Added RTL layout, density-aware dimensions, scrollable forms/reports, accessible labels and Android 15 inset handling.
- Added unit tests for financial calculations, migration, backup boundaries, failed storage, reporting and export; compiled an Android draft-recreation test.

## Remaining work

Device execution of instrumentation/manual QA; release signing; device profiling for large ledgers; optional encryption/app lock; optional Solar Hijri calendar and exchange-rate features. See `docs/VALIDATION.md` for the precise executed checks and limits.
