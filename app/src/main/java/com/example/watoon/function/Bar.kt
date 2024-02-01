package com.example.watoon.function

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.ui.theme.Watoon

@Composable
fun BasicTopBar(text:String, destination:String, onEnter: (String) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onEnter(destination)
                },
            colorFilter = ColorFilter.tint(Color.White)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
            contentDescription = "",
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Color.Black)
        )
    }
}

@Composable
fun TwoButtonTopBar(text:String, destination:String, destination2: String, onEnter: (String) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onEnter(destination)
                },
            colorFilter = ColorFilter.tint(Color.White)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(R.drawable.baseline_add_24),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onEnter(destination2)
                },
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBasicTopBar(onEnter: (String) -> Unit){
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black),
        title = { Text(text = " ") },
        actions =
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniButton(text = "Upload"){
                    onEnter(NavigationDestination.WebtoonUpload)
                }
                Image(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onEnter(NavigationDestination.Search) },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    )
}

@Composable
fun MainBasicBottomBar(showMainPage: Boolean, onMainPageClick: () -> Unit, onInterestClick: () -> Unit) {
    BottomAppBar(
        modifier = Modifier.height(50.dp),
        containerColor = Color.Black,
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainBasicBottomBarItem(
                    isSelected = showMainPage,
                    iconResId = R.drawable.baseline_auto_stories_24,
                    text = "메인",
                    onClick = onMainPageClick
                )

                MainBasicBottomBarItem(
                    isSelected = !showMainPage,
                    iconResId = R.drawable.baseline_face_24,
                    text = "관심 웹툰",
                    onClick = onInterestClick
                )
            }
        }
    )
}

@Composable
fun MainBasicBottomBarItem(isSelected: Boolean, iconResId: Int, text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "",
            colorFilter = ColorFilter.tint(if (isSelected) Color.White else Color.Gray)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(apiListNames: Array<String>, selectedListNum: Int, onListNumChange: (Int) -> Unit) {
    TopAppBar(
        title = { Text(text = " ") },
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in apiListNames.indices) {
                    Text(
                        text = translate(apiListNames[i]),
                        color = if (i == selectedListNum) Watoon else Color.Black,
                        modifier = Modifier
                            .clickable { onListNumChange(i) }
                            .drawBehind {
                                drawLine(
                                    color = if (i == selectedListNum) Watoon else Color.Black,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 1.dp.toPx(),
                                )
                            },
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    )
}
