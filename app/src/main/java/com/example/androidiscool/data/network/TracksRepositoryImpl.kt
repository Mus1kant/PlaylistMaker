package com.example.androidiscool.data.network

import com.example.androidiscool.data.NetworkClient
import com.example.androidiscool.data.dto.TracksSearchRequest
import com.example.androidiscool.data.dto.TracksSearchResponse
import com.example.androidiscool.data.mapper.toTrack
import com.example.androidiscool.domain.api.TracksRepository
import com.example.androidiscool.domain.model.Track
import kotlinx.coroutines.delay
import com.example.androidiscool.data.dto.TrackDto
import com.example.androidiscool.data.mapper.toTrack
import kotlinx.coroutines.delay

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    private val tracksDto = listOf(
        TrackDto(1, "Yesterday (Remastered 2009)", "The Beatles", 125000L),
        TrackDto(2, "Here Comes The Sun (Remastered...)", "The Beatles", 187000L),
        TrackDto(3, "No Reply", "The Beatles", 152000L),
        TrackDto(4, "Let It Be", "The Beatles", 243000L),
        TrackDto(5, "Girl", "The Beatles", 138000L),
        TrackDto(6, "Michelle", "The Beatles", 167000L),
        TrackDto(7, "Eleanor Rigby", "The Beatles", 198000L),
        TrackDto(8, "Come Together", "The Beatles", 259000L)
    )

    override suspend fun searchTracksContains(
        expression: String
    ): List<Track> {

        delay(3000L)

        return tracksDto
            .filter { track ->
                track.trackName.contains(expression, ignoreCase = true) ||
                        track.artistName.contains(expression, ignoreCase = true)
            }
            .map { trackDto ->
                trackDto.toTrack()
            }
    }


    override fun searchTracks(expression: String): List<Track> {

        val response = networkClient.doRequest(
            TracksSearchRequest(expression)
        )

        if (response.resultCode == 200 &&
            response is TracksSearchResponse
        ) {
            return response.results.map { trackDto ->
                trackDto.toTrack()
            }
        } else {
            return emptyList()
        }
    }

    override suspend fun loadSomeData(): List<Track> {
        delay(5000L)
        return emptyList()
    }
}