package com.example.lsp_assesment_jmp.model

import android.net.Uri

data class Peserta(
    val id: Int,
    val nik: String,
    val nama: String,
    val nohp: String,
    val jenisKelamin: String,
    val tanggal: String,
    val lokasi: String,
    val gambar: Uri
)
