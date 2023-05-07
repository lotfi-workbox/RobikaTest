@file:Suppress("unused")

package com.saeedlotfi.robikaTest.data.repository

import com.saeedlotfi.robikaTest.data.database.RealmManager
import com.saeedlotfi.robikaTest.data.mapper.asDataModel
import com.saeedlotfi.robikaTest.data.mapper.asDomainModel
import com.saeedlotfi.robikaTest.data.model.PostDaModel
import com.saeedlotfi.robikaTest.domain.model.PostDoModel
import com.saeedlotfi.robikaTest.domain.repository.PostRepository
import io.realm.Sort
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val realmManager: RealmManager
) : PostRepository {

    override suspend fun getPostFromDb(postId: Long?): Flow<PostDoModel> {
        return flow {
            val query = realmManager.localInstance.where(PostDaModel::class.java)
            if (postId != null) query.equalTo(PostDaModel::id.name, postId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun getPostByPageFromDb(page: Int, pageSize: Int): Flow<PostDoModel> {
        return flow {
            val query = realmManager.localInstance.where(PostDaModel::class.java)
            val listSize = query.count()

            //do not let load more than list size
            if (ceil(listSize / pageSize.toDouble()) < page) return@flow

            //load last page if size of it is smaller than page size
            val finalPageSize = if (page * pageSize <= listSize)
                pageSize.toLong() else (listSize % pageSize)

            //realm does not have offset function
            //the only way of paging is use sort and limit
            emitAll(
                query.sort(PostDaModel::time.name, Sort.DESCENDING).limit(page * pageSize.toLong())
                    .sort(PostDaModel::time.name, Sort.ASCENDING).limit(finalPageSize)
                    .sort(PostDaModel::time.name, Sort.DESCENDING)
                    .findAll().asFlow()
            )
        }.map { it.asDomainModel() }
    }

    override suspend fun getPostsByCreatorIdFromDb(creatorId: Long): Flow<PostDoModel> {
        return flow {
            val query = realmManager.localInstance.where(PostDaModel::class.java)
            query.equalTo(PostDaModel::creatorId.name, creatorId)
            emitAll(query.findAllAsync().asFlow())
        }.map { it.asDomainModel() }
    }

    override suspend fun insertOrUpdatePostsFromDb(posts: Flow<PostDoModel>) {
        posts.map { it.asDataModel() }
            .flowOn(Dispatchers.IO)
            .collect {
                realmManager.localInstance.executeTransactionAwait { db ->
                    db.insertOrUpdate(it)
                }
            }
    }

    override suspend fun deletePostFromDb(postId: Long) {
        realmManager.localInstance.executeTransactionAwait { db ->
            db.where(PostDaModel::class.java).equalTo(PostDaModel::id.name, postId)
                .findFirst()?.deleteFromRealm()
        }
    }

}