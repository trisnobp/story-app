package com.dicoding.storyapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.local.UserPreferences
import com.dicoding.storyapp.data.local.dataStore
import com.dicoding.storyapp.view.story.HomeActivity
import com.dicoding.storyapp.viewmodel.SessionViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModelFactory

class SplashActivity : AppCompatActivity() {

    private var isLogin: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref)).get(
            SessionViewModel::class.java
        )

        sessionViewModel.getUserToken().observe(this) {
            if (it != null) {
                isLogin = true
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (!isLogin) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, TIME_IN_MILLIS)
    }

    companion object {
        const val TIME_IN_MILLIS : Long = 3000
    }
}