# RoomAssistant

A landscape tablet dashboard for the Fourth West IVIZ dorm room at NCSSM Durham. Built with Jetpack Compose and styled with a nighttime Tokyo lofi aesthetic.

## Features

- **Scrolling marquee header** — Animated welcome banner cycling through messages
- **Photo collage** — Crossfading slideshow with neon-bordered frames (replace placeholders with your own photos)
- **Weather widget** — Live weather for NCSSM Durham via OpenWeatherMap, with temperature, conditions, and icon
- **Weather-reactive background** — Subtle animated rain streaks during rain, lightning flashes during thunderstorms
- **Govee LED control** — Power toggle and 6 color presets (Lofi Purple, Sunset, Ocean, Forest, Warm Reading, Party) via the Govee Developer API
- **NCSSMTime panel** — Countdown timer showing time remaining in the current schedule event, plus a vertical timeline with sunrise, meals, sunset, and check-in times
- **Immersive fullscreen** — Landscape-locked, system bars hidden, screen always on

## Dashboard Layout

```
+------------------------------------------------------------+
|  << Scrolling Marquee Header >>                            |
+----------------+---------------------+---------------------+
|                |                     |                     |
|  Photo         |  Govee LED          |  NCSSMTime Panel    |
|  Collage       |  Controls           |                     |
|                |                     |  [Countdown Timer]  |
|                |  [Power Toggle]     |  MM:SS              |
|----------------|                     |  "Left in A1 Class" |
|                |  [6 Preset          |                     |
|  Weather       |   Buttons in        |  [Vertical Timeline]|
|  Widget        |   2x3 grid]         |  Sunrise  Breakfast |
|                |                     |  Lunch    Dinner    |
|                |                     |  Sunset   Check-in  |
+----------------+---------------------+---------------------+
     25%                35%                    40%
```

## Prerequisites

- [Android Studio](https://developer.android.com/studio) (Ladybug or newer recommended)
- JDK 11+
- An Android tablet or emulator (landscape orientation)

## Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/Prorickey/RoomAssistant.git
   cd RoomAssistant
   ```

2. **Add API keys**

   Create or edit `local.properties` in the project root and add your API keys:

   ```properties
   govee.api.key=YOUR_GOVEE_API_KEY
   openweather.api.key=YOUR_OPENWEATHER_API_KEY
   ```

   - Get a Govee API key from the Govee Developer portal (request via the Govee Home app under Settings > About Us > Apply for API Key)
   - Get an OpenWeatherMap API key from [openweathermap.org](https://openweathermap.org/api)

   The app will still build and run without these keys — the weather widget and Govee controls will simply be inactive.

3. **Open in Android Studio**

   Open the project root folder in Android Studio. It will automatically sync Gradle and download dependencies.

## Building

### From the command line

```bash
# Build the debug APK
./gradlew assembleDebug

# Build and install on a connected device/emulator
./gradlew installDebug
```

### From Android Studio

Select the `app` run configuration and click **Run** (or press Shift+F10). Make sure a tablet device or emulator is selected as the target.

## Running & Testing

### Emulator setup

For the best experience, create a tablet AVD:

1. In Android Studio, open **Device Manager**
2. Click **Create Virtual Device**
3. Select a tablet profile (e.g. Pixel Tablet, 10" display)
4. Choose a system image with API 23 or higher
5. Launch the emulator and run the app

The app locks itself to landscape orientation automatically.

### Testing individual features

- **Weather**: Requires a valid `openweather.api.key` in `local.properties`. Weather data refreshes every 15 minutes.
- **Govee LEDs**: Requires a valid `govee.api.key` and at least one Govee device on your account. Check logcat for API responses.
- **Weather background effects**: To test rain/lightning without waiting for bad weather, temporarily hardcode the `weatherConditionId` in `WeatherViewModel.kt` to `500` (rain) or `200` (thunderstorm).
- **Schedule countdown**: Updates every second based on the embedded schedule in `app/src/main/assets/schedule_normal.json`. Edit this file to customize the schedule.

### Running unit tests

```bash
./gradlew test
```

### Running instrumented tests

Requires a connected device or running emulator:

```bash
./gradlew connectedAndroidTest
```

## Customization

- **Photos**: Replace the placeholder drawables (`res/drawable/collage_1.xml` through `collage_4.xml`) with your own images
- **Schedule**: Edit `app/src/main/assets/schedule_normal.json` to match your school's schedule
- **Marquee text**: Update the messages in `ui/components/MarqueeHeader.kt`
- **LED presets**: Modify colors and names in `viewmodel/GoveeViewModel.kt`
- **Location**: Weather and sunrise/sunset coordinates are hardcoded to NCSSM Durham (35.9847, -78.8986) in the ViewModels — change these for a different location

## Tech Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM (ViewModel + StateFlow) |
| Networking | Retrofit 2, OkHttp, Gson |
| Image loading | Coil |
| Async | Kotlin Coroutines |
| Min SDK | 23 (Android 6.0) |
| Target SDK | 36 |
