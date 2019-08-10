package xyz.fadli.pasbeli.ui.addharga

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationRequest
import xyz.fadli.pasbeli.R
import xyz.fadli.pasbeli.databinding.ActivityAddHarga2Binding
import dagger.android.AndroidInjection
import javax.inject.Inject
import xyz.fadli.pasbeli.ui.splash.SplashViewModel


class AddHargaActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var locationRequest: LocationRequest
    private lateinit var permissionDialog: AlertDialog
    private lateinit var hargaViewModel: HargaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val idPasar = intent.getIntExtra(EXTRA_NAMA_PASAR, 7)
        println("cari idPasar")
        println(idPasar)
        val bible = intent.getIntExtra("idUser", 10)
        val binding: ActivityAddHarga2Binding = DataBindingUtil
                .setContentView(this, R.layout.activity_add_harga2)
        binding.lifecycleOwner = this
        hargaViewModel = ViewModelProviders.of(this, viewModelFactory).get(HargaViewModel::class.java)
        binding.vm = hargaViewModel
        hargaViewModel.idBible(bible)
        hargaViewModel.barang()
        hargaViewModel.setIdPasar(idPasar)
        hargaViewModel.setLocationReq(locationRequest)
        hargaViewModel.setAlamatDanLokasi(idPasar)
//        subscribeEvent(binding, hargaViewModel)
    }

    companion object {
        const val EXTRA_NAMA_PASAR = "NAMA PASAR"
    }
    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        super.onPause()
        requestLocation(false)

    }

    override fun onResume() {
        super.onResume()
        requestLocation(true)
    }

    private fun requestLocation(status: Boolean) {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            hargaViewModel.startReqLocation(status)
        } else {
            permissionDialog.show()
        }
    }

    @Suppress("UNUSED_PARAMETER") //view required by xml
    fun onFabClick(view: View) {

        val nam = hargaViewModel.nama.value
        val kua = hargaViewModel.kualitas.value
        val har = hargaViewModel.hargaBarang.value
        println("onFabClick di addhargaactivity")
        if(nam != null && kua != null && har != null)
            {showDialog1()}
        hargaViewModel.onFabClick()
        showDialog()
    }

    private fun showDialog1() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Salah")
        alertDialogBuilder
                .setMessage("Mohon Ulangi Kembali")
                .setCancelable(false)
                .setPositiveButton("Kembali") { _, _ ->
                    finish()
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("BERHASIL")
        alertDialogBuilder
                .setMessage("Terima Kasih Sudah Berkontribusi")
                .setCancelable(false)
                .setPositiveButton("Kembali") { _, _ ->
                    finish()
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}