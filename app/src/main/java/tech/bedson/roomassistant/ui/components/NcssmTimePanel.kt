package tech.bedson.roomassistant.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.bedson.roomassistant.ui.theme.MutedGray
import tech.bedson.roomassistant.ui.theme.NeonCyan
import tech.bedson.roomassistant.ui.theme.NeonPink
import tech.bedson.roomassistant.ui.theme.NeonPurple
import tech.bedson.roomassistant.ui.theme.SoftWhite
import tech.bedson.roomassistant.ui.theme.SurfaceDark
import tech.bedson.roomassistant.ui.theme.WarmGlow
import tech.bedson.roomassistant.viewmodel.ScheduleViewModel

@Composable
fun NcssmTimePanel(
    viewModel: ScheduleViewModel,
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
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Countdown timer
            Text(
                text = String.format(
                    "%02d:%02d",
                    state.minutesRemaining,
                    state.secondsRemaining
                ),
                color = NeonCyan,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "Left in ${state.currentEvent}",
                color = MutedGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Next: ${state.nextEvent}",
                color = NeonPink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Vertical timeline
            val timelineEvents = buildList {
                if (state.sunrise.isNotEmpty()) add("Sunrise" to state.sunrise)
                state.mealTimes.breakfast?.let { add("Breakfast" to it) }
                state.mealTimes.lunch?.let { add("Lunch" to it) }
                state.mealTimes.dinner?.let { add("Dinner" to it) }
                if (state.sunset.isNotEmpty()) add("Sunset" to state.sunset)
                add("Check-in" to "11:00 PM")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                // Vertical line
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .align(Alignment.TopCenter)
                ) {
                    drawLine(
                        color = MutedGray.copy(alpha = 0.3f),
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2f
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timelineEvents.forEachIndexed { index, (label, time) ->
                        val isLeft = index % 2 == 0
                        val eventColor = when (label) {
                            "Sunrise", "Sunset" -> WarmGlow
                            "Breakfast", "Lunch", "Dinner" -> NeonCyan
                            "Check-in" -> NeonPink
                            else -> NeonPurple
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isLeft) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = label,
                                        color = eventColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = time,
                                        color = MutedGray,
                                        fontSize = 10.sp
                                    )
                                }
                                // Dot
                                Canvas(modifier = Modifier.padding(horizontal = 8.dp).width(8.dp).height(8.dp)) {
                                    drawCircle(
                                        color = eventColor,
                                        radius = 4.dp.toPx()
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                                Canvas(modifier = Modifier.padding(horizontal = 8.dp).width(8.dp).height(8.dp)) {
                                    drawCircle(
                                        color = eventColor,
                                        radius = 4.dp.toPx()
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = label,
                                        color = eventColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = time,
                                        color = MutedGray,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
