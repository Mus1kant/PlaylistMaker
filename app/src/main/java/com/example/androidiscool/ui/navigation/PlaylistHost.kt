package com.example.androidiscool.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidiscool.SettingsScreen
import com.example.androidiscool.domain.model.Track
import com.example.androidiscool.ui.activity.MainScreen
import com.example.androidiscool.ui.search.DetailsScreen
import com.example.androidiscool.ui.search.SearchScreen
import com.google.gson.Gson

enum class Screen(val route: String) {
    MAIN("main"),
    SEARCH("search"),
    SETTINGS("settings"),
    DETAILS("details/{trackJson}")
}

@Composable
fun PlaylistHost(
    navController: NavHostController
) {

    val gson = Gson()

    val navigateSearch: () -> Unit = {
        navController.navigate(Screen.SEARCH.route)
    }

    val navigateSettings: () -> Unit = {
        navController.navigate(Screen.SETTINGS.route)
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.route
    ) {

        // Главный экран
        composable(Screen.MAIN.route) {
            MainScreen(
                onSearchClick = navigateSearch,
                onSettingsClick = navigateSettings
            )
        }

        // Экран поиска
        composable(Screen.SEARCH.route) {
            SearchScreen(
                navigateToDetailScreen = { track ->
                    val trackJson = gson.toJson(track)

                    navController.navigate(
                        "details/${Uri.encode(trackJson)}"
                    )
                },

                navigateBack = navigateBack
            )
        }

        // Экран деталей трека
        composable(Screen.DETAILS.route) { backStackEntry ->

            val trackJson = backStackEntry.arguments
                ?.getString("trackJson")

            if (trackJson != null) {

                val track = gson.fromJson(
                    trackJson,
                    Track::class.java
                )

                DetailsScreen(
                    track = track,
                    navigateBack = navigateBack
                )
            }
        }

        // Экран настроек
        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBackClick = navigateBack
            )
        }
    }
}
