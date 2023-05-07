package com.saeedlotfi.robikaTest

import android.app.Application
import com.saeedlotfi.robikaTest.data.database.RealmManager
import com.saeedlotfi.robikaTest.dependencyInjection.ApplicationComponent
import com.saeedlotfi.robikaTest.dependencyInjection.DaggerApplicationComponent
import com.saeedlotfi.robikaTest.ui.dependencyInjection.UserInterfaceComponent
import com.saeedlotfi.robikaTest.ui.dependencyInjection.provider.UserInterfaceComponentProvider

class RobikaTest : Application(),
    UserInterfaceComponentProvider {

    private var applicationComponent: ApplicationComponent =
        DaggerApplicationComponent.builder().application(this).build()

    override fun onCreate() {
        RealmManager.initAndConfig(this)
        super.onCreate()
    }

    override fun getUserInterfaceComponent(): UserInterfaceComponent {
        return applicationComponent.getUserInterfaceComponent().create()
    }

}

