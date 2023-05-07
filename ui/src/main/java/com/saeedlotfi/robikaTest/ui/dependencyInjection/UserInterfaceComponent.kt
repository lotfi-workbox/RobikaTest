package com.saeedlotfi.robikaTest.ui.dependencyInjection

import com.saeedlotfi.robikaTest.ui.dependencyInjection.module.UseCaseModule
import com.saeedlotfi.robikaTest.ui.dependencyInjection.module.ViewModelModule
import com.saeedlotfi.robikaTest.ui.screen.comments.CommentsNavigation
import com.saeedlotfi.robikaTest.ui.screen.home.HomeNavigation
import com.saeedlotfi.robikaTest.ui.screen.posts.PostsNavigation
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ViewModelModule::class,
        UseCaseModule::class
    ],
)
interface UserInterfaceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserInterfaceComponent
    }

    //compose navigation

    fun inject(navigation: HomeNavigation)

    fun inject(navigation: PostsNavigation)

    fun inject(navigation: CommentsNavigation)

}