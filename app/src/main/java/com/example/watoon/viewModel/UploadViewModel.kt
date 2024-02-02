package com.example.watoon.viewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.watoon.MyApp
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.Tag
import com.example.watoon.data.Tags
import com.example.watoon.data.UploadDays
import com.example.watoon.data.UploadEpisodeRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.function.getToken
import com.example.watoon.network.MyRestAPI
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    suspend fun uploadWebtoon(title : String, description : String, uploadDays : List<String>, tag1 : String, tag2 : String, titleImage: Uri?){
        val uploadDaysTrimmed = uploadDays.drop(1)
        val uploadDaysList = uploadDaysTrimmed.map { UploadDays(it) }

        var tags : List<Tag> = mutableListOf()
        if(tag1 != "") tags = tags.plus(Tag(tag1))
        if(tag2 != "") tags = tags.plus(Tag(tag2))

        val titleJson = title.toRequestBody("application/json".toMediaTypeOrNull())
        val descriptionJson = description.toRequestBody("applicaton/json".toMediaTypeOrNull())
        val uploadDaysJson = createListRequestBody(uploadDays)
        val tagsJson = createTagListRequestBody(tags)

        //val uploadWebtoonRequest = UploadWebtoonRequest(title, description, uploadDaysList, tags, titleImage.toString())
        api.uploadWebtoon(getToken(), titleJson, descriptionJson, uploadDaysJson, tagsJson, image)
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
        api.deleteWebtoon("access=" + MyApp.preferences.getToken("token", ""), id.toString())
    }
}

fun createListRequestBody(stringList: List<String>): RequestBody {
    val joinedString = stringList.joinToString(separator = ",") // Join strings with a comma or your preferred separator
    return joinedString.toRequestBody("applidcation/json".toMediaTypeOrNull())
}

fun createTagListRequestBody(tagList: List<Tag>): RequestBody {
    val moshi = Moshi.Builder().build()
    val listType = Types.newParameterizedType(List::class.java, Tag::class.java)
    val adapter: JsonAdapter<List<Tag>> = moshi.adapter(listType)

    val json = adapter.toJson(tagList)
    return json.toRequestBody("application/json".toMediaTypeOrNull())
}