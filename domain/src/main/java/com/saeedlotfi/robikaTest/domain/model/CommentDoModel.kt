package com.saeedlotfi.robikaTest.domain.model

import com.saeedlotfi.robikaTest.domain._common.ErrorTypes

class CommentDoModel private constructor(
    text: String,
    time: Long,
    var creatorId: Long,
    var postId: Long,
    var likes: MutableList<LikeDoModel>,
    var id: Long
) {

    var text: String = validateText(text)
        set(value) {
            field = validateText(value)
        }

    private fun validateText(value: String): String {
        return value.ifEmpty {
            throw ErrorTypes.ModelValidationException(
                modelName = this::class.qualifiedName!!,
                propertyName = this::text.name
            )
        }
    }

    var time: Long = validateTime(time)
        set(value) {
            field = validateTime(value)
        }

    private fun validateTime(value: Long): Long {
        return if (value > 0L) value else {
            throw ErrorTypes.ModelValidationException(
                modelName = this::class.qualifiedName!!,
                propertyName = this::time.name
            )
        }
    }

    companion object Factory {
        fun create(
            text: String,
            creatorId: Long,
            postId: Long,
            time: Long = System.currentTimeMillis(),
            likes: MutableList<LikeDoModel> = mutableListOf(),
            id: Long = "${creatorId}${postId}${time}".hashCode().toLong()
        ): CommentDoModel {
            return CommentDoModel(
                text = text,
                time = time,
                creatorId = creatorId,
                postId = postId,
                likes = likes,
                id = id
            )
        }
    }

}