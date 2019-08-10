package xyz.fadli.pasbeli.ui.editpasar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import xyz.fadli.pasbeli.R
import dagger.android.AndroidInjection
import javax.inject.Inject
import android.content.Intent
import xyz.fadli.pasbeli.databinding.ActivityEditPasarBinding
import xyz.fadli.pasbeli.ui.main.MainActivity

class EditPasarActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var editPasarViewModel: EditPasarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val idPasar = intent.getIntExtra("idPasar",7)
        println("EditPasarActivity")
        println(idPasar)
        val binding:  ActivityEditPasarBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_edit_pasar)
        binding.lifecycleOwner = this

        editPasarViewModel = ViewModelProviders.of(this, viewModelFactory).get(EditPasarViewModel::class.java)
        binding.editVM = editPasarViewModel


        editPasarViewModel.setAlamatDanLokasi(idPasar)
//        editPasarViewModel.setLocationReq(locationRequest)

        subscribeEvent(editPasarViewModel)
    }

    override fun onBackPressed() {
//        if (permissionDialog.isShowing) {
//            permissionDialog.dismiss()
//            finish()
//        } else
//            super.onBackPressed()
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
            status.toString()
        } else {
        }
    }

    private fun subscribeEvent(hargaViewModel: EditPasarViewModel) {
        hargaViewModel.run {
            errorNotif.observe(this@EditPasarActivity, Observer {
                if (it == NORMAL) return@Observer

                errorNotif.postValue(NORMAL)
                val status: String = when (it) {
                    ERROR_LOCATION ->  "Kesalahan dalam mengambil lokasi"
                    ERROR_HARGA -> "Pastikan harga barang benar"
                    ERROR_NAMA -> "Nama barang kosong"
                    ERROR_KUALITAS -> "Pilih jenis barang"
                    else -> ""
                }
                if (status.isNotEmpty())
                    Toast.makeText(this@EditPasarActivity, status, Toast.LENGTH_SHORT).show()
            })
        }

    }

    companion object {
        const val NORMAL = 0
        const val ERROR_LOCATION = 1
        const val ERROR_NAMA = 2
        const val ERROR_KUALITAS = 3
        const val ERROR_HARGA = 4
    }

    fun onButtonDelete(view:View){
        view.animation
        showDialogPenekanan()
    }

    fun onButtonVerifikasi(view:View){
        view.animation
        showDialogVerifikasi()
    }

    private fun showDialogPenekanan() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("HAPUS DATA")
        alertDialogBuilder
                .setMessage("Apakah anda akan menghapus data ?")
                .setPositiveButton("Setuju") { _, _ ->
                    editPasarViewModel.onButtonDelete()
                    showDialog()
                }
                .setNegativeButton("Kembali") { _, _ ->
                    finish()
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showDialogVerifikasi() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("VERIFIKASI")
        alertDialogBuilder
                .setMessage("Apakah anda akan menyetujui data berikut sebagai Pasar?")
                .setPositiveButton("Setuju") { _, _ ->
                    editPasarViewModel.onButtonVerifikasi()
                    showDialog()
                }
                .setNegativeButton("Kembali") { _, _ ->
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
                    startActivity(Intent(this, MainActivity::class.java))
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


}