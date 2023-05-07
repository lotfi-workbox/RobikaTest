@file:Suppress("unused")

package com.saeedlotfi.robikaTest.data.repository

import com.saeedlotfi.robikaTest.data.database.RealmManager
import com.saeedlotfi.robikaTest.data.mapper.asDomainModel
import com.saeedlotfi.robikaTest.data.model.LikeDaModel
import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import com.saeedlotfi.robikaTest.domain.repository.LikeRepository
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikeRepositoryImpl @Inject constructor(
    private val realmManager: RealmManager
) : LikeRepository {

    override suspend fun getLikesByCommentIdFromDb(commentId: Long): Flow<LikeDoModel> {
        return flow {
            val query = realmManager.localInstance.where(LikeDaModel::class.java)
                .equalTo(LikeDaModel::targetType.name, LikeDoModel.Target.COMMENT.name)
                .equalTo(LikeDaModel::targetId.name, commentId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun getLikesByPostIdFromDb(postId: Long): Flow<LikeDoModel> {
        return flow {
            val query = realmManager.localInstance.where(LikeDaModel::class.java)
                .equalTo(LikeDaModel::targetType.name, LikeDoModel.Target.POST.name)
                .equalTo(LikeDaModel::targetId.name, postId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun getLikesByCreatorIdFromDb(creatorId: Long): Flow<LikeDoModel> {
        return flow {
            val query = realmManager.localInstance.where(LikeDaModel::class.java)
            query.equalTo(LikeDaModel::creatorId.name, creatorId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun deleteLikeFromDb(likeId: Long) {
        realmManager.localInstance.executeTransactionAwait { db ->
            db.where(LikeDaModel::class.java).equalTo(LikeDaModel::id.name, likeId)
                .findFirst()?.deleteFromRealm()
        }
    }

}