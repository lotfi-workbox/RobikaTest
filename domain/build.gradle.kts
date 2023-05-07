plugins {
    javaLib
    kotlin
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Dependencies.Jetbrains.Coroutines.core())
    //region test
    testImplementation(Dependencies.Test.coroutines())
    testImplementation(Dependencies.Test.kotlinTestJunit())
    testImplementation(Dependencies.Test.mockitoKotlin())
    //endregion test
}