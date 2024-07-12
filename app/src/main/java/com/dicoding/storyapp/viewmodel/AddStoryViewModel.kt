package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    val isSuccess: LiveData<Boolean> = storyRepository.isSuccess

    fun addStory(token: String?, photo: MultipartBody.Part, description: RequestBody) {
        storyRepository.addStory(token, photo, description)
    }

}