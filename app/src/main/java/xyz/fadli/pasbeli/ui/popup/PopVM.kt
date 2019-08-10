package xyz.fadli.pasbeli.ui.popup

import android.util.Log
import androidx.lifecycle.ViewModel
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.model.BarangHarga
import java.io.IOException
import javax.inject.Inject

class PopVM  @Inject constructor (
        private val webService: WebService
) :  ViewModel() {

    fun updateHarga(harga: BarangHarga): Boolean {
        return try {
            println("updateHarga")
            val response = webService.updateHarga(harga).execute()
            response.isSuccessful
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("onButtonVerifikasi", e.message)
            false
        }
    }

    fun deleteHarga(idDataInt: Int) {
        println("deleteHarga")
        val response = webService.deleteHarga(idDataInt).execute()
        response.isSuccessful
    }
}