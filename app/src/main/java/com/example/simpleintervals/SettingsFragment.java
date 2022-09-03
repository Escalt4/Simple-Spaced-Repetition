package com.example.simpleintervals;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public boolean onPreferenceTreeClick (Preference preference)
    {
        String key = preference.getKey();
        Log.d("SimpleIntervals", key);
//        if(key.equals("someKey")){
//            // do your work
//            return true;
//        }
        return false;
    }

}