package com.example.lsp_assesment_jmp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lsp_assesment_jmp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aksi untuk button Informasi Peserta
        binding.buttonInfo.setOnClickListener {
            val intent = Intent(this, InformasiPeserta::class.java)
            startActivity(intent)
        }

        // Aksi untuk button Pendaftaran Peserta
        binding.buttonDaftar.setOnClickListener {
            val intent = Intent(this, PendaftaranPeserta::class.java)
            startActivity(intent)
        }

        // Aksi untuk button About Developer
        binding.buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutDev::class.java)
            startActivity(intent)
        }

        // Aksi untuk button Logout
        binding.buttonLogout.setOnClickListener {
            finish() // Menutup aplikasi
        }
    }
}
