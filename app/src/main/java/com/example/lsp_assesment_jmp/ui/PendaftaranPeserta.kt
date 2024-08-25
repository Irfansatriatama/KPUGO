package com.example.lsp_assesment_jmp.ui

import com.example.lsp_assesment_jmp.model.Peserta
import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lsp_assesment_jmp.R
import com.example.lsp_assesment_jmp.databinding.ActivityPendaftaranPesertaBinding
import com.example.lsp_assesment_jmp.model.SqliteHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Locale

class PendaftaranPeserta : AppCompatActivity() {

    private lateinit var binding: ActivityPendaftaranPesertaBinding
    private lateinit var db: SqliteHelper
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val IMAGE_REQUEST_CODE = 1000
        private const val CAMERA_REQUEST_CODE = 1001
    }

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendaftaranPesertaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SqliteHelper
        db = SqliteHelper(this)

        // Dropdown setup
        val items = listOf("Laki-Laki", "Perempuan")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.autoTv.setAdapter(adapter)

        // Inisialisasi fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Handle image selection from gallery
        binding.btnGallery.setOnClickListener {
            pickImageFromGallery()
        }

        // Handle image capture from camera
        binding.btnCam.setOnClickListener {
            checkCameraPermission()
        }

        // Handle date picker
        binding.btnTtl.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                binding.etTtl.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        // Handle form submission
        binding.btnSubmit.setOnClickListener {
            saveData()
        }

        // Handle location retrieval
        binding.btnLokasi.setOnClickListener {
            getLocation()
        }
    }

    private fun validateInput(): Boolean {
        val nik = binding.etNik.text.toString().trim()
        val nama = binding.etNama.text.toString().trim()
        val nohp = binding.etNohp.text.toString().trim()
        val jenisKelamin = binding.autoTv.text.toString().trim()
        val tanggalsurvey = binding.etTtl.text.toString().trim()
        val lokasi = binding.etLokasi.text.toString().trim()

        if (nik.isEmpty()) {
            binding.etNik.error = "NIK harus diisi"
            Toast.makeText(this, "NIK harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (nama.isEmpty()) {
            binding.etNama.error = "Nama harus diisi"
            Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (nohp.isEmpty()) {
            binding.etNohp.error = "Nomor HP harus diisi"
            Toast.makeText(this, "Nomor HP harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (jenisKelamin.isEmpty()) {
            binding.autoTv.error = "Jenis kelamin harus dipilih"
            Toast.makeText(this, "Jenis kelamin harus dipilih", Toast.LENGTH_SHORT).show()
            return false
        }

        if (tanggalsurvey.isEmpty()) {
            binding.etTtl.error = "Tanggal lahir harus diisi"
            Toast.makeText(this, "Tanggal lahir harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lokasi.isEmpty()) {
            binding.etLokasi.error = "Lokasi harus diisi"
            Toast.makeText(this, "Lokasi harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageUri == null) {
            Toast.makeText(this, "Silakan pilih atau ambil gambar", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveData() {
        if (!validateInput()) {
            return
        }

        val nik = binding.etNik.text.toString().trim()
        val nama = binding.etNama.text.toString().trim()
        val nohp = binding.etNohp.text.toString().trim()
        val jenisKelamin = binding.autoTv.text.toString().trim()
        val tanggalsurvey = binding.etTtl.text.toString().trim()
        val lokasi = binding.etLokasi.text.toString().trim()
        val gambar = imageUri.toString().trim()

        // Convert the imageUri to a String only if it's not null
        val imgUri = Uri.parse(gambar)

        if (gambar == null) {
            Toast.makeText(this, "Gambar tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val peserta = Peserta(
            id = 0,
            nik = nik,
            nama = nama,
            nohp = nohp,
            jenisKelamin = jenisKelamin,
            tanggal = tanggalsurvey,
            lokasi = lokasi,
            gambar = imgUri
        )

        val success = db.addPeserta(peserta)
        if (success) {
            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_REQUEST_CODE
            )
        } else {
            captureImage()
        }
    }

    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Tidak ada aplikasi yang dapat digunakan untuk mengambil gambar", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                IMAGE_REQUEST_CODE -> {
                    imageUri = data?.data
                    binding.imagePreview.setImageURI(imageUri)
                }

                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    binding.imagePreview.setImageBitmap(imageBitmap)
                    imageUri = saveImageToExternalStorage(imageBitmap)
                }
            }
        }
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap?): Uri {
        val imageName = "IMG_${System.currentTimeMillis()}.jpg"
        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageName)
        FileOutputStream(imageFile).use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return Uri.fromFile(imageFile)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showPermissionDeniedDialog()
                } else {
                    Toast.makeText(this, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Izin Diperlukan")
            .setMessage("Izin kamera diperlukan untuk mengambil gambar. Silakan aktifkan di pengaturan aplikasi.")
            .setPositiveButton("Buka Pengaturan") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        val loc = fusedLocationProviderClient.lastLocation
        loc.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address: String = addresses[0].getAddressLine(0)
                    binding.etLokasi.setText(address)
                }
            } else {
                Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}