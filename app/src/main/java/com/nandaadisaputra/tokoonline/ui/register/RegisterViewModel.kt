package com.nandaadisaputra.tokoonline.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.tokoonline.data.network.RegisterResponse
import com.nandaadisaputra.tokoonline.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    // Periode Deklarasi LiveData dan Backing Property untuk enkapsulasi state UI (hasil registrasi, loading, dan error)
    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Periode Fungsi Register: Menjalankan proses pendaftaran akun secara asynchronous dengan coroutine scope
    fun register(username: String, email: String, pass: String, noTlp: String) {

        // Mengubah status loading menjadi true sebelum memanggil API
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Melakukan pemanggilan fungsi register ke Repository
                val response = repository.register(username, email, pass, noTlp)

                // Periode Penanganan Respon API: Cek keberhasilan status response
                if (response.isSuccessful) {
                    _registerResult.value = response.body()
                } else {
                    _errorMessage.value = "Registrasi gagal: ${response.errorBody()?.string() ?: response.message()}"
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