package com.saeedlotfi.robikaTest.ui.dependencyInjection.module

import com.saeedlotfi.robikaTest.domain.repository.*
import com.saeedlotfi.robikaTest.domain.useCase.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    //region CommentUseCases

    @Provides
    fun getCommentsByPostIdFromDbUseCase(
        commentRepository: CommentRepository
    ) = GetCommentsByPostIdAndPartFromDbUseCase(commentRepository)

    @Provides
    fun getCommentsByCreatorIdFromDbUseCase(
        commentRepository: CommentRepository
    ) = GetCommentsByCreatorIdFromDbUseCase(commentRepository)

    @Provides
    fun insertOrUpdateCommentsFromDbUseCase(
        commentRepository: CommentRepository
    ) = InsertOrUpdateCommentsFromDbUseCase(commentRepository)

    @Provides
    fun deleteCommentFromDbUseCase(
        commentRepository: CommentRepository
    ) = DeleteCommentFromDbUseCase(commentRepository)

    //endregion CommentUseCases

    //region LikeUseCases

    @Provides
    fun getLikesByCommentIdFromDbUseCase(
        likeRepository: LikeRepository
    ) = GetLikesByCommentIdFromDbUseCase(likeRepository)

    @Provides
    fun getLikesByPostIdFromDbUseCase(
        likeRepository: LikeRepository
    ) = GetLikesByPostIdFromDbUseCase(likeRepository)

    @Provides
    fun getLikesByCreatorIdFromDbUseCase(
        likeRepository: LikeRepository
    ) = GetLikesByCreatorIdFromDbUseCase(likeRepository)

    @Provides
    fun deleteLikeFromDbUseCase(
        likeRepository: LikeRepository
    ) = DeleteLikeFromDbUseCase(likeRepository)

    //endregion LikeUseCases

    //region PostUseCases

    @Provides
    fun getPostFromDbUseCase(
        postRepository: PostRepository
    ) = GetPostFromDbUseCase(postRepository)

    @Provides
    fun getPostByPageFromDbUseCase(
        postRepository: PostRepository
    ) = GetPostByPageFromDbUseCase(postRepository)

    @Provides
    fun getPostsByCreatorIdFromDbUseCase(
        postRepository: PostRepository
    ) = GetPostsByCreatorIdFromDbUseCase(postRepository)

    @Provides
    fun insertOrUpdatePostsFromDbUseCase(
        postRepository: PostRepository
    ) = InsertOrUpdatePostsFromDbUseCase(postRepository)

    @Provides
    fun deletePostFromDbUseCase(
        postRepository: PostRepository
    ) = DeletePostFromDbUseCase(postRepository)

    //endregion PostUseCases

    //region UserUseCases

    @Provides
    fun getUserFromDbUseCase(
        userRepository: UserRepository
    ) = GetUserFromDbUseCase(userRepository)

    @Provides
    fun insertOrUpdateUsersFromDbUseCase(
        userRepository: UserRepository
    ) = InsertOrUpdateUsersFromDbUseCase(userRepository)

    @Provides
    fun deleteUserFromDbUseCase(
        userRepository: UserRepository
    ) = DeleteUserFromDbUseCase(userRepository)

    //endregion UserUseCases

}