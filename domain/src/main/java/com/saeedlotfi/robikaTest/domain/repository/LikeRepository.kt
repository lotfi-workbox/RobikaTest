package com.saeedlotfi.robikaTest.domain.repository

import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import kotlinx.coroutines.flow.Flow

interface LikeRepository {

    suspend fun getLikesByCommentIdFromDb(commentId: Long): Flow<LikeDoModel>

    suspend fun getLikesByPostIdFromDb(postId: Long): Flow<LikeDoModel>

    suspend fun getLikesByCreatorIdFromDb(creatorId: Long): Flow<LikeDoModel>

    suspend fun deleteLikeFromDb(likeId: Long)

}