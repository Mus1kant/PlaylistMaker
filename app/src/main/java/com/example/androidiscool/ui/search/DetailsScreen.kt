package com.example.androidiscool.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidiscool.R
import com.example.androidiscool.domain.model.Track

@Composable
fun DetailsScreen(track: Track, navigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = navigateBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Text(text = stringResource(R.string.back))
        }
        Text(text = track.trackName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = stringResource(R.string.track_artist_label, track.artistName))
        Text(text = stringResource(R.string.track_duration_label, track.trackTime))
    }
}
