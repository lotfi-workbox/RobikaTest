package com.saeedlotfi.robikaTest.ui.dependencyInjection

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

@Composable
inline fun <reified T : ViewModel> ViewModelFactory.daggerViewModel(): T {
    return androidx.lifecycle.viewmodel.compose.viewModel(
        modelClass = T::class.java, factory = this
    )
}
