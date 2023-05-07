@file:Suppress("unused")

package com.saeedlotfi.robikaTest.ui.screen.home

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

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    navigate(HomeNavigation.HomeRoute, navOptions)

class HomeNavigation(private val onBackClick: () -> Unit) : ComposeNavigation() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Composable
    override fun NavBackStackEntry.Start() {
        (LocalContext.current.applicationContext as UserInterfaceComponentProvider)
            .getUserInterfaceComponent().inject(this@HomeNavigation)
        HomeScreen(
            viewModel = viewModelFactory.daggerViewModel(),
        )
    }

    override fun getRoute(): String = HomeRoute

    companion object {
        const val HomeRoute = "home_route"
    }

}