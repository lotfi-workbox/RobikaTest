@file:Suppress("unused")

package com.saeedlotfi.robikaTest.ui.screen.posts

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.saeedlotfi.robikaTest.ui.dependencyInjection.ViewModelFactory
import com.saeedlotfi.robikaTest.ui._common.ComposeNavigation
import com.saeedlotfi.robikaTest.ui.dependencyInjection.provider.UserInterfaceComponentProvider
import com.saeedlotfi.robikaTest.ui.dependencyInjection.daggerViewModel
import javax.inject.Inject

fun NavController.navigateToPosts(navOptions: NavOptions? = null) =
    navigate(PostsNavigation.PostsRoute, navOptions)

class PostsNavigation(
    private val navigateToComments: (postId: Long) -> Unit,
    private val onBackClick: () -> Unit
) : ComposeNavigation() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Composable
    override fun NavBackStackEntry.Start() {
        (LocalContext.current.applicationContext as UserInterfaceComponentProvider)
            .getUserInterfaceComponent().inject(this@PostsNavigation)
        PostsScreen(
            viewModel = viewModelFactory.daggerViewModel(),
            navigateToComments = navigateToComments
        )
    }

    override fun getRoute(): String = PostsRoute

    companion object {
        const val PostsRoute = "posts_route"
    }

}
