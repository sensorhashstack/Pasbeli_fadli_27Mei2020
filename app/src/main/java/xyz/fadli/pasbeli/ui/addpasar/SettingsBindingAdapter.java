package xyz.fadli.pasbeli.ui.addpasar;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class SettingsBindingAdapter {
    @BindingAdapter(value = {"bind:pmtOpt",
            "bind:pmtOptAttrChanged"}, requireAll = false)
    public static void setPmtOpt(final AppCompatSpinner spinner,
                                 final String selectedPmtOpt,
                                 final InverseBindingListener changeListener) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeListener.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                changeListener.onChange();
            }
        });

        spinner.setSelection(getIndexOfItem(spinner, selectedPmtOpt));

    }
    @InverseBindingAdapter(attribute = "bind:pmtOpt",
            event = "bind:pmtOptAttrChanged")
    public static String getPmtOpt(final AppCompatSpinner spinner) {
        return (String)spinner.getSelectedItem();
    }
    private static int getIndexOfItem(AppCompatSpinner spinner, String item){
        Adapter a = spinner.getAdapter();
        for(int i=0; i<a.getCount(); i++){
            if((a.getItem(i)).equals(item)){
                return i;
            }
        }
        return 0;
    }
}