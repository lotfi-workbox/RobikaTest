@file:Suppress("unused")

package com.saeedlotfi.robikaTest.data.repository

import com.saeedlotfi.robikaTest.data.database.RealmManager
import com.saeedlotfi.robikaTest.data.mapper.asDataModel
import com.saeedlotfi.robikaTest.data.mapper.asDomainModel
import com.saeedlotfi.robikaTest.data.model.CommentDaModel
import com.saeedlotfi.robikaTest.domain.model.CommentDoModel
import com.saeedlotfi.robikaTest.domain.repository.CommentRepository
import io.realm.Sort
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val realmManager: RealmManager
) : CommentRepository {

    override suspend fun getCommentsByPostIdAndPartFromDb(
        postId: Long,
        part: Int,
        partSize: Int
    ): Flow<CommentDoModel> {
        return flow {
            val query = realmManager.localInstance.where(CommentDaModel::class.java)
                .equalTo(CommentDaModel::postId.name, postId)
            val listSize = query.count()

            //do not let load more than list size
            if (ceil(listSize / partSize.toDouble()) < part) return@flow

            //load last page if size of it is smaller than page size
            val finalPageSize = if (part * partSize <= listSize)
                partSize.toLong() else (listSize % partSize)

            //realm does not have offset function
            //the only way of paging is use sort and limit
            emitAll(
                query.sort(CommentDaModel::time.name, Sort.DESCENDING).limit(part * partSize.toLong())
                    .sort(CommentDaModel::time.name, Sort.ASCENDING).limit(finalPageSize)
                    .sort(CommentDaModel::time.name, Sort.DESCENDING)
                    .findAll().asFlow()
            )
        }.map { it.asDomainModel() }
    }

    override suspend fun getCommentsByCreatorIdFromDb(creatorId: Long): Flow<CommentDoModel> {
        return flow {
            val query = realmManager.localInstance.where(CommentDaModel::class.java)
            query.equalTo(CommentDaModel::creatorId.name, creatorId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun insertOrUpdateCommentsFromDb(posts: Flow<CommentDoModel>) {
        posts.map { it.asDataModel() }
            .flowOn(Dispatchers.IO)
            .collect {
                realmManager.localInstance.executeTransactionAwait { db ->
                    db.insertOrUpdate(it)
                }
            }
    }

    override suspend fun deleteCommentFromDb(commentId: Long) {
        realmManager.localInstance.executeTransactionAwait { db ->
            db.where(CommentDaModel::class.java).equalTo(CommentDaModel::id.name, commentId)
                .findFirst()?.deleteFromRealm()
        }
    }

}