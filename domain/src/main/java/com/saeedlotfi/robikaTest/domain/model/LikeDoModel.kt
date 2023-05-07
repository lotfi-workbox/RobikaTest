package com.saeedlotfi.robikaTest.domain.model

class LikeDoModel private constructor(
    var creatorId: Long,
    var targetId: Long,
    var targetType: Target,
    var id: Long
) {

    enum class Target {
        POST, COMMENT
    }

    companion object Factory {
        fun create(
            creatorId: Long,
            targetId: Long,
            targetType: Target = Target.POST,
            id: Long = "${creatorId}${targetId}${targetType.name}".hashCode().toLong()
        ): LikeDoModel {
            return LikeDoModel(
                creatorId = creatorId,
                targetId = targetId,
                targetType = targetType,
                id = id
            )
        }
    }

}