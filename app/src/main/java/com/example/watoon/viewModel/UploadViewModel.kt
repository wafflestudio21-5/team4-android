package com.example.watoon.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.watoon.MyApp
import com.example.watoon.data.Tags
import com.example.watoon.data.UploadDays
import com.example.watoon.data.UploadEpisodeRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.network.MyRestAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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

    suspend fun uploadWebtoon(title : String, description : String, uploadDays : List<String>, tag1 : String, tag2 : String){
        val uploadDaysTrimmed = uploadDays.drop(1)
        val uploadDaysList = uploadDaysTrimmed.map { UploadDays(it) }

        var tags : List<Tags> = mutableListOf()
        if(tag1 != "") tags = tags.plus(Tags(tag1))
        if(tag2 != "") tags = tags.plus(Tags(tag2))
        Log.d("tag - viewModel", tags.toString())

        //title image 수정 필요
        val uploadWebtoonRequest = UploadWebtoonRequest(title, description, uploadDaysList, tags, "")
        api.uploadWebtoon("access=" + MyApp.preferences.getToken("token", ""), uploadWebtoonRequest)
    }

    suspend fun uploadEpisode(title: String, episodeNumber: String, selectedImageUri: Uri?){
        /*var episodeNumberInt: Int? = episodeNumber.toIntOrNull()
        if(episodeNumberInt==null) episodeNumberInt = -1
        val uploadEpisodeRequest = UploadEpisodeRequest(title, episodeNumberInt)
        val descriptionJson = Gson().toJson(uploadEpisodeRequest)
        val descriptionRequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), descriptionJson)*/
        val title_json = title.toRequestBody("application/json".toMediaTypeOrNull())
        val episodeNumber_json = episodeNumber.toRequestBody("applicaton/json".toMediaTypeOrNull())

        Log.d("selected image", selectedImageUri!!.toString())
        val file = File(selectedImageUri.toString())
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("picture", file.name, requestFile)

        api.uploadEpisode(
            "access=" + MyApp.preferences.getToken("token", ""),
            webtoonId.value.toString(),
            title_json, episodeNumber_json, imagePart
        )
    }

    suspend fun search(search : String){
        searchWebtoonList.value = api.search(search)
    }

    suspend fun deleteWebtoon(id : Int){
        api.deleteWebtoon("access=" + MyApp.preferences.getToken("token", ""), id.toString())
    }
}