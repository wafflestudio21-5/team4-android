package com.example.watoon.function

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watoon.MyApp
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.data.CommentContent
import com.example.watoon.viewModel.EpisodeViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun Comment(isComment: Boolean, isMaster:Boolean, viewModel: EpisodeViewModel, comment : CommentContent, onEnter: (String) -> Unit){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLike by remember { mutableStateOf(comment.liking) }
    var isDislike by remember { mutableStateOf(comment.disliking) }
    var likeNumber by remember { mutableIntStateOf(comment.likedBy.toInt()) }
    var dislikeNumber by remember { mutableIntStateOf(comment.dislikedBy.toInt()) }

    val myComment = (MyApp.preferences.getToken("id", "") == comment.createdBy.id.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx(),
                )
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ){
            if(!isComment && !isMaster){
                Image(
                    painter = painterResource(R.drawable.baseline_subdirectory_arrow_right_24),
                    contentDescription = null,
                )
            }

            Text(
                text = comment.createdBy.nickname,
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp)
            )

            Text(
                text = comment.dtCreated.substring(0..9) + " " + comment.dtCreated.substring(11..18),
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(5.dp)
            )

            if(myComment && !isMaster){
                Image(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        scope.launch {
                            try {
                                viewModel.deleteComment(comment.id.toString())
                                if(isComment) viewModel.getComment()
                                else viewModel.getRecomment()
                            } catch (e: HttpException) {
                                makeError(context, e)
                            }
                        }
                    }
                )
            }
        }

        Text(
            text = comment.content,
            fontSize = 15.sp,
            modifier = Modifier.padding(
                start = if(!isComment && !isMaster) 35.dp else 8.dp,
                top = 8.dp,
                bottom = 8.dp,
                end = 8.dp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isComment) {
                ClickableRow(
                    onClick = {
                        viewModel.commentId = comment.id
                        viewModel.comment = comment
                        onEnter(NavigationDestination.Recomment)
                    },
                    icon = null,
                    iconTint = Color.Black,
                    text = "답글"
                )
            }
            else Text(" ")

            Row(
            ){
                ClickableRow(
                    onClick = {
                        scope.launch {
                            try {
                                if (isLike) {
                                    viewModel.deleteLike(comment.id.toString())
                                    likeNumber--
                                    isLike = false
                                } else if (isDislike) Toast
                                    .makeText(context, "이미 싫어요를 누르셨습니다", Toast.LENGTH_LONG)
                                    .show()
                                else {
                                    viewModel.putLike(comment.id.toString(), true)
                                    likeNumber++
                                    isLike = true
                                }
                            } catch (e: HttpException) {
                                makeError(context, e)
                            }
                        }
                    },
                    icon = R.drawable.baseline_thumb_up_24,
                    iconTint = if (isLike) Color.Red else Color.Black,
                    text = likeNumber.toString()
                )

                Text(" ")

                ClickableRow(
                    onClick = {
                        scope.launch {
                            try {
                                if (isDislike) {
                                    viewModel.deleteLike(comment.id.toString())
                                    dislikeNumber--
                                    isDislike = false
                                } else if (isLike) Toast
                                    .makeText(context, "이미 좋아요를 누르셨습니다", Toast.LENGTH_LONG)
                                    .show()
                                else {
                                    viewModel.putLike(comment.id.toString(), false)
                                    dislikeNumber++
                                    isDislike = true
                                }
                            } catch (e: HttpException) {
                                makeError(context, e)
                            }
                        }
                    },
                    icon = R.drawable.baseline_thumb_down_24,
                    iconTint = if (isDislike) Color.Blue else Color.Black,
                    text = dislikeNumber.toString(),
                )
            }
        }
        Text(
            text = " ",
            fontSize = 6.sp
        )
    }
}

@Composable
fun ClickableRow(
    onClick: () -> Unit,
    icon: Int?,
    iconTint: Color,
    text: String,
) {
    Row(
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ) {
        if(icon != null){
            Image(
                painter = painterResource(icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(iconTint)
            )
        }
        Text(text = text)
    }
}