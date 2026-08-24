# Local data and backup compatibility

Preferences remain `hisabyar_local` / key `entries`. A legacy top-level JSON array is accepted without renaming the key. Numeric timestamp IDs become strings; IDs remain stable. Numeric amounts are migrated to fixed two-decimal strings on the next successful save. Unknown currencies, types, invalid dates, duplicate IDs and invalid amounts reject the entire file, rather than dropping rows.

New files and preferences use this versioned envelope:

```json
{"app":"HisabYar","version":1,"entries":[{"id":"unique-string","type":"expense","amount":"12.50","currency":"AFN","category":"غذا","source":"","note":"","date":"2026-08-02"}]}
```

Allowed types: `expense`, `income`. Allowed currencies: `AFN`, `USD`. Dates are Gregorian `yyyy-MM-dd`. Amounts are positive, at most `999999999999.99`, with at most two fractional digits. IDs: 1–128 characters; category: 1–80; source: 0–200; note: 0–1000. Maximum: 10000 entries and 8 MiB per imported file. CSV is export-only.

Loading a corrupt local ledger locks writes. Raw export preserves the original preference string for external diagnosis. Keep the raw file before clearing app data. Restore is an explicit whole-ledger replacement, with validation before storage. Normal backup/CSV export is blocked when local loading failed.

The data is stored without app-level encryption. Updates must preserve application ID and signing key to retain installed app data. An older app build does not understand the new envelope; export before downgrading. Large ledgers should be profiled on the target device because persistence currently uses synchronous SharedPreferences commits.
