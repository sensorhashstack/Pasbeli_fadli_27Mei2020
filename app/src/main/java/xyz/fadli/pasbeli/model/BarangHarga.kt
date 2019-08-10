package xyz.fadli.pasbeli.model

import androidx.room.ColumnInfo
//import java.sql.Date
import java.util.*


/**
 * @author Mahendri
 */
data class BarangHarga(
        val id_entry:Int,
        val nama_barang: String,
        val kualitas_barang: String,
        val harga_barang: Long,
        @ColumnInfo(name = "nama_tempat") val nama_pasar: String,
        @ColumnInfo(name = "waktu_catat") val created_at: Date
        ) {
    val strWaktu: String get() = created_at.toString()
}