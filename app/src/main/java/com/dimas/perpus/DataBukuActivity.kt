package com.dimas.perpus

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DataBukuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var rvBuku: RecyclerView
    private lateinit var adapter: BukuAdapter
    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_buku)

        dbHelper = DatabaseHelper(this)
        rvBuku = findViewById(R.id.rvBuku)
        etSearch = findViewById(R.id.etSearch)

        rvBuku.layoutManager = LinearLayoutManager(this)

        val btnBack = findViewById<androidx.cardview.widget.CardView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }
        
        val btnAdd = findViewById<androidx.cardview.widget.CardView>(R.id.btnAdd)
        btnAdd.setOnClickListener { 
            startActivity(android.content.Intent(this, FormBukuActivity::class.java)) 
        }

        loadDataBuku() // Muat data pertama kali

        // Fitur Pencarian / Search (Poin Bonus)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isNotEmpty()) {
                    val searchResult = dbHelper.searchBuku(keyword)
                    adapter.updateData(searchResult)
                    
                    val tvTotalBooks = findViewById<android.widget.TextView>(R.id.tvTotalBooks)
                    tvTotalBooks.text = "${searchResult.size} buku ditemukan"
                } else {
                    loadDataBuku() // Kembalikan ke seluruh data jika search kosong
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadDataBuku() // Agar data selalu diperbarui saat kembali dari halaman edit
    }

    private fun loadDataBuku() {
        val listBuku = dbHelper.getAllBuku()
        val tvTotalBooks = findViewById<android.widget.TextView>(R.id.tvTotalBooks)
        tvTotalBooks.text = "${listBuku.size} buku terdaftar"
        
        adapter = BukuAdapter(this, listBuku)
        rvBuku.adapter = adapter
    }
}