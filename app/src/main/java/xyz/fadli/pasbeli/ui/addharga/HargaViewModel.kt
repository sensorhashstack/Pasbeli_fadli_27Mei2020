package xyz.fadli.pasbeli.ui.addharga

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import io.reactivex.Completable
import xyz.fadli.pasbeli.entity.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.entity.Resource.success
import xyz.fadli.pasbeli.model.*
import java.io.IOException
import javax.inject.Inject

/**
 * @author Mahendri
 */

class HargaViewModel @Inject internal constructor(
        private val locationClient: FusedLocationProviderClient,
        private val webService: WebService
) : ViewModel() {

    private val dataAlamatLokasi = MutableLiveData<AddPasarGet>()
    private val barangGet2 = MutableLiveData<List<BarangNamaDISTINCTGet>>()
    val kualitasList = MutableLiveData<List<KualitasUnit>>()
    val nama = MutableLiveData<String>()
    val kualitas = MutableLiveData<String>()
    val unit = MutableLiveData<String>()
    val namaTempat = MutableLiveData<String>()
    val hargaBarang = MutableLiveData<String>()
    val location = MutableLiveData<Location>()
    val satuan =MutableLiveData<String>()
    val idUser=MutableLiveData<Int>()
    val idBarang=MutableLiveData<Int>()

    val idPasar = MutableLiveData<Int>()
    private val insertStatus = MutableLiveData<Resource<String>>()

    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null

    init {
        unit.postValue("satuan")
    }

    /**
     * used by databinding in autocomplete callback
     */
    fun barang(): List<BarangNamaDISTINCTGet>? {
        var barangGetT2 = barangGet2.value
        val b = CompositeDisposable().add(webService.getallBarang().subscribe {
            s -> barangGetT2 = s
            }
        )
        b.toString()
        return barangGetT2
    }

    fun changeKualitas(adapter: AdapterView<*>?) {
        val selected: BarangNamaDISTINCTGet? = adapter?.selectedItem as BarangNamaDISTINCTGet?
        nama.postValue(selected!!.nama_barang)
        var kualit = kualitasList.value
        CompositeDisposable().add(webService.getKualitasUnit(selected.nama_barang).subscribe( {
                s -> kualit = s
                insertStatus.postValue(success("NAMA BARANG TERSEDIA"))
            } ){
                insertStatus.postValue(Resource.error(it.message, "MAAF NAMA BARANG TIDAK TERSEDIA"))
        })
        kualitasList.postValue(kualit)
    }

    fun changeSelection(adapter: AdapterView<*>?) {
        val selected: KualitasUnit? = adapter?.selectedItem as KualitasUnit?
        if (selected == null) {
            kualitas.postValue(null)
            unit.postValue("satuan")
        } else {
            kualitas.postValue(selected.kualitas_barang)
            unit.postValue("per ${selected.satuan_barang}")
            satuan.postValue(selected.satuan_barang)
            idBarang.postValue(selected.id_barang)
        }
    }

    /**
     * used by databinding in fab click
     */
    internal fun onFabClick():Boolean {
        return try {
            println("OnFacClick 2")
            val currentHarga = hargaBarang.value
            val idPasarf = idPasar.value
            val idBarang = idBarang.value
            val post = HargaKonsumen(idBarang!!, idPasarf!!, currentHarga!!.toInt(),  idUser.value!!,0)
            val response = Completable
                    .fromAction {
                        webService.postHargaBarang(post).execute()
                    }
                    .subscribeOn(Schedulers.computation())
                    .doOnSubscribe {
                        insertStatus.postValue(Resource.loading(null))
                    }
                    .subscribe({
                        insertStatus.postValue(Resource.success("berhasil menambahkan"))
                    }, {
                        insertStatus.postValue(Resource.error(it.message, null))
                    })

            response.isDisposed
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("OnFacClick", e.message)
            false
        }
    }

    @SuppressLint("MissingPermission")
    internal fun startReqLocation(isAllowed: Boolean) {
        if (isAllowed) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    p0?.let {
                        location.postValue(it.lastLocation)
                    }
                }
            }

            locationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        } else {
            locationCallback?.apply {
                locationClient.removeLocationUpdates(this)
            }
        }
    }

    internal fun idBible(idBible: Int) {
        idUser.postValue(idBible)
    }

    internal fun setIdPasar(id: Int) {
        idPasar.postValue(id)
    }

    internal fun setLocationReq(locRequest: LocationRequest) {
        locationRequest = locRequest
    }

    internal fun setAlamatDanLokasi(idPasar:Int): AddPasarGet? {
        println("setAlamatDanLokasi")
        println(idPasar)
        var data = dataAlamatLokasi.value
        val b = webService.getPasarBaru(idPasar).subscribe {
            s->data = s
        }
        b.isDisposed
        namaTempat.postValue(data!!.nama_pasar)
//        alamat.postValue(data!!.alamat_pasar)
//        location.postValue(data!!.location.toString())
//        latitude.postValue(data!!.latitude_pasar)
//        longitude.postValue(data!!.longitude_pasar)
//        version.postValue(data!!.version_pasar)
        return data
    }
}