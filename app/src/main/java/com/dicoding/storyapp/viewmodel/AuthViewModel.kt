package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.data.remote.request.LoginDTO
import com.dicoding.storyapp.data.remote.request.RegisterDTO
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse

class AuthViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = storyRepository.isLoading

    val registerResponse: LiveData<RegisterResponse> = storyRepository.registerResponse

    val loginResponse: LiveData<LoginResponse> = storyRepository.loginResponse

    fun register(registerDTO: RegisterDTO) {
        storyRepository.register(registerDTO)
    }

    fun login(loginDTO: LoginDTO) {
        storyRepository.login(loginDTO)
    }
}