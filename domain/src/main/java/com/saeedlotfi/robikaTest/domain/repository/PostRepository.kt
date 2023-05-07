package com.saeedlotfi.robikaTest.domain.repository

import com.saeedlotfi.robikaTest.domain.model.PostDoModel
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    //when postId == null returns all Posts
    suspend fun getPostFromDb(postId: Long? = null): Flow<PostDoModel>

    suspend fun getPostByPageFromDb(page : Int,  pageSize: Int): Flow<PostDoModel>

    suspend fun getPostsByCreatorIdFromDb(creatorId: Long): Flow<PostDoModel>

    suspend fun insertOrUpdatePostsFromDb(posts: Flow<PostDoModel>)

    suspend fun deletePostFromDb(postId: Long)

}