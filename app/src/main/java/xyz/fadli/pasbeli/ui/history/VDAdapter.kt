package xyz.fadli.pasbeli.ui.history


import android.view.*
import androidx.recyclerview.widget.RecyclerView
import xyz.fadli.pasbeli.databinding.ItemKomoditiBinding
import xyz.fadli.pasbeli.model.BarangHarga



/**
 * @author Mahendri Dwicahyo
 */

class VDAdapter internal constructor(
        private var data: List<BarangHarga>?

) : RecyclerView.Adapter<VDAdapter.HistoryHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemKomoditiBinding.inflate(inflater, parent, false)
        return HistoryHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        data?.let {
            val barang = it[position]
            holder.bind(barang)
        }
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    fun getIDHarga(position: Int): Int {
        return this.data!![position].id_entry
    }

    internal fun swapData(newData: List<BarangHarga>?) {
        if (newData != null) {
            data = newData

            notifyDataSetChanged()
        }
    }

    inner class HistoryHolder(private val binding: ItemKomoditiBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(barangHarga: BarangHarga) {
            binding.catatan = barangHarga
            binding.executePendingBindings()
        }
    }
}