package com.calmi.app.ui.screens.player.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedCircularPlayerView(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    content: @Composable () -> Unit,
    numCircles: Int = 3,
    delayBetweenCircles: Long = 800
) {
    val pulseAnims = remember { List(numCircles) { Animatable(0f) } }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            pulseAnims.forEachIndexed { index, anim ->
                launch {
                    delay(index * delayBetweenCircles)
                    anim.animateTo(
                        1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(3000, easing = EaseIn),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        } else {
            pulseAnims.forEach { it.snapTo(0f) }
        }
    }

    Box(
        modifier = modifier.drawBehind {
            val maxRadius = size.width / 2

            // Static outer circle
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = maxRadius,
                center = center,
                style = Stroke(width = 2f)
            )

            // Animated spreading circles
            if (isPlaying) {
                pulseAnims.forEach { anim ->
                    val animatedProgress = anim.value
                    if (animatedProgress > 0f) { // Only draw if animation has started
                        val animatedRadius = maxRadius * (1 + animatedProgress * 0.3f)
                        val animatedAlpha = 4f * animatedProgress * (1f - animatedProgress)
                        drawCircle(
                            color = Color.White.copy(alpha = animatedAlpha),
                            radius = animatedRadius,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                    }
                }
            }
        },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun AnimatedCircularPlayerViewPreview() {
    var isPlaying by remember { mutableStateOf(true) }
    AnimatedCircularPlayerView(
        modifier = Modifier.size(200.dp),
        isPlaying = isPlaying,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("20:16", style = MaterialTheme.typography.displayLarge.copy(color = Color.White))
                Text("Timer", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
            }
        }
    )
}
