package com.historytoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.historytoday.navigation.NavGraph
import com.historytoday.ui.theme.HistoryTodayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoryTodayTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
