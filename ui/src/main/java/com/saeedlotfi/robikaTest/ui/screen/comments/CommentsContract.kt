package com.saeedlotfi.robikaTest.ui.screen.comments

import androidx.compose.runtime.Immutable
import com.saeedlotfi.robikaTest.domain.model.CommentDoModel
import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import com.saeedlotfi.robikaTest.ui.screen.posts.UserItem

@Immutable
data class CommentItem(
    val text: String,
    val creator: UserItem,
    val isLiked: Boolean,
    val postId: Long,
    val time: Long,
    //should converted to ui model
    val likes: List<LikeDoModel>,
    val id: Long
) {

    fun toDomain(): CommentDoModel {
        return CommentDoModel.create(
            text = text,
            creatorId = creator.id,
            postId = postId,
            time = time,
            likes = likes.toMutableList(),
            id = id
        )
    }

    companion object Factory {
        fun from(
            comment: CommentDoModel,
            isLiked: Boolean = false,
            user: UserItem? = null
        ): CommentItem {
            return CommentItem(
                text = comment.text,
                creator = user ?: UserItem(
                    name = "",
                    image = "",
                    id = comment.creatorId
                ),
                isLiked = isLiked,
                postId = comment.postId,
                time = comment.time,
                likes = comment.likes,
                id = comment.id
            )
        }
    }
}

@Immutable
sealed interface ViewIntent {
    data class LikeComment(val comment: CommentItem) : ViewIntent
    data class LoadMore(val postId: Long, val part: Int) : ViewIntent
    data class AddComment(val postId: Long, val comment: String) : ViewIntent
}

@Immutable
data class ViewState(
    val part: Int = 1,
    val caption: String,
    val comments: List<CommentItem>,
    val isLoading: Boolean,
    val error: Throwable?
)

internal sealed interface PartialStateChange {
    object Loading : PartialStateChange
    data class Like(val comment: CommentItem) : PartialStateChange
    data class Failure(val error: Throwable) : PartialStateChange
    data class AddNewComment(val newComment: CommentItem) : PartialStateChange
    data class ListChange(val newComments: List<CommentItem>) : PartialStateChange

    fun reduce(state: ViewState): ViewState = when (this) {
        is Failure -> state.copy(
            isLoading = false,
            error = error,
            caption = "",
            comments = listOf()
        )
        Loading -> state.copy(
            isLoading = true,
            error = null
        )
        is Like -> state.copy(
            isLoading = false,
            error = null,
            //add or remove new like to liked comment
            comments = state.comments.map { if (it.id == comment.id) comment else it }
        )
        is AddNewComment -> state.copy(
            isLoading = false,
            error = null,
            //add new comment to first of list
            comments = listOf(newComment) + state.comments,
        )
        is ListChange -> state.copy(
            part = state.part + 1,
            isLoading = false,
            error = null,
            //add new part of comments to old comments
            comments = state.comments + newComments,
        )
    }
}

sealed interface SingleEvent {
    data class LoadFailure(val error: Throwable) : SingleEvent
}
