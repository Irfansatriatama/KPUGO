package com.example.lsp_assesment_jmp.ui

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lsp_assesment_jmp.R
import com.example.lsp_assesment_jmp.model.SqliteHelper
import com.google.android.material.textfield.TextInputEditText

class DetailPeserta : AppCompatActivity() {

    private lateinit var dbHelper: SqliteHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_peserta)

        dbHelper = SqliteHelper(this)
        db = dbHelper.writableDatabase

        val nik = intent.getStringExtra("EXTRA_NIK")

        val etNik: TextInputEditText = findViewById(R.id.et_nik)
        val etNama: TextInputEditText = findViewById(R.id.et_nama)
        val etNoHp: TextInputEditText = findViewById(R.id.et_nohp)
        val etJenisKelamin: AutoCompleteTextView = findViewById(R.id.auto_tv)
        val etTanggal: TextInputEditText = findViewById(R.id.et_ttl)
        val etLokasi: TextInputEditText = findViewById(R.id.et_lokasi)

        // Setup dropdown items for gender
        val genderItems = listOf("Laki-Laki", "Perempuan")
        val adapter = ArrayAdapter(this, R.layout.list_item, genderItems)
        etJenisKelamin.setAdapter(adapter)

        // Fetch data from SQLite and populate fields
        val cursor = db.query("com.example.lsp_assesment_jmp.model.Peserta", null, "nik=?", arrayOf(nik), null, null, null)
        if (cursor.moveToFirst()) {
            etNik.setText(cursor.getString(cursor.getColumnIndexOrThrow("nik")))
            etNama.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama")))
            etNoHp.setText(cursor.getString(cursor.getColumnIndexOrThrow("nohp")))
            etJenisKelamin.setText(cursor.getString(cursor.getColumnIndexOrThrow("jenisKelamin")), false)
            etTanggal.setText(cursor.getString(cursor.getColumnIndexOrThrow("tanggalsurver")))
            etLokasi.setText(cursor.getString(cursor.getColumnIndexOrThrow("lokasi")))
        }
        cursor.close()

        findViewById<Button>(R.id.btn_update).setOnClickListener {
            updatePeserta(nik)
        }

        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            deletePeserta(nik)
        }
    }

    private fun updatePeserta(nik: String?) {
        val etNama: TextInputEditText = findViewById(R.id.et_nama)
        val etNoHp: TextInputEditText = findViewById(R.id.et_nohp)
        val etJenisKelamin: AutoCompleteTextView = findViewById(R.id.auto_tv)
        val etTanggal: TextInputEditText = findViewById(R.id.et_ttl)
        val etLokasi: TextInputEditText = findViewById(R.id.et_lokasi)

        val values = ContentValues().apply {
            put("nama", etNama.text.toString())
            put("nohp", etNoHp.text.toString())
            put("jenisKelamin", etJenisKelamin.text.toString())
            put("tanggalsurver", etTanggal.text.toString())
            put("lokasi", etLokasi.text.toString())
        }

        val count = db.update("com.example.lsp_assesment_jmp.model.Peserta", values, "nik=?", arrayOf(nik))
        if (count > 0) {
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePeserta(nik: String?) {
        val count = db.delete("com.example.lsp_assesment_jmp.model.Peserta", "nik=?", arrayOf(nik))
        if (count > 0) {
            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
        }
    }
}
