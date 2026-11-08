package com.example.androidiscool.ui.view_model

import com.example.androidiscool.domain.model.Track

enum class SearchError {
    NETWORK,
    UNKNOWN
}

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    object Empty : SearchState()
    data class Fail(val reason: SearchError) : SearchState()
}
