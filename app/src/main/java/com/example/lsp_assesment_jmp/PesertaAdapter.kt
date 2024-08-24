package com.example.lsp_assesment_jmp

import Peserta
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PesertaAdapter(private var listPeserta: List<Peserta>, private val context: Context) :
    RecyclerView.Adapter<PesertaAdapter.PesertaViewHolder>() {

    // ViewHolder to bind views
    class PesertaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nikView: TextView = itemView.findViewById(R.id.et_nik)      // NIK
        val namaView: TextView = itemView.findViewById(R.id.et_nama)    // Nama
        val noHpView: TextView = itemView.findViewById(R.id.et_nohp)    // No HP
        val jkView: TextView = itemView.findViewById(R.id.auto_tv)      // Jenis Kelamin
        val tglView: TextView = itemView.findViewById(R.id.et_ttl)      // Tanggal Lahir
        val lokasiView: TextView = itemView.findViewById(R.id.et_lokasi) // Lokasi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesertaViewHolder {
        // Inflate layout item_form.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false)
        return PesertaViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Return the size of the list
        return listPeserta.size
    }

    override fun onBindViewHolder(holder: PesertaViewHolder, position: Int) {
        // Bind data from listPeserta to the views
        val peserta = listPeserta[position]

        holder.namaView.text = peserta.nama
        holder.nikView.text = peserta.nik
        holder.noHpView.text = peserta.nohp
        holder.jkView.text = peserta.jenisKelamin
        holder.tglView.text = peserta.tanggal
        holder.lokasiView.text = peserta.lokasi
    }

    // Function to refresh data in the adapter
    fun refreshData(newPeserta: List<Peserta>) {
        listPeserta = newPeserta
        notifyDataSetChanged() // Notify that data has changed
    }
}