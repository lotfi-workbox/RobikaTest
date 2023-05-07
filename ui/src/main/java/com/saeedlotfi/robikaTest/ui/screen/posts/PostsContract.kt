package com.saeedlotfi.robikaTest.ui.screen.posts

import androidx.compose.runtime.Immutable
import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import com.saeedlotfi.robikaTest.domain.model.PostDoModel
import com.saeedlotfi.robikaTest.domain.model.UserDoModel
import com.saeedlotfi.robikaTest.ui.screen.comments.CommentItem

@Immutable
data class PostItem(
    val image: String,
    val caption: String,
    val time: Long,
    val creator: UserItem,
    val isLiked: Boolean,
    val comments: List<CommentItem>,
    //should converted to ui model
    val likes: List<LikeDoModel>,
    val id: Long
) {

    fun toDomain(): PostDoModel {
        return PostDoModel.create(
            image = image,
            caption = caption,
            time = time,
            creatorId = creator.id,
            comments = comments.map { it.toDomain() }.toMutableList(),
            likes = likes.toMutableList(),
            id = id
        )
    }

    companion object Factory {
        fun from(
            post: PostDoModel,
            creator: UserItem,
            isLiked: Boolean
        ): PostItem {
            return PostItem(
                image = post.image,
                caption = post.caption,
                time = post.time,
                creator = creator,
                isLiked = isLiked,
                comments = post.comments.map { CommentItem.from(it) },
                likes = post.likes,
                id = post.id
            )
        }
    }
}

//we don't need sensitive properties
@Immutable
data class UserItem(
    val name: String,
    val image: String,
    val id: Long
) {
    companion object Factory {
        fun from(user: UserDoModel): UserItem {
            return UserItem(
                name = user.name,
                image = user.image,
                id = user.id
            )
        }
    }
}

@Immutable
data class BackState(val postId: Long)

@Immutable
sealed interface ViewIntent {
    data class LikePost(val post: PostItem) : ViewIntent
    data class Refresh(val postId: Long) : ViewIntent
    data class LoadMore(val page: Int) : ViewIntent
    data class HandleBackState(val postId: Long) : ViewIntent
}

@Immutable
data class ViewState(
    val page: Int = 1,
    val posts: List<PostItem>,
    val isLoading: Boolean,
    val error: Throwable?,
    val backState: BackState?,
)

internal sealed interface PartialStateChange {
    object Loading : PartialStateChange
    data class Refresh(val post: PostItem) : PartialStateChange
    data class Failure(val error: Throwable) : PartialStateChange
    data class LoadMore(val newPosts: List<PostItem>) : PartialStateChange
    data class HandleBackState(val postId: Long) : PartialStateChange

    fun reduce(state: ViewState): ViewState = when (this) {
        is Failure -> state.copy(
            isLoading = false,
            error = error,
            posts = listOf()
        )
        Loading -> state.copy(
            isLoading = true,
            error = null
        )
        is Refresh -> state.copy(
            isLoading = false,
            error = null,
            backState = null,
            //add or remove new like to liked post
            posts = state.posts.map { if (it.id == post.id) post else it }
        )
        is LoadMore -> state.copy(
            page = state.page + 1,
            isLoading = false,
            error = null,
            //add new page posts to old posts
            posts = state.posts + newPosts,
        )
        is HandleBackState -> state.copy(
            isLoading = false,
            error = null,
            //add new page posts to old posts
            backState = BackState(postId),
        )
    }
}

sealed interface SingleEvent {
    data class LoadFailure(val error: Throwable) : SingleEvent
}
