package xyz.fadli.pasbeli.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import xyz.fadli.pasbeli.R
import xyz.fadli.pasbeli.databinding.ActivitySplashBinding
import xyz.fadli.pasbeli.helper.PermissionHelper
import xyz.fadli.pasbeli.ui.main.MainActivity
import dagger.android.AndroidInjection
import xyz.fadli.pasbeli.entity.FetchStatus
import xyz.fadli.pasbeli.ui.popup.PopupSplashActivity
import javax.inject.Inject

/**
 * @author Mahendri
 */
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        splashViewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        binding.viewmodel = splashViewModel

        permissionHelper = PermissionHelper(this)
        checkAndRequestPermissions()

        startActivity(Intent(this, PopupSplashActivity::class.java))

        splashViewModel.insertStatus.observe(this@SplashActivity, Observer {
            when (it?.fetchStatus) {
                FetchStatus.SUCCESS ->
                    {Toast.makeText(this@SplashActivity, it.data, Toast.LENGTH_SHORT).show()}
                FetchStatus.ERROR ->
                    {Toast.makeText(this@SplashActivity, it.data, Toast.LENGTH_SHORT).show()}
                FetchStatus.LOADING ->
                    {Toast.makeText(this@SplashActivity, it.data, Toast.LENGTH_SHORT).show()}
            }
        })
    }


    fun mASUK(view: View){
        view.animation
        if(splashViewModel.iDName.value != null && splashViewModel.password.value != null) {
//            splashViewModel.iDName.postValue("User")
//            splashViewModel.password.postValue("User")
//        }
            val pa = splashViewModel.prepos()
            if(pa != null ){
                val spa = pa!!.status
                val setData =  Bundle()
                val sendData2 = Intent(this, MainActivity::class.java)
                setData.putInt("Data1", spa)
                setData.putInt("idUser", pa.id)
                sendData2.putExtras(setData)
                startActivity(sendData2)
            }else{
                showDialog()
            }
        }else{
            showDialog()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Kesalahan Login")
        alertDialogBuilder
                .setMessage("Anda Salah Memasukkan Nama dan Password Login, Tolong Untuk Mengulangi")
                .setCancelable(false)
                .setPositiveButton("Ulangi") { _, _ ->
                    moveTaskToBack(false)
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun checkAndRequestPermissions(): Boolean {
        permissionHelper.permissionListener {
        }
        permissionHelper.checkAndRequestPermissions()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults)
    }

    companion object {
        const val EXTRA_MAIL = "MAIL_USER"
    }
}