package com.saeedlotfi.robikaTest.ui.screen.comments

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.saeedlotfi.robikaTest.ui.viewModels.CommentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@Composable
internal fun CommentsScreen(
    modifier: Modifier = Modifier,
    viewModel: CommentsViewModel,
    postId: Long
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

    //RootView
    Column(modifier = modifier.fillMaxSize()) {

        CommentList(
            modifier = modifier.weight(10f),
            comments = viewState.value.comments,
            onLoadNextPart = {
                viewIntentChannel.trySend(
                    ViewIntent.LoadMore(postId, viewState.value.part)
                )
            },
            onLike = {
                viewIntentChannel.trySend(
                    ViewIntent.LikeComment(comment = it)
                )
            }
        )

        CommentTextFieldWithSendButton(
            modifier = modifier.weight(1f),
            onSend = {
                viewIntentChannel.trySend(
                    ViewIntent.AddComment(comment = it, postId = postId)
                )
            }
        )

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommentTextFieldWithSendButton(
    modifier: Modifier = Modifier,
    onSend: (comment: String) -> Unit
) {
    val commentState = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RectangleShape
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicTextField(
            value = commentState.value,
            onValueChange = { commentState.value = it },
            modifier = modifier
                .padding(start = 5.dp)
                .padding(5.dp)
                .weight(1f),
            maxLines = 3,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 17.sp
            )
        ) { innerTextField ->
            Box(
                modifier = modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(5.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                innerTextField()
            }
        }

        IconButton(onClick = {
            onSend(commentState.value)
            keyboardController?.hide()
            commentState.value = ""
        }) {
            Icon(Icons.Filled.Send, "send")
        }

    }
}


@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    comments: List<CommentItem>,
    onLoadNextPart: () -> Unit,
    onLike: (post: CommentItem) -> Unit
) {
    val scrollState = rememberLazyListState()

    fun LazyListState.isScrolledToEnd() =
        layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

    val endOfListReached by remember { derivedStateOf { scrollState.isScrolledToEnd() } }

    LaunchedEffect(endOfListReached) {
        onLoadNextPart()
    }

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        state = scrollState,
    ) {

        items(count = comments.size, key = { comments[it].id }) { index ->
            CommentCard(
                comment = comments[index],
                onLike = onLike
            )
        }

    }

}

@Composable
fun CommentCard(
    modifier: Modifier = Modifier,
    comment: CommentItem,
    onLike: (comment: CommentItem) -> Unit
) {
    Row(modifier = modifier.padding(all = 8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = comment.creator.image),
            contentDescription = null,
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
        )
        Spacer(modifier = modifier.width(8.dp))

        Column {
            Text(
                text = comment.creator.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = modifier.padding(1.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = comment.text,
                        modifier = modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .padding(all = 4.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )

                    LikeButton(
                        comment = comment,
                        onLike = { onLike(comment) },
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    comment: CommentItem,
    onLike: () -> Unit
) {

    Row(
        modifier = modifier.padding(end = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconToggleButton(
            checked = comment.isLiked,
            onCheckedChange = {
                onLike()
            },
        ) {
            val transition = updateTransition(comment.isLiked, label = "")

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
            ) { 17.dp }

            Icon(
                imageVector = if (comment.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "like",
                tint = tint,
                modifier = modifier.size(size)
            )
        }

        Text(
            text = "${comment.likes.size} like",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp
        )

    }

}