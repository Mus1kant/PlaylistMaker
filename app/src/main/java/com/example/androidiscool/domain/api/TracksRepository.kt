package com.example.androidiscool.domain.api

import com.example.androidiscool.domain.model.Track

interface TracksRepository {

    fun searchTracks(expression: String): List<Track>

    suspend fun loadSomeData(): List<Track>

    suspend fun searchTracksContains(expression: String): List<Track>
}