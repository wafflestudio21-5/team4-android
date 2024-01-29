package com.example.watoon.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.watoon.data.Webtoon
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.EpisodeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EpisodePage(
    episodeId : Int,
    toWebtoonMain : (Webtoon) -> Unit
) {
    val viewModel:EpisodeViewModel = hiltViewModel()
    val episodeContent = viewModel.episodeInfo.collectAsState().value

    val scope = rememberCoroutineScope()

    val prevId = episodeContent.previousEpisode
    val nextId = episodeContent.nextEpisode

    val context = LocalContext.current

    LaunchedEffect(true){
        try {
            viewModel.getEpisodeContent(episodeId.toString())
        }catch (e:HttpException){
            makeError(context, e)
        }
    }

    Scaffold(
        topBar = {
            // TopBar content
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Gray),
                title = {
                    Text(text = " ")
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ){
                        IconButton(
                            onClick = {
                                toWebtoonMain(episodeContent.webtoon)
                            }
                        ){
                            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
                        }

                        Text("  " + episodeContent.title)
                    }

                }
            )
        },
        bottomBar = {
            BottomAppBar (
                containerColor = Color.Gray,
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Image(
                            painter = painterResource(R.drawable.baseline_chat_24),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                },
                            colorFilter = ColorFilter.tint(Color.Black)
                        )

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
                            colorFilter = if(prevId == null) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black),
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
                            colorFilter = if(nextId == null) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black),
                        )
                    }
                }
            )
        }
    ){

    }
}