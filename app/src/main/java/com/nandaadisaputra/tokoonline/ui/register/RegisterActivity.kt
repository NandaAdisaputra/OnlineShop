package com.nandaadisaputra.tokoonline.ui.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nandaadisaputra.tokoonline.data.network.ApiConfig
import com.nandaadisaputra.tokoonline.data.repository.UserRepository
import com.nandaadisaputra.tokoonline.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    // Deklarasi variabel binding untuk layout activity_register.xml dan ViewModel untuk logika registrasi
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Periode Inisialisasi View Binding untuk menghubungkan layout XML dengan Activity
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Periode Setup Dependency: Menginisialisasi ApiService, Repository, dan ViewModel menggunakan Factory
        val apiService = ApiConfig.provideApiService
        val repository = UserRepository(apiService)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(repository) as T
            }
        })[RegisterViewModel::class.java]

        // Memanggil fungsi untuk mulai mengamati perubahan LiveData dari ViewModel
        observeViewModel()

        // Periode Event Listener tombol Submit: Validasi kelengkapan form input dan eksekusi fungsi registrasi
        binding.btnSubmit.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val noTlp = binding.edtNoTlp.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || noTlp.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(username, email, password, noTlp)
            }
        }

        // Periode Event Listener teks Login: Menutup activity untuk kembali ke halaman login
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk mengamati (observe) perubahan state dari ViewModel secara reaktif
    private fun observeViewModel() {

        // Periode pengamatan hasil registrasi (sukses dari API), menampilkan pesan dan kembali otomatis ke halaman login
        viewModel.registerResult.observe(this) { response ->
            if (response != null && response.status) {
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                finish() // Kembali otomatis ke halaman login
            }
        }

        // Periode pengamatan pesan error jika terjadi kegagalan pada proses jaringan/registrasi
        viewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        // Periode pengamatan status loading untuk menampilkan ProgressBar dan mengatur status aktif tombol submit
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSubmit.isEnabled = !isLoading
        }
    }
}