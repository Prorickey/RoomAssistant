package tech.bedson.roomassistant.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun WeatherBackground(
    weatherConditionId: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isRaining = weatherConditionId in 200..531
    val isThunderstorm = weatherConditionId in 200..232

    Box(modifier = modifier.fillMaxSize()) {
        // Rain overlay
        if (isRaining) {
            RainOverlay()
        }

        // Lightning flash overlay
        if (isThunderstorm) {
            LightningOverlay()
        }

        content()
    }
}

@Composable
private fun RainOverlay() {
    val transition = rememberInfiniteTransition(label = "rain")
    val rainOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rainDrop"
    )

    val rainDrops = remember {
        List(60) {
            RainDrop(
                x = Random.nextFloat(),
                startY = Random.nextFloat() * -0.3f,
                length = Random.nextFloat() * 0.03f + 0.02f,
                speed = Random.nextFloat() * 0.3f + 0.7f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        rainDrops.forEach { drop ->
            val y = ((drop.startY + rainOffset * drop.speed) % 1.3f) * h
            val x = drop.x * w
            drawLine(
                color = Color.White.copy(alpha = 0.12f),
                start = Offset(x, y),
                end = Offset(x - 4f, y + drop.length * h),
                strokeWidth = 1.5f
            )
        }
    }
}

private data class RainDrop(
    val x: Float,
    val startY: Float,
    val length: Float,
    val speed: Float
)

@Composable
private fun LightningOverlay() {
    var flashAlpha by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(15000, 45000))
            // Flash sequence
            flashAlpha = 0.08f
            delay(80)
            flashAlpha = 0f
            delay(100)
            flashAlpha = 0.05f
            delay(60)
            flashAlpha = 0f
        }
    }

    if (flashAlpha > 0f) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color(0xFF00F0FF).copy(alpha = flashAlpha)
            )
        }
    }
}
