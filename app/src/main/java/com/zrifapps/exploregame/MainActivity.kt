package com.zrifapps.exploregame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.zrifapps.exploregame.navigation.AppRouter
import com.zrifapps.exploregame.navigation.GameList
import com.zrifapps.exploregame.ui.theme.ExploreGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploreGameTheme {
                AppRouter(
                    navController = rememberNavController(),
                    startDestination = GameList,
                )
            }
        }
    }
}
