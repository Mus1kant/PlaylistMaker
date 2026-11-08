package com.example.androidiscool.domain.impl

import com.example.androidiscool.domain.api.TrackSearchInteractor
import com.example.androidiscool.domain.api.TracksRepository
import com.example.androidiscool.domain.model.Track

class TrackSearchInteractorImpl(
    private val repository: TracksRepository
) : TrackSearchInteractor {
    override suspend fun searchTracksContains(expression: String): List<Track> =
        repository.searchTracksContains(expression)

    override fun searchTracks(expression: String): List<Track> =
        repository.searchTracks(expression)

    override suspend fun loadSomeData(): List<Track> =
        repository.loadSomeData()
}
