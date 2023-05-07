package com.saeedlotfi.robikaTest.data.database

import android.content.Context
import com.saeedlotfi.robikaTest.data.R
import com.saeedlotfi.robikaTest.data.model.UserDaModel
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class RealmManager @Inject constructor() {

    companion object {
        fun initAndConfig(context: Context) {
            Realm.init(context)
            val config = RealmConfiguration.Builder()
                .schemaVersion(1)
                .initialData {
                    it.importDataFromJson<UserDaModel>(
                        context.resources,
                        id = intArrayOf(
                            R.raw.current_user,
                            R.raw.first_user,
                            R.raw.second_user,
                            R.raw.third_user
                        )
                    )
                }
                .build()
            Realm.setDefaultConfiguration(config)
        }
    }

    private val localRealms = ThreadLocal<Realm?>()

    val localInstance: Realm
        get() = localRealms.get() ?: openInstance()

    private fun openInstance(): Realm {
        checkDefaultConfiguration()
        val realm = Realm.getDefaultInstance()
        if (localRealms.get() == null) {
            localRealms.set(realm)
        }
        return realm
    }

    private fun checkDefaultConfiguration() {
        checkNotNull(Realm.getDefaultConfiguration()) { "No default configuration is set." }
    }
}