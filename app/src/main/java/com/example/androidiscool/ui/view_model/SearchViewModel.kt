package com.example.androidiscool.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidiscool.domain.api.TracksRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackRepository: TracksRepository
) : ViewModel() {

    private val _searchScreenState =
        MutableStateFlow<SearchState>(SearchState.Initial)

    val searchScreenState = _searchScreenState.asStateFlow()

    private var searchJob: Job? = null

    fun searchTracks(query: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _searchScreenState.value = SearchState.Searching

            try {
                val result = trackRepository.searchTracksContains(query)

                if (result.isNotEmpty()) {
                    _searchScreenState.value = SearchState.Success(result)
                } else {
                    _searchScreenState.value = SearchState.Empty
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _searchScreenState.value =
                    SearchState.Fail("Ошибка при поиске: ${e.message}")
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        searchJob = null
        _searchScreenState.value = SearchState.Initial
    }
}