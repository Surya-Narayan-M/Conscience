# AGENT VERIFICATION & SECURITY GUIDE
### For AI Agents Implementing the Conscience App PRD
### Read this BEFORE implementing any code from the PRD

---

> **CRITICAL**: This document defines how an AI agent MUST verify each implementation step before continuing. Skipping verification steps is the primary cause of cascading failures. Every checkpoint below is mandatory.

---

## PART 1 — AGENT OPERATING PRINCIPLES

### 1.1 The Prime Directive
> **Never proceed to the next phase until the current phase compiles cleanly and all verification checks pass.**

An AI agent implementing this project must treat each phase as a hard gate. A phase is not complete until:
1. The code compiles with zero errors
2. The specific verification commands listed for that phase pass
3. The expected outputs match exactly

### 1.2 When to STOP and Ask
The agent must stop and request clarification (do not guess) when:
- A file in the PRD references a class that hasn't been created yet
- A Gradle dependency version produces a conflict
- An Android permission behaves differently than the manifest specifies
- A device-specific behavior blocks testing (e.g., Xiaomi MIUI overlay block)
- Any output doesn't match the "expected output" listed in this guide

### 1.3 Anti-Hallucination Rules
The agent MUST NOT:
- Invent class names not in the PRD
- Auto-upgrade library versions without testing
- Assume a permission works because it's declared — always verify it's granted
- Skip the Accessibility Service test by simulating events programmatically
- Combine phases (implement Phase 2 files before Phase 1 compiles)

---

## PART 2 — PHASE VERIFICATION CHECKPOINTS

### CHECKPOINT 0 — Environment Ready

Before writing a single line of code, verify:

```bash
# 1. Android Studio installed
studio.sh --version   # or check via Android Studio menu

# 2. ADB works
adb version
# Expected: Android Debug Bridge version 1.0.41+

# 3. Device connected
adb devices
# Expected: at least one device listed as "device" (not "unauthorized")

# 4. Instagram installed on test device
adb shell pm list packages | grep instagram
# Expected: package:com.instagram.android

# 5. Kotlin version
kotlinc -version
# Expected: kotlinc-jvm 1.9.x
```

**GATE**: Do not create the Android project until all 5 commands above produce expected output.

---

### CHECKPOINT 1 — Project Created

After creating the Android Studio project:

```bash
# 1. Project structure exists
ls app/src/main/java/com/conscience/app/
# Expected: directory exists (may be empty initially)

# 2. Manifest exists
ls app/src/main/AndroidManifest.xml
# Expected: file exists

# 3. Clean build works on empty project
./gradlew assembleDebug
# Expected: BUILD SUCCESSFUL
# If FAILED: resolve Gradle version or SDK issues before continuing
```

**Verify in AndroidManifest.xml**:
- Package name is exactly `com.conscience.app`
- `android:allowBackup="false"` is set
- No `INTERNET` permission present

**GATE**: Do not write any Kotlin files until clean build succeeds.

---

### CHECKPOINT 2 — Constants & Data Layer (Phase 1, Step 1–4)

After implementing `Constants.kt`, `SessionEntity.kt`, `SessionDao.kt`, `AppDatabase.kt`, `AppPreferences.kt`:

```bash
./gradlew compileDebugKotlin
# Expected: BUILD SUCCESSFUL, 0 errors

# Verify SQLCipher dependency resolved
./gradlew dependencies | grep sqlcipher
# Expected: net.zetetic:android-database-sqlcipher:4.5.4
```

**Code Review Checklist** (agent self-check):
- [ ] `Constants.INSTAGRAM_PACKAGE` = `"com.instagram.android"` (exact string)
- [ ] `AppDatabase` uses `SupportFactory` for SQLCipher encryption
- [ ] `AppPreferences` uses `EncryptedSharedPreferences` (not plain `SharedPreferences`)
- [ ] `SessionDao.deleteOldSessions` exists (needed by security manager)
- [ ] `exportSchema = false` in `@Database` annotation

**GATE**: Do not implement the Accessibility Service until data layer compiles clean.

---

### CHECKPOINT 3 — Accessibility Service (Phase 1, Step 5)

After implementing `ConscienceAccessibilityService.kt`:

```bash
# 1. Compile check
./gradlew compileDebugKotlin
# Expected: BUILD SUCCESSFUL

# 2. Install on device
./gradlew installDebug

# 3. Verify service appears in accessibility settings
adb shell settings get secure enabled_accessibility_services
# Expected before enabling: does NOT contain com.conscience.app
# (User must enable it manually — verify UI guides them there)

# 4. Enable via settings on device, then verify:
adb shell settings get secure enabled_accessibility_services
# Expected: CONTAINS com.conscience.app/.service.ConscienceAccessibilityService
```

**Functional Test — Instagram Detection**:
```bash
# With Accessibility Service enabled:
# 1. Open Instagram on device
# 2. Watch logcat:
adb logcat -s "ConscienceAccessibilityService"
# Expected log output:
# D/ConscienceAccessibilityService: Instagram opened — firing intervention

# 3. Leave Instagram (go to home screen)
# Expected log output:
# D/ConscienceAccessibilityService: Instagram closed — session duration: Xms
```

**GATE**: The accessibility service MUST log both open and close events correctly before proceeding. If only open fires, check `onAccessibilityEvent` package name comparison logic.

---

### CHECKPOINT 4 — Session Manager (Phase 1, Step 6)

After implementing `SessionManager.kt`:

```bash
./gradlew compileDebugKotlin
# Expected: BUILD SUCCESSFUL
```

**Unit Test — Escalation Logic** (write this test):

```kotlin
// app/src/test/java/com/conscience/app/session/SessionManagerTest.kt
class SessionManagerTest {
    @Test
    fun `escalation returns correct intervals`() {
        // These are verified against PRD spec
        val cases = listOf(
            0L to 10 * 60 * 1000L,           // 0 min → 10 min interval
            5 * 60 * 1000L to 10 * 60 * 1000L, // 5 min → 10 min interval
            15 * 60 * 1000L to 7 * 60 * 1000L, // 15 min → 7 min interval
            25 * 60 * 1000L to 5 * 60 * 1000L, // 25 min → 5 min interval
            35 * 60 * 1000L to 3 * 60 * 1000L, // 35 min → 3 min interval
            50 * 60 * 1000L to 2 * 60 * 1000L, // 50 min → 2 min interval
            65 * 60 * 1000L to 1 * 60 * 1000L  // 65 min → 1 min interval
        )
        // NOTE: SessionManager.getIntervalForDuration is a pure function,
        // extract it to a standalone util to make it testable without Android context
        cases.forEach { (sessionMs, expectedInterval) ->
            val actual = getIntervalForDuration(sessionMs)
            assertEquals("Failed for ${sessionMs}ms", expectedInterval, actual)
        }
    }
}
```

```bash
./gradlew test
# Expected: All tests PASS
```

**GATE**: Escalation logic must pass unit tests. This is the core timer behavior.

---

### CHECKPOINT 5 — Entry Intervention Screen (Phase 1, Step 7–8)

After implementing `EntryInterventionActivity.kt`:

**Functional Test**:
1. Install app on device
2. Grant all 3 permissions
3. Open Instagram
4. **Expected behavior**:
   - Fullscreen black screen appears within 1 second
   - 20-second countdown is visible and counting down
   - Back button does NOTHING (press it 5 times — screen must not dismiss)
   - After 20 seconds, screen disappears and Instagram is visible

```bash
# Verify back button is truly blocked by checking activity stack
adb shell dumpsys activity activities | grep -E "EntryIntervention|instagram"
# During intervention, EntryInterventionActivity should be on top of Instagram
```

**GATE**: The intervention MUST be unskippable. If back button dismisses it, the `onBackPressed` override is not working — investigate activity theme or Android 13+ back gesture handling.

---

### CHECKPOINT 6 — Reality Check Overlay (Phase 2)

After implementing `RealityCheckOverlayActivity.kt` and `QuestionBank.kt`:

**Functional Test**:
1. Open Instagram
2. Wait for first overlay (10 min in production; for testing, temporarily set interval to 10 seconds in Constants)
3. **Expected behavior**:
   - MCQ overlay appears over Instagram
   - Instagram touches are blocked (test by trying to scroll — it must not respond)
   - Back button blocked
   - All 4 answer options are tappable
   - Submit button disabled until an option is selected
   - Submit triggers either: close overlay (return to Instagram) OR punishment screen

**Test Avoidant Answer Path**:
- Select "Pure habit — I didn't even decide to" in goal_1 question
- Expected: 5-second red punishment screen appears
- Expected: After 5 seconds, returns to Instagram automatically

**GATE**: Both the normal path and punishment path must work before enabling production intervals.

---

### CHECKPOINT 7 — Security Verification

After implementing `DataSecurityManager.kt`:

#### 7.1 Verify No Internet Permission
```bash
adb shell dumpsys package com.conscience.app | grep INTERNET
# Expected: NO OUTPUT (no internet permission)
```

#### 7.2 Verify Database is Encrypted
```bash
# Pull the database from device
adb shell run-as com.conscience.app cp /data/data/com.conscience.app/databases/conscience_db /sdcard/
adb pull /sdcard/conscience_db /tmp/conscience_db

# Try to read it as plain SQLite
sqlite3 /tmp/conscience_db .tables
# Expected: "file is not a database" or gibberish
# This confirms SQLCipher encryption is working
```

#### 7.3 Verify SharedPreferences are Encrypted
```bash
adb shell run-as com.conscience.app cat /data/data/com.conscience.app/shared_prefs/conscience_prefs.xml
# Expected: Encrypted/unreadable content (not plain key-value pairs)
# You should see Base64-encoded blobs, NOT readable strings like "app_enabled=true"
```

#### 7.4 Verify Backup is Disabled
```bash
adb backup -apk -noshared com.conscience.app
# Expected: Backup should be empty or refused
# The data_extraction_rules.xml should prevent any data from being backed up
```

#### 7.5 Verify Data Retention (30-day pruning)
```kotlin
// In a test or debug screen, manually call:
DataSecurityManager.initialize(applicationContext)
// Then check database:
// Sessions older than 30 days should be deleted
// Verify via:
adb shell run-as com.conscience.app sqlite3 /data/data/com.conscience.app/databases/conscience_db \
  "SELECT COUNT(*) FROM sessions WHERE startTimeMs < $(date -d '31 days ago' +%s)000;"
// Expected: 0
```

**GATE**: ALL 5 security checks must pass. Do not ship any version of the app without passing 7.1 and 7.2 at minimum.

---

## PART 3 — DATA SECURITY MEASURES

### 3.1 What Data is Collected
The app collects ONLY:
- Session start/end timestamps (when Instagram was opened/closed)
- Session duration in milliseconds
- Count of questions answered per session
- Count of "avoidant" answers per session
- Daily open count and daily total time

### 3.2 What Data is NEVER Collected
- Instagram content (no screen reading, no content capture)
- User identity or account information
- Location data
- Contact information
- Any other app's data
- Screenshots or recordings

### 3.3 Data Storage Security
| Layer | Security Measure | Implementation |
|---|---|---|
| Database | SQLCipher AES-256 encryption | `SupportFactory` in `AppDatabase.kt` |
| Preferences | AES-256-GCM encryption | `EncryptedSharedPreferences` |
| Backup | Fully disabled | `data_extraction_rules.xml` |
| Network | No permission granted | No `INTERNET` in manifest |
| Retention | Auto-delete after 30 days | `DataSecurityManager.enforceDataRetention()` |

### 3.4 No Data Leaves the Device
The manifest deliberately omits `INTERNET` permission. This is architecturally enforced — the app cannot make network requests. Verified at runtime by `DataSecurityManager.verifyNoNetworkPermission()`.

### 3.5 Data Wipe on Demand
Users can delete all data via Settings → Wipe All Data, which calls:
```kotlin
DataSecurityManager.wipeAllData(context)
// Clears Room database + SharedPreferences
```

### 3.6 Accessibility Service Data Scope
The `ConscienceAccessibilityService` is configured with:
```xml
android:canRetrieveWindowContent="false"
android:packageNames="com.instagram.android"
```
This means:
- It can only receive events from Instagram (scoped by packageNames)
- It CANNOT read screen content (canRetrieveWindowContent=false)
- It only detects app open/close, not what the user is viewing

### 3.7 Threat Model

| Threat | Mitigation |
|---|---|
| Physical device access (attacker reads data) | SQLCipher encryption makes DB unreadable |
| Cloud backup exfiltration | Backup fully disabled |
| App cloning / data extraction | EncryptedSharedPreferences |
| Malicious app reads our data | Android sandbox — other apps cannot read our data |
| Developer accidentally adding network code | No INTERNET permission = network calls fail at OS level |

---

## PART 4 — COMMON FAILURE PATTERNS & FIXES

### Failure: Overlay doesn't appear over Instagram
**Cause**: `SYSTEM_ALERT_WINDOW` not granted or Android 12+ bubble behavior
**Fix**:
1. Verify permission: `adb shell appops get com.conscience.app SYSTEM_ALERT_WINDOW`
2. Check activity theme uses `Theme.Conscience.Overlay` (no ActionBar, fullscreen)
3. On Android 12+, add `android:showOnLockScreen="true"` to activity in manifest

### Failure: Accessibility Service stops working after phone restart
**Cause**: Battery optimization kills the service
**Fix**: Guide user to disable battery optimization for the app:
```
Settings → Apps → Conscience → Battery → Unrestricted
```

### Failure: Back gesture dismisses Entry Intervention on Android 13+
**Cause**: Android 13 introduced predictive back gestures which can bypass `onBackPressed`
**Fix**: Override the `OnBackPressedCallback` using the new API:
```kotlin
onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        // Do nothing — intervention is unskippable
    }
})
```

### Failure: SQLCipher crash on first install
**Cause**: SQLCipher native libraries not loaded
**Fix**: Add to `ConscienceApplication.onCreate()`:
```kotlin
System.loadLibrary("sqlcipher")
```

### Failure: `EncryptedSharedPreferences` crash on Android 9 (API 28)
**Cause**: Keystore issues on older Android versions
**Fix**: Wrap in try-catch and fall back to regular SharedPreferences for API < 23:
```kotlin
try {
    EncryptedSharedPreferences.create(...)
} catch (e: Exception) {
    context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
}
```

### Failure: Overlay appears but Instagram is still interactive underneath
**Cause**: `FLAG_NOT_TOUCHABLE` not set, or window type incorrect
**Fix**: The Activity-based overlay approach in the PRD handles this via activity task affinity. If Instagram remains touchable, verify `android:taskAffinity` is set on both overlay activities in the manifest.

---

## PART 5 — FINAL PRE-SHIP CHECKLIST

Before considering any version complete:

```
Security:
  ✅ No INTERNET permission in manifest
  ✅ Database confirmed encrypted (Checkpoint 7.2)
  ✅ SharedPreferences confirmed encrypted (Checkpoint 7.3)
  ✅ Backup disabled and verified (Checkpoint 7.4)
  ✅ 30-day data retention enforced

Functionality:
  ✅ Accessibility Service detects Instagram open and close (Checkpoint 3)
  ✅ Escalation timer intervals match PRD spec (Checkpoint 4)
  ✅ Entry intervention is unskippable for exactly 20 seconds (Checkpoint 5)
  ✅ MCQ overlay blocks Instagram interaction (Checkpoint 6)
  ✅ Avoidant answers trigger punishment screen (Checkpoint 6)
  ✅ App survives device restart (BootReceiver test)
  ✅ App works after battery optimization disabled

Permissions:
  ✅ Accessibility Service: user granted and verified
  ✅ Draw Over Apps: user granted and verified
  ✅ Usage Stats: user granted and verified
  ✅ App handles permission-not-granted state gracefully (shows warning in MainActivity)

Code Quality:
  ✅ No hardcoded strings (all in Constants.kt or strings.xml)
  ✅ No Log.d() calls containing session data in release build
  ✅ All coroutine scopes have proper lifecycle management (no leaks)
  ✅ All Activities handle onDestroy properly
```

---

*END OF AGENT VERIFICATION & SECURITY GUIDE*
