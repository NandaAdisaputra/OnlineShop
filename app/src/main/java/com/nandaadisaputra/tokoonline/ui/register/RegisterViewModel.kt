package com.nandaadisaputra.tokoonline.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.tokoonline.data.network.RegisterResponse
import com.nandaadisaputra.tokoonline.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun register(username: String, email: String, pass: String, noTlp: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(username, email, pass, noTlp)
                if (response.isSuccessful) {
                    _registerResult.value = response.body()
                } else {
                    _errorMessage.value = "Registrasi gagal: ${response.errorBody()?.string() ?: response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}