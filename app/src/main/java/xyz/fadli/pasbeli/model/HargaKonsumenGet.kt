package xyz.fadli.pasbeli.model


import androidx.room.ColumnInfo


/**
 * @author Mahendri
 */

data class HargaKonsumenGet (
        @ColumnInfo(name = "id_barang")
        var id_entry: Int,

        @ColumnInfo(name = "id_barang")
        var id_barang: Int,

        @ColumnInfo(name = "id_tempat")
        var id_pasar: Int,

        @ColumnInfo(name = "harga")
        var harga_barang: Long,

        var version:Int
)
