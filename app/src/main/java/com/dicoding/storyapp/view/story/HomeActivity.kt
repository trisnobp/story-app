package com.dicoding.storyapp.view.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.local.UserPreferences
import com.dicoding.storyapp.data.local.dataStore
import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.view.MainActivity
import com.dicoding.storyapp.view.map.MapsActivity
import com.dicoding.storyapp.view.utils.LoadingStateAdapter
import com.dicoding.storyapp.view.utils.StoryAdapter
import com.dicoding.storyapp.viewmodel.HomeViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModelFactory
import com.dicoding.storyapp.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserName().observe(this) { name ->
            binding.textView.text = resources.getString(R.string.welcome_message, name)
        }

        sessionViewModel.getUserToken().observe(this) {
            if (it != null) {
                provideStories(it)
            }
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

        binding.localizationButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.locationButton.setOnClickListener {
            val intent = Intent(this@HomeActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun provideStories(token: String?) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.getStoriesInPaging(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}