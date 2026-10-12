package com.nandaadisaputra.tokoonline.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    // Periode Konfigurasi Base URL untuk alamat utama tujuan endpoint API
    private const val BASE_URL = "https://pasarrakyatbangetayukulon.my.id/toko_online/"

    // Periode Inisialisasi ApiService menggunakan lazy initialization (hanya diinisialisasi saat pertama kali diakses)
    val provideApiService: ApiService by lazy {

        // Periode Setup Logging Interceptor untuk memantau detail request dan response jaringan pada Logcat
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Periode Setup OkHttpClient dengan penambahan interceptor serta pengaturan timeout koneksi dan baca data
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        // Periode Setup Retrofit Builder dengan konfigurasi Base URL, OkHttpClient, dan Gson Converter Factory
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Periode Pembuatan Instance ApiService dari interface yang telah didefinisikan
        retrofit.create(ApiService::class.java)
    }
}