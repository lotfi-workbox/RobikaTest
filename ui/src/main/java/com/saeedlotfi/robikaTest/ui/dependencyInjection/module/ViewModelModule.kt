@file:Suppress("unused")

package com.saeedlotfi.robikaTest.ui.dependencyInjection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saeedlotfi.robikaTest.ui.dependencyInjection.ViewModelFactory
import com.saeedlotfi.robikaTest.ui.viewModels.CommentsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass
import com.saeedlotfi.robikaTest.ui.viewModels.HomeViewModel
import com.saeedlotfi.robikaTest.ui.viewModels.PostsViewModel

@Module
abstract class ViewModelModule {

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    annotation class ViewModelKey(val value : KClass<out ViewModel>)

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun mainViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    abstract fun postsViewModel(viewModel: PostsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    abstract fun commentsViewModel(viewModel: CommentsViewModel): ViewModel

}