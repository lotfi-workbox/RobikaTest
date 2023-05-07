package com.saeedlotfi.robikaTest.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class PostDaModel(

    var image: String? = null,

    var caption: String? = null,

    var time: Long? = null,

    var creatorId: Long? = null,

    var comments: RealmList<CommentDaModel>? = null,

    var likes: RealmList<LikeDaModel>? = null,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

) : RealmObject()