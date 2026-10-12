package com.nandaadisaputra.tokoonline.data.network

import com.google.gson.annotations.SerializedName

// Periode Model RegisterResponse: Data class untuk menampung format respons dari server saat proses registrasi
data class RegisterResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)

// Periode Model LoginResponse: Data class untuk menampung format respons dari server saat proses autentikasi login
data class LoginResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserData?
)

// Periode Model UserData: Data class untuk merepresentasikan detail informasi entitas pengguna yang dikembalikan dari server
data class UserData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("no_tlp")
    val noTlp: String
)