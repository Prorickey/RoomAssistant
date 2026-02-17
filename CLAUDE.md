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
- Landscape-locked tablet dashboard with nighttime Tokyo lofi aesthetic
- MVVM pattern with ViewModels: GoveeViewModel, WeatherViewModel, ScheduleViewModel
- Network: Retrofit + OkHttp for Govee API, OpenWeatherMap, sunrise-sunset.org
- Image loading: Coil
- API keys via BuildConfig from `local.properties` (`govee.api.key`, `openweather.api.key`)

## Key Config
- Min SDK: 23
- Target/Compile SDK: 36
- Java compatibility: 11
- Gradle: 8.13
- Dependency versions managed via `gradle/libs.versions.toml`

## Source Layout
- App source: `app/src/main/java/tech/bedson/roomassistant/`
- Theme files: `ui/theme/` — Color.kt (lofi palette), Theme.kt (dark-only), Type.kt
- UI components: `ui/components/` — MarqueeHeader, PhotoCollage, WeatherWidget, GoveeControlPanel, NcssmTimePanel, WeatherBackground
- Screens: `ui/screens/DashboardScreen.kt` — Main 3-column landscape layout
- ViewModels: `viewmodel/` — GoveeViewModel, WeatherViewModel, ScheduleViewModel
- Network: `data/api/` — GoveeApi, WeatherApi, SunriseSunsetApi, RetrofitClient
- Models: `data/model/` — WeatherResponse, GoveeDevice, GoveeCommand, ScheduleModels, SunriseSunsetResponse
- Data: `data/ScheduleRepository.kt` — Schedule JSON loader
- Assets: `app/src/main/assets/schedule_normal.json` — NCSSM Durham schedule
- Build config: `app/build.gradle.kts`
