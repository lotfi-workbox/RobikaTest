package com.saeedlotfi.robikaTest.domain.useCase

import com.saeedlotfi.robikaTest.domain.model.PostDoModel
import com.saeedlotfi.robikaTest.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostFromDbUseCase(
    private val loginRepository: PostRepository
) {
    suspend operator fun invoke(postId: Long? = null): Flow<PostDoModel> =
        loginRepository.getPostFromDb(postId)
}

class GetPostByPageFromDbUseCase(
    private val loginRepository: PostRepository
) {
    suspend operator fun invoke(page: Int,  pageSize: Int = 10): Flow<PostDoModel> =
        loginRepository.getPostByPageFromDb(page, pageSize)
}

class GetPostsByCreatorIdFromDbUseCase(
    private val loginRepository: PostRepository
) {
    suspend operator fun invoke(creatorId: Long): Flow<PostDoModel> =
        loginRepository.getPostsByCreatorIdFromDb(creatorId)
}

class InsertOrUpdatePostsFromDbUseCase(
    private val loginRepository: PostRepository
) {
    suspend operator fun invoke(posts: Flow<PostDoModel>) =
        loginRepository.insertOrUpdatePostsFromDb(posts)
}

class DeletePostFromDbUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Long) =
        postRepository.deletePostFromDb(postId)
}