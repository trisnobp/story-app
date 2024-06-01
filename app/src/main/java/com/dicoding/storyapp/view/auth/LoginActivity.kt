package com.dicoding.storyapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.local.UserPreferences
import com.dicoding.storyapp.data.local.dataStore
import com.dicoding.storyapp.data.remote.request.LoginDTO
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.view.MainActivity
import com.dicoding.storyapp.view.story.HomeActivity
import com.dicoding.storyapp.viewmodel.AuthViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModel
import com.dicoding.storyapp.viewmodel.SessionViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref)).get(
            SessionViewModel::class.java
        )

        binding.backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        binding.registerLink.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            val emailField = binding.edLoginEmail.text.toString().trim()
            val passwordField = binding.edLoginPassword.text.toString().trim()

            if (emailField.isEmpty() || passwordField.isEmpty()) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_cant_be_empty)
                    it.visibility = View.VISIBLE
                }
            } else if (binding.edLoginEmail.error != null || binding.edLoginPassword.error != null) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_is_invalid)
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.errorMessage.visibility = View.GONE
                val loginDTO = LoginDTO(
                    emailField,
                    passwordField
                )
                authViewModel.login(loginDTO)

                authViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                authViewModel.loginResponse.observe(this) {
                    showToast(it.message)
                    if (!it.error!!) {
                        sessionViewModel.saveUserId(it.loginResult?.userId!!)
                        sessionViewModel.saveUserToken(it.loginResult.token!!)
                        sessionViewModel.saveUserName(it.loginResult.name!!)

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}