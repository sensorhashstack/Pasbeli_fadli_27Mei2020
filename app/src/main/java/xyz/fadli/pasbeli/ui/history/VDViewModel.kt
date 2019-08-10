package xyz.fadli.pasbeli.ui.history


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import xyz.fadli.pasbeli.api.WebService
import xyz.fadli.pasbeli.model.BarangHarga

import javax.inject.Inject

/**
 * @author Mahendri
 */
class VDViewModel @Inject constructor (
        private val webService: WebService
) : ViewModel() {

    val isSend = ObservableBoolean(false)
    internal val verData = MutableLiveData<String>()
    private val disposable = CompositeDisposable()
    private val listBarang = MutableLiveData<List<BarangHarga>>()

    private var listBarangHarga = listBarang.value



    //sebenarnya untuk melihat harga yang dimasukkan oleh user sendiri
    fun getListHarga(id_user:Int): List<BarangHarga>? {
        println("getListHarga VDViewModel wkwk")
        val b= webService.dapatcatatan(id_user).subscribe {
            s-> listBarangHarga = s
        }
        b.isDisposed
        println("getListHarga VDViewModel")
        println(listBarangHarga)

        return listBarangHarga
    }
    override fun onCleared() {
        disposable.dispose()
    }

    fun getData(): List<BarangHarga>? {
        println("getData VDViewModel wkwk")
        val b= webService.getDataHargaView().subscribe {
            s-> listBarangHarga = s
        }
        b.isDisposed
        println("getData VDViewModel")
        println(listBarangHarga)

        return listBarangHarga
    }

    fun getDataLayoutBaru(): List<BarangHarga>? {
        println("getDataLayoutBaru VDViewModel jangkrik")
        val b= webService.getDataLayoutBaru().subscribe{
            s-> listBarangHarga = s
        }
        b.isDisposed
        println("getDataLayoutBaru VDViewModel jangkrik")
        println(listBarangHarga)
        return listBarangHarga
    }

    fun getPasarHarga(id_pasar : Int): List<BarangHarga>? {
        println("getPasarHarga VDViewModel Siap")
        val b= webService.getPasarHarga(id_pasar).subscribe{
            s-> listBarangHarga = s
        }
        b.isDisposed
        println("getPasarHarga VDViewModel Siap")
        println(listBarangHarga)
        return listBarangHarga
    }

}