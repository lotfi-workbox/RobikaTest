@file:Suppress("NOTHING_TO_INLINE")

import model.Module

val appModule =  Module(
    name = "app",
    path = ":app",
    namespace = AppConfig.applicationId
)

val dataModule =  Module(
    name = "data",
    path = ":data",
    namespace = "${AppConfig.applicationId}.data"
)

val domainModule = Module(
    name = "domain",
    path = ":domain",
    namespace = "${AppConfig.applicationId}.domain"
)

val uiModule = Module(
    name = "ui",
    path = ":ui",
    namespace = "${AppConfig.applicationId}.ui"
)

