package xyz.fadli.pasbeli.ui.editpasar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.model.AddPasarGet
import java.io.IOException
import javax.inject.Inject

/**
 * @author Mahendri
 */

class EditPasarViewModel @Inject internal constructor(
        private val webService: WebService
) : ViewModel() {

    private val idPasarSet = MutableLiveData<Int>()
    val nama = MutableLiveData<String>()
    val alamat = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    private val latitude = MutableLiveData<Double>()
    private val longitude = MutableLiveData<Double>()
    val version = MutableLiveData<Int>()
    private val dataAlamatLokasi = MutableLiveData<AddPasarGet>()
    internal val errorNotif = MutableLiveData<Int>()

    fun onButtonVerifikasi(): Boolean {
        return try {
//            val pasar = setAlamatDanLokasi(nama.value!!)
//            val pasar1 = AddPasar(pasar!!.nama_pasar, pasar.alamat_pasar, pasar.latitude_pasar,
//                    pasar.longitude_pasar, pasar.version_pasar)
            val pasar1 = AddPasarGet(idPasarSet.value!!, nama.value!!, alamat.value!!, latitude.value!!,
                    longitude.value!!, version.value!!)
            println("onButtonVerifikasi")

            println(pasar1)
            val response = webService.sendEditPasar(pasar1).execute()
            response.isSuccessful
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("onButtonVerifikasi", e.message)
            false
        }
    }

    fun onButtonDelete(): Boolean {
        return try {
//            val pasar = setAlamatDanLokasi(nama.value!!)
            val idDari  = idPasarSet.value
            println("onButtonDelete")
//            println(pasar)
            println(idDari)
            val response = idDari?.let { webService.deletePasar(it).execute() }
            response!!.isSuccessful
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("onButtonVerifikasi", e.message)
            false
        }
    }

    internal fun setAlamatDanLokasi(idPasar:Int): AddPasarGet? {
        println("setAlamatDanLokasi")
        println(idPasar)
        var data = dataAlamatLokasi.value
        val b = webService.getPasarBaru(idPasar).subscribe {
            s->data = s
        }
        b.isDisposed
        idPasarSet.postValue(data!!.id_pasar)
        nama.postValue(data!!.nama_pasar)
        alamat.postValue(data!!.alamat_pasar)
        location.postValue(data!!.location.toString())
        latitude.postValue(data!!.latitude_pasar)
        longitude.postValue(data!!.longitude_pasar)
        version.postValue(data!!.version_pasar)
        return data
    }
}