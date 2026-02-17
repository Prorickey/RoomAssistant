package tech.bedson.roomassistant.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.bedson.roomassistant.ui.theme.DarkBackground
import tech.bedson.roomassistant.ui.theme.MutedGray
import tech.bedson.roomassistant.ui.theme.NeonCyan
import tech.bedson.roomassistant.ui.theme.NeonPink
import tech.bedson.roomassistant.ui.theme.SoftWhite
import tech.bedson.roomassistant.ui.theme.SurfaceDark
import tech.bedson.roomassistant.viewmodel.GoveeViewModel

@Composable
fun GoveeControlPanel(
    viewModel: GoveeViewModel,
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
            Text(
                text = "LED CONTROL",
                color = MutedGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Power toggle
            val powerColor by animateColorAsState(
                targetValue = if (state.isPoweredOn) NeonCyan else MutedGray,
                animationSpec = tween(300),
                label = "powerColor"
            )
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(if (state.isPoweredOn) NeonCyan.copy(alpha = 0.15f) else DarkBackground)
                    .border(2.dp, powerColor, CircleShape)
                    .clickable { viewModel.togglePower() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.isPoweredOn) "ON" else "OFF",
                    color = powerColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preset grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                itemsIndexed(viewModel.presets) { index, preset ->
                    val isActive = state.activePresetIndex == index
                    val borderColor by animateColorAsState(
                        targetValue = if (isActive) preset.color else MutedGray.copy(alpha = 0.3f),
                        animationSpec = tween(300),
                        label = "presetBorder"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isActive) preset.color.copy(alpha = 0.15f)
                                else DarkBackground
                            )
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .clickable { viewModel.applyPreset(index) }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(preset.color)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = preset.name,
                                color = if (isActive) SoftWhite else MutedGray,
                                fontSize = 11.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
