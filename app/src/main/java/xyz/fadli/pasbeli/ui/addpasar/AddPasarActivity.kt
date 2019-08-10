package xyz.fadli.pasbeli.ui.addpasar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationRequest
import xyz.fadli.pasbeli.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import xyz.fadli.pasbeli.databinding.ActivityAddPasarBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class AddPasarActivity: AppCompatActivity() {

    private lateinit var buttonChoose: Button
    private lateinit var bitmap: Bitmap
    private lateinit var decoded: Bitmap
    private var pickimage = 1
    private var bitmapsize = 60 // range 1 - 100
    private var kodeKamera = 99
    private lateinit var fileUri: Uri
    internal lateinit var intent: Intent
    private var photoFile = outputMediaFile
    private lateinit var imageView: ImageView

    private lateinit var addPasarViewModel: AddPasarViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var locationRequest: LocationRequest
    private lateinit var permissionDialog: AlertDialog

    //return Uri.fromFile(getOutputMediaFile());
    //(use your app signature + ".provider" )
    private val outputMediaFileUri: Uri
        get() = FileProvider.getUriForFile(
                this@AddPasarActivity,
                "xyz.fadli.pasbeli.provider",
                photoFile!!
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding : ActivityAddPasarBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_pasar)
        addPasarViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddPasarViewModel::class.java)
        imageView = findViewById(R.id.imageView)
        buttonChoose = findViewById(R.id.buttonChoose)
//        buttonSave = findViewById(R.id.button_tambah_pasar_baru);
        buttonChoose.setOnClickListener { selectImage() }

        val spinnerAddPasar = resources.getStringArray(R.array.spinner_add_pasar)
        binding.spinAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, spinnerAddPasar
        )
        addPasarViewModel.setLocationReq(locationRequest)
        binding.coba = addPasarViewModel

    }

    override fun onBackPressed() {
        if (permissionDialog.isShowing) {
            permissionDialog.dismiss()
            finish()
        } else
            super.onBackPressed()
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
            addPasarViewModel.startReqLocation(status)
        } else {
            // belum mendapat permission
            permissionDialog.show()
        }
    }

    private fun selectImage() {
        imageView.setImageResource(0)
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
        val builder = AlertDialog.Builder(this@AddPasarActivity)
        builder.setTitle("Add Photo!")
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Take Photo") {
                if (photoFile != null) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    // ini ada masalah biasanya diletak dibawah startActivityForResult tapi kalu begitu maka
                    // tidak ada thumbnailnya
                    fileUri = outputMediaFileUri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    if (takePictureIntent.resolveActivity(packageManager) != null) {
                        startActivityForResult(takePictureIntent, kodeKamera)
                    }
                }
            } else if (items[item] == "Choose from Library") {
                intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickimage)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // ini untuk nampilin thumbnail tapi ada masalah kalo ditimbulkan
        //        if(requestCode == kodeKamera && resultCode == RESULT_OK){
        //            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        //            imageView.setImageBitmap(bitmap);}
        if (requestCode == pickimage && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512))
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun setToImageView(bmp: Bitmap) {
        //compress image
        val bytes = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmapsize, bytes)
        decoded = BitmapFactory.decodeStream(ByteArrayInputStream(bytes.toByteArray()))
        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded)

    }

    // fungsi resize image
    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    companion object {
        val outputMediaFile: File?
            get() {
                val mediaStorageDir =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DeKa")
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e("Monitoring", "Oops! Failed create Monitoring directory")
                        return null
                    }
                }
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val mediaFile: File
                mediaFile = File(mediaStorageDir.path + File.separator + "IMG_DeKa_" + timeStamp + ".jpg")

                return mediaFile

            }
    }

    fun save(view:View) {
        view.animation
        val nam = addPasarViewModel.namaPasarBaru.value
        val kua = addPasarViewModel.alamatPasarBaru.value
        addPasarViewModel.save()

        if(nam != null && kua != null)
            showDialog()
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("BERHASIL")
        alertDialogBuilder
                .setMessage("Terima Kasih Sudah Berkontribusi")
                .setCancelable(false)
                .setPositiveButton("Kembali") { _, _ ->
                    this@AddPasarActivity.finish()
                }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


}
