package com.example.androidiscool.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidiscool.R
import com.example.androidiscool.domain.model.Track
import com.example.androidiscool.ui.view_model.SearchError
import com.example.androidiscool.ui.view_model.SearchState
import com.example.androidiscool.ui.view_model.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    navigateToDetailScreen: (Track) -> Unit,
    navigateBack: () -> Unit
) {
    val searchViewModel: SearchViewModel = koinViewModel()
    val screenState by searchViewModel.searchScreenState.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val submitSearch: () -> Unit = {
        if (text.isNotBlank()) {
            searchViewModel.searchTracks(text.trim())
            focusManager.clearFocus()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(
                text = stringResource(R.string.search_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        val hint = stringResource(R.string.search_hint)
        val searchActionDescription = stringResource(R.string.search_action)
        val clearSearchDescription = stringResource(R.string.clear_search)

        BasicTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                if (newText.isEmpty()) searchViewModel.clearSearch()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xFFE6E8EB), RoundedCornerShape(8.dp)),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { submitSearch() }),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = searchActionDescription,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp).clickable { submitSearch() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (text.isEmpty()) {
                            Text(text = hint, fontSize = 16.sp, color = Color.Gray)
                        }
                        innerTextField()
                    }
                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = clearSearchDescription,
                            tint = Color.DarkGray,
                            modifier = Modifier.size(20.dp).clickable {
                                text = ""
                                searchViewModel.clearSearch()
                            }
                        )
                    }
                }
            }
        )

        SearchResults(
            screenState = screenState,
            modifier = Modifier.weight(1f).padding(top = 16.dp),
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
        SearchState.Initial -> Unit
        SearchState.Searching -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is SearchState.Success -> LazyColumn(modifier = modifier.fillMaxSize()) {
            items(items = screenState.foundList, key = { it.id }) { track ->
                TrackListItem(track = track, onClick = { onTrackClick(track) })
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFD9DDE2))
            }
        }

        SearchState.Empty -> InformerState(
            titleText = stringResource(R.string.search_nothing_found),
            imageRes = R.drawable.ic_nothing_found,
            modifier = modifier
        )

        is SearchState.Fail -> {
            val networkFailure = screenState.reason == SearchError.NETWORK
            InformerState(
                titleText = stringResource(
                    if (networkFailure) R.string.search_no_internet
                    else R.string.search_error_title
                ),
                subtitleText = if (networkFailure) {
                    stringResource(R.string.search_check_internet)
                } else {
                    stringResource(R.string.search_generic_error)
                },
                imageRes = R.drawable.ic_no_internet,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun InformerState(
    titleText: String,
    subtitleText: String? = null,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = titleText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (subtitleText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitleText,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TrackListItem(track: Track, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.DarkGray
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = track.artistName,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(text = track.trackTime, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptySearchPreview() {
    SearchResults(SearchState.Empty, Modifier.fillMaxSize(), onTrackClick = {})
}

@Preview(showBackground = true)
@Composable
private fun NoInternetPreview() {
    SearchResults(SearchState.Fail(SearchError.NETWORK), Modifier.fillMaxSize(), onTrackClick = {})
}
