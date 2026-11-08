package com.example.androidiscool.data.repository

import com.example.androidiscool.data.NetworkClient
import com.example.androidiscool.data.dto.TracksSearchRequest
import com.example.androidiscool.data.dto.TracksSearchResponse
import com.example.androidiscool.data.mapper.toTrack
import com.example.androidiscool.data.storage.Storage
import com.example.androidiscool.domain.api.TracksRepository
import com.example.androidiscool.domain.model.Track
import kotlinx.coroutines.delay

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: Storage
) : TracksRepository {

    override suspend fun searchTracksContains(
        expression: String
    ): List<Track> {

        delay(3000L)

        return storage.tracks
            .filter { track ->
                track.trackName.contains(
                    expression,
                    ignoreCase = true
                ) ||
                        track.artistName.contains(
                            expression,
                            ignoreCase = true
                        )
            }
            .map { trackDto ->
                trackDto.toTrack()
            }
    }

    override fun searchTracks(
        expression: String
    ): List<Track> {

        val response = networkClient.doRequest(
            TracksSearchRequest(expression)
        )

        return if (
            response.resultCode == 200 &&
            response is TracksSearchResponse
        ) {
            response.results.map { trackDto ->
                trackDto.toTrack()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun loadSomeData(): List<Track> {
        delay(5000L)
        return emptyList()
    }
}
