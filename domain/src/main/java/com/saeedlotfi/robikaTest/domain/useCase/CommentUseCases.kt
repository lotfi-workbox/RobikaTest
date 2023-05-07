package com.saeedlotfi.robikaTest.domain.useCase

import com.saeedlotfi.robikaTest.domain.model.CommentDoModel
import com.saeedlotfi.robikaTest.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

class GetCommentsByPostIdAndPartFromDbUseCase(
    private val loginRepository: CommentRepository
) {
    suspend operator fun invoke(postId: Long, part: Int, partSize: Int = 10): Flow<CommentDoModel> =
        loginRepository.getCommentsByPostIdAndPartFromDb(postId, part, partSize)
}

class GetCommentsByCreatorIdFromDbUseCase(
    private val loginRepository: CommentRepository
) {
    suspend operator fun invoke(creatorId: Long): Flow<CommentDoModel> =
        loginRepository.getCommentsByCreatorIdFromDb(creatorId)
}

class InsertOrUpdateCommentsFromDbUseCase(
    private val loginRepository: CommentRepository
) {
    suspend operator fun invoke(comments: Flow<CommentDoModel>) =
        loginRepository.insertOrUpdateCommentsFromDb(comments)
}

class DeleteCommentFromDbUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: Long) =
        commentRepository.deleteCommentFromDb(commentId)
}