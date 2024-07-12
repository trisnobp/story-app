package com.dicoding.storyapp.data.remote.retrofit

import com.dicoding.storyapp.data.remote.request.LoginDTO
import com.dicoding.storyapp.data.remote.request.RegisterDTO
import com.dicoding.storyapp.data.remote.response.AddStoryResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    // Authentication
    @POST("register")
    fun register(@Body registerDTO: RegisterDTO): Call<RegisterResponse>

    @POST("login")
    fun login(@Body loginDTO: LoginDTO): Call<LoginResponse>

    @GET("stories")
    fun getAllStoriesWithLocation(
        @Header("Authorization") bearerToken: String,
        @Query("location") location : Int = 1,
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") bearerToken: String,
        @Part photoFile: MultipartBody.Part,
        @Part("description") storyDescription: RequestBody
    ) : Call<AddStoryResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoriesResponse
}