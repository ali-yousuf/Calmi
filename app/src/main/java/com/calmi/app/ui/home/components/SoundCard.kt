package com.calmi.app.ui.home.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.calmi.app.domain.model.Sound

@Composable
fun SoundCard(
    sound: Sound,
    onSoundClicked: (Sound) -> Unit,
) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .clickable { onSoundClicked(sound) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            Image(
                painter = rememberAsyncImagePainter(model = sound.imagePath),
                contentDescription = sound.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = sound.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(16.dp)
            )

            if (sound.isPlaying) {
                AnimatedPlaybackIndicator(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedPlaybackIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "playbackIndicatorTransition")

    val bar1HeightRatio by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "bar1HeightRatio"
    )
    val bar2HeightRatio by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = .8f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 100), RepeatMode.Reverse),
        label = "bar2HeightRatio"
    )
    val bar3HeightRatio by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(400, delayMillis = 200), RepeatMode.Reverse),
        label = "bar3HeightRatio"
    )
    val bar4HeightRatio by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(550, delayMillis = 50), RepeatMode.Reverse),
        label = "bar4HeightRatio"
    )

    Canvas(modifier = modifier) {
        val barWidth = size.width / 4f
        val spacing = barWidth / 3f

        val startX1 = 0f
        val startX2 = startX1 + barWidth + spacing
        val startX3 = startX2 + barWidth + spacing
        val startX4 = startX3 + barWidth + spacing

        val barColor = Color.White

        drawRect(
            color = barColor,
            topLeft = Offset(startX1, size.height * (1f - bar1HeightRatio)),
            size = Size(barWidth, size.height * bar1HeightRatio)
        )
        drawRect(
            color = barColor,
            topLeft = Offset(startX2, size.height * (1f - bar2HeightRatio)),
            size = Size(barWidth, size.height * bar2HeightRatio)
        )
        drawRect(
            color = barColor,
            topLeft = Offset(startX3, size.height * (1f - bar3HeightRatio)),
            size = Size(barWidth, size.height * bar3HeightRatio)
        )
        drawRect(
            color = barColor,
            topLeft = Offset(startX4, size.height * (1f - bar4HeightRatio)),
            size = Size(barWidth, size.height * bar4HeightRatio)
        )
    }
}
