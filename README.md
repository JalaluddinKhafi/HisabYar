# HisabYar — حساب‌یار

Offline Android income and expense ledger for AFN and USD. Java 17, Android API 23+, compile/target API 35. No Internet or storage permission is required.

## Build and verify

Install JDK 17 and Android SDK Platform 35 / Build Tools 34.0.0. Set `JAVA_HOME` and `ANDROID_HOME`, or set `sdk.dir` in an untracked `local.properties`.

Windows:
```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

Linux/macOS:
```sh
./gradlew testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

Gradle 8.9 is downloaded and checksum-verified by the wrapper. Dependencies require Internet access during the first build. The app itself remains offline.

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`. This is a debug build, not a signed store release. On a connected test device, use `./gradlew connectedDebugAndroidTest` for instrumentation tests.

See [Persian guide](README_FA.md), [audit](docs/REVIEW_FA.md), [manual QA](docs/QA_FA.md), and [validation](docs/VALIDATION.md).

## Commit dates

Changes were made on 27 October 2026. At the owner's explicit request, author and committer dates for the 75 improvement commits were assigned to 2–26 August 2026, three commits per day. Existing history was retained; timestamps do not represent actual work dates.
