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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun CommentPage (viewModel: CommentViewModel, onEnter: (String) -> Unit) {
    //val commentList = viewModel.commentList.collectAsLazyPagingItems()
    val commentList = listOf(
        CommentContent(100, "정말 재미있어요!", "240128", "240128",
            UserInfo(0, "하늘"), "0", "0", "0"
        ),
        CommentContent(101, "노잼임", "240127", "240128",
        UserInfo(5, "풀"), "0", "0", "0"
    )
    )
    var isLoading by remember { mutableStateOf(false) }

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
        }

        LaunchedEffect(true) {
            CoroutineScope(Dispatchers.Main).launch {
                //웹툰id 수정 필요
                viewModel.getComment("26")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(commentList./*itemCount*/size) { index ->
                Comment(viewModel, commentList.get(index)!!)
            }
        }
    }
}

@Composable
fun Comment(viewModel: CommentViewModel, comment : CommentContent){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        val myComment = (MyApp.preferences.getToken("id", "") == comment.createdBy.id.toString())

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            val context = LocalContext.current

            Text(comment.createdBy.nickname, color = Color.Gray, fontSize = 14.sp,)

            if(myComment){
                IconButton(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                viewModel.deleteComment(comment.id.toString())
                                Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
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
        Text(comment.dtCreated, color = Color.Gray, fontSize = 8.sp, modifier = Modifier.align(Alignment.End))
    }
}