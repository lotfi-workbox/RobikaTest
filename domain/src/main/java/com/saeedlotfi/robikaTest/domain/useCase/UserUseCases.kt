package com.saeedlotfi.robikaTest.domain.useCase

import com.saeedlotfi.robikaTest.domain.model.UserDoModel
import com.saeedlotfi.robikaTest.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserFromDbUseCase(
    private val loginRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long? = null): Flow<UserDoModel> =
        loginRepository.getUserFromDb(userId)
}

class InsertOrUpdateUsersFromDbUseCase(
    private val loginRepository: UserRepository
) {
    suspend operator fun invoke(users: Flow<UserDoModel>) =
        loginRepository.insertOrUpdateUsersFromDb(users)
}

class DeleteUserFromDbUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long) =
        userRepository.deleteUserFromDb(userId)
}