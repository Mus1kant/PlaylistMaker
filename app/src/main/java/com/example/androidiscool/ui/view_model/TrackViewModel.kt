package com.example.androidiscool.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidiscool.domain.api.TrackSearchInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackViewModel(
    private val tracksInteractor: TrackSearchInteractor
) : ViewModel() {

    private val _loadingStateFlow = MutableStateFlow(true)

    fun getLoadingStateFlow(): StateFlow<Boolean> {
        return _loadingStateFlow.asStateFlow()
    }

    init {
        viewModelScope.launch {
            tracksInteractor.loadSomeData()
            _loadingStateFlow.update { false }
        }
    }
}
