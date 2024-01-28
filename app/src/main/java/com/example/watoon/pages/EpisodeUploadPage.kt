package com.example.watoon.pages

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
fun EpisodeUploadPage(onEnter: (String) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    val viewModel: UploadViewModel = hiltViewModel()

    var episodeTitle by remember { mutableStateOf("") }
    var episodeNumber by remember { mutableStateOf("")}
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val chooseFile = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedFileUri = uri
    }

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
                text = "에피소드 업로드",
                modifier = Modifier.weight(1f)
            )
        }
        TextField(
            value = episodeNumber,
            onValueChange = { episodeNumber = it },
            label = { Text("에피소드 제목") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = episodeTitle,
            onValueChange = { episodeTitle = it },
            label = { Text("에피소드 제목") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "파일 첨부")
            IconButton(
                onClick = { chooseFile.launch("*/*") },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            }
        }
        if (selectedFileUri != null) {
            Text("Selected File: ${getFileName(selectedFileUri!!)}")
        }
        MenuButton(text = "업로드") {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    viewModel.uploadEpisode(episodeTitle, episodeNumber)
                    Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show()
                } catch (e: HttpException) {
                    var message = ""
                    val errorBody = JSONObject(e.response()?.errorBody()?.string())
                    errorBody.keys().forEach { key ->
                        message += ("$key - ${errorBody.getString(key)}" + "\n")
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                } finally {
                    isLoading = false
                }
            }
        }
        if (isLoading) {
            Text("로딩 중입니다...")
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