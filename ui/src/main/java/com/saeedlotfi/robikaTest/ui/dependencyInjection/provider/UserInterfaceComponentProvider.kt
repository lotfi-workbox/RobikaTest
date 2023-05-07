package com.saeedlotfi.robikaTest.ui.dependencyInjection.provider

import com.saeedlotfi.robikaTest.ui.dependencyInjection.UserInterfaceComponent

interface UserInterfaceComponentProvider {
    fun getUserInterfaceComponent(): UserInterfaceComponent
}
