package com.example.androidiscool.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidiscool.domain.api.TrackSearchInteractor
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackSearchInteractor: TrackSearchInteractor
) : ViewModel() {

    private val _searchScreenState =
        MutableStateFlow<SearchState>(SearchState.Initial)

    val searchScreenState = _searchScreenState.asStateFlow()

    private var searchJob: Job? = null

    fun searchTracks(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
            return
        }

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.update { SearchState.Searching }

            try {
                val tracks = trackSearchInteractor.searchTracksContains(query.trim())
                currentCoroutineContext().ensureActive()
                _searchScreenState.update {
                    if (tracks.isEmpty()) SearchState.Empty
                    else SearchState.Success(tracks)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _searchScreenState.update { SearchState.Fail(SearchError.NETWORK) }
            } catch (e: Exception) {
                _searchScreenState.update { SearchState.Fail(SearchError.UNKNOWN) }
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        searchJob = null
        _searchScreenState.update { SearchState.Initial }
    }
}
