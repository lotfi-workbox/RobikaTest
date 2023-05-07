package com.saeedlotfi.robikaTest.domain.useCase

import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import com.saeedlotfi.robikaTest.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow

class GetLikesByCommentIdFromDbUseCase(
    private val loginRepository: LikeRepository
) {
    suspend operator fun invoke(commentId: Long): Flow<LikeDoModel> =
        loginRepository.getLikesByCommentIdFromDb(commentId)
}

class GetLikesByPostIdFromDbUseCase(
    private val loginRepository: LikeRepository
) {
    suspend operator fun invoke(postId: Long): Flow<LikeDoModel> =
        loginRepository.getLikesByPostIdFromDb(postId)
}

class GetLikesByCreatorIdFromDbUseCase(
    private val loginRepository: LikeRepository
) {
    suspend operator fun invoke(creatorId: Long): Flow<LikeDoModel> =
        loginRepository.getLikesByCreatorIdFromDb(creatorId)
}

class DeleteLikeFromDbUseCase(
    private val likeRepository: LikeRepository
) {
    suspend operator fun invoke(likeId: Long) =
        likeRepository.deleteLikeFromDb(likeId)
}