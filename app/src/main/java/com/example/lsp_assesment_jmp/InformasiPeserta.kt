package com.example.lsp_assesment_jmp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lsp_assesment_jmp.databinding.ActivityInformasiPesertaBinding
import kotlinx.coroutines.launch

class InformasiPeserta : AppCompatActivity() {

    private lateinit var binding: ActivityInformasiPesertaBinding
    private lateinit var db: SqliteHelper
    private lateinit var pesertaAdapter: PesertaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInformasiPesertaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = SqliteHelper(this)
        pesertaAdapter = PesertaAdapter(db.viewPeserta(), this)

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(this@InformasiPeserta)
            adapter = pesertaAdapter
        }


    }
    private fun refreshData() {
        lifecycleScope.launch {
            pesertaAdapter.refreshData(db.viewPeserta())
        }
    }
    override fun onResume() {
        super.onResume()
        pesertaAdapter.refreshData(db.viewPeserta())
    }
}
