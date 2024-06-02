package com.dicoding.storyapp.view.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.view.utils.DateFormatter

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val story = intent.getParcelableExtra<ListStoryItem>("story") as ListStoryItem
        setContent(story)
    }

    private fun setContent(story: ListStoryItem) {
        binding.let {
            Glide.with(this)
                .load(story.photoUrl)
                .into(it.ivDetailPhoto)

            it.tvDetailDescription.text = story.description
            it.tvItemDate.text = DateFormatter.formatDate(story.createdAt!!)
            it.tvDetailName.text = story.name
        }
    }
}