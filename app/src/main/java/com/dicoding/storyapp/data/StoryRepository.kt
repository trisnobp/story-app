package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.remote.request.LoginDTO
import com.dicoding.storyapp.data.remote.request.RegisterDTO
import com.dicoding.storyapp.data.remote.response.AddStoryResponse
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _storiesResponse = MutableLiveData<List<ListStoryItem?>?>()
    val storiesResponse: LiveData<List<ListStoryItem?>?> = _storiesResponse

    fun getStoriesInPaging(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun register(registerDTO: RegisterDTO) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(registerDTO)
        client.enqueue(object : retrofit2.Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                } else {
                    _registerResponse.value = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        RegisterResponse::class.java
                    )
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun login(loginDTO: LoginDTO) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(loginDTO)
        client.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    _loginResponse.value = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        LoginResponse::class.java
                    )
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun addStory(token: String?, photo: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().addStory("Bearer $token", photo, description)
        client.enqueue(object : retrofit2.Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                _isSuccess.value = response.isSuccessful
            }
            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
            }
        })
    }

    fun getAllStoriesWithLocation(token: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStoriesWithLocation("Bearer $token")
        client.enqueue(object : retrofit2.Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storiesResponse.value = response.body()?.listStory
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}