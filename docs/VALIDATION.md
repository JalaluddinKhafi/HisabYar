# نتیجهٔ اعتبارسنجی

تاریخ اجرای واقعی: ۲۷ اکتبر ۲۰۲۶. شاخه: `codex/hisabyar-august-improvements`.

## بررسی‌های انجام‌شده

| بررسی | نتیجه |
|---|---|
| Gradle Wrapper | نسخهٔ 8.9 اجرا شد؛ ZIP با SHA-256 رسمی بررسی شد |
| `testDebugUnitTest` | 38 آزمون، صفر شکست و صفر خطا |
| `lintDebug` | صفر خطا؛ ۴ هشدار `SetTextI18n` |
| `assembleDebug` | موفق؛ APK نسخهٔ 1.1 / کد نسخهٔ 2 |
| `assembleDebugAndroidTest` | موفق؛ کد آزمون بازسازی Activity کامپایل و بسته‌بندی شد |
| امضای APK | `apksigner verify` موفق، امضاهای v1 و v2 معتبر؛ امضای debug |
| Manifest بستهٔ نهایی | package=`com.hisabyar.app`، min SDK=23، target SDK=35؛ بدون مجوز درخواستی |
| `git diff --check` | بدون خطای فاصله و patch |
| دستگاه متصل | خروجی `adb devices -l` خالی؛ آزمون دستگاه اجرا نشد |

آخرین ساخت کامل با پیام `BUILD SUCCESSFUL in 1m 31s` پایان یافت. فرمان اجراشده:

```powershell
.\gradlew.bat --no-daemon testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest --console=plain
```

محیط: Windows 11، JDK 17.0.13، Android Gradle Plugin 8.7.3، SDK Platform 35 و Build Tools 34.0.0. `JAVA_HOME` و `ANDROID_HOME` برای اجرای محلی تنظیم شدند و مسیرهای دستگاه به مخزن اضافه نشدند.

## پوشش آزمون‌ها

دقت اعشار و حدود مبلغ؛ ارقام فارسی و عربی؛ تاریخ کبیسه و تاریخ نامعتبر؛ جمع ماهانه، ماندهٔ منفی و جداسازی ارزها؛ فیلتر ترکیبی و ترتیب پایدار؛ مهاجرت شناسه و مبلغ قدیمی؛ پشتیبان نسخه‌دار و آرایهٔ قدیمی؛ شناسهٔ تکراری؛ محدودیت تعداد، حجم UTF-8 و طول متن؛ توان عددی بسیار بزرگ؛ بستن stream در خطا؛ خطای ذخیره و قفل دادهٔ خراب؛ بازیابی نامعتبر بدون تغییر اطلاعات؛ گزارش دسته و محافظت CSV در برابر اجرای فرمول.

هشدارهای باقی‌ماندهٔ Lint مربوط به ساخت متن‌های پویا با الحاق رشته‌اند. تبدیل کامل این متن‌ها به resourceهای دارای placeholder برای ترجمه‌های بعدی باقی است. در بررسی نهایی، هشدارهای تنظیم استخراج داده، نبود آیکون و resourceهای بلااستفاده اصلاح شدند.

## فایل‌های خروجی

- برنامه: `app/build/outputs/apk/debug/app-debug.apk` — 36460 بایت.
- آزمون: `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk`.
- گزارش آزمون: `app/build/reports/tests/testDebugUnitTest/index.html`.
- گزارش Lint: `app/build/reports/lint-results-debug.html`.

SHA-256 فایل APK برنامه:

```text
b086cffa0abf5b41f06bd3cbcb3a52673c05bc688c9ffca141f43e6e274de822
```

## حدود نتیجه

اجرای آزمون رابط کاربری و مراحل دستی روی گوشی/شبیه‌ساز انجام نشده است. [فهرست QA](QA_FA.md) برای این مرحله آماده است. ساخت CI روی GitHub اجرا نشده؛ تنظیم گردش‌کار فقط در مخزن اصلاح شده است. امضای انتشار، انتشار در فروشگاه و ارسال تغییرات به GitHub انجام نشده است. نزدیک سقف داده، کارایی ذخیرهٔ هم‌زمان باید روی دستگاه هدف بررسی شود.

تنظیم نسخه‌های ابزار بر اساس [سازگاری رسمی AGP 8.7](https://developer.android.com/build/releases/agp-8-7-0-release-notes) و تنظیم استخراج داده بر اساس [مستندات Android](https://developer.android.com/identity/data/autobackup) بررسی شد.
