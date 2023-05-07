package com.saeedlotfi.robikaTest.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.saeedlotfi.robikaTest.ui.screen.comments.CommentsNavigation
import com.saeedlotfi.robikaTest.ui.screen.comments.navigateToComments
import com.saeedlotfi.robikaTest.ui.screen.posts.PostsNavigation
import com.saeedlotfi.robikaTest.ui.viewModels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") viewModel: HomeViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Robika Test Application",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.W900
                    )
                },
                modifier = modifier.background(MaterialTheme.colorScheme.primary),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        }
    ) { innerPadding ->
        StartNavigation(
            innerPadding = innerPadding
        )
    }
}

@Composable
private fun StartNavigation(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        modifier = modifier.padding(innerPadding),
        startDestination = PostsNavigation.PostsRoute
    ) {

        PostsNavigation(
            navigateToComments = navController::navigateToComments,
            onBackClick = navController::popBackStack
        ).addTo(this)

        CommentsNavigation(
            onBackClick = navController::popBackStack
        ).addTo(this)

    }

}

