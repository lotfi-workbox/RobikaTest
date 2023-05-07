package com.saeedlotfi.robikaTest.data.database

import android.content.res.Resources
import io.realm.Realm
import io.realm.RealmAsyncTask
import io.realm.RealmObject

inline fun <reified T : RealmObject> Realm.importDataFromJson(
    resources: Resources,
    vararg id: Int
): RealmAsyncTask = executeTransactionAsync { db ->
    id.forEach {
        val inputStream = resources.openRawResource(it)
        db.createAllFromJson(T::class.java, inputStream)
    }
}
