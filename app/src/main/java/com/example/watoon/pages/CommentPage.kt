package com.example.watoon.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.watoon.MyApp
import com.example.watoon.NavigationDestination
import com.example.watoon.data.CommentContent
import com.example.watoon.data.UserInfo
import com.example.watoon.viewModel.CommentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentPage (viewModel: CommentViewModel, onEnter: (String) -> Unit) {
    val commentList = viewModel.commentList.collectAsLazyPagingItems()

    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    //목적지 수정 필요
                    onEnter(NavigationDestination.Main)
                }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Text("댓글")
        }

        LaunchedEffect(true) {
            CoroutineScope(Dispatchers.Main).launch {
                //에피소드 id 수정 필요
                viewModel.getComment("1")
            }
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
                        //에피소드 id 수정 필요
                        viewModel.uploadComment("1", content)
                        Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                        content = ""
                        viewModel.getComment("1")
                    } catch(e : HttpException){
                        makeError(context, e)
                    }
                }
            }
        }
    }
}

@Composable
fun Comment(isComment: Boolean, viewModel: CommentViewModel, comment : CommentContent, onEnter: (String) -> Unit){
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
            val context = LocalContext.current

            if(!isComment) Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            Text(comment.createdBy.nickname, fontSize = 14.sp, modifier = Modifier.padding(8.dp))
            Text(comment.dtCreated, color = Color.Gray, fontSize = 8.sp, modifier = Modifier.padding(8.dp))

            if(myComment){
                IconButton(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                viewModel.deleteComment(comment.id.toString())
                                //에피소드 id 추가 필요
                                if(isComment) viewModel.getComment("1")
                                else viewModel.getRecomment()
                            } catch (e: HttpException) {
                                var message = ""
                                val errorBody = JSONObject(e.response()?.errorBody()?.string())
                                errorBody.keys().forEach { key ->
                                    message += ("$key - ${errorBody.getString(key)}" + "\n")
                                }
                                message = message.substring(0, message.length-1)
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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