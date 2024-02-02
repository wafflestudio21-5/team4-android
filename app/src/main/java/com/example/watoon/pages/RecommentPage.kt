package com.example.watoon.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.watoon.NavigationDestination
import com.example.watoon.function.Comment
import com.example.watoon.function.CommentBottomBar
import com.example.watoon.function.CommentTopBar
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.EpisodeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun RecommentPage (viewModel: EpisodeViewModel,onEnter: (String) -> Unit) {
    val commentList = viewModel.recommentList.collectAsLazyPagingItems()
    var content by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getRecomment()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ){

        CommentTopBar(
            onClick = { onEnter(NavigationDestination.Comment) },
            commentNum = commentList.itemCount.toString(),
            text = "답글"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Comment(
                    isComment = false,
                    isMaster = true,
                    viewModel = viewModel,
                    comment = viewModel.comment!!,
                    onEnter = onEnter
                )
            }
            items(commentList.itemCount/*size*/) { index ->
                Comment(
                    isComment = false,
                    isMaster = false,
                    viewModel = viewModel,
                    comment = commentList.get(index)!!,
                    onEnter = onEnter
                )
            }
        }

        CommentBottomBar(
            content = content,
            onContentChange = { content = it },
            text = "답글을 입력해주세요:)",
            onClick = {
                scope.launch {
                    try {
                        viewModel.uploadRecomment(content)
                        Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                        content = ""
                        viewModel.getRecomment()
                    } catch(e : HttpException){
                        makeError(context, e)
                    }
                }
            }
        )
    }
}