package com.example.watoon.pages

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.watoon.NavigationDestination
import com.example.watoon.viewModel.WebtoonsViewModel
import com.example.watoon.data.User
import com.example.watoon.data.Webtoon
import com.example.watoon.function.MainTopBar
import com.example.watoon.function.makeError
import com.example.watoon.function.translate
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    toWebtoonMain : (Webtoon) -> Unit
) {
    val viewModel: WebtoonsViewModel = hiltViewModel()
    val webtoonList = viewModel.webtoonList.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    val calendar = Calendar.getInstance()
    var listNum by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_WEEK)-1) }

    val apiListNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "finished")

    val rowNum = 1 + (webtoonList.itemCount-1)/3
    val context = LocalContext.current

    LaunchedEffect(true){
        try {
            viewModel.getWebtoons(apiListNames[listNum])
        } catch (e: HttpException) {
            makeError(context, e)
        }
    }
    
    Scaffold(
        topBar = {
            MainTopBar(apiListNames, listNum) { newIndex ->
                listNum = newIndex
                scope.launch {
                    try {
                        viewModel.getWebtoons(apiListNames[listNum])
                    } catch (e: HttpException) {
                        makeError(context, e)
                    }
                }
            }
        }
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ){
            items(rowNum){ rowIndex ->
                val rangeStart = rowIndex*3
                var rangeEnd = rangeStart + 2
                if(rangeEnd >= webtoonList.itemCount) rangeEnd = webtoonList.itemCount-1
                RowOfWebtoon(webtoonList.itemSnapshotList.slice(rangeStart .. rangeEnd), toWebtoonMain)
            }
        }
    }
}



@Composable
fun RowOfWebtoon(
    rowList: List<Webtoon?>,
    toWebtoonMain : (Webtoon) -> Unit
){
    val emptyWebtoon = Webtoon()
    val emptyFunc : (Webtoon)->Unit = {}

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        for(i in 0..2){
            if(i<rowList.size){
                rowList[i]?.let { Webtoon(webtoon = it, toWebtoonMain = toWebtoonMain)}
            }
            else{
                Webtoon(webtoon = emptyWebtoon, toWebtoonMain = emptyFunc)
            }
        }
    }
}

@Composable
fun Webtoon(webtoon: Webtoon, toWebtoonMain : (Webtoon) -> Unit) {
    Column(
        modifier = Modifier
            .clickable {
                toWebtoonMain(webtoon)
            }
    ) {
        AsyncImage(
            model = webtoon.titleImage,
            contentDescription = null
        )
        Text(
            text = webtoon.title,
            color = Color.Black
        )
    }
}

