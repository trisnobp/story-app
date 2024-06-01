package com.dicoding.storyapp.view.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.local.UserPreferences
import com.dicoding.storyapp.data.local.dataStore
import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.view.MainActivity
import com.dicoding.storyapp.view.utils.StoryAdapter
import com.dicoding.storyapp.viewmodel.HomeViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref)).get(
            SessionViewModel::class.java
        )

        sessionViewModel.getUserName().observe(this) { name ->
            binding.textView.text = resources.getString(R.string.welcome_message, name)
        }

        sessionViewModel.getUserToken().observe(this) {
            provideStories(it)
        }

        binding.fabCreateStory.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            sessionViewModel.deleteAllData()
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    fun provideStories(token: String?) {
        homeViewModel.getAllStories(token)
        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        homeViewModel.storiesResponse.observe(this) {
            val layoutManager = LinearLayoutManager(this)
            binding.rvStories.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            binding.rvStories.addItemDecoration(itemDecoration)

            val adapter = StoryAdapter()
            adapter.submitList(it)
            binding.rvStories.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}