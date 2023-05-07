plugins {
    androidLib
    kotlinAndroid
    kotlinKapt
    realmAndroid
}

android {
    namespace = dataModule.namespace
    compileSdk = AppConfig.compileSdkVersion
    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    //local
    implementation(project(domainModule.path))

    //region google
    //dagger
    implementation(Dependencies.Google.Dagger.dagger())
    kapt(Dependencies.Google.Dagger.androidProcessor())
    kapt(Dependencies.Google.Dagger.compiler())
    //endregion google

    //region test
    testImplementation(Dependencies.Test.coroutines())
    testImplementation(Dependencies.Test.kotlinTestJunit())
    testImplementation(Dependencies.Test.mockitoKotlin())
    //endregion test

}