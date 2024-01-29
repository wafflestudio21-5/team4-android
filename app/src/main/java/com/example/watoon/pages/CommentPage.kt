package com.example.watoon.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.watoon.MyApp
import com.example.watoon.NavigationDestination
import com.example.watoon.data.CommentContent
import com.example.watoon.data.Episode
import com.example.watoon.data.EpisodeContent
import com.example.watoon.function.MenuButton
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.EpisodeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentPage (
    viewModel: EpisodeViewModel,
    onEnter: (String) -> Unit,
    toEpisode : (EpisodeContent) -> Unit
    ) {
    val commentList = viewModel.commentList.collectAsLazyPagingItems()
    val context = LocalContext.current

    var content by remember { mutableStateOf("") }

    LaunchedEffect(true){
        try {
            viewModel.getComment()
        }catch (e:HttpException){
            makeError(context, e)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    toEpisode(viewModel.episodeInfo.value)
                }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Text("댓글")
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(8.dp).weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(commentList.itemCount/*size*/) { index ->
                Comment(true, viewModel, commentList.get(index)!!, onEnter)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("댓글을 입력해주세요:)") },
                modifier = Modifier.padding(8.dp).weight(8f)
            )
            MenuButton(text = "등록") {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        viewModel.uploadComment(content)
                        Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                        content = ""
                        viewModel.getComment()
                    } catch(e : HttpException){
                        makeError(context, e)
                    }
                }
            }
        }
    }
}

@Composable
fun Comment(isComment: Boolean, viewModel: EpisodeViewModel, comment : CommentContent, onEnter: (String) -> Unit){

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        val myComment = (MyApp.preferences.getToken("id", "") == comment.createdBy.id.toString())

        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ){


            if(!isComment) Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            Text(comment.createdBy.nickname, fontSize = 14.sp, modifier = Modifier.padding(8.dp))
            Text(comment.dtCreated, color = Color.Gray, fontSize = 8.sp, modifier = Modifier.padding(8.dp))

            if(myComment){
                IconButton(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                viewModel.deleteComment(comment.id.toString())
                                if(isComment) viewModel.getComment()
                                else viewModel.getRecomment()
                            } catch (e: HttpException) {
                                makeError(context, e)
                            }
                        }

                    }
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
        Text(comment.content, fontSize = 13.sp, modifier = Modifier.padding(vertical = 14.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            if(isComment) {
                MenuButton(text = "답글") {
                    viewModel.commentId = comment.id
                    viewModel.comment = comment
                    onEnter(NavigationDestination.Recomment)
                }
            }
        }
    }
}