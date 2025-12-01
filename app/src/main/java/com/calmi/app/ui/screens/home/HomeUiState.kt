package com.calmi.app.ui.screens.home

import com.calmi.app.domain.model.Sound

data class HomeUiState(
    val isLoading: Boolean = false,
    val soundList: List<Sound> = emptyList(),
    val errorMessage: String? = null
)
