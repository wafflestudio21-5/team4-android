package com.example.watoon.pages

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.watoon.NavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebtoonUploadPage(onEnter: (String) -> Unit) {
    var webtoonTitle by remember { mutableStateOf("") }
    var episodeTitle by remember { mutableStateOf("") }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Set up file picker launcher
    val chooseFile = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedFileUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(8.dp)
        ) {
            IconButton(
                onClick = {
                    onEnter(NavigationDestination.Main)
                }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
        }
        TextField(
            value = webtoonTitle,
            onValueChange = { webtoonTitle = it },
            label = { Text("웹툰 제목") },
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
        ){
            Text(text = "파일 첨부")
            IconButton(
                onClick = { chooseFile.launch("*/*") },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            }
        }
        // Display the selected file information
        if (selectedFileUri != null) {
            Text("Selected File: ${getFileName(selectedFileUri!!)}")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ){
            MenuButton(text = "새 웹툰") {
            }
            MenuButton(text = "새 에피소드"){
            }
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