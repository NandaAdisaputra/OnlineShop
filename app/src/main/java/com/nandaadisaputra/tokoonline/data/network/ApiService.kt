package com.nandaadisaputra.tokoonline.data.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    // Periode Endpoint Register: Mendefinisikan method HTTP POST dengan format URL encoded untuk pendaftaran pengguna baru ke server
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") pass: String,
        @Field("no_tlp") noTlp: String
    ): Response<RegisterResponse>

    // Periode Endpoint Login: Mendefinisikan method HTTP POST dengan format URL encoded untuk proses autentikasi pengguna
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") pass: String
    ): Response<LoginResponse>
}