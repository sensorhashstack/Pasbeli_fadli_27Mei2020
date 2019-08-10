package xyz.fadli.pasbeli.ui.popup

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import xyz.fadli.pasbeli.R

class PopupSplashActivity : AppCompatActivity() {

    override fun onCreate( savedInstanceState: Bundle?)
    {
        println("PopupActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_splash)
        val dm =  DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout(((width * .8).toInt()), ((height * .4).toInt()))

    }

    override fun onBackPressed() {
        finish()
    }

}