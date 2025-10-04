# diagnostics-android

Azure Portal Extensions Dashboard implemented in Kotlin for Android.

## Prerequisites

- **JDK 17 or higher** (required by AGP 8.13.0)
- **Android Studio Narwhal or newer** (recommended)
- **Android SDK** (API level as specified in the project)
- **Gradle** (wrapper included, no manual install needed)

## Building the App

You can build the app using Android Studio or the command line:

### Using Android Studio

1. Open the project in Android Studio.
2. Let Gradle sync and download dependencies.
3. Click the **Run** button or use **Build > Make Project**.

### Using Command Line

1. Open a terminal in the project root.
2. Run:

```cmd
.\gradlew assembleDebug
```

The APK will be generated in `app\build\outputs\apk\debug`.

## Running Tests

To run unit and instrumentation tests:

- **Unit tests:**

```cmd
.\gradlew test
```

- **Instrumentation tests:**

```cmd
.\gradlew connectedAndroidTest
```

## Third-Party Libraries

- **Material Components** (`com.google.android.material:material`): UI components following Material
  Design.
- **OkHttp** (`com.squareup.okhttp3:okhttp`): HTTP client for network requests.
- **Retrofit** (`com.squareup.retrofit2:retrofit`): Type-safe HTTP client for Android and Java.
- **Kotlinx Serialization** (`org.jetbrains.kotlinx:kotlinx-serialization-json`): JSON serialization
  for Kotlin.

See `gradle/libs.versions.toml` for the full list of dependencies and versions.

## Instrumentation devices: how they are chosen and how to run on multiple

- What the project is configured to do
    - The app sets `testInstrumentationRunner = androidx.test.runner.AndroidJUnitRunner` and does
      not define any Gradle Managed Devices. There is no custom device matrix in Gradle. The minimum
      SDK is `minSdk = 35`.
- What this means in practice
    - Running from Android Studio (green Run/Debug button on an androidTest class): Studio runs the
      tests on the single device you select in the device drop‑down (e.g., "Pixel 8a"). If multiple
      emulators are running, Studio still uses the one currently selected in the toolbar or the
      device set in the Run Configuration.
    - Running from the command line with `connectedAndroidTest`: Gradle will attempt to run the
      tests on all connected, running devices (labeled "Running" in Android Studio) that are
      ELIGIBLE. A device is eligible only if it meets the app constraints (e.g., API level >=
      `minSdk`, correct ABI/system image). Devices below API 35 are skipped because this app’s
      `minSdk` is 35.
- How the device is chosen
    - Android Studio: the device selected in the device chooser (top toolbar) or specified in the
      Run Configuration.
    - Gradle `connectedAndroidTest`: all eligible connected devices. If only one is eligible, it
      will run only there.
- How to run on multiple devices
    - Start multiple eligible emulators (API 35+ for this project), then run:

       ```cmd
       .\gradlew connectedAndroidTest
       ```

      Gradle will execute the tests on each eligible, running device.

Tips

- Ensure all devices are "Running" and unlocked (booted and visible via `adb devices`).
- Match the emulator’s API level (35+) to the project’s `minSdk` to make them eligible.

### What "Available" vs "Running" means

- Available (in Device Manager): The AVD exists but is not currently running/booted. It will not be
  used by Gradle’s `connectedAndroidTest`.
- Running: The emulator/device is booted and visible in `adb devices` with the state `device` (
  Android Studio shows this status as "Running"). Gradle will run tests on these (if eligible by API
  level/ABI).

How to bring other devices to Running:

1) In Android Studio: open Device Manager and click the Play ▶ button to start each AVD you want to
   use.
2) From command line (examples):

```cmd
adb devices
emulator -list-avds
emulator -avd <yourAvdName>
```

Once the additional devices show up in `adb devices` as `device` (not `offline`/`unauthorized`),
re-run:

```cmd
.\gradlew connectedAndroidTest
```

Gradle will execute the instrumentation tests on all eligible, running devices.

