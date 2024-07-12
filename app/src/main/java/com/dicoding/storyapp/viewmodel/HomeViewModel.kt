package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.response.ListStoryItem

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesInPaging(token: String?): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getStoriesInPaging(token!!).cachedIn(viewModelScope)
    }
}