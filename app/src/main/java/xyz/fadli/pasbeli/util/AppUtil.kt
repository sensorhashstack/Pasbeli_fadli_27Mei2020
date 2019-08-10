package xyz.fadli.pasbeli.util

import android.location.Location

import com.google.android.gms.maps.model.LatLng

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @author Mahendri
 */

object AppUtil {

    fun toKm(currentLocation: Location, latLng: LatLng): String {

        // konversi LatLng ke Location
        val loc = Location("")
        loc.latitude = latLng.latitude
        loc.longitude = latLng.longitude
        val distance = currentLocation.distanceTo(loc) / 1000

        // pembulatan distance
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.roundingMode = RoundingMode.CEILING
        return decimalFormat.format(distance.toDouble())
    }

}
