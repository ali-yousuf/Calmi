package com.calmi.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calmi.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }
    val bgOffsetY = remember { Animatable(20f) }

    LaunchedEffect(Unit) {
        launch {
            alphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            bgOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = FastOutSlowInEasing
                )
            )
        }

        delay(2200)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_nature),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = bgOffsetY.value.dp)
                .blur(12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "CALMI",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color.White
                ),
                modifier = Modifier.alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Find your calm",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.8f),
                ),
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
}
