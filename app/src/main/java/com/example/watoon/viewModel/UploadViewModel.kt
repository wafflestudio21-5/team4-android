package com.example.watoon.viewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.watoon.MyApp
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.Tag
import com.example.watoon.data.UploadDays
import com.example.watoon.data.UploadEpisodeRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.function.getToken
import com.example.watoon.network.MyRestAPI
import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private var api : MyRestAPI) : ViewModel() {
    val myWebtoonList: MutableStateFlow<List<Webtoon>> = MutableStateFlow(listOf())
    var webtoonId: MutableStateFlow<Int> = MutableStateFlow(0)
    val searchWebtoonList: MutableStateFlow<List<Webtoon>> = MutableStateFlow(listOf())

    suspend fun loadMyWebtoon() {
        myWebtoonList.value = api.loadMyWebtoon(
            MyApp.preferences.getToken("id", ""))
    }

    suspend fun uploadWebtoon(context: Context, title : String, description : String, uploadDays : List<String>, tag1 : String, tag2 : String, selectedImageUri: Uri?){
        val uploadDaysTrimmed = uploadDays.drop(1)
        //val uploadDaysList = uploadDaysTrimmed.map { UploadDays(it) }

        var tags : List<String> = mutableListOf()
        if(tag1 != "") tags = tags.plus(tag1)
        if(tag2 != "") tags = tags.plus(tag2)

        //val uploadWebtoonRequest = UploadWebtoonRequest(title, description, uploadDaysList, tags)
        //val jsonObject = JSONObject(uploadWebtoonRequest)

        val titleJson = title.toRequestBody("application/json".toMediaTypeOrNull())
        val descriptionJson = description.toRequestBody("applicaton/json".toMediaTypeOrNull())
        val uploadDaysJson = createListRequestBody(uploadDaysTrimmed, "name")
        val tagsJson = createListRequestBody(tags, "content")

        val file = FileUtil.createTempFile(context, "image.jpg")
        var imagePart: MultipartBody.Part? = null

        if(selectedImageUri!=null) {
            FileUtil.copyToFile(context, selectedImageUri, file)
            val newFile = File(file.absolutePath)

            val requestFile = newFile.asRequestBody("image/*".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("titleImage", newFile.name, requestFile)
        }

        api.uploadWebtoon(getToken(), titleJson, descriptionJson, uploadDaysJson, tagsJson, imagePart)
    }

    suspend fun uploadEpisode(title: String, episodeNumber: String){
        var episodeNumberInt: Int? = episodeNumber.toIntOrNull()
        if(episodeNumberInt==null) episodeNumberInt = -1
        val uploadEpisodeRequest = UploadEpisodeRequest(title, episodeNumberInt)

        Log.d("final webtoonId", webtoonId.toString())
        api.uploadEpisode(
            getToken(),
            webtoonId.value.toString(),
            uploadEpisodeRequest)
    }

    suspend fun search(search : String){
        searchWebtoonList.value = api.search(search)
    }

    suspend fun deleteWebtoon(id : Int){
        api.deleteWebtoon(getToken(), id.toString())
    }
}

fun createListRequestBody(stringList: List<String>, keyword: String): RequestBody {
    if (stringList.isEmpty()) {
        return "[]".toRequestBody("application/json".toMediaTypeOrNull())
    }

    val array = JSONArray()
    for(word in stringList){
        array.put(JSONObject().put(keyword, word))
    }
    val str = array.toString()
    return str.toRequestBody("application/json".toMediaTypeOrNull())

    /*val jsonArray = stringList.map { mapOf(keyword to it) }
    val jsonString = Gson().toJson(jsonArray)
    return jsonString.toRequestBody("application/json".toMediaTypeOrNull())*/
}

object FileUtil {
    // 임시 파일 생성
    fun createTempFile(context: Context, fileName: String): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, fileName)
    }

    // 파일 내용 스트림 복사
    fun copyToFile(context: Context, uri: Uri, file: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(4 * 1024)
        while (true) {
            val byteCount = inputStream!!.read(buffer)
            if (byteCount < 0) break
            outputStream.write(buffer, 0, byteCount)
        }

        outputStream.flush()
    }
}