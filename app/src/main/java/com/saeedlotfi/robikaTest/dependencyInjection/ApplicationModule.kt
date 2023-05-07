package com.saeedlotfi.robikaTest.dependencyInjection

import com.saeedlotfi.robikaTest.ui.dependencyInjection.UserInterfaceComponent
import dagger.Module

@Module(subcomponents = [UserInterfaceComponent::class])
class ApplicationModule