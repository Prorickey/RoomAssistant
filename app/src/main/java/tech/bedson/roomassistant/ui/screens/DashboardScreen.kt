package tech.bedson.roomassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.bedson.roomassistant.ui.components.GoveeControlPanel
import tech.bedson.roomassistant.ui.components.MarqueeHeader
import tech.bedson.roomassistant.ui.components.NcssmTimePanel
import tech.bedson.roomassistant.ui.components.PhotoCollage
import tech.bedson.roomassistant.ui.components.WeatherBackground
import tech.bedson.roomassistant.ui.components.WeatherWidget
import tech.bedson.roomassistant.ui.theme.DarkBackground
import tech.bedson.roomassistant.viewmodel.GoveeViewModel
import tech.bedson.roomassistant.viewmodel.ScheduleViewModel
import tech.bedson.roomassistant.viewmodel.WeatherViewModel

@Composable
fun DashboardScreen(
    weatherViewModel: WeatherViewModel,
    goveeViewModel: GoveeViewModel,
    scheduleViewModel: ScheduleViewModel
) {
    val weatherState by weatherViewModel.uiState.collectAsState()

    WeatherBackground(
        weatherConditionId = weatherState.weatherConditionId
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground.copy(alpha = 0.92f))
        ) {
            // Marquee header
            MarqueeHeader()

            // Main content row
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Left column (25%) - Photo + Weather
                Column(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                ) {
                    PhotoCollage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    WeatherWidget(
                        viewModel = weatherViewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                    )
                }

                // Center column (35%) - Govee Controls
                Column(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                ) {
                    GoveeControlPanel(
                        viewModel = goveeViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Right column (40%) - NCSSM Time Panel
                Column(
                    modifier = Modifier
                        .weight(0.40f)
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    NcssmTimePanel(
                        viewModel = scheduleViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
