package com.saeedlotfi.robikaTest.domain.repository

import com.saeedlotfi.robikaTest.domain.model.CommentDoModel
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun getCommentsByPostIdAndPartFromDb(
        postId: Long,
        part: Int,
        partSize: Int
    ): Flow<CommentDoModel>

    suspend fun getCommentsByCreatorIdFromDb(creatorId: Long): Flow<CommentDoModel>

    suspend fun insertOrUpdateCommentsFromDb(posts: Flow<CommentDoModel>)

    suspend fun deleteCommentFromDb(commentId: Long)

}