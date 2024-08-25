package com.example.lsp_assesment_jmp.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lsp_assesment_jmp.R
import com.example.lsp_assesment_jmp.ui.DetailPeserta

class PesertaAdapter(private var listPeserta: List<Peserta>) :
    RecyclerView.Adapter<PesertaAdapter.PesertaViewHolder>() {

    // ViewHolder to bind views
    class PesertaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nikView: TextView = itemView.findViewById(R.id.textNik)
        val namaView: TextView = itemView.findViewById(R.id.textNama)
        val noHpView: TextView = itemView.findViewById(R.id.textNomorHandphone)
        val jkView: TextView = itemView.findViewById(R.id.textJenisKelamin)
        val tanggalView: TextView = itemView.findViewById(R.id.textTanggalSurvey)
        val lokasiView: TextView = itemView.findViewById(R.id.textLokasi)
        val imgView: ImageView = itemView.findViewById(R.id.ivPlaceholder) // Changed to ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesertaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false)
        return PesertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PesertaViewHolder, position: Int) {
        val peserta = listPeserta[position]

        // Handle image with Glide
        Glide.with(holder.itemView.context)
            .load(peserta.gambar) // Load the image URI
            .into(holder.imgView) // Display it in the ImageView

        holder.nikView.text = peserta.nik
        holder.namaView.text = peserta.nama
        holder.noHpView.text = peserta.nohp
        holder.jkView.text = peserta.jenisKelamin
        holder.tanggalView.text = peserta.tanggal
        holder.lokasiView.text = peserta.lokasi

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPeserta::class.java).apply {
                putExtra("EXTRA_NIK", peserta.nik)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listPeserta.size
    }

    fun refreshData(newList: List<Peserta>) {
        listPeserta = newList
        notifyDataSetChanged()
    }
}
