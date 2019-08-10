package xyz.fadli.pasbeli.model


import androidx.room.ColumnInfo


/**
 * @author Mahendri
 */

data class HargaKonsumen (

        @ColumnInfo(name = "id_barang")
        var id_barang: Int,

        @ColumnInfo(name = "id_tempat")
        var id_pasar: Int,

        @ColumnInfo(name = "harga")
        var harga_barang: Int,

        @ColumnInfo(name = "id_user")
        var id_user :Int,

        var version_harga:Int
)
