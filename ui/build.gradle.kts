@file:Suppress("UnstableApiUsage")

plugins {
    androidLib
    kotlinAndroid
    kotlinKapt
}

android {
    namespace = uiModule.namespace
    compileSdk = AppConfig.compileSdkVersion
    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Constants.composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //local
    implementation(project(domainModule.path))

    //region androidx
    implementation(Dependencies.Androidx.coreKtx())
    implementation(Dependencies.Androidx.appCompat())
    //compose
    implementation(Dependencies.Androidx.Compose.ui())
    implementation(Dependencies.Androidx.Compose.material3())
    implementation(Dependencies.Androidx.Compose.activity())
    implementation(Dependencies.Androidx.Compose.navigation())
    implementation(Dependencies.Androidx.Compose.coil())
    implementation(Dependencies.Androidx.Compose.Lifecycle.runtime())
    implementation(Dependencies.Androidx.Compose.Lifecycle.viewmodel())
    //endregion androidx

    //region google
    implementation(Dependencies.Google.material())
    //dagger
    implementation(Dependencies.Google.Dagger.dagger())
    kapt(Dependencies.Google.Dagger.androidProcessor())
    kapt(Dependencies.Google.Dagger.compiler())
    //endregion google

    //region test
    testImplementation(Dependencies.Test.coroutines())
    testImplementation(Dependencies.Test.kotlinTestJunit())
    testImplementation(Dependencies.Test.mockitoKotlin())
    testImplementation(Dependencies.Test.Androidx.Compose.junit4())
    testImplementation(Dependencies.Test.Androidx.Compose.manifest())
    //endregion test

}