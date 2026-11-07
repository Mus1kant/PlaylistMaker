package com.example.androidiscool.domain.api

import com.example.androidiscool.domain.model.Track

interface TrackSearchInteractor {

    fun searchTracks(expression: String): List<Track>

    suspend fun loadSomeData(): List<Track>
}