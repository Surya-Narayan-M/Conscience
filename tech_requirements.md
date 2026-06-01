# CONSCIENCE APP — TECH & HARDWARE REQUIREMENTS
### Environment Setup Guide for Developers & AI Agents

---

## 1. DEVELOPMENT MACHINE REQUIREMENTS

### Minimum Hardware
| Component | Minimum | Recommended |
|---|---|---|
| CPU | 4-core, 2.5 GHz | 8-core, 3.0 GHz+ |
| RAM | 8 GB | 16 GB (Android Studio is memory-heavy) |
| Storage | 20 GB free | 50 GB free SSD |
| Display | 1080p | 1440p+ (for emulator + code side-by-side) |

### Operating System
| OS | Support | Notes |
|---|---|---|
| Windows 10/11 (64-bit) | ✅ Full | Recommended for most users |
| macOS 12+ (Intel or Apple Silicon) | ✅ Full | M1/M2 requires Rosetta for some tools |
| Ubuntu 20.04+ / Linux | ✅ Full | Additional udev rules needed for USB debugging |

---

## 2. SOFTWARE REQUIREMENTS

### Core Development Tools

| Tool | Version | Download | Purpose |
|---|---|---|---|
| Android Studio | Hedgehog 2023.1.1+ | developer.android.com/studio | Primary IDE |
| JDK (bundled) | 17 | Bundled with Android Studio | Java compiler |
| Kotlin | 1.9.0+ | Bundled with Android Studio | Language |
| Android SDK | API 26–34 | Via Android Studio SDK Manager | Build target |
| Gradle | 8.2+ | Bundled with Android Studio | Build system |
| ADB (Android Debug Bridge) | Latest | Bundled with Android Studio | Device communication |

### Android Studio Setup Checklist
```
Android Studio → SDK Manager → Install:
  ✅ Android SDK Platform 34 (Android 14)
  ✅ Android SDK Platform 26 (Android 8 — minimum)
  ✅ Android SDK Build-Tools 34.0.0
  ✅ Android Emulator
  ✅ Android SDK Platform-Tools
  ✅ Google USB Driver (Windows only)

Android Studio → Plugins → Install:
  ✅ Kotlin (bundled — verify enabled)
  ✅ Jetpack Compose (bundled)
```

### Build Environment Variables (Windows)
```batch
ANDROID_HOME = C:\Users\<user>\AppData\Local\Android\Sdk
PATH += %ANDROID_HOME%\platform-tools
PATH += %ANDROID_HOME%\tools
```

### Build Environment Variables (macOS/Linux)
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk          # macOS
export ANDROID_HOME=$HOME/Android/Sdk                  # Linux
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
```

---

## 3. TEST DEVICE REQUIREMENTS

### Physical Android Device (Strongly Recommended over Emulator)
> **WHY**: Accessibility Services and Draw Over Apps overlays behave differently on emulators.
> Always test on a real device for this app.

| Requirement | Minimum | Notes |
|---|---|---|
| Android Version | 8.0 (API 26) | The app targets this as minimum |
| Android Version (ideal) | 10.0+ (API 29+) | Better overlay behavior |
| RAM | 2 GB | 4 GB+ recommended |
| Instagram installed | Required | `com.instagram.android` must be present for testing |
| Developer Mode | Must enable | See instructions below |
| USB Debugging | Must enable | For ADB connection |

### Enabling Developer Mode on Android Device
```
Settings → About Phone → Tap "Build Number" 7 times
→ "Developer Options" now appears in Settings
→ Settings → Developer Options → USB Debugging: ON
→ Settings → Developer Options → Install via USB: ON
```

### Verifying ADB Connection
```bash
# Connect device via USB, then:
adb devices
# Expected output:
# List of devices attached
# XXXXXXX    device
```

---

## 4. REQUIRED DEPENDENCIES (Gradle)

All dependencies are defined in `build.gradle` but listed here for clarity:

### Core Libraries
| Library | Version | Purpose |
|---|---|---|
| `androidx.compose:compose-bom` | 2024.02.00 | Jetpack Compose UI framework |
| `androidx.compose.material3:material3` | BOM | Material Design 3 components |
| `androidx.activity:activity-compose` | 1.8.2 | Compose integration with Activity |

### Database
| Library | Version | Purpose |
|---|---|---|
| `androidx.room:room-runtime` | 2.6.1 | Local SQLite database ORM |
| `androidx.room:room-ktx` | 2.6.1 | Kotlin coroutines support for Room |
| `net.zetetic:android-database-sqlcipher` | 4.5.4 | Database encryption |
| `androidx.sqlite:sqlite-ktx` | 2.4.0 | SQLite support library |

### Security
| Library | Version | Purpose |
|---|---|---|
| `androidx.security:security-crypto` | 1.1.0-alpha06 | EncryptedSharedPreferences |

### Background Processing
| Library | Version | Purpose |
|---|---|---|
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.7.3 | Async/background operations |
| `androidx.datastore:datastore-preferences` | 1.0.0 | Modern preferences storage |

### Dependency Injection
| Library | Version | Purpose |
|---|---|---|
| `com.google.dagger:hilt-android` | 2.50 | Dependency injection framework |
| `ksp` processor for hilt | 2.50 | Compile-time DI code generation |

### Architecture
| Library | Version | Purpose |
|---|---|---|
| `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.7.0 | ViewModel integration |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.7.0 | Lifecycle-aware coroutines |

---

## 5. ANDROID PERMISSIONS — SETUP GUIDE

This app requires 3 special permissions that users must manually grant:

### Permission 1: Accessibility Service
- **Why**: Only way to detect which app is in foreground without root
- **How user grants it**: Settings → Accessibility → Conscience → Enable
- **API used**: `android.accessibilityservice.AccessibilityService`
- **Test command**:
```bash
adb shell settings get secure enabled_accessibility_services
# Should contain: com.conscience.app/.service.ConscienceAccessibilityService
```

### Permission 2: Draw Over Other Apps (SYSTEM_ALERT_WINDOW)
- **Why**: Required to show overlay on top of Instagram
- **How user grants it**: Settings → Apps → Conscience → Display over other apps → Allow
- **API used**: `Settings.canDrawOverlays(context)`
- **Test command**:
```bash
adb shell appops get com.conscience.app SYSTEM_ALERT_WINDOW
# Should output: allow
```

### Permission 3: Usage Stats Access (PACKAGE_USAGE_STATS)
- **Why**: Track total time spent in Instagram across sessions
- **How user grants it**: Settings → Apps → Special app access → Usage access → Conscience
- **API used**: `UsageStatsManager`
- **Test command**:
```bash
adb shell appops get com.conscience.app GET_USAGE_STATS
# Should output: allow
```

### Grant All Permissions via ADB (Testing Only)
```bash
# Accessibility cannot be granted via ADB — must be done manually in Settings
# But overlay and usage stats can be:
adb shell appops set com.conscience.app SYSTEM_ALERT_WINDOW allow
adb shell appops set com.conscience.app GET_USAGE_STATS allow
```

---

## 6. BUILD & INSTALL COMMANDS

### Build Debug APK
```bash
# From project root:
./gradlew assembleDebug

# APK location:
app/build/outputs/apk/debug/app-debug.apk
```

### Install on Device
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Build and Install in One Command
```bash
./gradlew installDebug
```

### Run Tests
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests (requires device)
```

### View Logs (Filter for App Only)
```bash
adb logcat -s "ConscienceApp" "SessionManager" "AccessibilityService"
```

### Simulate Instagram Open (Testing)
```bash
adb shell am start -n com.instagram.android/.activity.MainTabActivity
```

---

## 7. EMULATOR SETUP (Fallback Only)

> Use physical device when possible. For emulator:

```
Android Studio → Device Manager → Create Virtual Device
→ Choose: Pixel 6 (or any phone with Play Store)
→ System Image: API 33 (Android 13) — x86_64
→ AVD Name: Conscience_Test
→ RAM: 4096 MB
→ Internal Storage: 8 GB
```

### Install Instagram on Emulator
```bash
# Emulator must have Google Play Store
# Open Play Store in emulator and install Instagram manually
# OR sideload APK:
adb -e install instagram.apk
```

---

## 8. PROJECT STRUCTURE VERIFICATION

After cloning/creating the project, verify this structure exists:

```
ConscienceApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/conscience/app/
│   │   │   ├── data/db/
│   │   │   ├── data/prefs/
│   │   │   ├── data/repository/
│   │   │   ├── service/
│   │   │   ├── overlay/
│   │   │   ├── questions/models/
│   │   │   ├── session/
│   │   │   ├── stats/
│   │   │   ├── ui/main/
│   │   │   ├── ui/onboarding/
│   │   │   ├── ui/settings/
│   │   │   ├── security/
│   │   │   └── utils/
│   │   ├── res/
│   │   │   ├── xml/
│   │   │   │   ├── accessibility_service_config.xml  ← CRITICAL
│   │   │   │   └── data_extraction_rules.xml
│   │   │   └── values/strings.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 9. KNOWN COMPATIBILITY ISSUES

| Issue | Affected Devices | Solution |
|---|---|---|
| Overlay blocked by MIUI (Xiaomi) | Xiaomi phones | Requires extra permission in MIUI Security app |
| Overlay blocked by One UI (Samsung) | Samsung phones API 29+ | User must also enable "Appear on top" in Samsung Settings |
| Accessibility service killed by battery optimization | Most Android phones | User must disable battery optimization for this app |
| Instagram update changes package name | N/A (very unlikely) | Update `Constants.INSTAGRAM_PACKAGE` if needed |

### Disable Battery Optimization (Required for reliable operation)
```
Settings → Apps → Conscience → Battery → Unrestricted
OR via ADB:
adb shell dumpsys deviceidle whitelist +com.conscience.app
```

---

*END OF TECH REQUIREMENTS DOCUMENT*
