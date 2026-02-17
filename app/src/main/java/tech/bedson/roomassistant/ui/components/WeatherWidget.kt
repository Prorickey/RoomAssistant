package tech.bedson.roomassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tech.bedson.roomassistant.ui.theme.MutedGray
import tech.bedson.roomassistant.ui.theme.SoftWhite
import tech.bedson.roomassistant.ui.theme.SurfaceDark
import tech.bedson.roomassistant.ui.theme.WarmGlow
import tech.bedson.roomassistant.viewmodel.WeatherViewModel

@Composable
fun WeatherWidget(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "NCSSM Durham",
                color = MutedGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "${state.temperature.toInt()}\u00B0F",
                        color = WarmGlow,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = state.description.replaceFirstChar { it.uppercase() },
                        color = SoftWhite,
                        fontSize = 14.sp
                    )
                }
                if (state.iconCode.isNotEmpty()) {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${state.iconCode}@2x.png",
                        contentDescription = "Weather icon",
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Feels like ${state.feelsLike.toInt()}\u00B0F",
                color = MutedGray,
                fontSize = 12.sp
            )
        }
    }
}
