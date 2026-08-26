# ثبت commitهای درخواستی

۷۵ commit واقعی با تغییر غیرخالی، در ۲۵ روز از ۲ تا ۲۶ آگست ۲۰۲۶، روزانه سه commit. ساعت‌های هر روز ۰۹:۰۰، ۱۲:۰۰ و ۱۵:۰۰ به وقت کابل (+04:30) هستند. تاریخ نویسنده و ثبت‌کننده یکسان است.

این کار در ۲۷ اکتبر ۲۰۲۶ انجام شد؛ تاریخ‌های گیت طبق درخواست صریح مالک به گذشته تنظیم شده‌اند و زمان واقعی انجام کار نیستند. commitهای قبلی بازنویسی نشده‌اند. پایهٔ کار `b13b4ac` و شاخهٔ جدید `codex/hisabyar-august-improvements` است. این جدول شامل commit خود جدول هم هست؛ برای پرهیز از ارجاع دوری، hashها را در جدول نیاورده‌ایم.

برای بررسی تعداد، روزها، تطابق تاریخ نویسنده/ثبت‌کننده و غیرخالی‌بودن تمام commitها:

```sh
python scripts/verify_commit_schedule.py
```

برای دیدن hashها:

```sh
git log --reverse --format="%h %aI %cI %s" b13b4ac..codex/hisabyar-august-improvements
```

| شماره | تاریخ | ساعت کابل | تغییر |
|---|---|---|---|
| 1 | 2026-08-02 | 09:00 | chore: ignore local build outputs and signing secrets |
| 2 | 2026-08-02 | 12:00 | docs: record Persian audit and disclose requested commit dates |
| 3 | 2026-08-02 | 15:00 | fix: restore Android source and manifest directory layout |
| 4 | 2026-08-03 | 09:00 | build: configure app module with pinned Android plugin and Java 17 |
| 5 | 2026-08-03 | 12:00 | fix: capture a stable deletion index in row listeners |
| 6 | 2026-08-03 | 15:00 | fix: remove unchecked JSON formatting exception in backup dialog |
| 7 | 2026-08-04 | 09:00 | fix: introduce exact decimal money parsing and formatting |
| 8 | 2026-08-04 | 12:00 | test: cover exact sums and invalid financial amounts |
| 9 | 2026-08-04 | 15:00 | feat: accept Persian and Arabic numerals without ambiguous grouping |
| 10 | 2026-08-05 | 09:00 | feat: add validated immutable transaction model and unique identifiers |
| 11 | 2026-08-05 | 12:00 | feat: preserve legacy transaction IDs and encode decimal amounts as strings |
| 12 | 2026-08-05 | 15:00 | test: verify transaction validation and legacy JSON migration |
| 13 | 2026-08-06 | 09:00 | fix: validate transaction dates strictly and centralize local dates |
| 14 | 2026-08-06 | 12:00 | test: cover leap years and malformed calendar dates |
| 15 | 2026-08-06 | 15:00 | fix: calculate currency-separated totals and balances using decimal arithmetic |
| 16 | 2026-08-07 | 09:00 | test: cover month boundaries negative balances and isolated currencies |
| 17 | 2026-08-07 | 12:00 | feat: add composable transaction filters and stable date sorting |
| 18 | 2026-08-07 | 15:00 | test: verify combined filtering ordering and source list preservation |
| 19 | 2026-08-08 | 09:00 | feat: add versioned backup codec with strict whole-file validation |
| 20 | 2026-08-08 | 12:00 | test: cover backup round trips duplicate IDs and all-or-nothing decoding |
| 21 | 2026-08-08 | 15:00 | fix: only update in-memory ledger after durable validated storage succeeds |
| 22 | 2026-08-09 | 09:00 | fix: lock writes after corrupt storage instead of silently erasing records |
| 23 | 2026-08-09 | 12:00 | feat: save edit and delete transactions atomically by stable identifier |
| 24 | 2026-08-09 | 15:00 | refactor: connect transaction UI to validated ledger and exact reports |
| 25 | 2026-08-10 | 09:00 | feat: define localized application identity and primary action resources |
| 26 | 2026-08-10 | 12:00 | fix: make full entry form scrollable and size padding in density units |
| 27 | 2026-08-10 | 15:00 | fix: support right-to-left forms and keyboard resizing |
| 28 | 2026-08-11 | 09:00 | feat: show amount validation at the input without clearing the draft |
| 29 | 2026-08-11 | 12:00 | feat: choose a Gregorian transaction date for historical records |
| 30 | 2026-08-11 | 15:00 | fix: preserve selected transaction date during activity recreation |
| 31 | 2026-08-12 | 09:00 | feat: edit existing transactions and cancel edits without changing identifiers |
| 32 | 2026-08-12 | 12:00 | feat: identify the exact transaction in deletion confirmations |
| 33 | 2026-08-12 | 15:00 | feat: allow undoing the last deletion after a successful save |
| 34 | 2026-08-13 | 09:00 | feat: filter transaction list by AFN or USD |
| 35 | 2026-08-13 | 12:00 | feat: filter income and expense independently of currency |
| 36 | 2026-08-13 | 15:00 | feat: filter the ledger by today current month or all dates |
| 37 | 2026-08-14 | 09:00 | feat: search transaction descriptions sources categories amounts and dates |
| 38 | 2026-08-14 | 12:00 | fix: restore list filters and search after configuration changes |
| 39 | 2026-08-14 | 15:00 | perf: paginate ledger rows to avoid creating thousands of views at once |
| 40 | 2026-08-15 | 09:00 | feat: distinguish an empty ledger missing search results and unreadable storage |
| 41 | 2026-08-15 | 12:00 | feat: clarify monthly summary scope and highlight negative balances |
| 42 | 2026-08-15 | 15:00 | feat: report monthly expenses by category with separate currency totals |
| 43 | 2026-08-16 | 09:00 | feat: export versioned JSON backups through Android document picker |
| 44 | 2026-08-16 | 12:00 | feat: preview and validate bounded backup files off the UI thread |
| 45 | 2026-08-16 | 15:00 | feat: restore validated backups atomically after explicit replacement confirmation |
| 46 | 2026-08-17 | 09:00 | fix: allow raw recovery export and prevent empty backups after load failure |
| 47 | 2026-08-17 | 12:00 | feat: export UTF-8 CSV with quoted fields and spreadsheet formula protection |
| 48 | 2026-08-17 | 15:00 | fix: retain unsaved form fields and edit identity across activity recreation |
| 49 | 2026-08-18 | 09:00 | feat: label screen-reader controls and provide accessible touch targets |
| 50 | 2026-08-18 | 12:00 | fix: keep the last deleted transaction available after rotation |
| 51 | 2026-08-18 | 15:00 | feat: include income categories and preserve categories imported from backups |
| 52 | 2026-08-19 | 09:00 | fix: bound form fields and reject excessively long numeric input |
| 53 | 2026-08-19 | 12:00 | feat: confirm before discarding unsaved transaction edits |
| 54 | 2026-08-19 | 15:00 | test: verify storage failure corruption locking and durable transaction edits |
| 55 | 2026-08-20 | 09:00 | test: exercise monetary boundaries ambiguous separators and oversized inputs |
| 56 | 2026-08-20 | 12:00 | test: verify bounded UTF-8 file handling and stream closure on write failure |
| 57 | 2026-08-20 | 15:00 | test: cover category report isolation and CSV formula escaping |
| 58 | 2026-08-21 | 09:00 | test: add Android launch and draft-recreation instrumentation coverage |
| 59 | 2026-08-21 | 12:00 | build: add official Gradle 8.9 wrapper for reproducible local builds |
| 60 | 2026-08-21 | 15:00 | build: verify Gradle distribution with published SHA-256 checksum |
| 61 | 2026-08-22 | 09:00 | ci: run unit tests Android lint and both APK compilations with wrapper |
| 62 | 2026-08-22 | 12:00 | ci: retain unit test and lint reports for failed builds |
| 63 | 2026-08-22 | 15:00 | ci: validate wrapper and check improvement branches with bounded job duration |
| 64 | 2026-08-23 | 09:00 | docs: document reproducible builds dependencies and transparent commit dating |
| 65 | 2026-08-23 | 12:00 | docs: explain transaction editing filtering exports and safe backup restoration |
| 66 | 2026-08-23 | 15:00 | docs: state offline privacy backup exposure and current product limitations |
| 67 | 2026-08-24 | 09:00 | docs: provide device QA scenarios for data safety lifecycle and accessibility |
| 68 | 2026-08-24 | 12:00 | docs: specify backup schema migration limits and downgrade considerations |
| 69 | 2026-08-24 | 15:00 | fix: target Android 15 and account for system bars and keyboard insets |
| 70 | 2026-08-25 | 09:00 | test: assert restored draft content on the recreated Android activity |
| 71 | 2026-08-25 | 12:00 | fix: resolve migration API compatibility lifecycle and UTF-8 validation edge cases |
| 72 | 2026-08-25 | 15:00 | test: enforce backup capacity exponent bounds and non-destructive restore failures |
| 73 | 2026-08-26 | 09:00 | docs: summarize completed fixes and explicitly track remaining product limitations |
| 74 | 2026-08-26 | 12:00 | fix: address backup privacy and launcher lint findings and record verified build results |
| 75 | 2026-08-26 | 15:00 | docs: publish complete August commit schedule and reproducible history audit |
