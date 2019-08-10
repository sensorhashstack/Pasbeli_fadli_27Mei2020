package xyz.fadli.pasbeli.ui.popup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import xyz.fadli.pasbeli.R
import xyz.fadli.pasbeli.model.BarangHarga
import xyz.fadli.pasbeli.ui.history.TambahDataActivity
import java.sql.Date
import javax.inject.Inject


class PopupVerifikasiActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var popVM: PopVM

    override fun onCreate( savedInstanceState: Bundle?)
    {
        AndroidInjection.inject(this)
        println("PopupVerifikasiActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_verfikasi)
        popVM = ViewModelProviders.of(this, viewModelFactory).get(PopVM::class.java)
        val dm =  DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout(((width * .8).toInt()), ((height * .4).toInt()))


    }

    fun buttontongsampahmerah(view: View){
        view.animation
        showDialogPenekanan()
        System.out.println("buttontongsampahmerah")

    }

    fun buttonverifikasihijau(view: View){
        view.animation
        showDialogVerifikasi()
        System.out.println("buttonverifikasihijau")
    }

    private fun showDialogPenekanan() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("HAPUS DATA")
        alertDialogBuilder
        .setMessage("Apakah anda akan menghapus data ?")
        .setPositiveButton("Setuju") { _, _ ->
            val idData = intent.getIntExtra("idData", 1)

            println("popopverifikasi")
            println(idData)

            popVM.deleteHarga(idData)
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
                    val idData = intent.getIntExtra("idData", 1)
                    println("popopverifikasi")
                    println(idData)
                    println(idData)
                    val entityBarang = BarangHarga(idData, "a", "b", 1
                            , "c", Date(System.currentTimeMillis()))
                    println(entityBarang)
                    popVM.updateHarga(entityBarang)
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
                    startActivity(Intent(this, TambahDataActivity::class.java)
                            .putExtra("main", 3))
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        finish()
    }

}