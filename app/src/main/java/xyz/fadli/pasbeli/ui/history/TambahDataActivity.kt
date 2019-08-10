package xyz.fadli.pasbeli.ui.history

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.fadli.pasbeli.databinding.ActivityTambahDataBinding
import javax.inject.Inject
import dagger.android.AndroidInjection
import xyz.fadli.pasbeli.R
import xyz.fadli.pasbeli.ui.popup.PopupActivity
import xyz.fadli.pasbeli.ui.popup.PopupVerifikasiActivity

class TambahDataActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var vDVM: VDViewModel
    private val adapter = VDAdapter(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityTambahDataBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_tambah_data)
        vDVM = ViewModelProviders.of(this, viewModelFactory).get(VDViewModel::class.java)
        binding.vmtida = vDVM
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_baru_lagi)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val idPasar = intent.getIntExtra("idPasar",7)
        val idUser = intent.getIntExtra("id_user", 0)
        val Data1 = intent.getIntExtra("Data1", 1)
        println("kesel gua")
        println(Data1)
        when(intent.getIntExtra("main",1)){
            1->vDVM.getDataLayoutBaru().let { adapter.swapData(it) }
            2->vDVM.getPasarHarga(idPasar).let { adapter.swapData(it) }
            3-> {
                startActivity(Intent(this, PopupActivity::class.java))
                vDVM.getData().let { adapter.swapData(it) }
                subscrribeView()
                if (recyclerView != null) {
                    recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, RecyclerItemClickListener.OnItemClickListener { _, position ->
                        println("onItemLongClick")
                        println(adapter.itemCount)
                        val getData = adapter.getIDHarga(position)
                        println(getData)
                        startActivity(Intent(this, PopupVerifikasiActivity::class.java)
                                .putExtra("idData", getData))
                    }))
                } else {
                    println("salah isi")
                }
            }
            4->vDVM.getListHarga(Data1).let { adapter.swapData(it) }
        }
        subscrribeView()
    }
    private fun subscrribeView() {
        vDVM.verData.observe(this, Observer {
            if (it != null) {
                vDVM.verData.postValue(null)
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
