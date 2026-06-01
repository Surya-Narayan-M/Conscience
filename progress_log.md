# ConscienceApp Progress Log

---

## Session 1 — 2026-06-01

### Summary
- Environment checks completed (CLI-only workflow). Kotlin updated to 1.9.24.
- Android project scaffold verified; Gradle build and install succeed on device.

### Phase 1: Core Behavior and Data Layer
- Created core data layer:
  - Constants, SessionEntity, SessionDao, AppDatabase (SQLCipher), AppPreferences (EncryptedSharedPreferences).
- Implemented Accessibility Service with Instagram open/close detection and entry intervention trigger.
- Fixed entry intervention loop by ignoring overlay activities and adding timing guard during the 20-second entry screen.
- Added SessionManager with escalation timer logic and logging.
- Implemented EntryInterventionActivity UI (full-screen, countdown, unskippable).

### Phase 2: Reality Check Overlays and Question Bank
- Implemented RealityCheckOverlayActivity UI with MCQ flow and punishment screen.
- Added OverlayEngine to launch entry and reality check overlays.
- Wired SessionManager overlay trigger to show reality check overlays during Instagram sessions.
- Expanded QuestionBank with full questions and quotes from question_quote_bank.md.
- Added getRandomQuote() in QuestionBank.

### Usage and Timing Improvements
- Added daily usage escalation thresholds and combined daily + session intervals.
- Adjusted daily thresholds to hit 1-minute interval after 90 minutes daily usage.
- Added UsageStatsManager-based daily usage (with fallback to local prefs) for system screen time.

### Stability and Trigger Fixes
- Added logging to trace overlay schedule and triggers.
- Debounced non-Instagram transitions and ignored IME/launcher windows to prevent false close events.
- Added guard to prevent overlays when Instagram is not active in foreground.

---

## Session 2 — 2026-06-01

> **Context**: Previous agent was stopped mid-session. This session audited the entire repo for breakage, completed all stubs, and fixed all wiring issues.

### Bugs Fixed (Broken/Stub Files)

| File | Problem | Fix |
|---|---|---|
| `ConscienceForegroundService.kt` | Empty stub — no `onStartCommand`, app would crash on service start | Full implementation: `START_STICKY`, notification channel, persistent foreground notification |
| `ConscienceApplication.kt` | Empty — nothing initialized | Now calls `DataSecurityManager.initialize()` + `NotificationScheduler.scheduleDailySummary()` on startup |
| `BootReceiver.kt` | Empty stub | Now starts foreground service + reschedules daily alarm after device reboot |
| `MainActivity.kt` | Placeholder "ConscienceApp" text | Full dashboard: today's stats (opens, time, questions, avoidant), permission warnings with action buttons, dark theme, settings nav |
| `OnboardingActivity.kt` | Placeholder "Onboarding" text | 4-page onboarding flow with progress dots, explains app purpose, mechanics, privacy, permissions |
| `SettingsActivity.kt` | Placeholder "Settings" text | Full settings: enable/disable toggle, frequency level (Low/Medium/High), daily limit slider (30–180 min), daily summary time picker (8–11 PM), permission shortcuts, data wipe with confirmation |
| `StatsEngine.kt` | Missing `getTodayStats()` + `formatTimeMs()` | Added both; `getTodayStats()` returns `Flow<DailyStats>` consumed by MainActivity |
| `AndroidManifest.xml` | `DailySummaryReceiver` not registered (alarms would silently fail); missing `SCHEDULE_EXACT_ALARM` permission | Both added |

### Architecture State (as of end of Session 2)

All 3 phases of the PRD are now implemented:

**Phase 1 — Core detection:** ✅ Complete
- `ConscienceAccessibilityService`, `SessionManager`, `EntryInterventionActivity`, data layer

**Phase 2 — Reality Checks:** ✅ Complete
- `RealityCheckOverlayActivity`, `QuestionBank` (76KB, 75 entry messages + 90 MCQ questions), `OverlayEngine`

**Phase 3 — Stats, Settings, Security:** ✅ Complete
- `StatsEngine`, `SettingsActivity`, `DataSecurityManager`, `NotificationScheduler`, `DailySummaryReceiver`

**App lifecycle:** ✅ Wired end-to-end
- First launch → Onboarding → MainActivity dashboard
- Boot → BootReceiver restarts foreground service + reschedules alarm
- Instagram open → Accessibility detects → EntryInterventionActivity (20s) → Instagram continues
- Every N minutes (escalating) → RealityCheckOverlayActivity (MCQ) → possible BonusPunishment (5s)
- 8–11 PM → DailySummaryReceiver fires → notification listing Q&A → clears answers → reschedules

### Pending / Not Yet Done

- **Monthly stats graph screen** — ✅ DONE in Session 3
- **Hilt DI wiring** — Hilt is declared as a dependency and plugin but `@HiltAndroidApp` / `@AndroidEntryPoint` annotations are not used anywhere. The app works without it (manual singleton pattern via `getInstance()`). Either remove Hilt from `build.gradle.kts` or fully adopt it.
- **DataStore migration** — `datastore-preferences` is listed as a dependency but `AppPreferences` uses `EncryptedSharedPreferences`. Either remove DataStore dependency or migrate to it.
- **`getRandomQuote()`** — Implemented in `QuestionBank` but never called in any UI. Could be added to `EntryInterventionActivity` or `RealityCheckOverlayActivity` as an additional motivational element.
- **`DAILY_ESCALATION_THRESHOLDS`** in `Constants.kt` — Defined but unused (SessionManager uses inline dynamic calculation). Consider removing.
- **Onboarding permission requests** — Onboarding explains that permissions are needed but does not programmatically prompt for them.
- **`SCHEDULE_EXACT_ALARM` on Android 12+** — Android 12+ may require the user to manually grant this.

---

## Session 3 — 2026-06-01

### Features Added

**1. Monthly Stats Chart (`StatsActivity`)**
- New screen: `app/.../ui/stats/StatsActivity.kt`
- Registered in `AndroidManifest.xml`
- Accessible from dashboard via red **📊 Stats** button
- **Minutes/day bar chart** (Canvas, horizontally scrollable):
  - Red bars = within daily limit, Orange = exceeded limit
  - Dashed grey line = user's set daily target
  - Yellow dot = today's column
  - Day labels on X-axis (1, 5, 10, 15, 20, 25, 30)
  - Time labels on Y-axis (0, mid, max)
- **Opens/day sub-chart** (blue bars, count label above each bar)
- **3 summary cards**: Total Time, Daily Avg (highlights red if > limit), Total Opens
- **Month navigator**: ‹ prev / next › arrows (can't go future)
- No third-party chart library — pure Compose Canvas

**2. Effective Daily Time Setting (20–120 min)**
- Slider range changed: `30–180 min` → `20 min to 2 hours (120 min)`
- 10 snap points (every 10 min)
- Smart display: `"45 min"` / `"1h 30m"` / `"2h"`
- Subtitle explains: *"Questions become more frequent as you approach this limit"*
- Default changed: `90 min` → `60 min`
- Updated `AppPreferences.effectiveDailyLimitMinutes` default
- Updated `SessionManager.getIntervalForDailyUsage()` coerceAtLeast: `30 min` → `20 min`

**3. Frequency Logic Rework**
Old logic had 3-hour base intervals that practically never fired independently.
New linear interpolation model:

| Daily Usage vs. Target | LOW | MEDIUM | HIGH |
|---|---|---|---|
| 0% used | 10 min | 7 min | 5 min |
| 50% used | ~5.5 min | ~4 min | ~3 min |
| 100%+ used | **1 min** | **1 min** | **1 min** |

Formula: `interval = baseInterval - (baseInterval - 1min) * (usage / limit)`

### Files Changed
- `app/.../ui/stats/StatsActivity.kt` ← **NEW**
- `app/.../ui/main/MainActivity.kt` — added Stats button + `onOpenStats` callback
- `app/.../ui/settings/SettingsActivity.kt` — slider 20–120, improved description
- `app/.../session/SessionManager.kt` — reworked `getIntervalForDailyUsage()`
- `app/.../data/prefs/AppPreferences.kt` — default 90→60 min
- `app/src/main/AndroidManifest.xml` — registered `StatsActivity`
- `progress_log.md` — this file
- **Hilt DI wiring** — Hilt is declared as a dependency and plugin but `@HiltAndroidApp` / `@AndroidEntryPoint` annotations are not used anywhere. The app works without it (manual singleton pattern via `getInstance()`). Either remove Hilt from `build.gradle.kts` or fully adopt it.
- **DataStore migration** — `datastore-preferences` is listed as a dependency but `AppPreferences` uses `EncryptedSharedPreferences`. Either remove DataStore dependency or migrate to it.
- **`getRandomQuote()`** — Implemented in `QuestionBank` but never called in any UI. Could be added to `EntryInterventionActivity` or `RealityCheckOverlayActivity` as an additional motivational element.
- **`DAILY_ESCALATION_THRESHOLDS`** in `Constants.kt` — Defined but unused (SessionManager uses inline dynamic calculation based on `prefs.effectiveDailyLimitMinutes` + `prefs.frequencyLevel`). Consider removing to avoid confusion.
- **Onboarding permission requests** — Onboarding explains that permissions are needed but does not programmatically prompt for them. After "Get Started", user lands on MainActivity which shows warning cards only if permissions are missing. Consider adding in-onboarding deep links to Settings for accessibility + overlay.
- **`SCHEDULE_EXACT_ALARM` on Android 12+** — Android 12+ may require the user to manually grant this. Consider adding a check + Settings redirect in `MainActivity` or `SettingsActivity`.

### Key Files Reference

```
app/src/main/java/com/conscience/app/
├── ConscienceApplication.kt          ← Initializes security + alarm on app start
├── data/db/
│   ├── AppDatabase.kt                ← SQLCipher encrypted Room DB
│   ├── SessionDao.kt                 ← DB queries (includes getSessionsBetween for monthly graph)
│   └── SessionEntity.kt
├── data/prefs/
│   └── AppPreferences.kt             ← EncryptedSharedPreferences; stores settings + daily answers JSON
├── notifications/
│   ├── DailySummaryReceiver.kt       ← Fires at user-set time; sends Q&A notification
│   └── NotificationScheduler.kt      ← Schedules setExactAndAllowWhileIdle alarm
├── overlay/
│   ├── EntryInterventionActivity.kt  ← 20s unskippable screen on Instagram open
│   ├── OverlayEngine.kt              ← Intent factory for both overlays
│   └── RealityCheckOverlayActivity.kt ← MCQ + BonusPunishment (5s)
├── questions/
│   ├── QuestionBank.kt               ← 75 entry messages, 90 MCQ, ~100 quotes; non-repeating session logic
│   └── models/
│       ├── AnswerResult.kt
│       └── Question.kt
├── security/
│   └── DataSecurityManager.kt        ← 30-day retention, internet-permission check, data wipe
├── service/
│   ├── BootReceiver.kt               ← Restarts service + alarm on reboot
│   ├── ConscienceAccessibilityService.kt ← Core: Instagram open/close detection, debounce, overlay trigger
│   └── ConscienceForegroundService.kt    ← Persistent foreground notification, START_STICKY
├── session/
│   └── SessionManager.kt             ← Singleton; escalation timer; DB writes; daily prefs
├── stats/
│   └── StatsEngine.kt                ← getTodayStats() Flow; getMonthlyUsage() Flow; formatTimeMs()
├── ui/
│   ├── main/MainActivity.kt          ← Dashboard: stats cards, permission warnings, settings button
│   ├── onboarding/OnboardingActivity.kt ← 4-page onboarding with progress dots
│   └── settings/SettingsActivity.kt  ← Full settings UI with all user options
└── utils/
    ├── Constants.kt
    ├── FrequencyLevel.kt             ← LOW / MEDIUM / HIGH enum
    └── UsageStatsHelper.kt           ← Queries UsageStatsManager for real Instagram screen time
```

### Notes for Next Agent

- The PRD (`prd_main.md`) is the authoritative spec. Read it fully before making changes.
- The `question_quote_bank.md` file contains the original raw question and quote data used to populate `QuestionBank.kt`.
- The `agent_verification_guide.md` file contains step-by-step device testing instructions.
- No INTERNET permission — intentional. All data stays on-device.
- Do NOT add cloud sync, analytics, or any network calls.
- Build command: `./gradlew assembleDebug` from project root.
- Install command: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
