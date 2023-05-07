package com.saeedlotfi.robikaTest.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CommentDaModel(

    var text: String? = null,

    var time: Long? = null,

    var creatorId: Long? = null,

    var postId: Long? = null,

    var likes: RealmList<LikeDaModel>? = null,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

) : RealmObject()