package com.saeedlotfi.robikaTest.data.mapper

import com.saeedlotfi.robikaTest.data.model.CommentDaModel
import com.saeedlotfi.robikaTest.data.model.LikeDaModel
import com.saeedlotfi.robikaTest.data.model.PostDaModel
import com.saeedlotfi.robikaTest.domain.model.PostDoModel
import io.realm.RealmList

fun PostDaModel.asDomainModel(): PostDoModel {
    return PostDoModel.create(
        image = image!!,
        caption = caption!!,
        time = time!!,
        creatorId = creatorId!!,
        comments = comments!!.map { it.asDomainModel() }.toMutableList(),
        likes = likes!!.map { it.asDomainModel() }.toMutableList(),
        id = id
    )
}

fun PostDoModel.asDataModel(): PostDaModel {
    return PostDaModel(
        image = image,
        caption = caption,
        time = time,
        creatorId = creatorId,
        comments = RealmList<CommentDaModel>().also {
            it.addAll(comments.map { item -> item.asDataModel() })
        },
        likes = RealmList<LikeDaModel>().also {
            it.addAll(likes.map { item -> item.asDataModel() })
        },
        id = id
    )
}