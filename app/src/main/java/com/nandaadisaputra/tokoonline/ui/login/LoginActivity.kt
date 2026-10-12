package com.nandaadisaputra.tokoonline.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nandaadisaputra.tokoonline.data.network.ApiConfig
import com.nandaadisaputra.tokoonline.data.repository.UserRepository
import com.nandaadisaputra.tokoonline.databinding.ActivityLoginBinding
import com.nandaadisaputra.tokoonline.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    // Deklarasi variabel binding untuk layout activity_login.xml dan ViewModel untuk logika login
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Periode Inisialisasi View Binding untuk menghubungkan layout XML dengan Activity
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Periode Setup Dependency: Menginisialisasi ApiService, Repository, dan ViewModel menggunakan Factory
        val apiService = ApiConfig.provideApiService
        val repository = UserRepository(apiService)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(repository) as T
            }
        })[LoginViewModel::class.java]

        // Memanggil fungsi untuk mulai mengamati perubahan LiveData dari ViewModel
        observeViewModel()

        // Periode Event Listener tombol Login: Validasi input form dan eksekusi fungsi login
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(username, password)
            }
        }

        // Periode Event Listener teks Register: Berpindah ke halaman registrasi
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Fungsi untuk mengamati (observe) perubahan state dari ViewModel secara reaktif
    private fun observeViewModel() {

        // Periode pengamatan hasil login (sukses/gagal dari API)
        viewModel.loginResult.observe(this) { response ->
            if (response != null && response.status) {
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                // Arahkan ke halaman utama/dashboard setelah sukses
            }
        }

        // Periode pengamatan pesan error jika terjadi kegagalan pada proses jaringan/login
        viewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        // Periode pengamatan status loading untuk menampilkan ProgressBar dan mengatur status aktif tombol
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }
    }
}