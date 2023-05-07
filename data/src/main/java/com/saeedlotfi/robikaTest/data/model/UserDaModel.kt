package com.saeedlotfi.robikaTest.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserDaModel(

    var username: String? = null,

    var password: String? = null,

    var name: String? = null,

    var image: String? = null,

    var posts: RealmList<PostDaModel>? = null,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

) : RealmObject()
