package com.example.watoon.pages

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.documentfile.provider.DocumentFile
import com.example.watoon.NavigationDestination
import com.example.watoon.function.BasicTopBar
import com.example.watoon.function.MyButton
import com.example.watoon.function.MyText
import com.example.watoon.function.MyTextField
import com.example.watoon.function.UploadButton
import com.example.watoon.function.makeError
import com.example.watoon.function.translate
import com.example.watoon.ui.theme.Watoon
import com.example.watoon.viewModel.UploadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWebtoonPage (viewModel:UploadViewModel, onEnter: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var uploadDays by remember { mutableStateOf(listOf("")) }
    var tag1 by remember { mutableStateOf("") }
    var tag2 by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val chooseFile = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BasicTopBar(
            text = "신규 웹툰 등록",
            destination = NavigationDestination.WebtoonUpload,
            onEnter = onEnter
        )

        MyText(text = "웹툰 제목")
        MyTextField(
            value = title,
            onValueChange = { title = it },
            label = "제목을 입력하세요",
            visible = true
        )

        MyText(text = "설명")
        MyTextField(
            value = description,
            onValueChange = { description = it },
            label = "설명을 입력하세요",
            visible = true
        )

        MyText(text = "업로드 요일 설정")
        Row(
            modifier = Modifier.padding(11.dp)
        ){
            daysOfWeek.forEach { day ->
                Text(
                    text = " " + translate(day) + " ",
                    modifier = Modifier
                        .background(
                            if (uploadDays.contains(day)) Watoon
                            else Color.LightGray,
                        )
                        .clickable {
                            uploadDays = if (uploadDays.contains(day)) {
                                uploadDays - day
                            } else {
                                uploadDays + day
                            }
                        }
                        .border(
                            width = 2.dp,
                            color = if (uploadDays.contains(day)) Watoon else Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    fontSize = 25.sp
                )
                Text("  ")
            }
        }

        MyText(text = "해시태그 설정 (최대 2개)")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#",
                fontSize = 20.sp
            )
            TextField(
                value = tag1,
                onValueChange = { tag1 = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shadow(2.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    placeholderColor = Color.LightGray
                )

            )
            Text(
                text = "#",
                fontSize = 20.sp
            )
            TextField(
                value = tag2,
                onValueChange = { tag2 = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shadow(2.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    placeholderColor = Color.LightGray
                )
            )
        }

        MyText(text = "썸네일 추가")
        UploadButton(
            mini = true,
            chooseFile = chooseFile,
            onFileSelected = { uri -> selectedImageUri = uri }
        )

        if (selectedImageUri != null) {
            MyText("Selected File: ${getFileName(selectedImageUri!!)}")
        }

        MyButton(text = "추가") {
            isLoading = true
            scope.launch {
                try {
                    viewModel.uploadWebtoon(context, title, description, uploadDays, tag1, tag2, selectedImageUri)
                    Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                    title = ""
                    description = ""
                    uploadDays = listOf()
                    tag1 = ""
                    tag2 = ""
                    selectedImageUri = null
                    onEnter(NavigationDestination.WebtoonUpload)
                } catch (e: HttpException) {
                    makeError(context, e)
                } finally {
                    isLoading = false
                }
            }
        }

        if (isLoading) {
            Text(
                text = "로딩 중입니다...",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
