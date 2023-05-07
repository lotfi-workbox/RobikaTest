import model.Dependency

object Dependencies {

    object Androidx {

        val coreKtx = Dependency(
            name = "core-ktx",
            packageName = "androidx.core",
            version = Constants.coreKtxVersion
        )

        val appCompat = Dependency(
            name = "appcompat",
            packageName = "androidx.appcompat",
            version = Constants.appCompatVersion
        )

        val media = Dependency(
            name = "media",
            packageName = "androidx.media",
            version = Constants.mediaVersion
        )

        val datastorePreferences = Dependency(
            name = "datastore-preferences",
            packageName = "androidx.datastore",
            version = Constants.datastorePreferencesVersion
        )

        object Compose {

            val ui = Dependency(
                name = "ui",
                packageName = "androidx.compose.ui",
                version = Constants.composeVersion
            )

            val material3 = Dependency(
                name = "material3",
                packageName = "androidx.compose.material3",
                version = Constants.composeMaterial3Version
            )

            val activity = Dependency(
                name = "activity-compose",
                packageName = "androidx.activity",
                version = Constants.composeActivityVersion
            )

            val navigation = Dependency(
                name = "navigation-compose",
                packageName = "androidx.navigation",
                version = Constants.composeNavigationVersion
            )

            val coil = Dependency(
                name = "coil-compose",
                packageName = "io.coil-kt",
                version = Constants.coilVersion
            )

            object Lifecycle {

                val runtime = Dependency(
                    name = "lifecycle-runtime-compose",
                    packageName = "androidx.lifecycle",
                    version = Constants.lifecycleVersion
                )


                val viewmodel = Dependency(
                    name = "lifecycle-viewmodel-compose",
                    packageName = "androidx.lifecycle",
                    version = Constants.lifecycleVersion
                )

            }

        }

        object Lifecycle {

            val viewModelKtx = Dependency(
                name = "lifecycle-viewmodel-ktx",
                packageName = "androidx.lifecycle",
                version = Constants.lifecycleVersion
            )

            val livedataKtx = Dependency(
                name = "lifecycle-livedata-ktx",
                packageName = "androidx.lifecycle",
                version = Constants.lifecycleVersion
            )

        }

    }

    object Google {

        val material = Dependency(
            name = "material",
            packageName = "com.google.android.material",
            version = Constants.materialVersion
        )

        object Dagger {

            val dagger = Dependency(
                name = "dagger",
                packageName = "com.google.dagger",
                version = Constants.daggerVersion
            )

            val androidProcessor = Dependency(
                name = "dagger-android-processor",
                packageName = "com.google.dagger",
                version = Constants.daggerVersion
            )

            val compiler = Dependency(
                name = "dagger-compiler",
                packageName = "com.google.dagger",
                version = Constants.daggerVersion
            )

        }

        object AutoValue {

            val autoValue = Dependency(
                name = "auto-value",
                packageName = "com.google.auto.value",
                version = Constants.autoValueVersion
            )

            val annotations = Dependency(
                name = "auto-value-annotations",
                packageName = "com.google.auto.value",
                version = Constants.autoValueVersion
            )

        }

    }

    object Jetbrains {

        object Coroutines {

            val core = Dependency(
                name = "kotlinx-coroutines-core",
                packageName = "org.jetbrains.kotlinx",
                version = Constants.coroutinesVersion
            )

            val android = Dependency(
                name = "kotlinx-coroutines-android",
                packageName = "org.jetbrains.kotlinx",
                version = Constants.coroutinesVersion
            )

            val test = Dependency(
                name = "kotlinx-coroutines-test",
                packageName = "org.jetbrains.kotlinx",
                version = Constants.coroutinesVersion
            )

        }

    }

    object Squareup {

        val loggingInterceptor = Dependency(
            name = "logging-interceptor",
            packageName = "com.squareup.okhttp3",
            version = Constants.loggingInterceptorVersion
        )

        object Retrofit2 {

            val retrofit = Dependency(
                name = "retrofit",
                packageName = "com.squareup.retrofit2",
                version = Constants.retrofitVersion
            )

            val converterGson = Dependency(
                name = "converter-gson",
                packageName = "com.squareup.retrofit2",
                version = Constants.retrofitVersion
            )

        }
    }

    object Build {

        val gradle = Dependency(
            name = "gradle",
            packageName = "com.android.tools.build",
            version = Constants.gradleVersion
        )

        val kotlinGradlePlugin = Dependency(
            name = "kotlin-gradle-plugin",
            packageName = "org.jetbrains.kotlin",
            version = Constants.kotlinVersion
        )

        //there is plugin in Plugins.kt
        val realmGradlePlugin = Dependency(
            name = "realm-gradle-plugin",
            packageName = "io.realm",
            version = Constants.realmVersion
        )

    }

    object Test {

        val kotlinTestJunit = Dependency(
            name = "kotlin-test-junit",
            packageName = "org.jetbrains.kotlin",
            version = Constants.kotlinVersion
        )

        object Androidx {

            object Compose {

                val junit4 = Dependency(
                    name = "ui-test-junit4",
                    packageName = "androidx.compose.ui",
                    version = Constants.composeVersion
                )

                val manifest = Dependency(
                    name = "ui-test-manifest",
                    packageName = "androidx.compose.ui",
                    version = Constants.composeVersion
                )

            }

            val coreKtx = Dependency(
                name = "core-ktx",
                packageName = "androidx.test",
                version = Constants.coreKtxTestVersion
            )

            val junitKtx = Dependency(
                name = "junit-ktx",
                packageName = "androidx.test.ext",
                version = Constants.junitKtxVersion
            )

        }

        val coroutines = Jetbrains.Coroutines.test

        val mockitoKotlin = Dependency(
            name = "mockito-kotlin",
            packageName = "org.mockito.kotlin",
            version = Constants.mockitoVersion
        )

    }

}



