package com.example.watoon.pages


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.data.DayOfWeek
import com.example.watoon.data.Episode
import com.example.watoon.data.Tag
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.function.makeError
import com.example.watoon.function.translate
import com.example.watoon.ui.theme.Watoon
import com.example.watoon.viewModel.WebtoonMainViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WebtoonMainPage(
    webtoonId : Int,
    onEnter: (String) -> Unit,
    toEpisode: (Episode) -> Unit
) {
    val viewModel: WebtoonMainViewModel = hiltViewModel()
    val episodeList = viewModel.episodeList.collectAsLazyPagingItems()
    val webtoonInfo = viewModel.webtoonInfo.collectAsState().value

    val subscribe = webtoonInfo.subscribing

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    LaunchedEffect(true){
        try {
            viewModel.getWebtoonInfo(webtoonId.toString())
            viewModel.getEpisodeList(webtoonId.toString())
        } catch (e: HttpException) {
            makeError(context, e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(50.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black),
                title = {
                    Text(text = " ")
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onEnter(NavigationDestination.Main)
                            },
                            colorFilter = ColorFilter.tint(Color.White)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                painter = if(subscribe) painterResource(R.drawable.baseline_check_24)
                                else painterResource(R.drawable.baseline_add_24),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        scope.launch {
                                            try {
                                                viewModel.changeSubscribe(webtoonId.toString())
                                            } catch (e: HttpException) {
                                                makeError(context, e)
                                            }
                                        }
                                    },
                                colorFilter = if(subscribe) ColorFilter.tint(Watoon) else ColorFilter.tint(Color.White)
                            )
                            Text(
                                text = "구독",
                                color = if(subscribe) Watoon else Color.White
                            )
                        }
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier.padding(top = 50.dp)
        ) {
            WebtoonInfo(webtoonInfo)

            LazyColumn{
                items(episodeList.itemSnapshotList){
                    if (it != null) {
                        EpisodeItem(it, toEpisode)
                    }
                }
            }
        }
    }
}
@Composable
fun WebtoonInfo(webtoonInfo:WebtoonDetailRequest){
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = webtoonInfo.title,
        fontSize = 25.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(8.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(webtoonInfo.author.nickname)
            Text(
                text = " / " + dayOfWeek(webtoonInfo.uploadDays),
                color = Color.Gray
            )
        }
        Image(
            painter = if(!expanded) painterResource(R.drawable.baseline_keyboard_arrow_down_24)
            else painterResource(R.drawable.baseline_keyboard_arrow_up_24),
            contentDescription = null,
            modifier = Modifier.clickable {
                expanded = !expanded
            },
        )
    }
    if(expanded){
        Text(
            text = "줄거리 " + webtoonInfo.description,
            color = Color.Gray,
            modifier = Modifier.padding(5.dp)
        )
        Row(
            modifier = Modifier
                .padding(5.dp)
        ) {
            for(tag:Tag in webtoonInfo.tags) {
                Tag(tag)
            }
        }
    }
}
@Composable
fun Tag(tag: Tag){
    Text(
        text = "#" + tag.content,
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(end = 5.dp, start = 5.dp)

    )
    Text(" ")
}
fun dayOfWeek(days:List<DayOfWeek>):String{
    return if(days.isEmpty()){
        "휴재 웹툰"
    }
    else if(days.size == 1){
        translate(days[0].name) + "요웹툰"
    }
    else{
        var str = ""
        for(day:DayOfWeek in days){
            str += (translate(day.name) + ",")
        }
        str.substring(0,str.length-1).plus(" 연재")
    }
}

@Composable
fun EpisodeItem(episode:Episode, toEpisode: (Episode) -> Unit){
    val rate = if(episode.totalRating == "") "0.00" else episode.totalRating

    Row(
        modifier = Modifier
            .clickable { toEpisode(episode) }
            .fillMaxWidth()
            .padding(5.dp)
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx(),
                )
            }
    ){
        Column {
            Text(
                text = episode.episodeNumber.toString() + "화 " + episode.title,
                fontSize = 20.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★ $rate",
                    fontSize = 18.sp
                )
                Text(
                    text = "   "+episode.releasedDate.substring(2..9),
                    fontSize = 15.sp
                )
            }
        }
    }
}