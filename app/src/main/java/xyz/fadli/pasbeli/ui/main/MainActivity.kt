package xyz.fadli.pasbeli.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import xyz.fadli.pasbeli.R
import xyz.fadli.pasbeli.databinding.ActivityMainBinding
import xyz.fadli.pasbeli.ui.addharga.AddHargaActivity
import xyz.fadli.pasbeli.ui.addpasar.AddPasarActivity
import xyz.fadli.pasbeli.ui.splash.SplashActivity
import xyz.fadli.pasbeli.util.VectorBitmapConvert
import dagger.android.AndroidInjection
import timber.log.Timber
import xyz.fadli.pasbeli.model.AddPasarGet
import xyz.fadli.pasbeli.ui.editpasar.EditPasarActivity
import xyz.fadli.pasbeli.ui.history.TambahDataActivity
import javax.inject.Inject
import android.widget.Button as Button1

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnSuccessListener<Location>,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private var map: GoogleMap? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewmodel = mainViewModel

        //check mail account
        title = intent.getStringExtra(SplashActivity.EXTRA_MAIL)

        //intent data dari splash
        val dataFromSplash = intent.getIntExtra("Data1", 0)
        val id = intent.getIntExtra("idUser", 10)
        mainViewModel.dataFromSplash(dataFromSplash, id)
        println("pura2")
        println(dataFromSplash)
        println(id)

        //check Play Services
        val availability = GoogleApiAvailability.getInstance()
        val playStatus = availability.isGooglePlayServicesAvailable(this)
        if (playStatus == ConnectionResult.SUCCESS) {
            setupGoogleMap()
            setupBinding(binding)
            setupViewSubscibe()
        } else {
            availability.getErrorDialog(this, playStatus, FIX_PLAY_SERVICES_REQUEST).show()
        }
    }

    private fun setupGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBinding(binding: ActivityMainBinding) {
        bottomSheetBehavior = BottomSheetBehavior.from<RelativeLayout>(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupViewSubscibe() {
        val bible = intent.getIntExtra("idUser", 10)
        println("lagi coba")
        println(bible)

        mainViewModel.intentAdd1.observe(this, Observer {
            if (it == null) return@Observer
            mainViewModel.intentAdd.value = null
            val intent = Intent(this, AddHargaActivity::class.java)
            .putExtra(AddHargaActivity.EXTRA_NAMA_PASAR, it)
            .putExtra("idUser", bible)
            startActivity(intent)
        })


//        mainViewModel.intentAdd.observe(this, Observer {
//            if (it == null) return@Observer
//            mainViewModel.intentAdd.value = null
//            val intent = Intent(this, AddHargaActivity::class.java)
//            intent.putExtra(AddHargaActivity.EXTRA_NAMA_PASAR, it)
//            intent.putExtra("idUser", bible)
//            startActivity(intent)
//        })
        mainViewModel.editIDPASAR.observe(this, Observer {
            if (it == null) return@Observer
            mainViewModel.editIDPASAR.value = null
            println("setupViewSubscibe")
            println(mainViewModel.editIDPASAR.value)
            val intent = Intent(this, EditPasarActivity::class.java)
            intent.putExtra("idPasar", it)
            startActivity(intent)
        })

        mainViewModel.lihatHargaPasar.observe(this, Observer {
            if (it == null) return@Observer
            mainViewModel.lihatHargaPasar.value = null
            startActivity(Intent(this, TambahDataActivity::class.java)
                    .putExtra("main", 2).putExtra("idPasar",it))
        })


        mainViewModel.addPasar.observe(this, Observer {
            if (it == null) return@Observer

            mainViewModel.addPasar.value = null
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onPrepareOptionsMenu(menu : Menu): Boolean {
        val dataFromSplash = mainViewModel.data.value
        println("onOptionsItemSelected")
        println(dataFromSplash)
        val register = menu.findItem(R.id.data_harga_belum_terverifikasi)
        val riwayat = menu.findItem(R.id.navigation_history)
        val tambahPasar = menu.findItem(R.id.new_pasar)
        register.isVisible = dataFromSplash == 0
        riwayat.isVisible = dataFromSplash != 2
        tambahPasar.isVisible = dataFromSplash != 2
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val iduser = intent.getIntExtra("Data1", 0)
        val iduser = mainViewModel.iduser.value
        when (item.itemId) {
            R.id.navigation_history -> {
                startActivity(Intent(this, TambahDataActivity::class.java).
                        putExtra("main", 4).putExtra("Data1", iduser))
                return true
            }
            R.id.history_terverifikasi -> {
                startActivity(Intent(this, TambahDataActivity::class.java)
                        .putExtra("main", 1))
                return true
            }
            R.id.new_pasar -> {
                startActivity(Intent(this, AddPasarActivity::class.java))
                return true
            }
            R.id.data_harga_belum_terverifikasi-> {
                startActivity(Intent(this, TambahDataActivity::class.java)
                        .putExtra("main", 3).putExtra("id_user", iduser))
                return true
            }
            R.id.sign_out -> {
                startActivity(Intent(this, SplashActivity::class.java))
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkMyLocationLayer()
    }

    override fun onResume() {
        super.onResume()
        checkMyLocationLayer()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_LOCATION -> {
                // granted permission
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addMyLocation()
                }
            }
        }
    }

    private fun checkMyLocationLayer() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(binding.rootLayout, "Dibutuhkan akses lokasi untuk mendata barang",
                        Snackbar.LENGTH_SHORT).setAction("OK") {
                    // meminta permission
                    ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_PERMISSION_LOCATION)
                }.show()

            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSION_LOCATION)
            }

        } else {
            addMyLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun addMyLocation() {
        // tambahkan titik sekarang
        map?.isMyLocationEnabled = true

        // get lokasi sekarang
        fusedLocationClient.lastLocation.addOnSuccessListener(this, this)
    }

    override fun onSuccess(location: Location?) {
        if (location == null) {
            Timber.w("tidak dapat lokasi padahal setMyLocationEnabled true")
            return
        }

        val dataFromSplash = mainViewModel.data.value
        currentLocation = location
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,
                location.longitude), 15f))

        // draw marker
        setupMarker(dataFromSplash!!)

        // set listener klik pada marker dan map
        map?.setOnMapClickListener(this)
        map?.setOnMarkerClickListener(this)
    }

    private fun setupMarker(data:Int) {
        currentLocation?.let {
            map?.clear()
            val listPasar = mainViewModel.getPasarAdmin()
            listPasar.let {
                if (it == null) {
                    Timber.d("data kosong, aneh jika terkoneksi internet")
                    return
                }
                println("data setup marker")
                println(it)
                val daftarPasar = it
                for (pasar in daftarPasar) {
                    when(data){
                        0->if(pasar.version_pasar == 0){
                            val markerOptions = MarkerOptions()
                                    .position(pasar.location)
                                    .icon(VectorBitmapConvert.fromVector(this@MainActivity,
                                            R.drawable.marker_admin))
                                    .title(pasar.nama_pasar)
                            val markerAdmin = map?.addMarker(markerOptions)
                            markerAdmin?.tag = pasar
                        }
                    }
                    when(pasar.version_pasar) {
                        1-> {
                            val markerOptions = MarkerOptions()
                                    .position(pasar.location)
                                    .icon(VectorBitmapConvert.fromVector(this@MainActivity,
                                            R.drawable.marker_market))
                                    .title(pasar.nama_pasar)
                            val marker = map?.addMarker(markerOptions)
                            marker?.tag = pasar
                        }
                        2-> {
                            val markerOptions = MarkerOptions()
                                    .position(pasar.location)
                                    .icon(VectorBitmapConvert.fromVector(this@MainActivity,
                                            R.drawable.marker_verifikasi))
                                    .title(pasar.nama_pasar)
                            val markerAdmin = map?.addMarker(markerOptions)
                            markerAdmin?.tag = pasar
                        }
                    }
                }
            }
        }
    }


    override fun onMapClick(latLng: LatLng) {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            mainViewModel.pasar.set(null)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val selectedPasarAdmin = marker.tag as AddPasarGet?
        val status = mainViewModel.data.value
        val location = currentLocation
        if (location != null && selectedPasarAdmin != null) {
            mainViewModel.openSheet(location, selectedPasarAdmin)
//            mainViewModel.openSheetAdmin(location, selectedPasarAdmin)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            val blue = findViewById<Button1>(R.id.buttonEdit)
            val red = findViewById<Button1>(R.id.buttonRed)
            val yellow = findViewById<Button1>(R.id.place_button)
            val purple = findViewById<Button1>(R.id.lihatHargaPasar)
            when(selectedPasarAdmin.version_pasar){
                0 -> {
                    purple.isClickable=false
                    purple.visibility =View.INVISIBLE
                    yellow.isClickable=false
                    yellow.visibility =View.INVISIBLE
                    red.isClickable=false
                    red.visibility=View.INVISIBLE
                    blue.isClickable=true
                    blue.visibility=View.VISIBLE
                }
                1 -> {
                    purple.isClickable=true
                    purple.visibility =View.VISIBLE
                    yellow.isClickable=true
                    yellow.visibility =View.VISIBLE
                    red.isClickable=false
                    red.visibility=View.INVISIBLE
                    blue.isClickable=false
                    blue.visibility=View.INVISIBLE
                }
                2 -> {
                    purple.isClickable=true
                    purple.visibility =View.VISIBLE
                    yellow.isClickable=false
                    yellow.visibility =View.INVISIBLE
                    red.isClickable=true
                    red.visibility=View.VISIBLE
                    blue.isClickable=false
                    blue.visibility=View.INVISIBLE
                }
            }
            when(status){
                2 ->{
                    purple.isClickable=true
                    purple.visibility =View.VISIBLE
                    yellow.isClickable=false
                    yellow.visibility =View.INVISIBLE
                    red.isClickable=false
                    red.visibility=View.INVISIBLE
                    blue.isClickable=false
                    blue.visibility=View.INVISIBLE
                }
            }
        } else
            checkMyLocationLayer()
        return false
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        } else
            super.onBackPressed()
        finish()
    }

    companion object {
        private const val REQUEST_PERMISSION_LOCATION = 1
        private const val FIX_PLAY_SERVICES_REQUEST = 2
    }
}

