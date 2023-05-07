package com.saeedlotfi.robikaTest.data.mapper

import com.saeedlotfi.robikaTest.data.model.CommentDaModel
import com.saeedlotfi.robikaTest.data.model.LikeDaModel
import com.saeedlotfi.robikaTest.domain.model.CommentDoModel
import io.realm.RealmList

fun CommentDaModel.asDomainModel(): CommentDoModel {
    return CommentDoModel.create(
        text = text!!,
        time = time!!,
        creatorId = creatorId!!,
        postId = postId!!,
        likes = likes!!.map { it.asDomainModel() }.toMutableList(),
        id = id
    )
}

fun CommentDoModel.asDataModel(): CommentDaModel {
    return CommentDaModel(
        text = text,
        time = time,
        creatorId = creatorId,
        postId = postId,
        likes = RealmList<LikeDaModel>().also {
            it.addAll(likes.map { item -> item.asDataModel() })
        },
        id = id
    )
}