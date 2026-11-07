package com.example.androidiscool.data.mapper

import com.example.androidiscool.data.dto.TrackDto
import com.example.androidiscool.domain.model.Track

fun TrackDto.toTrack(): Track {

    val totalSeconds = trackTimeMillis / 1000

    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    val formattedTime =
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    return Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = formattedTime
    )
}