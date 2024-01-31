package com.example.watoon.function

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watoon.R

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
fun UploadTopBar(text:String, destination:String, destination2: String, onEnter: (String) -> Unit){
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