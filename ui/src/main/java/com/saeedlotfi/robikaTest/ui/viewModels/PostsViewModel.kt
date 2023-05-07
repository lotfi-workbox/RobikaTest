package com.saeedlotfi.robikaTest.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeedlotfi.robikaTest.domain._common.ErrorTypes.EmptyOutFlowException
import com.saeedlotfi.robikaTest.domain.model.LikeDoModel
import com.saeedlotfi.robikaTest.domain.useCase.GetPostByPageFromDbUseCase
import com.saeedlotfi.robikaTest.domain.useCase.GetPostFromDbUseCase
import com.saeedlotfi.robikaTest.domain.useCase.GetUserFromDbUseCase
import com.saeedlotfi.robikaTest.domain.useCase.InsertOrUpdatePostsFromDbUseCase
import com.saeedlotfi.robikaTest.ui._common.FakeUserStore
import com.saeedlotfi.robikaTest.ui.screen.posts.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PostsViewModel @Inject constructor(
    private val getUserFromDbUseCase: GetUserFromDbUseCase,
    private val GetPostFromDbUseCase: GetPostFromDbUseCase,
    private val getPostsByPageFromDbUseCase: GetPostByPageFromDbUseCase,
    private val insertOrUpdatePostsFromDbUseCase: InsertOrUpdatePostsFromDbUseCase
) : ViewModel() {

    private val eventChannel = Channel<SingleEvent>(Channel.UNLIMITED)

    private val intentFlow = MutableSharedFlow<ViewIntent>(extraBufferCapacity = Int.MAX_VALUE)

    var viewState: StateFlow<ViewState>

    init {
        val initialVS = ViewState(
            page = 1,
            posts = listOf(),
            isLoading = true,
            error = null,
            backState = null
        )

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
            // save back state
            filterIsInstance<ViewIntent.HandleBackState>()
                .map { PartialStateChange.HandleBackState(it.postId) },
            // refresh single post data
            filterIsInstance<ViewIntent.Refresh>()
                .toRefreshChangeFlow(),
            // load more posts
            filterIsInstance<ViewIntent.LoadMore>()
                .toLoadMoreChangeFlow(),
            // like a post
            filterIsInstance<ViewIntent.LikePost>()
                .toLikePostChangeFlow()
        ).onStart { emit(PartialStateChange.Loading) }
    }

    private fun Flow<ViewIntent.Refresh>.toRefreshChangeFlow(): Flow<PartialStateChange> {
        return map<_, PartialStateChange> { viewIntent ->
            val post = GetPostFromDbUseCase(viewIntent.postId).single()
            val creator = getUserFromDbUseCase(post.creatorId).single()
            val isLiked = post.likes.any { it.creatorId == FakeUserStore.currentUser.id }
            PartialStateChange.Refresh(PostItem.from(post, UserItem.from(creator), isLiked))
        }.catch {
            emit(PartialStateChange.Failure(it))
        }.onStart {
            emit(PartialStateChange.Loading)
        }
    }

    private fun Flow<ViewIntent.LoadMore>.toLoadMoreChangeFlow(): Flow<PartialStateChange> {
        return map<_, PartialStateChange> { viewIntent ->
            val newPosts = getPostsByPageFromDbUseCase(viewIntent.page).onEmpty {
                //if flow is empty retry after one second
                if (viewIntent.page == 1) throw EmptyOutFlowException
            }.retry(10) {
                delay(1000)
                it is EmptyOutFlowException
            }.map { post ->
                //create ui model
                val creator = getUserFromDbUseCase(post.creatorId).single()
                val isLiked = post.likes.any { it.creatorId == FakeUserStore.currentUser.id }
                PostItem.from(post = post, creator = UserItem.from(creator), isLiked = isLiked)
            }.toList()
            PartialStateChange.LoadMore(newPosts)
        }.catch {
            emit(PartialStateChange.Failure(it))
        }.onStart {
            emit(PartialStateChange.Loading)
        }
    }

    private fun Flow<ViewIntent.LikePost>.toLikePostChangeFlow(): Flow<PartialStateChange> {
        return map<_, PartialStateChange> { viewIntent ->
            //find like
            val like = LikeDoModel.create(FakeUserStore.currentUser.id, viewIntent.post.id)
            val index = viewIntent.post.likes.indexOfFirst { it.creatorId == like.creatorId }
            val isLiked = index == -1
            //add or remove like from post(like or unlike)
            val likes = if (isLiked) viewIntent.post.likes + like
            else viewIntent.post.likes.filter { it.creatorId != like.creatorId }
            val post = viewIntent.post.copy(likes = likes, isLiked = isLiked)
            //insert to database and send viewState
            insertOrUpdatePostsFromDbUseCase(flow { emit(post.toDomain()) })
            PartialStateChange.Refresh(post)
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

