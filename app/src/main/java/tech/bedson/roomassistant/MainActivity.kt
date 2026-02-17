package tech.bedson.roomassistant

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.bedson.roomassistant.ui.screens.DashboardScreen
import tech.bedson.roomassistant.ui.theme.RoomAssistantTheme
import tech.bedson.roomassistant.viewmodel.GoveeViewModel
import tech.bedson.roomassistant.viewmodel.ScheduleViewModel
import tech.bedson.roomassistant.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Immersive fullscreen: hide status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            RoomAssistantTheme {
                val weatherViewModel: WeatherViewModel = viewModel()
                val goveeViewModel: GoveeViewModel = viewModel()
                val scheduleViewModel: ScheduleViewModel = viewModel()

                DashboardScreen(
                    weatherViewModel = weatherViewModel,
                    goveeViewModel = goveeViewModel,
                    scheduleViewModel = scheduleViewModel
                )
            }
        }
    }
}
