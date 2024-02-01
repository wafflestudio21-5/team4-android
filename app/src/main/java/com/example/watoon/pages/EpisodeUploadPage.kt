package com.example.watoon.pages

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.watoon.NavigationDestination
import com.example.watoon.function.BasicTopBar
import com.example.watoon.function.MyButton
import com.example.watoon.function.MyText
import com.example.watoon.function.MyTextField
import com.example.watoon.function.UploadButton
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.UploadViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun EpisodeUploadPage(viewModel:UploadViewModel,onEnter: (String) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    var episodeTitle by remember { mutableStateOf("") }
    var episodeNumber by remember { mutableStateOf("")}
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val chooseFile = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedFileUri = uri
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column (
    ) {
        BasicTopBar(
            text = "새로운 에피소드 추가",
            destination = NavigationDestination.WebtoonUpload,
            onEnter = onEnter
        )

        MyText(text = "화수")
        MyTextField(
            value = episodeNumber,
            onValueChange = { episodeNumber = it },
            label = "에피소드 화수를 입력하세요",
            visible = true
        )

        MyText(text = "에피소드 제목")
        MyTextField(
            value = episodeTitle,
            onValueChange = { episodeTitle = it },
            label = "에피소드 제목을 입력하세요",
            visible = true
        )

        MyText(text = "파일 첨부")
        UploadButton(
            chooseFile = chooseFile,
            onFileSelected = { uri -> selectedFileUri = uri }
        )

        if (selectedFileUri != null) {
            Text("Selected File: ${getFileName(selectedFileUri!!)}")
        }

        MyButton(text = "업로드") {
            isLoading = true
            scope.launch{
                try {
                    viewModel.uploadEpisode(episodeTitle, episodeNumber)
                    Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                    episodeTitle = ""
                    episodeNumber = ""
                } catch (e: HttpException) {
                    makeError(context, e)
                } finally {
                    isLoading = false
                }
            }
        }

        if (isLoading) {
            Text(text = "로딩 중입니다...",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
// Function to get the file name from the URI
fun getFileName(uri: Uri): String {
    val cursor = LocalContext.current.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        return it.getString(nameIndex)
    }
    return "Unknown"
}