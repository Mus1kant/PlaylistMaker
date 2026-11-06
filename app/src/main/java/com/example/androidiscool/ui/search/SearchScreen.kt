package com.example.androidiscool.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidiscool.domain.model.Track
import com.example.androidiscool.ui.view_model.SearchState
import com.example.androidiscool.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    navigateToDetailScreen: (Track) -> Unit,
    navigateBack: () -> Unit
) {
    val screenState by searchViewModel.searchScreenState.collectAsState()

    var text by rememberSaveable {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current

    val submitSearch: () -> Unit = {
        if (text.isNotBlank()) {
            searchViewModel.searchTracks(text.trim())
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = navigateBack) {
                Text("←", fontSize = 26.sp)
            }

            Text("Поиск", fontSize = 22.sp)
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Поиск треков")
            },
            singleLine = true,

            leadingIcon = {
                TextButton(onClick = submitSearch) {
                    Text("⌕", fontSize = 26.sp)
                }
            },

            trailingIcon = {
                if (text.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            text = ""
                            searchViewModel.clearSearch()
                        }
                    ) {
                        Text("✕")
                    }
                }
            },

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            keyboardActions = KeyboardActions(
                onSearch = {
                    submitSearch()
                }
            )
        )

        SearchResults(
            screenState = screenState,
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            onTrackClick = navigateToDetailScreen
        )
    }
}

@Composable
private fun SearchResults(
    screenState: SearchState,
    modifier: Modifier = Modifier,
    onTrackClick: (Track) -> Unit
) {
    when (screenState) {

        SearchState.Initial -> {
            // До первого поиска ничего не показываем
        }

        SearchState.Searching -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchState.Success -> {
            val tracks = screenState.foundList

            if (tracks.isEmpty()) {
                InformerState(
                    titleText = "Ничего не найдено",
                    modifier = modifier
                )
            } else {
                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = tracks,
                        key = { it.id }
                    ) { track ->
                        TrackListItem(
                            track = track,
                            onClick = {
                                onTrackClick(track)
                            }
                        )
                    }
                }
            }
        }

        is SearchState.Fail -> {
            InformerState(
                titleText = screenState.error,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun InformerState(
    titleText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = titleText,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun TrackListItem(
    track: Track,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(
                id = android.R.drawable.ic_menu_gallery
            ),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${track.artistName} • ${track.trackTime}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = "›",
            fontSize = 28.sp,
            color = Color.Gray
        )
    }
}