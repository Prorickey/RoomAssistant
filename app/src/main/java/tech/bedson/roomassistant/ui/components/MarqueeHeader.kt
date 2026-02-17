package tech.bedson.roomassistant.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.bedson.roomassistant.ui.theme.DarkBackground
import tech.bedson.roomassistant.ui.theme.NeonCyan
import tech.bedson.roomassistant.ui.theme.NeonPink

@Composable
fun MarqueeHeader(modifier: Modifier = Modifier) {
    val messages = "Welcome to Fourth West IVIZ  \u2022  Home of Trevor, Atlas, and Josh  \u2022  "
    val repeatedText = messages.repeat(3)

    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val transition = rememberInfiniteTransition(label = "marquee")
    val offset by transition.animateFloat(
        initialValue = screenWidth,
        targetValue = -screenWidth * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "marqueeOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(DarkBackground),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = repeatedText,
            color = NeonCyan,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier
                .offset(x = offset.dp)
                .padding(vertical = 8.dp)
        )
    }
}
