package com.example.watoon.pages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.watoon.NavigationDestination
import com.example.watoon.data.DayOfWeek
import com.example.watoon.data.Episode
import com.example.watoon.data.Tag
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.function.makeError
import com.example.watoon.function.translate
import com.example.watoon.viewModel.WebtoonMainViewModel
import retrofit2.HttpException

@Composable
fun WebtoonMainPage(
    webtoonId : Int,
    onEnter: (String) -> Unit
) {
    val viewModel: WebtoonMainViewModel = hiltViewModel()
    val episodeList = viewModel.episodeList.collectAsLazyPagingItems()
    val webtoonInfo = viewModel.webtoonInfo.collectAsState().value

    val context = LocalContext.current
    
    LaunchedEffect(true){
        try {
            println(webtoonId)
            viewModel.getWebtoonInfo(webtoonId.toString())
            viewModel.getEpisodeList(webtoonId.toString())
        } catch (e: HttpException) {
            makeError(context, e)
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Row{
            IconButton(
                onClick = {
                    onEnter(NavigationDestination.Main)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
        }

        WebtoonInfo(webtoonInfo)

        Text("\n")

        LazyColumn{
            items(episodeList.itemSnapshotList){
                if (it != null) {
                    EpisodeItem(it)
                }
            }
        }
    }
}
@Composable
fun WebtoonInfo(webtoonInfo:WebtoonDetailRequest){
    var expanded by remember { mutableStateOf(false) }

    Text(webtoonInfo.title)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(webtoonInfo.author.nickname + " " + dayOfWeek(webtoonInfo.uploadDays))
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                contentDescription = null
            )
        }
    }
    if(expanded){
        Text(webtoonInfo.description)
        Row {
            for(tag:Tag in webtoonInfo.tags) {
                Tag(tag)
            }
        }
    }
}
@Composable
fun Tag(tag: Tag){
    Text("#" + tag.content + " ")
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
fun EpisodeItem(episode:Episode){
    Text(episode.title)
}