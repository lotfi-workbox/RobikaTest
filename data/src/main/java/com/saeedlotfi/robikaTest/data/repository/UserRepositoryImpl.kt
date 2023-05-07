@file:Suppress("unused")

package com.saeedlotfi.robikaTest.data.repository

import com.saeedlotfi.robikaTest.data.database.RealmManager
import com.saeedlotfi.robikaTest.data.mapper.asDataModel
import com.saeedlotfi.robikaTest.data.mapper.asDomainModel
import com.saeedlotfi.robikaTest.data.model.UserDaModel
import com.saeedlotfi.robikaTest.domain.model.UserDoModel
import com.saeedlotfi.robikaTest.domain.repository.UserRepository
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val realmManager: RealmManager
) : UserRepository {

    override suspend fun getUserFromDb(userId: Long?): Flow<UserDoModel> {
        return flow {
            val query = realmManager.localInstance.where(UserDaModel::class.java)
            if (userId != null) query.equalTo(UserDaModel::id.name, userId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun insertOrUpdateUsersFromDb(users: Flow<UserDoModel>) {
        users.map { it.asDataModel() }
            .flowOn(Dispatchers.IO)
            .collect {
                realmManager.localInstance.executeTransactionAwait { db ->
                    db.insertOrUpdate(it)
                }
            }
    }

    override suspend fun deleteUserFromDb(userId: Long) {
        realmManager.localInstance.executeTransactionAwait { db ->
            db.where(UserDaModel::class.java).equalTo(UserDaModel::id.name, userId)
                .findFirst()?.deleteFromRealm()
        }
    }

}