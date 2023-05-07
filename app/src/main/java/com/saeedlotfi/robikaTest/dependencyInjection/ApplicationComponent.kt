package com.saeedlotfi.robikaTest.dependencyInjection

import android.app.Application
import com.saeedlotfi.robikaTest.data.dependencyInjection.DataModule
import com.saeedlotfi.robikaTest.ui.dependencyInjection.UserInterfaceComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, DataModule::class]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: Application): Builder

    }

    fun getUserInterfaceComponent(): UserInterfaceComponent.Factory

}