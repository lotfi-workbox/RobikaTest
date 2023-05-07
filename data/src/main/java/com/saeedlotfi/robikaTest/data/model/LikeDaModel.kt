package com.saeedlotfi.robikaTest.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LikeDaModel(

    var creatorId: Long? = null,

    var targetId: Long? = null,

    var targetType: String? = null,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

) : RealmObject()