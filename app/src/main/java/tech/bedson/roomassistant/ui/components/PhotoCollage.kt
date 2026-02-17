package tech.bedson.roomassistant.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import tech.bedson.roomassistant.R
import tech.bedson.roomassistant.ui.theme.NeonPurple

@Composable
fun PhotoCollage(modifier: Modifier = Modifier) {
    val photos = listOf(
        R.drawable.collage_1,
        R.drawable.collage_2,
        R.drawable.collage_3,
        R.drawable.collage_4
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(8000)
            currentIndex = (currentIndex + 1) % photos.size
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = NeonPurple.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Crossfade(
            targetState = currentIndex,
            animationSpec = tween(durationMillis = 1000),
            label = "photoFade"
        ) { index ->
            Image(
                painter = painterResource(id = photos[index]),
                contentDescription = "Photo ${index + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
