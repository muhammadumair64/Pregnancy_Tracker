package com.example.pregnancytrackerignite.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pregnancytrackerignite.data.models.CurrentUser
import com.example.pregnancytrackerignite.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    fun addUser(user: CurrentUser) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun updateUser(user: CurrentUser) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: CurrentUser) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }
}