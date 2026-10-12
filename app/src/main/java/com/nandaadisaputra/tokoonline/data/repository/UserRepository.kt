package com.nandaadisaputra.tokoonline.data.repository

import com.nandaadisaputra.tokoonline.data.network.ApiService
import com.nandaadisaputra.tokoonline.data.network.LoginResponse
import com.nandaadisaputra.tokoonline.data.network.RegisterResponse
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    // Periode Fungsi Register: Menghubungkan permintaan pendaftaran akun ke endpoint API melalui ApiService secara suspend function
    suspend fun register(
        username: String,
        email: String,
        password: String,
        noTlp: String
    ): Response<RegisterResponse> {
        return apiService.registerUser(username, email, password, noTlp)
    }

    // Periode Fungsi Login: Menghubungkan permintaan autentikasi login ke endpoint API melalui ApiService secara suspend function
    suspend fun login(
        username: String,
        password: String
    ): Response<LoginResponse> {
        return apiService.loginUser(username, password)
    }
}