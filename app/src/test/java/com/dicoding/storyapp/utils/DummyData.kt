package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.ListStoryItem

object DummyData {
    fun generatePagingData(): List<ListStoryItem> {
        val storyList = mutableListOf<ListStoryItem>()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "url $i",
                "date $i",
                "name $i",
                "desc $i",
                i.toFloat(),
                i.toFloat()
            )

            storyList.add(story)
        }
        return storyList
    }
}