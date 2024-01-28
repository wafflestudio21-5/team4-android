package com.example.watoon.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.viewModel.UploadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWebtoonPage (onEnter: (String) -> Unit){
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var uploadDays by remember { mutableStateOf(listOf("")) }
    var tag1 by remember { mutableStateOf("") }
    var tag2 by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    val viewModel: UploadViewModel = hiltViewModel()

    Column (
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onEnter(NavigationDestination.WebtoonUpload)
                }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Text(
                text = "웹툰 업로드",
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item{
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("웹툰 제목") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            item{
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("웹툰 설명") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            item{
                Text("정기적으로 업로드할 요일을 선택하세요.", modifier = Modifier.padding(8.dp))
            }

            val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

            item{
                daysOfWeek.forEach { day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = uploadDays.contains(day),
                            onCheckedChange = {
                                uploadDays = if (uploadDays.contains(day)) {
                                    uploadDays - day
                                } else {
                                    uploadDays + day
                                }
                            },
                        )
                        Text(text = day)
                    }
                }
            }

            item {
                Text("해시태그를 입력하세요(0~2개).", modifier = Modifier.padding(8.dp))
            }

            item {
                TextField(
                    value = tag1,
                    onValueChange = { tag1 = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            item {
                TextField(
                    value = tag2,
                    onValueChange = { tag2 = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            item {
                MenuButton(text = "추가") {
                    isLoading = true
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            viewModel.uploadWebtoon(
                                title,
                                description,
                                uploadDays,
                                tag1, tag2
                            )
                            Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                        } catch (e: HttpException) {
                            var message = ""
                            val errorBody = JSONObject(e.response()?.errorBody()?.string())
                            errorBody.keys().forEach { key ->
                                message += ("$key - ${errorBody.getString(key).substring(2 until errorBody.getString(key).length - 2)}" + "\n")
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }
            }
            if (isLoading) {
                item {
                    Text("로딩 중입니다...")
                }
            }
        }
    }
}