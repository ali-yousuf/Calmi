package com.calmi.app.ui.player.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun TimerBottomSheet(
    onTimerSelected: (Duration) -> Unit,
    onDismiss: () -> Unit
) {
    val timerOptions = listOf(
        "OFF" to Duration.ZERO,
        "5 mins" to 5.minutes,
        "15 mins" to 15.minutes,
        "30 mins" to 30.minutes,
        "45 mins" to 45.minutes,
        "1 hour" to 1.hours,
        "2 hours" to 2.hours
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set Timer", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(timerOptions) { (label, duration) ->
                OutlinedButton(
                    onClick = {
                        onTimerSelected(duration)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
