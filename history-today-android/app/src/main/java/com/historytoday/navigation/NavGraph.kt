package com.historytoday.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.historytoday.ui.calendar.CalendarScreen
import com.historytoday.ui.detail.DetailScreen
import com.historytoday.ui.history.HistoryScreen

@Composable
fun NavGraph(navController: NavController) {
    NavHost(navController = navController, startDestination = "calendar") {
        composable("calendar") {
            CalendarScreen().Content(navController = navController)
        }
        
        composable(
            "history/{year}/{month}/{day}",
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType },
                navArgument("day") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 2025
            val month = backStackEntry.arguments?.getInt("month") ?: 5
            val day = backStackEntry.arguments?.getInt("day") ?: 3
            HistoryScreen().Content(
                navController = navController,
                year = year,
                month = month,
                day = day
            )
        }
        
        composable(
            "detail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            DetailScreen().Content(
                navController = navController,
                eventId = eventId
            )
        }
    }
}
