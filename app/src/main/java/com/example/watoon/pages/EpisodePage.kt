package com.example.watoon.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import retrofit2.HttpException
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.data.EpisodeContent
import com.example.watoon.data.Webtoon
import com.example.watoon.function.EpisodeTopBar
import com.example.watoon.function.MiniButton
import com.example.watoon.function.MyDialog
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.EpisodeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EpisodePage(
    viewModel: EpisodeViewModel,
    episodeId : Int,
    toWebtoonMain : (Webtoon) -> Unit,
    onEnter : (String) -> Unit
) {
    val episodeContent = viewModel.episodeInfo.collectAsState().value

    val scope = rememberCoroutineScope()

    val prevId = episodeContent.previousEpisode
    val nextId = episodeContent.nextEpisode

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    var giveRating by remember { mutableIntStateOf(0) }

    LaunchedEffect(true){
        try {
            viewModel.getEpisodeContent(episodeId.toString())
            giveRating = viewModel.getEpisodeRate()
        }catch (e:HttpException){
            makeError(context, e)
        }
    }

    Scaffold(
        topBar = { EpisodeTopBar(toWebtoonMain = toWebtoonMain, episodeContent = episodeContent)},
        bottomBar = {
            BottomAppBar (
                containerColor = Color.Gray,
                modifier = Modifier
                    .height(50.dp),
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(R.drawable.baseline_chat_24),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    onEnter(NavigationDestination.Comment)
                                },
                            colorFilter = ColorFilter.tint(Color.Black)
                        )

                        Row(
                            modifier = Modifier.clickable {
                                scope.launch {
                                    try {
                                        viewModel.putEpisodeLike()
                                    } catch (e: HttpException) {
                                        makeError(context, e)
                                    }
                                }
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = if(episodeContent.liking) painterResource(R.drawable.baseline_favorite_24) else painterResource(R.drawable.baseline_favorite_border_24),
                                colorFilter = if(episodeContent.liking) ColorFilter.tint(Color.Red) else ColorFilter.tint(Color.Black),
                                contentDescription = ""
                                )
                            Text(episodeContent.likedBy.toString())
                        }

                        Row {
                            Image(
                                painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        if (prevId != null) {
                                            scope.launch {
                                                try {
                                                    viewModel.getEpisodeContent(prevId)
                                                } catch (e: HttpException) {
                                                    makeError(context, e)
                                                }
                                            }
                                        }
                                    },
                                colorFilter = if(prevId == null) ColorFilter.tint(Color.DarkGray) else ColorFilter.tint(Color.Black),
                            )

                            Image(
                                painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        if (nextId != null) {
                                            scope.launch {
                                                try {
                                                    viewModel.getEpisodeContent(nextId)
                                                } catch (e: HttpException) {
                                                    makeError(context, e)
                                                }
                                            }
                                        }
                                    },
                                colorFilter = if(nextId == null) ColorFilter.tint(Color.DarkGray) else ColorFilter.tint(Color.Black),
                            )
                        }
                    }
                }
            )
        }
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ){
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "★ ",
                            color = Color.Red,
                            fontSize = 20.sp
                        )
                        Text(
                            text = episodeContent.totalRating,
                            fontSize = 20.sp
                        )
                    }

                    MiniButton(text = "별점주기") {
                        showDialog = true
                    }
                }
            }
        }

    }


    MyDialog(
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row{
                    RatingStar(1, giveRating) { rating ->
                        giveRating = rating
                    }
                    RatingStar(2, giveRating) { rating ->
                        giveRating = rating
                    }
                    RatingStar(3, giveRating) { rating ->
                        giveRating = rating
                    }
                    RatingStar(4, giveRating) { rating ->
                        giveRating = rating
                    }
                    RatingStar(5, giveRating) { rating ->
                        giveRating = rating
                    }
                }
                Text(
                    text = "\n$giveRating.00\n",
                    fontSize = 20.sp
                )
            }
        },
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(giveRating == 0){
                Toast.makeText(context, "별점을 주지 않았습니다", Toast.LENGTH_LONG).show()
            }
            else{
                scope.launch {
                    try {
                        viewModel.putEpisodeRate(giveRating)
                        showDialog = false
                    } catch (e: HttpException) {
                        makeError(context, e)
                    }
                }
            }
        }
    )
}

@Composable
fun RatingStar(rate: Int, giveRating: Int, onRatingChanged: (Int) -> Unit) {
    val starColor = if (rate <= giveRating) Color.Red else Color.Gray

    Text(
        text = "★",
        color = starColor,
        modifier = Modifier
            .clickable {
                onRatingChanged(rate)
            },
        fontSize = 30.sp
    )

}