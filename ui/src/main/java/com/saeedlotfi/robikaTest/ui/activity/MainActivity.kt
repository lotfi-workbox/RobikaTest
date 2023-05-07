package com.saeedlotfi.robikaTest.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.saeedlotfi.robikaTest.ui.screen.home.HomeNavigation
import com.saeedlotfi.robikaTest.ui.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                StartNavigation()
            }
        }
    }

}

@Composable
private fun StartNavigation(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        modifier = modifier.fillMaxSize(),
        startDestination = HomeNavigation.HomeRoute
    ) {

        //If there is a login page, its navigation can be here

        HomeNavigation(
            onBackClick = navController::popBackStack,
        ).addTo(this)
        /**
         * also you can check
         * [com.saeedlotfi.robikaTest.ui.screen.home.StartNavigation]
         */

    }

}




