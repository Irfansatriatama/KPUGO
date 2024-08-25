package com.example.lsp_assesment_jmp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class SqliteHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    //SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){) {

    companion object{
        private const val DATABASE_NAME = "KPUGO.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "com.example.lsp_assesment_jmp.model.Peserta"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NIK = "nik"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_NO_HP = "nohp"
        private const val COLUMN_JENIS_KELAMIN = "jenisKelamin"
        private const val COLUMN_TANGGAL = "tanggalsurver"
        private const val COLUMN_LOKASI = "lokasi"
        private const val COLUMN_GAMBAR = "gambar"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NIK TEXT,
                $COLUMN_NAMA TEXT,
                $COLUMN_NO_HP TEXT,
                $COLUMN_JENIS_KELAMIN TEXT,
                $COLUMN_TANGGAL TEXT,
                $COLUMN_LOKASI TEXT,
                $COLUMN_GAMBAR TEXT
            )
        """
        db!!.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db!!.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addPeserta(peserta: Peserta): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NIK, peserta.nik)
            put(COLUMN_NAMA, peserta.nama)
            put(COLUMN_NO_HP, peserta.nohp)
            put(COLUMN_JENIS_KELAMIN, peserta.jenisKelamin)
            put(COLUMN_TANGGAL, peserta.tanggal)
            put(COLUMN_LOKASI, peserta.lokasi)
            put(COLUMN_GAMBAR, peserta.gambar ?: "")
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()

        return result != -1L // Return true if insert was successful, false otherwise
    }

    fun viewPeserta(): List<Peserta>{
        val db = readableDatabase
        val listPeserta = mutableListOf<Peserta>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nik = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIK))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
            val nohp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NO_HP))
            val jk = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS_KELAMIN))
            val tgl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL))
            val lokasi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOKASI))
            val img = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR))

            val imgUri = Uri.parse(img)

            val peserta = Peserta(id, nik, nama, nohp, jk, tgl, lokasi, imgUri.toString())
            listPeserta.add(peserta)

        }

        cursor.close()
        db.close()
        return listPeserta


    }
}