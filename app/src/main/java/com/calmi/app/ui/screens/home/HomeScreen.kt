package com.calmi.app.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.calmi.app.domain.model.Sound
import com.calmi.app.ui.screens.home.components.BottomMiniPlayer
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
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                HomeScreenContent(
                    paddingValues = paddingValues,
                    sounds = uiState.soundList,
                    onSoundClicked = { viewModel.onSoundClicked(it) },
                )
            }


            val isPlaying = uiState.soundList.any { it.isPlaying }
            if (isPlaying) {
                val currentPlayingSound = uiState.soundList.first { it.isPlaying }
                val badgeCount = uiState.soundList.map { it.isPlaying }.count { it }
                BottomMiniPlayer(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    isPlaying = true,
                    currentPlayingSound = currentPlayingSound,
                    badgeCount = badgeCount,
                    onPlayPauseClicked = { viewModel.onPlayPauseClicked() }
                )
            }
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
