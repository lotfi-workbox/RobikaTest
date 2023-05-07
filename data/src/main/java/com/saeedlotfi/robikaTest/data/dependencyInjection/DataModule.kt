package com.saeedlotfi.robikaTest.data.dependencyInjection

import com.saeedlotfi.robikaTest.data.repository.*
import com.saeedlotfi.robikaTest.domain.repository.*
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun userRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun postRepository(repository: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun commentRepository(repository: CommentRepositoryImpl): CommentRepository

    @Binds
    abstract fun likeRepository(repository: LikeRepositoryImpl): LikeRepository

}