package com.calmi.app.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.calmi.app.R
import com.calmi.app.domain.model.Sound
import com.calmi.app.ui.screens.home.components.SoundCard
import com.calmi.app.ui.theme.CalmiTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "CALMI",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            })
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "29:45", style = MaterialTheme.typography.bodyLarge)
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.ic_timer), contentDescription = "Timer")
                        }
                    }
                    IconButton(
                        onClick = { viewModel.onPlayPauseClicked() },
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (uiState.soundList.any { it.isPlaying }) R.drawable.ic_pause else R.drawable.ic_play,
                            ),
                            contentDescription = if (uiState.soundList.any { it.isPlaying }) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                    BadgedBox(
                        badge = { Badge { Text("3") } },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mixer),
                            contentDescription = "Mixer",
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            HomeScreenContent(
                paddingValues = paddingValues,
                sounds = uiState.soundList,
                onSoundClicked = { viewModel.onSoundClicked(it)},
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    sounds: List<Sound>,
    onSoundClicked: (Sound) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = paddingValues,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(sounds) { sound ->
            SoundCard(sound = sound, onSoundClicked = onSoundClicked)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    val sounds = listOf(
        Sound("ocean", "Ocean Waves", "file:///android_asset/images/ocean.jpg", "ocean.wav"),
        Sound("ocean", "Ocean Waves", "file:///android_asset/images/ocean.jpg", "ocean.wav"),
        Sound("ocean", "Ocean Waves", "file:///android_asset/images/ocean.jpg", "ocean.wav"),
        Sound("ocean", "Ocean Waves", "file:///android_asset/images/ocean.jpg", "ocean.wav"),
    )
    CalmiTheme {
        HomeScreenContent(
            paddingValues = PaddingValues(),
            sounds = sounds,
            onSoundClicked = {}
        )
    }
}
