package com.dimas.perpus

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "perpustakaan.db" // Sesuai permintaan PDF
        const val TABLE_BUKU = "buku"

        const val COLUMN_ID = "id"
        const val COLUMN_KODE = "kode_buku"
        const val COLUMN_JUDUL = "judul"
        const val COLUMN_PENULIS = "penulis"
        const val COLUMN_TAHUN = "tahun"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE " + TABLE_BUKU + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_KODE + " TEXT,"
                + COLUMN_JUDUL + " TEXT,"
                + COLUMN_PENULIS + " TEXT,"
                + COLUMN_TAHUN + " TEXT" + ")")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_BUKU)
        onCreate(db)
    }

    fun insertBuku(buku: Buku): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_KODE, buku.kodeBuku)
        values.put(COLUMN_JUDUL, buku.judul)
        values.put(COLUMN_PENULIS, buku.penulis)
        values.put(COLUMN_TAHUN, buku.tahun)
        val success = db.insert(TABLE_BUKU, null, values)
        db.close()
        return success
    }

    fun getAllBuku(): ArrayList<Buku> {
        val bukuList: ArrayList<Buku> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_BUKU"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val kode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KODE))
                val judul = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JUDUL))
                val penulis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENULIS))
                val tahun = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAHUN))
                bukuList.add(Buku(id, kode, judul, penulis, tahun))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return bukuList
    }

    fun updateBuku(buku: Buku): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_JUDUL, buku.judul)
        values.put(COLUMN_PENULIS, buku.penulis)
        values.put(COLUMN_TAHUN, buku.tahun)

        val success = db.update(TABLE_BUKU, values, "$COLUMN_KODE=?", arrayOf(buku.kodeBuku))
        db.close()
        return success
    }

    fun deleteBuku(kodeBuku: String): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_BUKU, "$COLUMN_KODE=?", arrayOf(kodeBuku))
        db.close()
        return success
    }

    fun generateKodeBuku(): String {
        val db = this.readableDatabase
        // Ambil semua angka dari kode_buku secara berurutan
        val query = "SELECT CAST(SUBSTR($COLUMN_KODE, 3) AS INTEGER) as num FROM $TABLE_BUKU ORDER BY num ASC"
        val cursor = db.rawQuery(query, null)
        
        var expectedNum = 1
        if (cursor.moveToFirst()) {
            do {
                val actualNum = cursor.getInt(0)
                if (actualNum > expectedNum) {
                    // Ditemukan angka yang terlewat (gap)!
                    break
                } else if (actualNum == expectedNum) {
                    expectedNum++
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return "BK" + String.format("%03d", expectedNum)
    }

    fun searchBuku(keyword: String): ArrayList<Buku> {
        val bukuList: ArrayList<Buku> = ArrayList()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_BUKU WHERE $COLUMN_KODE LIKE '%$keyword%' OR $COLUMN_JUDUL LIKE '%$keyword%' OR $COLUMN_PENULIS LIKE '%$keyword%'"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val kode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KODE))
                val judul = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JUDUL))
                val penulis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENULIS))
                val tahun = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAHUN))
                bukuList.add(Buku(id, kode, judul, penulis, tahun))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return bukuList
    }
}