package com.saeedlotfi.robikaTest.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.robikaTest.domain.model.CommentDoModel
import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import com.saeedlotfi.robikaTest.domain.useCase.*
import com.saeedlotfi.robikaTest.ui._common.FakeUserStore
import com.saeedlotfi.robikaTest.ui.screen.comments.*
import com.saeedlotfi.robikaTest.ui.screen.posts.UserItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val getUserFromDbUseCase: GetUserFromDbUseCase,
    private val getPostFromDbUseCase: GetPostFromDbUseCase,
    private val getCommentsByPostIdAndPartFromDbUseCase: GetCommentsByPostIdAndPartFromDbUseCase,
    private val insertOrUpdateCommentsFromDbUseCase: InsertOrUpdateCommentsFromDbUseCase,
    private val insertOrUpdatePostsFromDbUseCase: InsertOrUpdatePostsFromDbUseCase
) : ViewModel() {

    private val eventChannel = Channel<SingleEvent>(Channel.UNLIMITED)

    private val intentFlow = MutableSharedFlow<ViewIntent>(extraBufferCapacity = Int.MAX_VALUE)

    var viewState: StateFlow<ViewState>

    init {
        val initialVS = ViewState(caption = "", comments = listOf(), isLoading = true, error = null)

        viewState = intentFlow
            .toPartialStateChangeFlow()
            .onEach { eventChannel.trySend(it.toSingleEventOrNull() ?: return@onEach) }
            .scan(initialVS) { state, change -> change.reduce(state) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)
    }

    fun processIntent(intent: ViewIntent) {
        check(intentFlow.tryEmit(intent)) { "Failed to emit intent: $intent" }
    }

    private fun SharedFlow<ViewIntent>.toPartialStateChangeFlow(): Flow<PartialStateChange> {
        return merge(
            // load more comments
            filterIsInstance<ViewIntent.LoadMore>()
                .toLoadMoreChangeFlow(),
            // add comment to a post
            filterIsInstance<ViewIntent.AddComment>()
                .toAddCommentChangeFlow(),
            // like a comment
            filterIsInstance<ViewIntent.LikeComment>()
                .toLikeCommentChangeFlow()

        ).onStart { emit(PartialStateChange.Loading) }
    }

    private fun Flow<ViewIntent.LoadMore>.toLoadMoreChangeFlow(): Flow<PartialStateChange> {
        return map<_, PartialStateChange> { viewIntent ->
            PartialStateChange.ListChange(
                getCommentsByPostIdAndPartFromDbUseCase(
                    postId = viewIntent.postId,
                    part = viewIntent.part
                ).map { comment ->
                    //create ui model
                    val user = getUserFromDbUseCase(comment.creatorId).single()
                    val isLiked = comment.likes.any { it.creatorId == FakeUserStore.currentUser.id }
                    CommentItem.from(comment, isLiked, UserItem.from(user))
                }.toList()
            )
        }.catch {
            emit(PartialStateChange.Failure(it))
        }.onStart {
            emit(PartialStateChange.Loading)
        }
    }

    private fun Flow<ViewIntent.AddComment>.toAddCommentChangeFlow(): Flow<PartialStateChange> {
        return map<_, PartialStateChange> { viewIntent ->
            //add new comment
            val post = getPostFromDbUseCase(viewIntent.postId).single()
            val comment = CommentDoModel.create(
                text = viewIntent.comment,
                creatorId = FakeUserStore.currentUser.id,
                postId = viewIntent.postId
            )
            post.comments.add(comment)
            //insert to database and send viewState
            insertOrUpdatePostsFromDbUseCase(posts = flow { emit(post) })
            PartialStateChange.AddNewComment(
                newComment = CommentItem.from(
                    comment = comment,
                    user = FakeUserStore.currentUser
                )
            )
        }.catch {
            emit(PartialStateChange.Failure(it))
        }.onStart {
            emit(PartialStateChange.Loading)
        }
    }

    private fun Flow<ViewIntent.LikeComment>.toLikeCommentChangeFlow(): Flow<PartialStateChange> {
        return map<_, PartialStateChange> { viewIntent ->
            //find like
            val like = LikeDoModel.create(FakeUserStore.currentUser.id, viewIntent.comment.id)
            val index = viewIntent.comment.likes.indexOfFirst { it.creatorId == like.creatorId }
            val isLiked = index == -1
            //add or remove like from post(like or unlike)
            val likes = if (isLiked) viewIntent.comment.likes + like
            else viewIntent.comment.likes.filter { it.creatorId != like.creatorId }
            val post = viewIntent.comment.copy(likes = likes, isLiked = isLiked)
            //insert to database and send viewState
            insertOrUpdateCommentsFromDbUseCase(flow { emit(post.toDomain()) })
            PartialStateChange.Like(post)
        }.catch {
            emit(PartialStateChange.Failure(it))
        }.onStart {
            emit(PartialStateChange.Loading)
        }
    }

    companion object {
        private fun PartialStateChange.toSingleEventOrNull(): SingleEvent? = when (this) {
            is PartialStateChange.Failure -> SingleEvent.LoadFailure(error)
            else -> null
        }
    }

}