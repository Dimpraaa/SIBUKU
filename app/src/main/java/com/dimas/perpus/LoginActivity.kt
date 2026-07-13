package com.dimas.perpus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text?.toString() ?: ""
            val password = etPassword.text?.toString() ?: ""

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
            } else if (username == "admin" && password == "12345") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Username atau Password salah!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}