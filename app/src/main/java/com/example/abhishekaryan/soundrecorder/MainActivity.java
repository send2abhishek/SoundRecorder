package com.example.abhishekaryan.soundrecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abhishekaryan.soundrecorder.Activities.SettingsActivity;
import com.example.abhishekaryan.soundrecorder.Fragments.SoundRecorderFilesFragment;
import com.example.abhishekaryan.soundrecorder.Fragments.SoundRecorderFragment;
import com.example.abhishekaryan.soundrecorder.ViewHolder.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions();

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.activity_main_tabLaout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new SoundRecorderFragment(), "Record Sound");
        viewPagerAdapter.AddFragment(new SoundRecorderFilesFragment(), "Recorded Files");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sound_recorder_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id=item.getItemId();

        switch (id){

            case R.id.sound_recorder_menu_settings:

                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;


            case R.id.sound_recorder_menu_about:

                callAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callAbout() {

        AlertDialog.Builder dialog=new AlertDialog.Builder(this,R.style.MyDeleteDialogTheme);

        dialog.setMessage("This app is developed by Abhishek Aryan \n" +
                " Android Developer \n Attra Infotech \n" +
                "Contact - send2abhishek@live.com ");
        dialog.show();
    }


    private void permissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Explain to the user why we need to read the contacts

                Toast.makeText(this, "Record Audio permission need to give man at any cost", Toast.LENGTH_SHORT).show();
            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


            }
        } else {
            //Toast.makeText(this,"Record Audio permission alredy granted",Toast.LENGTH_SHORT).show();
        }

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(this, "Record Audio permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



}
