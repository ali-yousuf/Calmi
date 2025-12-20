package com.calmi.app.ui.screens.player

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.calmi.app.R
import com.calmi.app.navigation.Screens
import com.calmi.app.ui.screens.SoundPlayerEvent
import com.calmi.app.ui.screens.SoundPlayerUiState
import com.calmi.app.ui.screens.SoundPlayerViewModel
import com.calmi.app.ui.screens.player.components.AnimatedCircularPlayerView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayerScreen(navController: NavController) {
    // Find the back stack entry for the parent navigation graph
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(Screens.Home.route) // MUST be the same route used in HomeScreen
    }

    // Pass the same parent entry to get the SAME shared ViewModel instance
    val viewModel: SoundPlayerViewModel = hiltViewModel(parentEntry)
    val uiState by viewModel.uiState.collectAsState()
    PlayerScreenContent(
        uiState = uiState,
        onBackClicked = { navController.popBackStack() },
        onPlayPauseClicked = { viewModel.onEvent(SoundPlayerEvent.PlayPauseClicked) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreenContent(
    uiState: SoundPlayerUiState,
    onBackClicked: () -> Unit,
    onPlayPauseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        uiState.activeSounds.first().name,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_down),
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(model = uiState.activeSounds.first().imagePath),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 7.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                AnimatedCircularPlayerView(
                    modifier = Modifier.size(200.dp),
                    isPlaying = uiState.isPlaying,
                    content = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("20:16", style = MaterialTheme.typography.displayLarge.copy(color = Color.White))
                            TextButton(
                                onClick = {/*TODO: implement set timer */}
                            ) {
                                Text("Timer", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(48.dp))
                FilledTonalButton(
                    onClick = onPlayPauseClicked,
                    modifier = Modifier.width(150.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        contentColor = Color.Black.copy(alpha = 0.1f)
                    )

                ) {
                    Icon(
                        painter = painterResource(if (uiState.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = "play pause",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayerScreenPreview() {
    val uiState = SoundPlayerUiState()
    PlayerScreenContent(uiState = uiState, onBackClicked = {}, onPlayPauseClicked = {})
}
