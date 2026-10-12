package com.nandaadisaputra.tokoonline.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.tokoonline.data.network.LoginResponse
import com.nandaadisaputra.tokoonline.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun login(username: String, pass: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(username, pass)
                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                } else {
                    _errorMessage.value = "Login gagal: ${response.errorBody()?.string() ?: response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}