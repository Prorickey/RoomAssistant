# RoomAssistant

## Build Commands
- `./gradlew assembleDebug` — Build debug APK
- `./gradlew test` — Run unit tests
- `./gradlew connectedAndroidTest` — Run instrumented tests (requires device/emulator)
- `./gradlew installDebug` — Build and install debug APK on connected device

## Architecture
- Single-module Jetpack Compose app with Material 3
- Language: Kotlin
- Namespace: `tech.bedson.roomassistant`

## Key Config
- Min SDK: 23
- Target/Compile SDK: 36
- Java compatibility: 11
- Gradle: 8.13
- Dependency versions managed via `gradle/libs.versions.toml`

## Source Layout
- App source: `app/src/main/java/tech/bedson/roomassistant/`
- Theme files: `app/src/main/java/tech/bedson/roomassistant/ui/theme/`
- Build config: `app/build.gradle.kts`
