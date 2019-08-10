package xyz.fadli.pasbeli.ui.main

import android.location.Location
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.fadli.pasbeli.util.AppUtil
import io.reactivex.disposables.CompositeDisposable
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.model.AddPasarGet
import javax.inject.Inject

/**
 * @author Mahendri
 */

class MainViewModel @Inject constructor(
        private val webService: WebService
) : ViewModel() {

    val data = MutableLiveData<Int>()
    val iduser = MutableLiveData<Int>()
    val pasar = ObservableField<AddPasarGet>()
    val distance = ObservableField<String>()
    internal val addPasar = MutableLiveData<String>()
    internal val intentAdd = MutableLiveData<String>()
    internal val intentAdd1 = MutableLiveData<Int>()
    internal val editIDPASAR = MutableLiveData<Int>()

//    internal val tambahPasarNama = MutableLiveData<String>()
//    private val tambahPasarAlamat = MutableLiveData<String>()
//    private val tambahPasarLokasi = MutableLiveData<String>()
    internal val lihatHargaPasar = MutableLiveData<Int>()
    private val disposable = CompositeDisposable()
    private val mutableLiveData = MutableLiveData<List<AddPasarGet>>()

    fun getPasarAdmin(): List<AddPasarGet>? {
        val listPasarAdmin = webService.getPasarAdmin()
        var mutable = mutableLiveData.value
        val web = disposable.add(listPasarAdmin.subscribe { s -> mutable = s })
        web.toString()
        return mutable
    }

    override fun onCleared() {
        disposable.dispose()
    }

    fun dataFromSplash(id:Int, idser:Int){
        data.postValue(id)
        iduser.postValue(idser)
    }

    internal fun openSheet(currentLocation: Location, select: AddPasarGet) {
        pasar.set(select)
        distance.set(String.format("%s KM", AppUtil.toKm(currentLocation, select.location)))
    }

    /**
     * Called by the Data Binding library, bottom sheet click listener.
     */
    fun onAddClick() {
        intentAdd1.postValue(pasar.get()?.id_pasar)
        intentAdd.postValue(pasar.get()?.nama_pasar)
    }


    fun lihatHargaPasar(){
        lihatHargaPasar.postValue(pasar.get()!!.id_pasar)
    }

    fun onEditPasar() {
        editIDPASAR.postValue(pasar.get()!!.id_pasar)
    }
}
