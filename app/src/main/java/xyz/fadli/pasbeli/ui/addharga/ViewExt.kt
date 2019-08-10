package xyz.fadli.pasbeli.ui.addharga

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import xyz.fadli.pasbeli.R
import xyz.fadli.pasbeli.model.KualitasUnit
import timber.log.Timber
import xyz.fadli.pasbeli.model.BarangNamaDISTINCTGet


/**
 * @author Mahendri
 * extension dari view pada android
 */


//@BindingAdapter("app:list")
//fun setList(textView: AutoCompleteTextView, list: List<BarangNamaDISTINCTGet>) {
//
////    val resource1 = list
////    val resource = list
//
//    val isEnable = list.isNotEmpty()
//    textView.apply {
////        textView.setAdapter<ArrayAdapter<String>>(
////            ArrayAdapter(textView.context, android.R.layout.simple_dropdown_item_1line, this)
////        )
////        textView.validator = AutoValidator(this)
//        isEnabled = isEnable
//        textView.setAdapter( if (isEnable) BarangAdapter(textView.context,
//                android.R.layout.simple_dropdown_item_1line, list) else null)
//        textView.setOnFocusChangeListener { view, focus ->
//            if (!focus && view.id == textView.id) textView.performValidation()
//        }
//
//    }
//
//}

@BindingAdapter("app:list")
fun setList(spinner: Spinner, data: List<BarangNamaDISTINCTGet>) {
    val res = data
    val isEnable = res.isNotEmpty()
    Timber.i("kondisi list spinner: %s", isEnable)
    spinner.apply {
        isEnabled = isEnable
        adapter = if (isEnable) BarangAdapter(spinner.context, R.layout.item_spinner, res) else null
    }
}

@BindingAdapter("entry")
fun setEntries(spinner: Spinner, data: LiveData<List<KualitasUnit>>) {
    val res = data.value
    val isEnable = res != null && res.isNotEmpty()
    Timber.i("kondisi list spinner: %s", isEnable)
    spinner.apply {
        isEnabled = isEnable
        adapter = if (isEnable) KualitasAdapter(spinner.context, R.layout.item_spinner, res) else null
    }
}
