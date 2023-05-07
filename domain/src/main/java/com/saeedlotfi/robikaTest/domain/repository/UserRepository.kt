package com.saeedlotfi.robikaTest.domain.repository

import com.saeedlotfi.robikaTest.domain.model.UserDoModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    //when id == null returns all Users
    suspend fun getUserFromDb(userId: Long?): Flow<UserDoModel>

    suspend fun insertOrUpdateUsersFromDb(users: Flow<UserDoModel>)

    suspend fun deleteUserFromDb(userId: Long)

}