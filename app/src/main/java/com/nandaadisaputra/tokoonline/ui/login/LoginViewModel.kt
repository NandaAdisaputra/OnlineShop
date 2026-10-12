package com.nandaadisaputra.tokoonline.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.tokoonline.data.network.LoginResponse
import com.nandaadisaputra.tokoonline.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    // Periode Deklarasi LiveData dan Backing Property untuk enkapsulasi state UI (hasil login, loading, dan error)
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Periode Fungsi Login: Menjalankan proses autentikasi secara asynchronous dengan coroutine scope
    fun login(username: String, pass: String) {

        // Mengubah status loading menjadi true sebelum memanggil API
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Melakukan pemanggilan fungsi login ke Repository
                val response = repository.login(username, pass)

                // Periode Penanganan Respon API: Cek keberhasilan status response
                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                } else {
                    _errorMessage.value = "Login gagal: ${response.errorBody()?.string() ?: response.message()}"
                }
            } catch (e: Exception) {
                // Periode Penanganan Error/Exception jaringan atau sistem
                _errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                // Periode Cleanup: Mengembalikan status loading menjadi false setelah proses selesai
                _isLoading.value = false
            }
        }
    }
}