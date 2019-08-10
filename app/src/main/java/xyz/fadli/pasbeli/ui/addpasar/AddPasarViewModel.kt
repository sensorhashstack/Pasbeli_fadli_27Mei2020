package xyz.fadli.pasbeli.ui.addpasar

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.location.Location
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import xyz.fadli.pasbeli.entity.Resource
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.model.AddPasar
import javax.inject.Inject

//class AddPasarViewModel : ViewModel() {
class AddPasarViewModel @Inject internal constructor(
        private val locationClient: FusedLocationProviderClient,
        private val webService: WebService
)  : ViewModel() {
//    val isLogin
    val namaPasarBaru = MutableLiveData<String>()
    val spinner_add_pasar = MutableLiveData<String>()
    val alamatPasarBaru = MutableLiveData<String>()
    private val errorNotif = MutableLiveData<String>()
    private val insertStatus = MutableLiveData<Resource<String>>()
    val imageUrl = MutableLiveData<Drawable>()
    val location = MutableLiveData<Location>()
    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null

    /**x`
     * used by databinding in fab click
     */



    internal fun save() {
        println("bebek")
        val location = this.location.value
        val namaPasarBaru = namaPasarBaru.value
        val alamatPasarBaru = alamatPasarBaru.value
        val image = imageUrl.value
        val spinner = spinner_add_pasar.value
        println(location)
        println(namaPasarBaru)
        println(alamatPasarBaru)
        println(image)
        println(spinner)
        if (location == null || location.latitude == 0.0 || location.longitude == 0.0){
            errorNotif.postValue(ERROR_LOCATION)
            if (location != null) {
                println(location.latitude)
            }
            if (location != null) {
                println(location.longitude)
            }
        }
        else if (TextUtils.isEmpty(namaPasarBaru))
            errorNotif.postValue(ERROR_NAMA_PASAR_BARU)

        else if (TextUtils.isEmpty(alamatPasarBaru))
            errorNotif.postValue(ERROR_ALAMAT_PASAR_BARU)
        else if (namaPasarBaru != null && alamatPasarBaru != null) {
            println(namaPasarBaru)
            println(alamatPasarBaru)
            Completable
                .fromAction {
                    val newAddPasar = AddPasar(namaPasarBaru,alamatPasarBaru, location.latitude, location.longitude, 0)
                    webService.sendAddPasarFadli(newAddPasar).execute()
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
        }
    }


    companion object {
        const val ERROR_NAMA_PASAR_BARU = "ERROR NAMA PASAR BARU"
        const val ERROR_ALAMAT_PASAR_BARU = "ERROR ALAMAT PASAR BARU"
        const val ERROR_LOCATION = "ERROR LOCATION"
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

    internal fun setLocationReq(locRequest: LocationRequest) {
        locationRequest = locRequest
    }
}