package com.wirne.catandice

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wirne.catandice.feature.game.GameScreen
import com.wirne.catandice.feature.settings.SettingsScreen
import com.wirne.catandice.feature.stats.StatsScreen
import com.wirne.catandice.feature.timer.FloatingTimer
import com.wirne.catandice.ui.theme.CDTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CDActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            CDTheme {
                Box {
                    CDNavHost()
                    FloatingTimer(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
internal fun CDNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CDRoute.Game,
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 300)) },
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 300)) },
    ) {
        composable<CDRoute.Game> {
            GameScreen(
                openSettings = {
                    navController.navigate(route = CDRoute.Settings)
                },
                openStats = {
                    navController.navigate(route = CDRoute.Stats)
                },
            )
        }

        composable<CDRoute.Stats> {
            StatsScreen(onNavigationIconClick = navController::popBackStack)
        }

        composable<CDRoute.Settings> {
            SettingsScreen(onNavigationIconClick = navController::popBackStack)
        }
    }
}
