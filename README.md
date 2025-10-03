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

- **Material Components** (`com.google.android.material:material`): UI components following Material Design.
- **OkHttp** (`com.squareup.okhttp3:okhttp`): HTTP client for network requests.
- **Retrofit** (`com.squareup.retrofit2:retrofit`): Type-safe HTTP client for Android and Java.
- **Kotlinx Serialization** (`org.jetbrains.kotlinx:kotlinx-serialization-json`): JSON serialization for Kotlin.

See `gradle/libs.versions.toml` for the full list of dependencies and versions.
