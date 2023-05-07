package com.saeedlotfi.robikaTest.ui.screen.posts

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.saeedlotfi.robikaTest.ui.R
import com.saeedlotfi.robikaTest.ui.viewModels.PostsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@Composable
internal fun PostsScreen(
    viewModel: PostsViewModel,
    navigateToComments: (postId: Long) -> Unit,
) {

    //ViewIntent & States
    val viewIntentChannel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }
    val viewState = viewModel.viewState.collectAsStateWithLifecycle()

    //Init ViewIntent Channel
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            viewIntentChannel.consumeAsFlow().onEach(viewModel::processIntent).collect()
        }
    }

    //Handle Back State
    if (viewState.value.backState != null) viewIntentChannel
        .trySend(ViewIntent.Refresh(postId = viewState.value.backState!!.postId))

    //RootView
    if (viewState.value.isLoading) Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }

    PostList(
        posts = viewState.value.posts,
        onLoadNextPage = {
            viewIntentChannel.trySend(ViewIntent.LoadMore(viewState.value.page))
        },
        onLike = { post ->
            viewIntentChannel.trySend(ViewIntent.LikePost(post))
        },
        onComment = { postId ->
            viewIntentChannel.trySend(ViewIntent.HandleBackState(postId))
            navigateToComments(postId)
        }
    )

}

@Composable
fun PostList(
    posts: List<PostItem>,
    onLoadNextPage: () -> Unit,
    onLike: (post: PostItem) -> Unit,
    onComment: (postId: Long) -> Unit,
) {
    val scrollState = rememberLazyListState()

    fun LazyListState.isScrolledToEnd() =
        layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

    val endOfListReached by remember { derivedStateOf { scrollState.isScrolledToEnd() } }

    LaunchedEffect(endOfListReached) {
        onLoadNextPage()
    }

    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(count = posts.size, key = { posts[it].id }) { index ->

            Post(
                post = posts[index],
                onLike = onLike,
                onComment = onComment
            )
        }

    }

}

@Composable
internal fun Post(
    modifier: Modifier = Modifier,
    post: PostItem,
    onLike: (post: PostItem) -> Unit,
    onComment: (postId: Long) -> Unit,
) {
    Card(
        modifier = modifier.padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        )
        {

            Image(
                painter = rememberAsyncImagePainter(model = post.creator.image),
                contentDescription = null,
                modifier = modifier
                    .padding(10.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )

            Text(
                modifier = modifier.padding(start = 10.dp, top = 5.dp),
                text = post.creator.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

        }

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = post.image),
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentDescription = "post image",
                contentScale = ContentScale.Crop
            )

            LikeButtonWithTitle(
                post = post,
                onLike = { onLike(post) }
            )

            TextButton(
                onClick = { onComment(post.id) },
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment),
                        contentDescription = "comments",
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${post.comments.size} Comments",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
            }

            Text(
                text = post.caption,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, bottom = 10.dp),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

        }

    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun LikeButtonWithTitle(
    modifier: Modifier = Modifier,
    post: PostItem,
    onLike: () -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconToggleButton(
            checked = post.isLiked,
            onCheckedChange = {
                onLike()
            }
        ) {
            val transition = updateTransition(post.isLiked, label = "")

            val tint by transition.animateColor(label = "iconColor") { isChecked ->
                if (isChecked) Color.Red else MaterialTheme.colorScheme.onSurface
            }

            val size by transition.animateDp(
                transitionSpec = {
                    if (false isTransitioningTo true) {
                        keyframes {
                            durationMillis = 250
                            // on below line we are specifying animations
                            30.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                            35.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                            40.dp at 75 // ms
                            35.dp at 150 // ms
                        }
                    } else {
                        spring(stiffness = Spring.StiffnessVeryLow)
                    }
                },
                label = "Size"
            ) { /* button size = */ 27.dp }

            Icon(
                imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "like",
                tint = tint,
                modifier = Modifier.size(size)
            )
        }

        Text(
            text = "Liked By ${post.likes.size} people",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

    }

}

