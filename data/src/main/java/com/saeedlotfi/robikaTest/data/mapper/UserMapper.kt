package com.saeedlotfi.robikaTest.data.mapper

import com.saeedlotfi.robikaTest.data.model.PostDaModel
import com.saeedlotfi.robikaTest.data.model.UserDaModel
import com.saeedlotfi.robikaTest.domain.model.UserDoModel
import io.realm.RealmList

fun UserDaModel.asDomainModel(): UserDoModel {
    return UserDoModel.create(
        username = username!!,
        password = password!!,
        name = name!!,
        image = image!!,
        posts = posts!!.map { it.asDomainModel() }.toMutableList(),
        id = id
    )
}

fun UserDoModel.asDataModel(): UserDaModel {
    return UserDaModel(
        username = username,
        password = password,
        name = name,
        image = image,
        posts = RealmList<PostDaModel>().also {
            it.addAll(posts.map { item -> item.asDataModel() })
        },
        id = id
    )
}