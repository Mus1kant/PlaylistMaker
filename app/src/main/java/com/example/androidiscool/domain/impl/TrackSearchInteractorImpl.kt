package com.example.androidiscool.domain.impl

import com.example.androidiscool.domain.api.TrackSearchInteractor
import com.example.androidiscool.domain.api.TracksRepository
import com.example.androidiscool.domain.model.Track

class TrackSearchInteractorImpl(
    private val repository: TracksRepository
) : TrackSearchInteractor {

    override fun searchTracks(expression: String): List<Track> {
        return repository.searchTracks(expression)
    }

    override suspend fun loadSomeData(): List<Track> {
        return repository.loadSomeData()
    }
}