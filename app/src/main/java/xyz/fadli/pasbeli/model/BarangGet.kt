package xyz.fadli.pasbeli.model

import androidx.room.PrimaryKey

/**
 * @author Mahendri
 */


data class BarangGet(
        val id_barang : Int,
        val nama_barang: String,
        val kualitas_barang: String,
        val satuan_barang:String)
