package com.dimas.perpus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TentangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang)

        val btnBackHeader = findViewById<androidx.cardview.widget.CardView>(R.id.btnBackHeader)
        btnBackHeader.setOnClickListener {
            finish() // Kembali ke layar sebelumnya
        }
    }
}