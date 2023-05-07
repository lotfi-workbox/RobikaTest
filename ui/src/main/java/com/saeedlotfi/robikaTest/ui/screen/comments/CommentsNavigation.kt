@file:Suppress("unused")

package com.saeedlotfi.robikaTest.ui.screen.comments

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

fun NavController.navigateToComments(postId: Long, navOptions: NavOptions? = null) {
    val route = CommentsNavigation.CommentsRoute
        .replace("{postId}", "$postId")
    navigate(route, navOptions)
}

class CommentsNavigation(
    private val onBackClick: () -> Unit
) : ComposeNavigation() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Composable
    override fun NavBackStackEntry.Start() {
        (LocalContext.current.applicationContext as UserInterfaceComponentProvider)
            .getUserInterfaceComponent().inject(this@CommentsNavigation)
        CommentsScreen(
            viewModel = viewModelFactory.daggerViewModel(),
            postId = arguments?.getString("postId")!!.toLong()
        )
    }

    override fun getRoute(): String = CommentsRoute

    companion object {
        const val CommentsRoute = "comments_route/{postId}"
    }

}
