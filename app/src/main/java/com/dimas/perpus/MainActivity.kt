package com.dimas.perpus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTambahBuku = findViewById<CardView>(R.id.btnTambahBukuCard)
        val btnLihatBuku = findViewById<CardView>(R.id.btnLihatBukuCard)
        val btnTentang = findViewById<CardView>(R.id.btnTentangCard)
        val btnKeluar = findViewById<CardView>(R.id.btnKeluarCard)
        val btnMusic = findViewById<CardView>(R.id.btnMusic)

        btnMusic.setOnClickListener {
            android.widget.Toast.makeText(this, "Fitur Multimedia (Music) Segera Hadir!", android.widget.Toast.LENGTH_SHORT).show()
        }

        btnTambahBuku.setOnClickListener {
            startActivity(Intent(this, FormBukuActivity::class.java))
        }

        btnLihatBuku.setOnClickListener {
            startActivity(Intent(this, DataBukuActivity::class.java)) // Intent Aktif
        }

        btnTentang.setOnClickListener {
            startActivity(Intent(this, TentangActivity::class.java)) // Intent Aktif
        }

        btnKeluar.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Keluar")
            builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            builder.setPositiveButton("Ya") { dialog, _ ->
                dialog.dismiss()
                finishAffinity() // Keluar dari Aplikasi
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }
}
