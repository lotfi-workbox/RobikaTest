package com.saeedlotfi.robikaTest.ui._common

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

abstract class ComposeNavigation {

    fun addTo(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable(route = getRoute()) { it.Start() }
    }

    @Composable
    protected abstract fun NavBackStackEntry.Start()

    protected abstract fun getRoute(): String

}