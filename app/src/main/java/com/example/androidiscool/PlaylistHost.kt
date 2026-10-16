package com.example.androidiscool

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

enum class Screen(val route: String) {
    MAIN("main"),
    SEARCH("search"),
    SETTINGS("settings")
}

@Composable
fun PlaylistHost(
    navController: NavHostController
) {
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
        composable(Screen.MAIN.route) {
            MainScreen(
                onSearchClick = navigateSearch,
                onSettingsClick = navigateSettings
            )
        }

        composable(Screen.SEARCH.route) {
            SearchScreen(
                onBackClick = navigateBack
            )
        }

        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBackClick = navigateBack
            )
        }
    }
}