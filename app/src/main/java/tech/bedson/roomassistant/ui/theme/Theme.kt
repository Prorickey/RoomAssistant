package tech.bedson.roomassistant.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LofiDarkColorScheme = darkColorScheme(
    primary = NeonPink,
    secondary = NeonCyan,
    tertiary = NeonPurple,
    background = DarkBackground,
    surface = SurfaceDark,
    onPrimary = SoftWhite,
    onSecondary = DarkBackground,
    onTertiary = SoftWhite,
    onBackground = SoftWhite,
    onSurface = SoftWhite,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = MutedGray,
    outline = MutedGray
)

@Composable
fun RoomAssistantTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = LofiDarkColorScheme,
        typography = Typography,
        content = content
    )
}
