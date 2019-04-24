package com.example.abhishekaryan.soundrecorder.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.abhishekaryan.soundrecorder.Fragments.SettingsPrefFragment;
import com.example.abhishekaryan.soundrecorder.Fragments.SoundRecorderFragment;
import com.example.abhishekaryan.soundrecorder.R;



public class SettingsActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        frameLayout=(FrameLayout)findViewById(R.id.settings_fragment_container);
        if(frameLayout!=null){


            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.add(R.id.settings_fragment_container,new SettingsPrefFragment());
            transaction.commit();
        }


    }


}
