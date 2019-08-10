package xyz.fadli.pasbeli.model

import com.google.android.gms.maps.model.LatLng

data class AddPasar(
        var nama_pasar: String,
        var alamat_pasar: String,
        var latitude_pasar: Double,
        var longitude_pasar: Double,
        var version_pasar: Int = 0
){
    val location: LatLng
        get() = LatLng(latitude_pasar, longitude_pasar)
}

