package com.saeedlotfi.robikaTest.domain.model

import com.saeedlotfi.robikaTest.domain._common.ErrorTypes

class UserDoModel private constructor(
    username: String,
    password: String,
    var name: String,
    var image: String,
    var posts: MutableList<PostDoModel>,
    var id: Long
) {

    var username: String = validateUsernameAndInitId(username)
        set(value) {
            field = validateUsernameAndInitId(value)
        }

    private fun validateUsernameAndInitId(value: String): String {
        return if (value.length > MIN_LENGTH_USERNAME) value
        else throw ErrorTypes.ModelValidationException(
            modelName = this::class.qualifiedName!!,
            propertyName = this::username.name
        )
    }

    var password: String = validatePassword(password)
        set(value) {
            field = validatePassword(value)
        }

    private fun validatePassword(value: String): String {
        return if (value.length > MIN_LENGTH_PASSWORD) value
        else throw ErrorTypes.ModelValidationException(
            modelName = this::class.qualifiedName!!,
            propertyName = this::password.name
        )
    }

    companion object {
        const val MIN_LENGTH_USERNAME = 3
        const val MIN_LENGTH_PASSWORD = 8

        fun create(
            username: String,
            password: String,
            name: String = "user",
            image: String = "default",
            posts: MutableList<PostDoModel> = mutableListOf(),
            id: Long = System.currentTimeMillis()
        ): UserDoModel {
            return UserDoModel(
                username = username,
                password = password,
                name = name,
                image = image,
                posts = posts,
                id = id
            )
        }

    }

}