package com.saeedlotfi.robikaTest.data.mapper

import com.saeedlotfi.robikaTest.data.model.LikeDaModel
import com.saeedlotfi.robikaTest.domain.model.LikeDoModel

fun LikeDaModel.asDomainModel(): LikeDoModel {
    return LikeDoModel.create(
        creatorId = creatorId!!,
        targetId = targetId!!,
        targetType = enumValueOf(targetType!!),
        id = id
    )
}

fun LikeDoModel.asDataModel(): LikeDaModel {
    return LikeDaModel(
        creatorId = creatorId,
        targetId = targetId,
        targetType = targetType.name,
        id = id
    )
}