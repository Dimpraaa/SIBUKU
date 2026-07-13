package com.dimas.perpus

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BukuAdapter(
    private val context: Context,
    private var listBuku: ArrayList<Buku>
) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKode: TextView = itemView.findViewById(R.id.tvKode)
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        val tvPenulis: TextView = itemView.findViewById(R.id.tvPenulis)
        val tvTahun: TextView = itemView.findViewById(R.id.tvTahun)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_buku, parent, false)
        return BukuViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = listBuku[position]
        holder.tvKode.text = buku.kodeBuku
        holder.tvJudul.text = buku.judul
        holder.tvPenulis.text = buku.penulis
        holder.tvTahun.text = buku.tahun

        // Saat item buku diklik, buka FormBukuActivity dan lempar datanya (untuk update/delete)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FormBukuActivity::class.java)
            intent.putExtra("KODE", buku.kodeBuku)
            intent.putExtra("JUDUL", buku.judul)
            intent.putExtra("PENULIS", buku.penulis)
            intent.putExtra("TAHUN", buku.tahun)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listBuku.size
    }

    fun updateData(newList: ArrayList<Buku>) {
        listBuku = newList
        notifyDataSetChanged() // Refresh RecyclerView
    }
}
