package com.example.watoon.function

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.watoon.R
import com.example.watoon.data.Webtoon
import com.example.watoon.ui.theme.Watoon

@Composable
fun BasicWebtoonItem(
    webtoon: Webtoon,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WebtoonDetails(webtoon = webtoon)
    }
}

@Composable
fun ToMainWebtoonItem(
    webtoon: Webtoon,
    toWebtoonMain: (Webtoon) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
            .clickable {
                toWebtoonMain(webtoon)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WebtoonDetails(webtoon = webtoon)
    }
}

@Composable
private fun WebtoonDetails(webtoon: Webtoon) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = webtoon.titleImage,
            contentDescription = "",
            modifier = Modifier
                .size(100.dp, 100.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = stringCut(webtoon.title),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = webtoon.author.nickname,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 3.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 3.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_star_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.Red),
                    modifier = Modifier
                        .size(15.dp, 15.dp)
                    )
                Text(
                    text = " " + webtoon.totalRating,
                    fontSize = 13.sp,
                    color = Color.Red
                )
            }

        }
    }
}


@Composable
fun MainWebtoonItem(webtoon: Webtoon, toWebtoonMain : (Webtoon) -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = Modifier
            .clickable {
                toWebtoonMain(webtoon)
            }
    ) {
        AsyncImage(
            model = webtoon.titleImage,
            contentDescription = null,
            modifier = Modifier
                .size(screenWidth/3, screenWidth/3)
        )
        Text(
            text = stringCut(webtoon.title),
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 5.dp),
            fontSize = 15.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            if(webtoon.title != ""){
                if(webtoon.subscribing){
                    Text(
                        text = "구독",
                        fontSize = 10.sp,
                        color = Watoon,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(R.drawable.baseline_check_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Watoon),
                        modifier = Modifier
                            .size(10.dp, 10.dp)
                    )
                }
                else{
                    Text(
                        text = stringCut(webtoon.author.nickname) + " ",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 5.dp),
                    )
                    Image(
                        painter = painterResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Gray),
                        modifier = Modifier
                            .size(10.dp, 10.dp)
                    )
                    Text(
                        text = webtoon.totalRating,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}