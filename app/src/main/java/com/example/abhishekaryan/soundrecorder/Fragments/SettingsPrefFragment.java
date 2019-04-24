package com.example.abhishekaryan.soundrecorder.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.abhishekaryan.soundrecorder.R;

public class SettingsPrefFragment extends PreferenceFragment {

    private static final String Sound_quality = "Sound_quality";


    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    public SettingsPrefFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefences);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                String value;


                if (key.equals(Sound_quality)) {

                    Preference quality = findPreference(key);
                    value = sharedPreferences.getString(key, "");
                    quality.setSummary(value);
                    Log.i("Abhishek","came to quality block - " );
                } else if (key.equals(getResources().getString(R.string.sound_encoding_key))) {


                    Log.i("Abhishek","came to encoding block - " );


                    Preference encoding = findPreference(key);
                    encoding.setSummary(sharedPreferences.getString(key, ""));

                }
                else if(key.equals(getResources().getString(R.string.file_encoding_key))){

                    Preference file = findPreference(key);
                    file.setSummary(sharedPreferences.getString(key, ""));

                    Log.i("Abhishek","came to file  block - " );

                }

            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        Preference quality = findPreference(Sound_quality);
        quality.setSummary(getPreferenceScreen().getSharedPreferences().getString(Sound_quality, ""));

        Preference encoding = findPreference(getResources().getString(R.string.sound_encoding_key));
        encoding.setSummary(getPreferenceScreen().getSharedPreferences()
                .getString(getResources().getString(R.string.sound_encoding_key), ""));

        Preference file = findPreference(getResources().getString(R.string.file_encoding_key));
        file.setSummary(getPreferenceScreen().getSharedPreferences()
                .getString(getResources().getString(R.string.file_encoding_key), ""));
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


}


