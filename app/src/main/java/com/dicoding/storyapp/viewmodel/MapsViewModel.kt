package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {

    val storiesResponse = storyRepository.storiesResponse
    fun getStoriesWithLocation(token: String) = storyRepository.getAllStoriesWithLocation(token)

}