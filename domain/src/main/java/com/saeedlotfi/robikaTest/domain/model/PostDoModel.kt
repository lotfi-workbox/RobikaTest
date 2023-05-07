package com.saeedlotfi.robikaTest.domain.model

import com.saeedlotfi.robikaTest.domain._common.ErrorTypes

class PostDoModel private constructor(
    image: String,
    caption: String,
    time: Long,
    var creatorId: Long,
    var comments: MutableList<CommentDoModel>,
    var likes: MutableList<LikeDoModel>,
    var id: Long
) {

    var image: String = validateImage(image)
        set(value) {
            field = validateImage(value)
        }

    private fun validateImage(value: String): String {
        return value.ifEmpty {
            throw ErrorTypes.ModelValidationException(
                modelName = this::class.qualifiedName!!,
                propertyName = this::image.name
            )
        }
    }

    var caption: String = validateCaption(caption)
        set(value) {
            field = validateCaption(value)
        }

    private fun validateCaption(value: String): String {
        return value.ifEmpty {
            throw ErrorTypes.ModelValidationException(
                modelName = this::class.qualifiedName!!,
                propertyName = this::caption.name
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
            image: String,
            caption: String,
            creatorId: Long,
            time: Long = System.currentTimeMillis(),
            comments: MutableList<CommentDoModel> = mutableListOf(),
            likes: MutableList<LikeDoModel> = mutableListOf(),
            id: Long = "${creatorId}${time}".hashCode().toLong()
        ): PostDoModel {
            return PostDoModel(
                image = image,
                caption = caption,
                time = time,
                creatorId = creatorId,
                comments = comments,
                likes = likes,
                id = id
            )
        }
    }

}