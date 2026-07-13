package com.dimas.perpus

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class FormBukuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etKodeBuku: TextInputEditText
    private lateinit var etJudulBuku: TextInputEditText
    private lateinit var etPenulis: TextInputEditText
    private lateinit var etTahun: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_buku)

        dbHelper = DatabaseHelper(this)

        etKodeBuku = findViewById(R.id.etKodeBuku)
        etJudulBuku = findViewById(R.id.etJudulBuku)
        etPenulis = findViewById(R.id.etPenulis)
        etTahun = findViewById(R.id.etTahun)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val layoutEditDelete = findViewById<android.widget.LinearLayout>(R.id.layoutEditDelete)

        val btnBack = findViewById<androidx.cardview.widget.CardView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        // Menerima intent jika diklik dari DataBukuActivity (List Buku) untuk keperluan Edit/Delete
        val intentKode = intent.getStringExtra("KODE")
        if (intentKode != null) {
            // Mode EDIT: Sembunyikan tombol Simpan, Tampilkan Update/Delete
            btnSimpan.visibility = android.view.View.GONE
            layoutEditDelete.visibility = android.view.View.VISIBLE

            etKodeBuku.setText(intentKode)
            etKodeBuku.isEnabled = false // Kode Buku tidak boleh diedit (sebagai Primary Key semu)
            etJudulBuku.setText(intent.getStringExtra("JUDUL"))
            etPenulis.setText(intent.getStringExtra("PENULIS"))
            etTahun.setText(intent.getStringExtra("TAHUN"))
        } else {
            // Mode TAMBAH: Tampilkan tombol Simpan, Sembunyikan Update/Delete
            btnSimpan.visibility = android.view.View.VISIBLE
            layoutEditDelete.visibility = android.view.View.GONE
            
            etKodeBuku.setText(dbHelper.generateKodeBuku())
            etKodeBuku.isEnabled = false
        }

        btnSimpan.setOnClickListener {
            if (validasiInput()) {
                val buku = Buku(
                    kodeBuku = etKodeBuku.text.toString(),
                    judul = etJudulBuku.text.toString(),
                    penulis = etPenulis.text.toString(),
                    tahun = etTahun.text.toString()
                )
                val result = dbHelper.insertBuku(buku)
                if (result > -1) {
                    Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    kosongkanForm()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnUpdate.setOnClickListener {
            if (validasiInput()) {
                val buku = Buku(
                    kodeBuku = etKodeBuku.text.toString(),
                    judul = etJudulBuku.text.toString(),
                    penulis = etPenulis.text.toString(),
                    tahun = etTahun.text.toString()
                )
                val result = dbHelper.updateBuku(buku)
                if (result > 0) {
                    Toast.makeText(this, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke layar sebelumnya
                } else {
                    Toast.makeText(this, "Gagal update data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            val kode = etKodeBuku.text.toString()
            if (kode.isEmpty()) {
                Toast.makeText(this, "Kode buku tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                tampilkanDialogHapus(kode)
            }
        }
    }

    private fun validasiInput(): Boolean {
        // Validasi spesifik sesuai permintaan UAS
        if (etKodeBuku.text.toString().isEmpty()) {
            Toast.makeText(this, "Kode buku tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etJudulBuku.text.toString().isEmpty()) {
            Toast.makeText(this, "Judul buku tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etPenulis.text.toString().isEmpty()) {
            Toast.makeText(this, "Penulis buku tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etTahun.text.toString().isEmpty()) {
            Toast.makeText(this, "Tahun terbit tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun tampilkanDialogHapus(kodeBuku: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Hapus") // Poin Bonus
        builder.setMessage("Apakah Anda yakin ingin menghapus buku dengan kode $kodeBuku?")

        builder.setPositiveButton("Ya") { dialog, _ ->
            val result = dbHelper.deleteBuku(kodeBuku)
            if (result > 0) {
                Toast.makeText(this, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                kosongkanForm()
                finish() // Kembali
            } else {
                Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun kosongkanForm() {
        etKodeBuku.setText(dbHelper.generateKodeBuku())
        etJudulBuku.setText("")
        etPenulis.setText("")
        etTahun.setText("")
        etKodeBuku.isEnabled = false
    }
}
