package com.wirne.catandice

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wirne.catandice.feature.game.GameRoute
import com.wirne.catandice.feature.settings.SettingsRoute
import com.wirne.catandice.feature.stats.StatsRoute
import com.wirne.catandice.feature.timer.FloatingTimer
import com.wirne.catandice.ui.theme.CDTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CDActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CDTheme {
                Box {
                    CDNavHost()
                    FloatingTimer()
                }
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
private fun CDNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "game"
    ) {

        composable(route = "game") {
            GameRoute(
                openSettings = {
                    navController.navigate(route = "settings")
                },
                openStats = {
                    navController.navigate(route = "stats")
                }
            )
        }

        composable(route = "stats") {
            StatsRoute(onNavigationIconClick = navController::popBackStack)
        }

        composable(route = "settings") {
            SettingsRoute(onNavigationIconClick = navController::popBackStack)
        }
    }
}
